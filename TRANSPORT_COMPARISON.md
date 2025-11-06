# Transport Layer Comparison: Python vs Java

## Overview
Comparison of `subprocess_cli.py` (563 lines) vs `ProcessTransport.java` (192 lines)

**Status**: Java implementation is ~30% complete - missing critical functionality

---

## Detailed Method Comparison

### 1. CLI Discovery (`_find_cli()` vs `findClaudeCLI()`)

**Python** (lines 69-94):
```python
def _find_cli(self) -> str:
    if cli := shutil.which("claude"):
        return cli

    locations = [
        Path.home() / ".npm-global/bin/claude",
        Path("/usr/local/bin/claude"),
        Path.home() / ".local/bin/claude",
        Path.home() / "node_modules/.bin/claude",
        Path.home() / ".yarn/bin/claude",
        Path.home() / ".claude/local/claude",
    ]
    # ... check locations
```

**Java** (lines 87-124):
```java
private String findClaudeCLI() throws CLINotFoundException {
    // Try 'which' command first
    Process which = Runtime.getRuntime().exec(new String[]{"which", "claude"});
    // ... check same 6 locations
}
```

**Status**: ✅ **IDENTICAL** - Same 6 fallback locations, same logic

---

### 2. Command Building (`_build_command()` vs `buildCommand()`)

**Python** (lines 96-259): **164 LINES** of comprehensive CLI argument construction
```python
def _build_command(self) -> list[str]:
    cmd = [self._cli_path, "--output-format", "stream-json", "--verbose"]

    # System prompt handling (lines 100-111)
    if self._options.system_prompt is None:
        cmd.extend(["--system-prompt", ""])
    elif isinstance(self._options.system_prompt, str):
        cmd.extend(["--system-prompt", self._options.system_prompt])
    else:
        if (self._options.system_prompt.get("type") == "preset"
            and "append" in self._options.system_prompt):
            cmd.extend(["--append-system-prompt",
                       self._options.system_prompt["append"]])

    # Allowed tools (lines 113-114)
    if self._options.allowed_tools:
        cmd.extend(["--allowedTools", ",".join(self._options.allowed_tools)])

    # Max turns (lines 116-117)
    if self._options.max_turns:
        cmd.extend(["--max-turns", str(self._options.max_turns)])

    # Max budget (lines 119-120)
    if self._options.max_budget_usd is not None:
        cmd.extend(["--max-budget-usd", str(self._options.max_budget_usd)])

    # Disallowed tools (lines 122-123)
    if self._options.disallowed_tools:
        cmd.extend(["--disallowedTools", ",".join(self._options.disallowed_tools)])

    # Model (lines 125-126)
    if self._options.model:
        cmd.extend(["--model", self._options.model])

    # Permission prompt tool (lines 128-131)
    if self._options.permission_prompt_tool_name:
        cmd.extend(["--permission-prompt-tool",
                   self._options.permission_prompt_tool_name])

    # Permission mode (lines 133-134)
    if self._options.permission_mode:
        cmd.extend(["--permission-mode", self._options.permission_mode])

    # Continue conversation (lines 136-137)
    if self._options.continue_conversation:
        cmd.append("--continue")

    # Resume session (lines 139-140)
    if self._options.resume:
        cmd.extend(["--resume", self._options.resume])

    # Settings (lines 142-143)
    if self._options.settings:
        cmd.extend(["--settings", self._options.settings])

    # Add directories (lines 145-148)
    if self._options.add_dirs:
        for directory in self._options.add_dirs:
            cmd.extend(["--add-dir", str(directory)])

    # MCP servers (lines 150-175) - Complex dict/path handling
    if self._options.mcp_servers:
        if isinstance(self._options.mcp_servers, dict):
            servers_for_cli: dict[str, Any] = {}
            for name, config in self._options.mcp_servers.items():
                if isinstance(config, dict) and config.get("type") == "sdk":
                    # Strip instance field for SDK servers
                    sdk_config = {k: v for k, v in config.items() if k != "instance"}
                    servers_for_cli[name] = sdk_config
                else:
                    servers_for_cli[name] = config
            if servers_for_cli:
                cmd.extend(["--mcp-config",
                           json.dumps({"mcpServers": servers_for_cli})])
        else:
            cmd.extend(["--mcp-config", str(self._options.mcp_servers)])

    # Partial messages (lines 177-178)
    if self._options.include_partial_messages:
        cmd.append("--include-partial-messages")

    # Fork session (lines 180-181)
    if self._options.fork_session:
        cmd.append("--fork-session")

    # Agents (lines 183-189)
    if self._options.agents:
        agents_dict = {
            name: {k: v for k, v in asdict(agent_def).items() if v is not None}
            for name, agent_def in self._options.agents.items()
        }
        agents_json = json.dumps(agents_dict)
        cmd.extend(["--agents", agents_json])

    # Setting sources (lines 191-196)
    sources_value = (
        ",".join(self._options.setting_sources)
        if self._options.setting_sources is not None
        else ""
    )
    cmd.extend(["--setting-sources", sources_value])

    # Plugins (lines 198-204)
    if self._options.plugins:
        for plugin in self._options.plugins:
            if plugin["type"] == "local":
                cmd.extend(["--plugin-dir", plugin["path"]])
            else:
                raise ValueError(f"Unsupported plugin type: {plugin['type']}")

    # Extra args (lines 206-213)
    for flag, value in self._options.extra_args.items():
        if value is None:
            cmd.append(f"--{flag}")
        else:
            cmd.extend([f"--{flag}", str(value)])

    # Prompt handling (lines 215-221)
    if self._is_streaming:
        cmd.extend(["--input-format", "stream-json"])
    else:
        cmd.extend(["--print", "--", str(self._prompt)])

    # Max thinking tokens (lines 223-225)
    if self._options.max_thinking_tokens is not None:
        cmd.extend(["--max-thinking-tokens", str(self._options.max_thinking_tokens)])

    # Windows command length check (lines 228-258)
    cmd_str = " ".join(cmd)
    if len(cmd_str) > _CMD_LENGTH_LIMIT and self._options.agents:
        # Create temp file for agents to avoid Windows cmd limit
        temp_file = tempfile.NamedTemporaryFile(
            mode="w", suffix=".json", delete=False, encoding="utf-8"
        )
        temp_file.write(agents_json_value)
        temp_file.close()
        self._temp_files.append(temp_file.name)
        cmd[agents_idx + 1] = f"@{temp_file.name}"

    return cmd
```

