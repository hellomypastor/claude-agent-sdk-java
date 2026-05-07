package com.anthropic.claude.sdk.types.hooks;

/**
 * Hook input for the Stop event.
 * Fired when the agent loop is about to stop.
 */
public final class StopHookInput extends BaseHookInput implements HookInput {

    private final boolean stopHookActive;

    public StopHookInput(String sessionId, String transcriptPath, String cwd, String permissionMode,
                          boolean stopHookActive) {
        super(sessionId, transcriptPath, cwd, permissionMode);
        this.stopHookActive = stopHookActive;
    }

    public boolean stopHookActive() {
        return stopHookActive;
    }

    @Override
    public String hookEventName() {
        return "Stop";
    }
}
