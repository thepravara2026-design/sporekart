# Disaster Recovery

**SporeKart Enterprise Platform v2.0**  
**Document:** DisasterRecovery.md  
**Last Updated:** 2026-07-24

---

## Recovery Objectives

| Tier | RTO (Recovery Time Objective) | RPO (Recovery Point Objective) |
|------|------------------------------|------------------------------|
| Critical (orders, payments, auth) | < 15 min | < 1 min |
| Standard (catalog, cart, notifications) | < 1 hour | < 5 min |
| Background (analytics, reports, AI training) | < 4 hours | < 1 hour |

---

## Backup Strategy

### Automated Backups

| Component | Frequency | Retention | Method | Location |
|-----------|-----------|-----------|--------|----------|
| PostgreSQL (RDS) | Daily (automated) | 30 days | Automated snapshots | AWS RDS |
| PostgreSQL (WAL) | Continuous | 7 days (PITR) | WAL archiving | S3 bucket |
| Redis (ElastiCache) | Daily | 7 days | Snapshot | S3 bucket |
| Kubernetes manifests | On change | 30 days | Git (versioned) | GitHub |
| Terraform state | On apply | 30 days | S3 backend | S3 bucket |
| Environment config | On change | 90 days | Encrypted (.gpg) | S3 bucket |
| Docker images | On release | Indefinite | ECR | AWS ECR |

### Manual Backup Script

```bash
# Full platform backup
./infrastructure/disaster-recovery/backup.sh backup

# Available options
./infrastructure/disaster-recovery/backup.sh list
./infrastructure/disaster-recovery/backup.sh verify
./infrastructure/disaster-recovery/backup.sh cleanup
```

### Backup Verification

| Check | Frequency | Action |
|-------|-----------|--------|
| Database restore test | Weekly | Restore latest backup to staging environment |
| File integrity | Daily | Verify checksums of all backup files |
| DR simulation | Monthly | Full failover test to DR region |

---

## Disaster Scenarios

### Scenario 1: Single Service Failure

**Impact:** One service unavailable

**Detection:**
- Prometheus alert: `ServiceDown` (up == 0 for 1 min)
- CloudWatch alarm: ECS service unhealthy

**Response:**
1. Automatic: ECS/K8s restarts the container
2. If automatic fails: Manual rollback to previous version
3. Verify: Health check on /actuator/health

**RTO:** < 2 minutes | **RPO:** N/A

### Scenario 2: Database Failure

**Impact:** All persistence-dependent services unavailable

**Detection:**
- Prometheus alert: `DatabaseConnectionDown`
- CloudWatch alarm: RDS failover event

**Response:**
1. Automatic: RDS Multi-AZ failover (DNS update)
2. Verify: All services reconnect automatically (HikariCP retry)
3. If automatic fails: Manual failover via AWS CLI

```bash
aws rds failover-db-cluster --db-cluster-identifier sporekart-prod
```

**RTO:** < 60 seconds | **RPO:** < 5 seconds (WAL)

### Scenario 3: Full Region Outage

**Impact:** Complete loss of us-east-1

**Detection:**
- AWS Health Dashboard event
- Route53 health check failure
- External monitoring (Pingdom, Datadog)

**Response:**

```bash
# 1. Activate DR region (us-west-2)
cd infrastructure/terraform
terraform apply -var-file=dr.tfvars

# 2. Restore database from cross-region snapshot
aws rds restore-db-instance-from-db-snapshot \
  --db-instance-identifier sporekart-dr \
  --db-snapshot-identifier arn:aws:rds:us-west-2:...:snapshot/...

# 3. Promote read replica if available
aws rds promote-read-replica \
  --db-instance-identifier sporekart-dr

# 4. Update DNS failover
aws route53 change-resource-record-sets \
  --hosted-zone-id ZONE_ID \
  --change-batch file://dr-failover.json

# 5. Verify platform health
curl -s https://sporekart.com/actuator/health
```

**RTO:** < 4 hours | **RPO:** < 5 minutes

### Scenario 4: Data Corruption (Logical)

**Impact:** Incorrect data state (bad migration, bug, manual error)

**Detection:**
- Business logic validation
- Monitoring anomaly detection
- User/customer reports

**Response:**

```bash
# 1. Stop affected services
aws ecs update-service --cluster sporekart-prod \
  --service sporekart-api --desired-count 0

# 2. Restore database to point before corruption
aws rds restore-db-instance-to-point-in-time \
  --source-db-instance-identifier sporekart-prod \
  --target-db-instance-identifier sporekart-recovery \
  --restore-time "2026-07-24T10:00:00Z"

# 3. Verify data integrity
# 4. Point application to restored database
# 5. Resume services
```

**RTO:** < 1 hour | **RPO:** < 1 minute

### Scenario 5: Secrets Compromise

**Impact:** Unauthorized access to credentials

**Response:**
1. Rotate all compromised secrets immediately
2. Update AWS Secrets Manager
3. Restart affected services
4. Revoke any compromised API keys at provider
5. Audit access logs
6. Security incident report

---

## Recovery Testing

| Test | Frequency | Success Criteria |
|------|-----------|-----------------|
| Database restore | Weekly | Full data integrity check |
| Service rollback | Every deployment | Rollback to previous version succeeds |
| DR failover | Monthly | Full platform operational in DR region |
| Secrets rotation | Quarterly | All secrets rotated without service disruption |
| Backup restore | Monthly | Random backup restored and verified |

---

## Runbook

Full recovery procedures in `infrastructure/disaster-recovery/RUNBOOK.md`:

```
Scenario 1: Service Crash → Auto-recovery, rollback
Scenario 2: Database Down → Multi-AZ failover
Scenario 3: Region Outage → DR region activation
Scenario 4: Data Corruption → PITR restore
Scenario 5: Secrets Compromised → Rotate + restart
```

---

## Backup Infrastructure

```
┌──────────────────────────────────────────────────────────┐
│                Backup Pipeline                            │
│                                                           │
│  Daily 2330 UTC                                           │
│  ┌──────────────┐    ┌──────────────┐    ┌───────────┐  │
│  │ RDS Snapshot │───▶│ Env Backup   │───▶│ K8s/Infra │  │
│  │ (automated)  │    │ (encrypted)  │    │ (git)     │  │
│  └──────────────┘    └──────────────┘    └───────────┘  │
│         │                   │                   │         │
│         ▼                   ▼                   ▼         │
│  ┌──────────────────────────────────────────────────┐    │
│  │                    S3 Bucket                       │    │
│  │  s3://sporekart-backups/                          │    │
│  │  ├── database/ (RDS snapshots + WAL)              │    │
│  │  ├── config/ (encrypted env files)                │    │
│  │  └── infrastructure/ (terraform state snapshots)  │    │
│  └──────────────────────────────────────────────────┘    │
│         │                                                  │
│         ▼                                                  │
│  ┌──────────────────────────────────────────────────┐    │
│  │              Cross-Region Replication              │    │
│  │  us-east-1 ─────────────────────────────────▶ us-west-2 │
│  └──────────────────────────────────────────────────┘    │
└──────────────────────────────────────────────────────────┘
```
