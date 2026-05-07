package com.anthropic.claude.sdk.types.permissions;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Result of a permission check.
 */
public sealed interface PermissionResult permits PermissionResult.Allow, PermissionResult.Deny {

    static Allow allow() {
        return new Allow(null, Collections.emptyList(), null);
    }

    static Allow allow(Map<String, Object> updatedInput) {
        return new Allow(updatedInput, Collections.emptyList(), null);
    }

    static Allow allow(Map<String, Object> updatedInput, List<PermissionUpdate> updates) {
        return new Allow(updatedInput, updates != null ? updates : Collections.emptyList(), null);
    }

    static Deny deny(String message) {
        return new Deny(message, Boolean.FALSE, null);
    }

    static Deny deny(String message, Boolean interrupt) {
        return new Deny(message, interrupt, null);
    }

    /**
     * Permission granted.
     */
    record Allow(
            Map<String, Object> updatedInput,
            List<PermissionUpdate> updatedPermissions,
            String toolUseID
    ) implements PermissionResult {
    }

    /**
     * Permission denied.
     */
    record Deny(
            String message,
            Boolean interrupt,
            String toolUseID
    ) implements PermissionResult {
    }
}
