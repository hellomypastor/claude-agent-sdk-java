package com.anthropic.claude.sdk;

import java.util.List;
import java.util.Map;

/**
 * Result of setting MCP servers.
 *
 * @param added   list of server names that were added
 * @param removed list of server names that were removed
 * @param errors  map of server name to error message for servers that failed
 */
public record McpSetServersResult(
        List<String> added,
        List<String> removed,
        Map<String, String> errors
) {
}
