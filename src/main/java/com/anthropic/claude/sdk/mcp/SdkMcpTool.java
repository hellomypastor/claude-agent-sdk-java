package com.anthropic.claude.sdk.mcp;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Definition for an SDK MCP tool that runs in-process.
 */
public final class SdkMcpTool {

    private final String name;
    private final String description;
    private final Map<String, Object> inputSchema;
    private final ToolHandler handler;

    private SdkMcpTool(Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.inputSchema = builder.inputSchema;
        this.handler = builder.handler;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, Object> getInputSchema() {
        return inputSchema;
    }

    public ToolHandler getHandler() {
        return handler;
    }

    /**
     * Handler invoked when Claude calls this tool.
     */
    @FunctionalInterface
    public interface ToolHandler {
        CompletableFuture<Map<String, Object>> handle(Map<String, Object> input);
    }

    public Map<String, Object> toSchema() {
        if (inputSchema == null || inputSchema.isEmpty()) {
            return defaultSchema(Collections.emptyMap());
        }

        if (inputSchema.containsKey("type") && inputSchema.containsKey("properties")) {
            return inputSchema;
        }

        return defaultSchema(inputSchema);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> defaultSchema(Map<String, Object> definition) {
        Map<String, Object> properties = new HashMap<>();

        for (Map.Entry<String, Object> entry : definition.entrySet()) {
            Object type = entry.getValue();
            if (type instanceof Class<?>) {
                properties.put(entry.getKey(), mapClassToSchema((Class<?>) type));
            } else if (type instanceof Map<?, ?>) {
                properties.put(entry.getKey(), new HashMap<>((Map<String, Object>) type));
            } else if (type instanceof String) {
                properties.put(entry.getKey(), Collections.singletonMap("type", type));
            } else {
                properties.put(entry.getKey(), Collections.singletonMap("type", "string"));
            }
        }

        Map<String, Object> schema = new HashMap<>();
        schema.put("type", "object");
        schema.put("properties", properties);
        schema.put("required", properties.keySet());
        return schema;
    }

    private Map<String, Object> mapClassToSchema(Class<?> cls) {
        String type;
        if (cls == String.class) {
            type = "string";
        } else if (cls == Integer.class || cls == int.class) {
            type = "integer";
        } else if (cls == Double.class || cls == double.class
                || cls == Float.class || cls == float.class) {
            type = "number";
        } else if (cls == Boolean.class || cls == boolean.class) {
            type = "boolean";
        } else {
            type = "string";
        }
        return Collections.singletonMap("type", type);
    }

    public static final class Builder {
        private String name;
        private String description;
        private Map<String, Object> inputSchema;
        private ToolHandler handler;

        private Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder inputSchema(Map<String, Object> inputSchema) {
            this.inputSchema = inputSchema;
            return this;
        }

        public Builder handler(ToolHandler handler) {
            this.handler = handler;
            return this;
        }

        public SdkMcpTool build() {
            return new SdkMcpTool(this);
        }
    }
}
