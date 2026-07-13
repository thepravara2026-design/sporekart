# Governance Administration Runbook

## Configuration Management

### View Configuration
```
GET /api/v1/admin/config/{key}?module={module}&environment={environment}
```

### Create Configuration
```
POST /api/v1/admin/config
{
  "key": "policy.max-evaluation-timeout",
  "module": "POLICY_ENGINE",
  "environment": "production",
  "value": "5000",
  "type": "NUMBER"
}
```

### Update Configuration
```
PUT /api/v1/admin/config/{key}
{
  "module": "POLICY_ENGINE",
  "environment": "production",
  "value": "10000"
}
```

### Delete Configuration
```
DELETE /api/v1/admin/config/{key}?module={module}&environment={environment}
```

### Import Configurations
```
POST /api/v1/admin/config/import?dryRun=true
# Review validation report, then:
POST /api/v1/admin/config/import?dryRun=false
```

### Export Configurations
```
POST /api/v1/admin/config/export?module={module}&environment={environment}
```

## Feature Flag Management

### View Feature Flags
```
GET /api/v1/admin/features/{flagName}?module={module}&environment={environment}
```

### Toggle Feature Flag
```
PUT /api/v1/admin/features/{flagName}
{
  "enabled": true,
  "scope": "MODULE",
  "module": "POLICY_ENGINE"
}
```

### Common Operations
- **Enable a module:** Set module-level feature flag `{module}-enabled` to true
- **Disable a module:** Set module-level feature flag `{module}-enabled` to false
- **Production override:** Create environment-scoped flag with `environment: "production"`
- **Global rollout:** Use GLOBAL scope flag — affects all environments

## Module Management

### View Module Status
```
GET /api/v1/admin/modules
```

### Enable/Disable Module
```
PUT /api/v1/admin/modules/POLICY_ENGINE
{
  "enabled": false
}
```

### Module Dependency Considerations
- Disabling Policy Engine also disables all policy evaluation
- Disabling a module causes its REST endpoints to return 503
- Verify dependent modules before disabling

## Rollback Procedures

### Configuration Rollback
1. List configuration version history:
   ```
   GET /api/v1/admin/config/{key}/versions?module={module}&environment={environment}
   ```
2. Identify target version number
3. Execute rollback:
   ```
   POST /api/v1/admin/config/{key}/rollback?module={module}&environment={environment}&version={versionNumber}
   ```
4. Verify rolled-back configuration:
   ```
   GET /api/v1/admin/config/{key}?module={module}&environment={environment}
   ```
5. Check module health after rollback

### Snapshot Restore
1. List available snapshots:
   ```
   GET /api/v1/admin/snapshots
   ```
2. Execute restore:
   ```
   POST /api/v1/admin/snapshots/{snapshotId}/restore
   ```
3. Verify restored configurations
4. Check all governance module health

## Maintenance Mode

### Enable Maintenance Mode
```
PUT /api/v1/admin/maintenance
{
  "enabled": true,
  "reason": "Scheduled database migration",
  "expectedDuration": "PT30M"
}
```

### Disable Maintenance Mode
```
PUT /api/v1/admin/maintenance
{
  "enabled": false,
  "reason": "Migration completed"
}
```

### Behavior During Maintenance
- All non-admin API requests return 503 Service Unavailable
- Non-critical background jobs are paused
- Health endpoints return MAINTENANCE status
- Admin users can continue normal operations

## Environment Management

### Create Environment Profile
```
POST /api/v1/admin/environments
{
  "envName": "sandbox",
  "displayName": "Sandbox",
  "description": "Testing sandbox environment"
}
```

### Activate/Deactivate Environment
```
PUT /api/v1/admin/environments/{envName}
{
  "isActive": false
}
```

## Troubleshooting

### Configuration Not Found
- Verify the exact key, module, and environment combination
- Check if configuration was deleted (soft delete)
- Verify environment profile exists and is active
- Check Redis cache — may need invalidation

### Feature Flag Not Applied
- Verify scope resolution order (module > environment > global)
- Clear Redis cache: Delete keys under `admin:features:*`
- Confirm user has FEATURE_ADMIN role
- Check audit log for recent toggles

### Module Not Responding
- Verify module is enabled: `GET /api/v1/admin/modules`
- Check module health endpoint
- Review admin audit log for recent configuration changes
- Check if maintenance mode is active

### Rollback Failed
- Verify target version exists and is not corrupted
- Check for conflicting configurations
- Review validation errors in rollback response
- Perform dry-run before actual rollback

### Import Failed
- Review validation errors in dry-run report
- Check for duplicate configuration keys
- Verify value types match expected types
- Check user has required roles (CONFIG_ADMIN + AUDITOR)

### Audit Log Issues
- Verify user has AUDITOR role
- Check date range filters
- Audit records are immutable — cannot be modified or deleted
- For long-term queries, export audit data

## Alerting Thresholds

| Metric | Warning | Critical |
|--------|---------|----------|
| Configuration change frequency | >100 changes/hour | >500 changes/hour |
| Failed import operations | 1 | 3+ |
| Maintenance mode duration | >2 hours | >8 hours |
| Disabled module duration | >24 hours | >72 hours |
| Audit write failures | 1 | 3+ |