**Java** (lines 135-142): **7 LINES** - Almost empty!
```java
private List<String> buildCommand(String cliPath) {
    List<String> command = new ArrayList<>();
    command.add(cliPath);
    command.add("sdk");
    command.add("--streaming");

    return command;
}
```

**Status**: ❌ **CRITICAL GAP** - Java is missing ~95% of functionality
- Missing: 20+ CLI arguments
- Missing: System prompt handling
- Missing: MCP server configuration
- Missing: Agent configuration
- Missing: Plugin support
- Missing: All permission settings
- Missing: Windows command length handling
- Missing: Temporary file creation

---

### 3. Version Check (`_check_claude_version()`)

**Python** (lines 521-559): **39 LINES**
```python
async def _check_claude_version(self) -> None:
    """Check Claude Code version and warn if below minimum."""
    version_process = None
    try:
        with anyio.fail_after(2):  # 2 second timeout
            version_process = await anyio.open_process(
                [self._cli_path, "-v"],
                stdout=PIPE,
                stderr=PIPE,
            )

            if version_process.stdout:
                stdout_bytes = await version_process.stdout.receive()
                version_output = stdout_bytes.decode().strip()

                match = re.match(r"([0-9]+\.[0-9]+\.[0-9]+)", version_output)
                if match:
                    version = match.group(1)
                    version_parts = [int(x) for x in version.split(".")]
                    min_parts = [int(x) for x in MINIMUM_CLAUDE_CODE_VERSION.split(".")]

                    if version_parts < min_parts:
                        warning = (
                            f"Warning: Claude Code version {version} is unsupported. "
                            f"Minimum required version is {MINIMUM_CLAUDE_CODE_VERSION}. "
                            "Some features may not work correctly."
                        )
                        logger.warning(warning)
                        print(warning, file=sys.stderr)
    except Exception:
        pass  # Silently continue if version check fails
```

**Java**: **MISSING COMPLETELY**

**Status**: ❌ **CRITICAL GAP** - No version validation
- Missing: Version check with timeout
- Missing: Minimum version constant (2.0.0)
- Missing: Warning messages for old versions

---

### 4. Process Connection (`connect()` vs `start()`)

