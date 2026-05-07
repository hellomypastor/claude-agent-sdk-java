package com.anthropic.claude.sdk.types.permissions;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Callback for tool permission requests.
 * Allows programmatic control over whether tools can execute.
 */
@FunctionalInterface
public interface ToolPermissionCallback {

    /**
     * Decide whether a tool can be used.
     *
     * @param toolName  the name of the tool being requested
     * @param toolInput the input parameters for the tool
     * @param context   additional context (suggestions, signals, etc.)
     * @return permission result (allow or deny)
     */
    CompletableFuture<PermissionResult> canUseTool(
            String toolName,
            Map<String, Object> toolInput,
            PermissionContext context
    );
}
