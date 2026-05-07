package com.anthropic.claude.sdk;

/**
 * Status of an MCP server connection.
 */
public final class McpServerStatus {

    private final String name;
    private final String status;
    private final McpServerInfo serverInfo;
    private final String error;

    public McpServerStatus(String name, String status, McpServerInfo serverInfo, String error) {
        this.name = name;
        this.status = status;
        this.serverInfo = serverInfo;
        this.error = error;
    }

    public String name() {
        return name;
    }

    public String status() {
        return status;
    }

    public McpServerInfo serverInfo() {
        return serverInfo;
    }

    public String error() {
        return error;
    }

    /**
     * Basic information about an MCP server.
     */
    public static final class McpServerInfo {

        private final String name;
        private final String version;

        public McpServerInfo(String name, String version) {
            this.name = name;
            this.version = version;
        }

        public String name() {
            return name;
        }

        public String version() {
            return version;
        }
    }
}