**Python** (lines 261-335): **75 LINES** with sophisticated setup
```python
async def connect(self) -> None:
    """Start subprocess."""
    if self._process:
        return

    # Version check (can be skipped with env var)
    if not os.environ.get("CLAUDE_AGENT_SDK_SKIP_VERSION_CHECK"):
        await self._check_claude_version()

    cmd = self._build_command()
    try:
        # Merge environment variables
        process_env = {
            **os.environ,
            **self._options.env,  # User-provided env vars
            "CLAUDE_CODE_ENTRYPOINT": "sdk-py",
            "CLAUDE_AGENT_SDK_VERSION": __version__,
        }

        if self._cwd:
            process_env["PWD"] = self._cwd

        # Conditional stderr piping
        should_pipe_stderr = (
            self._options.stderr is not None
            or "debug-to-stderr" in self._options.extra_args
        )
        stderr_dest = PIPE if should_pipe_stderr else None

        self._process = await anyio.open_process(
            cmd,
            stdin=PIPE,
            stdout=PIPE,
            stderr=stderr_dest,
            cwd=self._cwd,
            env=process_env,
            user=self._options.user,  # Unix user switching
        )

        # Setup streams
        if self._process.stdout:
            self._stdout_stream = TextReceiveStream(self._process.stdout)

        # Setup stderr monitoring
        if should_pipe_stderr and self._process.stderr:
            self._stderr_stream = TextReceiveStream(self._process.stderr)
            self._stderr_task_group = anyio.create_task_group()
            await self._stderr_task_group.__aenter__()
            self._stderr_task_group.start_soon(self._handle_stderr)

        # Setup stdin
        if self._is_streaming and self._process.stdin:
            self._stdin_stream = TextSendStream(self._process.stdin)
        elif not self._is_streaming and self._process.stdin:
            await self._process.stdin.aclose()  # Close immediately for string mode

        self._ready = True

    except FileNotFoundError as e:
        # Distinguish between missing cwd and missing CLI
        if self._cwd and not Path(self._cwd).exists():
            error = CLIConnectionError(f"Working directory does not exist: {self._cwd}")
        else:
            error = CLINotFoundError(f"Claude Code not found at: {self._cli_path}")
        self._exit_error = error
        raise error from e
```

**Java** (lines 36-61): **26 LINES** with basic setup
```java
@Override
public void start() throws ClaudeSDKException {
    String cliPath = determineCliPath();
    List<String> command = buildCommand(cliPath);

    try {
        ProcessBuilder pb = new ProcessBuilder(command);
        if (options.getCwd() != null) {
            pb.directory(options.getCwd().toFile());
        }

        // Set environment variables
        pb.environment().put("CLAUDE_CODE_ENTRYPOINT", "sdk-java");

        // Add custom environment variables from options
        if (options.getEnv() != null) {
            pb.environment().putAll(options.getEnv());
        }

        logger.debug("Starting Claude Code CLI: {}", String.join(" ", command));
        process = pb.start();
        reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

    } catch (IOException e) {
        throw new ClaudeSDKException("Failed to start Claude Code CLI", e);
    }
}
```

**Status**: ⚠️ **PARTIAL** - Missing several features
- ✅ Has: Basic process start, env vars, cwd
- ❌ Missing: Version check
- ❌ Missing: SDK version in env
- ❌ Missing: Stderr monitoring
- ❌ Missing: Streaming mode support
- ❌ Missing: Ready flag
- ❌ Missing: Exit error tracking
- ❌ Missing: User switching (Unix)

---

### 5. Stderr Handling (`_handle_stderr()`)

**Python** (lines 337-363): **27 LINES**
```python
async def _handle_stderr(self) -> None:
    """Handle stderr stream - read and invoke callbacks."""
    if not self._stderr_stream:
        return

    try:
        async for line in self._stderr_stream:
            line_str = line.rstrip()
            if not line_str:
                continue

            # Call the stderr callback if provided
            if self._options.stderr:
                self._options.stderr(line_str)

            # For backward compatibility: write to debug_stderr if in debug mode
            elif ("debug-to-stderr" in self._options.extra_args
                  and self._options.debug_stderr):
                self._options.debug_stderr.write(line_str + "\n")
                if hasattr(self._options.debug_stderr, "flush"):
                    self._options.debug_stderr.flush()
    except anyio.ClosedResourceError:
        pass  # Stream closed, exit normally
    except Exception:
        pass  # Ignore other errors during stderr reading
```

**Java**: **MISSING COMPLETELY**

**Status**: ❌ **CRITICAL GAP** - No stderr handling at all
- Missing: Async stderr monitoring
- Missing: Stderr callback support
- Missing: Debug output support

---

### 6. Resource Cleanup (`close()`)

