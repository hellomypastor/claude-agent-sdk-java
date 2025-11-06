package com.anthropic.claude.examples;

import com.anthropic.claude.sdk.ClaudeAgent;
import com.anthropic.claude.sdk.client.ClaudeSDKClient;
import com.anthropic.claude.sdk.errors.ClaudeSDKException;
import com.anthropic.claude.sdk.types.*;

import java.util.Iterator;

/**
 * Example demonstrating bidirectional communication with ClaudeSDKClient.
 */
public class ClientExample {

    public static void main(String[] args) {
        ClaudeAgentOptions options = ClaudeAgentOptions.builder()
                .systemPrompt("You are a helpful coding assistant.")
                .build();

        try (ClaudeSDKClient client = ClaudeAgent.createClient(options)) {
            // Connect and send initial prompt
            client.connect("Hello! Can you help me with Java programming?");

            // Receive messages
            Iterator<Message> messages = client.receiveMessages();
            boolean firstResponse = true;

            while (messages.hasNext()) {
                Message message = messages.next();

                if (message instanceof AssistantMessage) {
                    AssistantMessage assistantMsg = (AssistantMessage) message;
                    for (ContentBlock block : assistantMsg.getContent()) {
                        if (block instanceof TextBlock) {
                            System.out.println("Claude: " + ((TextBlock) block).getText());
                        }
                    }

                    // Send follow-up query after first response
                    if (firstResponse) {
                        firstResponse = false;
                        client.query("What are some best practices for exception handling?", "default");
                    }
                } else if (message instanceof ResultMessage) {
                    System.out.println("Query completed.");
                    break;
                }
            }

        } catch (ClaudeSDKException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
