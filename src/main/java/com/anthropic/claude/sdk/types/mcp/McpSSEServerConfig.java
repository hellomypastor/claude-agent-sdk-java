package com.anthropic.claude.sdk.types.mcp;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

/**
 * MCP SSE (Server-Sent Events) server configuration.
 * <p>
 * Connects to a remote MCP server via SSE.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class McpSSEServerConfig implements McpServerConfig {

    private final String type;
    private final String url;
    private final Map<String, String> headers;

    public McpSSEServerConfig(String type, String url, Map<String, String> headers) {
        this.type = type != null ? type : "sse";
        this.url = url;
        this.headers = headers;
    }

    @Override
    public String type() {
        return type;
    }

    public String url() {
        return url;
    }

    public Map<String, String> headers() {
        return headers;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String type = "sse";
        private String url;
        private Map<String, String> headers;

        private Builder() {
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public McpSSEServerConfig build() {
            return new McpSSEServerConfig(type, url, headers);
        }
    }
}
