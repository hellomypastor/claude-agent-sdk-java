package com.anthropic.claude.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * A text content block.
 */
public class TextBlock implements ContentBlock {

    private final String text;

    @JsonCreator
    public TextBlock(@JsonProperty("text") String text) {
        this.text = text;
    }

    @Override
    public String getType() {
        return "text";
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextBlock textBlock = (TextBlock) o;
        return Objects.equals(text, textBlock.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }

    @Override
    public String toString() {
        return "TextBlock{text='" + text + "'}";
    }
}
