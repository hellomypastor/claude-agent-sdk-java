package com.anthropic.claude.sdk;

/**
 * Information about an available model.
 */
public final class ModelInfo {

    private final String value;
    private final String displayName;
    private final String description;

    public ModelInfo(String value, String displayName, String description) {
        this.value = value;
        this.displayName = displayName;
        this.description = description;
    }

    public String value() {
        return value;
    }

    public String displayName() {
        return displayName;
    }

    public String description() {
        return description;
    }
}
