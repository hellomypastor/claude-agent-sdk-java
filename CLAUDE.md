# Claude-Specific Guidance

This document provides guidance for Claude (AI assistant) when working with this codebase.

## Project Overview

This is a Java SDK for Claude Agent, providing programmatic interaction with Claude Code. It's a port of the Python SDK to Java, maintaining similar API design while following Java conventions.

## Key Architecture

- **Types Package** (`com.anthropic.claude.sdk.types`): Core data structures for messages, content blocks, and configuration
- **Client Package** (`com.anthropic.claude.sdk.client`): Main client implementation and transport layer
- **MCP Package** (`com.anthropic.claude.sdk.mcp`): Model Context Protocol implementation for custom tools
- **Hooks Package** (`com.anthropic.claude.sdk.hooks`): Hook system for agent loop control
- **Errors Package** (`com.anthropic.claude.sdk.errors`): Exception hierarchy

## Design Principles

1. **Builder Pattern**: Use builders for complex configuration objects (e.g., `ClaudeAgentOptions`)
2. **Immutability**: Prefer immutable objects where possible
3. **Type Safety**: Leverage Java's type system and generics
4. **Async Operations**: Use `CompletableFuture` for asynchronous operations
5. **Resource Management**: Implement `AutoCloseable` for resources that need cleanup

## Code Style

- Follow Java naming conventions (camelCase for methods, PascalCase for classes)
- Use `@Nullable` annotations for optional parameters
- Provide comprehensive JavaDoc comments
- Use Jackson for JSON serialization/deserialization
- Use SLF4J for logging

## Testing

- Unit tests should go in `src/test/java`
- Use JUnit 5 for testing
- Use Mockito for mocking
- Follow the AAA pattern: Arrange, Act, Assert

## Common Tasks

### Adding a New Message Type

1. Create a class implementing `Message` interface
2. Add `@JsonSubTypes.Type` annotation to `Message` interface
3. Implement proper Jackson serialization annotations
4. Add tests

### Adding a New Configuration Option

1. Add field to `ClaudeAgentOptions`
2. Add builder method
3. Update serialization in `ClaudeSDKClient`
4. Update documentation

### Adding a New Hook Event

1. Add enum value to `HookEvent`
2. Create corresponding input class extending `BaseHookInput`
3. Update hook processing logic
4. Add example

## Python to Java Mapping

- `async def` → `CompletableFuture<T>`
- `AsyncIterator` → `Iterator` (with blocking queue internally)
- `dict` → `Map<String, Object>`
- `list` → `List<T>`
- `Optional` → `@Nullable` annotation
- Python decorators → Java annotations
- `with` statement → try-with-resources

## Dependencies

- **Jackson**: JSON processing
- **SLF4J**: Logging facade
- **JSR-305**: Null annotations
- **JUnit 5**: Testing
- **Mockito**: Mocking framework

## Build System

- Maven is used for dependency management and building
- Standard Maven directory structure
- Examples are in a separate source directory

## Future Improvements

- Add comprehensive unit tests
- Add integration tests
- Improve error messages
- Add more examples
- Add performance benchmarks
- Consider reactive streams support (Project Reactor or RxJava)
- Add metrics and monitoring support
