package com.anthropic.claude.sdk.types;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;

/**
 * Represents a system prompt preset configuration.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SystemPromptPreset {

    private final String type;
    private final String preset;
    @Nullable
    private final String append;

    private SystemPromptPreset(Builder builder) {
        this.type = "preset";
        this.preset = builder.preset;
        this.append = builder.append;
    }

    public static Builder builder(String preset) {
        return new Builder(preset);
    }

    public String getType() {
        return type;
    }

    public String getPreset() {
        return preset;
    }

    @Nullable
    public String getAppend() {
        return append;
    }

    public static class Builder {
        private final String preset;
        private String append;

        private Builder(String preset) {
            this.preset = preset;
        }

        /**
         * Additional text to append to the preset.
         *
         * @param append text to append
         * @return this builder
         */
        public Builder append(String append) {
            this.append = append;
            return this;
        }

        public SystemPromptPreset build() {
            return new SystemPromptPreset(this);
        }
    }
}
