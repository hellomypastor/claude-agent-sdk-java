package com.anthropic.claude.sdk.types;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.nio.file.Path;

/**
 * Configuration for SDK plugins.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SdkPluginConfig {

    private final String type;
    private final String path;

    private SdkPluginConfig(Builder builder) {
        this.type = "local";
        this.path = builder.path.toString();
    }

    public static Builder builder(Path path) {
        return new Builder(path);
    }

    public static Builder builder(String path) {
        return new Builder(Path.of(path));
    }

    public String getType() {
        return type;
    }

    public String getPath() {
        return path;
    }

    public static class Builder {
        private final Path path;

        private Builder(Path path) {
            this.path = path;
        }

        public SdkPluginConfig build() {
            return new SdkPluginConfig(this);
        }
    }
}
