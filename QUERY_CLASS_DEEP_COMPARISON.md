# Deep Comparison: Python Query Class vs Java Implementation

## Executive Summary

Python's `_internal/query.py` (555 lines) implements comprehensive **bidirectional control protocol** handling. Java's `ClaudeSDKClient.java` contains some serialization logic but **MISSING most control protocol functionality**.

**Status:** Java is ~30% complete for control protocol features

**Critical Finding:** Java cannot handle incoming control requests from the CLI, making hooks and permissions non-functional.

---

## Python Query Class Overview (555 lines)

### Class Purpose
```python
class Query:
    """Handles bidirectional control protocol on top of Transport.

    This class manages:
    - Control request/response routing
    - Hook callbacks
    - Tool permission callbacks
    - Message streaming
    - Initialization handshake
    """
```

### Key Responsibilities

1. **Initialization with Hooks** - Registers hook callbacks with CLI
2. **Message Routing** - Routes control messages vs SDK messages
3. **Control Request Handling** - Responds to CLI control requests
4. **Permission Callbacks** - Executes tool permission checks
5. **Hook Callbacks** - Executes hook functions
6. **SDK MCP Bridge** - Routes MCP messages to in-process servers
7. **Control Commands** - Sends interrupt, set_permission_mode, set_model

---

## Detailed Feature Comparison

### 1. Initialization and Configuration

#### Python `__init__()` (Lines 64-105)

```python
def __init__(
    self,
    transport: Transport,
    is_streaming_mode: bool,
    can_use_tool: Callable[...] | None = None,  # Permission callback
    hooks: dict[str, list[dict[str, Any]]] | None = None,  # Hook matchers
    sdk_mcp_servers: dict[str, "McpServer"] | None = None,  # MCP servers
):
    self.transport = transport
    self.is_streaming_mode = is_streaming_mode
    self.can_use_tool = can_use_tool
    self.hooks = hooks or {}
    self.sdk_mcp_servers = sdk_mcp_servers or {}

    # Control protocol state
    self.pending_control_responses: dict[str, anyio.Event] = {}
    self.pending_control_results: dict[str, dict | Exception] = {}
    self.hook_callbacks: dict[str, Callable] = {}  # Callback registry
    self.next_callback_id = 0
    self._request_counter = 0

    # Message stream
    self._message_send, self._message_receive = anyio.create_memory_object_stream(max_buffer_size=100)
    self._tg: TaskGroup | None = None
```

**Key Features:**
- ✅ Stores permission callback (`can_use_tool`)
- ✅ Stores hook matchers
- ✅ Stores SDK MCP server instances
- ✅ Manages pending control requests/responses
- ✅ Maintains hook callback registry
- ✅ Creates message stream for routing

#### Java Constructor (ClaudeSDKClient)

```java
public ClaudeSDKClient(@Nullable ClaudeAgentOptions options, @Nullable Transport transport) {
    this.options = options != null ? options : ClaudeAgentOptions.builder().build();
    this.customTransport = transport;
    this.messageQueue = new LinkedBlockingQueue<>();

    // Configure Jackson
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
}
```

**Missing:**
- ❌ No permission callback storage
- ❌ No hook callback registry
- ❌ No SDK MCP server storage
- ❌ No control request/response tracking
- ❌ No message routing (all go to same queue)

---

### 2. Initialization Handshake

#### Python `initialize()` (Lines 107-145)

```python
async def initialize(self) -> dict[str, Any] | None:
    """Initialize control protocol if in streaming mode.

    Returns:
        Initialize response with supported commands, or None if not streaming
    """
    if not self.is_streaming_mode:
        return None

    # Build hooks configuration for initialization
    hooks_config: dict[str, Any] = {}
    if self.hooks:
        for event, matchers in self.hooks.items():
            if matchers:
                hooks_config[event] = []
                for matcher in matchers:
                    # Register each hook callback with unique ID
                    callback_ids = []
                    for callback in matcher.get("hooks", []):
                        callback_id = f"hook_{self.next_callback_id}"
                        self.next_callback_id += 1
                        self.hook_callbacks[callback_id] = callback  # Store callback
                        callback_ids.append(callback_id)
                    hooks_config[event].append({
                        "matcher": matcher.get("matcher"),
                        "hookCallbackIds": callback_ids,  # Send IDs to CLI
                    })

    # Send initialize request
    request = {
        "subtype": "initialize",
        "hooks": hooks_config if hooks_config else None,
    }

    response = await self._send_control_request(request)
    self._initialized = True
    self._initialization_result = response  # Store for later access
    return response
```

