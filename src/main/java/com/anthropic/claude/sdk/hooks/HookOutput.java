package com.anthropic.claude.sdk.hooks;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Output from a hook callback.
 */
public class HookOutput {

    @Nullable
    private final Boolean continue_;
    @Nullable
    private final Boolean suppressOutput;
    @Nullable
    private final String stopReason;
    @Nullable
    private final String decision;
    @Nullable
    private final String systemMessage;
    @Nullable
    private final String reason;
    @Nullable
    private final Map<String, Object> hookSpecificOutput;
    @Nullable
    private final Boolean async;
    @Nullable
    private final Integer asyncTimeout;

    private HookOutput(Builder builder) {
        this.continue_ = builder.continue_;
        this.suppressOutput = builder.suppressOutput;
        this.stopReason = builder.stopReason;
        this.decision = builder.decision;
        this.systemMessage = builder.systemMessage;
        this.reason = builder.reason;
        this.hookSpecificOutput = builder.hookSpecificOutput;
        this.async = builder.async;
        this.asyncTimeout = builder.asyncTimeout;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Converts this hook output to a map representation.
     *
     * @return a map containing all non-null hook output fields
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        if (continue_ != null) map.put("continue", continue_);
        if (suppressOutput != null) map.put("suppressOutput", suppressOutput);
        if (stopReason != null) map.put("stopReason", stopReason);
        if (decision != null) map.put("decision", decision);
        if (systemMessage != null) map.put("systemMessage", systemMessage);
        if (reason != null) map.put("reason", reason);
        if (hookSpecificOutput != null) map.put("hookSpecificOutput", hookSpecificOutput);
        if (async != null) map.put("async", async);
        if (asyncTimeout != null) map.put("asyncTimeout", asyncTimeout);
        return map;
    }

    public static class Builder {
        private Boolean continue_;
        private Boolean suppressOutput;
        private String stopReason;
        private String decision;
        private String systemMessage;
        private String reason;
        private Map<String, Object> hookSpecificOutput;
        private Boolean async;
        private Integer asyncTimeout;

        public Builder continue_(boolean continue_) {
            this.continue_ = continue_;
            return this;
        }

        public Builder suppressOutput(boolean suppressOutput) {
            this.suppressOutput = suppressOutput;
            return this;
        }

        public Builder stopReason(String stopReason) {
            this.stopReason = stopReason;
            return this;
        }

        public Builder decision(String decision) {
            this.decision = decision;
            return this;
        }

        public Builder systemMessage(String systemMessage) {
            this.systemMessage = systemMessage;
            return this;
        }

        public Builder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public Builder hookSpecificOutput(Map<String, Object> hookSpecificOutput) {
            this.hookSpecificOutput = hookSpecificOutput;
            return this;
        }

        public Builder async(boolean async) {
            this.async = async;
            return this;
        }

        public Builder asyncTimeout(int asyncTimeout) {
            this.asyncTimeout = asyncTimeout;
            return this;
        }

        public HookOutput build() {
            return new HookOutput(this);
        }
    }
}
