package com.anthropic.claude.sdk.types.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Usage statistics for a specific model.
 */
public record ModelUsage(
        @JsonProperty("input_tokens") long inputTokens,
        @JsonProperty("output_tokens") long outputTokens,
        @JsonProperty("cache_read_input_tokens") long cacheReadInputTokens,
        @JsonProperty("cache_creation_input_tokens") long cacheCreationInputTokens,
        @JsonProperty("web_search_requests") long webSearchRequests,
        @JsonProperty("cost_usd") double costUSD,
        @JsonProperty("context_window") long contextWindow,
        @JsonProperty("max_output_tokens") long maxOutputTokens
) {}
