package com.anthropic.claude.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

/**
 * A message from the user.
 */
public class UserMessage implements Message {

    private final List<ContentBlock> content;
    @Nullable
    private final String parentToolUseId;

    @JsonCreator
    public UserMessage(
            @JsonProperty("content") List<ContentBlock> content,
            @JsonProperty("parent_tool_use_id") @Nullable String parentToolUseId) {
        this.content = content;
        this.parentToolUseId = parentToolUseId;
    }

    public UserMessage(List<ContentBlock> content) {
        this(content, null);
    }

    @Override
    public String getRole() {
        return "user";
    }

    public List<ContentBlock> getContent() {
        return content;
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
        UserMessage that = (UserMessage) o;
        return Objects.equals(content, that.content) &&
               Objects.equals(parentToolUseId, that.parentToolUseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, parentToolUseId);
    }

    @Override
    public String toString() {
        return "UserMessage{content=" + content + ", parentToolUseId='" + parentToolUseId + "'}";
    }
}
