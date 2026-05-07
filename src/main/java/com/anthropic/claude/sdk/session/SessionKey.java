package com.anthropic.claude.sdk.session;

/**
 * Key for identifying a session in the session store.
 */
public final class SessionKey {

    private final String projectKey;
    private final String sessionId;
    private final String subpath;

    public SessionKey(String projectKey, String sessionId, String subpath) {
        this.projectKey = projectKey;
        this.sessionId = sessionId;
        this.subpath = subpath;
    }

    public String projectKey() {
        return projectKey;
    }

    public String sessionId() {
        return sessionId;
    }

    public String subpath() {
        return subpath;
    }
}
