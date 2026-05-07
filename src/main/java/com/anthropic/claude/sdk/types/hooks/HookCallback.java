package com.anthropic.claude.sdk.types.hooks;

import java.util.concurrent.CompletableFuture;

/**
 * Functional interface for hook callbacks.
 * Hooks allow deterministic processing at specific points in the agent loop.
 */
@FunctionalInterface
public interface HookCallback {

    /**
     * Execute the hook.
     *
     * @param input     the hook input data
     * @param toolUseId the ID of the tool use being processed (may be null for non-tool hooks)
     * @param context   hook execution context
     * @return a future resolving to the hook output
     */
    CompletableFuture<HookOutput> execute(HookInput input, String toolUseId, HookContext context);
}
