package com.anthropic.claude.sdk.mcp;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Configuration for an MCP server using HTTP transport.
 */
public class McpHttpServerConfig implements McpServerConfig {

    private final String url;
    @Nullable
    private final Map<String, String> headers;

    @JsonCreator
    public McpHttpServerConfig(
            @JsonProperty("url") String url,
            @JsonProperty("headers") @Nullable Map<String, String> headers) {
        this.url = url;
        this.headers = headers;
    }

    public McpHttpServerConfig(String url) {
        this(url, null);
    }

    @Override
    public String getType() {
        return "http";
    }

    public String getUrl() {
        return url;
    }

    @Nullable
    public Map<String, String> getHeaders() {
        return headers;
    }
}
