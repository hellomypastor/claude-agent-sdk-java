package com.anthropic.claude.sdk.types.mcp;

/**
 * Base type for MCP server configurations.
 * <p>
 * Implementations:
 * <ul>
 *   <li>{@link McpStdioServerConfig} — stdio-based MCP server (command + args)</li>
 *   <li>{@link McpHttpServerConfig} — HTTP-based MCP server (url + headers)</li>
 *   <li>{@link McpSSEServerConfig} — Server-Sent Events MCP server (url + headers)</li>
 *   <li>{@link McpSdkServerConfig} — in-process SDK MCP server</li>
 * </ul>
 */
public interface McpServerConfig {

    /**
     * MCP server type identifier (e.g. "stdio", "http", "sse", "sdk").
     */
    String type();
}
