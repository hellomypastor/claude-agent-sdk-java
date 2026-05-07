package com.anthropic.claude.sdk.session;

import java.util.Map;

/**
 * An entry in the session store.
 *
 * @param type the type of the entry
 * @param data the entry data
 */
public record SessionStoreEntry(String type, Map<String, Object> data) {
}
