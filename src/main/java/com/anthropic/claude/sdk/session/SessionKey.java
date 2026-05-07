package com.anthropic.claude.sdk.session;

/**
 * Key for identifying a session in the session store.
 *
 * @param projectKey the project identifier
 * @param sessionId  the session identifier
 * @param subpath    optional sub-path within the session
 */
public record SessionKey(String projectKey, String sessionId, String subpath) {
}
