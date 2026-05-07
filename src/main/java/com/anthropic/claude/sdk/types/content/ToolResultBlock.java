package com.anthropic.claude.sdk.types.content;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Tool result content block.
 */
public record ToolResultBlock(
        @JsonProperty("tool_use_id") String toolUseId,
        @JsonProperty("content") List<Object> content,
        @JsonProperty("is_error") Boolean isError
) implements ContentBlock {

    @JsonCreator
    public ToolResultBlock {
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
