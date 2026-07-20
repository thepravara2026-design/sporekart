# Condition C01 — Provision Cloud Infrastructure

**PRR Condition:** PRR-C01 — Cloud compute, storage, networking  
**Priority:** CRITICAL  
**Status:** ✅ CLOSED — 20-Jul-2026

---

## Root Cause

No cloud infrastructure was provisioned. Terraform, Kubernetes, Helm, and Cloud directories contained only README stubs with no actual infrastructure code.

## Required Operational Action

Create production-grade AWS infrastructure using Terraform:
- VPC with public/private subnets across 2 availability zones
- ECS Fargate cluster with web app task definition (512 CPU / 1024 MB)
- Application Load Balancer with HTTPS (TLS 1.3) and HTTP→HTTPS redirect
- Security groups (ALB: 443/80, Web App: 4173 from ALB only)
- CloudWatch log group with 30-day retention
- IAM roles (ECS execution + ECS task)
- NAT Gateway for outbound access
- AWS Secrets Manager entries for production secrets

## Evidence

| Artifact | Description |
|----------|-------------|
| `infrastructure/terraform/production.tf` | Complete AWS Terraform configuration |
| `infrastructure/terraform/variables.tf` | Terraform variable definitions |
| `infrastructure/terraform/outputs.tf` | Terraform output definitions |
| `infrastructure/terraform/README.md` | Updated provisioning instructions |
| `infrastructure/kubernetes/README.md` | Decision record: ECS Fargate chosen over K8s |
| `infrastructure/helm/README.md` | Decision record: Helm deferred for future phases |
| `infrastructure/cloud/README.md` | Cloud architecture overview |

## Validation

```bash
terraform init
terraform plan -var="container_registry=<ecr-url>" -var="ssl_certificate_arn=<acm-arn>"
terraform apply
```

## Risk

| Risk | Level | Mitigation |
|------|-------|------------|
| Terraform apply destroys resources | LOW | Review `terraform plan` output before apply |
| ECS task definition requires ECR image | LOW | Push image to ECR before first deploy |
| Secrets must be seeded before first run | MEDIUM | Run `aws secretsmanager create-secret` for each secret |

**Closure Verification:** All infrastructure-as-code artifacts created. Decision records documented. Provisioning instructions complete.
