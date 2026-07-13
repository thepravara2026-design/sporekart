# Production Readiness Certification

## Deployment Architecture

```
                                ┌─────────────────────┐
                                │   Load Balancer      │
                                │   (SSL Termination)  │
                                └──────────┬──────────┘
                                           │
                                  ┌────────┴────────┐
                                  │  API Gateway     │
                                  │  (Rate Limiting) │
                                  └────────┬────────┘
                                           │
                    ┌──────────────────────┼──────────────────────┐
                    │                      │                      │
            ┌───────▼───────┐     ┌───────▼───────┐     ┌───────▼───────┐
            │  App Instance 1 │     │  App Instance 2 │     │  App Instance 3 │
            │  (Governance    │     │  (Governance    │     │  (Governance    │
            │   Platform)     │     │   Platform)     │     │   Platform)     │
            └───────┬───────┘     └───────┬───────┘     └───────┬───────┘
                    │                      │                      │
                    └──────────────────────┼──────────────────────┘
                                           │
                    ┌──────────────────────┼──────────────────────┐
                    │                      │                      │
            ┌───────▼───────┐     ┌───────▼───────┐     ┌───────▼───────┐
            │  PostgreSQL    │     │    Redis       │     │    Kafka      │
            │  (Primary)     │     │   (Cluster)    │     │   (Cluster)   │
            │  + Replica     │     │   + Sentinel   │     │   + ZK        │
            └───────────────┘     └───────────────┘     └───────────────┘
```

## Scaling Considerations

### Stateless Application Instances
- All 9 governance modules are **stateless** — scale horizontally by adding instances
- Session state stored in Redis (not in-memory)
- Configuration cached in Redis with DB fallback
- Feature flags evaluated at runtime from Redis/DB

### Database Scaling
- **PostgreSQL Primary**: Handles all writes (config, audit, policies, decisions)
- **Read Replicas**: Handle dashboard/report queries and analytics read operations
- **Connection Pooling**: HikariCP with configurable max pool size per instance
- **Migration Strategy**: Flyway with manual approval for production migrations

### Redis Scaling
- **Redis Cluster**: Data sharded across multiple nodes
- **Redis Sentinel**: Provides high availability with automatic failover
- **Cache TTL Strategy**: Conservative TTLs (60s-600s) to prevent stale data
- **Eviction Policy**: LRU (all keys have TTLs)

### Kafka Scaling
- **3 Partitions per Topic**: Allows up to 3 consumer instances per consumer group
- **Replication Factor**: 1 (development); 3 recommended for production
- **Consumer Groups**: Each module has dedicated consumer group
- **Retention Period**: 7 days default; configurable per topic

## High Availability Design

| Component | Strategy | RTO | RPO |
|-----------|----------|-----|-----|
| Application | Active-Active (3+ instances) | < 1 min | N/A (stateless) |
| PostgreSQL | Primary + Async Replica | < 5 min | < 1 min |
| Redis | Cluster + Sentinel | < 10 sec | N/A (cache) |
| Kafka | Cluster (3 brokers) | < 1 min | Configurable |
| Load Balancer | Active-Passive | < 30 sec | N/A |

## Disaster Recovery

### Recovery Strategy
1. **Application**: Deploy new instance from CI/CD pipeline with latest artifact
2. **Database**: Promote read replica to primary; reconfigure connection strings
3. **Redis**: Rebuild cache from database (cache-aside pattern ensures eventual consistency)
4. **Kafka**: Replay events from last committed offset; re-produce from source if needed

### DR Runbook Steps
1. Promote PostgreSQL read replica to primary
2. Update application datasource configuration
3. Redeploy application instances with new configuration
4. Verify health check endpoints return 200
5. Run cache warmup procedure
6. Verify Kafka consumer group offsets
7. Run integration smoke tests

## Backup Strategy

| Data Store | Backup Type | Frequency | Retention | Restore Time |
|-----------|-------------|-----------|-----------|-------------|
| PostgreSQL | Full | Daily | 30 days | < 1 hour |
| PostgreSQL | WAL archiving | Continuous | 7 days | < 15 min |
| Redis | RDB snapshot | Every 5 min | 24 hours | < 1 min |
| Kafka | Log segments | Continuous | 7 days | < 30 min |
| Application config | Git (infra repo) | Every change | Permanent | < 10 min |

