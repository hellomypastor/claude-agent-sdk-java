package com.anthropic.claude.sdk.control;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * Request for permission to use a tool.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SDKControlPermissionRequest {

    @JsonProperty("subtype")
    private final String subtype = "can_use_tool";

    @JsonProperty("tool_name")
    private final String toolName;

    @JsonProperty("input")
    private final Map<String, Object> input;

    @JsonProperty("permission_suggestions")
    @Nullable
    private final List<Object> permissionSuggestions;

    @JsonProperty("blocked_path")
    @Nullable
    private final String blockedPath;

    public SDKControlPermissionRequest(
            String toolName,
            Map<String, Object> input,
            @Nullable List<Object> permissionSuggestions,
            @Nullable String blockedPath) {
        this.toolName = toolName;
        this.input = input;
        this.permissionSuggestions = permissionSuggestions;
        this.blockedPath = blockedPath;
    }

    public String getSubtype() {
        return subtype;
    }

    public String getToolName() {
        return toolName;
    }

    public Map<String, Object> getInput() {
        return input;
    }

    @Nullable
    public List<Object> getPermissionSuggestions() {
        return permissionSuggestions;
    }

    @Nullable
    public String getBlockedPath() {
        return blockedPath;
    }
}
