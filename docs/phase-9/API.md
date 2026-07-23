# API Reference

Base URL: `http://localhost:8110/api/v1/plugins`

Authentication: HTTP Basic (required for all endpoints except actuator)

Response Envelope: All endpoints return `MarketplaceResponse<T>`:

```json
{
  "success": true,
  "message": "Success",
  "data": { ... },
  "errorCode": null
}
```

On error:

```json
{
  "success": false,
  "message": "Plugin not found",
  "data": null,
  "errorCode": "NOT_FOUND"
}
```

---

## 1. List All Plugins

```
GET /api/v1/plugins
```

Returns a summary of all registered plugins with counts.

**Response Schema**: `MarketplaceResponse<PluginListResponse>`

```json
{
  "success": true,
  "data": {
    "total": 5,
    "installed": 5,
    "enabled": 3,
    "errored": 1,
    "plugins": [
      {
        "pluginId": "inventory-assistant",
        "name": "Inventory Assistant",
        "version": "1.0.0",
        "author": "SporeKart",
        "description": "Inventory forecasting plugin",
        "type": "AI_COPILOT",
        "state": "ENABLED",
        "healthStatus": "UNKNOWN"
      }
    ]
  }
}
```

**Fields**:
- `total`: All registered plugins
- `installed`: Count of plugins not in `UNINSTALLED` state
- `enabled`: Count of plugins in `ENABLED` state
- `errored`: Count of plugins in `ERROR` state
- `plugins`: Array of `PluginResponse` objects

---

## 2. Install Plugin

```
POST /api/v1/plugins/install
```

**Request Body**:

```json
{
  "pluginId": "analytics-plugin",
  "version": "1.0.0",
  "source": "marketplace"
}
```

| Field      | Type   | Required | Description                       |
|------------|--------|----------|-----------------------------------|
| `pluginId` | String | Yes      | Unique plugin identifier          |
| `version`  | String | No       | Plugin version (default 1.0.0)    |
| `source`   | String | No       | Install source (e.g., "marketplace", "local") |

**Response**: `MarketplaceResponse<PluginResponse>`

**Error Codes**:
- `VALIDATION_ERROR`: Manifest validation failed (400)
- `ALREADY_EXISTS`: Plugin already registered (409)

---

## 3. Enable Plugin

```
POST /api/v1/plugins/enable/{pluginId}
```

**Path Parameters**: `pluginId` - The plugin identifier

**Response**: `MarketplaceResponse<PluginResponse>`

**Error Codes**:
- `NOT_FOUND`: Plugin not registered (400)

---

## 4. Disable Plugin

```
POST /api/v1/plugins/disable/{pluginId}
```

**Path Parameters**: `pluginId` - The plugin identifier

**Response**: `MarketplaceResponse<PluginResponse>`

**Error Codes**:
- `NOT_FOUND`: Plugin not registered (400)

---

## 5. Update Plugin

```
POST /api/v1/plugins/update/{pluginId}
```

**Path Parameters**: `pluginId` - The plugin identifier

Updates the plugin version (bumps to 1.0.1 in the current implementation).

**Response**: `MarketplaceResponse<PluginResponse>`

**Error Codes**:
- `NOT_FOUND`: Plugin not registered (400)

---

## 6. Uninstall Plugin

```
DELETE /api/v1/plugins/uninstall/{pluginId}
```

**Path Parameters**: `pluginId` - The plugin identifier

Removes the plugin from the registry, revokes all permissions, and unregisters all capabilities.

**Response**: `MarketplaceResponse<Void>` (data is null)

**Error Codes**: None (idempotent)

---

## 7. Get Marketplace Catalog

```
GET /api/v1/plugins/marketplace
```

Returns a list of available (pre-defined) plugins for installation.

**Response**: `MarketplaceResponse<List<Map<String, Object>>>`

```json
{
  "success": true,
  "data": [
    {
      "id": "customer-copilot",
      "name": "Customer Copilot",
      "version": "1.0.0",
      "type": "AI_COPILOT"
    },
    {
      "id": "analytics-plugin",
      "name": "Analytics Plugin",
      "version": "1.0.0",
      "type": "ANALYTICS"
    },
    {
      "id": "knowledge-pack",
      "name": "Knowledge Pack",
      "version": "1.0.0",
      "type": "KNOWLEDGE"
    },
    {
      "id": "workflow-automation",
      "name": "Workflow Automation",
      "version": "1.0.0",
      "type": "WORKFLOW"
    },
    {
      "id": "reporting-plugin",
      "name": "Reporting Plugin",
      "version": "1.0.0",
      "type": "REPORTING"
    }
  ]
}
```

---

