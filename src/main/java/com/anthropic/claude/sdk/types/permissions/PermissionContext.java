package com.anthropic.claude.sdk.types.permissions;

import java.util.List;

/**
 * Context provided to permission callbacks.
 *
 * @param signal         abort signal placeholder (for cancellation support)
 * @param suggestions    suggested permission updates
 * @param blockedPath    path that was blocked, if any
 * @param decisionReason reason for the permission decision
 * @param toolUseId      the tool use ID associated with this request
 * @param agentId        the agent ID making the request
 */
public record PermissionContext(
        Object signal,
        List<PermissionUpdate> suggestions,
        String blockedPath,
        String decisionReason,
        String toolUseId,
        String agentId
) {
}
