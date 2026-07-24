# SporeKart Alert Policy

## Overview

This document defines the alerting philosophy, rules, routing, and response procedures for the SporeKart platform. Alerts are categorized by severity with clearly defined response times, escalation paths, and resolution SLAs.

## Alert Philosophy

- **Alert on symptoms, not causes**: Alert when user experience is impacted
- **Actionable alerts only**: Every alert must have a runbook or known response
- **No alert fatigue**: Alerts that don't trigger action are noise and are removed
- **Self-healing first**: Automate response before alerting humans
- **Burn rate alerts**: Alert on error budget consumption rate, not individual errors

## Alert Severity

### SEV1 — Critical
| Aspect | Detail |
|--------|--------|
| **Response Time** | 15 minutes |
| **Notification** | PagerDuty + Slack + Phone |
| **Example** | Service down, data loss, security breach |
| **Runbook** | Required |
| **Postmortem** | Within 24 hours |

Alert Conditions:
- Service unreachable > 1 minute
- Error rate > 10% for > 5 minutes
- P99 latency > 1s for > 5 minutes
- Error budget burn rate > 10x for 1 hour
- Availability < 99.9% for 10 minutes
- Container crash looping
- Database unreachable

### SEV2 — High
| Aspect | Detail |
|--------|--------|
| **Response Time** | 30 minutes |
| **Notification** | PagerDuty + Slack |
| **Example** | Service degradation > 50%, partial outage |
| **Runbook** | Recommended |
| **Postmortem** | Within 48 hours |

Alert Conditions:
- SLO violation > 10 minutes
- P95 latency > 500ms for > 5 minutes
- Error rate > 1% for > 5 minutes
- Connection pool > 80% for > 5 minutes
- AI provider failure
- DLQ growing > 10 events/hour
- Cache hit ratio < 80% for > 5 minutes

### SEV3 — Medium
| Aspect | Detail |
|--------|--------|
| **Response Time** | 2 hours |
| **Notification** | Slack |
| **Example** | Minor degradation, non-critical errors |
| **Postmortem** | Within 1 week |

Alert Conditions:
- CPU > 80% for > 5 minutes
- Memory > 85% for > 5 minutes
- GC frequency > 10/min for > 5 minutes
- Queue length > 10,000 for > 2 minutes
- Failed login rate elevated

### SEV4 — Low
| Aspect | Detail |
|--------|--------|
| **Response Time** | 8 hours |
| **Notification** | Slack (info channel) |
| **Example** | Informational, minor issues |
| **Postmortem** | Not required |

Alert Conditions:
- Certificate expiry < 30 days
- Old deployment version running
- Disk usage > 70%

## Alert Routing

### Routing Table

| Alert | Severity | Channel | Escalation |
|-------|----------|---------|------------|
| ServiceDown | SEV1 | PagerDuty + #sporekart-sev1 | Eng Lead → VP Eng |
| HighErrorRate | SEV1 | PagerDuty + #sporekart-sev1 | Eng Lead → VP Eng |
| HighLatency | SEV2 | PagerDuty + #sporekart-warnings | Eng Lead |
| DBConnectionPool | SEV2 | PagerDuty + #sporekart-warnings | DBA Team |
| AIProviderFailure | SEV2 | #sporekart-ai | AI Team |
| DLQGrowth | SEV2 | #sporekart-events | Platform Team |
| FailedLogins | SEV3 | #sporekart-security | Security Team |
| CPUHigh | SEV3 | #sporekart-warnings | Platform Team |
| CertExpiry | SEV4 | #sporekart-info | DevOps Team |

### Escalation Policy (Business Hours)
- **Level 1**: Primary On-Call — 15 min response
- **Level 2**: Engineering Lead — 10 min after Level 1 timeout
- **Level 3**: VP Engineering — 5 min after Level 2 timeout
- **Level 4**: CTO — Phone call only

### Escalation Policy (After Hours)
- **Level 1**: Primary + Secondary On-Call — 15 min
- **Level 2**: Engineering Lead — Immediate after Level 1 timeout

## Runbook Requirements

Every SEV1 alert must have a runbook. Runbooks should include:
1. **Symptoms**: What to look for
2. **Immediate actions**: What to do first
3. **Diagnosis steps**: How to find root cause
4. **Recovery actions**: How to fix
5. **Verification**: How to confirm resolution
6. **Contact**: Who to escalate to

## Alert Lifecycle

1. **Firing**: Condition met, alert triggered
2. **Acknowledged**: Engineer responding in PagerDuty
3. **Investigating**: Root cause analysis in progress
4. **Mitigated**: Short-term fix applied
5. **Resolved**: Permanent fix deployed, monitoring stable
6. **Reviewed**: Postmortem completed

## Alert Testing

- Monthly: Test all critical alert rules
- Quarterly: Test escalation paths
- Per release: Validate alert configuration
- Continuous: Monitor alert-to-ticket conversion rate
