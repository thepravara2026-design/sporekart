# RBAC — Role-Based Access Control

**Version:** 2.0.0
**Last Updated:** 2026-07-23
**Module:** identity-service

---

## Role Definitions

### Enumerated Roles (`RoleType.java`)

| Role | Constant | Intended Audience |
|------|----------|-------------------|
| `CUSTOMER` | `RoleType.CUSTOMER` | End consumers buying products |
| `GROWER` | `RoleType.GROWER` | Farmers/growers supplying products |
| `SUPPORT` | `RoleType.SUPPORT` | Customer support staff |
| `OPERATIONS` | `RoleType.OPERATIONS` | Operations team |
| `ADMIN` | `RoleType.ADMIN` | Platform administrators |
| `SUPER_ADMIN` | `RoleType.SUPER_ADMIN` | Super administrators |

### Java Definition

```java
public enum RoleType {
    CUSTOMER,
    GROWER,
    ADMIN,
    SUPER_ADMIN,
    SUPPORT,
    OPERATIONS
}
```

---

## Permission Mappings

Permissions are defined statically in `PermissionService.ROLE_PERMISSIONS`:

```java
private static final Map<RoleType, Set<Permission>> ROLE_PERMISSIONS = new ConcurrentHashMap<>();

static {
    ROLE_PERMISSIONS.put(RoleType.CUSTOMER, EnumSet.of(
            Permission.PRODUCT_READ, Permission.ORDER_READ, Permission.ORDER_WRITE));
    ROLE_PERMISSIONS.put(RoleType.GROWER, EnumSet.of(
            Permission.PRODUCT_READ, Permission.PRODUCT_WRITE,
            Permission.ORDER_READ, Permission.TRAINING_MANAGE));
    ROLE_PERMISSIONS.put(RoleType.SUPPORT, EnumSet.of(
            Permission.PRODUCT_READ, Permission.ORDER_READ, Permission.ORDER_WRITE,
            Permission.USER_MANAGE, Permission.AUDIT_VIEW));
    ROLE_PERMISSIONS.put(RoleType.OPERATIONS, EnumSet.of(
            Permission.PRODUCT_READ, Permission.PRODUCT_WRITE,
            Permission.ORDER_READ, Permission.ORDER_WRITE,
            Permission.USER_MANAGE, Permission.AUDIT_VIEW));
    ROLE_PERMISSIONS.put(RoleType.ADMIN, EnumSet.allOf(Permission.class));
    ROLE_PERMISSIONS.put(RoleType.SUPER_ADMIN, EnumSet.allOf(Permission.class));
}
```

---

## Role × Permission Matrix

| Permission | CUSTOMER | GROWER | SUPPORT | OPERATIONS | ADMIN | SUPER_ADMIN |
|------------|:--------:|:------:|:-------:|:----------:|:-----:|:-----------:|
| `PRODUCT_READ` | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
| `PRODUCT_WRITE` | ✗ | ✓ | ✗ | ✓ | ✓ | ✓ |
| `ORDER_READ` | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
| `ORDER_WRITE` | ✓ | ✗ | ✓ | ✓ | ✓ | ✓ |
| `USER_MANAGE` | ✗ | ✗ | ✓ | ✓ | ✓ | ✓ |
| `TRAINING_MANAGE` | ✗ | ✓ | ✗ | ✗ | ✓ | ✓ |
| `PAYMENT_MANAGE` | ✗ | ✗ | ✗ | ✗ | ✓ | ✓ |
| `ROLE_MANAGE` | ✗ | ✗ | ✗ | ✗ | ✓ | ✓ |
| `AUDIT_VIEW` | ✗ | ✗ | ✓ | ✓ | ✓ | ✓ |
| `SYSTEM_CONFIG` | ✗ | ✗ | ✗ | ✗ | ✓ | ✓ |

---

## How Permissions Are Checked

### 1. At Login

```java
// Permissions are embedded in the JWT access token
var permissions = user.getRoles().stream()
        .flatMap(r -> permissionService.getPermissionsForRoles(Set.of(r)).stream())
        .collect(Collectors.toSet());

var extraClaims = new HashMap<String, Object>();
extraClaims.put("permissions", List.copyOf(permissions));
var accessToken = jwtService.generateAccessToken(user.getId(), roles, extraClaims);
```

### 2. In Service Code

```java
// Service-level permission check
if (!permissionService.hasPermission(userRoles, "PAYMENT_MANAGE")) {
    throw new SecurityException("Access denied");
}
```

### 3. Via Security Config (Route-Level)

```java
.requestMatchers("/admin/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
```

---

## How to Add a New Role

1. **Add enum constant** in `RoleType.java`:
   ```java
   AUDITOR
   ```

2. **Add permission mapping** in `PermissionService` static block:
   ```java
   ROLE_PERMISSIONS.put(RoleType.AUDITOR, EnumSet.of(
           Permission.AUDIT_VIEW, Permission.ORDER_READ, Permission.PRODUCT_READ));
   ```

3. **Create role in database** via Flyway migration:
   ```sql
   INSERT INTO roles (role_type, name) VALUES ('AUDITOR', 'Auditor');
   ```

4. **Add endpoint rules** in `SecurityConfig` if needed:
   ```java
   .requestMatchers("/audit/**").hasRole("AUDITOR")
   ```

---

## How to Add a New Permission

1. **Add enum constant** in `Permission.java`:
   ```java
   REPORT_GENERATE
   ```

2. **Grant to roles** in `PermissionService` static block:
   ```java
   ROLE_PERMISSIONS.put(RoleType.OPERATIONS, EnumSet.of(
           ..., Permission.REPORT_GENERATE));
   ```

3. **Insert into database** via Flyway migration:
   ```sql
   INSERT INTO permissions (permission) VALUES ('REPORT_GENERATE');
   ```

4. **Add permission check** at service layer where needed:
   ```java
   permissionService.hasPermission(userRoles, "REPORT_GENERATE")
   ```

---

## Database Tables

### roles
```sql
CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    role_type VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL
);
```

### permissions
```sql
CREATE TABLE permissions (
    permission VARCHAR(80) PRIMARY KEY
);
```

### user_roles (join)
```sql
CREATE TABLE user_roles (
    user_id VARCHAR(36) NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id)
);
```

### role_permissions (join)
```sql
CREATE TABLE role_permissions (
    role_id BIGINT NOT NULL,
    permission VARCHAR(80) NOT NULL,
    PRIMARY KEY (role_id, permission)
);
```
