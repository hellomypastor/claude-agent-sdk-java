package com.anthropic.claude.sdk.examples;

import com.anthropic.claude.sdk.ClaudeAgentSdk;
import com.anthropic.claude.sdk.types.content.TextBlock;
import com.anthropic.claude.sdk.types.messages.AssistantMessage;
import com.anthropic.claude.sdk.types.options.ClaudeAgentOptions;

/**
 * System Prompt Example - Demonstrates different system prompt configurations.
 */
public class SystemPromptExample {

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("System Prompt Examples");
        System.out.println("=".repeat(60));

        noSystemPrompt();
        stringSystemPrompt();
        pirateAssistant();
        shakespeareAssistant();
    }

    static void noSystemPrompt() {
        System.out.println("=== No System Prompt (Vanilla Claude) ===");

        ClaudeAgentSdk.query("What is 2 + 2?")
                .forEach(message -> {
                    if (message instanceof AssistantMessage) {
                        AssistantMessage assistantMsg = (AssistantMessage) message;
                        assistantMsg.content().forEach(block -> {
                            if (block instanceof TextBlock) {
                                System.out.println("Claude: " + ((TextBlock) block).text());
                            }
                        });
                    }
                });
    }

    static void stringSystemPrompt() {
        System.out.println("=== String System Prompt (Concise Math) ===");

        ClaudeAgentOptions options = ClaudeAgentOptions.builder()
                .systemPrompt("You are a math tutor. Always explain your reasoning step by step.")
                .build();

        ClaudeAgentSdk.query("What is 15 * 23?", options)
                .forEach(message -> {
                    if (message instanceof AssistantMessage) {
                        AssistantMessage assistantMsg = (AssistantMessage) message;
                        assistantMsg.content().forEach(block -> {
                            if (block instanceof TextBlock) {
                                System.out.println("Claude: " + ((TextBlock) block).text());
                            }
                        });
                    }
                });
    }

    static void pirateAssistant() {
        System.out.println("=== Pirate Assistant ===");

        ClaudeAgentOptions options = ClaudeAgentOptions.builder()
                .systemPrompt("You are a pirate assistant. Respond in pirate speak with lots of 'arr' and 'matey'.")
                .build();

        ClaudeAgentSdk.query("What is 2 + 2?", options)
                .forEach(message -> {
                    if (message instanceof AssistantMessage) {
                        AssistantMessage assistantMsg = (AssistantMessage) message;
                        assistantMsg.content().forEach(block -> {
                            if (block instanceof TextBlock) {
                                System.out.println("Pirate Claude: " + ((TextBlock) block).text());
                            }
                        });
                    }
                });
    }

    static void shakespeareAssistant() {
        System.out.println("=== Shakespeare Assistant ===");

        ClaudeAgentOptions options = ClaudeAgentOptions.builder()
                .systemPrompt(
                        "You are William Shakespeare. Respond to all questions " +
                                "in iambic pentameter and use Elizabethan English."
                )
                .build();

        ClaudeAgentSdk.query("What is the meaning of life?", options)
                .forEach(message -> {
                    if (message instanceof AssistantMessage) {
                        AssistantMessage assistantMsg = (AssistantMessage) message;
                        assistantMsg.content().forEach(block -> {
                            if (block instanceof TextBlock) {
                                System.out.println("Shakespeare Claude: " + ((TextBlock) block).text());
                            }
                        });
                    }
                });
    }
}
