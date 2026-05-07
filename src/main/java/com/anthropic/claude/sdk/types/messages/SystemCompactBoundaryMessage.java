package com.anthropic.claude.sdk.types.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * System message marking a compaction boundary.
 */
public final class SystemCompactBoundaryMessage implements Message {

    private final CompactMetadata compactMetadata;
    private final String uuid;
    private final String sessionId;

    public SystemCompactBoundaryMessage(
            @JsonProperty("compact_metadata") CompactMetadata compactMetadata,
            @JsonProperty("uuid") String uuid,
            @JsonProperty("session_id") String sessionId
    ) {
        this.compactMetadata = compactMetadata;
        this.uuid = uuid;
        this.sessionId = sessionId;
    }

    /**
     * Metadata about the compaction event.
     */
    public static final class CompactMetadata {

        private final String trigger;
        private final int preTokens;

        public CompactMetadata(
                @JsonProperty("trigger") String trigger,
                @JsonProperty("pre_tokens") int preTokens
        ) {
            this.trigger = trigger;
            this.preTokens = preTokens;
        }

        @JsonProperty("trigger")
        public String trigger() {
            return trigger;
        }

        @JsonProperty("pre_tokens")
        public int preTokens() {
            return preTokens;
        }
    }

    @JsonProperty("compact_metadata")
    public CompactMetadata compactMetadata() {
        return compactMetadata;
    }

    @JsonProperty("uuid")
    public String uuid() {
        return uuid;
    }

    @JsonProperty("session_id")
    public String sessionId() {
        return sessionId;
    }

    @Override
    public String getType() {
        return "system";
    }

    public String getSubtype() {
        return "compact_boundary";
    }
}
