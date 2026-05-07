package com.anthropic.claude.sdk.types.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * System initialization message sent at the start of a session.
 */
public record SystemInitMessage(
        @JsonProperty("agents") List<String> agents,
        @JsonProperty("api_key_source") String apiKeySource,
        @JsonProperty("betas") List<String> betas,
        @JsonProperty("claude_code_version") String claudeCodeVersion,
        @JsonProperty("cwd") String cwd,
        @JsonProperty("tools") List<String> tools,
        @JsonProperty("mcp_servers") List<McpServerInfo> mcpServers,
        @JsonProperty("model") String model,
        @JsonProperty("permission_mode") String permissionMode,
        @JsonProperty("slash_commands") List<String> slashCommands,
        @JsonProperty("output_style") String outputStyle,
        @JsonProperty("skills") List<String> skills,
        @JsonProperty("plugins") List<PluginInfo> plugins,
        @JsonProperty("uuid") String uuid,
        @JsonProperty("session_id") String sessionId
) implements Message {

    /**
     * Information about an MCP server.
     */
    public record McpServerInfo(
            @JsonProperty("name") String name,
            @JsonProperty("status") String status
    ) {}

    /**
     * Information about a plugin.
     */
    public record PluginInfo(
            @JsonProperty("name") String name,
            @JsonProperty("path") String path
    ) {}

    @Override
    public String getType() {
        return "system";
    }

    public String getSubtype() {
        return "init";
    }
}