**Key Actions:**
1. ✅ Iterates through hook matchers
2. ✅ **Registers each hook callback with unique ID**
3. ✅ Stores callbacks in `self.hook_callbacks` registry
4. ✅ Sends hook configuration to CLI with callback IDs
5. ✅ Waits for and stores initialization response
6. ✅ Returns server info (commands, capabilities)

#### Java `connect()` + `serializeOptions()`

```java
public void connect(@Nullable String prompt) throws ClaudeSDKException {
    // ... transport setup ...

    // Send initial prompt if provided
    if (prompt != null) {
        query(prompt, "default");
    }
}

private Map<String, Object> serializeOptions() {
    // ...
    // Hooks serialization
    if (options.getHooks() != null) {
        opts.put("hooks", convertHooksToInternalFormat(options.getHooks()));
    }
    return opts;
}

private Map<String, List<Map<String, Object>>> convertHooksToInternalFormat(
        Map<String, List<HookMatcher>> hooks) {
    Map<String, List<Map<String, Object>>> converted = new HashMap<>();

    for (Map.Entry<String, List<HookMatcher>> entry : hooks.entrySet()) {
        String hookEvent = entry.getKey();
        List<HookMatcher> matchers = entry.getValue();

        List<Map<String, Object>> convertedMatchers = new ArrayList<>();
        for (HookMatcher matcher : matchers) {
            Map<String, Object> matcherMap = new HashMap<>();

            if (matcher.getMatcher() != null) {
                matcherMap.put("matcher", matcher.getMatcher());
            }

            // Just mark that hooks are present
            matcherMap.put("hasCallbacks", !matcher.getHooks().isEmpty());
            matcherMap.put("callbackCount", matcher.getHooks().size());

            convertedMatchers.add(matcherMap);
        }

        converted.put(hookEvent, convertedMatchers);
    }

    return converted;
}
```

**Missing:**
- ❌ **No callback ID generation**
- ❌ **No callback registration** (callbacks are never stored)
- ❌ **No initialize control request sent**
- ❌ **No initialization response handling**
- ❌ Only metadata (`hasCallbacks`, `callbackCount`) sent, not actual callback IDs
- ❌ Hooks will not work because CLI has no way to invoke them

**Impact:** 🔴 **CRITICAL** - Hooks completely non-functional

---

### 3. Message Reading and Routing

#### Python `_read_messages()` (Lines 154-204)

```python
async def _read_messages(self) -> None:
    """Read messages from transport and route them."""
    try:
        async for message in self.transport.read_messages():
            if self._closed:
                break

            msg_type = message.get("type")

            # Route control messages
            if msg_type == "control_response":
                # Handle response to our control request
                response = message.get("response", {})
                request_id = response.get("request_id")
                if request_id in self.pending_control_responses:
                    event = self.pending_control_responses[request_id]
                    if response.get("subtype") == "error":
                        self.pending_control_results[request_id] = Exception(...)
                    else:
                        self.pending_control_results[request_id] = response
                    event.set()  # Unblock waiting control request
                continue

            elif msg_type == "control_request":
                # Handle incoming control request from CLI
                request: SDKControlRequest = message
                if self._tg:
                    self._tg.start_soon(self._handle_control_request, request)
                continue

            elif msg_type == "control_cancel_request":
                # Handle cancel requests
                continue

            # Regular SDK messages go to the stream
            await self._message_send.send(message)

    except Exception as e:
        logger.error(f"Fatal error in message reader: {e}")
        await self._message_send.send({"type": "error", "error": str(e)})
    finally:
        await self._message_send.send({"type": "end"})
```

**Key Features:**
1. ✅ **Distinguishes 3 message types:**
   - `control_response` - Response to SDK's control request
   - `control_request` - Request from CLI (needs handling)
   - Regular messages - Forward to application
2. ✅ **Routes control_request to handler** (`_handle_control_request`)
3. ✅ **Matches responses to pending requests** by request_id
4. ✅ Separates control flow from application messages

#### Java `startReaderThread()` + `processLine()` (Lines 242-270)

