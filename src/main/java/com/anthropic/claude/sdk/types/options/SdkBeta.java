package com.anthropic.claude.sdk.types.options;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * SDK beta feature flags.
 */
public enum SdkBeta {
    CONTEXT_1M("context-1m-2025-08-07");

    private final String value;

    SdkBeta(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
