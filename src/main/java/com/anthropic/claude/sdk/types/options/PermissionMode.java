package com.anthropic.claude.sdk.types.options;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Permission mode for tool execution.
 */
public enum PermissionMode {
    DEFAULT("default"),
    ACCEPT_EDITS("acceptEdits"),
    BYPASS_PERMISSIONS("bypassPermissions"),
    PLAN("plan"),
    DELEGATE("delegate"),
    DONT_ASK("dontAsk");

    private final String value;

    PermissionMode(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public static PermissionMode fromValue(String value) {
        for (PermissionMode mode : values()) {
            if (mode.value.equals(value)) {
                return mode;
            }
        }
        throw new IllegalArgumentException("Unknown permission mode: " + value);
    }
}
