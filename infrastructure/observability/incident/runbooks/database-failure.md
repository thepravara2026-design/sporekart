# Runbook: Database Failure

## Severity: SEV1

## Symptoms
- Database health check failing
- Connection pool exhaustion alerts
- Slow queries or query timeouts
- Application errors: DataAccessException
- PagerDuty alert: DatabaseFailure

## Immediate Actions

1. **ACKNOWLEDGE** alert
2. **ANNOUNCE** in #sporekart-sev1
3. **CHECK** database connectivity:
   ```
   psql -h postgres -U sporekart -d sporekart_prod -c "SELECT 1"
   ```
4. **CHECK** connection count:
   ```sql
   SELECT count(*) FROM pg_stat_activity;
   ```

## Diagnosis

### Connection Pool Exhaustion
```sql
SELECT state, count(*) FROM pg_stat_activity WHERE backend_type = 'client backend' GROUP BY state;
```
- If `active` is high and `idle` is low → pool saturated
- Check for long-running queries:
  ```sql
  SELECT pid, now() - query_start, query FROM pg_stat_activity WHERE state = 'active' ORDER BY query_start;
  ```

### Slow Queries
```sql
SELECT queryid, calls, mean_exec_time, total_exec_time
FROM pg_stat_statements
ORDER BY mean_exec_time DESC LIMIT 20;
```

### Lock Contention
```sql
SELECT blocked_locks.pid, blocked_activity.query
FROM pg_locks blocked_locks
JOIN pg_stat_activity blocked_activity ON blocked_activity.pid = blocked_locks.pid
WHERE NOT blocked_locks.granted;
```

## Recovery Actions

### Option A: Kill Blocking Queries
```sql
SELECT pg_terminate_backend(pid) FROM pg_stat_activity
WHERE state = 'active' AND now() - query_start > interval '5 minutes';
```

### Option B: Force Connection Pool Reset
```bash
kubectl rollout restart deployment/<service> -n sporekart-platform
```

### Option C: Failover to Replica
```bash
kubectl exec -n sporekart-platform postgres-0 -- pg_ctl promote
```

### Option D: Scale Read Replicas
```bash
kubectl scale statefulset/postgres-read --replicas=3 -n sporekart-platform
```

## Verification
1. `SELECT 1` returns successfully
2. Connection pool Grafana shows active < 80%
3. Query latency P95 < 50ms
4. Application health check passes
5. No query timeouts in logs

## Post-Recovery
1. Analyze slow query log: `infrastructure/database/optimization/slow-query-analysis.sql`
2. Review connection pool sizing
3. Run VACUUM ANALYZE on affected tables
4. Update postmortem within 24h

## Contact
- DBA: @dba-team
- On-Call: @sre-oncall
