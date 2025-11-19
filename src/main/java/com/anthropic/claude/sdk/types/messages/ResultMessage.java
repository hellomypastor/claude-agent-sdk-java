package com.anthropic.claude.sdk.types.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Result message indicating session completion.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class ResultMessage implements Message {
    @JsonProperty("subtype")
    private String subtype;

    @JsonProperty("duration_ms")
    private long durationMs;

    @JsonProperty("duration_api_ms")
    private long durationApiMs;

    @JsonProperty("is_error")
    private boolean isError;

    @JsonProperty("num_turns")
    private int numTurns;

    @JsonProperty("session_id")
    private String sessionId;

    @JsonProperty("total_cost_usd")
    private Double totalCostUsd;

    @JsonProperty("usage")
    private Map<String, Object> usage;

    @JsonProperty("result")
    private Object result;

    @JsonProperty("structured_output")
    private Object structuredOutput;

    @Override
    public String getType() {
        return "result";
    }
}
