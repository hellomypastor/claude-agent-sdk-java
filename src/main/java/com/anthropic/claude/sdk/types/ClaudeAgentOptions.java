package com.anthropic.claude.sdk.types;

import com.anthropic.claude.sdk.hooks.HookMatcher;
import com.anthropic.claude.sdk.mcp.McpServerConfig;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Configuration options for Claude Agent.
 *
 * Builder pattern is used to construct instances.
 */
public class ClaudeAgentOptions {

    @Nullable
    private final List<String> allowedTools;
    @Nullable
    private final String systemPrompt;
    @Nullable
    private final Map<String, McpServerConfig> mcpServers;
    @Nullable
    private final PermissionMode permissionMode;
    @Nullable
    private final Integer maxTurns;
    @Nullable
    private final Double maxBudgetUsd;
    @Nullable
    private final Function<ToolUseBlock, ToolPermissionContext, PermissionResult> canUseTool;
    @Nullable
    private final Map<String, List<HookMatcher>> hooks;
    @Nullable
    private final Path cwd;
    @Nullable
    private final String model;
    @Nullable
    private final Integer maxThinkingTokens;

    private ClaudeAgentOptions(Builder builder) {
        this.allowedTools = builder.allowedTools;
        this.systemPrompt = builder.systemPrompt;
        this.mcpServers = builder.mcpServers;
        this.permissionMode = builder.permissionMode;
        this.maxTurns = builder.maxTurns;
        this.maxBudgetUsd = builder.maxBudgetUsd;
        this.canUseTool = builder.canUseTool;
        this.hooks = builder.hooks;
        this.cwd = builder.cwd;
        this.model = builder.model;
        this.maxThinkingTokens = builder.maxThinkingTokens;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Nullable
    public List<String> getAllowedTools() {
        return allowedTools;
    }

    @Nullable
    public String getSystemPrompt() {
        return systemPrompt;
    }

    @Nullable
    public Map<String, McpServerConfig> getMcpServers() {
        return mcpServers;
    }

    @Nullable
    public PermissionMode getPermissionMode() {
        return permissionMode;
    }

    @Nullable
    public Integer getMaxTurns() {
        return maxTurns;
    }

    @Nullable
    public Double getMaxBudgetUsd() {
        return maxBudgetUsd;
    }

    @Nullable
    public Function<ToolUseBlock, ToolPermissionContext, PermissionResult> getCanUseTool() {
        return canUseTool;
    }

    @Nullable
    public Map<String, List<HookMatcher>> getHooks() {
        return hooks;
    }

    @Nullable
    public Path getCwd() {
        return cwd;
    }

    @Nullable
    public String getModel() {
        return model;
    }

    @Nullable
    public Integer getMaxThinkingTokens() {
        return maxThinkingTokens;
    }

    public static class Builder {
        private List<String> allowedTools;
        private String systemPrompt;
        private Map<String, McpServerConfig> mcpServers;
        private PermissionMode permissionMode;
        private Integer maxTurns;
        private Double maxBudgetUsd;
        private Function<ToolUseBlock, ToolPermissionContext, PermissionResult> canUseTool;
        private Map<String, List<HookMatcher>> hooks;
        private Path cwd;
        private String model;
        private Integer maxThinkingTokens;

        private Builder() {
        }

        public Builder allowedTools(List<String> allowedTools) {
            this.allowedTools = allowedTools;
            return this;
        }

        public Builder systemPrompt(String systemPrompt) {
            this.systemPrompt = systemPrompt;
            return this;
        }

        public Builder mcpServers(Map<String, McpServerConfig> mcpServers) {
            this.mcpServers = mcpServers;
            return this;
        }

        public Builder permissionMode(PermissionMode permissionMode) {
            this.permissionMode = permissionMode;
            return this;
        }

        public Builder maxTurns(Integer maxTurns) {
            this.maxTurns = maxTurns;
            return this;
        }

        public Builder maxBudgetUsd(Double maxBudgetUsd) {
            this.maxBudgetUsd = maxBudgetUsd;
            return this;
        }

        public Builder canUseTool(Function<ToolUseBlock, ToolPermissionContext, PermissionResult> canUseTool) {
            this.canUseTool = canUseTool;
            return this;
        }

        public Builder hooks(Map<String, List<HookMatcher>> hooks) {
            this.hooks = hooks;
            return this;
        }

        public Builder cwd(Path cwd) {
            this.cwd = cwd;
            return this;
        }

        public Builder cwd(String cwd) {
            this.cwd = Path.of(cwd);
            return this;
        }

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder maxThinkingTokens(Integer maxThinkingTokens) {
            this.maxThinkingTokens = maxThinkingTokens;
            return this;
        }

        public ClaudeAgentOptions build() {
            return new ClaudeAgentOptions(this);
        }
    }
}
