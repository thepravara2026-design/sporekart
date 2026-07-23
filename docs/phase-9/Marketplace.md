# Enterprise Copilot Marketplace — Platform Overview

## Purpose

The SporeKart Enterprise Copilot Marketplace is a first-party and third-party plugin platform that extends the SporeKart copilot ecosystem. It enables developers to build, register, and distribute plugins that provide capabilities — from AI-powered conversational agents and knowledge packs to workflow automation, analytics dashboards, and notification services.

Inspired by extensibility models from VS Code, GitHub Apps, and JetBrains plugins, the Marketplace provides a secure sandboxed runtime, a permission-based security model, lifecycle management, event-driven integration, and deep observability via Micrometer Prometheus metrics.

## Architecture Diagram (Description)

```
 Gateway (port 8110)
      |
   /api/v1/plugins
      |
 PluginMarketplaceController
      |
 MarketplaceOrchestrator
      |---- PluginRegistry (ConcurrentHashMap)
      |---- CapabilityRegistry (ConcurrentHashMap)
      |---- PluginLifecycleManager
      |---- VersionManager (platform 1.0.0)
      |---- PluginLoader
      |---- PermissionManager
      |---- PluginHealthMonitor
      |---- MarketplaceMetricsService
      |
      +---- PluginSandbox (ScheduledExecutorService)
      |       +---- SandboxSecurityManager
      |       +---- ResourceLimiter
      |
      +---- PluginEventBus (CopyOnWriteArrayList pub/sub)
      |
      +---- KnowledgePackIndexer
```

The gateway service (`RouteConfig`) maps `copilot-marketplace` to the route prefix `/api/v1/plugins`. The marketplace service listens on port `8110`.

## Plugin Model

Every plugin is defined by a **PluginManifest** (16 fields) and backed by a Java implementation of the `SporekartPlugin` interface. Plugins are stored in-memory in `PluginRegistry` (backed by `ConcurrentHashMap`) and their capabilities are tracked in `CapabilityRegistry`.

Each plugin instance (`PluginInstance`) carries:
- A `PluginManifest` (static definition)
- A `PluginMetadata` (runtime metadata — install time, source, custom attributes)
- A `PluginState` (current lifecycle state)
- A reference to the `SporekartPlugin` implementation

### 10 Plugin Types

| Enum Value      | Description                                                    |
|-----------------|----------------------------------------------------------------|
| `AI_COPILOT`    | Conversational AI agents and copilot extensions                |
| `KNOWLEDGE`     | Knowledge packs (FAQ, SOP, domain docs, training, reference)   |
| `WORKFLOW`      | Business process automation workflows                          |
| `ANALYTICS`     | Data analysis and visualization engines                        |
| `CONNECTOR`     | Third-party system connectors and integrations                 |
| `DASHBOARD`     | Custom dashboard panels and widgets                            |
| `AUTOMATION`    | Scheduled or event-driven automation tasks                     |
| `REPORTING`     | Report generation and distribution                             |
| `NOTIFICATION`  | Notification channels and delivery plugins                     |
| `ML`            | Machine learning model inference and serving                   |

## Extensibility Patterns

The Marketplace follows patterns proven by major extensibility ecosystems:

- **VS Code-like contribution points**: Plugins declare capabilities and hooks that the platform discovers at runtime via `CapabilityRegistry`.
- **GitHub Apps-style permissions**: Each plugin declares required permissions in its manifest. The `PermissionManager` gates all operations via `requestApproval`/`approve`/`revoke` workflows.
- **JetBrains-like sandbox isolation**: Plugins execute in isolated sandbox contexts with resource limits (memory, threads, execution timeout) enforced by `PluginSandbox` and `ResourceLimiter`.

## Plugin Lifecycle

Plugins transition through these states:

```
 INSTALLED → ENABLED → DISABLED → UNINSTALLED
   ↑           ↓
   +── ERROR ←─+
```

Full state machine (7 states): `INSTALLED`, `ENABLED`, `DISABLED`, `ERROR`, `UPGRADING`, `UNINSTALLING`, `UNINSTALLED`.

