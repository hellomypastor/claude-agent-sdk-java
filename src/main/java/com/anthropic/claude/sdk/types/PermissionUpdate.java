package com.anthropic.claude.sdk.types;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a permission update.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PermissionUpdate {

    private final String type;
    @Nullable
    private final List<PermissionRuleValue> rules;
    @Nullable
    private final PermissionBehavior behavior;
    @Nullable
    private final PermissionMode mode;
    @Nullable
    private final List<String> directories;
    @Nullable
    private final String destination;

    public PermissionUpdate(
            String type,
            @Nullable List<PermissionRuleValue> rules,
            @Nullable PermissionBehavior behavior,
            @Nullable PermissionMode mode,
            @Nullable List<String> directories,
            @Nullable String destination) {
        this.type = type;
        this.rules = rules;
        this.behavior = behavior;
        this.mode = mode;
        this.directories = directories;
        this.destination = destination;
    }

    public String getType() {
        return type;
    }

    @Nullable
    public List<PermissionRuleValue> getRules() {
        return rules;
    }

    @Nullable
    public PermissionBehavior getBehavior() {
        return behavior;
    }

    @Nullable
    public PermissionMode getMode() {
        return mode;
    }

    @Nullable
    public List<String> getDirectories() {
        return directories;
    }

    @Nullable
    public String getDestination() {
        return destination;
    }

    /**
     * Convert to dictionary format for CLI protocol.
     */
    public Map<String, Object> toDict() {
        Map<String, Object> dict = new HashMap<>();
        dict.put("type", type);
        if (rules != null) {
            dict.put("rules", rules);
        }
        if (behavior != null) {
            dict.put("behavior", behavior.getValue());
        }
        if (mode != null) {
            dict.put("mode", mode.getValue());
        }
        if (directories != null) {
            dict.put("directories", directories);
        }
        if (destination != null) {
            dict.put("destination", destination);
        }
        return dict;
    }
}
