package com.anthropic.claude.sdk.types.hooks;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Context provided to hook executions.
 */
@Data
@AllArgsConstructor
public final class HookContext {
    private final Object signal;  // For cancellation support (future)
}
