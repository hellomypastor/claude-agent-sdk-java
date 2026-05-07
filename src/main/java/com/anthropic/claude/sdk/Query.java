package com.anthropic.claude.sdk;

import com.anthropic.claude.sdk.internal.StreamingQuery;
import com.anthropic.claude.sdk.types.messages.Message;
import com.anthropic.claude.sdk.types.options.PermissionMode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public final class Query implements AutoCloseable, Iterable<Message> {

    private final StreamingQuery streamingQuery;
    private final ObjectMapper mapper = new ObjectMapper();

    public Query(StreamingQuery streamingQuery) {
        this.streamingQuery = streamingQuery;
    }

    // Delegate message streaming
    public Stream<Message> stream() {
        return streamingQuery.streamMessages();
    }

    @Override
    public Iterator<Message> iterator() {
        return stream().iterator();
    }

    // Control methods - each sends a control request and waits for response

    public CompletableFuture<Void> interrupt() {
        return streamingQuery.interrupt();
    }

    public CompletableFuture<Void> setPermissionMode(PermissionMode mode) {
        ObjectNode request = mapper.createObjectNode();
        request.put("subtype", "set_permission_mode");
        request.put("mode", mode.getValue());
        return streamingQuery.sendControlRequest(request).thenAccept(r -> {});
    }

    public CompletableFuture<Void> setModel(String model) {
        ObjectNode request = mapper.createObjectNode();
        request.put("subtype", "set_model");
        if (model != null) {
            request.put("model", model);
        }
        return streamingQuery.sendControlRequest(request).thenAccept(r -> {});
    }

    public CompletableFuture<Void> setMaxThinkingTokens(Integer maxThinkingTokens) {
        ObjectNode request = mapper.createObjectNode();
        request.put("subtype", "set_max_thinking_tokens");
        if (maxThinkingTokens != null) {
            request.put("max_thinking_tokens", maxThinkingTokens);
        } else {
            request.putNull("max_thinking_tokens");
        }
        return streamingQuery.sendControlRequest(request).thenAccept(r -> {});
    }

    public CompletableFuture<List<SlashCommand>> supportedCommands() {
        ObjectNode request = mapper.createObjectNode();
        request.put("subtype", "supported_commands");
        return streamingQuery.sendControlRequest(request).thenApply(response -> {
            List<SlashCommand> commands = new ArrayList<>();
            JsonNode cmds = response.get("commands");
            if (cmds != null && cmds.isArray()) {
                for (JsonNode cmd : cmds) {
                    commands.add(new SlashCommand(
                        cmd.has("name") ? cmd.get("name").asText() : "",
                        cmd.has("description") ? cmd.get("description").asText() : "",
                        cmd.has("argumentHint") ? cmd.get("argumentHint").asText() : ""
                    ));
                }
            }
            return commands;
        });
    }

    public CompletableFuture<List<ModelInfo>> supportedModels() {
        ObjectNode request = mapper.createObjectNode();
        request.put("subtype", "supported_models");
        return streamingQuery.sendControlRequest(request).thenApply(response -> {
            List<ModelInfo> models = new ArrayList<>();
            JsonNode items = response.get("models");
            if (items != null && items.isArray()) {
                for (JsonNode item : items) {
                    models.add(new ModelInfo(
                        item.has("value") ? item.get("value").asText() : "",
                        item.has("displayName") ? item.get("displayName").asText() : "",
                        item.has("description") ? item.get("description").asText() : ""
                    ));
                }
            }
            return models;
        });
    }

    public CompletableFuture<List<McpServerStatus>> mcpServerStatus() {
        ObjectNode request = mapper.createObjectNode();
        request.put("subtype", "mcp_status");
        return streamingQuery.sendControlRequest(request).thenApply(response -> {
            List<McpServerStatus> statuses = new ArrayList<>();
            JsonNode servers = response.get("servers");
            if (servers != null && servers.isArray()) {
                for (JsonNode srv : servers) {
                    McpServerStatus.McpServerInfo info = null;
                    if (srv.has("serverInfo") && !srv.get("serverInfo").isNull()) {
                        JsonNode si = srv.get("serverInfo");
                        info = new McpServerStatus.McpServerInfo(
                            si.has("name") ? si.get("name").asText() : "",
                            si.has("version") ? si.get("version").asText() : ""
                        );
                    }
                    statuses.add(new McpServerStatus(
                        srv.has("name") ? srv.get("name").asText() : "",
                        srv.has("status") ? srv.get("status").asText() : "",
                        info,
                        srv.has("error") ? srv.get("error").asText() : null
                    ));
                }
            }
            return statuses;
        });
    }

    public CompletableFuture<AccountInfo> accountInfo() {
        ObjectNode request = mapper.createObjectNode();
        request.put("subtype", "account_info");
        return streamingQuery.sendControlRequest(request).thenApply(response -> {
            return new AccountInfo(
                response.has("email") ? response.get("email").asText() : null,
                response.has("organization") ? response.get("organization").asText() : null,
                response.has("subscriptionType") ? response.get("subscriptionType").asText() : null,
                response.has("tokenSource") ? response.get("tokenSource").asText() : null,
                response.has("apiKeySource") ? response.get("apiKeySource").asText() : null
            );
        });
    }

    public CompletableFuture<RewindFilesResult> rewindFiles(String userMessageId, boolean dryRun) {
        ObjectNode request = mapper.createObjectNode();
        request.put("subtype", "rewind_files");
        request.put("user_message_id", userMessageId);
        request.put("dry_run", dryRun);
        return streamingQuery.sendControlRequest(request).thenApply(response -> {
            boolean canRewind = response.has("canRewind") && response.get("canRewind").asBoolean();
            String error = response.has("error") ? response.get("error").asText() : null;
            List<String> filesChanged = new ArrayList<>();
            if (response.has("filesChanged") && response.get("filesChanged").isArray()) {
                for (JsonNode f : response.get("filesChanged")) {
                    filesChanged.add(f.asText());
                }
            }
            Integer insertions = response.has("insertions") ? response.get("insertions").asInt() : null;
            Integer deletions = response.has("deletions") ? response.get("deletions").asInt() : null;
            return new RewindFilesResult(canRewind, error, filesChanged, insertions, deletions);
        });
    }

    public CompletableFuture<McpSetServersResult> setMcpServers(Map<String, Object> servers) {
        ObjectNode request = mapper.createObjectNode();
        request.put("subtype", "mcp_set_servers");
        request.set("servers", mapper.valueToTree(servers));
        return streamingQuery.sendControlRequest(request).thenApply(response -> {
            List<String> added = new ArrayList<>();
            List<String> removed = new ArrayList<>();
            Map<String, String> errors = new HashMap<>();
            if (response.has("added") && response.get("added").isArray()) {
                for (JsonNode n : response.get("added")) added.add(n.asText());
            }
            if (response.has("removed") && response.get("removed").isArray()) {
                for (JsonNode n : response.get("removed")) removed.add(n.asText());
            }
            if (response.has("errors") && response.get("errors").isObject()) {
                response.get("errors").fields().forEachRemaining(e -> errors.put(e.getKey(), e.getValue().asText()));
            }
            return new McpSetServersResult(added, removed, errors);
        });
    }

    // Send a user message
    public CompletableFuture<Void> sendMessage(Map<String, Object> message) {
        return streamingQuery.sendMessage(message);
    }

    @Override
    public void close() {
        streamingQuery.close();
    }
}