**Python** (lines 365-413): **49 LINES** with comprehensive cleanup
```python
async def close(self) -> None:
    """Close the transport and clean up resources."""
    self._ready = False

    # Clean up temporary files first
    for temp_file in self._temp_files:
        with suppress(Exception):
            Path(temp_file).unlink(missing_ok=True)
    self._temp_files.clear()

    if not self._process:
        return

    # Close stderr task group if active
    if self._stderr_task_group:
        with suppress(Exception):
            self._stderr_task_group.cancel_scope.cancel()
            await self._stderr_task_group.__aexit__(None, None, None)
        self._stderr_task_group = None

    # Close all streams
    if self._stdin_stream:
        with suppress(Exception):
            await self._stdin_stream.aclose()
        self._stdin_stream = None

    if self._stderr_stream:
        with suppress(Exception):
            await self._stderr_stream.aclose()
        self._stderr_stream = None

    if self._process.stdin:
        with suppress(Exception):
            await self._process.stdin.aclose()

    # Terminate and wait for process
    if self._process.returncode is None:
        with suppress(ProcessLookupError):
            self._process.terminate()
            with suppress(Exception):
                await self._process.wait()

    # Clear all references
    self._process = None
    self._stdout_stream = None
    self._exit_error = None
```

**Java** (lines 175-191): **17 LINES** with basic cleanup
```java
@Override
public void close() {
    if (process != null) {
        process.destroy();
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    if (reader != null) {
        try {
            reader.close();
        } catch (IOException e) {
            logger.warn("Error closing reader", e);
        }
    }
}
```

**Status**: ⚠️ **PARTIAL** - Missing cleanup features
- ✅ Has: Process termination, reader cleanup
- ❌ Missing: Temporary file cleanup
- ❌ Missing: Stderr task group cleanup
- ❌ Missing: Multiple stream cleanup
- ❌ Missing: Ready flag reset

---

### 7. Data Writing (`write()`)

**Python** (lines 415-440): **26 LINES** with validation
```python
async def write(self, data: str) -> None:
    """Write raw data to the transport."""
    # Check if ready
    if not self._ready or not self._stdin_stream:
        raise CLIConnectionError("ProcessTransport is not ready for writing")

    # Check if process is still alive
    if self._process and self._process.returncode is not None:
        raise CLIConnectionError(
            f"Cannot write to terminated process (exit code: {self._process.returncode})"
        )

    # Check for exit errors
    if self._exit_error:
        raise CLIConnectionError(
            f"Cannot write to process that exited with error: {self._exit_error}"
        ) from self._exit_error

    try:
        await self._stdin_stream.send(data)
    except Exception as e:
        self._ready = False
        self._exit_error = CLIConnectionError(f"Failed to write to process stdin: {e}")
        raise self._exit_error from e
```

**Java**: **MISSING COMPLETELY** (uses `getOutputStream()` instead)

**Status**: ❌ **DIFFERENT API** - Java has lower-level API
- Java uses direct `OutputStream` access
- Python has higher-level `write()` method with validation

---

### 8. Input Stream End (`end_input()`)

**Python** (lines 442-447): **6 LINES**
```python
async def end_input(self) -> None:
    """End the input stream (close stdin)."""
    if self._stdin_stream:
        with suppress(Exception):
            await self._stdin_stream.aclose()
        self._stdin_stream = None
```

**Java**: **MISSING COMPLETELY**

**Status**: ❌ **GAP** - No explicit stdin close method

---

### 9. Message Reading (`read_messages()` / `_read_messages_impl()`)

**Python** (lines 449-519): **71 LINES** with sophisticated buffering
```python
async def _read_messages_impl(self) -> AsyncIterator[dict[str, Any]]:
    """Internal implementation of read_messages."""
    if not self._process or not self._stdout_stream:
        raise CLIConnectionError("Not connected")

    json_buffer = ""

    try:
        async for line in self._stdout_stream:
            line_str = line.strip()
            if not line_str:
                continue

            # Accumulate partial JSON until we can parse it
            # Note: TextReceiveStream can truncate long lines, so we buffer
            json_lines = line_str.split("\n")

            for json_line in json_lines:
                json_line = json_line.strip()
                if not json_line:
                    continue

                # Keep accumulating until we can parse
                json_buffer += json_line

                # Check buffer size limit
                if len(json_buffer) > self._max_buffer_size:
                    buffer_length = len(json_buffer)
                    json_buffer = ""
                    raise SDKJSONDecodeError(
                        f"JSON message exceeded maximum buffer size",
                        ValueError(f"Buffer size {buffer_length} exceeds limit")
                    )

                try:
                    data = json.loads(json_buffer)
                    json_buffer = ""
                    yield data
                except json.JSONDecodeError:
                    # Speculatively decoding until we get full JSON
                    continue

    except anyio.ClosedResourceError:
        pass
    except GeneratorExit:
        pass

    # Check process completion
    try:
        returncode = await self._process.wait()
    except Exception:
        returncode = -1

    # Handle non-zero exit codes
    if returncode is not None and returncode != 0:
        self._exit_error = ProcessError(
            f"Command failed with exit code {returncode}",
            exit_code=returncode,
            stderr="Check stderr output for details",
        )
        raise self._exit_error
```

