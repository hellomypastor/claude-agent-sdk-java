package com.anthropic.claude.sdk.types.permissions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Permission update payload returned to the CLI.
 */
public interface PermissionUpdate {

    /**
     * Serialize this update to a map for CLI consumption.
     */
    Map<String, Object> toMap();

    final class AddRules implements PermissionUpdate {
        private final List<PermissionRuleValue> rules;
        private final String behavior;
        private final String destination;

        public AddRules(List<PermissionRuleValue> rules, String behavior, String destination) {
            this.rules = rules;
            this.behavior = behavior;
            this.destination = destination;
        }

        public List<PermissionRuleValue> rules() {
            return rules;
        }

        public String behavior() {
            return behavior;
        }

        public String destination() {
            return destination;
        }

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

    final class ReplaceRules implements PermissionUpdate {
        private final List<PermissionRuleValue> rules;
        private final String behavior;
        private final String destination;

        public ReplaceRules(List<PermissionRuleValue> rules, String behavior, String destination) {
            this.rules = rules;
            this.behavior = behavior;
            this.destination = destination;
        }

        public List<PermissionRuleValue> rules() {
            return rules;
        }

        public String behavior() {
            return behavior;
        }

        public String destination() {
            return destination;
        }

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

    final class RemoveRules implements PermissionUpdate {
        private final List<PermissionRuleValue> rules;
        private final String behavior;
        private final String destination;

        public RemoveRules(List<PermissionRuleValue> rules, String behavior, String destination) {
            this.rules = rules;
            this.behavior = behavior;
            this.destination = destination;
        }

        public List<PermissionRuleValue> rules() {
            return rules;
        }

        public String behavior() {
            return behavior;
        }

        public String destination() {
            return destination;
        }

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

    final class SetMode implements PermissionUpdate {
        private final String mode;
        private final String destination;

        public SetMode(String mode, String destination) {
            this.mode = mode;
            this.destination = destination;
        }

        public String mode() {
            return mode;
        }

        public String destination() {
            return destination;
        }

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

    final class AddDirectories implements PermissionUpdate {
        private final List<String> directories;
        private final String destination;

        public AddDirectories(List<String> directories, String destination) {
            this.directories = directories;
            this.destination = destination;
        }

        public List<String> directories() {
            return directories;
        }

        public String destination() {
            return destination;
        }

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

    final class RemoveDirectories implements PermissionUpdate {
        private final List<String> directories;
        private final String destination;

        public RemoveDirectories(List<String> directories, String destination) {
            this.directories = directories;
            this.destination = destination;
        }

        public List<String> directories() {
            return directories;
        }

        public String destination() {
            return destination;
        }

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
