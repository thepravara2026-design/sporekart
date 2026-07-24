# Infrastructure Security

**SporeKart Enterprise Platform v2.0**  
**Document:** InfrastructureSecurity.md  
**Last Updated:** 2026-07-24

---

## Security Philosophy

**Defense in depth** — No single layer is relied upon. Security is implemented at every level from the CDN edge to the application runtime.

---

## Security Layers

```
Layer 1: CDN + WAF          ─── CloudFront + AWS WAF
Layer 2: Network Perimeter   ─── ALB, Security Groups, VPC
Layer 3: Ingress Security    ─── TLS, HSTS, Rate Limiting, CORS
Layer 4: Identity & Auth     ─── JWT, OAuth2, RBAC, Session Management
Layer 5: Application Security ─── Input Validation, SQL Injection Prevention, XSS Protection
Layer 6: Data Security       ─── Encryption at Rest, RLS, Secrets Management
Layer 7: Runtime Security    ─── Non-root Containers, Read-only Filesystem, Seccomp
Layer 8: Audit & Monitoring  ─── CloudTrail, Audit Logs, Prometheus Alerts
```

---

## Layer 1: CDN + WAF

### CloudFront

| Feature | Configuration |
|---------|---------------|
| TLS | TLSv1.2_2023 (minimum 1.2) |
| HTTP version | HTTP/2 + HTTP/3 |
| Geo restriction | None (global) |
| WAF association | `sporekart-prod-waf` |

### WAF Rules

| Rule | Action | Priority |
|------|--------|----------|
| Rate limit (1000 req/5min per IP) | Block | 1 |
| AWS Common Rule Set (SQLi, XSS, LFI) | Block | 2 |
| AWS SQLi Rule Set | Block | 3 |
| IP blacklist | Block | 4 |

---

## Layer 2: Network Perimeter

### Security Groups

```
┌─────────────────────────────────────────────────────────┐
│ ALB Security Group                                       │
│ Ingress:                                                 │
│   HTTPS (443)  ─── 0.0.0.0/0                           │
│   HTTP (80)    ─── 0.0.0.0/0 (redirects to 443)        │
│ Egress:                                                  │
│   8080 (services) ─── Private subnet                    │
└─────────────────────────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────┐
│ ECS Service Security Group                               │
│ Ingress:                                                 │
│   8080 ─── ALB Security Group only                      │
│   8080 ─── Prometheus Security Group (metrics)          │
│ Egress:                                                  │
│   443 (external APIs)                                   │
│   5432 (RDS PostgreSQL)                                  │
│   6379 (ElastiCache Redis)                               │
│   9092 (MSK Kafka)                                       │
└─────────────────────────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────┐
│ RDS Security Group                                       │
│ Ingress:                                                 │
│   5432 ─── ECS Service Security Group only              │
└─────────────────────────────────────────────────────────┘
```

### VPC

| Feature | Configuration |
|---------|---------------|
| CIDR | 10.0.0.0/16 |
| Subnets | Public (2), Private (2) |
| NAT | 1 per AZ |
| Endpoints | S3, ECR, Secrets Manager, CloudWatch |
| Flow logs | Enabled (CloudWatch) |

---

## Layer 3: Ingress Security

### TLS Configuration

| Component | Min TLS Version | Cipher Preference | Certificate |
|-----------|----------------|-------------------|-------------|
| CloudFront | TLSv1.2_2023 | TLS_AES_128_GCM_SHA256 (preferred) | ACM (auto-renewed) |
| ALB | TLS 1.2 | ELBSecurityPolicy-TLS13-1-2-2021-06 | ACM (auto-renewed) |
| Internal services | TLS 1.2 | ECDHE-RSA-AES128-GCM-SHA256 | Internal CA |

### Security Headers (nginx)

```nginx
add_header X-XSS-Protection "1; mode=block";
add_header X-Content-Type-Options "nosniff";
add_header X-Frame-Options "DENY";
add_header Referrer-Policy "strict-origin-when-cross-origin";
add_header Permissions-Policy "camera=(), microphone=(), geolocation=()";
add_header Strict-Transport-Security "max-age=31536000; includeSubDomains; preload";
add_header Content-Security-Policy "default-src 'self'; ...";
```

### Rate Limiting

