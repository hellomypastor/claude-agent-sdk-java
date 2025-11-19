package com.anthropic.claude.sdk.types.hooks;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Functional interface for hook callbacks.
 * Hooks allow deterministic processing at specific points in the agent loop.
 */
@FunctionalInterface
public interface Hook {

    /**
     * Execute the hook.
     *
     * @param input     The hook input data (tool name, input, etc.)
     * @param toolUseId The ID of the tool use being processed
     * @param context   Hook execution context (signals, etc.)
     * @return Hook output indicating permission decisions, modifications, etc.
     */
    CompletableFuture<Map<String, Object>> execute(
            Map<String, Object> input,
            String toolUseId,
            HookContext context
    );
}
