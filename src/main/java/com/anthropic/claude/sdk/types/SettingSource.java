package com.anthropic.claude.sdk.types;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Sources from which settings can be loaded.
 */
public enum SettingSource {
    USER("user"),
    PROJECT("project"),
    LOCAL("local");

    private final String value;

    SettingSource(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public static SettingSource fromString(String value) {
        for (SettingSource source : SettingSource.values()) {
            if (source.value.equals(value)) {
                return source;
            }
        }
        throw new IllegalArgumentException("Unknown SettingSource: " + value);
    }
}
