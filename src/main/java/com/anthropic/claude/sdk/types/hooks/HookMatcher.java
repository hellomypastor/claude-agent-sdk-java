package com.anthropic.claude.sdk.types.hooks;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Matches hooks to specific tools or patterns.
 */
@Data
@AllArgsConstructor
public final class HookMatcher {
    private final String matcher;      // Tool name pattern (e.g., "Bash", "*", etc.)
    private final List<Hook> hooks;    // List of hooks to execute when matched
}
