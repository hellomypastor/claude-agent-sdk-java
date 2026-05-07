package com.anthropic.claude.sdk.types.options;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Definition for a custom agent configuration.
 */
public final class AgentDefinition {

    private final String description;
    private final String prompt;
    private final List<String> tools;
    private final List<String> disallowedTools;
    private final String model;
    private final List<Object> mcpServers;
    private final String criticalSystemReminder;

    private AgentDefinition(Builder builder) {
        this.description = builder.description;
        this.prompt = builder.prompt;
        this.tools = builder.tools;
        this.disallowedTools = builder.disallowedTools;
        this.model = builder.model;
        this.mcpServers = builder.mcpServers;
        this.criticalSystemReminder = builder.criticalSystemReminder;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getDescription() {
        return description;
    }

    public String getPrompt() {
        return prompt;
    }

    public List<String> getTools() {
        return tools;
    }

    public List<String> getDisallowedTools() {
        return disallowedTools;
    }

    public String getModel() {
        return model;
    }

    public List<Object> getMcpServers() {
        return mcpServers;
    }

    public String getCriticalSystemReminder() {
        return criticalSystemReminder;
    }

    /**
     * Serialize definition for CLI consumption.
     */
    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        if (description != null) {
            result.put("description", description);
        }
        if (prompt != null) {
            result.put("prompt", prompt);
        }
        if (tools != null && !tools.isEmpty()) {
            result.put("tools", tools);
        }
        if (disallowedTools != null && !disallowedTools.isEmpty()) {
            result.put("disallowedTools", disallowedTools);
        }
        if (model != null) {
            result.put("model", model);
        }
        if (mcpServers != null && !mcpServers.isEmpty()) {
            result.put("mcpServers", mcpServers);
        }
        if (criticalSystemReminder != null) {
            result.put("criticalSystemReminder", criticalSystemReminder);
        }
        return result;
    }

    public static final class Builder {
        private String description;
        private String prompt;
        private List<String> tools = Collections.emptyList();
        private List<String> disallowedTools = Collections.emptyList();
        private String model;
        private List<Object> mcpServers = Collections.emptyList();
        private String criticalSystemReminder;

        private Builder() {
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder prompt(String prompt) {
            this.prompt = prompt;
            return this;
        }

        public Builder tools(List<String> tools) {
            this.tools = tools != null ? tools : Collections.emptyList();
            return this;
        }

        public Builder disallowedTools(List<String> disallowedTools) {
            this.disallowedTools = disallowedTools != null ? disallowedTools : Collections.emptyList();
            return this;
        }

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder mcpServers(List<Object> mcpServers) {
            this.mcpServers = mcpServers != null ? mcpServers : Collections.emptyList();
            return this;
        }

        public Builder criticalSystemReminder(String criticalSystemReminder) {
            this.criticalSystemReminder = criticalSystemReminder;
            return this;
        }

        public AgentDefinition build() {
            return new AgentDefinition(this);
        }
    }
}
