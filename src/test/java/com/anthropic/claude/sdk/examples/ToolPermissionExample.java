package com.anthropic.claude.sdk.examples;

import com.anthropic.claude.sdk.client.ClaudeSDKClient;
import com.anthropic.claude.sdk.types.content.TextBlock;
import com.anthropic.claude.sdk.types.messages.AssistantMessage;
import com.anthropic.claude.sdk.types.messages.Message;
import com.anthropic.claude.sdk.types.messages.ResultMessage;
import com.anthropic.claude.sdk.types.options.ClaudeAgentOptions;
import com.anthropic.claude.sdk.types.permissions.PermissionResult;
import com.anthropic.claude.sdk.types.permissions.ToolPermissionCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * Tool Permission Callback Example.
 * <p>
 * Demonstrates how to use tool permission callbacks to:
 * 1. Allow/deny tools based on type
 * 2. Modify tool inputs for safety
 * 3. Log tool usage
 * 4. Block dangerous commands
 * <p>
 * Usage:
 * java ToolPermissionExample
 */
public class ToolPermissionExample {

    // Track tool usage for demonstration
    static List<Map<String, Object>> toolUsageLog = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("Tool Permission Callback Example");
        System.out.println("=".repeat(60));
        System.out.println("This example demonstrates how to:");
        System.out.println("1. Allow/deny tools based on type");
        System.out.println("2. Modify tool inputs for safety");
        System.out.println("3. Log tool usage");
        System.out.println("4. Block dangerous commands");
        System.out.println("=".repeat(60));

        runExample();
    }

    static void runExample() {
        // Define our permission callback
        ToolPermissionCallback permissionCallback = (toolName, toolInput, context) -> {
            // Log the tool request
            Map<String, Object> logEntry = new HashMap<>();
            logEntry.put("tool", toolName);
            logEntry.put("input", toolInput);
            logEntry.put("suggestions", context.suggestions());
            toolUsageLog.add(logEntry);

            // Always allow read operations
            if (List.of("Read", "Glob", "Grep").contains(toolName)) {
                return CompletableFuture.completedFuture(PermissionResult.allow());
            }

            // Deny write operations to system directories
            if (List.of("Write", "Edit", "MultiEdit").contains(toolName)) {
                String filePath = (String) toolInput.get("file_path");

                if (filePath != null) {
                    if (filePath.startsWith("/etc/") || filePath.startsWith("/usr/")) {
                        System.out.println("   DENIED: write to system directory: " + filePath);
                        return CompletableFuture.completedFuture(
                                PermissionResult.deny("Cannot write to system directory: " + filePath)
                        );
                    }

                    // Redirect writes to a safe directory
                    if (!filePath.startsWith("/tmp/") && !filePath.startsWith("./")) {
                        String fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
                        String safePath = "./safe_output/" + fileName;
                        System.out.println("   Redirecting write from " + filePath + " to " + safePath);

                        Map<String, Object> modifiedInput = new HashMap<>(toolInput);
                        modifiedInput.put("file_path", safePath);

                        return CompletableFuture.completedFuture(
                                PermissionResult.allow(modifiedInput)
                        );
                    }
                }
            }

            // Check dangerous bash commands
            if ("Bash".equals(toolName)) {
                String command = (String) toolInput.get("command");
                String[] dangerousCommands = {"rm -rf", "sudo", "chmod 777", "dd if=", "mkfs"};

                for (String dangerous : dangerousCommands) {
                    if (command != null && command.contains(dangerous)) {
                        System.out.println("   DENIED: dangerous command: " + command);
                        return CompletableFuture.completedFuture(
                                PermissionResult.deny("Dangerous command pattern detected: " + dangerous)
                        );
                    }
                }

                return CompletableFuture.completedFuture(PermissionResult.allow());
            }

            // For other tools, allow by default
            return CompletableFuture.completedFuture(PermissionResult.allow());
        };

        // Configure options with our callback
        ClaudeAgentOptions options = ClaudeAgentOptions.builder()
                .canUseTool(permissionCallback)
                .build();

        // Create streaming client and send a query
        try (ClaudeSDKClient client = new ClaudeSDKClient(options)) {
            client.connect().join();
            System.out.println("Sending query to Claude...");

            client.query(
                    "Please do the following:\n" +
                            "1. List the files in the current directory\n" +
                            "2. Create a simple Python hello world script at hello.py\n" +
                            "3. Run the script to test it"
            ).join();

            System.out.println("Receiving response...");
            streamUntilResult(client);

            // Print tool usage summary
            System.out.println("=".repeat(60));
            System.out.println("Tool Usage Summary");
            System.out.println("=".repeat(60));

            for (int i = 0; i < toolUsageLog.size(); i++) {
                Map<String, Object> usage = toolUsageLog.get(i);
                System.out.println((i + 1) + ". Tool: " + usage.get("tool"));
                System.out.println("   Input: " + usage.get("input"));
                if (usage.get("suggestions") != null) {
                    System.out.println("   Suggestions: " + usage.get("suggestions"));
                }
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void streamUntilResult(ClaudeSDKClient client) {
        Stream<Message> stream = client.receiveMessages();
        try {
            int messageCount = 0;
            var iterator = stream.iterator();
            while (iterator.hasNext()) {
                Message message = iterator.next();
                messageCount++;

                if (message instanceof AssistantMessage assistantMsg) {
                    assistantMsg.content().forEach(block -> {
                        if (block instanceof TextBlock textBlock) {
                            System.out.println("Claude: " + textBlock.text());
                        }
                    });
                } else if (message instanceof ResultMessage resultMsg) {
                    System.out.println("Task completed!");
                    System.out.println("   Duration: " + resultMsg.getDurationMs() + "ms");
                    System.out.println("   Cost: $" + String.format("%.4f", resultMsg.getTotalCostUsd()));
                    System.out.println("   Messages processed: " + messageCount);
                    break;
                }
            }
        } finally {
            stream.close();
        }
    }
}
