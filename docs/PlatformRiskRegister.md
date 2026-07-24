# SporeKart Enterprise Platform — Platform Risk Register

## Executive Risk Summary

| Risk Level | Count | Status |
|------------|-------|--------|
| Critical | 0 | ✓ None |
| High | 2 | ⚠ Active (Conditions) |
| Medium | 2 | ⚠ Active |
| Low | 3 | ✓ Monitored |
| **Total** | **7** | |

---

## High Risks (Must Remediate in Phase 14 Sprint 1)

### H-01: AI Content Safety and Agent Authorization Stubs

| Field | Value |
|-------|-------|
| **Risk ID** | H-01 |
| **Domain** | AI Security |
| **Status** | Active (Certification Condition) |
| **Description** | `ContentSecurityService.containsPii()` and `containsProfanity()` return false (never detects). `AgentSecurityService` and `MemorySecurityService` authorization methods all return true (no enforcement). |
| **Impact** | PII can be exposed through AI responses. Unauthorized users can execute agents and access memory. |
| **Likelihood** | Medium (requires crafted input to exploit) |
| **Business Risk** | GDPR/PCAOB compliance violation, reputational damage |
| **Technical Risk** | Data leakage through AI responses |
| **Operational Risk** | Compliance audit failure |
| **Recommended Action** | Implement real PII/profanity detection (regex/NLP). Implement real authorization in Agent/Memory services. |
| **Effort** | 3 days |
| **Owner** | AI Platform Team |
| **Due** | Phase 14 Sprint 1 |

### H-02: No CI Security Scanning

| Field | Value |
|-------|-------|
| **Risk ID** | H-02 |
| **Domain** | Security/CI |
| **Status** | Active (Certification Condition) |
| **Description** | The CI security scanning pipeline (`ci/pipelines/security-scan.md`) is a 3-line placeholder. No Snyk, Trivy, Dependency-check, or OWASP plugin is integrated into CI. |
| **Impact** | Vulnerable dependencies can be introduced without detection. No automated vulnerability scanning. |
| **Likelihood** | High (without automation, vulnerabilities will be introduced) |
| **Business Risk** | Security breach via unpatched dependency |
| **Technical Risk** | Accumulated dependency vulnerabilities |
| **Operational Risk** | Incident response to preventable vulnerabilities |
| **Recommended Action** | Integrate `org.owasp:dependency-check-maven` or Trivy/Snyk into CI build workflow. Fail build on critical/high CVEs. |
| **Effort** | 2 days |
| **Owner** | Platform Team |
| **Due** | Phase 14 Sprint 1 |

---

## Medium Risks (Phase 14 Sprint 2-3)

### M-01: SMTP Failover Not Configured

| Risk ID | M-01 |
|---------|-------|
| **Domain** | Infrastructure |
| **Status** | Active |
| **Description** | No secondary SMTP provider configured. Primary SMTP failure causes email delivery outage. |
| **Impact** | Transactional emails (order confirmations, password resets) not delivered during SMTP outage |
| **Likelihood** | Low (SMTP rarely fails) |
| **Mitigation** | Configure secondary SMTP with auto-failover |
| **Owner** | Platform Team |
| **Due** | Phase 14 Sprint 2 |

### M-02: Multi-AZ Deployment Not Verified

| Risk ID | M-02 |
|---------|-------|
| **Domain** | Infrastructure |
| **Status** | Monitoring |
| **Description** | ECS Fargate tasks deployed across 2 AZs in Terraform, but not all services verified to span AZs with podAntiAffinity. |
| **Impact** | Reduced fault tolerance during AZ failure |
| **Likelihood** | Low (K8s not used, ECS manages placement) |
| **Mitigation** | Verify ECS service placement across AZs, test AZ failure scenario |
| **Owner** | Platform Team |
| **Due** | Phase 14 Sprint 2 |

---

## Low Risks (Phase 14 Sprint 3+)

### L-01: Performance Baseline Not in CI

| Risk ID | L-01 |
|---------|-------|
| **Domain** | Performance |
| **Status** | Monitoring |
| **Description** | No automated performance regression detection in CI. Manual comparison only. |
| **Mitigation** | Add k6/Locust test suite with baseline comparison in CI |
| **Owner** | Performance Team |
| **Due** | Phase 14 Sprint 3 |

### L-02: AI Provider Single-Region Dependency

| Risk ID | L-02 |
|---------|-------|
| **Domain** | AI Platform |
| **Status** | Monitoring |
| **Description** | AI providers deployed in single region. Region outage impacts AI platform. |
| **Mitigation** | Configure multi-region provider routing (architecture designed, implementation pending) |
| **Owner** | AI Team |
| **Due** | Phase 14 Sprint 3 |

### L-03: Event Backbone Consumer Adoption

| Risk ID | L-03 |
|---------|-------|
| **Domain** | Architecture |
| **Status** | Improving |
| **Description** | Event backbone has Kafka infrastructure but limited consumer adoption across services. |
| **Mitigation** | Implement event consumers for key event types in Phase 14 |
| **Owner** | Platform Team |
| **Due** | Phase 14 Sprint 3 |

---

## Remediated Risks

### R-001: SMTP Credentials in Git History (SEC-010) — ✓ RESOLVED 2026-07-24

| Field | Value |
|-------|-------|
| **Original Severity** | HIGH |
| **Resolution** | Credentials redacted from git history on affected feature branches. Force push applied. PRR docs updated to GO verdict. |
| **Residual** | Manual rotation of Gmail app password still recommended at provider |

---

## Risk Matrix

```
Likelihood
    ↑
  High    │                   H-02
          │
  Med     │  H-01
          │              M-01
  Low     │  L-02  L-03  L-01  M-02
          │
          └──────────────────────────→
          Low    Med    High   Critical
                     Impact
```

---

## Risk Governance

| Review Cycle | Frequency | Participants |
|-------------|-----------|-------------|
| Daily | Ongoing | On-call SRE |
| Weekly | Every Monday | Engineering team |
| Monthly | Every 1st | Platform leadership |
| Quarterly | Every quarter | Architecture committee |
