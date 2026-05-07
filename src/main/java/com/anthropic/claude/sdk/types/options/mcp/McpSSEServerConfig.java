package com.anthropic.claude.sdk.types.options.mcp;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

/**
 * MCP SSE (Server-Sent Events) server configuration.
 * <p>
 * Connects to a remote MCP server via SSE.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record McpSSEServerConfig(
        String type,
        String url,
        Map<String, String> headers
) implements McpServerConfig {

    public McpSSEServerConfig {
        if (type == null) {
            type = "sse";
        }
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
