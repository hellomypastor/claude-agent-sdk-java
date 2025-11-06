package com.anthropic.claude.sdk.mcp;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Base interface for MCP server configurations.
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = McpStdioServerConfig.class, name = "stdio"),
    @JsonSubTypes.Type(value = McpSSEServerConfig.class, name = "sse"),
    @JsonSubTypes.Type(value = McpHttpServerConfig.class, name = "http"),
    @JsonSubTypes.Type(value = McpSdkServerConfig.class, name = "sdk")
})
public interface McpServerConfig {
    String getType();
}
