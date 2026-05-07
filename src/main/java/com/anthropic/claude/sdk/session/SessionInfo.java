package com.anthropic.claude.sdk.session;

/**
 * Information about a stored session.
 *
 * @param sessionId the session identifier
 * @param mtime     the last modification time in epoch millis
 */
public record SessionInfo(String sessionId, long mtime) {
}
