# SporeKart — Helm Charts

**PRR-C01 Status:** ✅ VERIFIED — 20-Jul-2026

## Decision

**Helm not used for current production deployment.**

The production infrastructure uses AWS ECS Fargate (Terraform-managed), which does not use Helm. Helm charts will be created when Kubernetes-based microservices are deployed in future phases.

## Related

- Production infrastructure: `../terraform/production.tf`
- Container deployment: `../../docker/docker-compose.yml` (local/staging)
