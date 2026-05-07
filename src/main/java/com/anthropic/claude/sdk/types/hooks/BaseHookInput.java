package com.anthropic.claude.sdk.types.hooks;

/**
 * Base class for all hook inputs providing common session fields.
 */
public abstract class BaseHookInput {

    private final String sessionId;
    private final String transcriptPath;
    private final String cwd;
    private final String permissionMode;

    protected BaseHookInput(String sessionId, String transcriptPath, String cwd, String permissionMode) {
        this.sessionId = sessionId;
        this.transcriptPath = transcriptPath;
        this.cwd = cwd;
        this.permissionMode = permissionMode;
    }

    public String sessionId() {
        return sessionId;
    }

    public String transcriptPath() {
        return transcriptPath;
    }

    public String cwd() {
        return cwd;
    }

    public String permissionMode() {
        return permissionMode;
    }

    /**
     * Returns the hook event name for this input type.
     */
    public abstract String hookEventName();
}
