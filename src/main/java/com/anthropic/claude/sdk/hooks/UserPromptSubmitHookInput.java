package com.anthropic.claude.sdk.hooks;

import com.anthropic.claude.sdk.types.PermissionMode;

import javax.annotation.Nullable;

/**
 * Hook input for UserPromptSubmit event.
 */
public class UserPromptSubmitHookInput extends BaseHookInput {

    private final String hookEventName;
    private final String prompt;

    public UserPromptSubmitHookInput(
            String sessionId,
            String transcriptPath,
            String cwd,
            @Nullable PermissionMode permissionMode,
            String hookEventName,
            String prompt) {
        super(sessionId, transcriptPath, cwd, permissionMode);
        this.hookEventName = hookEventName;
        this.prompt = prompt;
    }

    public String getHookEventName() {
        return hookEventName;
    }

    public String getPrompt() {
        return prompt;
    }
}