## 8. Get Plugin Health

```
GET /api/v1/plugins/health
```

Returns aggregate health summary for all plugins.

**Response**: `MarketplaceResponse<PluginHealthResponse>`

```json
{
  "success": true,
  "data": {
    "totalPlugins": 5,
    "healthy": 4,
    "unhealthy": 0,
    "unknown": 1,
    "statuses": [
      {
        "pluginId": "inventory-assistant",
        "status": "HEALTHY",
        "lastCheckedAt": "2026-07-23T10:30:00Z",
        "responseTimeMs": 45,
        "details": { "status": "HEALTHY", "pluginId": "inventory-assistant" },
        "consecutiveFailures": 0
      }
    ],
    "summary": {
      "total": 5,
      "healthy": 4,
      "unhealthy": 0,
      "unknown": 1,
      "averageResponseTimeMs": 32.5
    }
  }
}
```

---

## 9. List Capabilities

```
GET /api/v1/plugins/capabilities
```

Returns all registered capability registrations.

**Response**: `MarketplaceResponse<List<CapabilityRegistration>>`

```json
{
  "success": true,
  "data": [
    {
      "capabilityId": "inventory-assistant:INVENTORY",
      "capability": "INVENTORY",
      "pluginId": "inventory-assistant",
      "pluginName": "Inventory Assistant",
      "registeredAt": "2026-07-23T10:30:00Z",
      "enabled": true
    }
  ]
}
```

---

## 10. Get Plugin by ID

```
GET /api/v1/plugins/{pluginId}
```

Returns a single plugin's details.

**Response**: `MarketplaceResponse<PluginResponse>`

**Error Codes**:
- `NOT_FOUND`: Plugin not registered (400)

---

## 11. Execute Plugin Action

```
POST /api/v1/plugins/{pluginId}/execute
```

**Request Body**:

```json
{
  "action": "process",
  "params": {
    "key": "value",
    "threshold": 0.8
  }
}
```

| Field    | Type                 | Required | Description                  |
|----------|----------------------|----------|------------------------------|
| `action` | String               | No       | Action name (default: "default") |
| `params` | Map<String, Object>  | No       | Parameters for the action    |

**Response**: `MarketplaceResponse<Map<String, Object>>`

The response data structure depends on the plugin implementation.

**Error Codes**:
- `NOT_FOUND`: Plugin not found or not in ENABLED state (400)
- `TIMEOUT`: Plugin execution exceeded timeout (408)
- `PERMISSION_DENIED`: Missing required permissions (403)

---

## Error Codes Summary

| HTTP Status | Error Code          | Description                         |
|-------------|---------------------|-------------------------------------|
| 200         | —                   | Success                             |
| 400         | `NOT_FOUND`         | Plugin not registered               |
| 400         | `VALIDATION_ERROR`  | Manifest validation failed          |
| 400         | `ALREADY_EXISTS`    | Plugin already registered           |
| 403         | `PERMISSION_DENIED` | Missing required permissions        |
| 408         | `TIMEOUT`           | Plugin sandbox execution timed out  |
| 500         | `INTERNAL_ERROR`    | Unexpected server error             |

---

## Data Type Reference

### PluginResponse

```json
{
  "pluginId": "string",
  "name": "string",
  "version": "string",
  "author": "string",
  "description": "string",
  "type": "string (PluginType enum)",
  "state": "string (PluginState enum)",
  "healthStatus": "string"
}
```

### PluginListResponse

```json
{
  "total": "integer",
  "installed": "integer",
  "enabled": "integer",
  "errored": "integer",
  "plugins": ["PluginResponse"]
}
```

### PluginHealthResponse

```json
{
  "totalPlugins": "integer",
  "healthy": "integer",
  "unhealthy": "integer",
  "unknown": "integer",
  "statuses": ["PluginHealthStatus"],
  "summary": "object"
}
```

### PluginEventResponse

```json
{
  "eventId": "string (UUID)",
  "eventType": "string (EventType enum)",
  "pluginId": "string",
  "timestamp": "ISO-8601 datetime",
  "data": "object"
}
```

---

## Authentication Example

```bash
# With HTTP Basic authentication
curl -X GET http://localhost:8110/api/v1/plugins \
  -u admin:password

# With Authorization header
curl -X GET http://localhost:8110/api/v1/plugins \
  -H "Authorization: Basic YWRtaW46cGFzc3dvcmQ="
```

## API Documentation (Swagger)

The service exposes OpenAPI documentation via SpringDoc:

- **Swagger UI**: `http://localhost:8110/swagger-ui.html`
- **OpenAPI Spec**: `http://localhost:8110/v3/api-docs`
