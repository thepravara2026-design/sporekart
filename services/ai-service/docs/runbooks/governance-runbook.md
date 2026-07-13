# Governance Module Operational Runbook

## Health Checks

### Endpoint

`GET /api/v1/governance/health`

**Response (200 OK):**
```json
{
  "status": "UP",
  "components": {
    "governance-db": { "status": "UP", "details": { "database": "PostgreSQL", "latency": "2ms" } },
    "governance-redis": { "status": "UP", "details": { "cacheHitRate": 0.87 } },
    "governance-kafka": { "status": "UP", "details": { "topic": "governance-events", "partitions": 3 } },
    "governance-policies": { "status": "UP", "details": { "activePolicies": 12 } },
    "governance-audit": { "status": "UP", "details": { "totalRecords": 15420 } }
  }
}
```

### Health Indicators

| Component | Check | Frequency | Action on Failure |
|-----------|-------|-----------|-------------------|
| Database | Connection pool test | Every health check | Restart service, check DB connectivity |
| Redis | Ping + cache hit rate | Every health check | Restart Redis, verify connectivity |
| Kafka | Topic metadata query | Every 30s | Restart Kafka, check broker status |
| Policy Store | Query active policy count | Every 60s | Verify DB is operational |
| Audit Store | Query recent record count | Every 60s | Verify write throughput |

### Custom Health Indicator

```bash
# Health check via curl
curl -s http://localhost:8080/api/v1/governance/health | jq .

# Expected healthy output
{
  "status": "UP",
  "components": {
    "governance-db": { "status": "UP" },
    "governance-redis": { "status": "UP" },
    "governance-kafka": { "status": "UP" }
  }
}
```

## Configuration Reload

### Hot Reload Mechanism

The governance module supports hot-reload of configuration without service restart:

**Method 1: API Trigger**
```bash
# Trigger configuration reload
curl -X POST http://localhost:8080/api/v1/governance/config/reload \
  -H "Authorization: Bearer <admin-token>"
```

**Method 2: Spring Cloud Config Bus (if available)**
```bash
# Refresh configuration via Spring Cloud Bus
curl -X POST http://localhost:8080/actuator/busrefresh
```

**Method 3: Application Properties Refresh**
```bash
# Refresh specific governance properties
curl -X POST http://localhost:8080/actuator/refresh \
  -H "Content-Type: application/json" \
  -d '["governance.cache.ttl.policies", "governance.cache.ttl.config"]'
```

### Reload Sequence

```
1. Receive reload trigger (API or Bus)
2. Reload `GovernanceConfig` @ConfigurationProperties bean
3. Invalidate all governance Redis cache entries
4. Re-apply active policy evaluation rules
5. Log reload event in audit trail
6. Publish ConfigReloaded event to Kafka
7. Return reload result summary
```

## Cache Invalidation

### Manual Cache Invalidation

```bash
# Invalidate all governance cache
curl -X POST http://localhost:8080/api/v1/governance/cache/clear

# Invalidate specific namespace
curl -X POST http://localhost:8080/api/v1/governance/cache/clear?namespace=gov:policies

# Invalidate cache for specific policy
curl -X POST http://localhost:8080/api/v1/governance/cache/clear?key=gov:policies:{policy-id}
```

### Automatic Cache Invalidation

Cache is automatically invalidated on:
- Policy create, update, activate, deactivate, archive
- Config entry create, update, delete
- Permission assignment create, update, delete
- Role assignment changes

### Cache Namespace Reference

| Namespace | Keys | TTL | Invalidation Triggers |
|-----------|------|-----|----------------------|
| `gov:policies` | `gov:policies:{id}` | 30 min | Policy CRUD, status change |
| `gov:config` | `gov:config:{scope}:{module}:{key}` | 60 min | Config entry CRUD |
| `gov:permissions` | `gov:permissions:{role}` | 15 min | Permission/role assignment |
| `gov:quotas` | `gov:quotas:{module}:{userId}:{period}` | 5 min | Quota increment/reset |
| `gov:audit:recent` | `gov:audit:recent:{module}` | 10 min | New audit record |

## Monitoring

### Key Metrics (Micrometer)

