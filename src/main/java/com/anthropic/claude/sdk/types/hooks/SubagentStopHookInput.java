package com.anthropic.claude.sdk.types.hooks;

/**
 * Hook input for the SubagentStop event.
 * Fired when a subagent is stopped.
 */
public final class SubagentStopHookInput extends BaseHookInput implements HookInput {

    private final boolean stopHookActive;
    private final String agentId;
    private final String agentTranscriptPath;

    public SubagentStopHookInput(String sessionId, String transcriptPath, String cwd, String permissionMode,
                                  boolean stopHookActive, String agentId, String agentTranscriptPath) {
        super(sessionId, transcriptPath, cwd, permissionMode);
        this.stopHookActive = stopHookActive;
        this.agentId = agentId;
        this.agentTranscriptPath = agentTranscriptPath;
    }

    public boolean stopHookActive() {
        return stopHookActive;
    }

    public String agentId() {
        return agentId;
    }

    public String agentTranscriptPath() {
        return agentTranscriptPath;
    }

    @Override
    public String hookEventName() {
        return "SubagentStop";
    }
}
