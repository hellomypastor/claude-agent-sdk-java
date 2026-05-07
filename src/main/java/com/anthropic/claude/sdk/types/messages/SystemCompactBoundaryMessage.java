package com.anthropic.claude.sdk.types.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * System message marking a compaction boundary.
 */
public record SystemCompactBoundaryMessage(
        @JsonProperty("compact_metadata") CompactMetadata compactMetadata,
        @JsonProperty("uuid") String uuid,
        @JsonProperty("session_id") String sessionId
) implements Message {

    /**
     * Metadata about the compaction event.
     */
    public record CompactMetadata(
            @JsonProperty("trigger") String trigger,
            @JsonProperty("pre_tokens") int preTokens
    ) {}

    @Override
    public String getType() {
        return "system";
    }

    public String getSubtype() {
        return "compact_boundary";
    }
}
