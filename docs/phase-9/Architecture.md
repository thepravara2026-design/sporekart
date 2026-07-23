# Architecture Guide

## Service Placement

The Copilot Marketplace runs as a dedicated Spring Boot microservice:

| Attribute          | Value                          |
|--------------------|--------------------------------|
| Application Name   | `copilot-marketplace-service`  |
| Port               | `8110`                         |
| Package            | `com.sporekart.marketplace`    |
| Java               | 21                             |
| Spring Boot        | 3.3.3                          |
| Main Class         | `CopilotMarketplaceApplication`|

### Gateway Routing

The Spring Cloud Gateway service routes external traffic to the marketplace via `RouteConfig`:

```java
ROUTE_MAP.put("copilot-marketplace", "/api/v1/plugins");
```

All requests to `/api/v1/plugins/**` are forwarded to the marketplace service at `localhost:8110`. The gateway strips one prefix level, applies a circuit breaker with fallback, and retries on `503`/`504` statuses.

---

## Component Diagram

```
                          ┌─────────────────────┐
                          │   API Gateway        │
                          │   /api/v1/plugins    │
                          │   port 8110          │
                          └──────────┬──────────┘
                                     │
                          ┌──────────▼──────────┐
                          │ SecurityConfig       │
                          │ (Spring Security)    │
                          │ HTTP Basic / Stateless│
                          └──────────┬──────────┘
                                     │
                          ┌──────────▼──────────────┐
                          │ PluginMarketplaceController │
                          │ (11 endpoints)          │
                          └──────────┬──────────────┘
                                     │
                          ┌──────────▼──────────────────┐
                          │  MarketplaceOrchestrator     │
                          │  (orchestration service)     │
                          └──┬────┬────┬────┬────┬──────┘
                             │    │    │    │    │
              ┌──────────────┘    │    │    │    └──────────────┐
              │                   │    │    │                   │
     ┌────────▼──────┐   ┌───────▼───────▼──┐   ┌─────────────▼──────┐
     │ PluginRegistry │   │ PluginLifecycle   │   │ CapabilityRegistry │
     │ ConcurrentHashMap│  │ Manager           │   │ ConcurrentHashMap   │
     │ pluginId →     │   │ install/enable/   │   │ capabilityId →     │
     │ PluginInstance │   │ disable/uninstall │   │ CapabilityReg.    │
     └────────────────┘   │ upgrade/downgrade │   └────────────────────┘
                          └───────┬───────────┘
                                  │
                 ┌────────────────┼────────────────┐
                 │                │                 │
        ┌────────▼──────┐ ┌──────▼───────┐ ┌───────▼──────────┐
        │ PluginValidator│ │VersionManager │ │ PermissionManager│
        │ validation     │ │ semver compare│ │ request/approve/ │
        │ rules          │ │ platform 1.0.0│ │ revoke           │
        └────────────────┘ └──────────────┘ └───────┬──────────┘
                                                    │
                                         ┌──────────▼──────────┐
                                         │ PermissionValidator  │
                                         │ execution validation │
                                         └─────────────────────┘

                          ┌──────────────────────────────────┐
                          │         PluginSandbox             │
                          │  ScheduledExecutorService(10)     │
                          │  ┌────────────────────────────┐   │
                          │  │ SandboxSecurityManager     │   │
                          │  │ grant/revoke/hasPermission │   │
                          │  └────────────────────────────┘   │
                          │  ┌────────────────────────────┐   │
                          │  │ ResourceLimiter            │   │
                          │  │ threads/memory/exec count  │   │
                          │  └────────────────────────────┘   │
                          └──────────────────────────────────┘

                          ┌──────────────────────────────────┐
                          │      PluginEventBus               │
                          │  CopyOnWriteArrayList pub/sub     │
                          │  9 event types                    │
                          └──────────────────────────────────┘

                          ┌──────────────────────────────────┐
                          │      PluginLoader                 │
                          │  load/unload/reload               │
                          │  → PluginSandbox                  │
                          │  → SandboxSecurityManager         │
                          │  → VersionManager                 │
                          └──────────────────────────────────┘

                          ┌──────────────────────────────────┐
                          │    KnowledgePackIndexer           │
                          │    5 pack types                   │
                          │    FAQ, SOP, DOMAIN_DOCS,         │
                          │    TRAINING_MATERIAL, REFERENCE   │
                          └──────────────────────────────────┘

                          ┌──────────────────────────────────┐
                          │    PluginHealthMonitor            │
                          │    ScheduledExecutorService       │
                          │    periodic check every 60s       │
                          └──────────────────────────────────┘

                          ┌──────────────────────────────────┐
                          │  MarketplaceMetricsService        │
                          │  query latency, install/uninstall │
                          │  update/error counters            │
                          │  → Micrometer Prometheus          │
                          └──────────────────────────────────┘
```

