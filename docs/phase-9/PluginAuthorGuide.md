# Plugin Author Guide

This guide is for developers who want to create and distribute plugins for the SporeKart Enterprise Copilot Marketplace.

---

## Creating a Plugin Project

### Project Structure

A typical plugin project follows this structure:

```
my-plugin/
├── pom.xml                              # or build.gradle
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── plugin/
│   │   │               ├── MyPlugin.java              # Main plugin implementation
│   │   │               ├── MyPluginConfig.java         # Configuration model
│   │   │               └── service/
│   │   │                   └── MyPluginService.java    # Business logic
│   │   └── resources/
│   │       └── plugin-manifest.json                   # Plugin manifest
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── plugin/
│                       └── MyPluginTest.java
├── README.md
└── Dockerfile                            # Optional: for containerized distribution
```

### Build Configuration (Maven)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>my-plugin</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <dependency>
            <groupId>com.sporekart</groupId>
            <artifactId>marketplace-sdk</artifactId>
            <version>1.0.0</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>3.5.1</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals><goal>shade</goal></goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```

Note: The `marketplace-sdk` dependency is marked as `provided` because the SDK classes are already available in the marketplace runtime.

---

## Implementing SporekartPlugin

### Full Plugin Implementation

```java
package com.example.plugin;

import com.sporekart.marketplace.sdk.*;
import java.util.*;
import java.util.concurrent.*;

public class MyPlugin extends AbstractPlugin {

    private static final PluginManifest MANIFEST = buildManifest();
    private ScheduledExecutorService scheduler;
    private Map<String, Object> runtimeState;

    private static PluginManifest buildManifest() {
        return new PluginManifest(
            "com.example.my-plugin",      // pluginId
            "My Plugin",                   // name
            "1.0.0",                       // version
            "Acme Corp",                   // author
            "Processes data and generates insights", // description
            PluginType.ANALYTICS,          // type
            List.of(PluginCapability.ANALYTICS, PluginCapability.REPORTING),  // capabilities
            List.of(PluginPermission.READ_ANALYTICS, PluginPermission.READ_PRODUCTS),  // permissions
            List.of("com.example.core-lib"),  // dependencies
            "1.0.0",                       // minPlatformVersion
            "2.0.0",                       // maxPlatformVersion
            "/health",                     // healthEndpoint
            Map.of(                        // configurationSchema
                "refreshInterval", Map.of("type", "integer", "default", 300),
                "maxResults", Map.of("type", "integer", "default", 100)
            ),
            "com.example.plugin.MyPlugin"  // entryPoint
        );
    }

    @Override
    public PluginManifest getManifest() {
        return MANIFEST;
    }

    @Override
    public void onInstall(PluginContext context) {
        super.onInstall(context);
        this.runtimeState = new ConcurrentHashMap<>();
        runtimeState.put("installedAt", System.currentTimeMillis());
    }

    @Override
    public void onEnable(PluginContext context) {
        super.onEnable(context);
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        String refreshStr = context.getConfigValue("refreshInterval");
        int refreshInterval = refreshStr != null ? Integer.parseInt(refreshStr) : 300;
        scheduler.scheduleAtFixedRate(
            this::backgroundTask,
            0, refreshInterval, TimeUnit.SECONDS
        );
    }

    @Override
    public void onDisable(PluginContext context) {
        if (scheduler != null) {
            scheduler.shutdown();
            scheduler = null;
        }
        super.onDisable(context);
    }

    @Override
    public void onUninstall(PluginContext context) {
        onDisable(context);
        runtimeState = null;
        super.onUninstall(context);
    }

    @Override
    public Map<String, Object> execute(String action, Map<String, Object> params) {
        switch (action) {
            case "analyze":
                return analyze(params);
            case "report":
                return generateReport(params);
            case "status":
                return Map.of("enabled", enabled, "state", runtimeState);
            default:
                return Map.of("error", "Unknown action: " + action);
        }
    }

    private Map<String, Object> analyze(Map<String, Object> params) {
        String metric = (String) params.getOrDefault("metric", "default");
        return Map.of(
            "metric", metric,
            "result", 42,
            "unit", "percent",
            "timestamp", System.currentTimeMillis()
        );
    }

    private Map<String, Object> generateReport(Map<String, Object> params) {
        return Map.of(
            "reportUrl", "/reports/latest",
            "format", params.getOrDefault("format", "pdf"),
            "generatedAt", System.currentTimeMillis()
        );
    }

