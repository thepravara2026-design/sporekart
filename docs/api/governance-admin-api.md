# Governance Admin API Reference

Base path: `/api/v1/admin`

## Endpoints (11)

### 1. Get Configuration
```
GET /api/v1/admin/config/{key}
```

**Query parameters:** `module`, `environment`

**Response 200:**
```json
{
  "key": "policy.max-evaluation-timeout",
  "module": "POLICY_ENGINE",
  "environment": "production",
  "value": "5000",
  "type": "NUMBER",
  "status": "ACTIVE",
  "version": 3,
  "createdAt": "2026-07-12T10:00:00Z",
  "updatedAt": "2026-07-12T12:00:00Z"
}
```

### 2. List Configurations
```
GET /api/v1/admin/config
```

**Query parameters:** `module`, `environment`, `type`, `status`, `page`, `size`

**Response 200:**
```json
{
  "content": [
    {
      "key": "policy.max-evaluation-timeout",
      "module": "POLICY_ENGINE",
      "value": "5000"
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 1
}
```

### 3. Create Configuration
```
POST /api/v1/admin/config
```

**Request:**
```json
{
  "key": "policy.max-evaluation-timeout",
  "module": "POLICY_ENGINE",
  "environment": "production",
  "value": "5000",
  "type": "NUMBER",
  "description": "Maximum evaluation timeout in milliseconds"
}
```

**Response 201:** Configuration record with version=1

### 4. Update Configuration
```
PUT /api/v1/admin/config/{key}
```

**Request:**
```json
{
  "module": "POLICY_ENGINE",
  "environment": "production",
  "value": "10000"
}
```

**Response 200:** Configuration record with incremented version

### 5. Delete Configuration
```
DELETE /api/v1/admin/config/{key}
```

**Query parameters:** `module`, `environment`

**Response 204:** No content

### 6. Get Feature Flag
```
GET /api/v1/admin/features/{flagName}
```

**Query parameters:** `module`, `environment`

**Response 200:**
```json
{
  "flagName": "policy-evaluation-enabled",
  "enabled": true,
  "scope": "MODULE",
  "module": "POLICY_ENGINE",
  "environment": null
}
```

### 7. Toggle Feature Flag
```
PUT /api/v1/admin/features/{flagName}
```

**Request:**
```json
{
  "enabled": false,
  "scope": "MODULE",
  "module": "POLICY_ENGINE",
  "environment": "production"
}
```

**Response 200:** Updated feature flag with audit metadata

### 8. List Modules
```
GET /api/v1/admin/modules
```

**Response 200:**
```json
{
  "modules": [
    {
      "name": "POLICY_ENGINE",
      "displayName": "Policy Engine",
      "enabled": true,
      "health": "UP",
      "version": "3.3.0"
    }
  ]
}
```

### 9. Toggle Module
```
PUT /api/v1/admin/modules/{moduleName}
```

**Request:**
```json
{
  "enabled": false
}
```

**Response 200:** Updated module status with audit metadata

### 10. Export Configurations
```
POST /api/v1/admin/config/export
```

**Query parameters:** `module`, `environment`

**Response 200:**
```json
{
  "exportedAt": "2026-07-12T12:00:00Z",
  "module": "POLICY_ENGINE",
  "environment": "production",
  "configurations": []
}
```

### 11. Import Configurations
```
POST /api/v1/admin/config/import
```

**Query parameters:** `dryRun` (boolean)

**Request:**
```json
{
  "configurations": [
    {
      "key": "policy.max-evaluation-timeout",
      "module": "POLICY_ENGINE",
      "environment": "production",
      "value": "5000",
      "type": "NUMBER"
    }
  ]
}
```

**Response 200 (dry-run=true):**
```json
{
  "dryRun": true,
  "valid": true,
  "entriesToCreate": 1,
  "entriesToUpdate": 0,
  "validationErrors": []
}
```

**Response 200 (dry-run=false):**
```json
{
  "dryRun": false,
  "imported": 1,
  "updated": 0,
  "failed": 0,
  "errors": []
}
```

## Error Format (RFC 9457)
```json
{
  "type": "urn:sporekart:admin:error:ADM_404",
  "title": "Configuration Not Found",
  "status": 404,
  "detail": "Configuration 'policy.max-evaluation-timeout' not found for module POLICY_ENGINE in environment production"
}
```

## Admin Error Codes
| Code | HTTP Status | Description |
|------|-------------|-------------|
| ADM_400 | 400 | Validation error |
| ADM_401 | 401 | Unauthorized |
| ADM_403 | 403 | Insufficient permissions |
| ADM_404 | 404 | Configuration not found |
| ADM_409 | 409 | Configuration conflict |
| ADM_422 | 422 | Unprocessable entity |
| ADM_500 | 500 | Internal error |
