# Repository Guidelines

## Project Structure & Module Organization

The SDK follows standard Maven layout. Core source sits in `src/main/java/com/anthropic/claude/sdk` with transport,
parser, and option types grouped by package. CLI integration helpers and JDK records live beside the public API so
updates stay cohesive. Future and existing unit tests belong in `src/test/java`; mirror package names so Surefire can
discover them automatically. Generated artifacts land in `target/` and should never be committed.

## Build, Test, and Development Commands

`mvn clean install` compiles the SDK, runs unit tests, and assembles the release artifacts; run it before publishing a
branch. `mvn test` is faster for inner-loop validation when you only need Surefire. `mvn -DskipTests package` produces a
jar for manual integration testing with Claude Code without executing the suites. Export Claude-specific environment
variables (e.g., `ANTHROPIC_API_KEY`) or override the CLI path via `ClaudeAgentOptions.cliPath(...)` while running local
experiments.

## Coding Style & Naming Conventions

Use Java 11-compatible syntax while embracing records, sealed interfaces, and builder patterns already present in
`ClaudeAgentSdk`, `ClaudeClient`, and the `types.*` packages. Stick to 4-space indentation, `UpperCamelCase` for
classes/records, `lowerCamelCase` for members, and `UPPER_SNAKE_CASE` for constants. Prefer Lombok annotations for
boilerplate data holders, but keep explicit methods when lifecycle hooks are required. When adding options or
configuration toggles, extend the existing builders instead of introducing setters.

## Testing Guidelines

JUnit 5 is wired through `maven-surefire-plugin`. Name classes `*Test` and keep fixtures under matching package paths
inside `src/test/java`. Use mocks for CLI process boundaries so tests stay hermetic. New features should include
coverage for message parsing, transport edge cases, and CLI discovery logic. Run `mvn test` locally and ensure no
temporary files leak outside `target/`.

## Commit & Pull Request Guidelines

Commit messages follow a `type: summary` pattern (`refactor: Complete project restructure and rewrite`); match that
style for clarity. Keep commits focused on one concern and describe observable behavior changes. Pull requests should
include context (linked issue, feature flag notes), runnable instructions (latest `mvn clean install` output), and
screenshots or logs when touching developer experience. Confirm CLA or licensing requirements before requesting review.

## Security & Configuration Tips

Never commit API keys or Claude CLI binaries; rely on local installations discovered by the search order documented in
`README.md`. If you must hardcode a CLI path for debugging, gate it behind `ClaudeAgentOptions` overrides and remove it
before merging. Prefer environment variables or `.env` files ignored by git for sensitive data.
