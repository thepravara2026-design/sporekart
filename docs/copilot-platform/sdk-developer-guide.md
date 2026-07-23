# SporeKart Copilot SDK Developer Guide

> **Version:** 0.2.0-SNAPSHOT | **SDK Package:** `com.sporekart.copilot:sdk:0.2.0`

---

## 1. SDK Overview

The SporeKart Copilot SDK provides a set of interfaces, base classes, and utilities for building copilot plugins. It is designed as a lightweight abstraction layer that lets teams focus on domain logic while the framework handles infrastructure concerns.

### SDK Modules

| Module | Artifact | Description |
|--------|----------|-------------|
| `sdk-core` | `copilot-sdk-core` | Core interfaces: `CopilotPlugin`, `CapabilityProvider`, `ToolProvider`, `PersonaProvider`, `ContextProvider` |
| `sdk-streaming` | `copilot-sdk-streaming` | Streaming support: `StreamingHandler`, `SSEEvent`, `TokenPublisher` |
| `sdk-testing` | `copilot-sdk-testing` | Test fixtures, mocks, and harnesses |
| `sdk-maven-plugin` | `copilot-sdk-maven` | Maven plugin for scaffolding and validation |

### Prerequisites

- Java 21+
- Spring Boot 3.x
- Maven 3.9+ or Gradle 8.x
- Access to the SporeKart internal Maven repository

---

## 2. Quick Start

### 2.1 Add Dependencies

```xml
<dependency>
    <groupId>com.sporekart.copilot</groupId>
    <artifactId>copilot-sdk-core</artifactId>
    <version>0.2.0-SNAPSHOT</version>
</dependency>
```

### 2.2 Create a Minimal Copilot

```java
package com.example.copilot;

import com.sporekart.copilot.sdk.CopilotPlugin;
import com.sporekart.copilot.sdk.model.ChatRequest;
import com.sporekart.copilot.sdk.model.ChatResponse;

public class GreetingCopilot implements CopilotPlugin {

    @Override
    public String getName() {
        return "greeting-copilot";
    }

    @Override
    public String getDescription() {
        return "A simple greeting copilot";
    }

    @Override
    public ChatResponse chat(ChatRequest request) {
        return ChatResponse.builder()
            .message("Hello, " + request.getMessage() + "!")
            .finishReason("stop")
            .build();
    }
}
```

### 2.3 Register the Copilot

```bash
curl -X POST http://localhost:8080/api/copilot/register \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "name": "greeting-copilot",
    "description": "A simple greeting copilot",
    "persona": "support",
    "capabilities": [],
    "tools": []
  }'
```

### 2.4 Chat with the Copilot

```bash
curl -X POST http://localhost:8080/api/copilot/chat \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "copilotId": "<copilot-id>",
    "message": "world"
  }'
```

---

## 3. Core Concepts and API Reference

### 3.1 CopilotPlugin

The main interface for implementing a copilot.

```java
public interface CopilotPlugin {
    String getName();
    String getDescription();
    String getVersion();
    ChatResponse chat(ChatRequest request);
    default StreamPublisher stream(StreamRequest request) { ... }
    default CopilotHealth health() { ... }
    default void onRegister(CopilotContext context) { ... }
    default void onDeregister() { ... }
}
```

| Method | Description | Required |
|--------|-------------|----------|
| `getName()` | Unique copilot name | Yes |
| `getDescription()` | Human-readable description | Yes |
| `getVersion()` | Semantic version string | No (default: "0.1.0") |
| `chat(ChatRequest)` | Process a chat message | Yes |
| `stream(StreamRequest)` | Process a streaming request | No |
| `health()` | Return health status | No |
| `onRegister(CopilotContext)` | Lifecycle hook on registration | No |
| `onDeregister()` | Lifecycle hook on deregistration | No |

### 3.2 CapabilityProvider

Provides a single domain capability.

```java
public interface CapabilityProvider {
    String getCapabilityName();
    String getDescription();
    CapabilityCategory getCategory();
    JsonSchema getInputSchema();
    JsonSchema getOutputSchema();
    CapabilityResult execute(CapabilityInput input);
}
```

