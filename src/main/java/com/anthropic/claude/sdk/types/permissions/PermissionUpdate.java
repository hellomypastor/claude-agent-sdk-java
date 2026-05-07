package com.anthropic.claude.sdk.types.permissions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Permission update payload returned to the CLI.
 */
public sealed interface PermissionUpdate
        permits PermissionUpdate.AddRules, PermissionUpdate.ReplaceRules,
                PermissionUpdate.RemoveRules, PermissionUpdate.SetMode,
                PermissionUpdate.AddDirectories, PermissionUpdate.RemoveDirectories {

    /**
     * Serialize this update to a map for CLI consumption.
     */
    Map<String, Object> toMap();

    record AddRules(List<PermissionRuleValue> rules, String behavior, String destination) implements PermissionUpdate {
        @Override
        public Map<String, Object> toMap() {
            Map<String, Object> result = new HashMap<>();
            result.put("type", "addRules");
            result.put("rules", rules);
            if (behavior != null) {
                result.put("behavior", behavior);
            }
            if (destination != null) {
                result.put("destination", destination);
            }
            return result;
        }
    }

    record ReplaceRules(List<PermissionRuleValue> rules, String behavior, String destination) implements PermissionUpdate {
        @Override
        public Map<String, Object> toMap() {
            Map<String, Object> result = new HashMap<>();
            result.put("type", "replaceRules");
            result.put("rules", rules);
            if (behavior != null) {
                result.put("behavior", behavior);
            }
            if (destination != null) {
                result.put("destination", destination);
            }
            return result;
        }
    }

    record RemoveRules(List<PermissionRuleValue> rules, String behavior, String destination) implements PermissionUpdate {
        @Override
        public Map<String, Object> toMap() {
            Map<String, Object> result = new HashMap<>();
            result.put("type", "removeRules");
            result.put("rules", rules);
            if (behavior != null) {
                result.put("behavior", behavior);
            }
            if (destination != null) {
                result.put("destination", destination);
            }
            return result;
        }
    }

    record SetMode(String mode, String destination) implements PermissionUpdate {
        @Override
        public Map<String, Object> toMap() {
            Map<String, Object> result = new HashMap<>();
            result.put("type", "setMode");
            result.put("mode", mode);
            if (destination != null) {
                result.put("destination", destination);
            }
            return result;
        }
    }

    record AddDirectories(List<String> directories, String destination) implements PermissionUpdate {
        @Override
        public Map<String, Object> toMap() {
            Map<String, Object> result = new HashMap<>();
            result.put("type", "addDirectories");
            result.put("directories", directories);
            if (destination != null) {
                result.put("destination", destination);
            }
            return result;
        }
    }

    record RemoveDirectories(List<String> directories, String destination) implements PermissionUpdate {
        @Override
        public Map<String, Object> toMap() {
            Map<String, Object> result = new HashMap<>();
            result.put("type", "removeDirectories");
            result.put("directories", directories);
            if (destination != null) {
                result.put("destination", destination);
            }
            return result;
        }
    }
}
