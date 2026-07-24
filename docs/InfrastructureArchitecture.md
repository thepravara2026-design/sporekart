# Infrastructure Architecture

**SporeKart Enterprise Platform v2.0**  
**Document:** InfrastructureArchitecture.md  
**Last Updated:** 2026-07-24

---

## Overview

The SporeKart Enterprise Infrastructure Platform provides a highly available, secure, scalable, cloud-native foundation for all services. It is designed for zero single point of failure, automatic recovery, and support for Autonomous AI workloads.

---

## Architecture Principles

1. **Highly Available**: No single point of failure; multi-AZ by default
2. **Secure**: Defense-in-depth; zero trust network architecture
3. **Observable**: Every component emits health, metrics, and logs
4. **Self-Healing**: Automatic restart, recovery, and scaling
5. **Immutable**: Every deployment is a new, immutable artifact
6. **Cloud Native**: Designed for Kubernetes, containers, and cloud APIs

---

## Global Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                         CLOUD PLATFORM                               │
│                                                                      │
│  ┌──────────────────────────────────────────────────────────────┐   │
│  │                      Edge Layer                                │   │
│  │  CloudFront CDN → WAF → Rate Limiting → TLS Termination       │   │
│  └──────────────────────────────────────────────────────────────┘   │
│                              │                                       │
│  ┌──────────────────────────────────────────────────────────────┐   │
│  │                     Gateway Layer                              │   │
│  │  ALB → Ingress Controller (nginx) → Gateway Service           │   │
│  │  JWT Validation → Authorization → Rate Limiting → Routing    │   │
│  └──────────────────────────────────────────────────────────────┘   │
│                              │                                       │
│  ┌──────────────────────────────────────────────────────────────┐   │
│  │                     Service Layer                              │   │
│  │  ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐            │   │
│  │  │Identity │ │Catalog  │ │ Cart    │ │ Order   │ ...        │   │
│  │  └─────────┘ └─────────┘ └─────────┘ └─────────┘            │   │
│  │  ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐            │   │
│  │  │Payment  │ │Inventory│ │AI       │ │Gateway  │            │   │
│  │  └─────────┘ └─────────┘ └─────────┘ └─────────┘            │   │
│  └──────────────────────────────────────────────────────────────┘   │
│                              │                                       │
│  ┌──────────────────────────────────────────────────────────────┐   │
│  │                     Data Layer                                 │   │
│  │  RDS (PostgreSQL) → ElastiCache (Redis) → MSK (Kafka)        │   │
│  │  S3 (Assets) → ECR (Containers) → EFS (Shared Storage)      │   │
│  └──────────────────────────────────────────────────────────────┘   │
│                              │                                       │
│  ┌──────────────────────────────────────────────────────────────┐   │
│  │                   Observability Layer                          │   │
│  │  Prometheus → Grafana → Alertmanager → CloudWatch → PagerDuty │   │
│  │  ELK Stack → Sentry → OpenTelemetry                           │   │
│  └──────────────────────────────────────────────────────────────┘   │
│                              │                                       │
│  ┌──────────────────────────────────────────────────────────────┐   │
│  │               Platform Infrastructure                         │   │
│  │  VPC → Subnets → NAT → IAM → Secrets Manager → KMS          │   │
│  │  Route53 → CloudFront → WAF → ACM → CloudTrail              │   │
│  └──────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
```

---

## Infrastructure Components

### Compute

| Component | Platform | Replicas | Auto-scaling | Strategy |
|-----------|----------|----------|-------------|----------|
| Gateway Service | ECS Fargate / K8s | 2 | 2-10 | Rolling update |
| Identity Service | ECS Fargate / K8s | 2 | 2-6 | Rolling update |
| Business Services | ECS Fargate / K8s | 2 | 2-8 | Rolling update |
| AI Service | ECS Fargate / K8s | 2 | 2-20 | Rolling update (maxSurge=1) |
| Frontend | ECS Fargate / S3+CloudFront | 2 | 2-6 | Blue-green |

### Networking

| Layer | Technology | Inbound | Outbound |
|-------|-----------|---------|----------|
| CDN | CloudFront | 443 (HTTPS) | Internal |
| Load Balancer | ALB (NLB for K8s) | 443 (HTTPS), 80 (→443) | Service ports |
| Ingress | nginx-ingress | TLS termination | ClusterIP services |
| Internal | Private subnets | None | NAT Gateway for egress |
| Database | Private subnets | 5432 from service SGs only | None |

### Data

| Store | Technology | HA Strategy | Backup |
|-------|-----------|-------------|--------|
| Primary Database | RDS PostgreSQL | Multi-AZ | Daily + WAL (PITR) |
| Cache | ElastiCache Redis | Cluster mode | Snapshot |
| Events | MSK Kafka | 3 brokers, replication=2 | Tiered storage |
| Secrets | AWS Secrets Manager | Regional | Cross-region replication |
| Docker Images | ECR | Regional | Cross-region replication |

---

## High Availability Design

| Component | SPOF Elimination | RTO | RPO |
|-----------|-----------------|-----|-----|
| Compute (ECS/K8s) | Multi-AZ, auto-scaling, PDB | <2 min | N/A |
| Database (RDS) | Multi-AZ, automated failover | <1 min | <5 sec |
| Cache (Redis) | Cluster mode, replica nodes | <30 sec | <1 sec |
| Events (Kafka) | 3 brokers, acks=all | <1 min | N/A |
| CDN (CloudFront) | Global edge network | Instant | N/A |
| DNS (Route53) | Global, health-check routing | Instant | N/A |

---

## Infrastructure as Code

All infrastructure is defined in `infrastructure/terraform/`:

| File | Purpose |
|------|---------|
| `production.tf` | Main production infrastructure (VPC, ECS, ALB, IAM) |
| `staging.tf` | Staging environment (reduced capacity mirror) |
| `variables.tf` | Input variables |
| `outputs.tf` | Output values |
| `acm.tf` | SSL certificate management |
| `dns.tf` | Route53 zone and records |
| `cdn.tf` | CloudFront distribution |
| `waf.tf` | WAF web ACL |
| `monitoring.tf` | CloudWatch dashboards and alarms |

---

## Deployment Environments

| Environment | Infrastructure | URL | Auto-deploy |
|-------------|---------------|-----|-------------|
| Development | Local Docker Compose | localhost | Manual |
| Testing | ECS Fargate (shared) | testing.sporekart.com | CI on PR |
| Staging | ECS Fargate (reduced) | staging.sporekart.com | CI on sporetest |
| Production | ECS Fargate (full) | sporekart.com | Manual approval |

---

## Performance Targets

| Metric | Target | Measurement |
|--------|--------|-------------|
| Service startup time | <30 sec | Container start to liveness pass |
| Health check response | <100ms | /actuator/health |
| Infrastructure recovery | <60 sec | ALB health check to 200 |
| Container restart | Automatic | ECS/K8s auto-restart |
| Database failover | <60 sec | RDS Multi-AZ DNS update |
| Full region recovery | <4 hours | DR runbook execution |
