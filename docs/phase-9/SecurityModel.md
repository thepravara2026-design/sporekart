# Security Model

The SporeKart Copilot Marketplace employs a multi-layered security architecture combining transport security, permission-based access control, sandbox isolation, and execution validation.

---

## Layer 1: Transport Security

`SecurityConfig` configures Spring Security for the marketplace service:

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/api/v1/plugins/**").authenticated()
                .anyRequest().permitAll()
            )
            .httpBasic(httpBasic -> {});
        return http.build();
    }
}
```

Key points:
- **Stateless sessions**: No server-side session state
- **Authenticated marketplace endpoints**: All `/api/v1/plugins/**` endpoints require HTTP Basic authentication
- **Actuator endpoints public**: Health, info, metrics, and Prometheus endpoints are accessible without authentication
- **CSRF disabled**: Stateless API architecture does not require CSRF protection

---

## Layer 2: Permission-Based Access Control

### 10 Plugin Permissions

`PluginPermission` defines the granular permissions that plugins can request:

| Permission            | Grants Access To                                      |
|-----------------------|-------------------------------------------------------|
| `READ_PRODUCTS`       | Read product catalog, pricing, and product data       |
| `READ_ORDERS`         | Read order history, status, and details               |
| `READ_INVENTORY`      | Read inventory levels, stock movements, and forecasts |
| `READ_CUSTOMERS`      | Read customer profiles, segments, and history         |
| `READ_ANALYTICS`      | Read analytics dashboards, reports, and metrics       |
| `READ_TRAINING`       | Read training materials, courses, and learning data   |
| `EXECUTE_AI`          | Execute AI model inference and predictions            |
| `ACCESS_KNOWLEDGE`    | Access knowledge base and documentation packs         |
| `SEND_NOTIFICATIONS`  | Send notifications through configured channels        |
| `CALL_APIS`           | Call external APIs and webhook endpoints              |

### Permission Declaration

Plugins declare required permissions in their `PluginManifest`:

```java
public record PluginManifest(
    List<PluginPermission> requiredPermissions,
) {}
```

During install, all declared permissions are automatically approved:

```java
manifest.requiredPermissions().forEach(p -> permissionManager.approve(pluginId, p));
```

---

## PermissionManager

The `PermissionManager` provides the full permission lifecycle:

### Request Approval

```java
public boolean requestApproval(String pluginId, PluginPermission permission)
```

Records a permission request and auto-approves it. In a production system, this would trigger an admin approval workflow.

### Approve

```java
public boolean approve(String pluginId, PluginPermission permission)
```

Grants a permission to a plugin. Stores in a `ConcurrentHashMap<String, Set<PluginPermission>>`.

### Revoke

```java
public boolean revoke(String pluginId, PluginPermission permission)
```

Revokes a specific permission from a plugin. Returns true if the permission was previously granted.

### Revoke All

```java
public void revokeAll(String pluginId)
```

Removes all permissions and permission config for a plugin. Called during uninstall.

### Check Permissions

```java
public boolean hasPermission(String pluginId, PluginPermission permission)
public boolean hasAllPermissions(String pluginId, List<PluginPermission> permissions)
```

### Audit

```java
public List<PluginPermission> getApprovedPermissions(String pluginId)
public Map<String, List<PluginPermission>> getAllApprovals()
```

Full visibility into all granted permissions across all plugins.

---

## PermissionValidator

The `PermissionValidator` provides execution-time permission enforcement:

```java
@Component
public class PermissionValidator {

    private final PermissionManager permissionManager;

    public boolean validateExecution(String pluginId, PluginPermission requiredPermission) {
        if (!permissionManager.hasPermission(pluginId, requiredPermission)) {
            return false;
        }
        return true;
    }

    public boolean validateBatch(String pluginId, List<PluginPermission> requiredPermissions) {
        var missing = requiredPermissions.stream()
            .filter(p -> !permissionManager.hasPermission(pluginId, p))
            .toList();
        if (!missing.isEmpty()) {
            return false;
        }
        return true;
    }

    public List<PluginPermission> getMissingPermissions(String pluginId, List<PluginPermission> required) {
        var granted = permissionManager.getApprovedPermissions(pluginId);
        return required.stream().filter(p -> !granted.contains(p)).toList();
    }
}
```

This validator should be consulted before executing any operation that requires specific permissions.

---

## Layer 3: Sandbox Isolation

### SandboxSecurityManager

The `SandboxSecurityManager` bridges the SDK permission system with the runtime sandbox:

```java
@Component
public class SandboxSecurityManager {

    private final Map<String, Set<PluginPermission>> grantedPermissions = new HashMap<>();

    public void grantPermissions(String pluginId, List<PluginPermission> permissions) {
        grantedPermissions.computeIfAbsent(pluginId, k -> new HashSet<>()).addAll(permissions);
    }

    public void revokePermissions(String pluginId) {
        grantedPermissions.remove(pluginId);
    }

    public boolean hasPermission(String pluginId, PluginPermission permission) {
        return grantedPermissions.getOrDefault(pluginId, Set.of()).contains(permission);
    }

    public boolean validateManifestPermissions(PluginManifest manifest) {
        if (manifest.requiredPermissions() == null) return false;
        return manifest.requiredPermissions().stream().allMatch(p -> p != null);
    }

    public List<PluginPermission> getGrantedPermissions(String pluginId) {
        return List.copyOf(grantedPermissions.getOrDefault(pluginId, Set.of()));
    }

    public Set<String> getPluginsWithPermission(PluginPermission permission) {
        var result = new HashSet<String>();
        grantedPermissions.forEach((id, perms) -> {
            if (perms.contains(permission)) result.add(id);
        });
        return result;
    }
}
```

Key methods:
- `grantPermissions`: Called by `PluginLoader.loadPlugin()` to grant all manifest-declared permissions
- `revokePermissions`: Called by `PluginLoader.unloadPlugin()` to clean up
- `validateManifestPermissions`: Ensures all entries in the manifest permissions list are non-null
- `getPluginsWithPermission`: Reverse-lookup to find all plugins granted a specific permission

### ResourceLimiter

The `ResourceLimiter` enforces resource constraints per plugin:

```java
@Component
public class ResourceLimiter {

    private final ConcurrentHashMap<String, PluginResourceUsage> usage = new ConcurrentHashMap<>();

    public boolean allocateResources(String pluginId) {
        var totalActive = usage.size();
        if (totalActive >= config.getMaxInstalledPlugins()) {
            return false;
        }
        usage.computeIfAbsent(pluginId, k -> new PluginResourceUsage());
        return true;
    }

    public void releaseResources(String pluginId) {
        usage.remove(pluginId);
    }

    public boolean isWithinLimits(String pluginId) {
        var res = usage.get(pluginId);
        if (res == null) return false;
        return res.threadCount.get() <= config.getSandbox().getMaxThreadsPerPlugin();
    }

    public static class PluginResourceUsage {
        private final AtomicInteger threadCount = new AtomicInteger(0);
        private final AtomicInteger executionCount = new AtomicInteger(0);
        private long totalExecutionTimeMs = 0;
        // incrementThreads, decrementThreads, getThreadCount
        // incrementExecutions, getExecutionCount
        // addExecutionTime, getTotalExecutionTimeMs
    }
}
```

### PluginSandbox

The `PluginSandbox` provides execution isolation via `ScheduledExecutorService`:

```java
@Component
public class PluginSandbox {

    private final ConcurrentHashMap<String, SandboxContext> activeSandboxes = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);

    public SandboxContext createSandbox(PluginInstance instance) { ... }
    public void destroySandbox(String pluginId) { ... }

    public Map<String, Object> executeInSandbox(String pluginId, Callable<Map<String, Object>> task) {
        var context = activeSandboxes.get(pluginId);
        if (context == null) {
            return Map.of("error", "No sandbox for plugin: " + pluginId);
        }
        try {
            var future = scheduler.submit(task);
            return future.get(config.getSandbox().getMaxExecutionTimeoutMs(), TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            context.incrementTimeoutCount();
            return Map.of("error", "Execution timed out", "pluginId", pluginId);
        } catch (Exception e) {
            context.incrementErrorCount();
            return Map.of("error", "Execution failed: " + e.getMessage(), "pluginId", pluginId);
        }
    }

    public static class SandboxContext {
        private final String pluginId;
        private final long maxExecutionTimeMs;
        private final int maxThreads;
        private final ConcurrentHashMap<String, Object> state = new ConcurrentHashMap<>();
        private int timeoutCount = 0;
        private int errorCount = 0;
        // Getters and state management methods
    }
}
```

---

## Layer 4: Execution Validation

When a plugin action is executed, the flow goes through:

```
PluginMarketplaceController
  -> MarketplaceOrchestrator.executePluginAction()
    -> PluginLifecycleManager.executeAction()
      -> PermissionValidator.validateExecution()  [not yet wired, but designed for]
      -> PluginRegistry plugin must be ENABLED
      -> PluginSandbox.executeInSandbox()
        -> SporekartPlugin.execute(action, params)
```

The sandbox enforces:
- **Execution timeout** (default 30s)
- **Error tracking** (consecutive failure count)
- **Resource limits** (thread count, execution count)

---

## Security by Default

The platform follows the principle of least privilege:

1. **No default permissions**: Plugins start with an empty permission set; all permissions must be explicitly declared and approved
2. **Sandboxed by default**: Every plugin runs in an isolated sandbox with resource limits
3. **No cross-plugin access**: Plugin sandboxes are independent; no shared state
4. **Audit trail**: `PermissionManager.getAllApprovals()` provides full visibility
5. **Revocation on uninstall**: All permissions and sandbox resources are cleaned up when a plugin is uninstalled
6. **Validation on install**: `PluginValidator` rejects malformed or incompatible manifests before registration
