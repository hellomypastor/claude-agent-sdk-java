package com.anthropic.claude.sdk.types.options;

/**
 * Plugin configuration for SDK-managed plugins.
 * The type is always "local".
 */
public record SdkPluginConfig(String type, String path) {

    /**
     * Create a local plugin config.
     */
    public static SdkPluginConfig local(String path) {
        return new SdkPluginConfig("local", path);
    }
}
