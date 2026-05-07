package com.anthropic.claude.sdk.types.content;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Thinking content block (extended thinking feature).
 */
public final class ThinkingBlock implements ContentBlock {

    private final String thinking;
    private final String signature;

    @JsonCreator
    public ThinkingBlock(
            @JsonProperty("thinking") String thinking,
            @JsonProperty("signature") String signature
    ) {
        this.thinking = thinking;
        this.signature = signature;
    }

    @JsonProperty("thinking")
    public String thinking() {
        return thinking;
    }

    @JsonProperty("signature")
    public String signature() {
        return signature;
    }

    @Override
    public String getType() {
        return "thinking";
    }
}
