# Governance Platform — Production Runbook

## 1. Startup Sequence

### Step 1: Infrastructure Dependencies
```bash
# Verify PostgreSQL is available
pg_isready -h postgres-primary -p 5432

# Verify Redis is available
redis-cli -h redis-cluster -p 6379 PING

# Verify Kafka is available
kafka-topics.sh --bootstrap-server kafka:9092 --list

# Verify all 9 governance topics exist
for topic in governance-events policy-events decision-events approval-events compliance-events risk-events analytics-events admin-events automation-events; do
    kafka-topics.sh --describe --topic "$topic" --bootstrap-server kafka:9092
done
```

### Step 2: Database Migrations
```bash
# Verify Flyway migrations are applied
java -jar governance-platform.jar --spring.flyway.enabled=false

# Or check via API
curl http://localhost:8080/actuator/flyway
```

### Step 3: Application Startup
```bash
# Start application instances (repeat for each instance)
java -jar governance-platform.jar \
  -Dspring.profiles.active=production \
  -Dserver.port=8080

# Verify startup
curl http://localhost:8080/actuator/health
```

### Step 4: Verify Module Health
```bash
# Check all 9 governance module health endpoints
for module in governance policies decisions approvals compliance risk admin automation; do
    curl -s "http://localhost:8080/api/v1/${module}/health" | jq .
done

# Check lifecycle endpoint
curl -s "http://localhost:8080/api/v1/governance/lifecycle/health" | jq .
```

## 2. Health Check Endpoints

| Module | Health Check | Expected Response |
|--------|-------------|-------------------|
| Governance Foundation | `GET /api/v1/governance/health` | `{"status":"UP","components":{"db":"UP","redis":"UP","kafka":"UP"}}` |
| Policy Engine | `GET /api/v1/policies/health` | `{"status":"UP","components":{"db":"UP","redis":"UP"}}` |
| Decision Engine | `GET /api/v1/decisions/health` | `{"status":"UP","components":{"db":"UP","redis":"UP"}}` |
| Approval Platform | `GET /api/v1/approvals/health` | `{"status":"UP","components":{"db":"UP","redis":"UP"}}` |
| Compliance Framework | `GET /api/v1/compliance/health` | `{"status":"UP","components":{"db":"UP","redis":"UP"}}` |
| Risk & Trust | `GET /api/v1/risk/health` | `{"status":"UP","components":{"db":"UP","redis":"UP"}}` |
| Governance Analytics | `GET /api/v1/governance/analytics/health` | `{"status":"UP","components":{"db":"UP","redis":"UP"}}` |
| Administration | `GET /api/v1/admin/health` | `{"status":"UP","components":{"db":"UP","redis":"UP"}}` |
| Automation & Lifecycle | `GET /api/v1/automation/health` | `{"status":"UP","components":{"db":"UP","redis":"UP"}}` |

## 3. Cache Warmup Procedure

```bash
# Warm governance policies cache
curl -X POST "http://localhost:8080/api/v1/governance/policies" \
  -H "Content-Type: application/json" \
  -d '{"scope":"GLOBAL","status":"ACTIVE"}'

# Warm policy registry
curl -X GET "http://localhost:8080/api/v1/policies/evaluate" \
  -H "Content-Type: application/json" \
  -d '{"module":"GOVERNANCE","action":"EVALUATE","scope":"GLOBAL"}'

# Warm dashboard data
curl -X GET "http://localhost:8080/api/v1/governance/analytics/dashboard"

# Verify cache hit ratios
redis-cli --stat
```

## 4. Kafka Topic Verification

```bash
# List all topics
kafka-topics.sh --list --bootstrap-server kafka:9092

# Describe topic configuration
kafka-topics.sh --describe --topic governance-events --bootstrap-server kafka:9092

# Check consumer groups
kafka-consumer-groups.sh --bootstrap-server kafka:9092 --list

# Check consumer lag (repeat for each group)
kafka-consumer-groups.sh --bootstrap-server kafka:9092 \
  --group governance-consumer \
  --describe

# Produce test event
echo '{"type":"HealthCheck","timestamp":"2026-07-12T10:00:00Z"}' | \
  kafka-console-producer.sh --topic governance-events --bootstrap-server kafka:9092
```

## 5. Database Migration Verification

```bash
# Check Flyway migration status via API
curl -s http://localhost:8080/actuator/flyway | jq '.migrations[] | select(.state == "SUCCESS") | .version'

# Expected output: V20, V21, V22, V23, V24, V25, V26, V27, V28

# Check migration checksums
curl -s http://localhost:8080/actuator/flyway | jq '.'

# Verify table counts
psql -h postgres-primary -d governance -c "
SELECT schemaname, tablename, n_live_tup
FROM pg_stat_user_tables
WHERE schemaname = 'public'
ORDER BY tablename;
"
```

