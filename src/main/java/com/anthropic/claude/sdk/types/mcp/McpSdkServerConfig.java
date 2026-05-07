package com.anthropic.claude.sdk.types.mcp;

import com.anthropic.claude.sdk.mcp.SdkMcpServer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * SDK MCP server configuration for in-process tools.
 * <p>
 * Wraps a {@link SdkMcpServer} instance that handles MCP messages in-process.
 * The {@code instance} field is excluded from JSON serialization.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class McpSdkServerConfig implements McpServerConfig {

    private final String type;
    private final String name;
    @JsonIgnore
    private final SdkMcpServer instance;

    public McpSdkServerConfig(String type, String name, SdkMcpServer instance) {
        this.type = type != null ? type : "sdk";
        this.name = name;
        this.instance = instance;
    }

    @Override
    public String type() {
        return type;
    }

    public String name() {
        return name;
    }

    public SdkMcpServer instance() {
        return instance;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String type = "sdk";
        private String name;
        private SdkMcpServer instance;

        private Builder() {
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder instance(SdkMcpServer instance) {
            this.instance = instance;
            return this;
        }

        public McpSdkServerConfig build() {
            return new McpSdkServerConfig(type, name, instance);
        }
    }
}
