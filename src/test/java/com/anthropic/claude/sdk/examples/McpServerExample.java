package com.anthropic.claude.sdk.examples;

import com.anthropic.claude.sdk.ClaudeAgentSdk;
import com.anthropic.claude.sdk.mcp.SdkMcpServer;
import com.anthropic.claude.sdk.mcp.SdkMcpTool;
import com.anthropic.claude.sdk.types.content.TextBlock;
import com.anthropic.claude.sdk.types.messages.AssistantMessage;
import com.anthropic.claude.sdk.types.messages.Message;
import com.anthropic.claude.sdk.types.options.ClaudeAgentOptions;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Demonstrates building an in-process MCP server in Java.
 * <p>
 * This example mirrors the Python MCP calculator example:
 * 1. Create a custom tool via SdkMcpTool
 * 2. Register tools on SdkMcpServer
 * 3. Supply server via ClaudeAgentOptions.mcpServers
 * 4. Allow Claude to call the custom tool
 */
@Slf4j
public class McpServerExample {

    public static void main(String[] args) {
        log.info("=".repeat(60));
        log.info("MCP Server Example");
        log.info("=".repeat(60));

        SdkMcpTool addTool = SdkMcpTool.builder()
                .name("add_numbers")
                .description("Add two numbers together")
                .inputSchema(Map.of("a", "number", "b", "number"))
                .handler(input -> {
                    double a = Double.parseDouble(input.get("a").toString());
                    double b = Double.parseDouble(input.get("b").toString());
                    double sum = a + b;
                    Map<String, Object> content = Map.of(
                            "type", "text",
                            "text", "Sum: " + sum
                    );
                    return CompletableFuture.completedFuture(Map.of("content", List.of(content)));
                })
                .build();

        SdkMcpServer server = SdkMcpServer.builder()
                .name("calculator")
                .version("1.0.0")
                .tools(Map.of(addTool.getName(), addTool))
                .build();

        ClaudeAgentOptions options = ClaudeAgentOptions.builder()
                .allowedTool("add_numbers")
                .mcpServer("calc", server)
                .build();

        ClaudeAgentSdk.query(
                "Use add_numbers to add 123.4 and 456.6",
                options
        ).forEach(McpServerExample::printMessage);
    }

    private static void printMessage(Message message) {
        if (message instanceof AssistantMessage) {
            AssistantMessage assistant = (AssistantMessage) message;
            assistant.getContent().forEach(block -> {
                if (block instanceof TextBlock) {
                    TextBlock textBlock = (TextBlock) block;
                    log.info("Claude: {}", textBlock.getText());
                }
            });
        }
    }
}
