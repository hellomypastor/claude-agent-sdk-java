package com.anthropic.claude.sdk.types.content;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

/**
 * Tool use content block.
 */
@Data
@AllArgsConstructor
public final class ToolUseBlock implements ContentBlock {
    @JsonProperty("id")
    private final String id;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("input")
    private final Map<String, Object> input;

    @Override
    public String getType() {
        return "tool_use";
    }
}
