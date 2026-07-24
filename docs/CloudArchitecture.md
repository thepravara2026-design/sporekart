# Cloud Architecture

**SporeKart Enterprise Platform v2.0**  
**Document:** CloudArchitecture.md  
**Last Updated:** 2026-07-24

---

## Cloud Provider: Amazon Web Services (AWS)

SporeKart is deployed on AWS with multi-region readiness. The architecture is cloud-agnostic where possible — Kubernetes manifests and containerization ensure portability to GCP, Azure, or on-premise.

---

## Service per Cloud Provider

| Service | Primary (AWS) | Alternative |
|---------|--------------|-------------|
| Compute | ECS Fargate | GCP Cloud Run, Azure Container Apps |
| Container Registry | ECR | GCR, ACR, Docker Hub |
| Database | RDS PostgreSQL | Cloud SQL, Azure Database |
| Cache | ElastiCache Redis | Memorystore, Azure Cache |
| DNS | Route53 | Cloud DNS, Azure DNS |
| CDN | CloudFront | Cloud CDN, Azure CDN |
| WAF | AWS WAF | Cloud Armor, Azure WAF |
| Monitoring | CloudWatch + Prometheus | Cloud Monitoring, Azure Monitor |
| Secrets | AWS Secrets Manager | Secret Manager, Key Vault |
| IAM | AWS IAM | Cloud IAM, Azure AD |

---

## AWS Resource Topology

### Region: us-east-1 (Primary)

```
Availability Zone A              Availability Zone B
┌─────────────────────┐         ┌─────────────────────┐
│ Public Subnet       │         │ Public Subnet       │
│ 10.0.1.0/24         │         │ 10.0.2.0/24         │
│ ┌──────────────┐    │         │ ┌──────────────┐    │
│ │ NAT Gateway  │    │         │ │ NAT Gateway  │    │
│ │ (AZ A)       │    │         │ │ (AZ B)       │    │
│ └──────────────┘    │         │ └──────────────┘    │
└─────────────────────┘         └─────────────────────┘

Availability Zone A              Availability Zone B
┌─────────────────────┐         ┌─────────────────────┐
│ Private Subnet      │         │ Private Subnet      │
│ 10.0.10.0/24        │         │ 10.0.11.0/24        │
│ ┌─────────────────┐ │         │ ┌─────────────────┐ │
│ │ ECS Tasks       │ │         │ │ ECS Tasks       │ │
│ │ (app-a)         │ │         │ │ (app-b)         │ │
│ └─────────────────┘ │         │ └─────────────────┘ │
│ ┌─────────────────┐ │         │ ┌─────────────────┐ │
│ │ RDS Primary     │ │         │ │ RDS Standby     │ │
│ └─────────────────┘ │         │ └─────────────────┘ │
└─────────────────────┘         └─────────────────────┘
```

### DR Region: us-west-2

Replica of primary region with:
- RDS cross-region read replica
- ECR cross-region replication
- S3 cross-region replication
- Route53 health-check failover

---

## Networking

### VPC Configuration

| Resource | Value |
|----------|-------|
| VPC CIDR | 10.0.0.0/16 |
| Public Subnets | 10.0.1.0/24 (us-east-1a), 10.0.2.0/24 (us-east-1b) |
| Private Subnets | 10.0.10.0/24 (us-east-1a), 10.0.11.0/24 (us-east-1b) |
| NAT Gateways | 1 per AZ (high availability) |
| VPC Endpoints | S3, ECR, Secrets Manager, CloudWatch |

### DNS Architecture

```
sporekart.com ──── Route53 ──── CloudFront ──── ALB ──── Services
                                     │
                              WAF Rate Limiting
```

---

## Cost Optimization

| Strategy | Savings | Implementation |
|----------|---------|---------------|
| Fargate (vs EC2) | ~30% | No node management overhead |
| Graviton (ARM) | ~20% | Use arm64 Docker images |
| Reserved Instances (RDS) | ~40% | 1-year commitment for DB |
| CloudFront (vs ALB directly) | ~50% on data transfer | Edge caching reduces origin load |
| Auto-scaling | Variable | Scale to zero (staging), min 2 (prod) |
| Spot instances | ~70% | Spot Fargate for non-critical services |

---

## Infrastructure Security

### IAM Roles

| Role | Trust | Permissions |
|------|-------|-------------|
| ECS Execution Role | ECS | Pull images, create logs, get secrets |
| ECS Task Role | ECS Tasks | Application-level AWS access |
| CI/CD Role | GitHub Actions | Deploy ECS, update services |
| Backup Role | Backup service | RDS snapshots, S3 uploads |

### Encryption

| Layer | Encryption |
|-------|-----------|
| In transit (external) | TLS 1.3 (CloudFront + ALB) |
| In transit (internal) | TLS 1.2 (service-to-service) |
| At rest (RDS) | AES-256 (AWS KMS) |
| At rest (S3) | AES-256 (SSE-S3) |
| At rest (EBS) | AES-256 (AWS KMS) |
| Secrets | AES-256 (AWS KMS) |
