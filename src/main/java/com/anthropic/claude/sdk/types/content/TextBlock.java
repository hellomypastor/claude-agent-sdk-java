package com.anthropic.claude.sdk.types.content;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Text content block.
 */
public record TextBlock(
        @JsonProperty("text") String text
) implements ContentBlock {

    @JsonCreator
    public TextBlock {
    }

    @Override
    public String getType() {
        return "text";
    }
}
