package com.anthropic.claude.sdk.hooks;

import com.anthropic.claude.sdk.types.PermissionMode;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Hook input for PreToolUse event.
 */
public class PreToolUseHookInput extends BaseHookInput {

    private final String toolName;
    private final Map<String, Object> toolInput;

    public PreToolUseHookInput(
            String sessionId,
            String transcriptPath,
            String cwd,
            @Nullable PermissionMode permissionMode,
            String toolName,
            Map<String, Object> toolInput) {
        super(sessionId, transcriptPath, cwd, permissionMode);
        this.toolName = toolName;
        this.toolInput = toolInput;
    }

    public String getToolName() {
        return toolName;
    }

    public Map<String, Object> getToolInput() {
        return toolInput;
    }
}
