# SporeKart — Production Terraform Configuration

**PRR-C01 Status:** ✅ CLOSED — 20-Jul-2026

## Overview

AWS infrastructure for SporeKart production deployment, provisioned via Terraform.

## Resources

| Resource | Description |
|----------|-------------|
| VPC | `10.0.0.0/16` with public/private subnets across 2 AZs |
| ECS Fargate | Web app task with 512 CPU / 1024 MB memory, 2 desired count |
| ALB | Application Load Balancer with HTTPS (TLS 1.3) and HTTP→HTTPS redirect |
| Security Groups | ALB (443/80 ingress) + Web App (4173 from ALB only) |
| CloudWatch | Log group with 30-day retention |
| Secrets Manager | Supabase, Sentry, Stripe secrets |
| NAT Gateway | Outbound access for private subnets |
| IAM | ECS execution + task roles |

## Prerequisites

- AWS CLI configured with production credentials
- ECR repository with container image pushed
- ACM certificate for `*.sporekart.com` (ARN passed as variable)

## Usage

```bash
terraform init
terraform plan -var="container_registry=123456789012.dkr.ecr.us-east-1.amazonaws.com" -var="ssl_certificate_arn=arn:aws:acm:us-east-1:123456789012:certificate/xxx"
terraform apply
```

## Outputs

After apply, use `terraform output` to get:
- `alb_dns_name` — DNS endpoint for DNS CNAME record
- `ecs_cluster_id` — ECS cluster reference
- `cloudwatch_log_group` — Log group for monitoring
- `secrets_manager_arns` — ARNs for all secrets
