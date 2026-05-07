package com.anthropic.claude.sdk;

import java.util.List;
import java.util.Map;

/**
 * Result of setting MCP servers.
 */
public final class McpSetServersResult {

    private final List<String> added;
    private final List<String> removed;
    private final Map<String, String> errors;

    public McpSetServersResult(List<String> added, List<String> removed, Map<String, String> errors) {
        this.added = added;
        this.removed = removed;
        this.errors = errors;
    }

    public List<String> added() {
        return added;
    }

    public List<String> removed() {
        return removed;
    }

    public Map<String, String> errors() {
        return errors;
    }
}
