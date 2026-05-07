package com.anthropic.claude.sdk.examples;

import com.anthropic.claude.sdk.ClaudeAgentSdk;
import com.anthropic.claude.sdk.types.content.TextBlock;
import com.anthropic.claude.sdk.types.messages.AssistantMessage;
import com.anthropic.claude.sdk.types.messages.ResultMessage;
import com.anthropic.claude.sdk.types.messages.ResultSuccess;
import com.anthropic.claude.sdk.types.options.ClaudeAgentOptions;

import java.util.List;

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
public class QuickStart {

    public static void main(String[] args) {
        System.out.println("Claude Agent SDK - Quick Start Examples");
        System.out.println("=".repeat(60));

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
        System.out.println("=== Basic Example ===");

        ClaudeAgentSdk.query("What is 2 + 2?")
                .forEach(message -> {
                    if (message instanceof AssistantMessage assistantMsg) {
                        assistantMsg.content().forEach(block -> {
                            if (block instanceof TextBlock textBlock) {
                                System.out.println("Claude: " + textBlock.text());
                            }
                        });
                    }
                });
    }

    /**
     * Example 2: Query with custom options.
     */
    static void withOptionsExample() {
        System.out.println("=== With Options Example ===");

        ClaudeAgentOptions options = ClaudeAgentOptions.builder()
                .systemPrompt("You are a helpful assistant that explains things simply.")
                .maxTurns(1)
                .build();

        ClaudeAgentSdk.query("Explain what Python is in one sentence.", options)
                .forEach(message -> {
                    if (message instanceof AssistantMessage assistantMsg) {
                        assistantMsg.content().forEach(block -> {
                            if (block instanceof TextBlock textBlock) {
                                System.out.println("Claude: " + textBlock.text());
                            }
                        });
                    }
                });
    }

    /**
     * Example 3: Query using tools.
     */
    static void withToolsExample() {
        System.out.println("=== With Tools Example ===");

        ClaudeAgentOptions options = ClaudeAgentOptions.builder()
                .allowedTools(List.of("Read", "Write"))
                .systemPrompt("You are a helpful file assistant.")
                .build();

        ClaudeAgentSdk.query(
                "Create a file called hello.txt with 'Hello, World!' in it",
                options
        ).forEach(message -> {
            if (message instanceof AssistantMessage assistantMsg) {
                assistantMsg.content().forEach(block -> {
                    if (block instanceof TextBlock textBlock) {
                        System.out.println("Claude: " + textBlock.text());
                    }
                });
            } else if (message instanceof ResultSuccess resultMsg) {
                if (resultMsg.totalCostUsd() > 0) {
                    System.out.println("Cost: $" + String.format("%.4f", resultMsg.totalCostUsd()));
                }
            }
        });
    }
}
