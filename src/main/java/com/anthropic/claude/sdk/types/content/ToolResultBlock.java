package com.anthropic.claude.sdk.types.content;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Tool result content block.
 */
@Data
@AllArgsConstructor
public final class ToolResultBlock implements ContentBlock {
    @JsonProperty("tool_use_id")
    private final String toolUseId;

    @JsonProperty("content")
    private final List<Object> content;

    @JsonProperty("is_error")
    private final Boolean isError;

    @Override
    public String getType() {
        return "tool_result";
    }

    /**
     * Convenience method to check if this is an error result.
     */
    public boolean isError() {
        return isError != null && isError;
    }
}
