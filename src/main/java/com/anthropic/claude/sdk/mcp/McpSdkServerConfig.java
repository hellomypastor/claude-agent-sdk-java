package com.anthropic.claude.sdk.mcp;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Configuration for an in-process SDK MCP server.
 */
public class McpSdkServerConfig implements McpServerConfig {

    private final String name;
    private final Object instance;

    @JsonCreator
    public McpSdkServerConfig(
            @JsonProperty("name") String name,
            @JsonProperty("instance") Object instance) {
        this.name = name;
        this.instance = instance;
    }

    @Override
    public String getType() {
        return "sdk";
    }

    public String getName() {
        return name;
    }

    public Object getInstance() {
        return instance;
    }
}
