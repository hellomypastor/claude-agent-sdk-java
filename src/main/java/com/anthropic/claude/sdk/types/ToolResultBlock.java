package com.anthropic.claude.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

/**
 * A tool result content block.
 */
public class ToolResultBlock implements ContentBlock {

    private final String toolUseId;
    private final List<ContentBlock> content;
    private final boolean isError;

    @JsonCreator
    public ToolResultBlock(
            @JsonProperty("tool_use_id") String toolUseId,
            @JsonProperty("content") @Nullable List<ContentBlock> content,
            @JsonProperty("is_error") boolean isError) {
        this.toolUseId = toolUseId;
        this.content = content;
        this.isError = isError;
    }

    @Override
    public String getType() {
        return "tool_result";
    }

    @JsonProperty("tool_use_id")
    public String getToolUseId() {
        return toolUseId;
    }

    @Nullable
    public List<ContentBlock> getContent() {
        return content;
    }

    @JsonProperty("is_error")
    public boolean isError() {
        return isError;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ToolResultBlock that = (ToolResultBlock) o;
        return isError == that.isError &&
               Objects.equals(toolUseId, that.toolUseId) &&
               Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(toolUseId, content, isError);
    }

    @Override
    public String toString() {
        return "ToolResultBlock{toolUseId='" + toolUseId + "', content=" + content + ", isError=" + isError + "}";
    }
}
