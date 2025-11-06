package com.anthropic.claude.sdk.types;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

/**
 * Definition for a Claude agent configuration.
 */
public class AgentDefinition {

    private final String description;
    private final String prompt;
    @Nullable
    private final List<String> tools;
    @Nullable
    private final String model;

    private AgentDefinition(Builder builder) {
        this.description = builder.description;
        this.prompt = builder.prompt;
        this.tools = builder.tools;
        this.model = builder.model;
    }

    public static Builder builder(String description, String prompt) {
        return new Builder(description, prompt);
    }

    public String getDescription() {
        return description;
    }

    public String getPrompt() {
        return prompt;
    }

    @Nullable
    public List<String> getTools() {
        return tools;
    }

    @Nullable
    public String getModel() {
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgentDefinition that = (AgentDefinition) o;
        return Objects.equals(description, that.description) &&
               Objects.equals(prompt, that.prompt) &&
               Objects.equals(tools, that.tools) &&
               Objects.equals(model, that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, prompt, tools, model);
    }

    @Override
    public String toString() {
        return "AgentDefinition{description='" + description + "', model='" + model + "'}";
    }

    public static class Builder {
        private final String description;
        private final String prompt;
        private List<String> tools;
        private String model;

        private Builder(String description, String prompt) {
            this.description = description;
            this.prompt = prompt;
        }

        public Builder tools(List<String> tools) {
            this.tools = tools;
            return this;
        }

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public AgentDefinition build() {
            return new AgentDefinition(this);
        }
    }
}
