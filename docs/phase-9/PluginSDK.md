# Plugin SDK Reference

The Plugin SDK defines the core interfaces, base classes, enums, and contracts that every marketplace plugin must implement. The SDK resides in the `com.sporekart.marketplace.sdk` package.

---

## SporekartPlugin Interface

The `SporekartPlugin` interface is the primary contract all plugins must implement. It defines 8 lifecycle and execution methods:

```java
public interface SporekartPlugin {
    PluginManifest getManifest();
    void onInstall(PluginContext context);
    void onEnable(PluginContext context);
    void onDisable(PluginContext context);
    void onUninstall(PluginContext context);
    void onUpgrade(PluginContext context, String previousVersion);
    void onDowngrade(PluginContext context, String previousVersion);
    Map<String, Object> execute(String action, Map<String, Object> params);
    Map<String, Object> healthCheck();
}
```

### Method Reference

| Method             | Called When                              | Purpose                                                |
|--------------------|------------------------------------------|--------------------------------------------------------|
| `getManifest`      | Registration and discovery               | Returns the plugin's static definition                 |
| `onInstall`        | Plugin first installed                   | Initialize resources, allocate state                   |
| `onEnable`         | Plugin enabled after install             | Activate plugin, start background tasks                |
| `onDisable`        | Plugin disabled                          | Suspend activity, release non-essential resources      |
| `onUninstall`      | Plugin removed                           | Clean up all resources, deallocate state               |
| `onUpgrade`        | Version upgraded                         | Migrate state from `previousVersion`                   |
| `onDowngrade`      | Version downgraded                       | Revert state from `previousVersion`                    |
| `execute`          | Runtime action invocation                | Perform a named action with parameters                 |
| `healthCheck`      | Scheduled health monitoring              | Return current health status and diagnostics           |

---

## AbstractPlugin Base Class

`AbstractPlugin` provides default implementations for all lifecycle methods, making it easy to create plugins that only need to override relevant methods:

```java
public abstract class AbstractPlugin implements SporekartPlugin {
    protected PluginContext context;
    protected boolean enabled = false;

    @Override public void onInstall(PluginContext ctx) { this.context = ctx; }
    @Override public void onEnable(PluginContext ctx)  { this.context = ctx; this.enabled = true; }
    @Override public void onDisable(PluginContext ctx) { this.enabled = false; }
    @Override public void onUninstall(PluginContext ctx) { this.enabled = false; this.context = null; }
    @Override public void onUpgrade(PluginContext ctx, String previousVersion) { this.context = ctx; }
    @Override public void onDowngrade(PluginContext ctx, String previousVersion) { this.context = ctx; }
    @Override public Map<String, Object> healthCheck() {
        return Map.of("status", enabled ? "HEALTHY" : "DISABLED",
                      "pluginId", getManifest().pluginId());
    }
    public boolean isEnabled() { return enabled; }
    public PluginContext getContext() { return context; }
}
```

Plugins should extend `AbstractPlugin` and override only the methods they need. The base class tracks the `enabled` flag and stores the `PluginContext`.

---

## PluginContext

`PluginContext` provides runtime access to the plugin's configuration and workspace:

```java
public class PluginContext {
    private final String pluginId;
    private final Map<String, Object> config;
    private final Map<String, Object> workspace;

    public String getPluginId();
    public Map<String, Object> getConfig();
    public Map<String, Object> getWorkspace();
    public String getConfigValue(String key);
}
```

- **pluginId**: Unique identifier for the plugin instance
- **config**: Configuration map set during install/enable (typically deserialized from `MarketplaceConfig`)
- **workspace**: Mutable workspace map for storing runtime state across lifecycle calls
- **getConfigValue**: Convenience method to retrieve a config value by key as a String

---

## PluginManifest Structure

The `PluginManifest` is a Java record with 16 fields that defines the static identity and requirements of a plugin:

| # | Field                  | Type                        | Description                                   |
|---|------------------------|-----------------------------|-----------------------------------------------|
| 1 | `pluginId`             | `String`                    | Unique identifier                             |
| 2 | `name`                 | `String`                    | Human-readable display name                   |
| 3 | `version`              | `String`                    | Semantic version (e.g., "1.0.0")              |
| 4 | `author`               | `String`                    | Plugin author/org name                        |
| 5 | `description`          | `String`                    | Short description of plugin purpose           |
| 6 | `type`                 | `PluginType`                | Plugin category (10 types)                    |
| 7 | `capabilities`         | `List<PluginCapability>`    | Capabilities this plugin provides             |
| 8 | `requiredPermissions`  | `List<PluginPermission>`    | Permissions the plugin needs                  |
| 9 | `dependencies`         | `List<String>`              | IDs of plugins this depends on                |
|10 | `minPlatformVersion`   | `String`                    | Minimum platform version required             |
|11 | `maxPlatformVersion`   | `String`                    | Maximum platform version supported            |
|12 | `healthEndpoint`       | `String`                    | Health check endpoint path                    |
|13 | `configurationSchema`  | `Map<String, Object>`       | JSON Schema for plugin configuration          |
|14 | `entryPoint`           | `String`                    | Fully qualified class name of plugin impl     |

