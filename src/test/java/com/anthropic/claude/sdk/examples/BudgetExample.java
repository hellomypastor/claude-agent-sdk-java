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
 */
public class BudgetExample {

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("Budget Control Example");
        System.out.println("=".repeat(60));

        withoutBudget();
        withReasonableBudget();
        withTightBudget();
    }

    static void withoutBudget() {
        System.out.println("=== Without Budget Limit ===");

        ClaudeAgentSdk.query("What is 2 + 2?")
                .forEach(message -> {
                    if (message instanceof AssistantMessage) {
                        AssistantMessage assistantMsg = (AssistantMessage) message;
                        assistantMsg.content().forEach(block -> {
                            if (block instanceof TextBlock) {
                                System.out.println("Claude: " + ((TextBlock) block).text());
                            }
                        });
                    } else if (message instanceof ResultMessage) {
                        ResultMessage resultMsg = (ResultMessage) message;
                        System.out.println("Total cost: $" + String.format("%.4f", resultMsg.getTotalCostUsd()));
                        System.out.println("Status: " + resultMsg.getSubtype());
                    }
                });
    }

    static void withReasonableBudget() {
        System.out.println("=== With Reasonable Budget ($0.10) ===");

        ClaudeAgentOptions options = ClaudeAgentOptions.builder()
                .maxBudgetUsd(0.10)
                .build();

        ClaudeAgentSdk.query("What is 2 + 2?", options)
                .forEach(message -> {
                    if (message instanceof AssistantMessage) {
                        AssistantMessage assistantMsg = (AssistantMessage) message;
                        assistantMsg.content().forEach(block -> {
                            if (block instanceof TextBlock) {
                                System.out.println("Claude: " + ((TextBlock) block).text());
                            }
                        });
                    } else if (message instanceof ResultMessage) {
                        ResultMessage resultMsg = (ResultMessage) message;
                        System.out.println("Total cost: $" + String.format("%.4f", resultMsg.getTotalCostUsd()));
                        System.out.println("Status: " + resultMsg.getSubtype());
                    }
                });
    }

    static void withTightBudget() {
        System.out.println("=== With Tight Budget ($0.0001) ===");

        ClaudeAgentOptions options = ClaudeAgentOptions.builder()
                .maxBudgetUsd(0.0001)
                .allowedTools(List.of("Read"))
                .build();

        ClaudeAgentSdk.query("Read the README.md file and summarize it", options)
                .forEach(message -> {
                    if (message instanceof AssistantMessage) {
                        AssistantMessage assistantMsg = (AssistantMessage) message;
                        assistantMsg.content().forEach(block -> {
                            if (block instanceof TextBlock) {
                                System.out.println("Claude: " + ((TextBlock) block).text());
                            }
                        });
                    } else if (message instanceof ResultError) {
                        ResultError resultErr = (ResultError) message;
                        System.out.println("Total cost: $" + String.format("%.4f", resultErr.getTotalCostUsd()));
                        System.out.println("Status: " + resultErr.getSubtype());
                        if ("error_max_budget_usd".equals(resultErr.getSubtype())) {
                            System.out.println("WARNING: Budget limit exceeded!");
                        }
                    } else if (message instanceof ResultMessage) {
                        ResultMessage resultMsg = (ResultMessage) message;
                        System.out.println("Total cost: $" + String.format("%.4f", resultMsg.getTotalCostUsd()));
                        System.out.println("Status: " + resultMsg.getSubtype());
                    }
                });
    }
}
