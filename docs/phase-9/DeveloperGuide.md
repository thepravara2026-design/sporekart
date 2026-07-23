# Developer Guide

This guide covers prerequisites, service setup, plugin development, capability registration, permission management, testing best practices, and publishing to the marketplace.

---

## Prerequisites

- Java 21 (JDK 21+)
- Maven 3.9+ or Gradle 8.5+
- Spring Boot 3.3.3
- Access to the `copilot-marketplace-service` source code
- Docker (optional, for containerized development)
- Gateway service running on port 8110 (for end-to-end testing)

---

## Setting Up the Service

### 1. Clone and Build

```bash
# Navigate to the marketplace service
cd copilot-marketplace-service

# Build with Maven
mvn clean install -DskipTests

# Build with Gradle
gradle clean build -x test
```

### 2. Configuration

The service is configured via `application.yml`:

```yaml
server:
  port: 8110

spring:
  application:
    name: copilot-marketplace-service

sporekart:
  marketplace:
    name: SporeKart Copilot Marketplace
    version: 1.0.0
    plugin-store-path: ./plugins
    max-plugin-size: 100MB
    max-installed-plugins: 100
    sandbox:
      enabled: true
      max-memory-per-plugin: 256MB
      max-execution-timeout-ms: 30000
      max-threads-per-plugin: 5
    health:
      check-interval-ms: 60000
      failure-threshold: 3
      recovery-threshold: 2

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  endpoint:
    health:
      show-details: always
  metrics:
    tags:
      application: ${spring.application.name}

springdoc:
  api-docs:
    path: /v3/api-docs
  swagger-ui:
    path: /swagger-ui.html
```

### 3. Run the Service

```bash
# Using Maven
mvn spring-boot:run

# Using Gradle
gradle bootRun

# Using java -jar
java -jar target/copilot-marketplace-service-1.0.0.jar
```

The service starts on port `8110`. Verify with:

```bash
curl http://localhost:8110/actuator/health
```

### 4. Gateway Integration

Ensure the gateway service has the marketplace route configured:

```java
ROUTE_MAP.put("copilot-marketplace", "/api/v1/plugins");
```

The gateway will forward `/api/v1/plugins/**` to `localhost:8110` with circuit breaker and retry filters.

---

## Building a Plugin (Step by Step)

### Step 1: Create a Java Project

Create a new Maven or Gradle project with a dependency on the marketplace SDK:

```xml
<dependency>
    <groupId>com.sporekart</groupId>
    <artifactId>marketplace-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Step 2: Define the Manifest

Create your manifest as a Java record or JSON/YAML file. See `PluginManifest.md` for the full specification.

### Step 3: Implement the Plugin

Extend `AbstractPlugin`:

```java
package com.example.plugin;

import com.sporekart.marketplace.sdk.*;
import java.util.List;
import java.util.Map;

public class MyPlugin extends AbstractPlugin {

    private static final PluginManifest MANIFEST = new PluginManifest(
        "com.example.my-plugin",
        "My Example Plugin",
        "1.0.0",
        "Acme Corp",
        "An example plugin demonstrating the SDK",
        PluginType.AUTOMATION,
        List.of(PluginCapability.WORKFLOW),
        List.of(PluginPermission.READ_PRODUCTS),
        List.of(),
        "1.0.0",
        "2.0.0",
        "/health",
        Map.of("timeout", Map.of("type", "integer", "default", 5000)),
        "com.example.plugin.MyPlugin"
    );

    @Override
    public PluginManifest getManifest() {
        return MANIFEST;
    }

    @Override
    public void onEnable(PluginContext context) {
        super.onEnable(context);
        // Start background tasks, initialize connections
        String timeout = context.getConfigValue("timeout");
        System.out.println("Plugin enabled with timeout: " + timeout);
    }

    @Override
    public Map<String, Object> execute(String action, Map<String, Object> params) {
        switch (action) {
            case "process":
                return Map.of("result", "processed", "input", params);
            case "validate":
                return Map.of("valid", true, "checks", params.size());
            default:
                return Map.of("error", "Unknown action: " + action);
        }
    }
}
```

### Step 4: Load and Install the Plugin

Plugins can be loaded programmatically via `PluginLoader`:

```java
@Service
public class PluginRegistrationService {

    private final PluginLoader pluginLoader;

    public void registerPlugin() {
        var plugin = new MyPlugin();
        var manifest = plugin.getManifest();
        pluginLoader.loadPlugin(manifest, plugin);
    }
}
```

Or installed via the REST API:

```bash
curl -X POST http://localhost:8110/api/v1/plugins/install \
  -H "Content-Type: application/json" \
  -d '{"pluginId": "com.example.my-plugin", "source": "local"}'
