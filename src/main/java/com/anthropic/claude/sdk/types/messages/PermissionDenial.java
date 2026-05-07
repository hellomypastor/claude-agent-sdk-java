package com.anthropic.claude.sdk.types.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Record of a permission denial during session execution.
 */
public final class PermissionDenial {

    private final String toolName;
    private final String toolUseId;
    private final Map<String, Object> toolInput;

    public PermissionDenial(
            @JsonProperty("tool_name") String toolName,
            @JsonProperty("tool_use_id") String toolUseId,
            @JsonProperty("tool_input") Map<String, Object> toolInput
    ) {
        this.toolName = toolName;
        this.toolUseId = toolUseId;
        this.toolInput = toolInput;
    }

    @JsonProperty("tool_name")
    public String toolName() {
        return toolName;
    }

    @JsonProperty("tool_use_id")
    public String toolUseId() {
        return toolUseId;
    }

    @JsonProperty("tool_input")
    public Map<String, Object> toolInput() {
        return toolInput;
    }
}