```java
private void startReaderThread() {
    Thread readerThread = new Thread(() -> {
        try {
            BufferedReader reader = transport.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                processLine(line);
            }
        } catch (IOException e) {
            logger.error("Error reading from transport", e);
        }
    });
    readerThread.setDaemon(true);
    readerThread.start();
}

private void processLine(String line) {
    try {
        Map<String, Object> data = objectMapper.readValue(line,
            new TypeReference<Map<String, Object>>() {});

        Message message = parseMessage(data);
        messageQueue.put(message);  // Put ALL messages in same queue

    } catch (JsonProcessingException e) {
        logger.error("Failed to parse message: {}", line, e);
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
}
```

**Missing:**
- ❌ **No message type routing**
- ❌ **No control_request handling**
- ❌ **No control_response matching**
- ❌ All messages go to same queue (no separation)
- ❌ Control messages treated as regular messages

**Impact:** 🔴 **CRITICAL** - Cannot respond to CLI control requests

---

### 4. Control Request Handling

#### Python `_handle_control_request()` (Lines 206-315)

This is the **MOST CRITICAL** missing feature. It handles 3 types of control requests:

##### 4a. Permission Callback (`can_use_tool`)

```python
async def _handle_control_request(self, request: SDKControlRequest) -> None:
    """Handle incoming control request from CLI."""
    request_id = request["request_id"]
    request_data = request["request"]
    subtype = request_data["subtype"]

    try:
        response_data: dict[str, Any] = {}

        if subtype == "can_use_tool":
            permission_request: SDKControlPermissionRequest = request_data
            original_input = permission_request["input"]

            # Handle tool permission request
            if not self.can_use_tool:
                raise Exception("canUseTool callback is not provided")

            context = ToolPermissionContext(
                signal=None,
                suggestions=permission_request.get("permission_suggestions", []) or [],
            )

            # Invoke user's permission callback
            response = await self.can_use_tool(
                permission_request["tool_name"],
                permission_request["input"],
                context,
            )

            # Convert PermissionResult to expected dict format
            if isinstance(response, PermissionResultAllow):
                response_data = {
                    "behavior": "allow",
                    "updatedInput": response.updated_input if response.updated_input is not None else original_input,
                }
                if response.updated_permissions is not None:
                    response_data["updatedPermissions"] = [
                        permission.to_dict() for permission in response.updated_permissions
                    ]
            elif isinstance(response, PermissionResultDeny):
                response_data = {"behavior": "deny", "message": response.message}
                if response.interrupt:
                    response_data["interrupt"] = response.interrupt
```

**What it does:**
1. CLI asks: "Can I use tool X with input Y?"
2. Invokes user's `can_use_tool` callback
3. User returns Allow or Deny
4. Sends response back to CLI

##### 4b. Hook Callback

```python
        elif subtype == "hook_callback":
            hook_callback_request: SDKHookCallbackRequest = request_data

            # Handle hook callback
            callback_id = hook_callback_request["callback_id"]
            callback = self.hook_callbacks.get(callback_id)  # Lookup by ID
            if not callback:
                raise Exception(f"No hook callback found for ID: {callback_id}")

            # Invoke user's hook callback
            hook_output = await callback(
                request_data.get("input"),
                request_data.get("tool_use_id"),
                {"signal": None},
            )
            # Convert Python-safe field names to CLI-expected names
            response_data = _convert_hook_output_for_cli(hook_output)
```

**What it does:**
1. CLI says: "Execute hook callback ID 'hook_123'"
2. Looks up callback in registry
3. Invokes user's hook function
4. Returns hook output to CLI

##### 4c. MCP Message Routing

```python
        elif subtype == "mcp_message":
            # Handle SDK MCP request
            server_name = request_data.get("server_name")
            mcp_message = request_data.get("message")

            if not server_name or not mcp_message:
                raise Exception("Missing server_name or message for MCP request")

            mcp_response = await self._handle_sdk_mcp_request(
                server_name, mcp_message
            )
            response_data = {"mcp_response": mcp_response}
```

**What it does:**
1. CLI sends MCP protocol message for SDK server
2. Routes to in-process MCP server
3. Returns server's response

##### Finally: Send Response

```python
        # Send success response
        success_response: SDKControlResponse = {
            "type": "control_response",
            "response": {
                "subtype": "success",
                "request_id": request_id,
                "response": response_data,
            },
        }
        await self.transport.write(json.dumps(success_response) + "\n")

    except Exception as e:
        # Send error response
        error_response: SDKControlResponse = {
            "type": "control_response",
            "response": {
                "subtype": "error",
                "request_id": request_id,
                "error": str(e),
            },
        }
        await self.transport.write(json.dumps(error_response) + "\n")
```

