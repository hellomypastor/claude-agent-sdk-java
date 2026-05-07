package com.anthropic.claude.sdk.types.content;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Text content block.
 */
public final class TextBlock implements ContentBlock {

    private final String text;

    @JsonCreator
    public TextBlock(
            @JsonProperty("text") String text
    ) {
        this.text = text;
    }

    @JsonProperty("text")
    public String text() {
        return text;
    }

    @Override
    public String getType() {
        return "text";
    }
}