```

---

## Registering Capabilities

Capabilities are automatically registered during installation based on the manifest's `capabilities` list. To register additional capabilities at runtime:

```java
// Via CapabilityRegistry directly
capabilityRegistry.register(pluginId, pluginName, PluginCapability.ANALYTICS);
```

For knowledge packs, use `KnowledgePackIndexer`:

```java
var pack = new KnowledgePackIndexer.KnowledgePack(
    "pack-001",
    pluginId,
    "Product FAQ",
    "Frequently asked questions about products",
    KnowledgePackIndexer.KnowledgePack.KnowledgePackType.FAQ,
    List.of("doc1.md", "doc2.md"),
    Instant.now()
);
knowledgePackIndexer.indexPack(pluginId, "My Plugin", pack);
```

---

## Requesting Permissions

Permissions are declared in the manifest and auto-approved during install. To manage permissions at runtime:

```java
// Check if a plugin has a permission
boolean canRead = permissionManager.hasPermission(pluginId, PluginPermission.READ_PRODUCTS);

// Approve additional permissions
permissionManager.approve(pluginId, PluginPermission.CALL_APIS);

// Revoke a permission
permissionManager.revoke(pluginId, PluginPermission.SEND_NOTIFICATIONS);

// Validate before execution
boolean allowed = permissionValidator.validateExecution(pluginId, PluginPermission.READ_ORDERS);
```

---

## Testing Plugins

### Unit Tests

Write unit tests following the existing test patterns in `src/test/java/com/sporekart/marketplace/`:

```java
public class MyPluginTest {

    private MyPlugin plugin;
    private PluginContext context;

    @BeforeEach
    void setUp() {
        plugin = new MyPlugin();
        context = new PluginContext("test-plugin", Map.of("timeout", "5000"), Map.of());
    }

    @Test
    void shouldReturnManifest() {
        var manifest = plugin.getManifest();
        assertNotNull(manifest);
        assertEquals("com.example.my-plugin", manifest.pluginId());
    }

    @Test
    void shouldExecuteAction() {
        plugin.onEnable(context);
        var result = plugin.execute("process", Map.of("key", "value"));
        assertEquals("processed", result.get("result"));
    }

    @Test
    void shouldReportHealthyWhenEnabled() {
        plugin.onEnable(context);
        var health = plugin.healthCheck();
        assertEquals("HEALTHY", health.get("status"));
    }
}
```

### Integration Tests

Use the existing `MarketplaceIntegrationTest` as a reference. The test configuration uses `src/test/resources/application.yml`:

```java
@SpringBootTest
@AutoConfigureMockMvc
class MyPluginIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldInstallAndEnablePlugin() throws Exception {
        var request = """
            {"pluginId": "test-plugin", "source": "test"}
            """;

        mockMvc.perform(post("/api/v1/plugins/install")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }
}
```

### Testing Best Practices

1. **Test all lifecycle methods**: Verify install, enable, disable, uninstall, upgrade, and downgrade callbacks
2. **Test error handling**: Verify plugin handles timeouts, invalid actions, and null parameters gracefully
3. **Test permission boundaries**: Verify plugin does not access resources without the required permissions
4. **Test concurrency**: Use `PluginSandbox` timeout and thread limit tests as reference
5. **Test health check**: Verify `healthCheck()` returns accurate status
6. **Mock PluginContext**: Use `Map.of()` for config and workspace in unit tests
7. **Test version compatibility**: Verify plugin works with min and max platform versions

---

## Publishing to the Marketplace

### 1. Package the Plugin

```bash
# Build the plugin JAR
mvn clean package

# The JAR will be at target/my-plugin-1.0.0.jar
```

### 2. Deploy the JAR

Copy the plugin JAR to the configured `plugin-store-path` (default: `./plugins`):

```bash
cp target/my-plugin-1.0.0.jar ./plugins/
```

### 3. Register via API

```bash
curl -X POST http://localhost:8110/api/v1/plugins/install \
  -H "Content-Type: application/json" \
  -u username:password \
  -d '{"pluginId": "com.example.my-plugin", "source": "marketplace"}'
```

### 4. Enable the Plugin

```bash
curl -X POST http://localhost:8110/api/v1/plugins/enable/com.example.my-plugin \
  -u username:password
```

### 5. Verify

```bash
# List all plugins
curl http://localhost:8110/api/v1/plugins \
  -u username:password

# Check plugin health
curl http://localhost:8110/api/v1/plugins/health \
  -u username:password

# Execute an action
curl -X POST http://localhost:8110/api/v1/plugins/com.example.my-plugin/execute \
  -H "Content-Type: application/json" \
  -u username:password \
  -d '{"action": "process", "params": {"key": "value"}}'
```

---

## Best Practices Summary

| Practice | Detail |
|----------|--------|
| Manifest validation | Always validate your manifest against `PluginValidator` rules before publishing |
| Resource cleanup | Implement `onUninstall` to clean up all resources, threads, and connections |
| Graceful degradation | Handle missing permissions in `execute()` with informative error messages |
| Stateless design | Prefer stateless plugins that store state in `PluginContext.workspace` |
| Version pinning | Always set `minPlatformVersion` and `maxPlatformVersion` explicitly |
| Health reporting | Return detailed diagnostic information in `healthCheck()` |
| Configuration schema | Provide a JSON Schema in `configurationSchema` for user-friendly config editing |
| Capability specificity | Register only the capabilities your plugin actually provides |
