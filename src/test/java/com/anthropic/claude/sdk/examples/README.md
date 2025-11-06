# Claude Agent SDK for Java - Examples

This directory contains examples demonstrating various features of the Claude Agent SDK for Java. These examples mirror the Python SDK examples while leveraging Java's type safety and language features.

## Prerequisites

1. **Java 17+** installed
2. **Claude Code CLI** installed: `npm install -g @anthropic-ai/claude-code`
3. **Maven** for building the SDK

## Building the Examples

```bash
# Build the SDK first
cd ..
mvn clean install

# Compile examples
cd examples
javac -cp ../target/claude-agent-sdk-0.1.0.jar:../target/dependency/* *.java
```

## Running Examples

```bash
# Basic usage
java -cp .:../target/claude-agent-sdk-0.1.0.jar QuickStart

# Hooks
java -cp .:../target/claude-agent-sdk-0.1.0.jar HooksExample

# Tool Permissions
java -cp .:../target/claude-agent-sdk-0.1.0.jar ToolPermissionExample

# System Prompts
java -cp .:../target/claude-agent-sdk-0.1.0.jar SystemPromptExample

# Budget Control
java -cp .:../target/claude-agent-sdk-0.1.0.jar BudgetExample
```

## Examples Overview

### 1. QuickStart.java

**Python equivalent**: `quick_start.py`

Demonstrates the basics of using the SDK:
- Simple query
- Query with custom options (system prompt, max turns)
- Query with tools (Read, Write)

```java
ClaudeAgentSdk.query("What is 2 + 2?")
    .forEach(System.out::println);
```

**Key concepts**:
- Static `query()` method for one-shot queries
- `ClaudeAgentOptions.Builder` for configuration
- Processing `AssistantMessage` and `ResultMessage`

---

### 2. HooksExample.java

**Python equivalent**: `hooks.py`

Demonstrates the hooks system for deterministic processing:
- **PreToolUse**: Block dangerous bash commands
- **PostToolUse**: Review tool output and add context
- **Strict Approval**: Control write operations

```java
Hook checkBashCommand = (input, toolUseId, context) -> {
    // Block forbidden commands
    if (command.contains("rm -rf")) {
        return deny("Dangerous command blocked");
    }
    return allow();
};
```

**Key concepts**:
- `Hook` functional interface
- `HookMatcher` for targeting specific tools
- Permission decisions (allow/deny)
- Adding system messages and context

**Run examples**:
```bash
java HooksExample
```

---

### 3. ToolPermissionExample.java

**Python equivalent**: `tool_permission_callback.py`

Demonstrates tool permission callbacks for fine-grained control:
- Allow/deny tools based on type
- Modify tool inputs for safety (e.g., redirect file paths)
- Log all tool usage
- Block dangerous bash commands

```java
ToolPermissionCallback callback = (toolName, input, context) -> {
    if (toolName.equals("Bash") && command.contains("rm -rf")) {
        return CompletableFuture.completedFuture(
            PermissionResult.deny("Dangerous command")
        );
    }
    return CompletableFuture.completedFuture(
        PermissionResult.allow()
    );
};
```

**Key concepts**:
- `ToolPermissionCallback` functional interface
- `PermissionResult.allow()` and `PermissionResult.deny()`
- Modifying inputs with `allow(updatedInput)`
- Tracking tool usage

**Features demonstrated**:
- ✅ Auto-allow read-only tools (Read, Glob, Grep)
- ❌ Block writes to system directories (/etc/, /usr/)
- ⚠️ Redirect writes to safe locations
- ❌ Block dangerous bash commands (rm -rf, sudo, etc.)

---

### 4. SystemPromptExample.java

**Python equivalent**: `system_prompt.py`

Demonstrates different system prompt configurations:
- No system prompt (vanilla Claude)
- Custom string prompts
- Specialized assistants (Pirate, Shakespeare)

```java
ClaudeAgentOptions options = ClaudeAgentOptions.builder()
    .systemPrompt("You are a pirate assistant. Respond in pirate speak!")
    .build();

ClaudeAgentSdk.query("What is 2 + 2?", options);
```

