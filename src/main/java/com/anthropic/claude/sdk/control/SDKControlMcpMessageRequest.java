package com.anthropic.claude.sdk.control;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request to send an MCP message to a server.
 */
public class SDKControlMcpMessageRequest {

    @JsonProperty("subtype")
    private final String subtype = "mcp_message";

    @JsonProperty("server_name")
    private final String serverName;

    @JsonProperty("message")
    private final Object message;

    public SDKControlMcpMessageRequest(String serverName, Object message) {
        this.serverName = serverName;
        this.message = message;
    }

    public String getSubtype() {
        return subtype;
    }

    public String getServerName() {
        return serverName;
    }

    public Object getMessage() {
        return message;
    }
}
