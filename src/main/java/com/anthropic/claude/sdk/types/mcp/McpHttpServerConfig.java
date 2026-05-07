package com.anthropic.claude.sdk.types.mcp;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

/**
 * MCP HTTP server configuration.
 * <p>
 * Connects to a remote MCP server via HTTP.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class McpHttpServerConfig implements McpServerConfig {

    private final String type;
    private final String url;
    private final Map<String, String> headers;

    public McpHttpServerConfig(String type, String url, Map<String, String> headers) {
        this.type = type != null ? type : "http";
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
        private String type = "http";
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

        public McpHttpServerConfig build() {
            return new McpHttpServerConfig(type, url, headers);
        }
    }
}
