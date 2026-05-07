package com.anthropic.claude.sdk.types.hooks;

import java.util.HashMap;
import java.util.Map;

/**
 * Interface for hook outputs.
 * A hook can return either an async result (requesting deferred processing)
 * or a synchronous result with control flow directives.
 */
public interface HookOutput {

    /**
     * Async hook output indicating the hook result will be provided later.
     */
    final class AsyncHookOutput implements HookOutput {
        private final int asyncTimeout;

        public AsyncHookOutput(int asyncTimeout) {
            this.asyncTimeout = asyncTimeout;
        }

        public int asyncTimeout() {
            return asyncTimeout;
        }
    }

    /**
     * Synchronous hook output with control flow directives and hook-specific data.
     * Use the {@link Builder} to construct instances.
     */
    final class SyncHookOutput implements HookOutput {

        private final Boolean continueExecution;
        private final Boolean suppressOutput;
        private final String stopReason;
        private final String decision;
        private final String systemMessage;
        private final String reason;
        private final Map<String, Object> hookSpecificOutput;

        private SyncHookOutput(Builder builder) {
            this.continueExecution = builder.continueExecution;
            this.suppressOutput = builder.suppressOutput;
            this.stopReason = builder.stopReason;
            this.decision = builder.decision;
            this.systemMessage = builder.systemMessage;
            this.reason = builder.reason;
            this.hookSpecificOutput = builder.hookSpecificOutput;
        }

        public Boolean continueExecution() {
            return continueExecution;
        }

        public Boolean suppressOutput() {
            return suppressOutput;
        }

        public String stopReason() {
            return stopReason;
        }

        public String decision() {
            return decision;
        }

        public String systemMessage() {
            return systemMessage;
        }

        public String reason() {
            return reason;
        }

        public Map<String, Object> hookSpecificOutput() {
            return hookSpecificOutput;
        }

        public static Builder builder() {
            return new Builder();
        }

        public static final class Builder {
            private Boolean continueExecution;
            private Boolean suppressOutput;
            private String stopReason;
            private String decision;
            private String systemMessage;
            private String reason;
            private Map<String, Object> hookSpecificOutput;

            private Builder() {
            }

            public Builder continueExecution(Boolean continueExecution) {
                this.continueExecution = continueExecution;
                return this;
            }

            public Builder suppressOutput(Boolean suppressOutput) {
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

            public Builder addHookSpecificOutput(String key, Object value) {
                if (this.hookSpecificOutput == null) {
                    this.hookSpecificOutput = new HashMap<>();
                }
                this.hookSpecificOutput.put(key, value);
                return this;
            }

            public SyncHookOutput build() {
                return new SyncHookOutput(this);
            }
        }
    }
}