### 3.3 ToolProvider

Provides a function-calling tool for LLM interaction.

```java
public interface ToolProvider {
    String getToolName();
    String getDescription();
    ToolSchema getSchema();
    ToolResult execute(ToolCall call);
}
```

### 3.4 PersonaProvider

Defines a persona for copilot interactions.

```java
public interface PersonaProvider {
    String getPersonaName();
    String getSystemPrompt();
    double getTemperature();
    int getMaxTokens();
    List<String> getAllowedCapabilities();
    List<String> getAllowedTools();
}
```

### 3.5 ContextProvider

Provides context data from a specific source.

```java
public interface ContextProvider {
    String getSourceName();
    ContextData fetchContext(ContextRequest request);
}
```

---

## 4. Creating a New Copilot

### 4.1 Step-by-Step

1. **Create a Maven/Gradle module** in your service repository
2. **Add the SDK dependency**
3. **Implement `CopilotPlugin`** with your chat logic
4. **Create a Spring configuration** to register your copilot as a bean
5. **Add a `@CopilotComponent` annotation** (optional, for auto-discovery)
6. **Implement capabilities** for domain operations
7. **Test locally** against the copilot service
8. **Package and deploy**

### 4.2 Spring Boot Auto-Configuration

```java
@Configuration
public class MyCopilotConfiguration {

    @Bean
    public CopilotPlugin myCopilot() {
        return new MyCopilot();
    }

    @Bean
    public CapabilityProvider orderLookup() {
        return new OrderLookupCapability();
    }
}
```

### 4.3 Full Example

```java
@CopilotComponent
public class SupportCopilot implements CopilotPlugin {

    private final OrderLookupCapability orderLookup;
    private final TicketTool ticketTool;

    public SupportCopilot(OrderLookupCapability orderLookup, TicketTool ticketTool) {
        this.orderLookup = orderLookup;
        this.ticketTool = ticketTool;
    }

    @Override
    public String getName() { return "support-copilot"; }

    @Override
    public String getDescription() { return "Customer support assistant"; }

    @Override
    public ChatResponse chat(ChatRequest request) {
        // Use context, capabilities, and tools to generate response
        return ChatResponse.builder()
            .message("Processed support request: " + request.getMessage())
            .copilotId(request.getCopilotId())
            .sessionId(request.getSessionId())
            .finishReason("stop")
            .build();
    }
}
```

---

## 5. Adding Capabilities

### 5.1 Capability Interface

Implement `CapabilityProvider` to expose a domain operation.

```java
public class OrderLookupCapability implements CapabilityProvider {

    private final OrderServiceClient orderClient;

    public OrderLookupCapability(OrderServiceClient orderClient) {
        this.orderClient = orderClient;
    }

    @Override
    public String getCapabilityName() { return "order.lookup"; }

    @Override
    public String getDescription() { return "Look up order details by order ID"; }

    @Override
    public CapabilityCategory getCategory() { return CapabilityCategory.QUERY; }

    @Override
    public JsonSchema getInputSchema() {
        return JsonSchema.of(Map.of(
            "orderId", Map.of("type", "string", "description", "The order ID")
        ));
    }

    @Override
    public JsonSchema getOutputSchema() {
        return JsonSchema.of(Map.of(
            "orderId", Map.of("type", "string"),
            "status", Map.of("type", "string"),
            "total", Map.of("type", "number")
        ));
    }

    @Override
    public CapabilityResult execute(CapabilityInput input) {
        String orderId = input.getString("orderId");
        var order = orderClient.getOrder(orderId);
        return CapabilityResult.success(Map.of(
            "orderId", order.getId(),
            "status", order.getStatus(),
            "total", order.getTotal()
        ));
    }
}
```

### 5.2 Registering Capabilities

Capabilities can be registered:
- **Programmatically**: via `CapabilityRegistry.register(provider)` at startup
- **Via configuration**: in `application.yml` under `sporekart.copilot.capabilities`
- **At runtime**: via the capabilities API endpoint

