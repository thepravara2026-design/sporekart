# PluginManifest Specification

The `PluginManifest` is a Java record with 16 fields that defines the static identity, capabilities, permissions, dependencies, and configuration of a marketplace plugin. It is defined at `com.sporekart.marketplace.sdk.PluginManifest`.

---

## Field Reference

| #  | Field                 | Type                        | Required | Description                                                    |
|----|-----------------------|-----------------------------|----------|----------------------------------------------------------------|
| 1  | `pluginId`            | `String`                    | Yes      | Globally unique identifier for the plugin                     |
| 2  | `name`                | `String`                    | Yes      | Human-readable display name                                   |
| 3  | `version`             | `String`                    | Yes      | Semantic version string (e.g., "1.2.3")                        |
| 4  | `author`              | `String`                    | Yes      | Plugin author or organization name                             |
| 5  | `description`         | `String`                    | Yes      | Short summary of plugin purpose and behaviour                  |
| 6  | `type`                | `PluginType`                | Yes      | Plugin category from the 10-type enum                          |
| 7  | `capabilities`        | `List<PluginCapability>`    | Yes      | Capabilities this plugin provides (at least 1 required)        |
| 8  | `requiredPermissions` | `List<PluginPermission>`    | Yes      | Permissions the plugin needs to function (must not be null)    |
| 9  | `dependencies`        | `List<String>`              | No       | Plugin IDs that must be installed alongside this plugin        |
| 10 | `minPlatformVersion`  | `String`                    | No       | Minimum platform version required (e.g., "1.0.0")             |
| 11 | `maxPlatformVersion`  | `String`                    | No       | Maximum platform version supported (e.g., "2.0.0")            |
| 12 | `healthEndpoint`      | `String`                    | No       | Relative path to the plugin's health endpoint                  |
| 13 | `configurationSchema` | `Map<String, Object>`       | No       | JSON Schema definition for plugin configuration                |
| 14 | `entryPoint`          | `String`                    | Yes      | Fully qualified class name implementing `SporekartPlugin`      |

Note: The record definition also includes `entryPoint` as the 14th field alongside the fields visible in the source:

```java
public record PluginManifest(
    String pluginId,              // 1
    String name,                  // 2
    String version,               // 3
    String author,                // 4
    String description,           // 5
    PluginType type,              // 6
    List<PluginCapability> capabilities,  // 7
    List<PluginPermission> requiredPermissions, // 8
    List<String> dependencies,    // 9
    String minPlatformVersion,    // 10
    String maxPlatformVersion,    // 11
    String healthEndpoint,        // 12
    Map<String, Object> configurationSchema, // 13
    String entryPoint             // 14
) {}
```

---

## Field Details

### pluginId
Must be globally unique across the marketplace. Convention: use reverse-domain notation (e.g., `com.sporekart.plugin.analytics`). Used as the primary key in `PluginRegistry`.

### name
Display name shown in the marketplace UI and API responses. Should be concise (max 100 characters).

### version
Follows semantic versioning (`MAJOR.MINOR.PATCH`). The `VersionManager` supports full semver comparison. The `PluginVersion.compareTo` implementation splits on `.` and compares numerically.

### author
May be an individual name, organization name, or both (e.g., `"SporeKart Inc."`).

### type
Must be one of: `AI_COPILOT`, `KNOWLEDGE`, `WORKFLOW`, `ANALYTICS`, `CONNECTOR`, `DASHBOARD`, `AUTOMATION`, `REPORTING`, `NOTIFICATION`, `ML`.

### capabilities
At least one capability is required. The full list: `CONVERSATION`, `KNOWLEDGE`, `SEARCH`, `FORECAST`, `MARKETING`, `ANALYTICS`, `REPORTING`, `INVENTORY`, `ORDERS`, `TRAINING`, `NOTIFICATIONS`, `AI_MODELS`, `DASHBOARD`, `WORKFLOW`, `AUTOMATION`.

Capabilities are registered in `CapabilityRegistry` on install.

### requiredPermissions
The permissions the plugin declares it needs. Must not be null (can be empty list). Available values: `READ_PRODUCTS`, `READ_ORDERS`, `READ_INVENTORY`, `READ_CUSTOMERS`, `READ_ANALYTICS`, `READ_TRAINING`, `EXECUTE_AI`, `ACCESS_KNOWLEDGE`, `SEND_NOTIFICATIONS`, `CALL_APIS`.

These permissions are auto-approved during install in `MarketplaceOrchestrator.installPlugin()` but can be individually controlled via `PermissionManager`.