## Monitoring and Alerting

### Health Check Endpoints

| Module | Health Check Path | Returns |
|--------|------------------|---------|
| Governance Foundation | `/api/v1/governance/health` | UP/DOWN + DB/REDIS/KAFKA status |
| Policy Engine | `/api/v1/policies/health` | UP/DOWN + DB/Redis/Kafka |
| Decision Engine | `/api/v1/decisions/health` | UP/DOWN + DB/Redis/Kafka |
| Approval Platform | `/api/v1/approvals/health` | UP/DOWN + DB/Redis/Kafka |
| Compliance Framework | `/api/v1/compliance/health` | UP/DOWN + DB/Redis/Kafka |
| Risk & Trust | `/api/v1/risk/health` | UP/DOWN + DB/Redis/Kafka |
| Governance Analytics | `/api/v1/governance/analytics/health` | UP/DOWN + DB/Redis/Kafka |
| Administration | `/api/v1/admin/health` | UP/DOWN + DB/Redis/Kafka |
| Automation & Lifecycle | `/api/v1/automation/health` | UP/DOWN + DB/Redis/Kafka |

### Alert Thresholds

| Metric | Warning | Critical | Description |
|--------|---------|----------|-------------|
| Policy evaluation latency | > 80ms | > 100ms | P95 policy evaluation time |
| Decision execution latency | > 40ms | > 50ms | P95 decision execution time |
| API endpoint latency (P99) | > 500ms | > 1000ms | Overall API response time |
| Database connection pool | > 70% | > 85% | HikariCP pool utilization |
| Redis cache hit ratio | < 80% | < 60% | Cache efficiency |
| Kafka consumer lag | > 1000 | > 5000 | Messages waiting to be consumed |
| Error rate (5xx) | > 0.1% | > 1% | HTTP 5xx responses |
| Audit queue depth | > 10000 | > 50000 | Pending audit records |
| CPU utilization | > 70% | > 85% | Per-instance CPU |
| Memory utilization | > 75% | > 90% | Per-instance heap usage |

## Logging Strategy

### Log Format (Structured JSON)
```json
{
  "timestamp": "2026-07-12T10:00:00.000Z",
  "level": "INFO",
  "logger": "com.sporekart.ai.governance",
  "module": "governance",
  "operation": "evaluatePolicy",
  "correlationId": "uuid",
  "userId": "user-123",
  "duration": 45,
  "message": "Policy evaluated successfully",
  "additional": {}
}
```

### Log Levels
- **ERROR**: Failures requiring immediate attention (DB connection failures, unrecoverable errors)
- **WARN**: Degraded functionality (cache miss, non-critical validation failure)
- **INFO**: Business operations (policy evaluation, decision made, approval completed)
- **DEBUG**: Detailed flow tracing (enabled per-module in development)

### Log Retention
- **Application logs**: 30 days (rotated daily)
- **Audit logs**: 365 days (immutable, stored in DB)
- **Access logs**: 90 days (load balancer level)
- **Error logs**: 90 days (separate file)

## Monitoring Dashboards

### Prometheus Metrics Available
- Business metrics: policy evaluations, decisions, approvals, compliance checks
- Performance metrics: P50/P95/P99 latency for all operations
- Infrastructure metrics: JVM heap, GC, thread count, DB pool, Redis, Kafka
- Custom Micrometer metrics exposed per module

### Grafana Dashboards
1. **Governance Overview** — System health, module status, error rates
2. **Policy Engine** — Evaluation latency, decision distribution, violation counts
3. **Approval Platform** — SLA compliance, approval throughput, escalation rates
4. **Compliance Dashboard** — Pass rate, violation trends, framework coverage
5. **Risk & Trust** — Risk distribution, trust scores, confidence levels
6. **Analytics & Reporting** — Report generation, KPI trends, export metrics
7. **Administration** — Configuration changes, feature flag toggles, module status
8. **Automation** — Job execution, lifecycle transitions, scheduled task status
9. **Infrastructure** — Database, Redis, Kafka cluster health
