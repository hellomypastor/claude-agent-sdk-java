package com.anthropic.claude.sdk.types.hooks;

/**
 * Hook input for the SubagentStart event.
 * Fired when a subagent is started.
 */
public final class SubagentStartHookInput extends BaseHookInput implements HookInput {

    private final String agentId;
    private final String agentType;

    public SubagentStartHookInput(String sessionId, String transcriptPath, String cwd, String permissionMode,
                                   String agentId, String agentType) {
        super(sessionId, transcriptPath, cwd, permissionMode);
        this.agentId = agentId;
        this.agentType = agentType;
    }

    public String agentId() {
        return agentId;
    }

    public String agentType() {
        return agentType;
    }

    @Override
    public String hookEventName() {
        return "SubagentStart";
    }
}