**Java**: Uses `getReader()` - returns `BufferedReader` directly
(Message parsing is done in `ClaudeSDKClient.processMessages()`)

**Status**: ⚠️ **DIFFERENT ARCHITECTURE**
- Python: Transport does JSON parsing with buffering
- Java: Transport returns raw reader, client does parsing
- Java missing: Buffer size limits
- Java missing: Partial JSON accumulation

---

### 10. Readiness Check (`is_ready()`)

**Python** (lines 561-563): **3 LINES**
```python
def is_ready(self) -> bool:
    """Check if transport is ready for communication."""
    return self._ready
```

**Java** (has `isAlive()` instead):
```java
@Override
public boolean isAlive() {
    return process != null && process.isAlive();
}
```

**Status**: ⚠️ **DIFFERENT API**
- Python: Has ready flag (set after successful connect)
- Java: Checks if process is alive

---

## Summary Table

| Feature | Python (Lines) | Java (Lines) | Status |
|---------|----------------|--------------|--------|
| CLI Discovery | 26 | 38 | ✅ Complete |
| Command Building | 164 | 7 | ❌ 95% Missing |
| Version Check | 39 | 0 | ❌ Missing |
| Process Start | 75 | 26 | ⚠️ Partial |
| Stderr Handling | 27 | 0 | ❌ Missing |
| Resource Cleanup | 49 | 17 | ⚠️ Partial |
| Write Method | 26 | 0 | ⚠️ Different API |
| End Input | 6 | 0 | ❌ Missing |
| Message Reading | 71 | 0 | ⚠️ Different Arch |
| Ready Check | 3 | 4 | ⚠️ Different API |
| **TOTAL** | **563** | **192** | **~30% Complete** |

---

## Critical Missing Features in Java

### 1. **Command Building** (HIGHEST PRIORITY)
- Missing 20+ CLI argument handlers
- Missing system prompt configuration
- Missing MCP server serialization
- Missing agent configuration
- Missing plugin directory support
- Missing all permission settings
- Missing Windows command length handling
- Missing temporary file creation

### 2. **Version Validation**
- No minimum version check (2.0.0)
- No version warning messages

### 3. **Stderr Monitoring**
- No async stderr reading
- No stderr callback support
- No debug output support

### 4. **Buffer Management**
- No max buffer size limit (default 1MB)
- Could lead to OOM on large messages

### 5. **Temporary File Handling**
- No Windows command line length check
- Will fail on Windows with large agent configs

### 6. **Error Handling**
- Less sophisticated error tracking
- Missing _exit_error field
- Missing ready flag

### 7. **Stream Management**
- No explicit stdin close method
- No streaming input mode support
- Basic cleanup vs comprehensive cleanup

---

## Architectural Differences

### Python Architecture:
```
SubprocessCLITransport
├── Handles CLI argument construction
├── Handles version checking
├── Handles stderr monitoring (async)
├── Handles JSON parsing with buffering
├── Handles temporary file management
└── Returns parsed messages as AsyncIterator
```

### Java Architecture:
```
ProcessTransport
├── Basic CLI discovery
├── Minimal command building (stub)
├── Returns raw BufferedReader
└── Client does all message parsing
```

**Java delegates more to the client**, which is fine, but **command building must be complete** in the transport layer.

---

## Recommendation

**CRITICAL**: The Java `ProcessTransport.buildCommand()` method needs a complete rewrite to match the Python implementation. This is the single biggest gap affecting functionality.

Priority order:
1. **Fix `buildCommand()`** - Add all 20+ CLI arguments (CRITICAL)
2. **Add version check** - Prevent version mismatch issues (HIGH)
3. **Add stderr handling** - Enable debugging (MEDIUM)
4. **Add buffer limits** - Prevent OOM (MEDIUM)
5. **Add temp file support** - Fix Windows compatibility (LOW - rare case)

Without fixing #1, the Java SDK **cannot pass any configuration to the CLI**, making it essentially non-functional for real use cases.
