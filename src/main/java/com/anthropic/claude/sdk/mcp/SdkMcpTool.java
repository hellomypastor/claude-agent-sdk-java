package com.anthropic.claude.sdk.mcp;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * Definition for an SDK MCP tool.
 *
 * @param <T> The input type for the tool
 */
public class SdkMcpTool<T> {

    private final String name;
    private final String description;
    private final Class<T> inputClass;
    private final Map<String, Object> inputSchema;
    private final Function<T, CompletableFuture<Map<String, Object>>> handler;

    private SdkMcpTool(Builder<T> builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.inputClass = builder.inputClass;
        this.inputSchema = builder.inputSchema;
        this.handler = builder.handler;
    }

    public static <T> Builder<T> builder(String name, String description) {
        return new Builder<>(name, description);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Class<T> getInputClass() {
        return inputClass;
    }

    public Map<String, Object> getInputSchema() {
        return inputSchema;
    }

    public Function<T, CompletableFuture<Map<String, Object>>> getHandler() {
        return handler;
    }

    public static class Builder<T> {
        private final String name;
        private final String description;
        private Class<T> inputClass;
        private Map<String, Object> inputSchema;
        private Function<T, CompletableFuture<Map<String, Object>>> handler;

        private Builder(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public Builder<T> inputClass(Class<T> inputClass) {
            this.inputClass = inputClass;
            return this;
        }

        public Builder<T> inputSchema(Map<String, Object> inputSchema) {
            this.inputSchema = inputSchema;
            return this;
        }

        public Builder<T> handler(Function<T, CompletableFuture<Map<String, Object>>> handler) {
            this.handler = handler;
            return this;
        }

        public SdkMcpTool<T> build() {
            if (handler == null) {
                throw new IllegalStateException("Handler is required");
            }
            return new SdkMcpTool<>(this);
        }
    }
}
