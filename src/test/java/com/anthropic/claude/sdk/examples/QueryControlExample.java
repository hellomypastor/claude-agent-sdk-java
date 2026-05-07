package com.anthropic.claude.sdk.examples;

import com.anthropic.claude.sdk.AccountInfo;
import com.anthropic.claude.sdk.McpServerStatus;
import com.anthropic.claude.sdk.ModelInfo;
import com.anthropic.claude.sdk.Query;
import com.anthropic.claude.sdk.client.ClaudeSDKClient;
import com.anthropic.claude.sdk.types.content.TextBlock;
import com.anthropic.claude.sdk.types.messages.AssistantMessage;
import com.anthropic.claude.sdk.types.messages.Message;
import com.anthropic.claude.sdk.types.messages.ResultSuccess;
import com.anthropic.claude.sdk.types.options.ClaudeAgentOptions;
import com.anthropic.claude.sdk.types.options.PermissionMode;

import java.util.List;

/**
 * Query Control Example - Demonstrates the Query interface for runtime control.
 * <p>
 * This example shows how to:
 * 1. Get available models
 * 2. Check MCP server status
 * 3. Get account info
 * 4. Change model at runtime
 * 5. Send queries and process responses
 */
public class QueryControlExample {
    public static void main(String[] args) throws Exception {
        ClaudeAgentOptions options = ClaudeAgentOptions.builder()
                .permissionMode(PermissionMode.ACCEPT_EDITS)
                .build();

        try (ClaudeSDKClient client = new ClaudeSDKClient(options)) {
            client.connect().join();

            Query query = client.getQuery();

            // Get available models
            List<ModelInfo> models = query.supportedModels().join();
            for (ModelInfo model : models) {
                System.out.println("Model: " + model.displayName() + " (" + model.value() + ")");
            }

            // Check MCP server status
            List<McpServerStatus> statuses = query.mcpServerStatus().join();
            for (McpServerStatus status : statuses) {
                System.out.println("MCP: " + status.name() + " -> " + status.status());
            }

            // Get account info
            AccountInfo account = query.accountInfo().join();
            System.out.println("Account: " + account.email());

            // Change model at runtime
            query.setModel("claude-sonnet-4-5-20250929").join();

            // Send a query
            client.query("What is 2+2?").join();

            for (Message msg : client.receiveMessages().toList()) {
                if (msg instanceof AssistantMessage am) {
                    for (var block : am.content()) {
                        if (block instanceof TextBlock tb) {
                            System.out.println("Response: " + tb.text());
                        }
                    }
                } else if (msg instanceof ResultSuccess rs) {
                    System.out.println("Cost: $" + rs.totalCostUsd());
                }
            }
        }
    }
}