---

## 6. Implementing Tools

### 6.1 Tool Definition

Tools follow the OpenAI function-calling convention.

```java
public class TicketTool implements ToolProvider {

    private final SupportServiceClient supportClient;

    @Override
    public String getToolName() { return "create_support_ticket"; }

    @Override
    public String getDescription() {
        return "Create a support ticket for the user";
    }

    @Override
    public ToolSchema getSchema() {
        return ToolSchema.builder()
            .type("function")
            .function(FunctionSchema.builder()
                .name("create_support_ticket")
                .description("Create a new support ticket")
                .parameters(Map.of(
                    "type", "object",
                    "properties", Map.of(
                        "subject", Map.of(
                            "type", "string",
                            "description", "Ticket subject"
                        ),
                        "priority", Map.of(
                            "type", "string",
                            "enum", List.of("low", "medium", "high", "critical")
                        ),
                        "description", Map.of(
                            "type", "string",
                            "description", "Detailed issue description"
                        )
                    ),
                    "required", List.of("subject", "priority", "description")
                ))
            .build())
        .build();
    }

    @Override
    public ToolResult execute(ToolCall call) {
        var args = call.getArguments();
        var ticket = supportClient.createTicket(
            args.getString("subject"),
            args.getString("priority"),
            args.getString("description")
        );
        return ToolResult.success(Map.of(
            "ticketId", ticket.getId(),
            "status", ticket.getStatus()
        ));
    }
}
```

### 6.2 Tool Execution Flow

```
LLM ─▶ Tool Call Request ─▶ Tool Engine ─▶ ToolProvider.execute()
    ─▶ Result ─▶ LLM ─▶ Final Response
```

---

## 7. Creating Custom Personas

### 7.1 Persona Definition

```java
public class SupportPersona implements PersonaProvider {

    @Override
    public String getPersonaName() { return "support-agent"; }

    @Override
    public String getSystemPrompt() {
        return """
            You are a helpful customer support agent for SporeKart.
            Your goal is to resolve customer issues efficiently and empathetically.
            
            Guidelines:
            - Always be polite and professional
            - If you don't know the answer, escalate to a human agent
            - Use the available tools to look up orders and create tickets
            - Never share internal system information
            - Keep responses concise and actionable
            
            Available capabilities: order.lookup, order.status, ticket.create
            """;
    }

    @Override
    public double getTemperature() { return 0.5; }

    @Override
    public int getMaxTokens() { return 1024; }

    @Override
    public List<String> getAllowedCapabilities() {
        return List.of("order.lookup", "order.status", "ticket.create");
    }

    @Override
    public List<String> getAllowedTools() {
        return List.of("create_support_ticket", "lookup_order");
    }
}
```

### 7.2 Persona Categories

| Category | Use Case | Example |
|----------|----------|---------|
| `support` | Customer-facing support | Help desk, ticket management |
| `sales` | Product recommendations, upsell | Cart assistance, promotions |
| `admin` | Internal admin operations | User management, config |
| `technical` | Developer/internal tools | API docs, troubleshooting |
| `custom` | User-defined | Any specialized domain |

---

## 8. Plugin Development Guide

### 8.1 Plugin Architecture

Plugins are self-contained JAR files that implement one or more provider interfaces. The Plugin Manager handles classpath scanning, classloading isolation, and lifecycle management.

### 8.2 Creating a Plugin

```xml
<!-- Maven POM structure -->
<project>
    <artifactId>my-copilot-plugin</artifactId>
    <packaging>jar</packaging>
    
    <dependencies>
        <dependency>
            <groupId>com.sporekart.copilot</groupId>
            <artifactId>copilot-sdk-core</artifactId>
            <version>0.2.0-SNAPSHOT</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>com.sporekart.copilot</groupId>
                <artifactId>copilot-sdk-maven</artifactId>
                <version>0.2.0-SNAPSHOT</version>
                <executions>
                    <execution>
                        <goals><goal>validate</goal></goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```

### 8.3 Plugin Manifest

