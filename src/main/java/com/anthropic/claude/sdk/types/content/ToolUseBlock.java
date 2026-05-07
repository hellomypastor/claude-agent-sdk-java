package com.anthropic.claude.sdk.types.content;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Tool use content block.
 */
public record ToolUseBlock(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("input") Map<String, Object> input
) implements ContentBlock {

    @JsonCreator
    public ToolUseBlock {
    }

    @Override
    public String getType() {
        return "tool_use";
    }
}