## 6. Module Enable/Disable

### Enable a module
```bash
curl -X POST "http://localhost:8080/api/v1/admin/modules/policy-engine/enable" \
  -H "Content-Type: application/json" \
  -d '{"reason":"Deployment complete"}'
```

### Disable a module
```bash
curl -X POST "http://localhost:8080/api/v1/admin/modules/policy-engine/disable" \
  -H "Content-Type: application/json" \
  -d '{"reason":"Scheduled maintenance"}'
```

### Check module status
```bash
curl -s "http://localhost:8080/api/v1/admin/modules" | jq '.'
```

## 7. Configuration Management

### View current configuration
```bash
curl -s "http://localhost:8080/api/v1/admin/config" | jq '.'
```

### Update configuration
```bash
curl -X PUT "http://localhost:8080/api/v1/admin/config/sporekert.ai.governance.caching" \
  -H "Content-Type: application/json" \
  -d '{"value":"true","module":"governance"}'
```

### Rollback configuration
```bash
curl -X POST "http://localhost:8080/api/v1/admin/config/rollback" \
  -H "Content-Type: application/json" \
  -d '{"version":5}'
```

### Toggle feature flag
```bash
curl -X POST "http://localhost:8080/api/v1/admin/features/governance-caching/toggle" \
  -H "Content-Type: application/json" \
  -d '{"reason":"Cache performance test"}'
```

## 8. Monitoring Dashboards

| Dashboard | URL | Metrics Displayed |
|-----------|-----|-------------------|
| Governance Overview | `/dashboards/governance-overview` | System health, module status, error rates |
| Policy Engine | `/dashboards/policy-engine` | Evaluation latency, decision distribution |
| Approval Platform | `/dashboards/approval-platform` | SLA compliance, approval throughput |
| Compliance | `/dashboards/compliance` | Pass rate, violation trends |
| Risk & Trust | `/dashboards/risk-trust` | Risk distribution, trust scores |
| Analytics & Reporting | `/dashboards/analytics` | Report generation, KPI trends |
| Administration | `/dashboards/administration` | Configuration changes, module status |
| Automation | `/dashboards/automation` | Job execution, lifecycle transitions |
| Infrastructure | `/dashboards/infrastructure` | DB, Redis, Kafka cluster health |

## 9. Alert Thresholds

| Alert | Warning Threshold | Critical Threshold | Action |
|-------|-------------------|--------------------|--------|
| Policy Eval Latency | > 80ms (P95) | > 100ms (P95) | Investigate policy engine |
| Decision Latency | > 40ms (P95) | > 50ms (P95) | Investigate decision engine |
| API P99 Latency | > 500ms | > 1000ms | Scale application instances |
| DB Pool Utilization | > 70% | > 85% | Increase pool size |
| Redis Hit Ratio | < 80% | < 60% | Review cache strategy |
| Kafka Consumer Lag | > 1000 | > 5000 | Scale consumers |
| Error Rate (5xx) | > 0.1% | > 1% | Review error logs |
| Audit Queue Depth | > 10000 | > 50000 | Scale audit processing |
| CPU Utilization | > 70% | > 85% | Scale application instances |
| Memory Utilization | > 75% | > 90% | Review heap configuration |

## 10. Incident Response

### Incident Severity Levels
| Severity | Response Time | Example |
|----------|--------------|---------|
| SEV1 (Critical) | < 15 min | Platform down, data loss |
| SEV2 (High) | < 30 min | Module unavailable, high error rate |
| SEV3 (Medium) | < 2 hours | Degraded performance, non-critical feature broken |
| SEV4 (Low) | < 24 hours | Cosmetic issue, minor bug |

### Incident Response Steps
1. **Acknowledge**: Respond to alert, acknowledge in PagerDuty
2. **Assess**: Check health endpoints, error logs, metrics dashboards
3. **Contain**: If critical, disable affected module via admin API
4. **Diagnose**: Review application logs, DB queries, Redis, Kafka
5. **Resolve**: Apply fix, configuration change, or rollback
6. **Verify**: Run health checks, verify module status, run smoke tests
7. **Communicate**: Post-incident summary, root cause, preventive measures

### Common Incident Procedures

