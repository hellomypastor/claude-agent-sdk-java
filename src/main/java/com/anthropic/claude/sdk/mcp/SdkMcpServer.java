package com.anthropic.claude.sdk.mcp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * In-process MCP server implementation for Java SDK.
 */
public final class SdkMcpServer {

    private static final String PROTOCOL_VERSION = "2024-11-05";

    private final String name;
    private final String version;
    private final Map<String, SdkMcpTool> tools;
    private final ObjectMapper mapper = new ObjectMapper();

    private SdkMcpServer(Builder builder) {
        this.name = builder.name;
        this.version = builder.version;
        this.tools = builder.tools;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public Map<String, SdkMcpTool> getTools() {
        return tools;
    }

    public Map<String, Object> toCliConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("type", "sdk");
        config.put("name", name);
        config.put("version", version);
        return config;
    }

    public Map<String, Object> handleMessage(JsonNode message) {
        String method = message.has("method") ? message.get("method").asText() : null;
        JsonNode idNode = message.get("id");
        Object id = idNode != null ? mapper.convertValue(idNode, Object.class) : null;

        try {
            Map<String, Object> response = new HashMap<>();
            response.put("jsonrpc", "2.0");
            response.put("id", id);

            if ("initialize".equals(method)) {
                response.put("result", buildInitializeResult());
            } else if ("tools/list".equals(method)) {
                response.put("result", buildToolsList());
            } else if ("tools/call".equals(method)) {
                JsonNode params = message.get("params");
                response.put("result", handleCallTool(params));
            } else {
                response.put("error", Map.of(
                        "code", -32601,
                        "message", "Method not found: " + method
                ));
            }

            return response;
        } catch (Exception ex) {
            Map<String, Object> response = new HashMap<>();
            response.put("jsonrpc", "2.0");
            response.put("id", id);
            response.put("error", Map.of(
                    "code", -32000,
                    "message", ex.getMessage() != null ? ex.getMessage() : "SDK MCP error"
            ));
            return response;
        }
    }

    private Map<String, Object> buildInitializeResult() {
        Map<String, Object> result = new HashMap<>();
        result.put("protocolVersion", PROTOCOL_VERSION);
        result.put("capabilities", Collections.singletonMap("tools", Collections.emptyMap()));
        result.put("serverInfo", Map.of("name", name, "version", version));
        return result;
    }

    private Map<String, Object> buildToolsList() {
        List<Object> toolEntries = new ArrayList<>();
        for (SdkMcpTool tool : tools.values()) {
            toolEntries.add(Map.of(
                    "name", tool.getName(),
                    "description", tool.getDescription(),
                    "inputSchema", tool.toSchema()
            ));
        }
        return Map.of("tools", toolEntries);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> handleCallTool(JsonNode params) throws Exception {
        if (params == null) {
            throw new IllegalArgumentException("Missing params for tools/call");
        }

        String toolName = params.has("name") ? params.get("name").asText() : null;
        if (toolName == null || !tools.containsKey(toolName)) {
            throw new IllegalArgumentException("Tool not found: " + toolName);
        }

        Map<String, Object> arguments = params.has("arguments")
                ? mapper.convertValue(params.get("arguments"), Map.class)
                : Collections.emptyMap();

        SdkMcpTool tool = tools.get(toolName);
        CompletableFuture<Map<String, Object>> future = tool.getHandler().handle(arguments);
        Map<String, Object> result = future.join();

        if (result == null) {
            result = Collections.emptyMap();
        }

        return result;
    }

    public static final class Builder {
        private String name;
        private String version = "1.0.0";
        private Map<String, SdkMcpTool> tools = new LinkedHashMap<>();

        private Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder tools(Map<String, SdkMcpTool> tools) {
            this.tools = tools != null ? new LinkedHashMap<>(tools) : new LinkedHashMap<>();
            return this;
        }

        public Builder addTool(SdkMcpTool tool) {
            this.tools.put(tool.getName(), tool);
            return this;
        }

        public SdkMcpServer build() {
            return new SdkMcpServer(this);
        }
    }
}
