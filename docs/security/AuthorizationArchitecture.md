# Authorization Architecture

**Version:** 2.0.0
**Last Updated:** 2026-07-23
**Module:** identity-service

---

## Overview

The authorization model combines **RBAC (Role-Based Access Control)** with **fine-grained permissions**. Roles are assigned to users, and each role maps to a set of permissions. Authorization checks are performed at the API gateway, controller, and service layers.

---

## Authorization Model

```
User ──has──> Roles ──grant──> Permissions
                     
                     ┌───────────────┐
         ┌──────────►│ PRODUCT_READ  │
         │           ├───────────────┤
         │           │ PRODUCT_WRITE │
         │           ├───────────────┤
         │           │ ORDER_READ    │
User ────┼──► Role ──┤ ORDER_WRITE   │
         │           ├───────────────┤
         │           │ USER_MANAGE   │
         │           ├───────────────┤
         │           │ ...           │
         │           └───────────────┘
```

---

## Role Hierarchy

```
SUPER_ADMIN ──> All permissions
     │
ADMIN ──> All permissions
     │
OPERATIONS ──> PRODUCT_READ, PRODUCT_WRITE, ORDER_READ, ORDER_WRITE, USER_MANAGE, AUDIT_VIEW
     │
SUPPORT ──> PRODUCT_READ, ORDER_READ, ORDER_WRITE, USER_MANAGE, AUDIT_VIEW
     │
GROWER ──> PRODUCT_READ, PRODUCT_WRITE, ORDER_READ, TRAINING_MANAGE
     │
CUSTOMER ──> PRODUCT_READ, ORDER_READ, ORDER_WRITE
```

Note: There is no hierarchical inheritance between roles. Each role has its own explicit permission set defined in `PermissionService.ROLE_PERMISSIONS`. Multiple roles can be assigned to a single user, with permissions being the union of all assigned roles.

---

## Permission Checking

### Service Layer

`PermissionService` provides three check methods:

```java
// Check if user has a specific permission
public boolean hasPermission(Set<RoleType> userRoles, String permission)

// Check if user has ANY of the listed permissions
public boolean hasAnyPermission(Set<RoleType> userRoles, String... permissions)

// Check if user has ALL of the listed permissions
public boolean hasAllPermissions(Set<RoleType> userRoles, String... permissions)
```

### Permission Resolution

Permissions are resolved dynamically from the role-permission map:

```java
public Set<String> getPermissionsForRoles(Set<RoleType> roles) {
    var permissions = new HashSet<String>();
    for (var role : roles) {
        var perms = ROLE_PERMISSIONS.get(role);
        if (perms != null) {
            perms.stream().map(Permission::name).forEach(permissions::add);
        }
    }
    return permissions;
}
```

---

## Endpoint Authorization

### Public Endpoints (No Auth Required)

```
GET  /actuator/health
GET  /actuator/health/**
GET  /actuator/info
GET  /actuator/prometheus
GET  /swagger-ui/**
GET  /v3/api-docs/**
POST /auth/register
POST /auth/login
POST /auth/refresh
POST /auth/otp/**
GET  /error
```

### Authenticated Endpoints (Any Authenticated User)

```
GET    /users/me
GET    /users/{id}
GET    /sessions/me
DELETE /sessions/me
DELETE /sessions/{sessionId}
POST   /auth/logout
POST   /devices/register
GET    /devices/me
POST   /devices/{id}/trust
DELETE /devices/{id}
POST   /workspaces
GET    /workspaces/me
GET    /workspaces/{workspaceId}
```

### Admin-Only Endpoints (ADMIN or SUPER_ADMIN)

```
GET  /admin/permissions
GET  /admin/audit/events
GET  /admin/audit/stats
```

Configuration in `SecurityConfig`:

```java
.authorizeHttpRequests(authorize -> authorize
    .requestMatchers("/auth/register", "/auth/login", "/auth/refresh", "/auth/otp/**")
    .permitAll()
    .requestMatchers("/admin/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
    .anyRequest().authenticated())
```

---

## JWT Authentication Filter Flow

The `JwtAuthenticationFilter` (inner class of `SecurityConfig`) processes every request:

1. Extract Bearer token from `Authorization` header
2. Call `JwtService.extractSubject(token)` — validates signature, expiry, blacklist
3. Extract roles from the `roles` claim
4. Build `SimpleGrantedAuthority` objects with `ROLE_` prefix
5. Set `UsernamePasswordAuthenticationToken` in `SecurityContextHolder`

```java
var roles = jwtService.extractRoles(token);
var authorities = roles.stream()
        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
        .collect(Collectors.toList());
var authToken = new UsernamePasswordAuthenticationToken(
        subject, null, authorities);
SecurityContextHolder.getContext().setAuthentication(authToken);
```

---

## Workspace Isolation

- Sessions carry a `workspaceContext` field identifying the active workspace
- Workspace membership is tracked in the `workspace_members` join table
- Data access is scoped by workspace for multi-tenant isolation
- Each workspace has a single `ownerId` for ownership enforcement

---

## Security Headers

All responses include these security headers:

| Header | Value |
|--------|-------|
| X-XSS-Protection | 1; mode=block |
| Content-Security-Policy | default-src 'self' |
| X-Frame-Options | DENY |
| Strict-Transport-Security | max-age=31536000; includeSubDomains |

---

## Configuration Reference

```yaml
app:
  security:
    jwt:
      secret: ${APP_JWT_SECRET}
      issuer: https://identity.sporekart.example
      expiration-minutes: 15
      refresh-expiration-minutes: 10080
      clock-skew-seconds: 30
      blacklist-size: 10000
    cors:
      allowed-origins: http://localhost:3000
```
