package com.anthropic.claude.sdk.types;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Destination for permission updates.
 */
public enum PermissionUpdateDestination {
    USER_SETTINGS("userSettings"),
    PROJECT_SETTINGS("projectSettings"),
    LOCAL_SETTINGS("localSettings"),
    SESSION("session");

    private final String value;

    PermissionUpdateDestination(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public static PermissionUpdateDestination fromString(String value) {
        for (PermissionUpdateDestination dest : PermissionUpdateDestination.values()) {
            if (dest.value.equals(value)) {
                return dest;
            }
        }
        throw new IllegalArgumentException("Unknown PermissionUpdateDestination: " + value);
    }
}
