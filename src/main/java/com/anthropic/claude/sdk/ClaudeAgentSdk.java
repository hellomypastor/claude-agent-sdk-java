package com.anthropic.claude.sdk;

import com.anthropic.claude.sdk.client.ClaudeClient;
import com.anthropic.claude.sdk.types.messages.Message;
import com.anthropic.claude.sdk.types.options.ClaudeAgentOptions;

import java.util.stream.Stream;

/**
 * Main entry point for Claude Agent SDK.
 * <p>
 * Provides static methods for simple one-shot queries to Claude Code.
 * <p>
 * Example:
 * <pre>{@code
 * // Simple query
 * Stream<Message> messages = ClaudeAgentSdk.query("What is 2 + 2?");
 * messages.forEach(System.out::println);
 *
 * // Query with custom options
 * ClaudeAgentOptions options = ClaudeAgentOptions.builder()
 *     .allowedTools("Read", "Write")
 *     .maxTurns(5)
 *     .build();
 *
 * Stream<Message> messages = ClaudeAgentSdk.query("Analyze this codebase", options);
 * messages.forEach(System.out::println);
 * }</pre>
 */
public class ClaudeAgentSdk {

    /**
     * Query Claude with a simple prompt.
     * Uses default options.
     *
     * @param prompt The prompt to send to Claude
     * @return Stream of messages from Claude
     */
    public static Stream<Message> query(String prompt) {
        return query(prompt, ClaudeAgentOptions.builder().build());
    }

    /**
     * Query Claude with a prompt and custom options.
     *
     * @param prompt  The prompt to send to Claude
     * @param options Custom options for the query
     * @return Stream of messages from Claude
     */
    public static Stream<Message> query(String prompt, ClaudeAgentOptions options) {
        ClaudeClient client = new ClaudeClient(options);

        try {
            return client.query(prompt).join();
        } finally {
            // Register shutdown hook to cleanup
            Runtime.getRuntime().addShutdownHook(new Thread(client::close));
        }
    }

    /**
     * Get SDK version.
     */
    public static String getVersion() {
        return "0.1.0";
    }
}
