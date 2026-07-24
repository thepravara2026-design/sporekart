# Permission Model

**Version:** 2.0.0
**Last Updated:** 2026-07-23
**Module:** identity-service

---

## Permission Enum

All 10 permissions are defined in `Permission.java`:

```java
public enum Permission {
    PRODUCT_READ,
    PRODUCT_WRITE,
    ORDER_READ,
    ORDER_WRITE,
    USER_MANAGE,
    TRAINING_MANAGE,
    PAYMENT_MANAGE,
    ROLE_MANAGE,
    AUDIT_VIEW,
    SYSTEM_CONFIG
}
```

---

## Permission Descriptions

| Permission | Description | Typical Use |
|------------|-------------|-------------|
| `PRODUCT_READ` | View product catalog, listings, details | Browsing products, search results |
| `PRODUCT_WRITE` | Create, update, delete products | Grower product management, admin catalog |
| `ORDER_READ` | View order history and details | Order tracking, admin order view |
| `ORDER_WRITE` | Create, update, cancel orders | Placing orders, order fulfillment |
| `USER_MANAGE` | View, create, update, disable user accounts | Customer support, user administration |
| `TRAINING_MANAGE` | Manage training content and programs | Grower training, content management |
| `PAYMENT_MANAGE` | Manage payments, refunds, transactions | Finance operations, payment administration |
| `ROLE_MANAGE` | Assign and modify user roles | Role management, access control |
| `AUDIT_VIEW` | View audit logs and security events | Compliance, security monitoring |
| `SYSTEM_CONFIG` | Modify system-level configuration | Platform administration |

---

## How Permissions Are Checked in Code

### `PermissionService` API

```java
// Check single permission
boolean hasPermission(Set<RoleType> userRoles, String permission)

// Check if user has ANY of the listed permissions
boolean hasAnyPermission(Set<RoleType> userRoles, String... permissions)

// Check if user has ALL of the listed permissions
boolean hasAllPermissions(Set<RoleType> userRoles, String... permissions)

// Get all permissions for a set of roles (union)
Set<String> getPermissionsForRoles(Set<RoleType> roles)

// Get all defined permissions
Set<String> getAllPermissions()
```

### Check Pattern 1: Service Layer

```java
@Service
public class OrderService {
    private final PermissionService permissionService;

    public void cancelOrder(String userId, String orderId) {
        var user = userRepository.findById(userId);
        if (!permissionService.hasPermission(user.getRoles(), "ORDER_WRITE")) {
            throw new SecurityException("Insufficient permissions");
        }
        // proceed with cancellation
    }
}
```

### Check Pattern 2: Controller Layer

Permissions are embedded in the JWT at login time, and controllers check via `Authentication`:

```java
@GetMapping("/admin/audit/events")
public ResponseEntity<List<AuditEvent>> auditEvents(Authentication auth) {
    // auth.getAuthorities() contains ROLE_ADMIN / ROLE_SUPER_ADMIN
    // Already filtered by SecurityConfig route matching
}
```

### Check Pattern 3: Inline with Authentication Object

```java
Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
if (authentication == null || !authentication.isAuthenticated()) {
    return ResponseEntity.status(401).build();
}
```

---

## JWT Permission Embedding

Permissions are embedded in the JWT access token at login and refresh time:

```java
// IdentityService.loginWithSession()
var extraClaims = new HashMap<String, Object>();
extraClaims.put("permissions", List.copyOf(permissions));
var accessToken = jwtService.generateAccessToken(user.getId(), roles, extraClaims);
```

This allows API gateways and downstream services to check permissions without calling back to the identity service.

---

## Gateway Integration

### Route-Level Authorization (Spring Security)

```java
// SecurityConfig.java
.authorizeHttpRequests(authorize -> authorize
    .requestMatchers("/auth/register", "/auth/login", "/auth/refresh", "/auth/otp/**")
        .permitAll()
    .requestMatchers("/admin/**")
        .hasAnyRole("ADMIN", "SUPER_ADMIN")
    .anyRequest()
        .authenticated())
```

### Permission Mapping for Gateway

| Route Prefix | Required Role | Required Permission |
|-------------|---------------|-------------------|
| `/auth/**` | Public or Authenticated | None (auth itself) |
| `/users/**` | Any authenticated user | Varies by method |
| `/sessions/**` | Any authenticated user | None (own data) |
| `/devices/**` | Any authenticated user | None (own data) |
| `/workspaces/**` | Any authenticated user | None (own data) |
| `/admin/permissions` | ADMIN, SUPER_ADMIN | ROLE_MANAGE |
| `/admin/audit/events` | ADMIN, SUPER_ADMIN | AUDIT_VIEW |
| `/admin/audit/stats` | ADMIN, SUPER_ADMIN | AUDIT_VIEW |

---

## Adding a New Permission

1. Add to `Permission.java` enum
2. Add database entry via Flyway migration: `INSERT INTO permissions (permission) VALUES ('NEW_PERM');`
3. Update `PermissionService.ROLE_PERMISSIONS` static map to grant to appropriate roles
4. Use `permissionService.hasPermission(roles, "NEW_PERM")` in service code
5. Optionally add route-level checks in `SecurityConfig`
