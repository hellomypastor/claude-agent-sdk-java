package com.anthropic.claude.sdk.mcp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Creates an in-process MCP server running within your Java application.
 */
public class SdkMcpServer {

    private final String name;
    private final String version;
    private final List<SdkMcpTool<?>> tools;

    private SdkMcpServer(String name, String version, List<SdkMcpTool<?>> tools) {
        this.name = name;
        this.version = version;
        this.tools = tools != null ? new ArrayList<>(tools) : new ArrayList<>();
    }

    /**
     * Creates a new SDK MCP server.
     *
     * @param name The name of the server
     * @param version The version of the server
     * @param tools The list of tools to include
     * @return A new SdkMcpServer instance
     */
    public static SdkMcpServer create(String name, String version, List<SdkMcpTool<?>> tools) {
        return new SdkMcpServer(name, version, tools);
    }

    /**
     * Creates a new SDK MCP server with default version 1.0.0.
     *
     * @param name The name of the server
     * @param tools The list of tools to include
     * @return A new SdkMcpServer instance
     */
    public static SdkMcpServer create(String name, List<SdkMcpTool<?>> tools) {
        return new SdkMcpServer(name, "1.0.0", tools);
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public List<SdkMcpTool<?>> getTools() {
        return Collections.unmodifiableList(tools);
    }
}
