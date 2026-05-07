package com.anthropic.claude.sdk.types.hooks;

/**
 * Hook input for the UserPromptSubmit event.
 * Fired when a user prompt is submitted.
 */
public final class UserPromptSubmitHookInput extends BaseHookInput implements HookInput {

    private final String prompt;

    public UserPromptSubmitHookInput(String sessionId, String transcriptPath, String cwd, String permissionMode,
                                     String prompt) {
        super(sessionId, transcriptPath, cwd, permissionMode);
        this.prompt = prompt;
    }

    public String prompt() {
        return prompt;
    }

    @Override
    public String hookEventName() {
        return "UserPromptSubmit";
    }
}
