package com.anthropic.claude.sdk.types.options;

import java.util.List;

/**
 * Network configuration for sandbox environment.
 */
public record SandboxNetworkConfig(
        List<String> allowedDomains,
        List<String> allowUnixSockets,
        Boolean allowAllUnixSockets,
        Boolean allowLocalBinding,
        Integer httpProxyPort,
        Integer socksProxyPort
) {
}
