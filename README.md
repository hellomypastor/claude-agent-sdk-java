# Claude Agent SDK for Java

Java SDK for programmatic interaction with Claude Code - build AI-powered applications with Claude's capabilities.

This is a Java port of the official [Python SDK](https://github.com/anthropics/claude-agent-sdk-python), providing the same powerful features for Java developers.

## Features

- **Simple Query API**: One-shot queries for straightforward interactions
- **Bidirectional Client**: Full stateful conversations with Claude
- **Custom Tools (In-Process MCP)**: Define Java methods as tools Claude can use
- **Hook System**: Implement deterministic processing at agent loop points
- **Flexible Configuration**: Control permissions, tools, working directory, and more

## Requirements

- Java 11 or higher
- Node.js (for Claude Code CLI)
- Claude Code CLI 2.0.0+: `npm install -g @anthropic-ai/claude-code`

## Installation

Add to your Maven `pom.xml`:

```xml
<dependency>
    <groupId>com.anthropic</groupId>
    <artifactId>claude-agent-sdk</artifactId>
    <version>0.1.0</version>
</dependency>
```

Or for Gradle (`build.gradle`):

```gradle
implementation 'com.anthropic:claude-agent-sdk:0.1.0'
```

## Quick Start

### Basic Query

```java
import com.anthropic.claude.sdk.ClaudeAgent;
import com.anthropic.claude.sdk.types.*;
import java.util.Iterator;

public class Example {
    public static void main(String[] args) throws Exception {
        Iterator<Message> messages = ClaudeAgent.query("What is 2 + 2?");

        while (messages.hasNext()) {
            Message message = messages.next();
            if (message instanceof AssistantMessage) {
                AssistantMessage assistantMsg = (AssistantMessage) message;
                for (ContentBlock block : assistantMsg.getContent()) {
                    if (block instanceof TextBlock) {
                        System.out.println(((TextBlock) block).getText());
                    }
                }
            }
        }
    }
}
```

### Query with Configuration

```java
ClaudeAgentOptions options = ClaudeAgentOptions.builder()
    .systemPrompt("You are a helpful assistant")
    .maxTurns(1)
    .allowedTools(Arrays.asList("Read", "Write", "Bash"))
    .permissionMode(PermissionMode.ACCEPT_EDITS)
    .cwd("/path/to/working/directory")
    .build();

Iterator<Message> messages = ClaudeAgent.query("Tell me a joke", options);
```

## Advanced Usage

### Bidirectional Client

Use `ClaudeSDKClient` for interactive, stateful conversations:

```java
import com.anthropic.claude.sdk.client.ClaudeSDKClient;

try (ClaudeSDKClient client = ClaudeAgent.createClient(options)) {
    // Connect and send initial prompt
    client.connect("Hello! Can you help me with Java?");

    Iterator<Message> messages = client.receiveMessages();
    while (messages.hasNext()) {
        Message message = messages.next();
        // Process message

        // Send follow-up queries
        client.query("What about exception handling?", "default");
    }
}
```

### Custom Tools (In-Process MCP Servers)

Define Java methods as tools Claude can invoke:

```java
import com.anthropic.claude.sdk.mcp.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;

// Create a custom tool
SdkMcpTool<Map<String, Object>> greetTool =
    SdkMcpTool.<Map<String, Object>>builder("greet", "Greet a user by name")
        .inputSchema(createSchema())
        .handler(input -> {
            String name = (String) input.get("name");
            Map<String, Object> response = new HashMap<>();
            response.put("content", Arrays.asList(
                Map.of("type", "text", "text", "Hello, " + name + "!")
            ));
            return CompletableFuture.completedFuture(response);
        })
        .build();

// Create MCP server
SdkMcpServer server = SdkMcpServer.create("my-tools", Arrays.asList(greetTool));

// Configure options
Map<String, McpServerConfig> mcpServers = new HashMap<>();
mcpServers.put("tools", new McpSdkServerConfig("my-tools", server));

ClaudeAgentOptions options = ClaudeAgentOptions.builder()
    .mcpServers(mcpServers)
    .allowedTools(Arrays.asList("mcp__tools__greet"))
    .build();
```

**Benefits of in-process MCP servers:**
- No subprocess overhead - tools run directly in your JVM
- Better performance with reduced IPC
- Simplified deployment and debugging
- Direct access to your application's state and objects

### Hooks for Policy Enforcement

Implement deterministic processing at specific agent loop points:

```java
import com.anthropic.claude.sdk.hooks.*;

HookCallback checkBashCommand = (inputData, toolUseId, context) -> {
    if (!"Bash".equals(inputData.get("tool_name"))) {
        return CompletableFuture.completedFuture(new HashMap<>());
    }

    Map<String, Object> toolInput = (Map<String, Object>) inputData.get("tool_input");
    String command = (String) toolInput.get("command");

    if (command.contains("rm -rf")) {
        Map<String, Object> hookOutput = new HashMap<>();
        Map<String, Object> hookSpecific = new HashMap<>();
        hookSpecific.put("hookEventName", "PreToolUse");
        hookSpecific.put("permissionDecision", "deny");
        hookSpecific.put("permissionDecisionReason", "Dangerous command detected");
        hookOutput.put("hookSpecificOutput", hookSpecific);
        return CompletableFuture.completedFuture(hookOutput);
    }

    return CompletableFuture.completedFuture(new HashMap<>());
};

Map<String, List<HookMatcher>> hooks = new HashMap<>();
hooks.put("PreToolUse", Arrays.asList(
    HookMatcher.matchTool("Bash", Arrays.asList(checkBashCommand))
));

ClaudeAgentOptions options = ClaudeAgentOptions.builder()
    .allowedTools(Arrays.asList("Bash"))
    .hooks(hooks)
    .build();
```

### Mixed Server Configuration

Combine in-process SDK servers with external MCP servers:

```java
Map<String, McpServerConfig> mcpServers = new HashMap<>();

// In-process SDK server
mcpServers.put("internal", new McpSdkServerConfig("my-tools", sdkServer));

// External stdio server
mcpServers.put("external", new McpStdioServerConfig(
    "external-server",
    Arrays.asList("--option", "value"),
    null // environment variables
));

ClaudeAgentOptions options = ClaudeAgentOptions.builder()
    .mcpServers(mcpServers)
    .build();
```

## Error Handling

Handle specific exceptions appropriately:

```java
import com.anthropic.claude.sdk.errors.*;

try {
    Iterator<Message> messages = ClaudeAgent.query("Hello");
    // Process messages
} catch (CLINotFoundException e) {
    System.err.println("Claude Code CLI not found. Please install it.");
} catch (CLIConnectionException e) {
    System.err.println("Failed to connect to Claude Code: " + e.getMessage());
} catch (ProcessException e) {
    System.err.println("CLI process failed with exit code: " + e.getExitCode());
} catch (ClaudeSDKException e) {
    System.err.println("SDK error: " + e.getMessage());
}
```

## Configuration Options

The `ClaudeAgentOptions` builder supports:

| Option | Type | Description |
|--------|------|-------------|
| `allowedTools` | `List<String>` | Tools Claude can use (e.g., "Read", "Write", "Bash") |
| `systemPrompt` | `String` | Custom system prompt |
| `mcpServers` | `Map<String, McpServerConfig>` | MCP server configurations |
| `permissionMode` | `PermissionMode` | Permission mode (DEFAULT, ACCEPT_EDITS, PLAN, BYPASS_PERMISSIONS) |
| `maxTurns` | `Integer` | Maximum conversation turns |
| `maxBudgetUsd` | `Double` | Maximum cost budget |
| `cwd` | `Path` or `String` | Working directory |
| `model` | `String` | Claude model to use |
| `maxThinkingTokens` | `Integer` | Maximum thinking tokens |

## Examples

See the `examples/` directory for complete working examples:

- **QuickStartExample.java**: Basic query usage
- **ConfiguredQueryExample.java**: Query with options
- **ClientExample.java**: Bidirectional client
- **ToolExample.java**: Custom MCP tools

Run examples:
```bash
cd examples
mvn compile exec:java -Dexec.mainClass="com.anthropic.claude.examples.QuickStartExample"
```

## Architecture

This Java SDK mirrors the Python SDK's architecture:

- **Types**: Message types, content blocks, and configuration options
- **Client**: Main `ClaudeSDKClient` for bidirectional communication
- **Transport**: Process-based communication with Claude Code CLI
- **MCP**: In-process tool server system
- **Hooks**: Event-based processing hooks
- **Errors**: Specific exception types for different failure modes

## Building

```bash
mvn clean install
```

## Testing

```bash
mvn test
```

## License

MIT License - see LICENSE file for details.

## Links

- [Python SDK](https://github.com/anthropics/claude-agent-sdk-python) - Official Python version
- [Claude Code Documentation](https://docs.anthropic.com/en/docs/claude-code)
- [Available Tools](https://docs.anthropic.com/en/docs/claude-code/settings#tools-available-to-claude)

## Contributing

Contributions are welcome! Please feel free to submit issues or pull requests.

## Version

Current version: 0.1.0

This is a Java port of the Python SDK, maintaining API compatibility where possible while following Java conventions and best practices.
