# CLI 发现逻辑修复总结

## 问题描述

运行示例代码时出现 `CLINotFoundException` 错误：
```
Error: Claude Code CLI not found. Please install it with: npm install -g @anthropic-ai/claude-code
```

## 根本原因

Java 实现的 CLI 查找逻辑与 Python SDK 不一致：

1. **错误的命令名称**: Java 版本使用 `claude-code`，但正确的命令名称是 `claude`
2. **错误的搜索顺序**: Java 版本先检查固定路径，然后使用 `which`；Python 版本先使用 `which`
3. **不完整的路径列表**: Java 版本缺少一些常见的安装路径
4. **验证逻辑不足**: 没有检查路径是否是常规文件（可能是目录）

## 修复详情

### 1. 命令名称修正
**之前**: `claude-code`
**之后**: `claude`

所有查找逻辑中的命令名称都已更正。

### 2. 搜索顺序调整

**之前**:
1. 检查固定路径列表
2. 使用 `which claude-code`

**之后** (匹配 Python SDK):
1. 先使用 `which claude` 在 PATH 中搜索
2. 如果失败，再检查固定路径列表

### 3. 完整的回退路径列表

现在包含所有 Python SDK 中的路径：
```java
String[] possiblePaths = {
    homeDir + "/.npm-global/bin/claude",    // NPM global packages
    "/usr/local/bin/claude",                // Standard Unix location
    homeDir + "/.local/bin/claude",         // User local binaries
    homeDir + "/node_modules/.bin/claude",  // Project-level NPM
    homeDir + "/.yarn/bin/claude",          // Yarn package manager
    homeDir + "/.claude/local/claude"       // Claude-specific directory
};
```

### 4. 增强的文件验证

**之前**:
```java
return Files.exists(p) && Files.isExecutable(p);
```

**之后**:
```java
return Files.exists(p) && Files.isRegularFile(p) && Files.isExecutable(p);
```

添加了 `Files.isRegularFile()` 检查，确保不会将目录误认为可执行文件。

## 代码对比

### Python SDK 实现逻辑
```python
def _find_cli(self) -> str:
    # First try system PATH
    cli_path = shutil.which("claude")
    if cli_path:
        return cli_path

    # Fallback to common locations
    possible_paths = [
        Path.home() / ".npm-global/bin/claude",
        Path("/usr/local/bin/claude"),
        Path.home() / ".local/bin/claude",
        Path.home() / "node_modules/.bin/claude",
        Path.home() / ".yarn/bin/claude",
        Path.home() / ".claude/local/claude"
    ]

    for path in possible_paths:
        if path.exists() and path.is_file():
            return str(path)

    raise CLINotFoundError()
```

### Java SDK 实现（修复后）
```java
private String findClaudeCLI() throws CLINotFoundException {
    // First, try to find in PATH using 'which' command
    try {
        Process which = Runtime.getRuntime().exec(new String[]{"which", "claude"});
        BufferedReader whichReader = new BufferedReader(new InputStreamReader(which.getInputStream()));
        String line = whichReader.readLine();
        whichReader.close();
        which.waitFor();

        if (line != null && !line.isEmpty()) {
            Path p = Paths.get(line.trim());
            if (Files.exists(p) && Files.isRegularFile(p)) {
                return line.trim();
            }
        }
    } catch (IOException | InterruptedException e) {
        // Continue to fallback locations
    }

    // Fallback to checking common installation locations
    String homeDir = System.getProperty("user.home");
    String[] possiblePaths = {
            homeDir + "/.npm-global/bin/claude",
            "/usr/local/bin/claude",
            homeDir + "/.local/bin/claude",
            homeDir + "/node_modules/.bin/claude",
            homeDir + "/.yarn/bin/claude",
            homeDir + "/.claude/local/claude"
    };

    for (String path : possiblePaths) {
        if (isExecutable(path)) {
            return path;
        }
    }

    throw new CLINotFoundException(null);
}

private boolean isExecutable(String path) {
    try {
        Path p = Paths.get(path);
        return Files.exists(p) && Files.isRegularFile(p) && Files.isExecutable(p);
    } catch (Exception e) {
        return false;
    }
}
```

## 验证

修复后的代码现在：
1. ✅ 使用正确的命令名称 `claude`
2. ✅ 遵循与 Python SDK 相同的搜索顺序
3. ✅ 检查所有相同的安装路径
4. ✅ 正确验证文件类型和权限
5. ✅ 在找不到 CLI 时抛出适当的异常

## 测试建议

确保 Claude CLI 已正确安装：
```bash
# 安装 Claude Code CLI
npm install -g @anthropic-ai/claude-code

# 验证安装
which claude
claude --version
```

然后运行 Java 示例：
```bash
cd examples
mvn compile exec:java -Dexec.mainClass="com.anthropic.claude.examples.QuickStartExample"
```

## 相关文件

修改的文件：
- `src/main/java/com/anthropic/claude/sdk/client/ProcessTransport.java`

相关类：
- `com.anthropic.claude.sdk.errors.CLINotFoundException`
- `com.anthropic.claude.sdk.client.Transport`

## Git 提交

提交哈希: `94dd0c5`
提交消息: "Fix CLI discovery: Use 'claude' command and match Python SDK logic"
分支: `claude/python-to-java-conversion-011CUrt8rFSuXCXdzMgvzx9o`
