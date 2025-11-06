package com.anthropic.claude.examples;

import com.anthropic.claude.sdk.ClaudeAgent;
import com.anthropic.claude.sdk.errors.ClaudeSDKException;
import com.anthropic.claude.sdk.types.*;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Example demonstrating query with configuration options.
 */
public class ConfiguredQueryExample {

    public static void main(String[] args) {
        try {
            // Create options
            ClaudeAgentOptions options = ClaudeAgentOptions.builder()
                    .systemPrompt("You are a helpful assistant specialized in mathematics.")
                    .maxTurns(1)
                    .allowedTools(Arrays.asList("Read", "Write"))
                    .permissionMode(PermissionMode.ACCEPT_EDITS)
                    .build();

            // Query with options
            Iterator<Message> messages = ClaudeAgent.query(
                    "Calculate the factorial of 5",
                    options
            );

            // Process messages
            while (messages.hasNext()) {
                Message message = messages.next();
                System.out.println("Received: " + message.getClass().getSimpleName());

                if (message instanceof AssistantMessage) {
                    AssistantMessage assistantMsg = (AssistantMessage) message;
                    for (ContentBlock block : assistantMsg.getContent()) {
                        if (block instanceof TextBlock) {
                            System.out.println("Text: " + ((TextBlock) block).getText());
                        }
                    }
                } else if (message instanceof ResultMessage) {
                    ResultMessage resultMsg = (ResultMessage) message;
                    System.out.println("Query completed in " + resultMsg.getDurationMs() + "ms");
                    System.out.println("Total turns: " + resultMsg.getNumTurns());
                }
            }

        } catch (ClaudeSDKException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