Refer to `PluginManifest.md` for the full specification with YAML/JSON examples.

---

## PluginMetadata

`PluginMetadata` captures runtime information about an installed plugin:

```java
public record PluginMetadata(
    String pluginId,
    String installedVersion,
    Instant installedAt,
    Instant lastUpdatedAt,
    Instant lastHealthCheckAt,
    String installSource,
    Map<String, String> customAttributes
) {}
```

| Field                | Description                                      |
|----------------------|--------------------------------------------------|
| `installedVersion`   | Version string at install time                   |
| `installedAt`        | Timestamp of first installation                  |
| `lastUpdatedAt`      | Timestamp of last version update                 |
| `lastHealthCheckAt`  | Timestamp of last health check                   |
| `installSource`      | Source identifier (e.g., "local", "marketplace") |
| `customAttributes`   | Extensible key-value metadata store              |

---

## PluginType Enum (10 Values)

```java
public enum PluginType {
    AI_COPILOT,
    KNOWLEDGE,
    WORKFLOW,
    ANALYTICS,
    CONNECTOR,
    DASHBOARD,
    AUTOMATION,
    REPORTING,
    NOTIFICATION,
    ML
}
```

See `Marketplace.md` for descriptions of each type.

---

## PluginCapability (16 Values)

```java
public enum PluginCapability {
    CONVERSATION, KNOWLEDGE, SEARCH, FORECAST,
    MARKETING, ANALYTICS, REPORTING, INVENTORY,
    ORDERS, TRAINING, NOTIFICATIONS, AI_MODELS,
    DASHBOARD, WORKFLOW, AUTOMATION
}
```

Capabilities are registered in `CapabilityRegistry` and are discoverable via the `GET /api/v1/plugins/capabilities` endpoint.

---

## PluginPermission (10 Values)

```java
public enum PluginPermission {
    READ_PRODUCTS,
    READ_ORDERS,
    READ_INVENTORY,
    READ_CUSTOMERS,
    READ_ANALYTICS,
    READ_TRAINING,
    EXECUTE_AI,
    ACCESS_KNOWLEDGE,
    SEND_NOTIFICATIONS,
    CALL_APIS
}
```

Permissions are declared in the manifest and approved via `PermissionManager`. See `SecurityModel.md` for details.

---

## PluginHook (10 Hooks)

```java
public enum PluginHook {
    BEFORE_CONVERSATION,
    AFTER_CONVERSATION,
    BEFORE_QUERY,
    AFTER_QUERY,
    BEFORE_ANALYTICS,
    AFTER_ANALYTICS,
    ON_ERROR,
    ON_DASHBOARD_LOAD,
    ON_REPORT_GENERATE,
    ON_NOTIFICATION_SEND
}
```

| Hook                     | Fires Before/After              |
|--------------------------|----------------------------------|
| `BEFORE_CONVERSATION`    | Before copilot conversation      |
| `AFTER_CONVERSATION`     | After copilot conversation       |
| `BEFORE_QUERY`           | Before a data query              |
| `AFTER_QUERY`            | After a data query               |
| `BEFORE_ANALYTICS`       | Before analytics computation     |
| `AFTER_ANALYTICS`        | After analytics computation      |
| `ON_ERROR`               | When an error occurs             |
| `ON_DASHBOARD_LOAD`      | When a dashboard loads           |
| `ON_REPORT_GENERATE`     | When a report is generated       |
| `ON_NOTIFICATION_SEND`   | When a notification is sent      |

Hooks are available for plugins to register interest in platform events, though the hook subscription mechanism is handled at the platform level.

---

## How to Implement a Plugin

1. **Create a Java project** that depends on the Plugin SDK module
2. **Create a manifest** as `plugin-manifest.json` or `plugin-manifest.yaml`
3. **Implement `SporekartPlugin`** or extend `AbstractPlugin`
4. **Override lifecycle methods** for your plugin's needs
5. **Implement `execute()`** to handle runtime action invocations
6. **Implement `healthCheck()`** to return status diagnostics
7. **Package** your plugin as a JAR with the manifest bundled

Example minimal plugin:

```java
public class MyPlugin extends AbstractPlugin {
    private final PluginManifest manifest = new PluginManifest(
        "my-plugin", "My Plugin", "1.0.0", "Acme Corp",
        "Does something useful", PluginType.AUTOMATION,
        List.of(PluginCapability.WORKFLOW),
        List.of(PluginPermission.READ_PRODUCTS),
        List.of(), "1.0.0", "2.0.0",
        "/health", Map.of(), "com.example.MyPlugin"
    );

    @Override
    public PluginManifest getManifest() { return manifest; }

    @Override
    public Map<String, Object> execute(String action, Map<String, Object> params) {
        return Map.of("result", "executed " + action, "params", params);
    }
}
```
