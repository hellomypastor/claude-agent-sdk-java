package com.anthropic.claude.sdk.types.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * System message containing the response from a hook execution.
 */
public final class SystemHookResponseMessage implements Message {

    private final String hookName;
    private final String hookEvent;
    private final String stdout;
    private final String stderr;
    private final Integer exitCode;
    private final String uuid;
    private final String sessionId;

    public SystemHookResponseMessage(
            @JsonProperty("hook_name") String hookName,
            @JsonProperty("hook_event") String hookEvent,
            @JsonProperty("stdout") String stdout,
            @JsonProperty("stderr") String stderr,
            @JsonProperty("exit_code") Integer exitCode,
            @JsonProperty("uuid") String uuid,
            @JsonProperty("session_id") String sessionId
    ) {
        this.hookName = hookName;
        this.hookEvent = hookEvent;
        this.stdout = stdout;
        this.stderr = stderr;
        this.exitCode = exitCode;
        this.uuid = uuid;
        this.sessionId = sessionId;
    }

    @JsonProperty("hook_name")
    public String hookName() {
        return hookName;
    }

    @JsonProperty("hook_event")
    public String hookEvent() {
        return hookEvent;
    }

    @JsonProperty("stdout")
    public String stdout() {
        return stdout;
    }

    @JsonProperty("stderr")
    public String stderr() {
        return stderr;
    }

    @JsonProperty("exit_code")
    public Integer exitCode() {
        return exitCode;
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
        return "hook_response";
    }
}
