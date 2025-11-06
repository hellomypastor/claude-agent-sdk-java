package com.anthropic.claude.sdk.types;

/**
 * Permission behavior for tool usage.
 */
public enum PermissionBehavior {
    ALLOW("allow"),
    DENY("deny"),
    ASK("ask");

    private final String value;

    PermissionBehavior(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
