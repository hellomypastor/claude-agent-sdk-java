package com.anthropic.claude.sdk.examples;

import com.anthropic.claude.sdk.client.ClaudeSDKClient;
import com.anthropic.claude.sdk.types.content.TextBlock;
import com.anthropic.claude.sdk.types.hooks.HookCallback;
import com.anthropic.claude.sdk.types.hooks.HookCallbackMatcher;
import com.anthropic.claude.sdk.types.hooks.HookOutput;
import com.anthropic.claude.sdk.types.hooks.PreToolUseHookInput;
import com.anthropic.claude.sdk.types.hooks.PostToolUseHookInput;
import com.anthropic.claude.sdk.types.messages.AssistantMessage;
import com.anthropic.claude.sdk.types.messages.Message;
import com.anthropic.claude.sdk.types.messages.ResultMessage;
import com.anthropic.claude.sdk.types.options.ClaudeAgentOptions;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * Hooks Example - Demonstrates using hooks with Claude Agent SDK.
 * <p>
 * This example shows various hook patterns:
 * - PreToolUse: Block dangerous commands
 * - PostToolUse: Review tool output
 * - UserPromptSubmit: Add custom context
 * <p>
 * Usage:
 * java HooksExample
 */
public class HooksExample {

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("Claude Agent SDK - Hooks Examples");
        System.out.println("=".repeat(60));

