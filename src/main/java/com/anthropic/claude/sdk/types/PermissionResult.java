package com.anthropic.claude.sdk.types;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * Base interface for permission results.
 */
public interface PermissionResult {
    PermissionBehavior getBehavior();

    /**
     * Permission result indicating to allow the tool use.
     */
    class Allow implements PermissionResult {
        @Nullable
        private final Map<String, Object> updatedInput;
        @Nullable
        private final List<PermissionUpdate> updatedPermissions;

        public Allow() {
            this(null, null);
        }

        public Allow(@Nullable Map<String, Object> updatedInput, @Nullable List<PermissionUpdate> updatedPermissions) {
            this.updatedInput = updatedInput;
            this.updatedPermissions = updatedPermissions;
        }

        @Override
        public PermissionBehavior getBehavior() {
            return PermissionBehavior.ALLOW;
        }

        @Nullable
        public Map<String, Object> getUpdatedInput() {
            return updatedInput;
        }

        @Nullable
        public List<PermissionUpdate> getUpdatedPermissions() {
            return updatedPermissions;
        }
    }

    /**
     * Permission result indicating to deny the tool use.
     */
    class Deny implements PermissionResult {
        private final String message;
        private final boolean interrupt;

        public Deny(String message) {
            this(message, false);
        }

        public Deny(String message, boolean interrupt) {
            this.message = message;
            this.interrupt = interrupt;
        }

        @Override
        public PermissionBehavior getBehavior() {
            return PermissionBehavior.DENY;
        }

        public String getMessage() {
            return message;
        }

        public boolean isInterrupt() {
            return interrupt;
        }
    }
}