The `PluginLifecycleManager` orchestrates state transitions by invoking the corresponding lifecycle methods on the `SporekartPlugin` interface (`onInstall`, `onEnable`, `onDisable`, `onUninstall`, `onUpgrade`, `onDowngrade`).

## Sandbox Model

Plugins run inside a sandbox (`PluginSandbox`) that provides:
- **Execution isolation**: Each plugin gets a `SandboxContext` with its own state map and resource tracking
- **Timeout enforcement**: Plugins must complete execution within `maxExecutionTimeoutMs` (default 30s), configurable via `MarketplaceConfig.SandboxConfig`
- **Thread limits**: Maximum threads per plugin controlled by `maxThreadsPerPlugin` (default 5)
- **Resource accounting**: `ResourceLimiter` tracks thread count, execution count, and total execution time per plugin
- **Global limit**: Maximum installed plugins capped by `maxInstalledPlugins` (default 100)

## Security Model

Security is multi-layered:

1. **Transport security**: Spring Security with stateless session management and HTTP Basic authentication for `/api/v1/plugins/**`
2. **Permission-based access control**: 10 `PluginPermission` values covering read access to products, orders, inventory, customers, analytics, training, AI execution, knowledge access, notifications, and API calls
3. **Permission lifecycle**: `PermissionManager` supports `requestApproval` → `approve` → `revoke` workflows
4. **Execution validation**: `PermissionValidator` checks permissions before allowing plugin actions
5. **Sandbox isolation**: `SandboxSecurityManager` grants/revokes permissions per plugin and validates manifest permissions
6. **Security by default**: Plugins start with no permissions; each must be explicitly approved

## Event Model

The event system (`PluginEventBus`) uses a publish-subscribe pattern with `CopyOnWriteArrayList` for thread-safe listener management.

### 9 Event Types

| Event Type                | Trigger                                      |
|---------------------------|----------------------------------------------|
| `PLUGIN_INSTALLED`        | Plugin installed via lifecycle manager       |
| `PLUGIN_UPDATED`          | Plugin upgraded/downgraded                   |
| `PLUGIN_REMOVED`          | Plugin uninstalled                           |
| `PLUGIN_FAILED`           | Plugin execution or validation failure       |
| `PLUGIN_LOADED`           | Plugin loaded by PluginLoader                |
| `PLUGIN_HEALTH_CHANGED`   | Health status transition                     |
| `CAPABILITY_REGISTERED`   | New capability registered                    |
| `CAPABILITY_REMOVED`      | Capability unregistered                      |
| `CONFIGURATION_CHANGED`   | Plugin configuration updated                 |

Each event carries a UUID-based `eventId`, `EventType`, `pluginId`, `Instant` timestamp, and a `Map<String, Object>` data payload.

## Knowledge Pack Integration

The `KnowledgePackIndexer` manages five types of knowledge packs that plugins can provide:

| Knowledge Pack Type | Description                              |
|---------------------|------------------------------------------|
| `FAQ`               | Frequently asked questions               |
| `SOP`               | Standard operating procedures            |
| `DOMAIN_DOCS`       | Domain-specific documentation            |
| `TRAINING_MATERIAL` | Training content and learning resources  |
| `REFERENCE`         | Reference guides and API documentation   |

Knowledge packs are indexed via `CapabilityRegistry` under the `KNOWLEDGE` capability, making them discoverable through the marketplace capability API.

## Monitoring and Observability

The `MarketplaceMetricsService` tracks:
- **Query latency**: Total and average latency for marketplace operations
- **Install/uninstall/update/error counters**: Atomic counters for each operation type
- **Action counts**: Per-action-type invocation counters
- **Micrometer Prometheus integration**: Metrics exposed at `/actuator/prometheus` alongside health, info, and standard metrics endpoints
- **SpringDoc OpenAPI**: API documentation at `/swagger-ui.html` and OpenAPI spec at `/v3/api-docs`
