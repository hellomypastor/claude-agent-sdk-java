package com.anthropic.claude.sdk.protocol;

import com.anthropic.claude.sdk.exceptions.MessageParseException;
import com.anthropic.claude.sdk.types.content.*;
import com.anthropic.claude.sdk.types.messages.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parser for CLI JSON messages.
 */
public class MessageParser {

    private static final Logger logger = LoggerFactory.getLogger(MessageParser.class);
    private final ObjectMapper objectMapper;

    public MessageParser() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Parse a JSON line into a Message object.
     */
    public Message parse(String jsonLine) {
        try {
            JsonNode root = objectMapper.readTree(jsonLine);
            String type = root.get("type").asText();

            switch (type) {
                case "user":
                    return parseUserMessage(root);
                case "assistant":
                    return parseAssistantMessage(root);
                case "system":
                    return parseSystemMessage(root);
                case "result":
                    return parseResultMessage(root);
                case "stream_event":
                    return parseStreamEvent(root);
                case "tool_progress":
                    return parseToolProgressMessage(root);
                case "auth_status":
                    return parseAuthStatusMessage(root);
                default:
                    throw new MessageParseException(
                            "Unknown message type: " + type,
                            jsonLine
                    );
            }

        } catch (Exception e) {
            throw new MessageParseException("Failed to parse message", jsonLine, e);
        }
    }

    private UserMessage parseUserMessage(JsonNode root) {
        JsonNode messageNode = root.get("message");
        JsonNode contentNode = messageNode != null ? messageNode.get("content") : null;

        List<ContentBlock> content = new ArrayList<>();
        if (contentNode != null && contentNode.isArray()) {
            for (JsonNode blockNode : contentNode) {
                content.add(parseContentBlock(blockNode));
            }
        }

        String parentToolUseId = root.has("parent_tool_use_id")
                ? root.get("parent_tool_use_id").asText()
                : null;
        boolean isSynthetic = root.has("is_synthetic") && root.get("is_synthetic").asBoolean();
        Object toolUseResult = root.has("tool_use_result")
                ? objectMapper.convertValue(root.get("tool_use_result"), Object.class)
                : null;
        String uuid = root.has("uuid") ? root.get("uuid").asText() : null;
        String sessionId = root.has("session_id") ? root.get("session_id").asText() : null;
        Boolean isReplay = root.has("is_replay") ? root.get("is_replay").asBoolean() : null;

        return new UserMessage(content, parentToolUseId, isSynthetic, toolUseResult, uuid, sessionId, isReplay);
    }

    private AssistantMessage parseAssistantMessage(JsonNode root) {
        JsonNode messageNode = root.get("message");
        JsonNode contentNode = messageNode != null ? messageNode.get("content") : null;

        List<ContentBlock> content = new ArrayList<>();
        if (contentNode != null && contentNode.isArray()) {
            for (JsonNode blockNode : contentNode) {
                content.add(parseContentBlock(blockNode));
            }
        }

        String model = messageNode != null && messageNode.has("model")
                ? messageNode.get("model").asText()
                : null;
        String parentToolUseId = root.has("parent_tool_use_id")
                ? root.get("parent_tool_use_id").asText()
                : null;
        String error = root.has("error") ? root.get("error").asText() : null;
        String uuid = root.has("uuid") ? root.get("uuid").asText() : null;
        String sessionId = root.has("session_id") ? root.get("session_id").asText() : null;

        Object rawMessage = messageNode != null
                ? objectMapper.convertValue(messageNode, Object.class)
                : null;

        return new AssistantMessage(rawMessage, content, model, parentToolUseId, error, uuid, sessionId);
    }

    private Message parseSystemMessage(JsonNode root) {
        String subtype = root.has("subtype") ? root.get("subtype").asText() : null;
        String uuid = root.has("uuid") ? root.get("uuid").asText() : null;
        String sessionId = root.has("session_id") ? root.get("session_id").asText() : null;

        if ("init".equals(subtype)) {
            return parseSystemInitMessage(root, uuid, sessionId);
        } else if ("status".equals(subtype)) {
            String status = root.has("status") ? root.get("status").asText() : null;
            return new SystemStatusMessage(status, uuid, sessionId);
        } else if ("compact_boundary".equals(subtype)) {
            SystemCompactBoundaryMessage.CompactMetadata metadata = null;
            if (root.has("compact_metadata")) {
                metadata = objectMapper.convertValue(
                        root.get("compact_metadata"),
                        SystemCompactBoundaryMessage.CompactMetadata.class
                );
            }
            return new SystemCompactBoundaryMessage(metadata, uuid, sessionId);
        } else if ("hook_response".equals(subtype)) {
            return parseSystemHookResponseMessage(root, uuid, sessionId);
        } else {
            // Fallback: return as a status message with the raw data
            String status = root.has("status") ? root.get("status").asText() : null;
            return new SystemStatusMessage(status, uuid, sessionId);
        }
    }

