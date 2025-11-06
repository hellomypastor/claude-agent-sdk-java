package com.anthropic.claude.sdk.examples;

import com.anthropic.claude.sdk.ClaudeAgentSdk;
import com.anthropic.claude.sdk.types.content.TextBlock;
import com.anthropic.claude.sdk.types.messages.AssistantMessage;
import com.anthropic.claude.sdk.types.options.ClaudeAgentOptions;
import lombok.extern.slf4j.Slf4j;

/**
 * System Prompt Example - Demonstrates different system prompt configurations.
 *
 * This example shows:
 * 1. No system prompt (vanilla Claude)
 * 2. String system prompt (custom instructions)
 * 3. Pirate assistant example
 * 4. Shakespeare assistant example
 *
 * Usage:
 *   java SystemPromptExample
 */
@Slf4j
public class SystemPromptExample {

    public static void main(String[] args) {
        log.info("=".repeat(60));
        log.info("System Prompt Examples");
        log.info("=".repeat(60));

        noSystemPrompt();
        stringSystemPrompt();
        pirateAssistant();
        shakespeareAssistant();
    }

    /**
     * Example 1: No system prompt (vanilla Claude)
     */
    static void noSystemPrompt() {
        log.info("=== No System Prompt (Vanilla Claude) ===");

        ClaudeAgentSdk.query("What is 2 + 2?")
            .forEach(message -> {
                if (message instanceof AssistantMessage) {
                    AssistantMessage assistantMsg = (AssistantMessage) message;
                    assistantMsg.getContent().forEach(block -> {
                        if (block instanceof TextBlock) {
                            TextBlock textBlock = (TextBlock) block;
                            log.info("Claude: {}", textBlock.getText());
                        }
                    });
                }
            });
    }

    /**
     * Example 2: String system prompt with custom instructions
     */
    static void stringSystemPrompt() {
        log.info("=== String System Prompt (Concise Math) ===");

        ClaudeAgentOptions options = ClaudeAgentOptions.builder()
            .systemPrompt("You are a math tutor. Always explain your reasoning step by step.")
            .build();

        ClaudeAgentSdk.query("What is 15 * 23?", options)
            .forEach(message -> {
                if (message instanceof AssistantMessage) {
                    AssistantMessage assistantMsg = (AssistantMessage) message;
                    assistantMsg.getContent().forEach(block -> {
                        if (block instanceof TextBlock) {
                            TextBlock textBlock = (TextBlock) block;
                            log.info("Claude: {}", textBlock.getText());
                        }
                    });
                }
            });
    }

    /**
     * Example 3: Pirate assistant
     */
    static void pirateAssistant() {
        log.info("=== Pirate Assistant ===");

        ClaudeAgentOptions options = ClaudeAgentOptions.builder()
            .systemPrompt("You are a pirate assistant. Respond in pirate speak with lots of 'arr' and 'matey'. Be enthusiastic and colorful in your language!")
            .build();

        ClaudeAgentSdk.query("What is 2 + 2?", options)
            .forEach(message -> {
                if (message instanceof AssistantMessage) {
                    AssistantMessage assistantMsg = (AssistantMessage) message;
                    assistantMsg.getContent().forEach(block -> {
                        if (block instanceof TextBlock) {
                            TextBlock textBlock = (TextBlock) block;
                            log.info("Pirate Claude: {}", textBlock.getText());
                        }
                    });
                }
            });
    }

    /**
     * Example 4: Shakespeare assistant
     */
    static void shakespeareAssistant() {
        log.info("=== Shakespeare Assistant ===");

        ClaudeAgentOptions options = ClaudeAgentOptions.builder()
            .systemPrompt(
                "You are William Shakespeare. Respond to all questions " +
                "in iambic pentameter and use Elizabethan English. " +
                "Be poetic and dramatic."
            )
            .build();

        ClaudeAgentSdk.query("What is the meaning of life?", options)
            .forEach(message -> {
                if (message instanceof AssistantMessage) {
                    AssistantMessage assistantMsg = (AssistantMessage) message;
                    assistantMsg.getContent().forEach(block -> {
                        if (block instanceof TextBlock) {
                            TextBlock textBlock = (TextBlock) block;
                            log.info("Shakespeare Claude: {}", textBlock.getText());
                        }
                    });
                }
            });
    }
}