```yaml
# META-INF/copilot-plugin.yml
name: my-copilot-plugin
version: 1.0.0
description: My custom copilot plugin
provider: com.example.MyCopilotPlugin
requires: ">=0.2.0"
providers:
  - type: copilot
    class: com.example.MyCopilot
  - type: capability
    class: com.example.MyCapability
  - type: tool
    class: com.example.MyTool
```

### 8.4 Plugin Lifecycle

```
LOADED ─▶ RESOLVED ─▶ REGISTERED ─▶ STARTED ─▶ STOPPED
             │                           │
             └── ERROR ◀─────────────────┘
```

| State | Description |
|-------|-------------|
| `LOADED` | JAR loaded into classloader |
| `RESOLVED` | Dependencies checked, manifest validated |
| `REGISTERED` | Providers registered with framework |
| `STARTED` | Plugin initialized and active |
| `STOPPED` | Plugin gracefully shut down |
| `ERROR` | Plugin in error state |

---

## 9. Extension Points

| Extension Point | Interface | Description |
|----------------|-----------|-------------|
| Pre-chat filter | `ChatFilter` | Intercept/modify chat requests before processing |
| Post-chat filter | `ChatResponseFilter` | Modify responses before returning to client |
| Context enhancer | `ContextEnhancer` | Add additional context during assembly |
| Metrics collector | `MetricsCollector` | Collect custom metrics |
| Audit logger | `AuditLogger` | Custom audit event handling |
| Rate limiter | `RateLimiterProvider` | Custom rate-limiting strategy |
| Cache provider | `CacheProvider` | Custom caching backend |

### Example: Chat Filter

```java
public class LoggingChatFilter implements ChatFilter {

    @Override
    public ChatRequest filter(ChatRequest request, CopilotContext context) {
        log.info("Chat request from user: {}, copilot: {}",
            context.getUserId(), request.getCopilotId());
        return request;
    }

    @Override
    public int getOrder() {
        return 0; // Lower values execute first
    }
}
```

---

## 10. Testing Guidelines

### 10.1 Unit Testing

```java
class GreetingCopilotTest {

    private GreetingCopilot copilot;

    @BeforeEach
    void setUp() {
        copilot = new GreetingCopilot();
    }

    @Test
    void shouldRespondToGreeting() {
        var request = ChatRequest.builder()
            .message("world")
            .build();

        var response = copilot.chat(request);

        assertThat(response.getMessage()).isEqualTo("Hello, world!");
        assertThat(response.getFinishReason()).isEqualTo("stop");
    }
}
```

### 10.2 Integration Testing with Test Harness

```java
@CopilotTest
class SupportCopilotIntegrationTest {

    @Autowired
    private CopilotTestHarness harness;

    @Test
    void shouldHandleSupportRequest() {
        var request = ChatRequest.builder()
            .copilotId("support-copilot")
            .message("I need help with my order")
            .sessionId(UUID.randomUUID())
            .build();

        var response = harness.chat(request);

        assertThat(response.getFinishReason()).isEqualTo("stop");
        assertThat(response.getMessage()).isNotEmpty();
    }

    @Test
    void shouldStreamResponse() {
        var request = StreamRequest.builder()
            .copilotId("support-copilot")
            .message("Tell me a joke")
            .build();

        var tokens = harness.stream(request)
            .collectList()
            .block();

        assertThat(tokens).isNotEmpty();
        assertThat(tokens.get(tokens.size() - 1).getEvent())
            .isEqualTo("done");
    }
}
```

### 10.3 Test Harness Features

| Feature | Description |
|---------|-------------|
| `chat(ChatRequest)` | Simulates a chat request |
| `stream(StreamRequest)` | Simulates a streaming request |
| `registerCopilot(CopilotPlugin)` | Registers a copilot for testing |
| `mockContext()` | Provides mock context assembly |
| `mockService(Class)` | Mocks domain service calls |
| `assertAuditLog()` | Verifies audit events were emitted |

### 10.4 Testing Tools

