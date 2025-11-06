package com.anthropic.claude.sdk.hooks;

import com.anthropic.claude.sdk.types.PermissionMode;

import javax.annotation.Nullable;

/**
 * Base class for all hook input types.
 */
public abstract class BaseHookInput {

    private final String sessionId;
    private final String transcriptPath;
    private final String cwd;
    private final PermissionMode permissionMode;

    protected BaseHookInput(
            String sessionId,
            String transcriptPath,
            String cwd,
            @Nullable PermissionMode permissionMode) {
        this.sessionId = sessionId;
        this.transcriptPath = transcriptPath;
        this.cwd = cwd;
        this.permissionMode = permissionMode;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getTranscriptPath() {
        return transcriptPath;
    }

    public String getCwd() {
        return cwd;
    }

    @Nullable
    public PermissionMode getPermissionMode() {
        return permissionMode;
    }
}
