# Deployment Topology

**SporeKart Enterprise Platform v2.0**  
**Document:** DeploymentTopology.md  
**Last Updated:** 2026-07-24

---

## Environment Topology

```
┌─────────────────────────────────────────────────────────────────────┐
│                        PRODUCTION                                    │
│                                                                      │
│  sporekart.com                                                       │
│  ├── CloudFront (CDN + WAF)                                         │
│  ├── ALB (HTTPS, TLS 1.3)                                           │
│  ├── ECS Fargate (min 2, max 20 per service)                        │
│  │   ├── gateway-service (8080)                                     │
│  │   ├── identity-service (8080)                                    │
│  │   ├── catalog-service (8080)                                     │
│  │   ├── cart-service (8080)                                        │
│  │   ├── order-service (8080)                                       │
│  │   ├── payment-service (8080)                                     │
│  │   ├── notification-service (8080)                                │
│  │   ├── inventory-service (8080)                                   │
│  │   ├── fulfillment-service (8080)                                 │
│  │   ├── ai-service (8080)                                          │
│  │   ├── training-service (8080)                                    │
│  │   ├── admin-service (8080)                                       │
│  │   ├── analytics-service (8080)                                   │
│  │   ├── search-service (8080)                                      │
│  │   ├── content-service (8080)                                     │
│  │   ├── risk-service (8080)                                        │
│  │   └── support-service (8080)                                     │
│  ├── RDS PostgreSQL (Multi-AZ)                                      │
│  ├── ElastiCache Redis (Cluster)                                    │
│  ├── MSK Kafka (3 brokers)                                          │
│  └── S3 (assets, backups)                                           │
│                                                                      │
│  DR Region: us-west-2                                               │
│  └── RDS replica + passive ECS cluster                              │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────┐
│                         STAGING                                      │
│                                                                      │
│  staging.sporekart.com                                               │
│  ├── ALB (HTTPS)                                                    │
│  ├── ECS Fargate (min 1 replica)                                    │
│  │   └── Same services as production (reduced resources)            │
│  ├── RDS PostgreSQL (Single-AZ)                                     │
│  └── ElastiCache Redis (Single-node)                                │
│                                                                      │
│  CI trigger: Every push to sporetest branch                         │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────┐
│                      DEVELOPMENT                                     │
│                                                                      │
│  localhost (Docker Compose)                                          │
│  ├── nginx reverse proxy (443 → services)                           │
│  ├── web-app (4173)                                                 │
│  ├── H2 in-memory database                                          │
│  └── Redis (alpine)                                                  │
│                                                                      │
│  CI trigger: Every PR to sporetest/main                              │
└─────────────────────────────────────────────────────────────────────┘
```

---

## Service Discovery

### Internal DNS (Kubernetes)

```
Service                    DNS Name                              Port
gateway-service            gateway-service.sporekart-platform     8080
identity-service           identity-service.sporekart-platform    8080
catalog-service            catalog-service.sporekart-services     8080
cart-service               cart-service.sporekart-services        8080
order-service              order-service.sporekart-services       8080
payment-service            payment-service.sporekart-services     8080
ai-service                 ai-service.sporekart-ai                8080
notification-service       notification-service.sporekart-services 8080
```

### ECS Service Discovery (AWS Cloud Map)

```
Service                    DNS Name
gateway-service            gateway-service.sporekart.local
identity-service           identity-service.sporekart.local
catalog-service            catalog-service.sporekart.local
```

---

## Port Mapping

| Service | Container Port | Host Port (dev) | Health Check Path |
|---------|---------------|-----------------|-------------------|
| gateway-service | 8080 | 8080 | /actuator/health |
| identity-service | 8080 | 8081 | /actuator/health |
| catalog-service | 8080 | 8082 | /actuator/health |
| cart-service | 8080 | 8083 | /actuator/health |
| order-service | 8080 | 8084 | /actuator/health |
| payment-service | 8080 | 8085 | /actuator/health |
| inventory-service | 8080 | 8086 | /actuator/health |
| notification-service | 8080 | 8087 | /actuator/health |
| fulfillment-service | 8080 | 8088 | /actuator/health |
| ai-service | 8080 | 8089 | /actuator/health |
| training-service | 8080 | 8090 | /actuator/health |
| admin-service | 8080 | 8091 | /actuator/health |
| analytics-service | 8080 | 8092 | /actuator/health |
| search-service | 8080 | 8093 | /actuator/health |
| content-service | 8080 | 8094 | /actuator/health |
| risk-service | 8080 | 8095 | /actuator/health |
| support-service | 8080 | 8096 | /actuator/health |
| nginx | 443/80 | 443/80 | /health |
| redis | 6379 | 6379 | PING |
| kafka | 9092 | 9092 | Broker health |

---

## Ingress Routing

```
Host                          Path                          Service
sporekart.com                 / (static assets)             CloudFront → S3
api.sporekart.com             /                             gateway-service
api.sporekart.com             /auth/                        identity-service
api.sporekart.com             /api/catalog                  gateway → catalog-service
api.sporekart.com             /api/cart                     gateway → cart-service
api.sporekart.com             /api/orders                   gateway → order-service
api.sporekart.com             /api/payments                 gateway → payment-service
api.sporekart.com             /api/ai                       gateway → ai-service
identity.sporekart.com        /                             identity-service
health.sporekart.com          /                             gateway-service
staging.sporekart.com         /                             Staging ALB
```
