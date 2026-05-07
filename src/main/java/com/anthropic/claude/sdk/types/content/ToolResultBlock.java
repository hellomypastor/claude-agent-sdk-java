package com.anthropic.claude.sdk.types.content;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Tool result content block.
 */
public final class ToolResultBlock implements ContentBlock {

    private final String toolUseId;
    private final List<Object> content;
    private final Boolean isError;

    @JsonCreator
    public ToolResultBlock(
            @JsonProperty("tool_use_id") String toolUseId,
            @JsonProperty("content") List<Object> content,
            @JsonProperty("is_error") Boolean isError
    ) {
        this.toolUseId = toolUseId;
        this.content = content;
        this.isError = isError;
    }

    @JsonProperty("tool_use_id")
    public String toolUseId() {
        return toolUseId;
    }

    @JsonProperty("content")
    public List<Object> content() {
        return content;
    }

    @JsonProperty("is_error")
    public Boolean isError() {
        return isError;
    }

    @Override
    public String getType() {
        return "tool_result";
    }

    /**
     * Convenience method to check if this is an error result.
     */
    public boolean hasError() {
        return isError != null && isError;
    }
}
