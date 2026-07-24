# Rollback Guide

**SporeKart Enterprise Platform v2.0**  
**Document:** RollbackGuide.md  
**Last Updated:** 2026-07-24

---

## Rollback Philosophy

Every deployment must be reversible. The SporeKart rollback platform ensures:

1. **Deterministic**: Rollback to any previous version with git tag
2. **Safe**: Database compatibility checked before rollback
3. **Fast**: Service rollback completes in <5 minutes
4. **Auditable**: Every rollback is logged and notified

---

## Rollback Triggers

| Trigger | Response | Time Target |
|---------|----------|-------------|
| Health check failure | Automatic rollback to previous version | 2 min |
| Error rate > 1% (5 min window) | Automatic rollback | 3 min |
| P99 latency > 500ms | Automatic rollback | 3 min |
| Manual discovery of regression | Manual rollback via workflow | 5 min |
| Security incident | Immediate rollback + incident response | 2 min |

---

## Rollback Procedure

### Method 1: GitHub Actions (Recommended)

1. Go to Actions → Rollback — Enterprise Rollback Platform
2. Click "Run workflow"
3. Select environment: `staging` or `production`
4. Enter rollback target: `v2.0.0` (exact git tag)
5. Optionally specify service name (empty = all)
6. Monitor the workflow execution

The workflow performs:
1. **Validate**: Check rollback target exists in git history
2. **Database check**: Verify migration compatibility
3. **Execute**: Force new deployment with previous image
4. **Verify**: Health check + smoke test
5. **Notify**: Slack notification with result

### Method 2: Command Line

```bash
# Rollback staging to v2.0.0
./scripts/rollback.sh staging v2.0.0

# Rollback production to v2.0.0
./scripts/rollback.sh production v2.0.0
```

### Method 3: AWS CLI (Emergency)

```bash
# Get the previous task definition
PREV_TASK=$(aws ecs describe-services \
  --cluster sporekart-prod \
  --services sporekart-api \
  --region us-east-1 \
  --query 'services[0].deployments[1].taskDefinition' \
  --output text)

# Rollback to previous task definition
aws ecs update-service \
  --cluster sporekart-prod \
  --service sporekart-api \
  --task-definition "$PREV_TASK" \
  --force-new-deployment \
  --region us-east-1
```

---

## Database Rollback

### Schema Rollback

**Important**: Flyway does NOT automatically run undo migrations.

```sql
-- Manual rollback example (V2 must be reverse of V2)
-- V2__add_column.sql
ALTER TABLE users ADD COLUMN phone VARCHAR(20);

-- U2__add_column.sql (undo)
ALTER TABLE users DROP COLUMN phone;
```

To execute database rollback:

```bash
# Connect to database
psql $DATABASE_URL

# Check current version
SELECT version, description FROM flyway_schema_history ORDER BY installed_rank DESC LIMIT 5;

# Manually undo (if undo migration exists)
# Flyway repair if needed
```

### Data Rollback

For data-only rollbacks (not schema):

1. Restore from RDS snapshot (point-in-time recovery)
2. Verify data integrity
3. Re-run any lost transactions from event replay

---

## Rollback Safety Checklist

- [ ] Rollback target is a valid git tag (not a branch)
- [ ] Target version has compatible database schema
- [ ] Target version's migrations are earlier than current
- [ ] No active long-running transactions
- [ ] Rollback tested on staging first
- [ ] Team notified via Slack
- [ ] Incident ticket created if rollback was automated

---

## Post-Rollback Actions

1. **Verify**: Health check, smoke tests, monitoring dashboards
2. **Stabilize**: If rollback is successful, keep environment stable for 30 min
3. **Investigate**: Root cause analysis for the failed deployment
4. **Fix**: Address root cause in new feature branch
5. **Redeploy**: After fix is validated, redeploy through normal pipeline
6. **Learn**: Update deployment checklist, monitoring, and runbooks

---

## Key Metrics

| Metric | Target | Measurement |
|--------|--------|-------------|
| Rollback detection time | <2 min | Monitoring alert to trigger |
| Rollback execution time | <5 min | Workflow duration |
| Database compatibility check | <30 sec | Migration validation |
| Service recovery time | <3 min | ECS service stability |