#### Java Implementation

**COMPLETELY MISSING** - No equivalent code at all.

**Impact:** 🔴 **CRITICAL** - The following features DO NOT WORK:
1. ❌ Permission callbacks (`can_use_tool`)
2. ❌ Hook callbacks (all hooks)
3. ❌ SDK MCP servers (in-process tools)

---

### 5. SDK MCP Server Bridge

#### Python `_handle_sdk_mcp_request()` (Lines 357-489)

This is a **133-line implementation** that bridges JSONRPC and in-process MCP servers:

```python
async def _handle_sdk_mcp_request(
    self, server_name: str, message: dict[str, Any]
) -> dict[str, Any]:
    """Handle an MCP request for an SDK server.

    This acts as a bridge between JSONRPC messages from the CLI
    and the in-process MCP server.
    """
    if server_name not in self.sdk_mcp_servers:
        return {
            "jsonrpc": "2.0",
            "id": message.get("id"),
            "error": {"code": -32601, "message": f"Server '{server_name}' not found"},
        }

    server = self.sdk_mcp_servers[server_name]
    method = message.get("method")
    params = message.get("params", {})

    try:
        if method == "initialize":
            # Handle MCP initialization
            return {
                "jsonrpc": "2.0",
                "id": message.get("id"),
                "result": {
                    "protocolVersion": "2024-11-05",
                    "capabilities": {"tools": {}},
                    "serverInfo": {
                        "name": server.name,
                        "version": server.version or "1.0.0",
                    },
                },
            }

        elif method == "tools/list":
            request = ListToolsRequest(method=method)
            handler = server.request_handlers.get(ListToolsRequest)
            if handler:
                result = await handler(request)
                # Convert MCP result to JSONRPC response
                tools_data = [
                    {
                        "name": tool.name,
                        "description": tool.description,
                        "inputSchema": tool.inputSchema.model_dump() if hasattr(tool.inputSchema, "model_dump") else tool.inputSchema,
                    }
                    for tool in result.root.tools
                ]
                return {
                    "jsonrpc": "2.0",
                    "id": message.get("id"),
                    "result": {"tools": tools_data},
                }

        elif method == "tools/call":
            call_request = CallToolRequest(
                method=method,
                params=CallToolRequestParams(
                    name=params.get("name"), arguments=params.get("arguments", {})
                ),
            )
            handler = server.request_handlers.get(CallToolRequest)
            if handler:
                result = await handler(call_request)
                # Convert MCP result to JSONRPC response
                content = []
                for item in result.root.content:
                    if hasattr(item, "text"):
                        content.append({"type": "text", "text": item.text})
                    elif hasattr(item, "data") and hasattr(item, "mimeType"):
                        content.append({
                            "type": "image",
                            "data": item.data,
                            "mimeType": item.mimeType,
                        })

                response_data = {"content": content}
                if hasattr(result.root, "is_error") and result.root.is_error:
                    response_data["is_error"] = True

                return {
                    "jsonrpc": "2.0",
                    "id": message.get("id"),
                    "result": response_data,
                }

        elif method == "notifications/initialized":
            return {"jsonrpc": "2.0", "result": {}}

        return {
            "jsonrpc": "2.0",
            "id": message.get("id"),
            "error": {"code": -32601, "message": f"Method '{method}' not found"},
        }

    except Exception as e:
        return {
            "jsonrpc": "2.0",
            "id": message.get("id"),
            "error": {"code": -32603, "message": str(e)},
        }
```

**Handles 4 MCP Methods:**
1. `initialize` - Returns server capabilities
2. `tools/list` - Lists available tools
3. `tools/call` - Executes a tool
4. `notifications/initialized` - Acknowledges initialization

#### Java Implementation

**COMPLETELY MISSING** - No MCP message routing at all.

The Java `SdkMcpServer` class just stores metadata:

```java
public class SdkMcpServer {
    private final String name;
    private final String version;
    private final List<SdkMcpTool<?>> tools;

    // No actual server instance
    // No message handling
    // No JSONRPC routing
}
```

**Impact:** 🔴 **CRITICAL** - SDK MCP servers completely non-functional

---

### 6. Control Commands (Outgoing)

#### Python - Sends Control Requests