| Path | Rate | Burst | Response |
|------|------|-------|----------|
| /auth/* | 5 req/s | 10 | 429 Too Many Requests |
| /api/* | 100 req/s | 20 | 429 Too Many Requests |
| /* (fallback) | 200 req/s | 50 | 429 Too Many Requests |

---

## Layer 4: Identity & Auth

| Component | Implementation |
|-----------|---------------|
| Authentication | JWT (RS256), OAuth2 |
| Token validation | Gateway-level (ReactiveJwtDecoder) |
| Authorization | RBAC (6 roles), @PreAuthorize |
| Session management | Redis-backed, 15 min TTL |
| Token rotation | Refresh token rotation |
| Rate limiting | Auth endpoints: 5 req/s |

---

## Layer 5: Application Security

| Risk | Mitigation |
|------|-----------|
| SQL Injection | Parameterized queries only (no string concatenation) |
| XSS | Input sanitization, CSP headers, output encoding |
| CSRF | Disabled for REST APIs (JWT-based), enabled for cookies |
| Prompt injection | 12 detection patterns in AssistantSecurityService |
| PII leakage | Output sanitization (email, phone, SSN, credit card) |

---

## Layer 6: Data Security

| Store | Encryption at Rest | Encryption in Transit | Access Control |
|-------|-------------------|----------------------|----------------|
| RDS (PostgreSQL) | AES-256 (KMS) | TLS 1.2 | Security group + IAM + DB user |
| ElastiCache (Redis) | AES-256 | TLS 1.2 | Security group + AUTH token |
| MSK (Kafka) | AES-256 | TLS 1.2 | IAM + SCRAM |
| S3 | AES-256 (SSE-S3) | HTTPS | IAM + Bucket Policy |
| Secrets Manager | AES-256 (KMS) | HTTPS | IAM + Resource Policy |
| ECR | AES-256 | HTTPS | IAM + Repository Policy |

---

## Layer 7: Runtime Security

| Measure | Implementation |
|---------|---------------|
| Non-root user | `USER sporekart` in all Dockerfiles |
| Read-only root filesystem | `securityContext.readOnlyRootFilesystem: true` |
| Capability drop | `securityContext.capabilities.drop: ["ALL"]` |
| Seccomp | Default (runtime default) |
| AppArmor | `securityContext.appArmorProfile.type: RuntimeDefault` |
| Pod security | `PodSecurityStandard: restricted` |
| Network isolation | NetworkPolicies (deny-all-ingress by default) |

### Kubernetes Pod Security Context

```yaml
securityContext:
  runAsNonRoot: true
  runAsUser: 1000
  runAsGroup: 1000
  fsGroup: 1000
  seccompProfile:
    type: RuntimeDefault
  capabilities:
    drop: ["ALL"]
  readOnlyRootFilesystem: true
  allowPrivilegeEscalation: false
```

---

## Layer 8: Audit & Monitoring

| Source | Tool | Retention | Alerting |
|--------|------|-----------|----------|
| AWS API calls | CloudTrail | 90 days (S3) + 1 year (Glacier) | WAF changes, IAM changes |
| Application logs | CloudWatch Logs | 30 days | Error rate > 5% |
| Security events | SecurityHub | 90 days | CRITICAL/HIGH findings |
| Authentication events | Application audit logs | 90 days | Failed login rate > 10/min |
| Infrastructure changes | Terraform state | Indefinite (S3) | Drift detection |

---

## Secrets Management

### Architecture

```
┌─────────────────────────┐
│     AWS Secrets Manager  │
│  (primary secret store)  │
└────────┬────────────────┘
         │
         ▼
┌─────────────────────────┐
│  HashiCorp Vault         │
│  (dynamic secrets,       │
│   rotation, audit trails)│
└────────┬────────────────┘
         │
         ▼
┌─────────────────────────┐
│  Kubernetes Secrets      │
│  (Vault Agent Injector)  │
└─────────────────────────┘
```

### Secret Rotation Schedule

| Secret | Rotation Period | Method | Requires Restart? |
|--------|----------------|--------|-------------------|
| JWT Signing Key | 30 days | Automated rotation | Yes (rolling restart) |
| Database Password | 90 days | Automated + app restart | Yes (rolling) |
| API Keys (AI) | 30 days | Manual at provider | Yes (pod refresh) |
| SMTP Credentials | 90 days | Manual | No (runtime env) |
| Payment Gateway | 90 days | Manual | Yes (pod refresh) |
| Supabase Keys | 90 days | Manual | No (runtime env) |

---

## Compliance Mapping

| Standard | Requirement | Implementation |
|----------|-------------|---------------|
| OWASP Top 10 | A01:2021-Broken Access Control | RBAC + JWT validation |
| OWASP Top 10 | A03:2021-Injection | Parameterized queries + WAF |
| OWASP Top 10 | A04:2021-Insecure Design | Security review in CI |
| OWASP Top 10 | A05:2021-Security Misconfiguration | Terraform + config lint |
| OWASP Top 10 | A07:2021-Identification Failures | JWT + OAuth2 |
| SOC2 | Access Control | IAM + Least Privilege |
| SOC2 | Encryption | TLS everywhere + AES-256 |
| SOC2 | Monitoring | CloudTrail + Prometheus |
| SOC2 | Incident Response | PagerDuty + Runbook |
| GDPR | Data Protection | Encryption + Access Controls |
