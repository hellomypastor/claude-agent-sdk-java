package com.anthropic.claude.sdk.hooks;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Matcher for routing hooks to specific tools or all tools.
 */
public class HookMatcher {

    @Nullable
    private final String matcher;
    private final List<HookCallback> hooks;

    public HookMatcher(@Nullable String matcher, List<HookCallback> hooks) {
        this.matcher = matcher;
        this.hooks = hooks != null ? new ArrayList<>(hooks) : new ArrayList<>();
    }

    /**
     * Creates a matcher for all tools.
     *
     * @param hooks the list of hook callbacks to apply
     * @return a new HookMatcher that matches all tools
     */
    public static HookMatcher matchAll(List<HookCallback> hooks) {
        return new HookMatcher(null, hooks);
    }

    /**
     * Creates a matcher for a specific tool.
     *
     * @param toolName the name of the tool to match
     * @param hooks the list of hook callbacks to apply
     * @return a new HookMatcher for the specific tool
     */
    public static HookMatcher matchTool(String toolName, List<HookCallback> hooks) {
        return new HookMatcher(toolName, hooks);
    }

    @Nullable
    public String getMatcher() {
        return matcher;
    }

    public List<HookCallback> getHooks() {
        return Collections.unmodifiableList(hooks);
    }

    /**
     * Checks if this matcher applies to the given tool name.
     *
     * @param toolName the tool name to check
     * @return true if this matcher applies to the tool, false otherwise
     */
    public boolean matches(String toolName) {
        return matcher == null || matcher.equals(toolName);
    }
}
