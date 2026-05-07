package com.anthropic.claude.sdk.examples;

import com.anthropic.claude.sdk.ClaudeAgentSdk;
import com.anthropic.claude.sdk.types.content.TextBlock;
import com.anthropic.claude.sdk.types.messages.AssistantMessage;
import com.anthropic.claude.sdk.types.messages.ResultError;
import com.anthropic.claude.sdk.types.messages.ResultMessage;
import com.anthropic.claude.sdk.types.messages.ResultSuccess;
import com.anthropic.claude.sdk.types.options.ClaudeAgentOptions;

import java.util.List;

/**
 * Budget Control Example - Demonstrates max_budget_usd option.
 * <p>
 * This example shows how to:
 * 1. Run queries without budget limits
 * 2. Set reasonable budget limits
 * 3. Handle budget exceeded scenarios
 * <p>
 * Usage:
 * java BudgetExample
 */
public class BudgetExample {

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("Budget Control Example");
        System.out.println("=".repeat(60));
        System.out.println("This example demonstrates using max_budget_usd to control API costs.");

        withoutBudget();
        withReasonableBudget();
        withTightBudget();

        System.out.println("Note: Budget checking happens after each API call completes,");
        System.out.println("so the final cost may slightly exceed the specified budget.");
    }

    /**
     * Example 1: Without budget limit
     */
    static void withoutBudget() {
        System.out.println("=== Without Budget Limit ===");

        ClaudeAgentSdk.query("What is 2 + 2?")
                .forEach(message -> {
                    if (message instanceof AssistantMessage assistantMsg) {
                        assistantMsg.content().forEach(block -> {
                            if (block instanceof TextBlock textBlock) {
                                System.out.println("Claude: " + textBlock.text());
                            }
                        });
                    } else if (message instanceof ResultMessage resultMsg) {
                        System.out.println("Total cost: $" + String.format("%.4f", resultMsg.getTotalCostUsd()));
                        System.out.println("Status: " + resultMsg.getSubtype());
                    }
                });
    }

    /**
     * Example 2: With reasonable budget ($0.10)
     */
    static void withReasonableBudget() {
        System.out.println("=== With Reasonable Budget ($0.10) ===");

        ClaudeAgentOptions options = ClaudeAgentOptions.builder()
                .maxBudgetUsd(0.10)  // 10 cents - plenty for a simple query
                .build();

        ClaudeAgentSdk.query("What is 2 + 2?", options)
                .forEach(message -> {
                    if (message instanceof AssistantMessage assistantMsg) {
                        assistantMsg.content().forEach(block -> {
                            if (block instanceof TextBlock textBlock) {
                                System.out.println("Claude: " + textBlock.text());
                            }
                        });
                    } else if (message instanceof ResultMessage resultMsg) {
                        System.out.println("Total cost: $" + String.format("%.4f", resultMsg.getTotalCostUsd()));
                        System.out.println("Status: " + resultMsg.getSubtype());
                    }
                });
    }

    /**
     * Example 3: With very tight budget ($0.0001)
     * This will likely be exceeded
     */
    static void withTightBudget() {
        System.out.println("=== With Tight Budget ($0.0001) ===");

        ClaudeAgentOptions options = ClaudeAgentOptions.builder()
                .maxBudgetUsd(0.0001)  // Very small budget - will be exceeded quickly
                .allowedTools(List.of("Read"))  // Allow Read tool to make the query more expensive
                .build();

        ClaudeAgentSdk.query("Read the README.md file and summarize it", options)
                .forEach(message -> {
                    if (message instanceof AssistantMessage assistantMsg) {
                        assistantMsg.content().forEach(block -> {
                            if (block instanceof TextBlock textBlock) {
                                System.out.println("Claude: " + textBlock.text());
                            }
                        });
                    } else if (message instanceof ResultError resultErr) {
                        System.out.println("Total cost: $" + String.format("%.4f", resultErr.getTotalCostUsd()));
                        System.out.println("Status: " + resultErr.getSubtype());

                        // Check if budget was exceeded
                        if ("error_max_budget_usd".equals(resultErr.getSubtype())) {
                            System.out.println("WARNING: Budget limit exceeded!");
                            System.out.println("Note: The cost may exceed the budget by up to one API call's worth");
                        }
                    } else if (message instanceof ResultMessage resultMsg) {
                        System.out.println("Total cost: $" + String.format("%.4f", resultMsg.getTotalCostUsd()));
                        System.out.println("Status: " + resultMsg.getSubtype());
                    }
                });
    }
}
