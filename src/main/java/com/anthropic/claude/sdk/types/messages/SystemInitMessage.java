package com.anthropic.claude.sdk.types.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * System initialization message sent at the start of a session.
 */
public final class SystemInitMessage implements Message {

    private final List<String> agents;
    private final String apiKeySource;
    private final List<String> betas;
    private final String claudeCodeVersion;
    private final String cwd;
    private final List<String> tools;
    private final List<McpServerInfo> mcpServers;
    private final String model;
    private final String permissionMode;
    private final List<String> slashCommands;
    private final String outputStyle;
    private final List<String> skills;
    private final List<PluginInfo> plugins;
    private final String uuid;
    private final String sessionId;

    public SystemInitMessage(
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
    ) {
        this.agents = agents;
        this.apiKeySource = apiKeySource;
        this.betas = betas;
        this.claudeCodeVersion = claudeCodeVersion;
        this.cwd = cwd;
        this.tools = tools;
        this.mcpServers = mcpServers;
        this.model = model;
        this.permissionMode = permissionMode;
        this.slashCommands = slashCommands;
        this.outputStyle = outputStyle;
        this.skills = skills;
        this.plugins = plugins;
        this.uuid = uuid;
        this.sessionId = sessionId;
    }

    /**
     * Information about an MCP server.
     */
    public static final class McpServerInfo {

        private final String name;
        private final String status;

        public McpServerInfo(
                @JsonProperty("name") String name,
                @JsonProperty("status") String status
        ) {
            this.name = name;
            this.status = status;
        }

        @JsonProperty("name")
        public String name() {
            return name;
        }

        @JsonProperty("status")
        public String status() {
            return status;
        }
    }

    /**
     * Information about a plugin.
     */
    public static final class PluginInfo {

        private final String name;
        private final String path;

        public PluginInfo(
                @JsonProperty("name") String name,
                @JsonProperty("path") String path
        ) {
            this.name = name;
            this.path = path;
        }

        @JsonProperty("name")
        public String name() {
            return name;
        }

        @JsonProperty("path")
        public String path() {
            return path;
        }
    }

    @JsonProperty("agents")
    public List<String> agents() {
        return agents;
    }

    @JsonProperty("api_key_source")
    public String apiKeySource() {
        return apiKeySource;
    }

    @JsonProperty("betas")
    public List<String> betas() {
        return betas;
    }

    @JsonProperty("claude_code_version")
    public String claudeCodeVersion() {
        return claudeCodeVersion;
    }

    @JsonProperty("cwd")
    public String cwd() {
        return cwd;
    }

    @JsonProperty("tools")
    public List<String> tools() {
        return tools;
    }

    @JsonProperty("mcp_servers")
    public List<McpServerInfo> mcpServers() {
        return mcpServers;
    }

    @JsonProperty("model")
    public String model() {
        return model;
    }

    @JsonProperty("permission_mode")
    public String permissionMode() {
        return permissionMode;
    }

    @JsonProperty("slash_commands")
    public List<String> slashCommands() {
        return slashCommands;
    }

    @JsonProperty("output_style")
    public String outputStyle() {
        return outputStyle;
    }

    @JsonProperty("skills")
    public List<String> skills() {
        return skills;
    }

    @JsonProperty("plugins")
    public List<PluginInfo> plugins() {
        return plugins;
    }

    @JsonProperty("uuid")
    public String uuid() {
        return uuid;
    }

    @JsonProperty("session_id")
    public String sessionId() {
        return sessionId;
    }

    @Override
    public String getType() {
        return "system";
    }

    public String getSubtype() {
        return "init";
    }
}
