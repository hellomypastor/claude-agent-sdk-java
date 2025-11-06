package com.anthropic.claude.sdk.mcp;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * Configuration for an MCP server using stdio transport.
 */
public class McpStdioServerConfig implements McpServerConfig {

    private final String command;
    @Nullable
    private final List<String> args;
    @Nullable
    private final Map<String, String> env;

    @JsonCreator
    public McpStdioServerConfig(
            @JsonProperty("command") String command,
            @JsonProperty("args") @Nullable List<String> args,
            @JsonProperty("env") @Nullable Map<String, String> env) {
        this.command = command;
        this.args = args;
        this.env = env;
    }

    public McpStdioServerConfig(String command) {
        this(command, null, null);
    }

    @Override
    public String getType() {
        return "stdio";
    }

    public String getCommand() {
        return command;
    }

    @Nullable
    public List<String> getArgs() {
        return args;
    }

    @Nullable
    public Map<String, String> getEnv() {
        return env;
    }
}
