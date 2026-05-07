package com.anthropic.claude.sdk.types.mcp;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

/**
 * MCP stdio server configuration.
 * <p>
 * Launches a child process and communicates via stdin/stdout JSON-RPC.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record McpStdioServerConfig(
        String type,
        String command,
        List<String> args,
        Map<String, String> env
) implements McpServerConfig {

    /**
     * Create a stdio config with explicit type.
     */
    public McpStdioServerConfig {
    }

    /**
     * Convenience: create without specifying type (optional for backwards compatibility).
     */
    public McpStdioServerConfig(String command, List<String> args, Map<String, String> env) {
        this(null, command, args, env);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String type;
        private String command;
        private List<String> args;
        private Map<String, String> env;

        private Builder() {
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder command(String command) {
            this.command = command;
            return this;
        }

        public Builder args(List<String> args) {
            this.args = args;
            return this;
        }

        public Builder env(Map<String, String> env) {
            this.env = env;
            return this;
        }

        public McpStdioServerConfig build() {
            return new McpStdioServerConfig(type, command, args, env);
        }
    }
}