- Use `@CopilotTest` annotation for Spring Boot integration tests
- Use `CopilotTestHarness` for full-stack testing without deploying
- Use `CapabilityTestFixture` for testing individual capabilities
- Use `ToolTestFixture` for testing tool execution

---

## 11. Examples

### 11.1 Simple FAQ Copilot

```java
@CopilotComponent
public class FaqCopilot implements CopilotPlugin {

    private final Map<String, String> faqs = Map.of(
        "return policy", "Our return policy allows returns within 30 days.",
        "shipping", "Free shipping on orders over $50.",
        "payment", "We accept Visa, Mastercard, and PayPal."
    );

    @Override
    public String getName() { return "faq-copilot"; }

    @Override
    public String getDescription() { return "Answers frequently asked questions"; }

    @Override
    public ChatResponse chat(ChatRequest request) {
        String message = request.getMessage().toLowerCase();
        for (var entry : faqs.entrySet()) {
            if (message.contains(entry.getKey())) {
                return ChatResponse.builder()
                    .message(entry.getValue())
                    .finishReason("stop")
                    .build();
            }
        }
        return ChatResponse.builder()
            .message("I don't have an answer for that. Please contact support.")
            .finishReason("stop")
            .build();
    }
}
```

### 11.2 Order Status Copilot with Capabilities

```java
@CopilotComponent
public class OrderStatusCopilot implements CopilotPlugin {

    private final OrderLookupCapability orderLookup;

    @Override
    public String getName() { return "order-status"; }

    @Override
    public ChatResponse chat(ChatRequest request) {
        var input = CapabilityInput.of("orderId", extractOrderId(request.getMessage()));
        var result = orderLookup.execute(input);

        if (result.isSuccess()) {
            return ChatResponse.builder()
                .message("Order " + result.getData("orderId") +
                    " is " + result.getData("status"))
                .build();
        }
        return ChatResponse.builder()
            .message("Could not find order. Please check the order ID.")
            .build();
    }

    private String extractOrderId(String message) {
        // Simple regex extraction logic
        return message.replaceAll("[^0-9]", "");
    }
}
```

### 11.3 Multi-Turn Chat Copilot with Memory

```java
@CopilotComponent
public class MemoryAwareCopilot implements CopilotPlugin {

    private final MemoryManager memoryManager;

    @Override
    public String getName() { return "memory-aware"; }

    @Override
    public ChatResponse chat(ChatRequest request) {
        var history = memoryManager.getHistory(request.getSessionId());
        history.add(new Message("user", request.getMessage()));

        var response = ChatResponse.builder()
            .message("I remember you asked " + history.size() + " questions.")
            .sessionId(request.getSessionId())
            .build();

        history.add(new Message("assistant", response.getMessage()));
        memoryManager.saveHistory(request.getSessionId(), history);

        return response;
    }
}
```

---

## Appendix A: Annotation Reference

| Annotation | Target | Description |
|------------|--------|-------------|
| `@CopilotComponent` | Class | Marks a class as a copilot plugin for auto-discovery |
| `@CapabilityProvider` | Class | Marks a capability provider for auto-registration |
| `@ToolProvider` | Class | Marks a tool provider for auto-registration |
| `@PersonaProvider` | Class | Marks a persona provider for auto-registration |
| `@ContextProvider` | Class | Marks a context provider for auto-registration |
| `@CopilotTest` | Class | Enables copilot test harness in Spring Boot tests |

## Appendix B: Configuration Properties

| Property | Default | Description |
|----------|---------|-------------|
| `sporekart.copilot.plugins.path` | `plugins/` | Directory for plugin JAR files |
| `sporekart.copilot.plugins.auto-scan` | `true` | Auto-discover plugins from classpath |
| `sporekart.copilot.plugins.isolation` | `false` | Load plugins in isolated classloaders |
| `sporekart.copilot.session.ttl` | `60m` | Default session TTL |
| `sporekart.copilot.context.max-tokens` | `4096` | Max tokens for context assembly |
| `sporekart.copilot.tool.timeout` | `10s` | Default tool execution timeout |
| `sporekart.copilot.rate-limit.default` | `60` | Default requests per minute |
