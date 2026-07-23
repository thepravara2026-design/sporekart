# Plugin Lifecycle Management

## Lifecycle States

The `PluginState` enum defines 7 states that a plugin instance can be in:

| State          | Description                                               |
|----------------|-----------------------------------------------------------|
| `INSTALLED`    | Plugin has been registered in the registry but not yet enabled |
| `ENABLED`      | Plugin is active and ready to handle requests             |
| `DISABLED`     | Plugin is inactive; no actions can be executed            |
| `ERROR`        | Plugin encountered a failure during install, validation, or execution |
| `UPGRADING`    | Plugin is in the process of being upgraded to a new version |
| `UNINSTALLING` | Plugin is in the process of being removed                 |
| `UNINSTALLED`  | Plugin has been fully removed from the registry           |

---

## State Machine Transitions

```
                    ┌─────────────┐
                    │  NEW        │ (not yet registered)
                    └──────┬──────┘
                           │ validate + register
                           ▼
                    ┌─────────────┐
           ┌─────── │  INSTALLED  │ ←───────┐
           │        └──────┬──────┘         │
           │               │ enable         │
           │               ▼                │
           │        ┌─────────────┐         │
           │ ┌───── │   ENABLED   │ ────────┤
           │ │      └──────┬──────┘         │
           │ │             │ disable        │
           │ │             ▼                │
           │ │      ┌─────────────┐         │
           │ │  ┌── │  DISABLED   │ ────────┘
           │ │  │   └──────┬──────┘
           │ │  │          │ upgrade/downgrade
           │ │  │          ▼
           │ │  │   ┌─────────────┐
           │ │  │   │  UPGRADING  │ (transient)
           │ │  │   └─────────────┘
           │ │  │          │
           │ │  │          ▼  (back to ENABLED)
           │ │  │
           │ │  │          ┌─────────────┐
           │ │  ├───────── │    ERROR    │ ← validation failure, execution error
           │ │  │          └─────────────┘
           │ │  │
           │ │  │          ┌────────────────┐
           │ │  ├───────── │  UNINSTALLING  │ (transient)
           │ │  │          └───────┬────────┘
           │ │  │                  ▼
           │ │  │          ┌─────────────┐
           │ │  └───────── │ UNINSTALLED  │ (terminal)
           │ │             └─────────────┘
           │ │
           │ │  --- re-install from UNINSTALLED goes back to INSTALLED ---
           │ └────────────────────────────────────────────────────────────┘
           │
           └─── re-enable from ERROR (via reinstall) ──┘
```

---

## PluginLifecycleManager

The `PluginLifecycleManager` is the central orchestrator for all lifecycle transitions. It coordinates between `PluginRegistry` (storage), `PluginValidator` (validation), and the plugin's `SporekartPlugin` implementation (lifecycle callbacks).

### Install

```java
public PluginInstance install(PluginInstance instance)
```

1. Validates the manifest via `PluginValidator.validate()`
2. If validation fails → sets state to `ERROR`, registers the instance, throws `IllegalStateException`
3. If validation passes → registers the instance, sets state to `INSTALLED`
4. Returns the registered instance

Called from `MarketplaceOrchestrator.installPlugin()` which additionally records version, registers capabilities, and approves permissions.

### Enable

```java
public Optional<PluginInstance> enable(String pluginId, Map<String, Object> config)
```

1. Looks up the plugin in registry
2. If found and has a plugin implementation → creates `PluginContext` with config → calls `plugin.onEnable(context)`
3. Sets state to `ENABLED`
4. Returns the instance

### Disable

```java
public Optional<PluginInstance> disable(String pluginId)
```

1. Looks up the plugin
2. Calls `plugin.onDisable(context)` if plugin is loaded
3. Sets state to `DISABLED`
4. Returns the instance

### Uninstall

```java
public Optional<PluginInstance> uninstall(String pluginId)
```

1. Looks up the plugin
2. Calls `plugin.onUninstall(context)` if plugin is loaded
3. Sets state to `UNINSTALLED`
4. Unregisters from `PluginRegistry` (removes from ConcurrentHashMap)
5. Returns the instance

Note: The orchestrator layer (`MarketplaceOrchestrator`) also calls `permissionManager.revokeAll()` and `capabilityRegistry.unregisterAll()` after the lifecycle manager completes.

### Upgrade

```java
public Optional<PluginInstance> upgrade(String pluginId, SporekartPlugin newPlugin, String previousVersion)
```

