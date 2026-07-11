# Sprint 16 — PK Switch Monitoring & Validation Guide

## Overview

This document covers the 24–72 hour monitoring window after deploying Phase C (PK swap) and Phase D (application cutover). The goal is to validate that all PK-switched services correctly handle both legacy and UUID identifiers, and that no regressions occur in RBAC, auth, or cross-service flows.

### Services in scope

| Service | Port | PK Switch Status |
|---------|------|-----------------|
| order-service | 8081 | ✅ Phase A-D deployed |
| inventory-service | 8082 | ✅ Phase A-D deployed |
| fulfillment-service | 8083 | ✅ Phase A-D deployed |
| payment-service | 8084 | ✅ Phase A-D deployed |
| catalog-service | 8085 | ✅ Phase A-D deployed |
| training-service | 8087 | ✅ Phase A-D deployed |
| notification-service | 8088 | ✅ Phase A-D deployed |
| admin-service | 8089 | ✅ Phase A-D deployed |
| analytics-service | 8090 | ✅ Phase A-D deployed |
| identity-service | 8080 | ✅ Phase A deployed; V3-V5 migrations applied |

---

## Phase 1 — Immediate Post-Deployment Validation (T+0)

### 1.1 Health checks

Run against every service:

```
curl -s http://localhost:<port>/actuator/health | jq .status
# Expected: "UP"
```

### 1.2 Database migration verification

Connect to the database and run for each PK-switched table:

```sql
-- Confirm tmp_ tables were swapped and old_ tables exist
SELECT table_name FROM information_schema.tables WHERE table_name LIKE 'old_%';

-- Confirm no NULL id_uuid remains
SELECT COUNT(*) FROM <table> WHERE id IS NULL;

-- Confirm id values are valid UUIDs
SELECT COUNT(*) FROM <table> WHERE id NOT SIMILAR TO '[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}';
-- Expected: 0
```

### 1.3 Feature flag status

Each service should have `sporekart.pk-switch.read-by-uuid: true`. Verify via:

```
curl -s http://localhost:<port>/actuator/env/sporekart.pk-switch.read-by-uuid | jq .
# Expected: {"property": {"value": "true"}, ...}
```

---

## Phase 2 — Read-Path Validation (T+1h)

### 2.1 findByIdOrUuid acceptance test

For each service, call the GET endpoint with:
1. A UUID identifier → expect 200
2. A legacy VARCHAR identifier (where applicable) → expect 200
3. A random non-existent identifier → expect 404

**catalog-service** `GET /products/{identifier}`
**order-service** `GET /orders/{identifier}`
**fulfillment-service** `GET /shipments/{identifier}`
**payment-service** `GET /payments/{identifier}`
**inventory-service** `GET /inventory/{identifier}`
**admin-service** `GET /admin/support` (list only; no individual GET endpoint — verify via service)
**notification-service** `GET /notifications` (list only)
**training-service** `GET /trainings/{id}/publish` (uses findByIdOrUuid internally)
**identity-service** `GET /users/{identifier}`

### 2.2 Write-path validation

Create a new entity via POST and verify the returned `id` is a UUID:

```
POST /orders
{"customerId": "test-customer", "amount": 99.99, "items": [...]}
→ Response id should match UUID format
```

### 2.3 Legacy ID fallback validation (T+2h)

Toggle the feature flag to `false` and verify that the system still functions with legacy VARCHAR lookups:

```
PUT sporekart.pk-switch.read-by-uuid=false
curl -s http://localhost:<port>/<entity>/<legacy-id> → 200
```

Re-enable after test.

---

## Phase 3 — Log & Error Monitoring (T+2h to T+24h)

### 3.1 Log patterns to watch

| Pattern | Meaning | Action |
|---------|---------|--------|
| `ERROR.*findByIdOrUuid` | UUID lookup failed | Check DB migration state |
| `WARN.*legacy fallback` | Falling back to VARCHAR lookup | Expected during transition |
| `ERROR.*ConstraintViolation` | FK constraint issue | Check child table backfill |
| `ERROR.*psql.*UUID` | UUID type mismatch | Verify migration V4 ran correctly |
| `ERROR.*DuplicateKey` | Duplicate UUID generated | Check id_uuid uniqueness |

### 3.2 Metrics to monitor (Prometheus)

| Metric | Threshold | Alert |
|--------|-----------|-------|
| `http_server_requests_seconds_count{status=5xx}` | > 0 | Error rate increased |
| `http_server_requests_seconds_count{status=4xx}` | > baseline + 20% | Client errors |
| `jdbc_connections_active` | > 80% of pool | Connection pool exhaustion |
| `spring_data_repository_*` | spike in query time | Slow queries on new PKs |

### 3.3 Key Grafana dashboards

