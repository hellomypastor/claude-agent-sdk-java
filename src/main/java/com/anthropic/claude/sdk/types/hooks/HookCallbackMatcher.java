package com.anthropic.claude.sdk.types.hooks;

import java.util.List;

/**
 * Matches hooks to specific tools or patterns, with an optional timeout.
 *
 * @param matcher tool name pattern (e.g. "Bash", "*", etc.)
 * @param hooks   list of hook callbacks to execute when matched
 * @param timeout optional timeout in milliseconds (may be null)
 */
public record HookCallbackMatcher(String matcher, List<HookCallback> hooks, Integer timeout) {
}
