package com.anthropic.claude.sdk.types.permissions;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Result of a permission check.
 */
public interface PermissionResult {

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
    final class Allow implements PermissionResult {
        private final Map<String, Object> updatedInput;
        private final List<PermissionUpdate> updatedPermissions;
        private final String toolUseID;

        public Allow(Map<String, Object> updatedInput, List<PermissionUpdate> updatedPermissions, String toolUseID) {
            this.updatedInput = updatedInput;
            this.updatedPermissions = updatedPermissions;
            this.toolUseID = toolUseID;
        }

        public Map<String, Object> updatedInput() {
            return updatedInput;
        }

        public List<PermissionUpdate> updatedPermissions() {
            return updatedPermissions;
        }

        public String toolUseID() {
            return toolUseID;
        }
    }

    /**
     * Permission denied.
     */
    final class Deny implements PermissionResult {
        private final String message;
        private final Boolean interrupt;
        private final String toolUseID;

        public Deny(String message, Boolean interrupt, String toolUseID) {
            this.message = message;
            this.interrupt = interrupt;
            this.toolUseID = toolUseID;
        }

        public String message() {
            return message;
        }

        public Boolean interrupt() {
            return interrupt;
        }

        public String toolUseID() {
            return toolUseID;
        }
    }
}