        try {
            preToolUseExample();
            postToolUseExample();
            strictApprovalExample();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Example 1: PreToolUse Hook - Block dangerous Bash commands
     */
    static void preToolUseExample() {
        System.out.println("=== PreToolUse Example ===");
        System.out.println("This example blocks specific bash commands using PreToolUse hook.");

        // Define hook to check bash commands
        HookCallback checkBashCommand = (input, toolUseId, context) -> {
            if (input instanceof PreToolUseHookInput preToolInput) {
                String toolName = preToolInput.toolName();

                if (!"Bash".equals(toolName)) {
                    return CompletableFuture.completedFuture(
                            HookOutput.SyncHookOutput.builder().build());
                }

                @SuppressWarnings("unchecked")
                Map<String, Object> toolInput = (Map<String, Object>) preToolInput.toolInput();
                String command = toolInput != null ? (String) toolInput.get("command") : null;
                String[] blockPatterns = {"foo.sh", "rm -rf", "sudo"};

                for (String pattern : blockPatterns) {
                    if (command != null && command.contains(pattern)) {
                        System.out.println("WARNING: Blocked command: " + command);

                        return CompletableFuture.completedFuture(
                                HookOutput.SyncHookOutput.builder()
                                        .decision("deny")
                                        .reason("Command contains forbidden pattern: " + pattern)
                                        .hookSpecificOutput(Map.of(
                                                "hookEventName", "PreToolUse",
                                                "permissionDecision", "deny",
                                                "permissionDecisionReason",
                                                "Command contains forbidden pattern: " + pattern
                                        ))
                                        .build());
                    }
                }
            }
            return CompletableFuture.completedFuture(
                    HookOutput.SyncHookOutput.builder().build());
        };

        // Configure options with hook
        ClaudeAgentOptions options = ClaudeAgentOptions.builder()
                .allowedTools(List.of("Bash"))
                .hooks(Map.of(
                        "PreToolUse", List.of(
                                new HookCallbackMatcher("Bash", List.of(checkBashCommand), null)
                        )
                ))
                .build();

        try (ClaudeSDKClient client = new ClaudeSDKClient(options)) {
            client.connect().join();

            // Test 1: Blocked command
            System.out.println("Test 1: Trying a blocked command (./foo.sh)...");
            client.query("Run the bash command: ./foo.sh --help").join();
            consumeUntilResult(client);

            System.out.println("=".repeat(50));

            // Test 2: Allowed command
            System.out.println("Test 2: Trying an allowed command (echo)...");
            client.query("Run the bash command: echo 'Hello from hooks!'").join();
            consumeUntilResult(client);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Example 2: PostToolUse Hook - Review tool output
     */
    static void postToolUseExample() {
        System.out.println("=== PostToolUse Example ===");
        System.out.println("This example reviews tool output and adds context.");

        HookCallback reviewToolOutput = (input, toolUseId, context) -> {
            if (input instanceof PostToolUseHookInput postToolInput) {
                Object toolResponse = postToolInput.toolResponse();

                // Check if output contains an error
                if (toolResponse != null && toolResponse.toString().toLowerCase().contains("error")) {
                    System.out.println("WARNING: Tool execution produced an error");

                    return CompletableFuture.completedFuture(
                            HookOutput.SyncHookOutput.builder()
                                    .systemMessage("The command produced an error")
                                    .reason("Tool execution failed")
                                    .hookSpecificOutput(Map.of(
                                            "hookEventName", "PostToolUse",
                                            "additionalContext",
                                            "The command encountered an error. Try a different approach."
                                    ))
                                    .build());
                }
            }

            return CompletableFuture.completedFuture(
                    HookOutput.SyncHookOutput.builder().build());
        };

        ClaudeAgentOptions options = ClaudeAgentOptions.builder()
                .allowedTools(List.of("Bash"))
                .hooks(Map.of(
                        "PostToolUse", List.of(
                                new HookCallbackMatcher("Bash", List.of(reviewToolOutput), null)
                        )
                ))
                .build();

        try (ClaudeSDKClient client = new ClaudeSDKClient(options)) {
            client.connect().join();
            System.out.println("Running command that will produce an error...");
            client.query("Run this command: ls /nonexistent_directory").join();
            consumeUntilResult(client);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Example 3: Strict Approval Hook - Control Write operations
     */
    static void strictApprovalExample() {
        System.out.println("=== Strict Approval Example ===");
        System.out.println("This example blocks writes to files containing 'important'.");

        HookCallback strictApproval = (input, toolUseId, context) -> {
            if (input instanceof PreToolUseHookInput preToolInput) {
                String toolName = preToolInput.toolName();

                if ("Write".equals(toolName)) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> toolInput = (Map<String, Object>) preToolInput.toolInput();
                    String filePath = toolInput != null ? (String) toolInput.get("file_path") : null;

                    if (filePath != null && filePath.toLowerCase().contains("important")) {
                        System.out.println("BLOCKED: Write to: " + filePath);

                        return CompletableFuture.completedFuture(
                                HookOutput.SyncHookOutput.builder()
                                        .decision("deny")
                                        .reason("Security policy blocks writes to important files")
                                        .systemMessage("Write operation blocked")
                                        .hookSpecificOutput(Map.of(
                                                "hookEventName", "PreToolUse",
                                                "permissionDecision", "deny",
                                                "permissionDecisionReason",
                                                "Files with 'important' in name are protected"
                                        ))
                                        .build());
                    }

                    // Allow other writes
                    return CompletableFuture.completedFuture(
                            HookOutput.SyncHookOutput.builder()
                                    .decision("allow")
                                    .reason("Security check passed")
                                    .hookSpecificOutput(Map.of(
                                            "hookEventName", "PreToolUse",
                                            "permissionDecision", "allow",
                                            "permissionDecisionReason", "Security check passed"
                                    ))
                                    .build());
                }
            }

            return CompletableFuture.completedFuture(
                    HookOutput.SyncHookOutput.builder().build());
        };

        ClaudeAgentOptions options = ClaudeAgentOptions.builder()
                .allowedTools(List.of("Write", "Bash"))
                .hooks(Map.of(
                        "PreToolUse", List.of(
                                new HookCallbackMatcher("Write", List.of(strictApproval), null)
                        )
                ))
                .build();

        try (ClaudeSDKClient client = new ClaudeSDKClient(options)) {
            client.connect().join();

            // Test 1: Blocked write
            System.out.println("Test 1: Trying to write to important_config.txt (blocked)...");
            client.query("Write 'test' to important_config.txt").join();
            consumeUntilResult(client);
            System.out.println("=".repeat(50));

            // Test 2: Allowed write
            System.out.println("Test 2: Trying to write to regular_file.txt (allowed)...");
            client.query("Write 'test' to regular_file.txt").join();
            consumeUntilResult(client);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Helper method to consume messages until result
     */
    static void consumeUntilResult(ClaudeSDKClient client) {
        Stream<Message> stream = client.receiveMessages();
        try {
            Iterator<Message> iterator = stream.iterator();
            while (iterator.hasNext()) {
                Message msg = iterator.next();
                displayMessage(msg);
                if (msg instanceof ResultMessage) {
                    break;
                }
            }
        } finally {
            stream.close();
        }
    }

    /**
     * Helper method to display messages
     */
    static void displayMessage(Message msg) {
        if (msg instanceof AssistantMessage assistantMsg) {
            assistantMsg.content().forEach(block -> {
                if (block instanceof TextBlock textBlock) {
                    System.out.println("Claude: " + textBlock.text());
                }
            });
        } else if (msg instanceof ResultMessage) {
            System.out.println("Result ended");
        }
    }
}
