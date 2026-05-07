package com.anthropic.claude.sdk.types.permissions;

import java.util.List;

/**
 * Context provided to permission callbacks.
 */
public final class PermissionContext {
    private final Object signal;
    private final List<PermissionUpdate> suggestions;
    private final String blockedPath;
    private final String decisionReason;
    private final String toolUseId;
    private final String agentId;

    public PermissionContext(
            Object signal,
            List<PermissionUpdate> suggestions,
            String blockedPath,
            String decisionReason,
            String toolUseId,
            String agentId
    ) {
        this.signal = signal;
        this.suggestions = suggestions;
        this.blockedPath = blockedPath;
        this.decisionReason = decisionReason;
        this.toolUseId = toolUseId;
        this.agentId = agentId;
    }

    public Object signal() {
        return signal;
    }

    public List<PermissionUpdate> suggestions() {
        return suggestions;
    }

    public String blockedPath() {
        return blockedPath;
    }

    public String decisionReason() {
        return decisionReason;
    }

    public String toolUseId() {
        return toolUseId;
    }

    public String agentId() {
        return agentId;
    }
}
