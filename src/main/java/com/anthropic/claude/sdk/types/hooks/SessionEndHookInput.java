package com.anthropic.claude.sdk.types.hooks;

/**
 * Hook input for the SessionEnd event.
 * Fired when a session ends.
 */
public final class SessionEndHookInput extends BaseHookInput implements HookInput {

    private final String reason;

    public SessionEndHookInput(String sessionId, String transcriptPath, String cwd, String permissionMode,
                                String reason) {
        super(sessionId, transcriptPath, cwd, permissionMode);
        this.reason = reason;
    }

    public String reason() {
        return reason;
    }

    @Override
    public String hookEventName() {
        return "SessionEnd";
    }
}
