package com.anthropic.claude.sdk.mcp;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for marking methods as MCP tools.
 *
 * Example usage:
 * <pre>
 * {@code
 * @Tool(name = "greet", description = "Greet a user")
 * public CompletableFuture<Map<String, Object>> greetUser(Map<String, Object> args) {
 *     String name = (String) args.get("name");
 *     return CompletableFuture.completedFuture(Map.of(
 *         "content", List.of(Map.of("type", "text", "text", "Hello, " + name + "!"))
 *     ));
 * }
 * }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ToolAnnotation {
    /**
     * The name of the tool.
     */
    String name();

    /**
     * A description of what the tool does.
     */
    String description();
}
