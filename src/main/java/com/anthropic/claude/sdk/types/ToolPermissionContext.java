package com.anthropic.claude.sdk.types;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Context for tool permission checks.
 */
public class ToolPermissionContext {

    @Nullable
    private final Object signal;
    @Nullable
    private final List<PermissionUpdate> suggestions;

    public ToolPermissionContext(@Nullable Object signal, @Nullable List<PermissionUpdate> suggestions) {
        this.signal = signal;
        this.suggestions = suggestions;
    }

    @Nullable
    public Object getSignal() {
        return signal;
    }

    @Nullable
    public List<PermissionUpdate> getSuggestions() {
        return suggestions;
    }
}
