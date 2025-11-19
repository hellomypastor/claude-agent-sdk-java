package com.anthropic.claude.sdk.types.options;

import lombok.Builder;
import lombok.Getter;

import java.nio.file.Path;

/**
 * Plugin configuration for SDK-managed plugins.
 */
@Getter
@Builder
public class SdkPluginConfig {
    public enum PluginType {
        LOCAL
    }

    private final PluginType type;
    private final Path path;
}
