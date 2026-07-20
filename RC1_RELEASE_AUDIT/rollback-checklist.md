# Rollback Checklist — SporeKart v1.0 RC1

**Prepared:** 2026-07-20
**Status:** ❌ RC1 NOT APPROVED — checklist prepared for future use

---

## 1. Rollback Triggers

Initiate rollback if any of the following occur within 2 hours of deployment:
- Error rate increases >5% above baseline
- P95 response time increases >500ms above baseline
- Payment failures >1% of transactions
- Authentication failures >5% of attempts
- Any P0/P1 incident confirmed
- Database corruption detected
- Security breach detected

## 2. Rollback Steps

### Immediate (first 5 minutes)
```
1. PAUSE traffic: Update load balancer to stop sending traffic to new version
   - If using blue/green: switch DNS/CNAME to previous environment
   - If using rolling: scale down new replicas to 0
2. ALERT: Notify on-call engineer and release manager
3. ASSESS: Is rollback necessary or can issue be hotfixed?
```

### Rapid Rollback (within 15 minutes)
```
4. RESTORE previous Docker image tag:
   docker pull sporekart/web-app:{previous-tag}
   docker-compose -f docker-compose.prod.yml up -d web-app
5. VERIFY health endpoint returns 200:
   curl -f http://localhost:4173/health
   curl -f http://localhost:4173/api/health
6. RESUME traffic to restored version
```

### Database Rollback (if migration involved)
```
7. IDENTIFY the migration to revert:
   SELECT * FROM migrations ORDER BY applied_at DESC LIMIT 5;
8. REVERT migration:
   - If using supabase: supabase db diff --use-migra
   - If custom: run DOWN migration script
9. VERIFY data integrity:
   - Row counts match pre-deployment backup
   - Critical entities queryable
10. RESTORE from backup if migration cannot be cleanly reverted:
    pg_restore -d sporekart_production /backups/sporekart_pre_rc1.dump
```

### Full Rollback (within 1 hour)
```
11. DNS/CNAME: Point to previous deployment environment
12. DEPLOY previous artifact version as confirmed-good
13. VERIFY all health checks pass
14. NOTIFY: Release manager, engineering team, product owner
15. INCIDENT: Create post-mortem ticket
```

## 3. Verification After Rollback

- [ ] Application loads and renders
- [ ] Login flow works
- [ ] All critical customer journeys pass
- [ ] API endpoints return expected data
- [ ] Database connections healthy
- [ ] Error rate returns to baseline
- [ ] Support team notified of rollback status
- [ ] Stakeholders notified

## 4. Communication Template

```
Subject: [ROLLBACK] SporeKart v{VERSION} rolled back to v{PREVIOUS_VERSION}

Time: {TIMESTAMP}
Trigger: {REASON}
Action: {FULL / PARTIAL / DATABASE_ONLY}
Current Status: {STABLE / INVESTIGATING / MONITORING}

Impact: {ESTIMATED_DOWNTIME}
Affected Users: {COUNT / PERCENTAGE}

Next Steps:
1. Root cause analysis in progress
2. Patch scheduled for {ESTIMATED_FIX_TIME}
3. Follow-up at {NEXT_UPDATE_TIME}
```

## 5. Post-Rollback Tasks

- [ ] Root cause analysis opened
- [ ] Bug fix prioritized in next sprint
- [ ] Rollback tested in staging environment
- [ ] Monitoring alerts adjusted if needed
- [ ] Deployment process updated to prevent recurrence
- [ ] Rollback checklist updated based on lessons learned

---

*This checklist is prepared for the RC1 release but is not yet actionable as the release was not approved.*