1. **Service Health** — all `/actuator/health` endpoints green
2. **Error Rates** — 5xx per service should be 0
3. **Latency** — P99 response time should match pre-deployment baseline
4. **Database** — active connections, query times, migration status

---

## Phase 4 — Cross-Service Flow Validation (T+4h)

### 4.1 End-to-end smoke test

```
POST /auth/register → user created with UUID id
POST /orders → order created referencing user UUID
GET /orders/{id} → order returned with UUID
POST /payments → payment created with UUID
GET /shipments → shipment references UUIDs
```

### 4.2 RBAC validation (identity-service specific)

```
POST /auth/register → user assigned CUSTOMER role
POST /auth/login → auth succeeds
GET /users/{id} → UserAccount returned with roles set
```

Verify that `user_roles.user_id_uuid` is populated:
```sql
SELECT COUNT(*) FROM user_roles WHERE user_id_uuid IS NULL AND user_id IS NOT NULL;
-- Expected: 0
```

### 4.3 External API contract check

Verify that all API responses return the `id` field as a UUID string:

```
curl -s http://localhost:<port>/<entity> | jq '.id'
# Expected: "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
```

---

## Phase 5 — Monitoring Period (T+24h to T+72h)

### 5.1 Ongoing checks (every 8 hours)

1. Review error logs for UUID-related failures
2. Check that query performance is stable
3. Verify no FK constraint violations
4. Spot-check random entity lookups by both UUID and legacy ID
5. Confirm Prometheus targets are all UP

### 5.2 Rollback triggers

| Condition | Action |
|-----------|--------|
| > 5% error rate increase on any service | Roll back application, keep old_ tables |
| Any data integrity violation | Roll back to DB snapshot |
| Identity/auth flow failures | Immediate rollback; restore users table |
| findByIdOrUuid returns wrong entity | Disable feature flag → set `read-by-uuid=false` |
| Grafana target down > 5 min | Investigate service health |

### 5.3 Rollback procedure

```bash
# 1. Set feature flag to false on all services
# 2. Revert application to pre-PK-switch build
# 3. If needed: rename old_<table> back to <table>
# 4. Verify health and run smoke tests
```

---

## Phase 6 — Cleanup Approval (T+72h)

### 6.1 Pre-cleanup checklist

- [ ] No UUID-related errors in logs *(requires monitoring period)*
- [ ] All services return 200 on UUID lookups *(requires monitoring period)*
- [ ] Legacy fallback works when flag is toggled *(requires monitoring period)*
- [ ] Cross-service flows complete without errors *(requires monitoring period)*
- [ ] Prometheus targets all UP *(requires monitoring period)*
- [ ] Grafana dashboards show no anomalies *(requires monitoring period)*
- [x] All V5 migrations correctly structured (DROP old_ tables, ALTER DROP legacy columns)
- [x] Zero PK switch references remain in source code
- [x] Java code cleanup (PkCompatibility, readByUuid, findByIdOrUuid, feature flags) completed

### 6.2 Execute V5 cleanup

```sql
-- Run V5__sprint16_pk_cleanup.sql for each service
DROP TABLE IF EXISTS old_<table>;
ALTER TABLE <table> DROP COLUMN IF EXISTS id_uuid;
ALTER TABLE <table> DROP COLUMN IF EXISTS id_legacy;
```

### 6.3 Remove feature flag (completed)

The following Java code changes have been applied across all 10 services:

- [x] Delete `PkCompatibility` utility classes (7 services)
- [x] Delete `sporekart.pk-switch.read-by-uuid` property from all application.yml files
- [x] Remove `readByUuid` constructor parameters from repository adapters
- [x] Remove `PkCompatibility.lookupByIdOrUuid` calls; use direct UUID lookup (`findById`)
- [x] Remove `findByIdOrUuid` from repository ports (keep only `findById`)
- [x] No remaining references to PK switch patterns in any source file

Recompile and redeploy will be needed after the monitoring period when the DB cleanup migrations are applied.

---

## Service Registry

| Service | Port | Actuator Health | Prometheus Metrics |
|---------|------|----------------|-------------------|
| identity-service | 8080 | `/actuator/health` | `/actuator/prometheus` |
| order-service | 8081 | `/actuator/health` | `/actuator/prometheus` |
| inventory-service | 8082 | `/actuator/health` | `/actuator/prometheus` |
| fulfillment-service | 8083 | `/actuator/health` | `/actuator/prometheus` |
| payment-service | 8084 | `/actuator/health` | `/actuator/prometheus` |
| catalog-service | 8085 | `/actuator/health` | `/actuator/prometheus` |
| training-service | 8087 | `/actuator/health` | `/actuator/prometheus` |
| notification-service | 8088 | `/actuator/health` | `/actuator/prometheus` |
| admin-service | 8089 | `/actuator/health` | `/actuator/prometheus` |
| analytics-service | 8090 | `/actuator/health` | `/actuator/prometheus` |