#### High Error Rate (5xx)
```bash
# 1. Check health
curl -s http://localhost:8080/actuator/health | jq .

# 2. Check recent errors
tail -1000 /var/log/app/error.log | grep "ERROR"

# 3. Check DB connectivity
pg_isready -h postgres-primary -p 5432

# 4. Check DB pool
curl -s http://localhost:8080/actuator/metrics/hikaricp.connections.active | jq .

# 5. Restart if necessary
kubectl rollout restart deployment/governance-platform
```

#### Kafka Consumer Lag
```bash
# 1. Check consumer lag
kafka-consumer-groups.sh --bootstrap-server kafka:9092 \
  --group governance-consumer --describe

# 2. Restart consumer group
kubectl rollout restart deployment/governance-consumer

# 3. If persistent lag, scale consumers
kubectl scale deployment/governance-consumer --replicas=5
```

#### Redis Cache Issues
```bash
# 1. Check cache hit ratio
redis-cli INFO stats | grep hit

# 2. Clear specific cache namespace (caution — will impact performance)
redis-cli --scan --pattern "policy:*" | head -10

# 3. Clear all governance cache (last resort)
redis-cli --scan --pattern "governance:*" | xargs redis-cli DEL
```

## 11. Backup and Restore

### Database Backup
```bash
# Full backup
pg_dump -h postgres-primary -U admin -d governance \
  -F c -f /backups/governance_$(date +%Y%m%d_%H%M%S).dump

# Schema-only backup
pg_dump -h postgres-primary -U admin -d governance \
  --schema-only -f /backups/governance_schema_$(date +%Y%m%d).sql
```

### Database Restore
```bash
# Restore from full backup
pg_restore -h postgres-primary -U admin -d governance \
  --clean --if-exists /backups/governance_20260712_100000.dump

# Run migrations post-restore
java -jar governance-platform.jar --spring.flyway.clean-disabled=false
```

### Configuration Backup
```bash
# Export all governance configurations
curl -s "http://localhost:8080/api/v1/governance/config/export" | \
  jq '.governance.configs' > /backups/governance_config_$(date +%Y%m%d).json

# Export admin configurations
curl -s "http://localhost:8080/api/v1/admin/config" | \
  jq '.' > /backups/admin_config_$(date +%Y%m%d).json
```

## 12. Rollback Procedures

### Application Rollback
```bash
# Rollback to previous version
kubectl rollout undo deployment/governance-platform

# Verify rollback
kubectl rollout status deployment/governance-platform
```

### Database Rollback
```bash
# Flyway undo (requires undo scripts)
curl -X POST http://localhost:8080/actuator/flyway/undo

# Manual undo — apply V28__undo.sql
psql -h postgres-primary -U admin -d governance -f V28__undo.sql
```

### Configuration Rollback
```bash
# Rollback to specific version
curl -X POST "http://localhost:8080/api/v1/admin/config/rollback" \
  -H "Content-Type: application/json" \
  -d '{"version":3}'
```

## 13. Scaling Guidelines

### Horizontal Scaling (Application)
```bash
# Scale up
kubectl scale deployment/governance-platform --replicas=5

# Scale down
kubectl scale deployment/governance-platform --replicas=3
```

### Vertical Scaling (Database)
- Monitor connection pool utilization
- Increase `hikari.maximum-pool-size` if > 80% utilized
- Add read replicas for dashboard/report queries

### Kafka Scaling
- Increase partitions for high-volume topics
- Add consumer group members for lag reduction
- Increase retention period if needed

### Redis Scaling
- Add cluster nodes for larger datasets
- Monitor eviction rate — increase maxmemory if > 10/sec

## 14. Maintenance Mode

### Enable Maintenance Mode
```bash
curl -X POST "http://localhost:8080/api/v1/admin/maintenance" \
  -H "Content-Type: application/json" \
  -d '{"enabled":true,"reason":"Scheduled maintenance","duration":"30m"}'
```

### Schedule Maintenance Window
```bash
curl -X POST "http://localhost:8080/api/v1/admin/maintenance/schedule" \
  -H "Content-Type: application/json" \
  -d '{"startTime":"2026-07-13T02:00:00Z","endTime":"2026-07-13T03:00:00Z","reason":"DB migration"}'
```

### Disable Maintenance Mode
```bash
curl -X POST "http://localhost:8080/api/v1/admin/maintenance" \
  -H "Content-Type: application/json" \
  -d '{"enabled":false}'
```

### Maintenance Mode Behavior
- Health checks return `UP` with `"maintenance": true`
- API endpoints return `503 Service Unavailable` with maintenance message
- Kafka consumers pause processing
- Scheduled jobs skip execution
- Health check monitoring suppresses alerts during maintenance window
