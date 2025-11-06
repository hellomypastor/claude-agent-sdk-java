package com.anthropic.claude.sdk.hooks;

import com.anthropic.claude.sdk.types.PermissionMode;

import javax.annotation.Nullable;

/**
 * Hook input for SubagentStop event.
 */
public class SubagentStopHookInput extends BaseHookInput {

    private final String hookEventName;
    private final boolean stopHookActive;

    public SubagentStopHookInput(
            String sessionId,
            String transcriptPath,
            String cwd,
            @Nullable PermissionMode permissionMode,
            String hookEventName,
            boolean stopHookActive) {
        super(sessionId, transcriptPath, cwd, permissionMode);
        this.hookEventName = hookEventName;
        this.stopHookActive = stopHookActive;
    }

    public String getHookEventName() {
        return hookEventName;
    }

    public boolean isStopHookActive() {
        return stopHookActive;
    }
}
