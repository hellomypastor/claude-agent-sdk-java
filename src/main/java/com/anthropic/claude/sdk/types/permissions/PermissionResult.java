package com.anthropic.claude.sdk.types.permissions;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Result of a permission check.
 */
public interface PermissionResult {

    /**
     * Permission granted.
     */
    @Data
    @AllArgsConstructor
    final class Allow implements PermissionResult {
        private final Map<String, Object> updatedInput;
        private final List<PermissionUpdate> updatedPermissions;
    }

    /**
     * Permission denied.
     */
    @Data
    @AllArgsConstructor
    final class Deny implements PermissionResult {
        private final String message;
        private final Boolean interrupt;
    }
}
