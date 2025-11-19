package com.anthropic.claude.sdk.types.options;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Definition for a custom agent configuration.
 */
@Getter
@Builder
public class AgentDefinition {
    private final String description;
    private final String prompt;

    @Singular
    private final List<String> tools;

    /**
     * Preferred model for this agent. Expected values:
     * sonnet / opus / haiku / inherit.
     */
    private final String model;

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
        if (model != null) {
            result.put("model", model);
        }
        return result;
    }
}
