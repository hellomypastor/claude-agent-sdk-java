package com.anthropic.claude.sdk.examples;

import com.anthropic.claude.sdk.client.ClaudeSDKClient;
import com.anthropic.claude.sdk.types.messages.AssistantMessage;
import com.anthropic.claude.sdk.types.messages.Message;
import com.anthropic.claude.sdk.types.messages.ResultMessage;
import com.anthropic.claude.sdk.types.options.ClaudeAgentOptions;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * Streaming mode example similar to Python's streaming_mode.py.
 * <p>
 * Demonstrates:
 * 1. Connecting without an initial prompt
 * 2. Streaming user events (string and dict formats)
 * 3. Interrupting mid-conversation
 * 4. Consuming messages lazily from Claude
 */
public class StreamingModeExample {

    public static void main(String[] args) throws Exception {
        System.out.println("=".repeat(60));
        System.out.println("Streaming Mode Example");
        System.out.println("=".repeat(60));

        ClaudeAgentOptions options = ClaudeAgentOptions.builder()
                .allowedTools(List.of("Read"))
                .includePartialMessages(true)
                .build();

        try (ClaudeSDKClient client = new ClaudeSDKClient(options)) {
            client.connect().join();

            // Simple string prompt
            System.out.println("Sending initial prompt...");
            client.query("Describe the Claude Agent SDK in two sentences.").join();
            drainStream(client);

            // Structured events
            System.out.println("Sending structured events...");
            client.query(structuredConversation()).join();
            drainStream(client);

            // Interrupt demo
            CompletableFuture<Void> streaming = client.query(longRunningPrompt());
            Thread.sleep(1000);
            System.out.println("Interrupting conversation...");
            client.interrupt().join();
            streaming.join();
            drainStream(client);
        }
    }

    private static Iterable<Map<String, Object>> structuredConversation() {
        Map<String, Object> first = new LinkedHashMap<>();
        first.put("type", "user");
        first.put("message", Map.of("role", "user", "content", "List three JVM languages."));
        first.put("session_id", "stream-demo");

        Map<String, Object> second = new LinkedHashMap<>();
        second.put("type", "user");
        second.put("message", Map.of("role", "user", "content", "Now explain why Java 17 is useful."));
        second.put("session_id", "stream-demo");

        return List.of(first, second);
    }

    private static Iterable<Map<String, Object>> longRunningPrompt() {
        Map<String, Object> prompt = new LinkedHashMap<>();
        prompt.put("type", "user");
        prompt.put("message", Map.of("role", "user", "content", "Write a 500-line story about streaming mode."));
        prompt.put("session_id", "interrupt-demo");
        return List.of(prompt);
    }

    private static void drainStream(ClaudeSDKClient client) {
        try (Stream<Message> stream = client.receiveMessages()) {
            Iterator<Message> iterator = stream.iterator();
            while (iterator.hasNext()) {
                Message msg = iterator.next();
                if (msg instanceof AssistantMessage) {
                    System.out.println("Assistant chunk received");
                } else if (msg instanceof ResultMessage result) {
                    System.out.println("Session " + result.getSessionId()
                            + " finished with subtype " + result.getSubtype());
                    break;
                }
            }
        }
    }
}