    private void backgroundTask() {
        // Periodic background processing
        if (!enabled) return;
        // ... analytics computation ...
    }
}
```

---

## Writing PluginManifest

### Inline Java Declaration

When manifests are defined in code, use the `PluginManifest` record constructor:

```java
new PluginManifest(
    "com.example.my-plugin",              // pluginId
    "My Plugin",                          // name
    "1.0.0",                              // version
    "Acme Corp",                          // author
    "Description",                        // description
    PluginType.ANALYTICS,                 // type
    List.of(PluginCapability.ANALYTICS),  // capabilities
    List.of(PluginPermission.READ_PRODUCTS),  // requiredPermissions
    List.of("dep-plugin"),                // dependencies
    "1.0.0",                              // minPlatformVersion
    "2.0.0",                              // maxPlatformVersion
    "/health",                            // healthEndpoint
    Map.of("key", Map.of("type", "string")), // configurationSchema
    "com.example.plugin.MyPlugin"         // entryPoint
);
```

### External JSON/YAML File

For complex plugins, define the manifest externally:

```json
{
  "pluginId": "com.example.my-plugin",
  "name": "My Plugin",
  "version": "1.0.0",
  "author": "Acme Corp",
  "description": "Does something useful",
  "type": "ANALYTICS",
  "capabilities": ["ANALYTICS", "REPORTING"],
  "requiredPermissions": ["READ_ANALYTICS", "READ_PRODUCTS"],
  "dependencies": ["com.example.core-lib"],
  "minPlatformVersion": "1.0.0",
  "maxPlatformVersion": "2.0.0",
  "healthEndpoint": "/health",
  "configurationSchema": {
    "type": "object",
    "properties": {
      "refreshInterval": { "type": "integer", "default": 300 }
    }
  },
  "entryPoint": "com.example.plugin.MyPlugin"
}
```

---

## Using PluginContext

The `PluginContext` is provided to all lifecycle methods. Use it to:

### Read Configuration

```java
@Override
public void onEnable(PluginContext context) {
    // Access typed config values
    int timeout = Integer.parseInt(
        context.getConfigValue("timeout")
    );

    // Access the full config map for complex structures
    Map<String, Object> config = context.getConfig();
    Map<String, Object> nestedConfig = (Map<String, Object>) config.get("database");
}
```

### Manage Workspace State

```java
@Override
public void onInstall(PluginContext context) {
    // Store runtime state in the workspace
    context.getWorkspace().put("initialized", true);
    context.getWorkspace().put("createdAt", Instant.now().toString());
}

@Override
public Map<String, Object> execute(String action, Map<String, Object> params) {
    // Read workspace state
    Object initialized = getContext().getWorkspace().get("initialized");
    return Map.of("initialized", initialized);
}
```

---

## Handling Lifecycle Hooks

### onInstall

Called once when the plugin is first registered. Initialize permanent state:

```java
@Override
public void onInstall(PluginContext context) {
    // Create required directories
    // Initialize database schemas
    // Register webhook URLs
    // Allocate long-lived resources
}
```

### onEnable / onDisable

Called each time the plugin is activated or deactivated:

```java
@Override
public void onEnable(PluginContext context) {
    // Start background threads
    // Open connections
    // Register event listeners
    // Begin periodic tasks
}

@Override
public void onDisable(PluginContext context) {
    // Stop background threads (scheduler.shutdown())
    // Close connections gracefully
    // Flush pending data
    // Save checkpoint state
}
```

### onUpgrade / onDowngrade

Called when the plugin version changes. Migrate state between versions:

```java
@Override
public void onUpgrade(PluginContext context, String previousVersion) {
    switch (previousVersion) {
        case "1.0.0" -> migrateFromV1();
        case "1.1.0" -> migrateFromV1_1();
    }
}

@Override
public void onDowngrade(PluginContext context, String previousVersion) {
    // Revert schema changes
    // Roll back data migrations
    log.warn("Downgrading from {} - some data may be lost", previousVersion);
}
```

### onUninstall

Clean up all resources permanently:

```java
@Override
public void onUninstall(PluginContext context) {
    // Drop database tables
    // Delete plugin directories
    // Unregister webhooks
    // Remove temporary files
    // Revoke API keys
}
```

---

## Registering Capabilities

Capabilities are automatically registered during install based on the manifest. For dynamic capability registration:

```java
// Via the KnowledgePackIndexer (for knowledge packs)
var pack = new KnowledgePack(
    "faq-pack-001",
    "com.example.my-plugin",
    "Product FAQ",
    "FAQ about products",
    KnowledgePackType.FAQ,
    List.of("faq-1.md", "faq-2.md"),
    Instant.now()
);
knowledgePackIndexer.indexPack("com.example.my-plugin", "My Plugin", pack);

