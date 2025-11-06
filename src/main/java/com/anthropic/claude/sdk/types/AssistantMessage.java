package com.anthropic.claude.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

/**
 * A message from the assistant.
 */
public class AssistantMessage implements Message {

    private final List<ContentBlock> content;
    @Nullable
    private final String model;
    @Nullable
    private final String parentToolUseId;

    @JsonCreator
    public AssistantMessage(
            @JsonProperty("content") List<ContentBlock> content,
            @JsonProperty("model") @Nullable String model,
            @JsonProperty("parent_tool_use_id") @Nullable String parentToolUseId) {
        this.content = content;
        this.model = model;
        this.parentToolUseId = parentToolUseId;
    }

    public AssistantMessage(List<ContentBlock> content) {
        this(content, null, null);
    }

    @Override
    public String getRole() {
        return "assistant";
    }

    public List<ContentBlock> getContent() {
        return content;
    }

    @Nullable
    public String getModel() {
        return model;
    }

    @Nullable
    @JsonProperty("parent_tool_use_id")
    public String getParentToolUseId() {
        return parentToolUseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssistantMessage that = (AssistantMessage) o;
        return Objects.equals(content, that.content) &&
               Objects.equals(model, that.model) &&
               Objects.equals(parentToolUseId, that.parentToolUseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, model, parentToolUseId);
    }

    @Override
    public String toString() {
        return "AssistantMessage{content=" + content + ", model='" + model + "', parentToolUseId='" + parentToolUseId + "'}";
    }
}