| Metric Name | Type | Description | Alert Threshold |
|-------------|------|-------------|-----------------|
| `governance.policy.evaluations` | Counter | Total policy evaluations | N/A (monitor rate) |
| `governance.policy.violations` | Counter | Total policy violations | > 100 in 5 min |
| `governance.audit.records.created` | Counter | Audit records per minute | > 1000 in 1 min |
| `governance.config.changes` | Counter | Config changes per hour | > 50 in 1 hour |
| `governance.rate.limits.exceeded` | Counter | Rate limit violations | > 10 in 1 min |
| `governance.quota.exceeded` | Counter | Quota exceeded events | > 5 in 5 min |
| `governance.active.policies` | Gauge | Current active policies | < 1 or > 100 |
| `governance.quota.utilization` | Gauge | Overall quota usage % | > 80% |
| `governance.cache.hit.ratio` | Gauge | Cache hit rate | < 0.5 |
| `governance.compliance.status` | Gauge | 0=pass, 1=warn, 2=fail | > 0 (warn), > 1 (critical) |

### Grafana Dashboard Panels

1. **Policy Health** — Active policies by type (pie chart)
2. **Policy Activity** — Policy creation/activation/deactivation events (time series)
3. **Policy Violations** — Violations by severity (stacked bar)
4. **Audit Volume** — Audit records created per minute (time series)
5. **Audit Breakdown** — Audit records by event type (pie chart)
6. **Cache Performance** — Hit ratio by namespace (time series)
7. **Rate Limiting** — Rate limit exceeded events (time series)
8. **Quota Utilization** — Usage by module/operation (heatmap)
9. **Configuration Changes** — Config changes per hour (time series)
10. **Compliance Status** — Overall compliance score (gauge)

### Prometheus Scrape Config

```yaml
scrape_configs:
  - job_name: 'governance-module'
    metrics_path: '/actuator/prometheus'
    scrape_interval: 15s
    static_configs:
      - targets: ['localhost:8080']
```

## Alerting

### Alert Rules

| Alert Name | Condition | Severity | Response |
|------------|-----------|----------|----------|
| HighPolicyViolations | `governance.policy.violations > 100 in 5m` | CRITICAL | Investigate root cause, check for misconfigured policies |
| RateLimitSpike | `governance.rate.limits.exceeded > 10 in 1m` | WARNING | Check for abusive traffic patterns |
| QuotaExhaustion | `governance.quota.exceeded > 5 in 5m` | WARNING | Verify quota limits, contact affected users |
| CacheHitRateDrop | `governance.cache.hit.ratio < 0.5` | WARNING | Check Redis connectivity, verify cache TTL config |
| AuditWriteFailure | `governance.audit.records.created == 0 for 5m` | CRITICAL | Check DB connectivity, disk space, write throughput |
| LowActivePolicies | `governance.active.policies < 1` | WARNING | Verify policy store, check for accidental deactivation |
| HighActivePolicies | `governance.active.policies > 100` | INFO | Review policy count, consider consolidation |
| ConfigChangeBurst | `governance.config.changes > 50 in 1h` | WARNING | Verify changes are authorized, review audit trail |
| ComplianceFailure | `governance.compliance.status > 0` | CRITICAL | Immediate investigation, possible security incident |

### Prometheus Alert Rules Example

```yaml
groups:
  - name: governance-alerts
    rules:
      - alert: HighPolicyViolations
        expr: rate(governance_policy_violations_total[5m]) > 100
        for: 1m
        labels:
          severity: critical
        annotations:
          summary: "High policy violation rate"
          description: "Policy violation rate is {{ $value }} per second over 5 minutes"

      - alert: CacheHitRateDrop
        expr: governance_cache_hit_ratio < 0.5
        for: 5m
        labels:
          severity: warning
        annotations:
          summary: "Governance cache hit rate below threshold"
          description: "Cache hit ratio is {{ $value }} (threshold: 0.5)"
```

### Notification Channels

| Severity | Channel | Recipients |
|----------|---------|------------|
| CRITICAL | PagerDuty + Slack | On-call engineer + Platform team |
| WARNING | Slack only | Platform team |
| INFO | Slack (optional) | Platform lead |

