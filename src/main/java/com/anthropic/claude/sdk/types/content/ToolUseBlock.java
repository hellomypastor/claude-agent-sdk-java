package com.anthropic.claude.sdk.types.content;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Tool use content block.
 */
public final class ToolUseBlock implements ContentBlock {

    private final String id;
    private final String name;
    private final Map<String, Object> input;

    @JsonCreator
    public ToolUseBlock(
            @JsonProperty("id") String id,
            @JsonProperty("name") String name,
            @JsonProperty("input") Map<String, Object> input
    ) {
        this.id = id;
        this.name = name;
        this.input = input;
    }

    @JsonProperty("id")
    public String id() {
        return id;
    }

    @JsonProperty("name")
    public String name() {
        return name;
    }

    @JsonProperty("input")
    public Map<String, Object> input() {
        return input;
    }

    @Override
    public String getType() {
        return "tool_use";
    }
}
