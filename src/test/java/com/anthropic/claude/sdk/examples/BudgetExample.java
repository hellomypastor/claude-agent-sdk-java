package com.anthropic.claude.sdk.examples;

import com.anthropic.claude.sdk.ClaudeAgentSdk;
import com.anthropic.claude.sdk.types.content.TextBlock;
import com.anthropic.claude.sdk.types.messages.AssistantMessage;
import com.anthropic.claude.sdk.types.messages.ResultMessage;
import com.anthropic.claude.sdk.types.options.ClaudeAgentOptions;
import lombok.extern.slf4j.Slf4j;

/**
 * Budget Control Example - Demonstrates max_budget_usd option.
 *
 * This example shows how to:
 * 1. Run queries without budget limits
 * 2. Set reasonable budget limits
 * 3. Handle budget exceeded scenarios
 *
 * Usage:
 *   java BudgetExample
 */
@Slf4j
public class BudgetExample {

    public static void main(String[] args) {
        log.info("=".repeat(60));
        log.info("Budget Control Example");
        log.info("=".repeat(60));
        log.info("This example demonstrates using max_budget_usd to control API costs.");

        withoutBudget();
        withReasonableBudget();
        withTightBudget();

        log.info("Note: Budget checking happens after each API call completes,");
        log.info("so the final cost may slightly exceed the specified budget.");
    }

    /**
     * Example 1: Without budget limit
     */
    static void withoutBudget() {
        log.info("=== Without Budget Limit ===");

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
                } else if (message instanceof ResultMessage) {
                    ResultMessage resultMsg = (ResultMessage) message;
                    if (resultMsg.getTotalCostUsd() != null) {
                        log.info("Total cost: ${}", String.format("%.4f", resultMsg.getTotalCostUsd()));
                    }
                    log.info("Status: {}", resultMsg.getSubtype());
                }
            });
    }

    /**
     * Example 2: With reasonable budget ($0.10)
     */
    static void withReasonableBudget() {
        log.info("=== With Reasonable Budget ($0.10) ===");

        ClaudeAgentOptions options = ClaudeAgentOptions.builder()
            .maxBudgetUsd(0.10)  // 10 cents - plenty for a simple query
            .build();

        ClaudeAgentSdk.query("What is 2 + 2?", options)
            .forEach(message -> {
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
                    if (resultMsg.getTotalCostUsd() != null) {
                        log.info("Total cost: ${}", String.format("%.4f", resultMsg.getTotalCostUsd()));
                    }
                    log.info("Status: {}", resultMsg.getSubtype());
                }
            });
    }

    /**
     * Example 3: With very tight budget ($0.0001)
     * This will likely be exceeded
     */
    static void withTightBudget() {
        log.info("=== With Tight Budget ($0.0001) ===");

        ClaudeAgentOptions options = ClaudeAgentOptions.builder()
            .maxBudgetUsd(0.0001)  // Very small budget - will be exceeded quickly
            .allowedTool("Read")  // Allow Read tool to make the query more expensive
            .build();

        ClaudeAgentSdk.query("Read the README.md file and summarize it", options)
            .forEach(message -> {
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
                    if (resultMsg.getTotalCostUsd() != null) {
                        log.info("Total cost: ${}", String.format("%.4f", resultMsg.getTotalCostUsd()));
                    }
                    log.info("Status: {}", resultMsg.getSubtype());

                    // Check if budget was exceeded
                    if ("error_max_budget_usd".equals(resultMsg.getSubtype())) {
                        log.warn("⚠️  Budget limit exceeded!");
                        log.warn("Note: The cost may exceed the budget by up to one API call's worth");
                    }
                }
            });
    }
}
