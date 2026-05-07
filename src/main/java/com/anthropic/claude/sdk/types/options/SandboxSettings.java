package com.anthropic.claude.sdk.types.options;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Sandbox configuration settings.
 */
public final class SandboxSettings {

    private final Boolean enabled;
    private final Boolean autoAllowBashIfSandboxed;
    private final Boolean allowUnsandboxedCommands;
    private final SandboxNetworkConfig network;
    private final Map<String, List<String>> ignoreViolations;
    private final Boolean enableWeakerNestedSandbox;
    private final List<String> excludedCommands;

    private SandboxSettings(Builder builder) {
        this.enabled = builder.enabled;
        this.autoAllowBashIfSandboxed = builder.autoAllowBashIfSandboxed;
        this.allowUnsandboxedCommands = builder.allowUnsandboxedCommands;
        this.network = builder.network;
        this.ignoreViolations = builder.ignoreViolations;
        this.enableWeakerNestedSandbox = builder.enableWeakerNestedSandbox;
        this.excludedCommands = builder.excludedCommands;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public Boolean getAutoAllowBashIfSandboxed() {
        return autoAllowBashIfSandboxed;
    }

    public Boolean getAllowUnsandboxedCommands() {
        return allowUnsandboxedCommands;
    }

    public SandboxNetworkConfig getNetwork() {
        return network;
    }

    public Map<String, List<String>> getIgnoreViolations() {
        return ignoreViolations;
    }

    public Boolean getEnableWeakerNestedSandbox() {
        return enableWeakerNestedSandbox;
    }

    public List<String> getExcludedCommands() {
        return excludedCommands;
    }

    public static final class Builder {
        private Boolean enabled;
        private Boolean autoAllowBashIfSandboxed;
        private Boolean allowUnsandboxedCommands;
        private SandboxNetworkConfig network;
        private Map<String, List<String>> ignoreViolations = Collections.emptyMap();
        private Boolean enableWeakerNestedSandbox;
        private List<String> excludedCommands = Collections.emptyList();

        private Builder() {
        }

        public Builder enabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder autoAllowBashIfSandboxed(Boolean autoAllowBashIfSandboxed) {
            this.autoAllowBashIfSandboxed = autoAllowBashIfSandboxed;
            return this;
        }

        public Builder allowUnsandboxedCommands(Boolean allowUnsandboxedCommands) {
            this.allowUnsandboxedCommands = allowUnsandboxedCommands;
            return this;
        }

        public Builder network(SandboxNetworkConfig network) {
            this.network = network;
            return this;
        }

        public Builder ignoreViolations(Map<String, List<String>> ignoreViolations) {
            this.ignoreViolations = ignoreViolations != null ? ignoreViolations : Collections.emptyMap();
            return this;
        }

        public Builder enableWeakerNestedSandbox(Boolean enableWeakerNestedSandbox) {
            this.enableWeakerNestedSandbox = enableWeakerNestedSandbox;
            return this;
        }

        public Builder excludedCommands(List<String> excludedCommands) {
            this.excludedCommands = excludedCommands != null ? excludedCommands : Collections.emptyList();
            return this;
        }

        public SandboxSettings build() {
            return new SandboxSettings(this);
        }
    }
}
