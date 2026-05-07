package com.anthropic.claude.sdk.types.hooks;

import com.anthropic.claude.sdk.types.permissions.PermissionUpdate;

import java.util.List;

/**
 * Hook input for the PermissionRequest event.
 * Fired when a tool requests permission to execute.
 */
public final class PermissionRequestHookInput extends BaseHookInput implements HookInput {

    private final String toolName;
    private final Object toolInput;
    private final List<PermissionUpdate> permissionSuggestions;

    public PermissionRequestHookInput(String sessionId, String transcriptPath, String cwd, String permissionMode,
                                       String toolName, Object toolInput,
                                       List<PermissionUpdate> permissionSuggestions) {
        super(sessionId, transcriptPath, cwd, permissionMode);
        this.toolName = toolName;
        this.toolInput = toolInput;
        this.permissionSuggestions = permissionSuggestions;
    }

    public String toolName() {
        return toolName;
    }

    public Object toolInput() {
        return toolInput;
    }

    public List<PermissionUpdate> permissionSuggestions() {
        return permissionSuggestions;
    }

    @Override
    public String hookEventName() {
        return "PermissionRequest";
    }
}
