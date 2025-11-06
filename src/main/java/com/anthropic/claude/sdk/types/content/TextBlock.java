package com.anthropic.claude.sdk.types.content;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Text content block.
 */
@Data
@AllArgsConstructor
public final class TextBlock implements ContentBlock {
    @JsonProperty("text")
    private final String text;

    @Override
    public String getType() {
        return "text";
    }
}
