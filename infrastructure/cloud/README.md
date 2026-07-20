# SporeKart — Cloud Architecture

**PRR-C01 Status:** ✅ CLOSED — 20-Jul-2026

## Provider: AWS

| Service | Purpose | Configuration |
|---------|---------|---------------|
| ECS Fargate | Compute for web app | `../terraform/production.tf` |
| ALB | HTTPS load balancer | `../terraform/production.tf` |
| CloudWatch | Logging + monitoring | `../terraform/production.tf` |
| Secrets Manager | Production secrets | `../terraform/production.tf` |
| Route53 | DNS management | `../terraform/dns.tf` |
| CloudFront | CDN for static assets | `../terraform/cdn.tf` |
| ACM | SSL/TLS certificates | Via AWS Certificate Manager |

## Architecture

```
CloudFront CDN
    ↓
ALB (HTTPS - TLS 1.3)
    ↓
ECS Fargate (2 tasks, 512/1024)
    ↓
Supabase (managed Postgres + Auth)
```

## Deployment

See `../terraform/README.md` for provisioning instructions.
