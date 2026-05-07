package com.anthropic.claude.sdk.types.hooks;

/**
 * Context provided to hook executions.
 */
public final class HookContext {
    private final Object signal;

    public HookContext(Object signal) {
        this.signal = signal;
    }

    public Object signal() {
        return signal;
    }
}