1. Looks up the current plugin
2. Calls `plugin.onDisable(context)` on the current plugin
3. Replaces the plugin reference with `newPlugin`
4. Calls `newPlugin.onUpgrade(context, previousVersion)` on the new plugin
5. Sets state to `ENABLED`
6. Returns the instance

### Downgrade

```java
public Optional<PluginInstance> downgrade(String pluginId, SporekartPlugin downgradedPlugin, String previousVersion)
```

Same flow as upgrade but calls `onDowngrade` instead of `onUpgrade`.

### Execute Action

```java
public Optional<Map<String, Object>> executeAction(String pluginId, String action, Map<String, Object> params)
```

1. Looks up the plugin
2. Filters: only if plugin is in `ENABLED` state (`instance.isEnabled()`)
3. Calls `plugin.execute(action, params)` if plugin is loaded
4. Returns the result map (or error map if plugin not loaded)
5. Returns `Optional.empty()` if plugin not found or not enabled

---

## VersionManager

The `VersionManager` handles platform version compatibility and semver comparison.

### Platform Version

The current platform version is `1.0.0`:

```java
private static final String PLATFORM_VERSION = "1.0.0";
```

### Version Compatibility

```java
public boolean isCompatible(PluginVersion version)
```

Checks that:
- `version.minPlatformVersion <= PLATFORM_VERSION`
- `PLATFORM_VERSION <= version.maxPlatformVersion` (if max is specified)

### Upgrade/Downgrade Detection

```java
public boolean canUpgrade(String currentVersion, String targetVersion)  // target > current
public boolean canDowngrade(String currentVersion, String targetVersion) // target < current
```

### Version History

```java
public void recordVersion(String pluginId, PluginVersion version)
public List<PluginVersion> getVersionHistory(String pluginId)
public Optional<PluginVersion> getLatestVersion(String pluginId)
```

### Semver Comparison

The `compareVersions` method splits on `.` and compares numerically:

```java
private int compareVersions(String v1, String v2) {
    var parts1 = v1.split("\\.");
    var parts2 = v2.split("\\.");
    for (int i = 0; i < Math.min(parts1.length, parts2.length); i++) {
        int cmp = Integer.compare(Integer.parseInt(parts1[i]), Integer.parseInt(parts2[i]));
        if (cmp != 0) return cmp;
    }
    return Integer.compare(parts1.length, parts2.length);
}
```

Handles any number of version segments (e.g., `1.0.0`, `2.1`, `3.0.0.alpha`).

---

## PluginValidator

The `PluginValidator` enforces the following validation rules during install:

| Rule                    | Failure Behaviour                        |
|-------------------------|------------------------------------------|
| `pluginId` not blank    | Error added to list                      |
| `name` not blank        | Error added to list                      |
| `version` not blank     | Error added to list                      |
| `type` not null         | Error added to list                      |
| `author` not blank      | Error added to list                      |
| `entryPoint` not blank  | Error added to list                      |
| `capabilities` non-empty| Error added to list                      |
| `requiredPermissions` not null | Error added to list              |
| Platform compatibility  | Error if min/max version not compatible  |

If any errors are found, the `PluginLifecycleManager.install()` method sets the state to `ERROR` and throws an `IllegalStateException`.

---

## Edge Cases

### Duplicate Installation
If a plugin with the same `pluginId` is already registered, `PluginLoader.loadPlugin()` throws `IllegalStateException("Plugin already registered: " + id)`.

### Resource Exhaustion
`ResourceLimiter.allocateResources()` checks if the total active plugins exceeds `maxInstalledPlugins` (default 100). If exceeded, allocation returns `false`.

### Timeout During Execution
`PluginSandbox.executeInSandbox()` wraps plugin execution in a `Future.get()` with a configurable timeout (default 30s). If the plugin exceeds this timeout, it returns an error map `{"error": "Execution timed out"}`.

### Consecutive Health Check Failures
`PluginHealthMonitor` tracks `consecutiveFailures` in `PluginHealthStatus`. The failure threshold (default 3) and recovery threshold (default 2) are configurable via `MarketplaceConfig.HealthConfig`.

### Plugin Not Loaded
If a plugin instance exists but has no attached `SporekartPlugin` implementation (e.g., it was registered but not loaded), `executeAction` returns `Map.of("error", "Plugin not loaded")`.

### Invalid Version Format
If version strings are not parseable (non-numeric segments), `VersionManager.compareVersions()` logs a warning and returns `0` (equal), allowing the operation to proceed rather than throwing an exception.