// Via CapabilityRegistry directly
capabilityRegistry.register("com.example.my-plugin", "My Plugin", PluginCapability.ANALYTICS);
```

---

## Security Best Practices

### 1. Request Minimal Permissions

Only declare the permissions your plugin absolutely needs:

```java
// Good: only request what you need
List.of(PluginPermission.READ_PRODUCTS)

// Bad: request everything
List.of(PluginPermission.values())  // AVOID THIS
```

### 2. Validate Before Execution

Always check permissions in your `execute()` method:

```java
@Override
public Map<String, Object> execute(String action, Map<String, Object> params) {
    // Permission check (platform layer enforced, but good practice)
    if ("delete".equals(action) && !isAuthorizedForDeletion()) {
        return Map.of("error", "Not authorized");
    }
    // ... proceed with action ...
}
```

### 3. Sandbox Resource Awareness

Be mindful of sandbox limits:
- **30s execution timeout**: Long-running tasks should be broken into chunks
- **5 threads max**: Use a bounded thread pool, not unlimited threads
- **256MB memory**: Avoid loading large datasets into memory at once

### 4. Secure Configuration

Never hardcode secrets. Use configuration parameters:

```java
// Good: read from context config
String apiKey = context.getConfigValue("apiKey");

// Bad: hardcoded
String apiKey = "sk-123456789";  // AVOID THIS
```

### 5. Handle Errors Gracefully

```java
@Override
public Map<String, Object> execute(String action, Map<String, Object> params) {
    try {
        // Plugin logic
        return Map.of("result", "success");
    } catch (Exception e) {
        return Map.of(
            "error", e.getMessage(),
            "action", action,
            "timestamp", Instant.now().toString()
        );
    }
}
```

### 6. Clean Up on Uninstall

Use `onUninstall` to revoke all external access:

```java
@Override
public void onUninstall(PluginContext context) {
    // Invalidate API keys
    // Drop webhook subscriptions
    // Close all connections
    // Delete temporary data
}
```

---

## Packaging and Distribution

### Step 1: Build the Plugin JAR

```bash
# Build with dependencies bundled
mvn clean package shade:shade

# Output: target/my-plugin-1.0.0.jar
```

### Step 2: Create a Plugin Bundle

A plugin bundle includes:
- The plugin JAR file
- The manifest file (if external)
- A README with usage instructions
- Optional: Dockerfile for sandboxed deployment

### Step 3: Deploy to Marketplace

```bash
# Copy the JAR to the marketplace plugin store
cp target/my-plugin-1.0.0.jar /path/to/copilot-marketplace-service/plugins/

# Or deploy via API
curl -X POST http://localhost:8110/api/v1/plugins/install \
  -H "Content-Type: application/json" \
  -d '{"pluginId": "com.example.my-plugin", "source": "marketplace"}'
```

### Step 4: Enable and Verify

```bash
# Enable
curl -X POST http://localhost:8110/api/v1/plugins/enable/com.example.my-plugin

# Execute
curl -X POST http://localhost:8110/api/v1/plugins/com.example.my-plugin/execute \
  -H "Content-Type: application/json" \
  -d '{"action": "analyze", "params": {"metric": "sales"}}'

# Check health
curl http://localhost:8110/api/v1/plugins/health
```

---

## Quick Checklist

Before submitting your plugin:

- [ ] Manifest has all required fields populated
- [ ] `pluginId` follows reverse-domain convention
- [ ] `entryPoint` matches the fully qualified class name
- [ ] `capabilities` list is non-empty
- [ ] `requiredPermissions` lists only needed permissions
- [ ] `minPlatformVersion` / `maxPlatformVersion` are set correctly
- [ ] All lifecycle methods are implemented
- [ ] `execute()` handles unknown actions gracefully
- [ ] `healthCheck()` returns accurate status
- [ ] `onUninstall()` cleans up all resources
- [ ] Plugin builds successfully with `mvn clean package`
- [ ] Plugin passes all unit tests
- [ ] No hardcoded secrets or credentials
- [ ] Configuration schema is provided (if applicable)
- [ ] Dependencies are documented
