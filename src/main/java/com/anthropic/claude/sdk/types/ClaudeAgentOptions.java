package com.anthropic.claude.sdk.types;

import com.anthropic.claude.sdk.hooks.HookMatcher;
import com.anthropic.claude.sdk.mcp.McpServerConfig;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

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
    private final BiFunction<ToolUseBlock, ToolPermissionContext, PermissionResult> canUseTool;
    @Nullable
    private final Map<String, List<HookMatcher>> hooks;
    @Nullable
    private final Path cwd;
    @Nullable
    private final String model;
    @Nullable
    private final Integer maxThinkingTokens;
    // New fields to match Python SDK
    @Nullable
    private final Path cliPath;
    @Nullable
    private final Map<String, AgentDefinition> agents;
    @Nullable
    private final List<String> disallowedTools;
    private final boolean continueConversation;
    @Nullable
    private final String resume;
    @Nullable
    private final Map<String, String> env;
    @Nullable
    private final List<Path> addDirs;
    @Nullable
    private final String user;
    private final boolean includePartialMessages;
    private final boolean forkSession;
    // Additional Python SDK fields
    @Nullable
    private final String permissionPromptToolName;
    @Nullable
    private final String settings;
    @Nullable
    private final Map<String, String> extraArgs;
    @Nullable
    private final Integer maxBufferSize;
    @Nullable
    private final Object debugStderr;
    @Nullable
    private final Consumer<String> stderr;
    @Nullable
    private final List<SettingSource> settingSources;
    @Nullable
    private final List<SdkPluginConfig> plugins;

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
        // New fields
        this.cliPath = builder.cliPath;
        this.agents = builder.agents;
        this.disallowedTools = builder.disallowedTools;
        this.continueConversation = builder.continueConversation;
        this.resume = builder.resume;
        this.env = builder.env;
        this.addDirs = builder.addDirs;
        this.user = builder.user;
        this.includePartialMessages = builder.includePartialMessages;
        this.forkSession = builder.forkSession;
        // Additional fields
        this.permissionPromptToolName = builder.permissionPromptToolName;
        this.settings = builder.settings;
        this.extraArgs = builder.extraArgs;
        this.maxBufferSize = builder.maxBufferSize;
        this.debugStderr = builder.debugStderr;
        this.stderr = builder.stderr;
        this.settingSources = builder.settingSources;
        this.plugins = builder.plugins;
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
    public BiFunction<ToolUseBlock, ToolPermissionContext, PermissionResult> getCanUseTool() {
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

    @Nullable
    public Path getCliPath() {
        return cliPath;
    }

    @Nullable
    public Map<String, AgentDefinition> getAgents() {
        return agents;
    }

    @Nullable
    public List<String> getDisallowedTools() {
        return disallowedTools;
    }

    public boolean isContinueConversation() {
        return continueConversation;
    }

    @Nullable
    public String getResume() {
        return resume;
    }

    @Nullable
    public Map<String, String> getEnv() {
        return env;
    }

    @Nullable
    public List<Path> getAddDirs() {
        return addDirs;
    }

    @Nullable
    public String getUser() {
        return user;
    }

    public boolean isIncludePartialMessages() {
        return includePartialMessages;
    }

    public boolean isForkSession() {
        return forkSession;
    }

    @Nullable
    public String getPermissionPromptToolName() {
        return permissionPromptToolName;
    }

    @Nullable
    public String getSettings() {
        return settings;
    }

    @Nullable
    public Map<String, String> getExtraArgs() {
        return extraArgs;
    }

    @Nullable
    public Integer getMaxBufferSize() {
        return maxBufferSize;
    }

    @Nullable
    public Object getDebugStderr() {
        return debugStderr;
    }

    @Nullable
    public Consumer<String> getStderr() {
        return stderr;
    }

    @Nullable
    public List<SettingSource> getSettingSources() {
        return settingSources;
    }

    @Nullable
    public List<SdkPluginConfig> getPlugins() {
        return plugins;
    }

    public static class Builder {
        private List<String> allowedTools;
        private String systemPrompt;
        private Map<String, McpServerConfig> mcpServers;
        private PermissionMode permissionMode;
        private Integer maxTurns;
        private Double maxBudgetUsd;
        private BiFunction<ToolUseBlock, ToolPermissionContext, PermissionResult> canUseTool;
        private Map<String, List<HookMatcher>> hooks;
        private Path cwd;
        private String model;
        private Integer maxThinkingTokens;
        // New fields
        private Path cliPath;
        private Map<String, AgentDefinition> agents;
        private List<String> disallowedTools;
        private boolean continueConversation = false;
        private String resume;
        private Map<String, String> env;
        private List<Path> addDirs;
        private String user;
        private boolean includePartialMessages = false;
        private boolean forkSession = false;
        // Additional fields
        private String permissionPromptToolName;
        private String settings;
        private Map<String, String> extraArgs;
        private Integer maxBufferSize;
        private Object debugStderr;
        private Consumer<String> stderr;
        private List<SettingSource> settingSources;
        private List<SdkPluginConfig> plugins;

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

        public Builder canUseTool(BiFunction<ToolUseBlock, ToolPermissionContext, PermissionResult> canUseTool) {
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

        /**
         * Set the path to the Claude CLI executable.
         *
         * @param cliPath the CLI path
         * @return this builder
         */
        public Builder cliPath(Path cliPath) {
            this.cliPath = cliPath;
            return this;
        }

        /**
         * Set the path to the Claude CLI executable.
         *
         * @param cliPath the CLI path as a string
         * @return this builder
         */
        public Builder cliPath(String cliPath) {
            this.cliPath = Path.of(cliPath);
            return this;
        }

        /**
         * Set agent definitions.
         *
         * @param agents map of agent name to definition
         * @return this builder
         */
        public Builder agents(Map<String, AgentDefinition> agents) {
            this.agents = agents;
            return this;
        }

        /**
         * Set tools that should not be allowed.
         *
         * @param disallowedTools list of disallowed tool names
         * @return this builder
         */
        public Builder disallowedTools(List<String> disallowedTools) {
            this.disallowedTools = disallowedTools;
            return this;
        }

        /**
         * Set whether to continue an existing conversation.
         *
         * @param continueConversation true to continue conversation
         * @return this builder
         */
        public Builder continueConversation(boolean continueConversation) {
            this.continueConversation = continueConversation;
            return this;
        }

        /**
         * Set session ID to resume.
         *
         * @param resume session ID to resume
         * @return this builder
         */
        public Builder resume(String resume) {
            this.resume = resume;
            return this;
        }

        /**
         * Set environment variables for the CLI process.
         *
         * @param env environment variables
         * @return this builder
         */
        public Builder env(Map<String, String> env) {
            this.env = env;
            return this;
        }

        /**
         * Set additional directories to include.
         *
         * @param addDirs list of directories
         * @return this builder
         */
        public Builder addDirs(List<Path> addDirs) {
            this.addDirs = addDirs;
            return this;
        }

        /**
         * Set user identifier.
         *
         * @param user user identifier
         * @return this builder
         */
        public Builder user(String user) {
            this.user = user;
            return this;
        }

        /**
         * Set whether to include partial messages.
         *
         * @param includePartialMessages true to include partial messages
         * @return this builder
         */
        public Builder includePartialMessages(boolean includePartialMessages) {
            this.includePartialMessages = includePartialMessages;
            return this;
        }

        /**
         * Set whether to fork the session.
         *
         * @param forkSession true to fork session
         * @return this builder
         */
        public Builder forkSession(boolean forkSession) {
            this.forkSession = forkSession;
            return this;
        }

        /**
         * Set the tool name to use for permission prompts.
         *
         * @param permissionPromptToolName tool name for permission prompts
         * @return this builder
         */
        public Builder permissionPromptToolName(String permissionPromptToolName) {
            this.permissionPromptToolName = permissionPromptToolName;
            return this;
        }

        /**
         * Set the settings configuration path.
         *
         * @param settings settings path
         * @return this builder
         */
        public Builder settings(String settings) {
            this.settings = settings;
            return this;
        }

        /**
         * Set extra command-line arguments.
         *
         * @param extraArgs extra arguments map
         * @return this builder
         */
        public Builder extraArgs(Map<String, String> extraArgs) {
            this.extraArgs = extraArgs;
            return this;
        }

        /**
         * Set maximum buffer size for communication.
         *
         * @param maxBufferSize maximum buffer size in bytes
         * @return this builder
         */
        public Builder maxBufferSize(Integer maxBufferSize) {
            this.maxBufferSize = maxBufferSize;
            return this;
        }

        /**
         * Set debug stderr output stream or handler.
         *
         * @param debugStderr debug stderr handler
         * @return this builder
         */
        public Builder debugStderr(Object debugStderr) {
            this.debugStderr = debugStderr;
            return this;
        }

        /**
         * Set stderr message consumer.
         *
         * @param stderr stderr message consumer
         * @return this builder
         */
        public Builder stderr(Consumer<String> stderr) {
            this.stderr = stderr;
            return this;
        }

        /**
         * Set setting sources to use.
         *
         * @param settingSources list of setting sources
         * @return this builder
         */
        public Builder settingSources(List<SettingSource> settingSources) {
            this.settingSources = settingSources;
            return this;
        }

        /**
         * Set SDK plugins to load.
         *
         * @param plugins list of plugin configurations
         * @return this builder
         */
        public Builder plugins(List<SdkPluginConfig> plugins) {
            this.plugins = plugins;
            return this;
        }

        public ClaudeAgentOptions build() {
            return new ClaudeAgentOptions(this);
        }
    }
}
