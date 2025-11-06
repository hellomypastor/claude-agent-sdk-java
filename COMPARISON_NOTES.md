# Python vs Java SDK 对比笔记

## 已实现的类型

### ✅ 完全实现
- ContentBlock（所有 4 种类型）
- Message（所有 5 种类型，包括新增的 StreamEvent）
- Permission 相关类型
- Hook 输入类型（所有 6 种）
- MCP Server 配置类型
- 异常类型

### ⚠️ 部分实现

#### ClaudeAgentOptions
当前已实现字段（11 个）：
- ✅ allowed_tools
- ✅ system_prompt
- ✅ mcp_servers
- ✅ permission_mode
- ✅ max_turns
- ✅ max_budget_usd
- ✅ can_use_tool
- ✅ hooks
- ✅ cwd
- ✅ model
- ✅ maxThinkingTokens

Python SDK 中的额外字段（17 个）：
- ❌ continue_conversation (bool)
- ❌ resume (str)
- ❌ disallowed_tools (list[str])
- ❌ permission_prompt_tool_name (str)
- ❌ cli_path (str/Path) - **重要：允许指定 CLI 路径**
- ❌ settings (str)
- ❌ add_dirs (list[str/Path])
- ❌ env (dict[str, str])
- ❌ extra_args (dict[str, str])
- ❌ max_buffer_size (int)
- ❌ debug_stderr (Any)
- ❌ stderr (Callable)
- ❌ user (str)
- ❌ include_partial_messages (bool)
- ❌ fork_session (bool)
- ❌ agents (dict[str, AgentDefinition]) - **重要：agent 定义**
- ❌ setting_sources (list[SettingSource])
- ❌ plugins (list[SdkPluginConfig])

#### ProcessTransport
当前实现：
- ✅ CLI 查找逻辑（已修复为使用 'claude'）
- ✅ 进程启动
- ❌ 不支持 cli_path 选项
- ❌ 不支持 env 环境变量
- ❌ 不支持 extra_args

## 关键差异

### 1. CLI 路径指定
**Python**: 支持 `cli_path` 选项
```python
options = ClaudeAgentOptions(cli_path="/custom/path/to/claude")
```

**Java**: 当前不支持，需要添加

### 2. Agent 定义
**Python**: 支持通过 `agents` 字段定义多个 agent
```python
options = ClaudeAgentOptions(
    agents={"agent1": AgentDefinition(...)}
)
```

**Java**: 已创建 AgentDefinition 类，但 ClaudeAgentOptions 中缺少 `agents` 字段

### 3. 工具黑名单
**Python**: 支持 `disallowed_tools`
```python
options = ClaudeAgentOptions(
    allowed_tools=["Read", "Write"],
    disallowed_tools=["Bash"]
)
```

**Java**: 当前只有 `allowed_tools`

## 建议的改进优先级

### 高优先级
1. ✅ 添加 StreamEvent 类型
2. ✅ 添加缺失的 Hook 输入类型
3. ✅ 添加 AgentDefinition 类型
4. ⚠️ 添加 cli_path 到 ClaudeAgentOptions
5. ⚠️ 添加 agents 到 ClaudeAgentOptions

### 中优先级
6. disallowed_tools
7. continue_conversation
8. resume
9. env 环境变量支持

### 低优先级
10. 其他高级选项（debug_stderr, max_buffer_size 等）

## 功能完整性评估

### 核心功能
- ✅ 基本查询
- ✅ 双向客户端
- ✅ 自定义工具 (MCP)
- ✅ Hook 系统
- ✅ 权限管理
- ⚠️ CLI 路径指定（部分）
- ⚠️ Agent 配置（部分）

### 高级功能
- ❌ 会话恢复 (resume)
- ❌ 会话分叉 (fork_session)
- ❌ 部分消息 (include_partial_messages)
- ❌ 自定义环境变量
- ❌ 插件系统

## 当前状态

**总体完成度**: ~70%

- 核心类型系统: 95%
- 客户端功能: 75%
- 配置选项: 40%
- MCP 工具: 90%
- Hook 系统: 95%
- Transport 层: 70%

## 后续工作

1. 添加 cli_path 支持到 ProcessTransport
2. 扩展 ClaudeAgentOptions 添加关键字段
3. 更新 ProcessTransport 支持环境变量
4. 添加会话管理功能
5. 文档更新