    private SystemInitMessage parseSystemInitMessage(JsonNode root, String uuid, String sessionId) {
        List<String> agents = root.has("agents")
                ? objectMapper.convertValue(root.get("agents"),
                objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class))
                : null;
        String apiKeySource = root.has("apiKeySource") ? root.get("apiKeySource").asText()
                : (root.has("api_key_source") ? root.get("api_key_source").asText() : null);
        List<String> betas = root.has("betas")
                ? objectMapper.convertValue(root.get("betas"),
                objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class))
                : null;
        String claudeCodeVersion = root.has("claude_code_version") ? root.get("claude_code_version").asText() : null;
        String cwd = root.has("cwd") ? root.get("cwd").asText() : null;
        List<String> tools = root.has("tools")
                ? objectMapper.convertValue(root.get("tools"),
                objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class))
                : null;
        List<SystemInitMessage.McpServerInfo> mcpServers = root.has("mcp_servers")
                ? objectMapper.convertValue(root.get("mcp_servers"),
                objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, SystemInitMessage.McpServerInfo.class))
                : null;
        String model = root.has("model") ? root.get("model").asText() : null;
        String permissionMode = root.has("permissionMode") ? root.get("permissionMode").asText()
                : (root.has("permission_mode") ? root.get("permission_mode").asText() : null);
        List<String> slashCommands = root.has("slash_commands")
                ? objectMapper.convertValue(root.get("slash_commands"),
                objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class))
                : null;
        String outputStyle = root.has("output_style") ? root.get("output_style").asText() : null;
        List<String> skills = root.has("skills")
                ? objectMapper.convertValue(root.get("skills"),
                objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class))
                : null;
        List<SystemInitMessage.PluginInfo> plugins = root.has("plugins")
                ? objectMapper.convertValue(root.get("plugins"),
                objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, SystemInitMessage.PluginInfo.class))
                : null;

        return new SystemInitMessage(agents, apiKeySource, betas, claudeCodeVersion, cwd,
                tools, mcpServers, model, permissionMode, slashCommands, outputStyle,
                skills, plugins, uuid, sessionId);
    }

    private SystemHookResponseMessage parseSystemHookResponseMessage(JsonNode root, String uuid, String sessionId) {
        String hookName = root.has("hook_name") ? root.get("hook_name").asText() : null;
        String hookEvent = root.has("hook_event") ? root.get("hook_event").asText() : null;
        String stdout = root.has("stdout") ? root.get("stdout").asText() : null;
        String stderr = root.has("stderr") ? root.get("stderr").asText() : null;
        Integer exitCode = root.has("exit_code") ? root.get("exit_code").asInt() : null;
        return new SystemHookResponseMessage(hookName, hookEvent, stdout, stderr, exitCode, uuid, sessionId);
    }

    private ResultMessage parseResultMessage(JsonNode root) {
        String subtype = root.has("subtype") ? root.get("subtype").asText() : "success";
        long durationMs = root.has("duration_ms") ? root.get("duration_ms").asLong() : 0;
        long durationApiMs = root.has("duration_api_ms") ? root.get("duration_api_ms").asLong() : 0;
        boolean isError = root.has("is_error") && root.get("is_error").asBoolean();
        int numTurns = root.has("num_turns") ? root.get("num_turns").asInt() : 0;
        String sessionId = root.has("session_id") ? root.get("session_id").asText() : null;
        String uuid = root.has("uuid") ? root.get("uuid").asText() : null;
        double totalCostUsd = root.has("total_cost_usd") ? root.get("total_cost_usd").asDouble() : 0.0;

        Map<String, Object> usage = root.has("usage")
                ? objectMapper.convertValue(
                root.get("usage"),
                objectMapper.getTypeFactory().constructMapType(
                        HashMap.class,
                        String.class,
                        Object.class
                )
        )
                : null;

        Map<String, ModelUsage> modelUsage = root.has("model_usage")
                ? objectMapper.convertValue(
                root.get("model_usage"),
                objectMapper.getTypeFactory().constructMapType(
                        HashMap.class,
                        String.class,
                        ModelUsage.class
                )
        )
                : null;

        List<PermissionDenial> permissionDenials = root.has("permission_denials")
                ? objectMapper.convertValue(
                root.get("permission_denials"),
                objectMapper.getTypeFactory().constructCollectionType(
                        ArrayList.class,
                        PermissionDenial.class
                )
        )
                : null;

        if ("success".equals(subtype)) {
            String result = root.has("result") ? root.get("result").asText() : null;
            Object structuredOutput = root.has("structured_output")
                    ? objectMapper.convertValue(root.get("structured_output"), Object.class)
                    : null;

            return new ResultSuccess(
                    durationMs, durationApiMs, isError, numTurns, result, totalCostUsd,
                    usage, modelUsage, permissionDenials, structuredOutput, uuid, sessionId
            );
        } else {
            List<String> errors = root.has("errors")
                    ? objectMapper.convertValue(
                    root.get("errors"),
                    objectMapper.getTypeFactory().constructCollectionType(
                            ArrayList.class,
                            String.class
                    )
            )
                    : null;

            return new ResultError(
                    subtype, durationMs, durationApiMs, isError, numTurns, totalCostUsd,
                    usage, modelUsage, permissionDenials, errors, uuid, sessionId
            );
        }
    }

    private ContentBlock parseContentBlock(JsonNode blockNode) {
        String type = blockNode.get("type").asText();

        switch (type) {
            case "text":
                return new TextBlock(blockNode.get("text").asText());
            case "thinking":
                return new ThinkingBlock(
                        blockNode.get("thinking").asText(),
                        blockNode.get("signature").asText()
                );
            case "tool_use": {
                Map<String, Object> input = objectMapper.convertValue(
                        blockNode.get("input"),
                        objectMapper.getTypeFactory().constructMapType(
                                HashMap.class,
                                String.class,
                                Object.class
                        )
                );
                return new ToolUseBlock(
                        blockNode.get("id").asText(),
                        blockNode.get("name").asText(),
                        input
                );
            }
            case "tool_result": {
                List<Object> content = null;
                if (blockNode.has("content")) {
                    JsonNode contentNode = blockNode.get("content");
                    if (contentNode.isArray()) {
                        // Content is an array
                        content = objectMapper.convertValue(
                                contentNode,
                                objectMapper.getTypeFactory().constructCollectionType(
                                        ArrayList.class,
                                        Object.class
                                )
                        );
                    } else if (contentNode.isTextual()) {
                        // Content is a string - wrap it in a list
                        content = new ArrayList<>();
                        content.add(contentNode.asText());
                    }
                }
                Boolean isError = blockNode.has("is_error")
                        ? blockNode.get("is_error").asBoolean()
                        : null;
                return new ToolResultBlock(
                        blockNode.get("tool_use_id").asText(),
                        content,
                        isError
                );
            }
            default:
                throw new IllegalArgumentException("Unknown content block type: " + type);
        }
    }

    private StreamEvent parseStreamEvent(JsonNode root) {
        String uuid = root.has("uuid") ? root.get("uuid").asText() : null;
        String sessionId = root.has("session_id") ? root.get("session_id").asText() : null;

        Object event = root.has("event") && !root.get("event").isNull()
                ? objectMapper.convertValue(root.get("event"), Object.class)
                : null;

        String parentToolUseId = root.has("parent_tool_use_id")
                ? root.get("parent_tool_use_id").asText()
                : null;

        return new StreamEvent(event, parentToolUseId, uuid, sessionId);
    }

    private ToolProgressMessage parseToolProgressMessage(JsonNode root) {
        String toolUseId = root.has("tool_use_id") ? root.get("tool_use_id").asText() : null;
        String toolName = root.has("tool_name") ? root.get("tool_name").asText() : null;
        String parentToolUseId = root.has("parent_tool_use_id") ? root.get("parent_tool_use_id").asText() : null;
        double elapsedTimeSeconds = root.has("elapsed_time_seconds") ? root.get("elapsed_time_seconds").asDouble() : 0.0;
        String uuid = root.has("uuid") ? root.get("uuid").asText() : null;
        String sessionId = root.has("session_id") ? root.get("session_id").asText() : null;

        return new ToolProgressMessage(toolUseId, toolName, parentToolUseId, elapsedTimeSeconds, uuid, sessionId);
    }

    private AuthStatusMessage parseAuthStatusMessage(JsonNode root) {
        boolean isAuthenticating = root.has("is_authenticating") && root.get("is_authenticating").asBoolean();
        List<String> output = root.has("output")
                ? objectMapper.convertValue(
                root.get("output"),
                objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class)
        )
                : null;
        String error = root.has("error") ? root.get("error").asText() : null;
        String uuid = root.has("uuid") ? root.get("uuid").asText() : null;
        String sessionId = root.has("session_id") ? root.get("session_id").asText() : null;

        return new AuthStatusMessage(isAuthenticating, output, error, uuid, sessionId);
    }
}
