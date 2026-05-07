package com.anthropic.claude.sdk.types.mcp;

/**
 * Base type for MCP server configurations.
 * <p>
 * Sealed interface with four permitted implementations:
 * <ul>
 *   <li>{@link McpStdioServerConfig} — stdio-based MCP server (command + args)</li>
 *   <li>{@link McpHttpServerConfig} — HTTP-based MCP server (url + headers)</li>
 *   <li>{@link McpSSEServerConfig} — Server-Sent Events MCP server (url + headers)</li>
 *   <li>{@link McpSdkServerConfig} — in-process SDK MCP server</li>
 * </ul>
 */
public sealed interface McpServerConfig
        permits McpStdioServerConfig, McpHttpServerConfig, McpSSEServerConfig, McpSdkServerConfig {

    /**
     * MCP server type identifier (e.g. "stdio", "http", "sse", "sdk").
     */
    String type();
}
