package com.anthropic.claude.sdk.types.options;

/**
 * Plugin configuration for SDK-managed plugins.
 * The type is always "local".
 */
public final class SdkPluginConfig {
    private final String type;
    private final String path;

    public SdkPluginConfig(String type, String path) {
        this.type = type;
        this.path = path;
    }

    public String type() {
        return type;
    }

    public String path() {
        return path;
    }

    /**
     * Create a local plugin config.
     */
    public static SdkPluginConfig local(String path) {
        return new SdkPluginConfig("local", path);
    }
}
