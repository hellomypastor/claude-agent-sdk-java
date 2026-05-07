package com.anthropic.claude.sdk.session;

/**
 * Information about a stored session.
 */
public final class SessionInfo {

    private final String sessionId;
    private final long mtime;

    public SessionInfo(String sessionId, long mtime) {
        this.sessionId = sessionId;
        this.mtime = mtime;
    }

    public String sessionId() {
        return sessionId;
    }

    public long mtime() {
        return mtime;
    }
}