---

## Data Flows

### Install Flow

```
1. Client → POST /api/v1/plugins/install { pluginId, source }
2. Controller → MarketplaceOrchestrator.installPlugin(pluginId, source)
3. Orchestrator creates PluginManifest and PluginMetadata
4. → PluginLifecycleManager.install(instance)
5.   → PluginValidator.validate(manifest) — validates all 16 fields
6.   → PluginRegistry.register(id, instance) — stores in ConcurrentHashMap
7.   → State → INSTALLED
8. → VersionManager.recordVersion() — records plugin version
9. → CapabilityRegistry.register() — registers each capability
10. → PermissionManager.approve() — auto-approves manifest permissions
11. → MarketplaceMetricsService.recordPluginInstall()
12. Response ← MarketplaceResponse<PluginResponse>
```

### Enable Flow

```
1. Client → POST /api/v1/plugins/enable/{pluginId}
2. Controller → MarketplaceOrchestrator.enablePlugin(pluginId)
3. → PluginLifecycleManager.enable(pluginId, config)
4.   → PluginRegistry.get(pluginId)
5.   → SporekartPlugin.onEnable(context) — activate plugin
6.   → State → ENABLED
7. Response ← MarketplaceResponse<PluginResponse>
```

### Disable Flow

```
1. Client → POST /api/v1/plugins/disable/{pluginId}
2. Controller → MarketplaceOrchestrator.disablePlugin(pluginId)
3. → PluginLifecycleManager.disable(pluginId)
4.   → PluginRegistry.get(pluginId)
5.   → SporekartPlugin.onDisable(context) — deactivate plugin
6.   → State → DISABLED
7. Response ← MarketplaceResponse<PluginResponse>
```

### Uninstall Flow

```
1. Client → DELETE /api/v1/plugins/uninstall/{pluginId}
2. Controller → MarketplaceOrchestrator.uninstallPlugin(pluginId)
3. → PluginLifecycleManager.uninstall(pluginId)
4.   → PluginRegistry.get(pluginId)
5.   → SporekartPlugin.onUninstall(context) — cleanup
6.   → State → UNINSTALLED
7.   → PluginRegistry.unregister(pluginId) — removes from map
8. → PermissionManager.revokeAll(pluginId) — removes all permissions
9. → CapabilityRegistry.unregisterAll(pluginId) — removes capabilities
10. → MarketplaceMetricsService.recordPluginUninstall()
11. Response ← MarketplaceResponse<Void>
```

### Execute Action Flow

```
1. Client → POST /api/v1/plugins/{pluginId}/execute { action, params }
2. Controller → MarketplaceOrchestrator.executePluginAction(pluginId, action, params)
3. → PluginLifecycleManager.executeAction(pluginId, action, params)
4.   → PluginRegistry.get(pluginId) — find instance
5.   → Filter: instance.isEnabled() — must be ENABLED state
6.   → SporekartPlugin.execute(action, params) — run plugin logic
7. Response ← MarketplaceResponse<Map<String, Object>>
```

### Health Check Flow

```
1. PluginHealthMonitor.start() — @PostConstruct
2. ScheduledExecutorService — runs every 60s (configurable via health.checkIntervalMs)
3. runHealthChecks() → registry.getAll().forEach(this::checkPlugin)
4.   → SporekartPlugin.healthCheck() — calls plugin health method
5.   → Records PluginHealthStatus (status, responseTime, consecutiveFailures)
6. Exposed via GET /api/v1/plugins/health
```

---

## Sandbox Isolation Model

The sandbox provides four dimensions of isolation:

```
                    ┌─────────────────────────────────┐
                    │         PluginSandbox            │
                    │                                  │
                    │  ┌───────────────────────────┐   │
                    │  │ SandboxContext per plugin  │   │
                    │  │ - maxExecutionTimeoutMs    │   │
                    │  │ - maxThreads               │   │
                    │  │ - state map                │   │
                    │  │ - timeout/error counters   │   │
                    │  └───────────────────────────┘   │
                    │                                  │
                    │  ScheduledExecutorService(10)    │
                    │  - submits Callable tasks        │
                    │  - Future.get(timeout, unit)     │
                    │  - TimeoutException → error      │
                    └─────────────────────────────────┘
```

| Dimension   | Mechanism                                    | Default |
|-------------|----------------------------------------------|---------|
| Execution   | `Future.get(timeout)` with hard timeout      | 30s     |
| Threads     | `ResourceLimiter` max threads per plugin     | 5       |
| Memory      | `MarketplaceConfig.SandboxConfig` max memory | 256MB   |
| Isolation   | Each plugin gets independent `SandboxContext` | —       |

---

## Configuration

The `MarketplaceConfig` class (bound to `sporekart.marketplace.*`) organizes settings into three sections:

```yaml
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
```
