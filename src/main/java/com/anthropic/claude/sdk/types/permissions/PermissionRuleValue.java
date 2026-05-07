package com.anthropic.claude.sdk.types.permissions;

/**
 * A permission rule value consisting of a tool name and rule content.
 */
public final class PermissionRuleValue {
    private final String toolName;
    private final String ruleContent;

    public PermissionRuleValue(String toolName, String ruleContent) {
        this.toolName = toolName;
        this.ruleContent = ruleContent;
    }

    public String toolName() {
        return toolName;
    }

    public String ruleContent() {
        return ruleContent;
    }
}
