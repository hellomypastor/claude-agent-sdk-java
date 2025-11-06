package com.anthropic.claude.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Represents a permission rule value.
 */
public class PermissionRuleValue {

    private final String toolName;
    @Nullable
    private final String ruleContent;

    @JsonCreator
    public PermissionRuleValue(
            @JsonProperty("tool_name") String toolName,
            @JsonProperty("rule_content") @Nullable String ruleContent) {
        this.toolName = toolName;
        this.ruleContent = ruleContent;
    }

    @JsonProperty("tool_name")
    public String getToolName() {
        return toolName;
    }

    @Nullable
    @JsonProperty("rule_content")
    public String getRuleContent() {
        return ruleContent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PermissionRuleValue that = (PermissionRuleValue) o;
        return Objects.equals(toolName, that.toolName) &&
               Objects.equals(ruleContent, that.ruleContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(toolName, ruleContent);
    }

    @Override
    public String toString() {
        return "PermissionRuleValue{toolName='" + toolName + "', ruleContent='" + ruleContent + "'}";
    }
}
