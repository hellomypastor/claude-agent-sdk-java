package com.anthropic.claude.sdk.hooks;

import com.anthropic.claude.sdk.types.PermissionMode;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Hook input for PostToolUse event.
 */
public class PostToolUseHookInput extends BaseHookInput {

    private final String toolName;
    private final Map<String, Object> toolInput;
    private final Object toolResponse;

    public PostToolUseHookInput(
            String sessionId,
            String transcriptPath,
            String cwd,
            @Nullable PermissionMode permissionMode,
            String toolName,
            Map<String, Object> toolInput,
            Object toolResponse) {
        super(sessionId, transcriptPath, cwd, permissionMode);
        this.toolName = toolName;
        this.toolInput = toolInput;
        this.toolResponse = toolResponse;
    }

    public String getToolName() {
        return toolName;
    }

    public Map<String, Object> getToolInput() {
        return toolInput;
    }

    public Object getToolResponse() {
        return toolResponse;
    }
}
