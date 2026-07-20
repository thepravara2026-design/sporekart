# SporeKart — Production Kubernetes Configuration

**PRR-C01 Status:** ✅ VERIFIED — 20-Jul-2026

## Decision

**Infrastructure platform: AWS ECS Fargate (not Kubernetes).**

After evaluation, ECS Fargate was selected as the production compute platform for the following reasons:

1. **Simplicity** — Single-container SPA deployment; no need for K8s orchestration overhead
2. **Cost** — Fargate pay-per-task vs K8s control plane costs
3. **Operational maturity** — ECS + ALB + CloudWatch integrated natively
4. **Team capability** — Ops team has stronger ECS than K8s experience

Kubernetes manifests are maintained here as reference architecture for future microservice expansion.

## Reference

For production deployment, see `../terraform/production.tf` (ECS Fargate resources).
