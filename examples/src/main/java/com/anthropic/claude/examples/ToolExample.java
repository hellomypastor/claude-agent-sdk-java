package com.anthropic.claude.examples;

import com.anthropic.claude.sdk.ClaudeAgent;
import com.anthropic.claude.sdk.errors.ClaudeSDKException;
import com.anthropic.claude.sdk.mcp.McpSdkServerConfig;
import com.anthropic.claude.sdk.mcp.SdkMcpServer;
import com.anthropic.claude.sdk.mcp.SdkMcpTool;
import com.anthropic.claude.sdk.types.ClaudeAgentOptions;
import com.anthropic.claude.sdk.types.Message;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Example demonstrating custom tool creation with MCP.
 */
public class ToolExample {

    public static void main(String[] args) {
        try {
            // Create a custom tool
            SdkMcpTool<Map<String, Object>> greetTool = SdkMcpTool.<Map<String, Object>>builder("greet", "Greet a user by name")
                    .inputSchema(createGreetInputSchema())
                    .handler(input -> {
                        String name = (String) input.get("name");
                        Map<String, Object> response = new HashMap<>();
                        List<Map<String, String>> content = new ArrayList<>();
                        Map<String, String> textContent = new HashMap<>();
                        textContent.put("type", "text");
                        textContent.put("text", "Hello, " + name + "! Nice to meet you!");
                        content.add(textContent);
                        response.put("content", content);
                        return CompletableFuture.completedFuture(response);
                    })
                    .build();

            // Create MCP server with the tool
            SdkMcpServer server = SdkMcpServer.create("my-tools", Arrays.asList(greetTool));

            // Configure options with the MCP server
            Map<String, com.anthropic.claude.sdk.mcp.McpServerConfig> mcpServers = new HashMap<>();
            mcpServers.put("tools", new McpSdkServerConfig("my-tools", server));

            ClaudeAgentOptions options = ClaudeAgentOptions.builder()
                    .mcpServers(mcpServers)
                    .allowedTools(Arrays.asList("mcp__tools__greet"))
                    .build();

            // Query Claude to use the tool
            Iterator<Message> messages = ClaudeAgent.query(
                    "Please greet Alice using the available tool.",
                    options
            );

            while (messages.hasNext()) {
                Message message = messages.next();
                System.out.println("Message: " + message);
            }

        } catch (ClaudeSDKException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static Map<String, Object> createGreetInputSchema() {
        Map<String, Object> schema = new HashMap<>();
        schema.put("type", "object");

        Map<String, Object> properties = new HashMap<>();
        Map<String, Object> nameProperty = new HashMap<>();
        nameProperty.put("type", "string");
        nameProperty.put("description", "The name of the person to greet");
        properties.put("name", nameProperty);

        schema.put("properties", properties);
        schema.put("required", Arrays.asList("name"));

        return schema;
    }
}
