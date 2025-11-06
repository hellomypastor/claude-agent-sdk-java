# Spring Boot 集成指南

本指南说明如何在 Spring Boot 项目中集成和使用 Claude Agent SDK for Java，重点介绍依赖注入、配置管理和最佳实践。

## 目录

- [前置要求](#前置要求)
- [Maven 依赖配置](#maven-依赖配置)
- [快速开始](#快速开始)
- [Spring Bean 配置](#spring-bean-配置)
- [依赖注入使用](#依赖注入使用)
- [配置管理](#配置管理)
- [完整示例](#完整示例)
- [异步处理](#异步处理)
- [最佳实践](#最佳实践)
- [故障排查](#故障排查)

## 前置要求

- **Java 11+**（推荐 Java 17+ 以使用 Records 和 Sealed Interfaces）
- **Spring Boot 2.7+ 或 3.x**
- **Maven 3.6+**
- **Claude Code CLI**: `npm install -g @anthropic-ai/claude-code`

## Maven 依赖配置

### 方式 1: 使用已发布的依赖（推荐）

在 `pom.xml` 中添加依赖：

```xml
<dependency>
    <groupId>com.anthropic</groupId>
    <artifactId>claude-agent-sdk</artifactId>
    <version>0.1.0</version>
</dependency>
```

### 方式 2: 使用本地构建版本

如果 SDK 尚未发布到 Maven Central，可以先构建并安装到本地仓库：

```bash
# 进入 SDK 目录
cd claude-agent-sdk-java

# 构建并安装到本地 Maven 仓库
mvn clean install

# 返回 Spring Boot 项目
cd /path/to/your/spring-boot-project
```

然后在 Spring Boot 项目的 `pom.xml` 中添加依赖（同方式 1）。

### 验证依赖

```bash
mvn dependency:tree | grep claude-agent-sdk
```

## 快速开始

### 1. 创建配置类

```java
package com.example.demo.config;

import com.anthropic.claude.sdk.types.options.ClaudeAgentOptions;
import com.anthropic.claude.sdk.types.options.PermissionMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClaudeConfig {

    @Bean
    public ClaudeAgentOptions claudeAgentOptions() {
        return ClaudeAgentOptions.builder()
            .allowedTools("Read", "Write", "Bash")
            .permissionMode(PermissionMode.ACCEPT_EDITS)
            .maxTurns(10)
            .model("claude-sonnet-4")
            .build();
    }
}
```

### 2. 创建服务类

```java
package com.example.demo.service;

import com.anthropic.claude.sdk.ClaudeAgentSdk;
import com.anthropic.claude.sdk.types.messages.Message;
import com.anthropic.claude.sdk.types.options.ClaudeAgentOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ClaudeAgentService {

    private final ClaudeAgentOptions options;

    public Stream<Message> query(String prompt) {
        return ClaudeAgentSdk.query(prompt, options);
    }
}
```

### 3. 在 Controller 中使用

```java
package com.example.demo.controller;

import com.anthropic.claude.sdk.types.messages.AssistantMessage;
import com.anthropic.claude.sdk.types.messages.Message;
import com.anthropic.claude.sdk.types.content.TextBlock;
import com.example.demo.service.ClaudeAgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/claude")
@RequiredArgsConstructor
public class ClaudeController {

    private final ClaudeAgentService claudeAgentService;

    @PostMapping("/query")
    public String query(@RequestBody String prompt) {
        return claudeAgentService.query(prompt)
            .filter(msg -> msg instanceof AssistantMessage)
            .map(msg -> (AssistantMessage) msg)
            .flatMap(msg -> msg.content().stream())
            .filter(block -> block instanceof TextBlock)
            .map(block -> ((TextBlock) block).text())
            .collect(Collectors.joining("\n"));
    }
}
```

## Spring Bean 配置

### 基础配置

```java
@Configuration
public class ClaudeConfig {

    @Bean
    public ClaudeAgentOptions defaultClaudeOptions() {
        return ClaudeAgentOptions.builder()
            .allowedTools("Read", "Write", "Grep", "Glob")
            .permissionMode(PermissionMode.ACCEPT_EDITS)
            .maxTurns(20)
            .model("claude-sonnet-4")
            .systemPrompt("You are a helpful coding assistant.")
            .build();
    }
}
```

### 高级配置：工具权限控制

```java
@Configuration
public class ClaudeConfig {

    @Bean
    public ClaudeAgentOptions secureClaudeOptions() {
        return ClaudeAgentOptions.builder()
            .allowedTools("Read", "Write", "Bash")
            .canUseTool((toolName, toolInput, context) -> {
                // 拦截危险命令
                if (toolName.equals("Bash")) {
                    String command = (String) toolInput.get("command");
                    if (command.contains("rm -rf") || command.contains("sudo")) {
                        return CompletableFuture.completedFuture(
                            PermissionResult.deny("Dangerous command blocked")
                        );
                    }
                }

                // 限制文件写入路径
                if (toolName.equals("Write")) {
                    String filePath = (String) toolInput.get("file_path");
                    if (!filePath.startsWith("/tmp/")) {
                        return CompletableFuture.completedFuture(
                            PermissionResult.deny("Can only write to /tmp/")
                        );
                    }
                }

                return CompletableFuture.completedFuture(PermissionResult.allow());
            })
            .build();
    }
}
```

### 多环境配置

```java
@Configuration
public class ClaudeConfig {

    @Bean
    @Profile("development")
    public ClaudeAgentOptions devClaudeOptions() {
        return ClaudeAgentOptions.builder()
            .allowedTools("Read", "Write", "Bash", "Grep", "Glob")
            .permissionMode(PermissionMode.ACCEPT_ALL) // 开发环境宽松
            .maxTurns(50)
            .build();
    }

    @Bean
    @Profile("production")
    public ClaudeAgentOptions prodClaudeOptions() {
        return ClaudeAgentOptions.builder()
            .allowedTools("Read", "Grep", "Glob") // 生产环境只读
            .permissionMode(PermissionMode.ASK)
            .maxTurns(10)
            .maxBudgetUsd(0.50) // 预算控制
            .build();
    }
}
```

## 依赖注入使用

### 在 Service 层注入

```java
@Service
@RequiredArgsConstructor
@Slf4j
public class CodeAnalysisService {

    private final ClaudeAgentOptions options;

    public String analyzeCode(String codeSnippet) {
        String prompt = String.format(
            "Analyze this code and suggest improvements:\n\n%s",
            codeSnippet
        );

        StringBuilder result = new StringBuilder();

        ClaudeAgentSdk.query(prompt, options).forEach(message -> {
            if (message instanceof AssistantMessage assistantMsg) {
                assistantMsg.content().forEach(block -> {
                    if (block instanceof TextBlock textBlock) {
                        result.append(textBlock.text()).append("\n");
                    }
                });
            }
        });

        return result.toString();
    }
}
```

### 在 Controller 层注入

```java
@RestController
@RequestMapping("/api/code")
@RequiredArgsConstructor
public class CodeController {

    private final CodeAnalysisService codeAnalysisService;

    @PostMapping("/analyze")
    public ResponseEntity<String> analyzeCode(@RequestBody CodeRequest request) {
        try {
            String analysis = codeAnalysisService.analyzeCode(request.getCode());
            return ResponseEntity.ok(analysis);
        } catch (CLINotFoundException e) {
            return ResponseEntity.status(503)
                .body("Claude CLI not available");
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body("Error: " + e.getMessage());
        }
    }
}
```

### 多个 Service 共享配置

```java
@Service
@RequiredArgsConstructor
public class DocumentationService {
    private final ClaudeAgentOptions options;

    public String generateDocs(String className) {
        return ClaudeAgentSdk.query(
            "Generate documentation for class: " + className,
            options
        ).map(this::extractText).collect(Collectors.joining());
    }

    private String extractText(Message message) {
        // 提取文本逻辑
        return "";
    }
}

@Service
@RequiredArgsConstructor
public class RefactoringService {
    private final ClaudeAgentOptions options;

    public String suggestRefactoring(String code) {
        return ClaudeAgentSdk.query(
            "Suggest refactoring for: " + code,
            options
        ).map(this::extractText).collect(Collectors.joining());
    }

    private String extractText(Message message) {
        // 提取文本逻辑
        return "";
    }
}
```

## 配置管理

### 使用 application.yml

```yaml
claude:
  agent:
    model: claude-sonnet-4
    max-turns: 20
    max-budget-usd: 0.50
    system-prompt: "You are a helpful coding assistant."
    allowed-tools:
      - Read
      - Write
      - Bash
      - Grep
      - Glob
    permission-mode: ACCEPT_EDITS
    cli-path: /usr/local/bin/claude
```

### 创建配置属性类

```java
@ConfigurationProperties(prefix = "claude.agent")
@Data
@Component
public class ClaudeProperties {
    private String model = "claude-sonnet-4";
    private Integer maxTurns = 20;
    private Double maxBudgetUsd;
    private String systemPrompt;
    private List<String> allowedTools = List.of("Read", "Write", "Bash");
    private String permissionMode = "ACCEPT_EDITS";
    private String cliPath;
}
```

### 使用配置属性

```java
@Configuration
@EnableConfigurationProperties(ClaudeProperties.class)
@RequiredArgsConstructor
public class ClaudeConfig {

    private final ClaudeProperties properties;

    @Bean
    public ClaudeAgentOptions claudeAgentOptions() {
        var builder = ClaudeAgentOptions.builder()
            .model(properties.getModel())
            .maxTurns(properties.getMaxTurns())
            .allowedTools(properties.getAllowedTools().toArray(String[]::new))
            .permissionMode(PermissionMode.valueOf(properties.getPermissionMode()));

        if (properties.getSystemPrompt() != null) {
            builder.systemPrompt(properties.getSystemPrompt());
        }

        if (properties.getMaxBudgetUsd() != null) {
            builder.maxBudgetUsd(properties.getMaxBudgetUsd());
        }

        if (properties.getCliPath() != null) {
            builder.cliPath(Path.of(properties.getCliPath()));
        }

        return builder.build();
    }
}
```

## 完整示例

### 项目结构

```
src/main/java/com/example/demo/
├── config/
│   ├── ClaudeConfig.java
│   └── ClaudeProperties.java
├── service/
│   └── ClaudeAgentService.java
├── controller/
│   └── ClaudeController.java
├── dto/
│   ├── QueryRequest.java
│   └── QueryResponse.java
└── DemoApplication.java
```

### DTO 类

```java
// QueryRequest.java
@Data
public class QueryRequest {
    private String prompt;
    private Integer maxTurns;
    private String systemPrompt;
}

// QueryResponse.java
@Data
@Builder
public class QueryResponse {
    private String response;
    private Double costUsd;
    private Integer turns;
}
```

### 增强的 Service

```java
@Service
@RequiredArgsConstructor
@Slf4j
public class ClaudeAgentService {

    private final ClaudeAgentOptions defaultOptions;

    public QueryResponse query(QueryRequest request) {
        // 根据请求动态创建选项
        ClaudeAgentOptions options = buildOptions(request);

        StringBuilder responseText = new StringBuilder();
        Double[] totalCost = {0.0};
        Integer[] turns = {0};

        try {
            ClaudeAgentSdk.query(request.getPrompt(), options)
                .forEach(message -> {
                    processMessage(message, responseText, totalCost, turns);
                });
        } catch (Exception e) {
            log.error("Query failed", e);
            throw new RuntimeException("Claude query failed: " + e.getMessage());
        }

        return QueryResponse.builder()
            .response(responseText.toString())
            .costUsd(totalCost[0])
            .turns(turns[0])
            .build();
    }

    private ClaudeAgentOptions buildOptions(QueryRequest request) {
        var builder = ClaudeAgentOptions.builder()
            .allowedTools(defaultOptions.allowedTools().toArray(String[]::new))
            .permissionMode(defaultOptions.permissionMode());

        if (request.getMaxTurns() != null) {
            builder.maxTurns(request.getMaxTurns());
        }
        if (request.getSystemPrompt() != null) {
            builder.systemPrompt(request.getSystemPrompt());
        }

        return builder.build();
    }

    private void processMessage(Message message, StringBuilder responseText,
                               Double[] totalCost, Integer[] turns) {
        if (message instanceof AssistantMessage assistantMsg) {
            assistantMsg.content().forEach(block -> {
                if (block instanceof TextBlock textBlock) {
                    responseText.append(textBlock.text()).append("\n");
                }
            });
        } else if (message instanceof ResultMessage resultMsg) {
            totalCost[0] = resultMsg.totalCostUsd();
            log.info("Query completed. Cost: ${}, Turns: {}",
                resultMsg.totalCostUsd(), resultMsg.totalTurns());
        }
    }
}
```

### 完整的 Controller

```java
@RestController
@RequestMapping("/api/claude")
@RequiredArgsConstructor
@Slf4j
public class ClaudeController {

    private final ClaudeAgentService claudeAgentService;

    @PostMapping("/query")
    public ResponseEntity<QueryResponse> query(@RequestBody QueryRequest request) {
        try {
            QueryResponse response = claudeAgentService.query(request);
            return ResponseEntity.ok(response);
        } catch (CLINotFoundException e) {
            log.error("Claude CLI not found", e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(QueryResponse.builder()
                    .response("Claude CLI not installed")
                    .build());
        } catch (Exception e) {
            log.error("Query failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(QueryResponse.builder()
                    .response("Error: " + e.getMessage())
                    .build());
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        // 检查 Claude CLI 是否可用
        try {
            ClaudeAgentSdk.query("ping")
                .findFirst()
                .orElseThrow();
            return ResponseEntity.ok("Claude CLI is available");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Claude CLI not available: " + e.getMessage());
        }
    }
}
```

## 异步处理

### 启用异步支持

```java
@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("claude-async-");
        executor.initialize();
        return executor;
    }
}
```

### 异步 Service

```java
@Service
@RequiredArgsConstructor
@Slf4j
public class AsyncClaudeService {

    private final ClaudeAgentOptions options;

    @Async
    public CompletableFuture<String> queryAsync(String prompt) {
        log.info("Starting async query: {}", prompt);

        StringBuilder result = new StringBuilder();

        ClaudeAgentSdk.query(prompt, options).forEach(message -> {
            if (message instanceof AssistantMessage assistantMsg) {
                assistantMsg.content().forEach(block -> {
                    if (block instanceof TextBlock textBlock) {
                        result.append(textBlock.text());
                    }
                });
            }
        });

        log.info("Async query completed");
        return CompletableFuture.completedFuture(result.toString());
    }
}
```

### 使用异步 Service

```java
@RestController
@RequestMapping("/api/async")
@RequiredArgsConstructor
public class AsyncController {

    private final AsyncClaudeService asyncClaudeService;

    @PostMapping("/query")
    public CompletableFuture<ResponseEntity<String>> query(@RequestBody String prompt) {
        return asyncClaudeService.queryAsync(prompt)
            .thenApply(ResponseEntity::ok)
            .exceptionally(ex -> ResponseEntity.status(500).body(ex.getMessage()));
    }
}
```

### WebFlux 集成（响应式）

```java
@Service
@RequiredArgsConstructor
public class ReactiveClaudeService {

    private final ClaudeAgentOptions options;

    public Flux<String> queryStream(String prompt) {
        return Flux.fromStream(
            ClaudeAgentSdk.query(prompt, options)
                .filter(msg -> msg instanceof AssistantMessage)
                .map(msg -> (AssistantMessage) msg)
                .flatMap(msg -> msg.content().stream())
                .filter(block -> block instanceof TextBlock)
                .map(block -> ((TextBlock) block).text())
        );
    }
}
```

## 最佳实践

### 1. 资源管理

对于交互式会话，使用 try-with-resources：

```java
@Service
public class InteractiveClaudeService {

    public void interactiveSession(ClaudeAgentOptions options) {
        try (ClaudeClient client = new ClaudeClient(options)) {
            client.query("First query").join();

            client.receiveMessages().forEach(message -> {
                // 处理消息
            });

            // ClaudeClient 自动关闭
        } catch (Exception e) {
            log.error("Session failed", e);
        }
    }
}
```

### 2. 错误处理

```java
@Service
@Slf4j
public class RobustClaudeService {

    public String queryWithRetry(String prompt, ClaudeAgentOptions options, int maxRetries) {
        int attempt = 0;
        Exception lastException = null;

        while (attempt < maxRetries) {
            try {
                return ClaudeAgentSdk.query(prompt, options)
                    .filter(msg -> msg instanceof AssistantMessage)
                    .map(msg -> extractText(msg))
                    .collect(Collectors.joining());
            } catch (CLIConnectionException e) {
                lastException = e;
                attempt++;
                log.warn("Retry attempt {}/{}", attempt, maxRetries);
                try {
                    Thread.sleep(1000 * attempt); // 指数退避
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Interrupted during retry", ie);
                }
            }
        }

        throw new RuntimeException("Failed after " + maxRetries + " retries", lastException);
    }

    private String extractText(Message message) {
        // 提取文本逻辑
        return "";
    }
}
```

### 3. 日志和监控

```java
@Service
@Slf4j
public class MonitoredClaudeService {

    private final ClaudeAgentOptions options;
    private final MeterRegistry meterRegistry;

    public MonitoredClaudeService(ClaudeAgentOptions options, MeterRegistry meterRegistry) {
        this.options = options;
        this.meterRegistry = meterRegistry;
    }

    public String query(String prompt) {
        Timer.Sample sample = Timer.start(meterRegistry);

        try {
            String result = ClaudeAgentSdk.query(prompt, options)
                .map(this::extractText)
                .collect(Collectors.joining());

            sample.stop(Timer.builder("claude.query")
                .tag("status", "success")
                .register(meterRegistry));

            meterRegistry.counter("claude.queries.total", "status", "success").increment();

            return result;
        } catch (Exception e) {
            sample.stop(Timer.builder("claude.query")
                .tag("status", "error")
                .register(meterRegistry));

            meterRegistry.counter("claude.queries.total", "status", "error").increment();

            log.error("Query failed: {}", e.getMessage(), e);
            throw e;
        }
    }

    private String extractText(Message message) {
        // 提取文本逻辑
        return "";
    }
}
```

### 4. 缓存

```java
@Service
@Slf4j
public class CachedClaudeService {

    private final ClaudeAgentOptions options;
    private final Cache<String, String> cache;

    public CachedClaudeService(ClaudeAgentOptions options) {
        this.options = options;
        this.cache = Caffeine.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(1, TimeUnit.HOURS)
            .build();
    }

    @Cacheable(value = "claude-queries", key = "#prompt")
    public String queryCached(String prompt) {
        log.info("Cache miss for prompt: {}", prompt);

        return ClaudeAgentSdk.query(prompt, options)
            .filter(msg -> msg instanceof AssistantMessage)
            .map(this::extractText)
            .collect(Collectors.joining());
    }

    private String extractText(Message message) {
        // 提取文本逻辑
        return "";
    }
}
```

### 5. 预算控制

```java
@Service
public class BudgetControlService {

    @Value("${claude.max-daily-budget:1.0}")
    private Double maxDailyBudget;

    private final AtomicDouble dailySpent = new AtomicDouble(0.0);
    private LocalDate lastResetDate = LocalDate.now();

    public synchronized String queryWithBudget(String prompt, ClaudeAgentOptions options) {
        resetDailyBudgetIfNeeded();

        if (dailySpent.get() >= maxDailyBudget) {
            throw new RuntimeException("Daily budget exceeded: $" + dailySpent.get());
        }

        StringBuilder result = new StringBuilder();

        ClaudeAgentSdk.query(prompt, options).forEach(message -> {
            if (message instanceof ResultMessage resultMsg) {
                dailySpent.addAndGet(resultMsg.totalCostUsd());
            }
            if (message instanceof AssistantMessage assistantMsg) {
                // 处理响应
            }
        });

        return result.toString();
    }

    private void resetDailyBudgetIfNeeded() {
        LocalDate today = LocalDate.now();
        if (!today.equals(lastResetDate)) {
            dailySpent.set(0.0);
            lastResetDate = today;
        }
    }
}
```

## 故障排查

### 常见问题

#### 1. Claude CLI 未找到

**错误**: `CLINotFoundException: Claude Code CLI not found`

**解决方案**:
```bash
# 安装 Claude CLI
npm install -g @anthropic-ai/claude-code

# 验证安装
claude --version
```

或在配置中指定路径：
```yaml
claude:
  agent:
    cli-path: /usr/local/bin/claude
```

#### 2. 连接超时

**错误**: `CLIConnectionException: Failed to connect to CLI`

**解决方案**:
- 增加超时时间（Spring Boot 配置）
- 检查系统资源
- 查看日志中的详细错误

#### 3. 依赖注入失败

**错误**: `NoSuchBeanDefinitionException: No qualifying bean of type 'ClaudeAgentOptions'`

**解决方案**:
确保配置类被扫描：
```java
@SpringBootApplication
@ComponentScan(basePackages = {"com.example.demo"})
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

#### 4. 权限问题

**错误**: Tool execution denied

**解决方案**:
检查 `permissionMode` 配置：
```yaml
claude:
  agent:
    permission-mode: ACCEPT_ALL  # 或 ACCEPT_EDITS, ASK
```

#### 5. 内存不足

**症状**: 处理大型代码库时 OOM

**解决方案**:
```yaml
# application.yml
spring:
  main:
    lazy-initialization: true

claude:
  agent:
    max-turns: 5  # 减少轮次
```

调整 JVM 参数：
```bash
java -Xmx2g -jar your-app.jar
```

### 调试技巧

#### 启用详细日志

```yaml
logging:
  level:
    com.anthropic.claude.sdk: DEBUG
    com.example.demo: DEBUG
```

#### 健康检查端点

```java
@RestController
@RequestMapping("/actuator/claude")
public class ClaudeHealthIndicator {

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> health = new HashMap<>();

        try {
            // 测试 CLI 可用性
            ClaudeAgentSdk.query("ping").findFirst();
            health.put("status", "UP");
            health.put("cliAvailable", true);
        } catch (Exception e) {
            health.put("status", "DOWN");
            health.put("cliAvailable", false);
            health.put("error", e.getMessage());
        }

        return health;
    }
}
```

## 示例项目

完整的示例项目可在以下找到：
- [Spring Boot 示例项目](./examples/spring-boot-demo)（即将推出）

## 相关资源

- [Claude Agent SDK for Java - README](./README.md)
- [API 示例](./src/test/java/com/anthropic/claude/sdk/examples/)
- [Claude Code 文档](https://docs.anthropic.com/en/docs/claude-code)
- [Spring Boot 官方文档](https://spring.io/projects/spring-boot)

## 贡献

如果您在 Spring Boot 集成中遇到问题或有改进建议，请提交 Issue 或 Pull Request。

## 许可证

MIT License
