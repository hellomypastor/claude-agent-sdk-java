package com.anthropic.claude.sdk.types.permissions;

/**
 * A permission rule value consisting of a tool name and rule content.
 */
public record PermissionRuleValue(String toolName, String ruleContent) {
}
