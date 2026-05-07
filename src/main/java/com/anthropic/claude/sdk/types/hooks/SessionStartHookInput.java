package com.anthropic.claude.sdk.types.hooks;

/**
 * Hook input for the SessionStart event.
 * Fired when a new session begins.
 */
public final class SessionStartHookInput extends BaseHookInput implements HookInput {

    private final String source;
    private final String agentType;

    public SessionStartHookInput(String sessionId, String transcriptPath, String cwd, String permissionMode,
                                  String source, String agentType) {
        super(sessionId, transcriptPath, cwd, permissionMode);
        this.source = source;
        this.agentType = agentType;
    }

    public String source() {
        return source;
    }

    public String agentType() {
        return agentType;
    }

    @Override
    public String hookEventName() {
        return "SessionStart";
    }
}
