package com.anthropic.claude.sdk.types.content;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Thinking content block (extended thinking feature).
 */
@Data
@AllArgsConstructor
public final class ThinkingBlock implements ContentBlock {
    @JsonProperty("thinking")
    private final String thinking;

    @JsonProperty("signature")
    private final String signature;

    @Override
    public String getType() {
        return "thinking";
    }
}
