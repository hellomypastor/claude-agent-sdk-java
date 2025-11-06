package com.anthropic.claude.sdk.types.options;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Permission mode for tool execution.
 */
public enum PermissionMode {
    /**
     * Prompt for permission for each tool use.
     */
    PROMPT("prompt"),

    /**
     * Automatically accept file edits.
     */
    ACCEPT_EDITS("acceptEdits"),

    /**
     * Automatically accept all tool uses.
     */
    ACCEPT_ALL("acceptAll");

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
