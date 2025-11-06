package com.anthropic.claude.sdk.hooks;

import com.anthropic.claude.sdk.types.PermissionMode;

import javax.annotation.Nullable;

/**
 * Hook input for Stop event.
 */
public class StopHookInput extends BaseHookInput {

    private final String hookEventName;
    private final boolean stopHookActive;

    public StopHookInput(
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
