# Role-Based Access Control (RBAC)

## Role Types

| Role          | Scope                        | Description                          |
|---------------|------------------------------|--------------------------------------|
| `CUSTOMER`    | Self-service                 | Browse, order, manage own profile    |
| `GROWER`      | Self + inventory             | Manage own products, fulfill orders  |
| `ADMIN`       | Platform-wide                | Moderate content, manage users       |
| `SUPER_ADMIN` | System-wide                  | All access including configuration   |
| `SUPPORT`     | Read + ticket management     | View users/orders, resolve tickets   |
| `OPERATIONS`  | Operational read/write       | Monitor platform, run ops tasks      |

## Permission Model

Roles are encoded as a `roles` claim in the JWT. The gateway's `AuthorizationFilter` inspects this claim per-route.

```json
{
  "sub": "user-uuid",
  "roles": ["CUSTOMER", "GROWER"],
  "iss": "sporekart-identity",
  "iat": 1696000000,
  "exp": 1696000900,
  "jti": "unique-token-id"
}
```

## Authorization Enforcement

- **Layer 1 – Gateway**: `AuthorizationFilter` matches request path to required role(s). Requests without sufficient roles receive HTTP 403.
- **Layer 2 – Service**: Individual services may enforce finer-grained permissions (e.g., a grower can only edit their own products).
- **Layer 3 – Data**: Row-level security enforced at the database layer for multi-tenant isolation.

## Route-Role Mapping (Gateway)

| Route Pattern              | Required Role       |
|----------------------------|---------------------|
| `POST /api/auth/**`        | Anonymous           |
| `GET /api/products/**`     | Anonymous           |
| `POST /api/orders`         | `CUSTOMER`          |
| `PUT /api/products/**`     | `GROWER`, `ADMIN`   |
| `GET /api/admin/**`        | `ADMIN`             |
| `GET /api/super/**`        | `SUPER_ADMIN`       |
| `GET /api/support/**`      | `SUPPORT`           |
| `POST /api/ops/**`         | `OPERATIONS`        |

## Multi-Role Support

A single user can hold multiple roles (e.g., `CUSTOMER` and `GROWER`). The `AuthorizationFilter` grants access if the user holds **any** of the required roles for the requested route.