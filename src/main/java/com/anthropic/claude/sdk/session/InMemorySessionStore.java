package com.anthropic.claude.sdk.session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory implementation of {@link SessionStore}, backed by a {@link ConcurrentHashMap}.
 * Suitable for testing and short-lived processes.
 */
public final class InMemorySessionStore implements SessionStore {

    private final ConcurrentHashMap<String, List<SessionStoreEntry>> store = new ConcurrentHashMap<>();

    private static String toStoreKey(SessionKey key) {
        StringBuilder sb = new StringBuilder();
        sb.append(key.projectKey() != null ? key.projectKey() : "");
        sb.append(":");
        sb.append(key.sessionId() != null ? key.sessionId() : "");
        if (key.subpath() != null && !key.subpath().isEmpty()) {
            sb.append(":").append(key.subpath());
        }
        return sb.toString();
    }

    @Override
    public CompletableFuture<Void> append(SessionKey key, List<SessionStoreEntry> entries) {
        String storeKey = toStoreKey(key);
        store.compute(storeKey, (k, existing) -> {
            List<SessionStoreEntry> list = existing != null ? new ArrayList<>(existing) : new ArrayList<>();
            list.addAll(entries);
            return list;
        });
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<List<SessionStoreEntry>> load(SessionKey key) {
        String storeKey = toStoreKey(key);
        List<SessionStoreEntry> entries = store.get(storeKey);
        if (entries == null) {
            return CompletableFuture.completedFuture(Collections.emptyList());
        }
        return CompletableFuture.completedFuture(Collections.unmodifiableList(new ArrayList<>(entries)));
    }

    @Override
    public CompletableFuture<List<SessionInfo>> listSessions(String projectKey) {
        String prefix = (projectKey != null ? projectKey : "") + ":";
        List<SessionInfo> sessions = store.entrySet().stream()
                .filter(e -> e.getKey().startsWith(prefix))
                .map(e -> {
                    String remaining = e.getKey().substring(prefix.length());
                    String sessionId = remaining.contains(":")
                            ? remaining.substring(0, remaining.indexOf(':'))
                            : remaining;
                    return new SessionInfo(sessionId, System.currentTimeMillis());
                })
                .distinct()
                .collect(Collectors.toList());
        return CompletableFuture.completedFuture(sessions);
    }

    @Override
    public CompletableFuture<Void> delete(SessionKey key) {
        String storeKey = toStoreKey(key);
        store.remove(storeKey);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<List<String>> listSubkeys(SessionKey key) {
        String prefix = toStoreKey(key) + ":";
        List<String> subkeys = store.keySet().stream()
                .filter(k -> k.startsWith(prefix))
                .map(k -> k.substring(prefix.length()))
                .collect(Collectors.toList());
        return CompletableFuture.completedFuture(subkeys);
    }
}
