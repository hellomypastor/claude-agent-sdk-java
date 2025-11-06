package com.anthropic.claude.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Objects;

/**
 * A tool use content block.
 */
public class ToolUseBlock implements ContentBlock {

    private final String id;
    private final String name;
    private final Map<String, Object> input;

    @JsonCreator
    public ToolUseBlock(
            @JsonProperty("id") String id,
            @JsonProperty("name") String name,
            @JsonProperty("input") Map<String, Object> input) {
        this.id = id;
        this.name = name;
        this.input = input;
    }

    @Override
    public String getType() {
        return "tool_use";
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Map<String, Object> getInput() {
        return input;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ToolUseBlock that = (ToolUseBlock) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(name, that.name) &&
               Objects.equals(input, that.input);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, input);
    }

    @Override
    public String toString() {
        return "ToolUseBlock{id='" + id + "', name='" + name + "', input=" + input + "}";
    }
}