```python
async def interrupt(self) -> None:
    """Send interrupt control request."""
    await self._send_control_request({"subtype": "interrupt"})

async def set_permission_mode(self, mode: str) -> None:
    """Change permission mode."""
    await self._send_control_request({
        "subtype": "set_permission_mode",
        "mode": mode,
    })

async def set_model(self, model: str | None) -> None:
    """Change the AI model."""
    await self._send_control_request({
        "subtype": "set_model",
        "model": model,
    })

async def _send_control_request(self, request: dict[str, Any]) -> dict[str, Any]:
    """Send control request to CLI and wait for response."""
    if not self.is_streaming_mode:
        raise Exception("Control requests require streaming mode")

    # Generate unique request ID
    self._request_counter += 1
    request_id = f"req_{self._request_counter}_{os.urandom(4).hex()}"

    # Create event for response
    event = anyio.Event()
    self.pending_control_responses[request_id] = event

    # Build and send request
    control_request = {
        "type": "control_request",
        "request_id": request_id,
        "request": request,
    }

    await self.transport.write(json.dumps(control_request) + "\n")

    # Wait for response with timeout
    try:
        with anyio.fail_after(60.0):
            await event.wait()

        result = self.pending_control_results.pop(request_id)
        # ... handle result ...
    except TimeoutError:
        raise Exception("Control request timeout")
```

**Features:**
- ✅ Generates unique request IDs
- ✅ Tracks pending requests
- ✅ Waits for response with 60s timeout
- ✅ Matches responses by request_id

#### Java - Direct Message Sending

```java
public void interrupt() {
    Map<String, Object> message = new HashMap<>();
    message.put("type", "interrupt");
    message.put("session_id", "default");
    sendMessage(message);
}

public void setPermissionMode(String mode) {
    Map<String, Object> message = new HashMap<>();
    message.put("type", "control");
    message.put("request", "set_permission_mode");
    message.put("mode", mode);
    sendMessage(message);
}

public void setModel(@Nullable String model) {
    Map<String, Object> message = new HashMap<>();
    message.put("type", "control");
    message.put("request", "set_model");
    message.put("model", model);
    sendMessage(message);
}
```

**Missing:**
- ❌ No request ID generation
- ❌ No response tracking
- ❌ No timeout handling
- ❌ Fire-and-forget (no confirmation)
- ⚠️ Message format may be incorrect (needs verification)

**Status:** ⚠️ **PARTIAL** - Commands sent but no response handling

---

## Summary Table

| Feature | Python Query Class | Java ClaudeSDKClient | Status |
|---------|-------------------|---------------------|--------|
| **Initialization** |
| Hook callback registration | ✅ 39 lines | ❌ Missing | ❌ Critical |
| Callback ID generation | ✅ | ❌ | ❌ Critical |
| Callback storage | ✅ | ❌ | ❌ Critical |
| Initialize control request | ✅ | ❌ | ❌ Critical |
| **Message Routing** |
| Control vs SDK separation | ✅ | ❌ | ❌ Critical |
| control_request handling | ✅ | ❌ | ❌ Critical |
| control_response matching | ✅ | ❌ | ❌ Critical |
| **Control Request Handling** |
| can_use_tool callback | ✅ 51 lines | ❌ | ❌ Critical |
| hook_callback execution | ✅ 14 lines | ❌ | ❌ Critical |
| mcp_message routing | ✅ 15 lines | ❌ | ❌ Critical |
| **SDK MCP Bridge** |
| initialize method | ✅ | ❌ | ❌ Critical |
| tools/list method | ✅ | ❌ | ❌ Critical |
| tools/call method | ✅ | ❌ | ❌ Critical |
| JSONRPC formatting | ✅ | ❌ | ❌ Critical |
| **Control Commands** |
| interrupt() | ✅ | ⚠️ Partial | ⚠️ No response |
| set_permission_mode() | ✅ | ⚠️ Partial | ⚠️ No response |
| set_model() | ✅ | ⚠️ Partial | ⚠️ No response |
| Request ID tracking | ✅ | ❌ | ❌ Critical |
| Response timeout | ✅ 60s | ❌ | ❌ Medium |
| **Message Streaming** |
| stream_input() | ✅ | ❌ | ❌ Medium |
| receive_messages() | ✅ | ✅ | ✅ Complete |
| **Lifecycle** |
| start() | ✅ | ✅ | ✅ Complete |
| close() | ✅ | ✅ | ✅ Complete |