**Key concepts**:
- Customizing Claude's behavior with system prompts
- Different personalities and response styles
- Math tutor example with step-by-step explanations

**Examples included**:
1. **Vanilla Claude** - Default behavior
2. **Math Tutor** - Step-by-step explanations
3. **Pirate Assistant** - Arr, matey!
4. **Shakespeare** - Iambic pentameter responses

---

### 5. BudgetExample.java

**Python equivalent**: `max_budget_usd.py`

Demonstrates budget control to prevent unexpected API costs:
- No budget limit
- Reasonable budget ($0.10)
- Tight budget ($0.0001) that gets exceeded

```java
ClaudeAgentOptions options = ClaudeAgentOptions.builder()
    .maxBudgetUsd(0.10)  // 10 cents limit
    .build();
```

**Key concepts**:
- Setting budget limits with `maxBudgetUsd()`
- Handling budget exceeded errors
- Monitoring actual costs via `ResultMessage.totalCostUsd()`

**Important note**: Budget checking happens after each API call, so the final cost may slightly exceed the limit.

---

## Comparison with Python Examples

| Feature | Python SDK | Java SDK | Notes |
|---------|-----------|----------|-------|
| Simple queries | `query()` | `ClaudeAgentSdk.query()` | Same API |
| Async/await | `async`/`await` | `CompletableFuture` | Java async |
| Hooks | Dict callbacks | Functional interfaces | Type-safe |
| Pattern matching | `isinstance()` | `instanceof` pattern | Java 17+ |
| Options | Keyword args | Builder pattern | More explicit |
| Streaming | `async for` | `Stream` API | Lazy evaluation |

## Additional Examples (Not Yet Implemented)

The following Python examples are not yet implemented in Java:

- `streaming_mode.py` - Bidirectional streaming
- `mcp_calculator.py` - In-process MCP tools
- `agents.py` - Subagent definitions
- `plugin_example.py` - Plugin loading
- `setting_sources.py` - Settings configuration
- `include_partial_messages.py` - Partial message streaming

These will be added as the SDK matures.

## Tips for Java Developers

### 1. Pattern Matching with Sealed Types

```java
// Clean exhaustive matching
switch (message) {
    case AssistantMessage m -> processAssistant(m);
    case ResultMessage m -> processResult(m);
    case UserMessage m -> processUser(m);
    case SystemMessage m -> processSystem(m);
}
```

### 2. Stream Processing

```java
// Process messages with Stream API
client.receiveMessages()
    .filter(m -> m instanceof AssistantMessage)
    .map(m -> (AssistantMessage) m)
    .flatMap(m -> m.content().stream())
    .filter(b -> b instanceof TextBlock)
    .forEach(b -> System.out.println(((TextBlock) b).text()));
```

### 3. CompletableFuture Composition

```java
// Chain async operations
CompletableFuture<Void> pipeline = client.query("Hello")
    .thenAccept(stream -> stream.forEach(this::process))
    .exceptionally(ex -> {
        logger.error("Error", ex);
        return null;
    });
```

### 4. Try-with-Resources

```java
// Automatic cleanup
try (ClaudeClient client = new ClaudeClient(options)) {
    client.query("Hello").join();
    // Client automatically closed
}
```

## Error Handling

All examples include basic error handling. For production use, add comprehensive exception handling:

```java
try {
    ClaudeAgentSdk.query("Hello").forEach(System.out::println);
} catch (CLINotFoundException e) {
    System.err.println("Claude Code not installed");
} catch (CLIConnectionException e) {
    System.err.println("Failed to connect: " + e.getMessage());
} catch (ProcessException e) {
    System.err.println("CLI error (code " + e.getExitCode() + ")");
}
```

## Contributing

To add new examples:
1. Create a new `.java` file in this directory
2. Mirror the corresponding Python example when possible
3. Add comprehensive comments
4. Update this README
5. Test with `mvn test` (when tests are added)

## Resources

- [Java SDK Documentation](../../../../../../../../README.md)
- [Claude Code Documentation](https://docs.anthropic.com/en/docs/claude-code)
- [Python SDK Examples](../../examples/) - Original Python versions
