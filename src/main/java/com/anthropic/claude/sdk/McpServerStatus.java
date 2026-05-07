package com.anthropic.claude.sdk;

/**
 * Status of an MCP server connection.
 *
 * @param name       the server name
 * @param status     the connection status (e.g. "connected", "disconnected", "error")
 * @param serverInfo information about the server
 * @param error      error message if the server is in an error state
 */
public record McpServerStatus(
        String name,
        String status,
        McpServerInfo serverInfo,
        String error
) {

    /**
     * Basic information about an MCP server.
     *
     * @param name    the server name
     * @param version the server version
     */
    public record McpServerInfo(
            String name,
            String version
    ) {
    }
}
