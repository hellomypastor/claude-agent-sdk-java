package com.anthropic.claude.sdk.types.content;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Thinking content block (extended thinking feature).
 */
public record ThinkingBlock(
        @JsonProperty("thinking") String thinking,
        @JsonProperty("signature") String signature
) implements ContentBlock {

    @JsonCreator
    public ThinkingBlock {
    }

    @Override
    public String getType() {
        return "thinking";
    }
}
