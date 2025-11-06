package com.anthropic.claude.sdk.hooks;

import com.anthropic.claude.sdk.types.PermissionMode;

import javax.annotation.Nullable;

/**
 * Hook input for PreCompact event.
 */
public class PreCompactHookInput extends BaseHookInput {

    private final String hookEventName;
    private final String trigger;
    @Nullable
    private final String customInstructions;

    public PreCompactHookInput(
            String sessionId,
            String transcriptPath,
            String cwd,
            @Nullable PermissionMode permissionMode,
            String hookEventName,
            String trigger,
            @Nullable String customInstructions) {
        super(sessionId, transcriptPath, cwd, permissionMode);
        this.hookEventName = hookEventName;
        this.trigger = trigger;
        this.customInstructions = customInstructions;
    }

    public String getHookEventName() {
        return hookEventName;
    }

    public String getTrigger() {
        return trigger;
    }

    @Nullable
    public String getCustomInstructions() {
        return customInstructions;
    }
}
