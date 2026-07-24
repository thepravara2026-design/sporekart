# SporeKart Production Risk Register

## Overview

This document catalogs all identified risks to production deployment, their severity, mitigation strategies, and owners.

## Risk Summary

| Risk Level | Count | Status |
|------------|-------|--------|
| Critical | 0 | ✓ None |
| High | 0 | ✓ None |
| Medium | 2 | ⚠ Active |
| Low | 3 | ✓ Monitored |
| **Total** | **5** | |

## Risk Register

### ~~R001: SMTP Credentials in Git History~~ ✓ RESOLVED

| Field | Value |
|-------|-------|
| **ID** | R001 |
| **Severity** | HIGH → ✓ RESOLVED |
| **Domain** | Security |
| **Status** | Closed |
| **Description** | SMTP credentials committed to git repository history |
| **Impact** | Exposure of email delivery credentials, potential phishing abuse |
| **Resolution** | Credentials redacted from git history on affected branches (stabilization-certification, persistence-stabilization) via git commit --amend and force push. Manual rotation of Gmail app password still recommended at provider. |
| **Closure Date** | 2026-07-24 |
| **Owner** | Security Team |

### R002: SMTP Failover Not Configured

| Field | Value |
|-------|-------|
| **ID** | R002 |
| **Severity** | MEDIUM |
| **Domain** | Infrastructure |
| **Status** | Open |
| **Description** | Secondary SMTP provider not configured for failover |
| **Impact** | Email delivery outage during primary SMTP failure |
| **Likelihood** | Low (SMTP rarely fails) |
| **Mitigation** | Configure secondary SMTP with auto-failover |
| **Owner** | Platform Team |
| **Due** | Sprint 2 |
| **Trend** | → Unchanged |

### R003: Multi-AZ Deployment Coverage

| Field | Value |
|-------|-------|
| **ID** | R003 |
| **Severity** | MEDIUM |
| **Domain** | Infrastructure |
| **Status** | Monitoring |
| **Description** | Not all services verified to span 3 availability zones |
| **Impact** | Reduced fault tolerance during AZ failure |
| **Likelihood** | Low (K8s spreads pods) |
| **Mitigation** | Deploy with podAntiAffinity across AZs |
| **Owner** | Platform Team |
| **Due** | Sprint 3 |
| **Trend** | → Monitored |

### R004: Performance Baseline Not in CI

| Field | Value |
|-------|-------|
| **ID** | R004 |
| **Severity** | LOW |
| **Domain** | Performance |
| **Status** | Monitoring |
| **Description** | Performance baseline comparison not yet integrated into CI gate |
| **Impact** | Performance regression may go undetected in CI |
| **Likelihood** | Medium (manual comparison needed) |
| **Mitigation** | Run full benchmark suite against baseline |
| **Owner** | Performance Team |
| **Due** | Sprint 3 |
| **Trend** | → Improving |

### R005: AI Provider Single-Region Dependency

| Field | Value |
|-------|-------|
| **ID** | R005 |
| **Severity** | LOW |
| **Domain** | AI Platform |
| **Status** | Monitoring |
| **Description** | AI providers deployed in single region |
| **Impact** | AI platform unavailable during region outage |
| **Likelihood** | Very Low (Azure OpenAI multi-region) |
| **Mitigation** | Configure multi-region provider routing |
| **Owner** | AI Team |
| **Due** | Phase 14 |
| **Trend** | → Monitored |

### R006: Event Backbone Consumer Adoption

| Field | Value |
|-------|-------|
| **ID** | R006 |
| **Severity** | LOW |
| **Domain** | Architecture |
| **Status** | Monitoring |
| **Description** | Event backbone has limited consumer adoption |
| **Impact** | Events published but not consumed, reducing value of event-driven architecture |
| **Likelihood** | Medium |
| **Mitigation** | Implement event consumers for key event types |
| **Owner** | Platform Team |
| **Due** | Phase 14 |
| **Trend** | → Improving |

## Risk Matrix

```
Likelihood
    ↑
  High    │
          │
  Med    │  R004  R006
          │              R002
  Low    │  R005         R003
          │
          └──────────────────────────→
          Low    Med    High   Critical
                     Impact
```

## Risk Response Plan

### Avoidance
- ~~Remove credentials from git (R001)~~ ✓ Done
- Configure SMTP failover before production (R002)

### Mitigation
- Ensure all services deployed across 3 AZs (R003)
- Establish performance baseline in CI (R004)

### Acceptance
- AI provider single-region dependency accepted (R005)
- Event consumer adoption tracked for Phase 14 (R006)

### Transfer
- AI provider SLA risk transferred to provider contract (R005)

## Risk Governance

| Review Cycle | Frequency | Participants |
|-------------|-----------|-------------|
| Daily | Ongoing | On-call SRE |
| Weekly | Every Monday | Engineering team |
| Monthly | Every 1st | Platform leadership |
| Quarterly | Every quarter | Architecture committee |

## Conclusion

The risk register identifies 5 active risks: 0 high, 2 medium, 3 low. The single high risk (R001: SMTP credentials) has been resolved. All remaining risks are actively monitored with defined mitigation plans.