### dependencies
List of `pluginId` values that must be installed before this plugin. The platform does not currently enforce dependency resolution at install time; it is the plugin author's responsibility to document dependencies.

### minPlatformVersion / maxPlatformVersion
Define the compatibility range with the platform. The platform version is `1.0.0`. The `VersionManager.isCompatible()` method checks:

```java
public boolean isCompatible(String minVersion, String maxVersion) {
    if (minVersion != null && compareVersions(PLATFORM_VERSION, minVersion) < 0) return false;
    if (maxVersion != null && !maxVersion.isBlank()
        && compareVersions(PLATFORM_VERSION, maxVersion) > 0) return false;
    return true;
}
```

So a plugin claiming `minPlatformVersion = "1.0.0"` and `maxPlatformVersion = "2.0.0"` is compatible with platform `1.0.0`.

### healthEndpoint
Optional path for detailed health checks (e.g., `"/health"`). Used by `PluginHealthMonitor` for periodic health polling.

### configurationSchema
A JSON Schema-compatible map that describes the expected configuration structure. Used for validating plugin config in `PluginContext.getConfig()`.

### entryPoint
The fully qualified class name of the plugin implementation (e.g., `"com.example.MyPlugin"`). Used by `PluginLoader` to instantiate the plugin.

---

## Validation Rules

The `PluginValidator` enforces these rules during `lifecycleManager.install()`:

| Field                | Validation Rule                                       |
|----------------------|-------------------------------------------------------|
| `pluginId`           | Must not be null or blank                             |
| `name`               | Must not be null or blank                             |
| `version`            | Must not be null or blank                             |
| `type`               | Must not be null                                      |
| `author`             | Must not be null or blank                             |
| `entryPoint`         | Must not be null or blank                             |
| `capabilities`       | Must not be null or empty                             |
| `requiredPermissions`| Must not be null                                      |
| `minPlatformVersion` | Must be compatible with platform version if specified |
| `maxPlatformVersion` | Must be compatible with platform version if specified |

If validation fails, the plugin enters the `ERROR` state and the install operation throws an `IllegalStateException`.

---

## Example Manifest (YAML)

```yaml
pluginId: com.sporekart.plugin.inventory-assistant
name: Inventory Assistant
version: 1.2.0
author: SporeKart Inc.
description: AI-powered inventory forecasting and management plugin
type: AI_COPILOT
capabilities:
  - CONVERSATION
  - FORECAST
  - INVENTORY
requiredPermissions:
  - READ_INVENTORY
  - READ_PRODUCTS
  - EXECUTE_AI
dependencies:
  - com.sporekart.plugin.core-models
minPlatformVersion: "1.0.0"
maxPlatformVersion: "2.0.0"
healthEndpoint: /health
configurationSchema:
  type: object
  properties:
    forecastWindowDays:
      type: integer
      default: 90
    alertThreshold:
      type: number
      default: 0.2
entryPoint: com.sporekart.plugin.inventory.InventoryAssistantPlugin
```

## Example Manifest (JSON)

```json
{
  "pluginId": "com.sporekart.plugin.inventory-assistant",
  "name": "Inventory Assistant",
  "version": "1.2.0",
  "author": "SporeKart Inc.",
  "description": "AI-powered inventory forecasting and management plugin",
  "type": "AI_COPILOT",
  "capabilities": ["CONVERSATION", "FORECAST", "INVENTORY"],
  "requiredPermissions": ["READ_INVENTORY", "READ_PRODUCTS", "EXECUTE_AI"],
  "dependencies": ["com.sporekart.plugin.core-models"],
  "minPlatformVersion": "1.0.0",
  "maxPlatformVersion": "2.0.0",
  "healthEndpoint": "/health",
  "configurationSchema": {
    "type": "object",
    "properties": {
      "forecastWindowDays": { "type": "integer", "default": 90 },
      "alertThreshold": { "type": "number", "default": 0.2 }
    }
  },
  "entryPoint": "com.sporekart.plugin.inventory.InventoryAssistantPlugin"
}
```

## Minimal Example Manifest

The most minimal valid manifest:

```json
{
  "pluginId": "simple-plugin",
  "name": "Simple Plugin",
  "version": "1.0.0",
  "author": "Dev",
  "description": "A minimal plugin",
  "type": "AUTOMATION",
  "capabilities": ["WORKFLOW"],
  "requiredPermissions": [],
  "dependencies": [],
  "entryPoint": "com.example.SimplePlugin"
}
```
