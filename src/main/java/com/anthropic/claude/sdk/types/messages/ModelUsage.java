package com.anthropic.claude.sdk.types.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Usage statistics for a specific model.
 */
public final class ModelUsage {

    private final long inputTokens;
    private final long outputTokens;
    private final long cacheReadInputTokens;
    private final long cacheCreationInputTokens;
    private final long webSearchRequests;
    private final double costUSD;
    private final long contextWindow;
    private final long maxOutputTokens;

    public ModelUsage(
            @JsonProperty("input_tokens") long inputTokens,
            @JsonProperty("output_tokens") long outputTokens,
            @JsonProperty("cache_read_input_tokens") long cacheReadInputTokens,
            @JsonProperty("cache_creation_input_tokens") long cacheCreationInputTokens,
            @JsonProperty("web_search_requests") long webSearchRequests,
            @JsonProperty("cost_usd") double costUSD,
            @JsonProperty("context_window") long contextWindow,
            @JsonProperty("max_output_tokens") long maxOutputTokens
    ) {
        this.inputTokens = inputTokens;
        this.outputTokens = outputTokens;
        this.cacheReadInputTokens = cacheReadInputTokens;
        this.cacheCreationInputTokens = cacheCreationInputTokens;
        this.webSearchRequests = webSearchRequests;
        this.costUSD = costUSD;
        this.contextWindow = contextWindow;
        this.maxOutputTokens = maxOutputTokens;
    }

    @JsonProperty("input_tokens")
    public long inputTokens() {
        return inputTokens;
    }

    @JsonProperty("output_tokens")
    public long outputTokens() {
        return outputTokens;
    }

    @JsonProperty("cache_read_input_tokens")
    public long cacheReadInputTokens() {
        return cacheReadInputTokens;
    }

    @JsonProperty("cache_creation_input_tokens")
    public long cacheCreationInputTokens() {
        return cacheCreationInputTokens;
    }

    @JsonProperty("web_search_requests")
    public long webSearchRequests() {
        return webSearchRequests;
    }

    @JsonProperty("cost_usd")
    public double costUSD() {
        return costUSD;
    }

    @JsonProperty("context_window")
    public long contextWindow() {
        return contextWindow;
    }

    @JsonProperty("max_output_tokens")
    public long maxOutputTokens() {
        return maxOutputTokens;
    }
}
