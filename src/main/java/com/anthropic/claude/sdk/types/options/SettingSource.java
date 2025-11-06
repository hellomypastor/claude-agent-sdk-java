package com.anthropic.claude.sdk.types.options;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Sources for settings configuration.
 */
public enum SettingSource {
    /**
     * Global settings.
     */
    GLOBAL("global"),

    /**
     * Project-specific settings.
     */
    PROJECT("project"),

    /**
     * User-specific settings.
     */
    USER("user");

    private final String value;

    SettingSource(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public static SettingSource fromValue(String value) {
        for (SettingSource source : values()) {
            if (source.value.equals(value)) {
                return source;
            }
        }
        throw new IllegalArgumentException("Unknown setting source: " + value);
    }
}
