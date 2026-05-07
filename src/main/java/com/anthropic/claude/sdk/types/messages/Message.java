package com.anthropic.claude.sdk.types.messages;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Base sealed interface for all SDK message types.
 * Mirrors the TypeScript SDK's SDKMessage union type.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = UserMessage.class, name = "user"),
        @JsonSubTypes.Type(value = AssistantMessage.class, name = "assistant"),
        @JsonSubTypes.Type(value = SystemInitMessage.class, name = "system"),
        @JsonSubTypes.Type(value = SystemStatusMessage.class, name = "system"),
        @JsonSubTypes.Type(value = SystemCompactBoundaryMessage.class, name = "system"),
        @JsonSubTypes.Type(value = SystemHookResponseMessage.class, name = "system"),
        @JsonSubTypes.Type(value = ResultSuccess.class, name = "result"),
        @JsonSubTypes.Type(value = ResultError.class, name = "result"),
        @JsonSubTypes.Type(value = StreamEvent.class, name = "stream_event"),
        @JsonSubTypes.Type(value = ToolProgressMessage.class, name = "tool_progress"),
        @JsonSubTypes.Type(value = AuthStatusMessage.class, name = "auth_status")
})
public sealed interface Message permits
        UserMessage, AssistantMessage, SystemInitMessage, SystemStatusMessage,
        SystemCompactBoundaryMessage, SystemHookResponseMessage,
        ResultMessage, StreamEvent, ToolProgressMessage, AuthStatusMessage {

    /**
     * Get the type of this message.
     */
    String getType();
}
