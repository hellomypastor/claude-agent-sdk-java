package com.anthropic.claude.sdk.examples;

import com.anthropic.claude.sdk.ClaudeAgentSdk;
import com.anthropic.claude.sdk.types.content.TextBlock;
import com.anthropic.claude.sdk.types.messages.AssistantMessage;
import com.anthropic.claude.sdk.types.messages.ResultMessage;
import com.anthropic.claude.sdk.types.options.ClaudeAgentOptions;
import lombok.extern.slf4j.Slf4j;

/**
 * Quick start examples for Claude Agent SDK for Java.
 * <p>
 * This file mirrors the Python SDK's quick_start.py example, demonstrating:
 * 1. Basic query - simple question
 * 2. Query with options - custom system prompt and max turns
 * 3. Query with tools - file operations
 * <p>
 * Usage:
 * java QuickStart
 */
@Slf4j
public class QuickStart {

    public static void main(String[] args) {
        log.info("Claude Agent SDK - Quick Start Examples");
        log.info("=".repeat(60));

        // Example 1: Basic query
        basicExample();

        // Example 2: Query with options
        withOptionsExample();

        // Example 3: Query with tools
        withToolsExample();
    }

    /**
     * Example 1: Basic query - simple question.
     */
    static void basicExample() {
        log.info("=== Basic Example ===");

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
     * Example 2: Query with custom options.
     */
    static void withOptionsExample() {
        log.info("=== With Options Example ===");

        ClaudeAgentOptions options = ClaudeAgentOptions.builder()
                .systemPrompt("You are a helpful assistant that explains things simply.")
                .maxTurns(1)
                .build();

        ClaudeAgentSdk.query("Explain what Python is in one sentence.", options)
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
     * Example 3: Query using tools.
     */
    static void withToolsExample() {
        log.info("=== With Tools Example ===");

        ClaudeAgentOptions options = ClaudeAgentOptions.builder()
                .allowedTool("Read")
                .allowedTool("Write")
                .systemPrompt("You are a helpful file assistant.")
                .build();

        ClaudeAgentSdk.query(
                "Create a file called hello.txt with 'Hello, World!' in it",
                options
        ).forEach(message -> {
            if (message instanceof AssistantMessage) {
                AssistantMessage assistantMsg = (AssistantMessage) message;
                assistantMsg.getContent().forEach(block -> {
                    if (block instanceof TextBlock) {
                        TextBlock textBlock = (TextBlock) block;
                        log.info("Claude: {}", textBlock.getText());
                    }
                });
            } else if (message instanceof ResultMessage) {
                ResultMessage resultMsg = (ResultMessage) message;
                if (resultMsg.getTotalCostUsd() != null && resultMsg.getTotalCostUsd() > 0) {
                    log.info("Cost: ${}", String.format("%.4f", resultMsg.getTotalCostUsd()));
                }
            }
        });
    }
}
