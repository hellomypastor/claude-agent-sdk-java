package com.anthropic.claude.sdk.session;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Interface for persisting and loading session data.
 */
public interface SessionStore {

    /**
     * Append entries to a session.
     *
     * @param key     the session key
     * @param entries the entries to append
     * @return a future that completes when the entries are persisted
     */
    CompletableFuture<Void> append(SessionKey key, List<SessionStoreEntry> entries);

    /**
     * Load all entries for a session.
     *
     * @param key the session key
     * @return a future with the loaded entries
     */
    CompletableFuture<List<SessionStoreEntry>> load(SessionKey key);

    /**
     * List all sessions for a given project key.
     *
     * @param projectKey the project key
     * @return a future with session info list
     */
    default CompletableFuture<List<SessionInfo>> listSessions(String projectKey) {
        return CompletableFuture.completedFuture(Collections.emptyList());
    }

    /**
     * Delete a session.
     *
     * @param key the session key
     * @return a future that completes when the session is deleted
     */
    default CompletableFuture<Void> delete(SessionKey key) {
        return CompletableFuture.completedFuture(null);
    }

    /**
     * List subkeys under a session key.
     *
     * @param key the session key
     * @return a future with the subkey list
     */
    default CompletableFuture<List<String>> listSubkeys(SessionKey key) {
        return CompletableFuture.completedFuture(Collections.emptyList());
    }
}
