package com.anthropic.claude.sdk.hooks;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Functional interface for hook callbacks.
 */
@FunctionalInterface
public interface HookCallback {

    /**
     * Process a hook event.
     *
     * @param inputData The input data from the hook event
     * @param toolUseId The tool use ID (if applicable)
     * @param context Additional context information
     * @return A future that resolves to the hook output
     */
    CompletableFuture<Map<String, Object>> apply(
            Map<String, Object> inputData,
            String toolUseId,
            Map<String, Object> context
    );
}