## Troubleshooting

### Common Issues

#### Issue 1: Policy not being enforced

**Symptoms:** Policy violations not being recorded, requests passing despite active blocking policies

**Checks:**
```bash
# 1. Verify policy is ACTIVE
curl -s http://localhost:8080/api/v1/governance/policies/{id} | jq .status

# 2. Check policy scope matches request module
curl -s http://localhost:8080/api/v1/governance/policies/{id} | jq .scope

# 3. Verify governance security filter is in the filter chain
curl -s http://localhost:8080/actuator/beans | grep GovernanceSecurityManager

# 4. Check audit trail for enforcement logs
curl -s "http://localhost:8080/api/v1/governance/audit?eventType=POLICY_EVALUATION&page=0&size=5"
```

**Resolution:**
- Activate the policy if in DRAFT/INACTIVE status
- Ensure the security filter chain includes `GovernanceSecurityManager`
- Check module name matches exactly (case-sensitive)

#### Issue 2: Audit records not being created

**Symptoms:** Empty audit query results, missing governance events

**Checks:**
```bash
# 1. Verify DB connectivity
curl -s http://localhost:8080/api/v1/governance/health | jq .components.governance-db

# 2. Check disk space
df -h

# 3. Check audit table row count
psql -c "SELECT count(*) FROM governance_audit_records;"

# 4. Check for DB errors in logs
grep -i "governance_audit" /var/log/app/ai-service.log
```

**Resolution:**
- Restart service if DB connection pool is exhausted
- Free up disk space if DB volume is full
- Check for table corruption: `VACUUM ANALYZE governance_audit_records;`

#### Issue 3: Cache performance degradation

**Symptoms:** High latency on governance operations, increased cache miss rate

**Checks:**
```bash
# 1. Check cache hit ratio
curl -s http://localhost:8080/actuator/metrics/governance.cache.hit.ratio

# 2. Check Redis memory usage
redis-cli INFO memory | grep used_memory_human

# 3. Check Redis slow log
redis-cli SLOWLOG GET 10

# 4. Verify TTL configurations
curl -s http://localhost:8080/actuator/env/governance.cache.ttl | jq .
```

**Resolution:**
- Increase Redis maxmemory if approaching limit
- Adjust TTLs for frequently evicted namespaces
- Restart Redis if performance is degraded

#### Issue 4: Rate limiting false positives

**Symptoms:** Users incorrectly getting 429 responses

**Checks:**
```bash
# 1. Check rate limit policy configuration
curl -s http://localhost:8080/api/v1/governance/policies?type=RATE_LIMIT&status=ACTIVE | jq .

# 2. Check user quota usage
curl -s "http://localhost:8080/api/v1/governance/quotas?userId=affected-user&operation=AI_CHAT" | jq .

# 3. Check if quota counters need reset
curl -s http://localhost:8080/api/v1/governance/quotas/reset -X POST \
  -H "Content-Type: application/json" \
  -d '{"userId": "affected-user", "operation": "AI_CHAT", "period": "DAILY"}'
```

**Resolution:**
- Adjust rate limit thresholds if too restrictive
- Reset stale quota counters
- Check for clock skew between services affecting window alignment

## Backup and Recovery

### Audit Table Archival

```bash
# Archive audit records older than 90 days (run monthly)
psql -c "
  INSERT INTO governance_audit_records_archive
  SELECT * FROM governance_audit_records
  WHERE timestamp < NOW() - INTERVAL '90 days';
  
  DELETE FROM governance_audit_records
  WHERE timestamp < NOW() - INTERVAL '90 days';
"
```

### Configuration Export (Disaster Recovery)

```bash
# Export all governance configuration
curl -s http://localhost:8080/api/v1/governance/config/export?format=JSON \
  -H "Authorization: Bearer <admin-token>" \
  -o governance-config-backup-$(date +%Y%m%d).json
```

### Policy Export

```bash
# Export all active policies
curl -s http://localhost:8080/api/v1/governance/policies?status=ACTIVE&size=1000 \
  -H "Authorization: Bearer <admin-token>" \
  -o governance-policies-backup-$(date +%Y%m%d).json
```
