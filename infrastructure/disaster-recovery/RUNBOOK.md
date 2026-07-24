# SporeKart Disaster Recovery Runbook
# Quick reference for recovery procedures

RTO_TARGET="< 1 hour"
RPO_TARGET="< 5 minutes"

## Recovery Scenarios

### Scenario 1: Service Crash
1. Auto-recovery: Kubernetes/ECS restarts container
2. Manual: ./scripts/rollback.sh production v{last-stable-tag}
3. Verify: health check + smoke test

### Scenario 2: Database Corruption
1. Latest automated backup: ./infrastructure/disaster-recovery/backup.sh restore
2. Point-in-time: RDS PITR to 5 min before incident
3. Verify: data integrity checks + application smoke tests

### Scenario 3: Full Region Outage
1. Activate DR region (us-west-2)
2. Restore RDS snapshot from cross-region replication
3. Deploy infrastructure: cd infrastructure/terraform && terraform apply -var-file=dr.tfvars
4. Update DNS: Route53 failover to DR region
5. Verify: full platform health check

### Scenario 4: Secrets Compromise
1. Rotate compromised secrets immediately
2. Update secrets in AWS Secrets Manager / Vault
3. Update K8s secrets: kubectl apply -f infrastructure/kubernetes/base/secrets.yaml
4. Restart affected services: kubectl rollout restart deployment -n sporekart-platform
5. Rotate credentials at the provider level (SMTP, API keys, etc.)

### Scenario 5: Infrastructure State Corruption
1. terraform plan to detect drift
2. terraform apply to restore from state file
3. If state corrupted: restore from backup in /mnt/backups/sporekart/
4. Verify: terraform plan shows no changes

## Key Contacts
- SRE Lead: [on-call]
- Database Admin: [on-call]
- Security Lead: [on-call]
- DevOps Lead: [on-call]
