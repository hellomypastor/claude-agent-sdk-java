package com.anthropic.claude.sdk.types.options;

import com.anthropic.claude.sdk.session.SessionStore;
import com.anthropic.claude.sdk.transport.SpawnProcessFunction;
import com.anthropic.claude.sdk.types.hooks.HookCallbackMatcher;
import com.anthropic.claude.sdk.types.permissions.ToolPermissionCallback;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Configuration options for Claude Agent SDK.
 * Use {@link #builder()} to create instances.
 */
public final class ClaudeAgentOptions {

    private final String systemPrompt;
    private final Object systemPromptPreset;
    private final List<String> allowedTools;
    private final List<String> disallowedTools;
    private final List<String> tools;
    private final Integer maxTurns;
    private final Double maxBudgetUsd;
    private final String model;
    private final String fallbackModel;
    private final PermissionMode permissionMode;
    private final Boolean allowDangerouslySkipPermissions;
    private final String permissionPromptToolName;
    private final ToolPermissionCallback canUseTool;
    private final Map<String, List<HookCallbackMatcher>> hooks;
    private final Path cliPath;
    private final Path cwd;
    private final Map<String, Object> mcpServers;
    private final Map<String, String> env;
    private final List<Path> addDirs;
    private final boolean continueConversation;
    private final String resume;
    private final String resumeSessionAt;
    private final String settings;
    private final String user;
    private final List<SettingSource> settingSources;
    private final Integer maxThinkingTokens;
    private final Map<String, String> extraArgs;
    private final boolean includePartialMessages;
    private final boolean forkSession;
    private final boolean persistSession;
    private final boolean enableFileCheckpointing;
    private final Boolean strictMcpConfig;
    private final Integer maxBufferSize;
    private final Map<String, AgentDefinition> agents;
    private final List<SdkPluginConfig> plugins;
    private final Map<String, Object> outputFormat;
    private final Consumer<String> stderr;
    private final List<SdkBeta> betas;
    private final SandboxSettings sandbox;
    private final SpawnProcessFunction spawnClaudeCodeProcess;
    private final String executable;
    private final List<String> executableArgs;
    private final SessionStore sessionStore;

    private ClaudeAgentOptions(Builder builder) {
        this.systemPrompt = builder.systemPrompt;
        this.systemPromptPreset = builder.systemPromptPreset;
        this.allowedTools = Collections.unmodifiableList(builder.allowedTools);
        this.disallowedTools = Collections.unmodifiableList(builder.disallowedTools);
        this.tools = builder.tools != null ? Collections.unmodifiableList(builder.tools) : null;
        this.maxTurns = builder.maxTurns;
        this.maxBudgetUsd = builder.maxBudgetUsd;
        this.model = builder.model;
        this.fallbackModel = builder.fallbackModel;
        this.permissionMode = builder.permissionMode;
        this.allowDangerouslySkipPermissions = builder.allowDangerouslySkipPermissions;
        this.permissionPromptToolName = builder.permissionPromptToolName;
        this.canUseTool = builder.canUseTool;
        this.hooks = Collections.unmodifiableMap(builder.hooks);
        this.cliPath = builder.cliPath;
        this.cwd = builder.cwd;
        this.mcpServers = Collections.unmodifiableMap(builder.mcpServers);
        this.env = Collections.unmodifiableMap(builder.env);
        this.addDirs = Collections.unmodifiableList(builder.addDirs);
        this.continueConversation = builder.continueConversation;
        this.resume = builder.resume;
        this.resumeSessionAt = builder.resumeSessionAt;
        this.settings = builder.settings;
        this.user = builder.user;
        this.settingSources = Collections.unmodifiableList(builder.settingSources);
        this.maxThinkingTokens = builder.maxThinkingTokens;
        this.extraArgs = Collections.unmodifiableMap(builder.extraArgs);
        this.includePartialMessages = builder.includePartialMessages;
        this.forkSession = builder.forkSession;
        this.persistSession = builder.persistSession;
        this.enableFileCheckpointing = builder.enableFileCheckpointing;
        this.strictMcpConfig = builder.strictMcpConfig;
        this.maxBufferSize = builder.maxBufferSize;
        this.agents = Collections.unmodifiableMap(builder.agents);
        this.plugins = Collections.unmodifiableList(builder.plugins);
        this.outputFormat = builder.outputFormat;
        this.stderr = builder.stderr;
        this.betas = builder.betas;
        this.sandbox = builder.sandbox;
        this.spawnClaudeCodeProcess = builder.spawnClaudeCodeProcess;
        this.executable = builder.executable;
        this.executableArgs = builder.executableArgs;
        this.sessionStore = builder.sessionStore;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Create a new builder pre-populated with this instance's values.
     */
    public Builder toBuilder() {
        Builder b = new Builder();
        b.systemPrompt = this.systemPrompt;
        b.systemPromptPreset = this.systemPromptPreset;
        b.allowedTools = new ArrayList<>(this.allowedTools);
        b.disallowedTools = new ArrayList<>(this.disallowedTools);
        b.tools = this.tools != null ? new ArrayList<>(this.tools) : null;
        b.maxTurns = this.maxTurns;
        b.maxBudgetUsd = this.maxBudgetUsd;
        b.model = this.model;
        b.fallbackModel = this.fallbackModel;
        b.permissionMode = this.permissionMode;
        b.allowDangerouslySkipPermissions = this.allowDangerouslySkipPermissions;
        b.permissionPromptToolName = this.permissionPromptToolName;
        b.canUseTool = this.canUseTool;
        b.hooks = new HashMap<>(this.hooks);
        b.cliPath = this.cliPath;
        b.cwd = this.cwd;
        b.mcpServers = new HashMap<>(this.mcpServers);
        b.env = new HashMap<>(this.env);
        b.addDirs = new ArrayList<>(this.addDirs);
        b.continueConversation = this.continueConversation;
        b.resume = this.resume;
        b.resumeSessionAt = this.resumeSessionAt;
        b.settings = this.settings;
        b.user = this.user;
        b.settingSources = new ArrayList<>(this.settingSources);
        b.maxThinkingTokens = this.maxThinkingTokens;
        b.extraArgs = new HashMap<>(this.extraArgs);
        b.includePartialMessages = this.includePartialMessages;
        b.forkSession = this.forkSession;
        b.persistSession = this.persistSession;
        b.enableFileCheckpointing = this.enableFileCheckpointing;
        b.strictMcpConfig = this.strictMcpConfig;
        b.maxBufferSize = this.maxBufferSize;
        b.agents = new HashMap<>(this.agents);
        b.plugins = new ArrayList<>(this.plugins);
        b.outputFormat = this.outputFormat;
        b.stderr = this.stderr;
        b.betas = this.betas;
        b.sandbox = this.sandbox;
        b.spawnClaudeCodeProcess = this.spawnClaudeCodeProcess;
        b.executable = this.executable;
        b.executableArgs = this.executableArgs;
        b.sessionStore = this.sessionStore;
        return b;
    }

    // --- Getters ---

    public String getSystemPrompt() {
        return systemPrompt;
    }

    public Object getSystemPromptPreset() {
        return systemPromptPreset;
    }

    public List<String> getAllowedTools() {
        return allowedTools;
    }

    public List<String> getDisallowedTools() {
        return disallowedTools;
    }

    public List<String> getTools() {
        return tools;
    }

    public Integer getMaxTurns() {
        return maxTurns;
    }

    public Double getMaxBudgetUsd() {
        return maxBudgetUsd;
    }

    public String getModel() {
        return model;
    }

    public String getFallbackModel() {
        return fallbackModel;
    }

    public PermissionMode getPermissionMode() {
        return permissionMode;
    }

    public Boolean getAllowDangerouslySkipPermissions() {
        return allowDangerouslySkipPermissions;
    }

    public String getPermissionPromptToolName() {
        return permissionPromptToolName;
    }

    public ToolPermissionCallback getCanUseTool() {
        return canUseTool;
    }

    public Map<String, List<HookCallbackMatcher>> getHooks() {
        return hooks;
    }

    public Path getCliPath() {
        return cliPath;
    }

    public Path getCwd() {
        return cwd;
    }

    public Map<String, Object> getMcpServers() {
        return mcpServers;
    }

    public Map<String, String> getEnv() {
        return env;
    }

    public List<Path> getAddDirs() {
        return addDirs;
    }

    public boolean isContinueConversation() {
        return continueConversation;
    }

    public String getResume() {
        return resume;
    }

    public String getResumeSessionAt() {
        return resumeSessionAt;
    }

    public String getSettings() {
        return settings;
    }

    public String getUser() {
        return user;
    }

    public List<SettingSource> getSettingSources() {
        return settingSources;
    }

    public Integer getMaxThinkingTokens() {
        return maxThinkingTokens;
    }

    public Map<String, String> getExtraArgs() {
        return extraArgs;
    }

    public boolean isIncludePartialMessages() {
        return includePartialMessages;
    }

    public boolean isForkSession() {
        return forkSession;
    }

    public boolean isPersistSession() {
        return persistSession;
    }

    public boolean isEnableFileCheckpointing() {
        return enableFileCheckpointing;
    }

    public Boolean getStrictMcpConfig() {
        return strictMcpConfig;
    }

    public Integer getMaxBufferSize() {
        return maxBufferSize;
    }

    public Map<String, AgentDefinition> getAgents() {
        return agents;
    }

    public List<SdkPluginConfig> getPlugins() {
        return plugins;
    }

    public Map<String, Object> getOutputFormat() {
        return outputFormat;
    }

    public Consumer<String> getStderr() {
        return stderr;
    }

    public List<SdkBeta> getBetas() {
        return betas;
    }

    public SandboxSettings getSandbox() {
        return sandbox;
    }

    public SpawnProcessFunction getSpawnClaudeCodeProcess() {
        return spawnClaudeCodeProcess;
    }

    public String getExecutable() {
        return executable;
    }

    public List<String> getExecutableArgs() {
        return executableArgs;
    }

    public SessionStore getSessionStore() {
        return sessionStore;
    }

    /**
     * Merge hooks into a single resolved map.
     */
    public Map<String, List<HookCallbackMatcher>> resolvedHooks() {
        return new HashMap<>(hooks);
    }

    // --- Builder ---

    public static final class Builder {
        private String systemPrompt;
        private Object systemPromptPreset;
        private List<String> allowedTools = new ArrayList<>();
        private List<String> disallowedTools = new ArrayList<>();
        private List<String> tools;
        private Integer maxTurns;
        private Double maxBudgetUsd;
        private String model;
        private String fallbackModel;
        private PermissionMode permissionMode;
        private Boolean allowDangerouslySkipPermissions;
        private String permissionPromptToolName;
        private ToolPermissionCallback canUseTool;
        private Map<String, List<HookCallbackMatcher>> hooks = new HashMap<>();
        private Path cliPath;
        private Path cwd;
        private Map<String, Object> mcpServers = new HashMap<>();
        private Map<String, String> env = new HashMap<>();
        private List<Path> addDirs = new ArrayList<>();
        private boolean continueConversation = false;
        private String resume;
        private String resumeSessionAt;
        private String settings;
        private String user;
        private List<SettingSource> settingSources = new ArrayList<>();
        private Integer maxThinkingTokens;
        private Map<String, String> extraArgs = new HashMap<>();
        private boolean includePartialMessages = false;
        private boolean forkSession = false;
        private boolean persistSession = true;
        private boolean enableFileCheckpointing = false;
        private Boolean strictMcpConfig;
        private Integer maxBufferSize;
        private Map<String, AgentDefinition> agents = new HashMap<>();
        private List<SdkPluginConfig> plugins = new ArrayList<>();
        private Map<String, Object> outputFormat;
        private Consumer<String> stderr;
        private List<SdkBeta> betas;
        private SandboxSettings sandbox;
        private SpawnProcessFunction spawnClaudeCodeProcess;
        private String executable;
        private List<String> executableArgs;
        private SessionStore sessionStore;

        private Builder() {
        }

        public Builder systemPrompt(String systemPrompt) {
            this.systemPrompt = systemPrompt;
            return this;
        }

        public Builder systemPromptPreset(Object systemPromptPreset) {
            this.systemPromptPreset = systemPromptPreset;
            return this;
        }

        public Builder allowedTools(List<String> allowedTools) {
            this.allowedTools = allowedTools != null ? new ArrayList<>(allowedTools) : new ArrayList<>();
            return this;
        }

        public Builder disallowedTools(List<String> disallowedTools) {
            this.disallowedTools = disallowedTools != null ? new ArrayList<>(disallowedTools) : new ArrayList<>();
            return this;
        }

        public Builder tools(List<String> tools) {
            this.tools = tools;
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

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder fallbackModel(String fallbackModel) {
            this.fallbackModel = fallbackModel;
            return this;
        }

        public Builder permissionMode(PermissionMode permissionMode) {
            this.permissionMode = permissionMode;
            return this;
        }

        public Builder allowDangerouslySkipPermissions(Boolean allowDangerouslySkipPermissions) {
            this.allowDangerouslySkipPermissions = allowDangerouslySkipPermissions;
            return this;
        }

        public Builder permissionPromptToolName(String permissionPromptToolName) {
            this.permissionPromptToolName = permissionPromptToolName;
            return this;
        }

        public Builder canUseTool(ToolPermissionCallback canUseTool) {
            this.canUseTool = canUseTool;
            return this;
        }

        public Builder hooks(Map<String, List<HookCallbackMatcher>> hooks) {
            this.hooks = hooks != null ? new HashMap<>(hooks) : new HashMap<>();
            return this;
        }

        public Builder cliPath(Path cliPath) {
            this.cliPath = cliPath;
            return this;
        }

        public Builder cwd(Path cwd) {
            this.cwd = cwd;
            return this;
        }

        public Builder mcpServers(Map<String, Object> mcpServers) {
            this.mcpServers = mcpServers != null ? new HashMap<>(mcpServers) : new HashMap<>();
            return this;
        }

        public Builder env(Map<String, String> env) {
            this.env = env != null ? new HashMap<>(env) : new HashMap<>();
            return this;
        }

        public Builder addDirs(List<Path> addDirs) {
            this.addDirs = addDirs != null ? new ArrayList<>(addDirs) : new ArrayList<>();
            return this;
        }

        public Builder continueConversation(boolean continueConversation) {
            this.continueConversation = continueConversation;
            return this;
        }

        public Builder resume(String resume) {
            this.resume = resume;
            return this;
        }

        public Builder resumeSessionAt(String resumeSessionAt) {
            this.resumeSessionAt = resumeSessionAt;
            return this;
        }

        public Builder settings(String settings) {
            this.settings = settings;
            return this;
        }

        public Builder user(String user) {
            this.user = user;
            return this;
        }

        public Builder settingSources(List<SettingSource> settingSources) {
            this.settingSources = settingSources != null ? new ArrayList<>(settingSources) : new ArrayList<>();
            return this;
        }

        public Builder maxThinkingTokens(Integer maxThinkingTokens) {
            this.maxThinkingTokens = maxThinkingTokens;
            return this;
        }

        public Builder extraArgs(Map<String, String> extraArgs) {
            this.extraArgs = extraArgs != null ? new HashMap<>(extraArgs) : new HashMap<>();
            return this;
        }

        public Builder includePartialMessages(boolean includePartialMessages) {
            this.includePartialMessages = includePartialMessages;
            return this;
        }

        public Builder forkSession(boolean forkSession) {
            this.forkSession = forkSession;
            return this;
        }

        public Builder persistSession(boolean persistSession) {
            this.persistSession = persistSession;
            return this;
        }

        public Builder enableFileCheckpointing(boolean enableFileCheckpointing) {
            this.enableFileCheckpointing = enableFileCheckpointing;
            return this;
        }

        public Builder strictMcpConfig(Boolean strictMcpConfig) {
            this.strictMcpConfig = strictMcpConfig;
            return this;
        }

        public Builder maxBufferSize(Integer maxBufferSize) {
            this.maxBufferSize = maxBufferSize;
            return this;
        }

        public Builder agents(Map<String, AgentDefinition> agents) {
            this.agents = agents != null ? new HashMap<>(agents) : new HashMap<>();
            return this;
        }

        public Builder plugins(List<SdkPluginConfig> plugins) {
            this.plugins = plugins != null ? new ArrayList<>(plugins) : new ArrayList<>();
            return this;
        }

        public Builder outputFormat(Map<String, Object> outputFormat) {
            this.outputFormat = outputFormat;
            return this;
        }

        public Builder stderr(Consumer<String> stderr) {
            this.stderr = stderr;
            return this;
        }

        public Builder betas(List<SdkBeta> betas) {
            this.betas = betas;
            return this;
        }

        public Builder sandbox(SandboxSettings sandbox) {
            this.sandbox = sandbox;
            return this;
        }

        public Builder spawnClaudeCodeProcess(SpawnProcessFunction spawnClaudeCodeProcess) {
            this.spawnClaudeCodeProcess = spawnClaudeCodeProcess;
            return this;
        }

        public Builder executable(String executable) {
            this.executable = executable;
            return this;
        }

        public Builder executableArgs(List<String> executableArgs) {
            this.executableArgs = executableArgs;
            return this;
        }

        public Builder sessionStore(SessionStore sessionStore) {
            this.sessionStore = sessionStore;
            return this;
        }

        public ClaudeAgentOptions build() {
            return new ClaudeAgentOptions(this);
        }
    }
}
