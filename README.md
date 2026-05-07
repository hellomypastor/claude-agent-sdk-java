# Claude Agent SDK for Java

Java SDK for Claude Code - Build AI agents with Claude.

A feature-complete Java implementation of the [Claude Agent SDK](https://docs.anthropic.com/en/docs/claude-code/sdk),
aligned with the official [TypeScript SDK](https://www.npmjs.com/package/@anthropic-ai/claude-agent-sdk) (v0.2.x).
Built with Java 17 sealed interfaces, records, and pattern matching for maximum type safety.

## Features

- **One-shot queries** via static `ClaudeAgentSdk.query()`
- **Interactive sessions** via `ClaudeSDKClient` with full streaming support
- **Query interface** with runtime control methods (`setModel`, `setPermissionMode`, `mcpServerStatus`, `rewindFiles`, etc.)
- **Tool permissions** with async callbacks, enriched context, and 6 permission update types
- **12 hook events** with typed inputs/outputs (`PreToolUse`, `PostToolUse`, `PostToolUseFailure`, `Notification`, `SessionStart`, `SessionEnd`, `SubagentStart`, `SubagentStop`, `Stop`, `PreCompact`, `UserPromptSubmit`, `PermissionRequest`)
- **SessionStore SPI** for persisting session transcripts (with `InMemorySessionStore` built-in)
- **MCP SDK Servers** (in-process tools) with `SdkMcpServer` and runtime management
- **SpawnedProcess abstraction** for running Claude Code in VMs, containers, or remote environments
- **Rich message types** — 11 typed message classes including `SystemInitMessage`, `ToolProgressMessage`, `AuthStatusMessage`, `ResultSuccess`/`ResultError`
- **Sandbox settings** for command execution isolation
- **Builder pattern** with `toBuilder()` for easy configuration
- **CompletableFuture** based async API
- **Zero runtime dependencies** beyond Jackson and SLF4J

## Prerequisites

- Java 17 or higher
- Claude Code 2.0.0+: `npm install -g @anthropic-ai/claude-code`

## Installation

### Maven

```xml
<dependency>
    <groupId>com.anthropic</groupId>
    <artifactId>claude-agent-sdk</artifactId>
    <version>0.2.0</version>
</dependency>
```

### Gradle

```gradle
implementation 'com.anthropic:claude-agent-sdk:0.2.0'
```

## Quick Start

### Simple Query

```java
import com.anthropic.claude.sdk.ClaudeAgentSdk;
import com.anthropic.claude.sdk.types.messages.*;
import com.anthropic.claude.sdk.types.content.*;

public class Example {
    public static void main(String[] args) {
        ClaudeAgentSdk.query("What is 2 + 2?")
            .forEach(message -> {
                if (message instanceof AssistantMessage am) {
                    am.content().forEach(block -> {
                        if (block instanceof TextBlock tb) {
                            System.out.println(tb.text());
                        }
                    });
                }
            });
    }
}
```

### Query with Options

```java
import com.anthropic.claude.sdk.types.options.*;
import java.util.List;

ClaudeAgentOptions options = ClaudeAgentOptions.builder()
    .allowedTools(List.of("Read", "Write", "Bash"))
    .permissionMode(PermissionMode.ACCEPT_EDITS)
    .maxTurns(10)
    .model("claude-sonnet-4-5-20250929")
    .build();

ClaudeAgentSdk.query("Analyze this codebase", options)
    .forEach(message -> {
        if (message instanceof ResultSuccess rs) {
            System.out.println("Cost: $" + rs.totalCostUsd());
        }
    });
```

### Interactive Session with Query Control

```java
import com.anthropic.claude.sdk.*;
import com.anthropic.claude.sdk.client.ClaudeSDKClient;

ClaudeAgentOptions options = ClaudeAgentOptions.builder()
    .permissionMode(PermissionMode.ACCEPT_EDITS)
    .build();

try (ClaudeSDKClient client = new ClaudeSDKClient(options)) {
    client.connect().join();

    // Access the Query interface for runtime control
    Query query = client.getQuery();

    // List available models
    query.supportedModels().join().forEach(model ->
        System.out.println(model.displayName() + " (" + model.value() + ")")
    );

    // Change model at runtime
    query.setModel("claude-sonnet-4-5-20250929").join();

    // Check MCP server status
    query.mcpServerStatus().join().forEach(status ->
        System.out.println("MCP: " + status.name() + " -> " + status.status())
    );

    // Send a query and process results
    client.query("What files are in the current directory?").join();
    client.receiveMessages().forEach(msg -> {
        if (msg instanceof AssistantMessage am) {
            am.content().forEach(block -> {
                if (block instanceof TextBlock tb) {
                    System.out.println(tb.text());
                }
            });
        }
    });
}
```

### Tool Permissions

```java
import com.anthropic.claude.sdk.types.permissions.*;
import java.util.concurrent.CompletableFuture;

ClaudeAgentOptions options = ClaudeAgentOptions.builder()
    .allowedTools(List.of("Bash"))
    .canUseTool((toolName, toolInput, context) -> {
        String command = (String) toolInput.get("command");

        // Block dangerous commands
        if (command != null && command.contains("rm -rf")) {
            return CompletableFuture.completedFuture(
                PermissionResult.deny("Dangerous command blocked", true)
            );
        }

        // Access enriched context
        System.out.println("Reason: " + context.decisionReason());
        System.out.println("Agent: " + context.agentId());

        return CompletableFuture.completedFuture(PermissionResult.allow());
    })
    .build();
```

### Hooks (12 Event Types)

```java
import com.anthropic.claude.sdk.types.hooks.*;
import java.util.concurrent.CompletableFuture;

HookCallback preToolUseHook = (input, toolUseId, context) -> {
    if (input instanceof PreToolUseHookInput ptu) {
        System.out.println("Tool: " + ptu.getToolName());
        System.out.println("Input: " + ptu.getToolInput());
    }

    return CompletableFuture.completedFuture(
        HookOutput.SyncHookOutput.builder()
            .continueExecution(true)
            .build()
    );
};

HookCallback postToolUseHook = (input, toolUseId, context) -> {
    if (input instanceof PostToolUseHookInput post) {
        System.out.println("Tool response: " + post.getToolResponse());
    }
    return CompletableFuture.completedFuture(
        HookOutput.SyncHookOutput.builder().continueExecution(true).build()
    );
};

ClaudeAgentOptions options = ClaudeAgentOptions.builder()
    .hooks(Map.of(
        "PreToolUse", List.of(new HookCallbackMatcher("Bash", List.of(preToolUseHook), null)),
        "PostToolUse", List.of(new HookCallbackMatcher("*", List.of(postToolUseHook), null))
    ))
    .build();
```

### MCP SDK Servers (In-Process Tools)

```java
import com.anthropic.claude.sdk.mcp.*;

SdkMcpTool addTool = SdkMcpTool.builder()
    .name("add")
    .description("Add two numbers")
    .inputSchema(Map.of(
        "a", Map.of("type", "number"),
        "b", Map.of("type", "number")
    ))
    .handler(input -> {
        double a = ((Number) input.get("a")).doubleValue();
        double b = ((Number) input.get("b")).doubleValue();
        return CompletableFuture.completedFuture(
            Map.of("content", List.of(Map.of("type", "text", "text", String.valueOf(a + b))))
        );
    })
    .build();

SdkMcpServer server = SdkMcpServer.builder()
    .name("calculator")
    .tool(addTool)
    .build();

ClaudeAgentOptions options = ClaudeAgentOptions.builder()
    .mcpServers(Map.of("calc", server))
    .build();
```

### SessionStore

```java
import com.anthropic.claude.sdk.session.*;

// Use the built-in InMemorySessionStore (or implement your own for Redis/S3/Postgres)
SessionStore store = new InMemorySessionStore();

ClaudeAgentOptions options = ClaudeAgentOptions.builder()
    .sessionStore(store)
    .build();

ClaudeAgentSdk.query("Hello", options).forEach(System.out::println);

// List stored sessions
store.listSessions("default").join().forEach(session ->
    System.out.println("Session: " + session.sessionId())
);
```

### Custom Process Spawning

```java
import com.anthropic.claude.sdk.transport.*;

// Run Claude Code in a container or VM
ClaudeAgentOptions options = ClaudeAgentOptions.builder()
    .spawnClaudeCodeProcess(spawnOptions -> {
        // Custom spawning logic - return a SpawnedProcess
        return myContainerRuntime.spawn(spawnOptions.command(), spawnOptions.args());
    })
    .build();
```

## Architecture

```
┌──────────────────────────────────────────────┐
│  ClaudeAgentSdk / ClaudeSDKClient / Query    │  Public API
└───────────────────┬──────────────────────────┘
                    │
┌───────────────────▼──────────────────────────┐
│  StreamingQuery                              │  Control Protocol
│  - Request/response dispatching              │
│  - Hook callbacks & tool permissions         │
│  - MCP message routing                       │
└───────────────────┬──────────────────────────┘
                    │
┌───────────────────▼──────────────────────────┐
│  MessageParser                               │  Protocol Layer
│  - JSON → 11 typed message classes           │
│  - Sealed interface pattern matching         │
└───────────────────┬──────────────────────────┘
                    │
┌───────────────────▼──────────────────────────┐
│  SubprocessTransport / SpawnedProcess        │  Transport Layer
│  - Process lifecycle management              │
│  - Custom spawn function support             │
└──────────────────────────────────────────────┘
                    │
                    ▼
             Claude Code CLI
```

### Key Components

| Component | Description |
|---|---|
| `ClaudeAgentSdk` | Static entry point for one-shot queries |
| `ClaudeSDKClient` | Full-featured client for interactive sessions |
| `Query` | Enriched query object with runtime control methods |
| `ClaudeAgentOptions` | Builder-based configuration with 30+ options |
| `StreamingQuery` | Control protocol handler (hooks, permissions, MCP) |
| `MessageParser` | JSON → typed Message objects |
| `SubprocessTransport` | CLI subprocess management |
| `SessionStore` | SPI for session transcript persistence |
| `SdkMcpServer` | In-process MCP server for custom tools |

## Type System

The SDK uses Java 17 sealed interfaces and records for exhaustive type safety:

### Messages (11 types)

```java
sealed interface Message permits
    UserMessage, AssistantMessage,
    SystemInitMessage, SystemStatusMessage,
    SystemCompactBoundaryMessage, SystemHookResponseMessage,
    ResultMessage,  // sealed: ResultSuccess | ResultError
    StreamEvent, ToolProgressMessage, AuthStatusMessage
```

### Content Blocks

```java
sealed interface ContentBlock permits
    TextBlock, ThinkingBlock, ToolUseBlock, ToolResultBlock
```

### Result Types

```java
sealed interface ResultMessage extends Message permits ResultSuccess, ResultError

record ResultSuccess(
    long durationMs, long durationApiMs, int numTurns,
    String result, double totalCostUsd,
    Map<String, ModelUsage> modelUsage,
    List<PermissionDenial> permissionDenials,
    Object structuredOutput, ...
) implements ResultMessage

record ResultError(
    String subtype,  // error_during_execution | error_max_turns | error_max_budget_usd | ...
    List<String> errors, ...
) implements ResultMessage
```

### Hook Inputs (12 types)

```java
sealed interface HookInput permits
    PreToolUseHookInput, PostToolUseHookInput, PostToolUseFailureHookInput,
    NotificationHookInput, UserPromptSubmitHookInput,
    SessionStartHookInput, SessionEndHookInput, StopHookInput,
    SubagentStartHookInput, SubagentStopHookInput,
    PreCompactHookInput, PermissionRequestHookInput
```

### Permission Updates (6 types)

```java
sealed interface PermissionUpdate permits
    AddRules, ReplaceRules, RemoveRules, SetMode, AddDirectories, RemoveDirectories
```

## Query Control Methods

The `Query` interface provides runtime control over the session:

| Method | Description |
|---|---|
| `interrupt()` | Stop the current query |
| `setModel(model)` | Change the model at runtime |
| `setPermissionMode(mode)` | Change permission mode |
| `setMaxThinkingTokens(n)` | Limit thinking tokens |
| `supportedModels()` | List available models |
| `supportedCommands()` | List available skills/commands |
| `mcpServerStatus()` | Check MCP server connection status |
| `accountInfo()` | Get authenticated account info |
| `rewindFiles(messageId, dryRun)` | Rewind files to a checkpoint |
| `setMcpServers(servers)` | Dynamically add/remove MCP servers |

## Configuration Options

`ClaudeAgentOptions.builder()` supports 30+ options:

| Option | Type | Description |
|---|---|---|
| `model` | `String` | Claude model to use |
| `systemPrompt` | `String` | Custom system prompt |
| `allowedTools` | `List<String>` | Tools auto-allowed without prompting |
| `disallowedTools` | `List<String>` | Tools to remove entirely |
| `tools` | `List<String>` | Base set of available tools |
| `maxTurns` | `Integer` | Max conversation turns |
| `maxBudgetUsd` | `Double` | Maximum budget in USD |
| `permissionMode` | `PermissionMode` | `DEFAULT`, `ACCEPT_EDITS`, `BYPASS_PERMISSIONS`, `PLAN`, `DELEGATE`, `DONT_ASK` |
| `canUseTool` | `ToolPermissionCallback` | Custom permission handler |
| `hooks` | `Map<String, List<HookCallbackMatcher>>` | Hook callbacks for 12 events |
| `mcpServers` | `Map<String, Object>` | MCP server configurations |
| `betas` | `List<SdkBeta>` | Beta features (e.g., 1M context) |
| `sandbox` | `SandboxSettings` | Command execution isolation |
| `sessionStore` | `SessionStore` | Session transcript persistence |
| `spawnClaudeCodeProcess` | `SpawnProcessFunction` | Custom process spawning |
| `resume` | `String` | Resume a previous session |
| `enableFileCheckpointing` | `boolean` | Enable file rewind support |
| `persistSession` | `boolean` | Persist session to disk (default: true) |
| ... | | See `ClaudeAgentOptions` Javadoc for full list |

## Feature Comparison

| Feature | TypeScript SDK | Java SDK |
|---|---|---|
| One-shot queries | `query()` | `ClaudeAgentSdk.query()` |
| Interactive sessions | `AsyncGenerator` | `ClaudeSDKClient` + `Stream<Message>` |
| Query control methods | `Query` interface | `Query` class |
| Message types | 11 union types | 11 sealed interface records |
| Hook events | 12 events | 12 events |
| SessionStore SPI | Interface + 3 adapters | Interface + InMemorySessionStore |
| Permission updates | 6 union types | 6 sealed record types |
| MCP SDK servers | `createSdkMcpServer()` | `SdkMcpServer.builder()` |
| SpawnedProcess | Interface | Interface |
| Sandbox settings | Zod schema | Builder class |
| Async model | `async`/`await` | `CompletableFuture` |
| Builder pattern | N/A | Full builder + `toBuilder()` |
| Type safety | TypeScript types | Sealed interfaces + Records |

## CLI Discovery

The SDK searches for the `claude` CLI in order:

1. Custom path via `ClaudeAgentOptions.builder().cliPath(...)` 
2. Bundled CLI in classpath (`_bundled/claude`)
3. System PATH
4. `~/.npm-global/bin/claude`
5. `/usr/local/bin/claude`
6. `~/.local/bin/claude`
7. `~/node_modules/.bin/claude`
8. `~/.yarn/bin/claude`
9. `~/.claude/local/claude`

## Error Handling

```java
import com.anthropic.claude.sdk.exceptions.*;

try {
    ClaudeAgentSdk.query("Hello").forEach(System.out::println);
} catch (CLINotFoundException e) {
    System.err.println("Claude Code not installed");
} catch (CLIConnectionException e) {
    System.err.println("Failed to connect: " + e.getMessage());
} catch (ProcessException e) {
    System.err.println("CLI process failed (exit code: " + e.getExitCode() + ")");
} catch (MessageParseException e) {
    System.err.println("Invalid message: " + e.getRawData());
}
```

## Building from Source

```bash
git clone https://github.com/hellymypastor/claude-agent-sdk-java
cd claude-agent-sdk-java

# Requires Java 17+
mvn clean install
```

## Examples

See the [`src/test/java/com/anthropic/claude/sdk/examples/`](src/test/java/com/anthropic/claude/sdk/examples/) directory:

| Example | Description |
|---|---|
| `QuickStart.java` | Basic queries with options |
| `ToolPermissionExample.java` | Permission callbacks with input modification |
| `HooksExample.java` | PreToolUse/PostToolUse hooks with typed inputs |
| `McpServerExample.java` | In-process MCP server with custom tools |
| `BudgetExample.java` | Cost control with `maxBudgetUsd` |
| `StreamingModeExample.java` | Interactive streaming with `ClaudeSDKClient` |
| `SystemPromptExample.java` | Custom system prompts |
| `QueryControlExample.java` | Runtime control via Query interface |
| `SessionStoreExample.java` | Session transcript persistence |

## License

Apache-2.0

## Related Projects

- [Claude Agent SDK for TypeScript](https://github.com/anthropics/claude-agent-sdk-typescript)
- [Claude Code Documentation](https://docs.anthropic.com/en/docs/claude-code)
