package com.anthropic.claude.sdk.session;

import java.util.Map;

/**
 * An entry in the session store.
 */
public final class SessionStoreEntry {

    private final String type;
    private final Map<String, Object> data;

    public SessionStoreEntry(String type, Map<String, Object> data) {
        this.type = type;
        this.data = data;
    }

    public String type() {
        return type;
    }

    public Map<String, Object> data() {
        return data;
    }
}
