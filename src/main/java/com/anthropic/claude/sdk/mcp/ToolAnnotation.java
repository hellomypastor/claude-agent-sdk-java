package com.anthropic.claude.sdk.mcp;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for marking methods as MCP tools.
 *
 * <p>Example usage:</p>
 * <pre>
 * &#64;Tool(name = "greet", description = "Greet a user")
 * public CompletableFuture&lt;Map&lt;String, Object&gt;&gt; greetUser(Map&lt;String, Object&gt; args) {
 *     String name = (String) args.get("name");
 *     return CompletableFuture.completedFuture(Map.of(
 *         "content", List.of(Map.of("type", "text", "text", "Hello, " + name + "!"))
 *     ));
 * }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ToolAnnotation {
    /**
     * The name of the tool.
     *
     * @return the tool name
     */
    String name();

    /**
     * A description of what the tool does.
     *
     * @return the tool description
     */
    String description();
}
