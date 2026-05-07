package com.anthropic.claude.sdk.types.options;

import java.util.List;

/**
 * Network configuration for sandbox environment.
 */
public final class SandboxNetworkConfig {
    private final List<String> allowedDomains;
    private final List<String> allowUnixSockets;
    private final Boolean allowAllUnixSockets;
    private final Boolean allowLocalBinding;
    private final Integer httpProxyPort;
    private final Integer socksProxyPort;

    public SandboxNetworkConfig(
            List<String> allowedDomains,
            List<String> allowUnixSockets,
            Boolean allowAllUnixSockets,
            Boolean allowLocalBinding,
            Integer httpProxyPort,
            Integer socksProxyPort
    ) {
        this.allowedDomains = allowedDomains;
        this.allowUnixSockets = allowUnixSockets;
        this.allowAllUnixSockets = allowAllUnixSockets;
        this.allowLocalBinding = allowLocalBinding;
        this.httpProxyPort = httpProxyPort;
        this.socksProxyPort = socksProxyPort;
    }

    public List<String> allowedDomains() {
        return allowedDomains;
    }

    public List<String> allowUnixSockets() {
        return allowUnixSockets;
    }

    public Boolean allowAllUnixSockets() {
        return allowAllUnixSockets;
    }

    public Boolean allowLocalBinding() {
        return allowLocalBinding;
    }

    public Integer httpProxyPort() {
        return httpProxyPort;
    }

    public Integer socksProxyPort() {
        return socksProxyPort;
    }
}
