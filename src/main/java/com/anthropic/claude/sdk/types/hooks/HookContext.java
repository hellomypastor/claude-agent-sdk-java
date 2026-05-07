package com.anthropic.claude.sdk.types.hooks;

/**
 * Context provided to hook executions.
 *
 * @param signal placeholder for cancellation support (AbortSignal equivalent)
 */
public record HookContext(Object signal) {
}
