package com.anthropic.claude.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * A thinking content block (extended thinking).
 */
public class ThinkingBlock implements ContentBlock {

    private final String thinking;
    private final String signature;

    @JsonCreator
    public ThinkingBlock(
            @JsonProperty("thinking") String thinking,
            @JsonProperty("signature") String signature) {
        this.thinking = thinking;
        this.signature = signature;
    }

    @Override
    public String getType() {
        return "thinking";
    }

    public String getThinking() {
        return thinking;
    }

    public String getSignature() {
        return signature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ThinkingBlock that = (ThinkingBlock) o;
        return Objects.equals(thinking, that.thinking) &&
               Objects.equals(signature, that.signature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(thinking, signature);
    }

    @Override
    public String toString() {
        return "ThinkingBlock{thinking='" + thinking + "', signature='" + signature + "'}";
    }
}
