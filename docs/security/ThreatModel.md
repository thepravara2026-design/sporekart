# Threat Model

## Methodology

STRIDE per-service, focused on external-facing and AI attack surfaces.

## Threat Matrix

| Threat                   | Vector                  | Impact         | Likelihood | Mitigation                                      |
|--------------------------|-------------------------|----------------|------------|-------------------------------------------------|
| Credential theft         | Hardcoded secrets, env leakage | Full compromise | Low        | Env vars only, fail-fast, no defaults, `.env` excluded |
| JWT forgery              | `alg: none`, weak key   | Authentication bypass | Low  | HMAC-SHA256, 32B min key, NimbusReactiveJwtDecoder |
| Token theft (XSS)        | Stolen access token     | Account takeover | Medium   | 15-min expiry, refresh rotation, CSP `self`    |
| Token replay             | Captured JWT replayed   | Unauthorized access | Medium | `jti` uniqueness, short expiry, refresh invalidation |
| Prompt injection         | Crafted AI prompt       | Data leakage, policy bypass | High | 12-pattern detection, semantic analysis, input sanitization |
| PII leakage (AI)         | AI reveals user PII     | Regulatory violation | High | Automated redaction (email, phone, SSN, CC, bank, PAN, Aadhaar) |
| Denial of Service        | High-volume requests    | Service unavailability | Medium | 3-layer rate limiting (Nginx, Gateway, AI Service) |
| SQL injection            | Malformed query params  | Data exfiltration | Low | Parameterized queries, Bean Validation, sanitization |
| CORS abuse               | Unauthorized origin     | Cross-origin data theft | Low | Dynamic origin reflection, restricted whitelist |
| Supply chain attack      | Compromised dependency  | Remote code execution | Low | Gradle checksums, dependency audit, SBOM |
| SSRF (AI service)        | Model endpoint redirect | Internal network access | Low | URL validation, allow-list, network segmentation |
| Request tampering        | oversized payload       | Buffer overflow / memory exhaustion | Low | Size limits at Gateway (10MB / 100KB AI) |

## Data Flow Threats

### Authentication Flow

```
Client → Nginx → Gateway → Identity Service → DB
  │        │         │            │              │
  │        │         │            │    SQLi: mitigated (param queries)
  │        │         │            │    Cred theft: mitigated (env vars)
  │        │         │            └──────────────┘
  │        │         │  JWT forgery: mitigated (HMAC-SHA256)
  │        │         └───────────────────────────┘
  │        │  MITM: mitigated (TLS)
  └────────┘────────────────────────────────────┘
```

### AI Inference Flow

```
Client → Nginx → Gateway → AI Service → LLM API
  │        │         │           │            │
  │        │         │           │   SSRF: mitigated (allow-list)
  │        │         │           │   PII leak: mitigated (redaction)
  │        │         │           └────────────┘
  │        │         │  DoS: mitigated (rate limit)
  │        │         └─────────────────────────┘
  │        │  Prompt injection: mitigated (6 services)
  └────────┘───────────────────────────────────┘
```

## Assumptions & Dependencies

- TLS termination at Nginx is correctly configured with valid certificates
- `JWT_SECRET` is generated with sufficient entropy and protected at rest
- AWS Secrets Manager has appropriate IAM policies restricting access
- Network segmentation prevents direct access to databases from public routes