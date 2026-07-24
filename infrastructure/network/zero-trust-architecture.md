# SporeKart Enterprise Zero Trust Network Architecture
# Defense-in-depth: layers from edge to application

## Network Layers

```
Internet
    │
    ▼
┌─────────────────────────────────────────┐
│ Layer 1: CloudFront / CDN               │
│   - WAF rate limiting (1000 req/5min)   │
│   - TLS 1.3 termination                 │
│   - DDoS protection (AWS Shield)        │
└─────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────┐
│ Layer 2: Application Load Balancer      │
│   - HTTPS only (port 443 → 80 redirect) │
│   - TLS 1.2 minimum                     │
│   - Security group: 443 from CloudFront │
└─────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────┐
│ Layer 3: Ingress Controller (nginx)     │
│   - TLS termination (cert-manager)      │
│   - Rate limiting (100 req/s per IP)    │
│   - CORS validation                     │
│   - Security headers                    │
│   - Request size limits (10MB)          │
└─────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────┐
│ Layer 4: Service Mesh / Network Policy  │
│   - Namespace isolation                 │
│   - deny-all-ingress by default         │
│   - Allow only gateway→services         │
│   - Allow only prometheus→/metrics      │
│   - No direct external access to pods   │
└─────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────┐
│ Layer 5: Application Security           │
│   - JWT authentication (gateway)        │
│   - Role-based authorization            │
│   - Input validation                     │
│   - Parameterized queries (no SQLi)     │
│   - Output sanitization (XSS prevention) │
└─────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────┐
│ Layer 6: Data Security                  │
│   - Encrypted at rest (RDS encryption)  │
│   - Encrypted in transit (TLS)          │
│   - Secrets in Vault / AWS Secrets Mgr  │
│   - Database RLS (Row Level Security)   │
└─────────────────────────────────────────┘
```

## Network Policy Rules

| From | To | Port | Protocol | Purpose |
|------|----|------|----------|---------|
| ingress-nginx | gateway-service | 8080 | TCP | External traffic |
| gateway-service | identity-service | 8080 | TCP | Auth requests |
| gateway-service | catalog-service | 8080 | TCP | Catalog queries |
| gateway-service | cart-service | 8080 | TCP | Cart operations |
| gateway-service | order-service | 8080 | TCP | Order management |
| gateway-service | payment-service | 8080 | TCP | Payment processing |
| gateway-service | ai-service | 8080 | TCP | AI requests |
| prometheus | all services | 8080 | TCP | Metrics scraping |
| kafka | ai-service | 9092 | TCP | Event streaming |
| redis | identity-service | 6379 | TCP | Session cache |

## Security Groups (AWS Equivalent)

```
┌─────────────────────────────────────┐
│ ALB Security Group                  │
│ Ingress: 443 (0.0.0.0/0)           │
│ Ingress: 80 (0.0.0.0/0 → redirect) │
│ Egress: 8080 (service SGs)         │
└─────────────────────────────────────┘
           │
           ▼
┌─────────────────────────────────────┐
│ ECS Service Security Group          │
│ Ingress: 8080 (ALB SG only)         │
│ Ingress: 8080 (prometheus SG only)  │
│ Egress: 443 (0.0.0.0/0)            │
│ Egress: 5432 (RDS SG)              │
│ Egress: 6379 (ElastiCache SG)      │
└─────────────────────────────────────┘
           │
           ▼
┌─────────────────────────────────────┐
│ RDS Security Group                  │
│ Ingress: 5432 (ECS SG only)         │
└─────────────────────────────────────┘
```

## TLS Configuration

| Component | Minimum TLS | Preferred Cipher | Certificate Source |
|-----------|-------------|-----------------|-------------------|
| CloudFront | TLSv1.2_2023 | TLS_AES_128_GCM_SHA256 | ACM us-east-1 |
| ALB | TLS 1.2 | ELBSecurityPolicy-TLS13-1-2-2021-06 | ACM |
| Ingress | TLS 1.2 | ECDHE-RSA-AES128-GCM-SHA256 | cert-manager / Let's Encrypt |
| Internal | TLS 1.2 | ECDHE-RSA-AES128-GCM-SHA256 | Internal CA |

## Secrets Access Control

| Secret | Accessors | Rotation | Storage |
|--------|-----------|----------|---------|
| JWT Secret | identity-service only | 30 days | Vault + Secrets Manager |
| Database passwords | service + Flyway only | 90 days | Vault + Secrets Manager |
| API keys (AI) | ai-service only | 30 days | Vault + Secrets Manager |
| SMTP credentials | notification-service only | 90 days | Vault + Secrets Manager |
| Payment keys | payment-service only | 90 days | Vault + Secrets Manager |
| OAuth credentials | identity-service only | 90 days | Vault + Secrets Manager |
