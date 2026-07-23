# Capability Registry

The `CapabilityRegistry` is a thread-safe in-memory registry that tracks what capabilities each plugin provides. It is a core discovery mechanism that enables the marketplace to answer questions like "which plugins can handle inventory forecasting?" or "what capabilities does plugin X expose?"

---

## What Capabilities Are

Capabilities are defined by the `PluginCapability` enum (15 values at present):

```java
public enum PluginCapability {
    CONVERSATION,   // Conversational AI interactions
    KNOWLEDGE,      // Knowledge base and information retrieval
    SEARCH,         // Search functionality across datasets
    FORECAST,       // Predictive forecasting and projections
    MARKETING,      // Marketing campaign management
    ANALYTICS,      // Data analysis and visualization
    REPORTING,      // Report generation and distribution
    INVENTORY,      // Inventory management and tracking
    ORDERS,         // Order processing and management
    TRAINING,       // Training content and learning management
    NOTIFICATIONS,  // Notification delivery and channels
    AI_MODELS,      // AI model inference and serving
    DASHBOARD,      // Custom dashboard panels and widgets
    WORKFLOW,       // Business process automation workflows
    AUTOMATION      // Scheduled/event-driven automation tasks
}
```

Each capability represents a discrete unit of functionality that a plugin can provide to the platform. Multiple plugins can provide the same capability; the registry enables distribution analysis and conflict detection.

---

## Registration Flow

Capabilities are registered during plugin installation in `MarketplaceOrchestrator.installPlugin()`:

```java
// Register the primary capability
capabilityRegistry.register(pluginId, manifest.name(), PluginCapability.CONVERSATION);

// Register all other capabilities from the manifest
if (manifest.capabilities() != null) {
    manifest.capabilities().stream()
        .filter(c -> c != PluginCapability.CONVERSATION)
        .forEach(cap -> capabilityRegistry.register(pluginId, manifest.name(), cap));
}
```

The `CapabilityRegistry.register()` method creates a `CapabilityRegistration` record:

```java
public record CapabilityRegistration(
    String capabilityId,     // Composite key: pluginId + ":" + capability.name()
    PluginCapability capability,  // The capability enum value
    String pluginId,         // Owning plugin's ID
    String pluginName,       // Owning plugin's display name
    Instant registeredAt,    // Timestamp of registration
    boolean enabled          // Whether the capability is active
) {}
```

The `capabilityId` follows the pattern `{pluginId}:{CAPABILITY_NAME}` (e.g., `inventory-assistant:INVENTORY`), which guarantees uniqueness.

---

## Capability Distribution

The `getCapabilityDistribution()` method returns a map of `PluginCapability → Long` showing how many plugins provide each capability:

```java
public Map<PluginCapability, Long> getCapabilityDistribution() {
    var dist = new HashMap<PluginCapability, Long>();
    capabilities.values().forEach(c -> dist.merge(c.capability(), 1L, Long::sum));
    return dist;
}
```

This is useful for:
- Identifying capability gaps (capabilities with zero providers)
- Avoiding single-vendor lock-in (capabilities with only one provider)
- Marketplace analytics and reporting

---

## Lookup Operations

The registry provides several query methods:

### By Plugin

```java
// Get all capabilities registered by a specific plugin
List<CapabilityRegistration> getByPlugin(String pluginId);

// Check if a specific plugin has a specific capability
boolean hasCapability(String pluginId, PluginCapability capability);
```

### By Capability

```java
// Get all plugins that provide a specific capability
List<CapabilityRegistration> getByCapability(PluginCapability capability);
```

### By Capability ID

```java
// Get a single registration by its composite capabilityId
Optional<CapabilityRegistration> get(String capabilityId);
```

### All Registrations

```java
// Get every capability registration in the registry
List<CapabilityRegistration> getAll();

// Total count of registered capabilities
int count();
```

---

## Unregistration

When a plugin is uninstalled, all its capability registrations are removed:

```java
public void unregisterAll(String pluginId) {
    var toRemove = capabilities.values().stream()
        .filter(c -> c.pluginId().equals(pluginId))
        .map(CapabilityRegistration::capabilityId)
        .toList();
    toRemove.forEach(capabilities::remove);
}
```

This is called from `MarketplaceOrchestrator.uninstallPlugin()` and also from `KnowledgePackIndexer.removePluginPacks()`.

---

## Underlying Data Structure

The registry is backed by a `ConcurrentHashMap<String, CapabilityRegistration>`:

```java
@Component
public class CapabilityRegistry {
    private final ConcurrentHashMap<String, CapabilityRegistration> capabilities = new ConcurrentHashMap<>();
    // ...
}
```

This provides:
- Thread-safe concurrent access without explicit synchronization
- O(1) lookups by capabilityId
- Atomic register/unregister operations
- Lock-free reads for query operations

---

## Integration with Enterprise RAG (Knowledge Pack Indexer)

The `KnowledgePackIndexer` uses `CapabilityRegistry` to integrate knowledge packs into the platform:

```java
public void indexPack(String pluginId, String pluginName, KnowledgePack pack) {
    indexedPacks.put(pack.packId(), pack);
    capabilityRegistry.register(pluginId, pluginName, PluginCapability.KNOWLEDGE);
    log.info("Knowledge pack indexed: {} by plugin {}", pack.packId(), pluginId);
}
```

This means:
- **Every knowledge pack automatically registers** the `KNOWLEDGE` capability
- **Knowledge packs are discoverable** through the standard capability API
- **Enterprise RAG systems** can query `GET /api/v1/plugins/capabilities` to find all knowledge sources

### Knowledge Pack Types

The `KnowledgePackIndexer.KnowledgePackType` enum defines 5 knowledge pack categories:

| Type               | Use Case                                |
|--------------------|-----------------------------------------|
| `FAQ`              | Question-answer pairs for common queries |
| `SOP`              | Standard operating procedures           |
| `DOMAIN_DOCS`      | Domain-specific documentation           |
| `TRAINING_MATERIAL`| Tutorials, courses, learning content    |
| `REFERENCE`        | API references, technical guides        |

Each `KnowledgePack` record contains:

```java
public record KnowledgePack(
    String packId,
    String pluginId,
    String name,
    String description,
    KnowledgePackType type,
    List<String> documents,
    Instant indexedAt
) {}
```

---

## API Exposure

The capability registry is exposed via the `GET /api/v1/plugins/capabilities` endpoint in `PluginMarketplaceController`:

```java
@GetMapping("/capabilities")
public ResponseEntity<MarketplaceResponse<List<CapabilityRegistration>>> capabilities() {
    var caps = capabilityRegistry.getAll();
    return ResponseEntity.ok(MarketplaceResponse.success(caps));
}
```

Response format:

```json
{
  "success": true,
  "message": "Success",
  "data": [
    {
      "capabilityId": "inventory-assistant:INVENTORY",
      "capability": "INVENTORY",
      "pluginId": "inventory-assistant",
      "pluginName": "Inventory Assistant",
      "registeredAt": "2026-07-23T10:30:00Z",
      "enabled": true
    }
  ],
  "errorCode": null
}
```
