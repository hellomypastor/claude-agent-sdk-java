package com.anthropic.claude.sdk.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;

/**
 * A result message indicating the completion of a query.
 */
public class ResultMessage implements Message {

    private final String subtype;
    @Nullable
    private final Long durationMs;
    @Nullable
    private final Long durationApiMs;
    private final boolean isError;
    @Nullable
    private final Integer numTurns;
    @Nullable
    private final String sessionId;
    @Nullable
    private final Double totalCostUsd;
    @Nullable
    private final Map<String, Object> usage;
    @Nullable
    private final Object result;

    @JsonCreator
    public ResultMessage(
            @JsonProperty("subtype") String subtype,
            @JsonProperty("duration_ms") @Nullable Long durationMs,
            @JsonProperty("duration_api_ms") @Nullable Long durationApiMs,
            @JsonProperty("is_error") boolean isError,
            @JsonProperty("num_turns") @Nullable Integer numTurns,
            @JsonProperty("session_id") @Nullable String sessionId,
            @JsonProperty("total_cost_usd") @Nullable Double totalCostUsd,
            @JsonProperty("usage") @Nullable Map<String, Object> usage,
            @JsonProperty("result") @Nullable Object result) {
        this.subtype = subtype;
        this.durationMs = durationMs;
        this.durationApiMs = durationApiMs;
        this.isError = isError;
        this.numTurns = numTurns;
        this.sessionId = sessionId;
        this.totalCostUsd = totalCostUsd;
        this.usage = usage;
        this.result = result;
    }

    @Override
    public String getRole() {
        return "result";
    }

    public String getSubtype() {
        return subtype;
    }

    @Nullable
    @JsonProperty("duration_ms")
    public Long getDurationMs() {
        return durationMs;
    }

    @Nullable
    @JsonProperty("duration_api_ms")
    public Long getDurationApiMs() {
        return durationApiMs;
    }

    @JsonProperty("is_error")
    public boolean isError() {
        return isError;
    }

    @Nullable
    @JsonProperty("num_turns")
    public Integer getNumTurns() {
        return numTurns;
    }

    @Nullable
    @JsonProperty("session_id")
    public String getSessionId() {
        return sessionId;
    }

    @Nullable
    @JsonProperty("total_cost_usd")
    public Double getTotalCostUsd() {
        return totalCostUsd;
    }

    @Nullable
    public Map<String, Object> getUsage() {
        return usage;
    }

    @Nullable
    public Object getResult() {
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultMessage that = (ResultMessage) o;
        return isError == that.isError &&
               Objects.equals(subtype, that.subtype) &&
               Objects.equals(durationMs, that.durationMs) &&
               Objects.equals(durationApiMs, that.durationApiMs) &&
               Objects.equals(numTurns, that.numTurns) &&
               Objects.equals(sessionId, that.sessionId) &&
               Objects.equals(totalCostUsd, that.totalCostUsd) &&
               Objects.equals(usage, that.usage) &&
               Objects.equals(result, that.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subtype, durationMs, durationApiMs, isError, numTurns, sessionId, totalCostUsd, usage, result);
    }

    @Override
    public String toString() {
        return "ResultMessage{subtype='" + subtype + "', durationMs=" + durationMs + ", isError=" + isError + ", sessionId='" + sessionId + "'}";
    }
}
