package com.anthropic.claude.sdk.types.hooks;

import java.util.List;

/**
 * Matches hooks to specific tools or patterns, with an optional timeout.
 */
public final class HookCallbackMatcher {
    private final String matcher;
    private final List<HookCallback> hooks;
    private final Integer timeout;

    public HookCallbackMatcher(String matcher, List<HookCallback> hooks, Integer timeout) {
        this.matcher = matcher;
        this.hooks = hooks;
        this.timeout = timeout;
    }

    public String matcher() {
        return matcher;
    }

    public List<HookCallback> hooks() {
        return hooks;
    }

    public Integer timeout() {
        return timeout;
    }
}