---

## Critical Missing Features

### 1. Control Protocol Foundation (HIGHEST PRIORITY)

**Missing:** 200+ lines of control protocol infrastructure

**Components Needed:**
1. **Callback Registry**
   ```java
   private Map<String, HookCallback> hookCallbacks = new HashMap<>();
   private int nextCallbackId = 0;
   ```

2. **Pending Request Tracking**
   ```java
   private Map<String, CompletableFuture<Map<String, Object>>> pendingControlRequests = new ConcurrentHashMap<>();
   private AtomicInteger requestCounter = new AtomicInteger(0);
   ```

3. **Message Router**
   ```java
   private void routeMessage(Map<String, Object> message) {
       String type = (String) message.get("type");
       if ("control_response".equals(type)) {
           handleControlResponse(message);
       } else if ("control_request".equals(type)) {
           handleControlRequest(message);
       } else {
           // Regular SDK message
           messageQueue.put(parseMessage(message));
       }
   }
   ```

**Impact:** Without this, NO control protocol features work.

### 2. Control Request Handler (HIGHEST PRIORITY)

**Missing:** 109 lines implementing 3 request handlers

**Needed:**
```java
private void handleControlRequest(Map<String, Object> request) {
    String requestId = (String) request.get("request_id");
    Map<String, Object> requestData = (Map<String, Object>) request.get("request");
    String subtype = (String) requestData.get("subtype");

    CompletableFuture.runAsync(() -> {
        try {
            Map<String, Object> responseData = new HashMap<>();

            if ("can_use_tool".equals(subtype)) {
                // Handle permission callback
                responseData = handlePermissionRequest(requestData);
            } else if ("hook_callback".equals(subtype)) {
                // Handle hook callback
                responseData = handleHookCallback(requestData);
            } else if ("mcp_message".equals(subtype)) {
                // Handle MCP message
                responseData = handleMcpMessage(requestData);
            }

            sendControlResponse(requestId, "success", responseData);
        } catch (Exception e) {
            sendControlError(requestId, e.getMessage());
        }
    });
}
```

**Impact:** Hooks, permissions, and SDK MCP servers completely broken.

### 3. SDK MCP Server Bridge (HIGHEST PRIORITY)

**Missing:** 133 lines of MCP protocol implementation

**Needed:**
```java
private Map<String, Object> handleMcpMessage(String serverName, Map<String, Object> message) {
    // Get server instance (need to store during initialization)
    McpServer server = sdkMcpServers.get(serverName);
    if (server == null) {
        return createJsonRpcError(-32601, "Server not found");
    }

    String method = (String) message.get("method");

    if ("initialize".equals(method)) {
        return createMcpInitializeResponse(server);
    } else if ("tools/list".equals(method)) {
        return handleToolsList(server);
    } else if ("tools/call".equals(method)) {
        return handleToolsCall(server, message);
    }

    return createJsonRpcError(-32601, "Method not found");
}
```

**Impact:** SDK MCP servers (in-process tools) completely broken.

---

## Effort Estimate

| Task | Lines to Add | Complexity | Time |
|------|-------------|------------|------|
| Control protocol foundation | ~100 | Medium | 4 hours |
| Message routing | ~50 | Medium | 2 hours |
| Control request handler | ~150 | High | 6 hours |
| Permission callback handler | ~50 | Medium | 2 hours |
| Hook callback handler | ~30 | Medium | 2 hours |
| MCP message bridge | ~150 | High | 6 hours |
| Request/response matching | ~40 | Medium | 2 hours |
| Testing and debugging | - | High | 4 hours |
| **Total** | **~570 lines** | | **28 hours (~3.5 days)** |

---

## Recommendation

**Status:** Java SDK is ~30% complete for control protocol functionality.

**Critical Path:**
1. Implement control protocol foundation (registry, tracking)
2. Implement message routing
3. Implement control request handler
4. Implement SDK MCP bridge

**Without these features:**
- ❌ Hooks do not work
- ❌ Permission callbacks do not work
- ❌ SDK MCP servers do not work
- ⚠️ Control commands work but have no response confirmation

**Priority:** 🔴 **CRITICAL** - These are core features, not nice-to-haves.

The Java SDK currently has ~30% of Query functionality implemented. To reach feature parity, approximately 570 lines of control protocol code need to be added.
