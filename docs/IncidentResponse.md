# SporeKart Incident Response

## Overview

This document defines the incident response framework for the SporeKart platform. It covers severity classification, response procedures, escalation paths, communication protocols, and postmortem requirements.

## Incident Severity

| Severity | Label | Response | Resolution | Examples |
|----------|-------|----------|------------|----------|
| SEV1 | Critical | 15 min | 4 hours | Complete outage, data loss, breach |
| SEV2 | High | 30 min | 8 hours | Major degradation, partial outage |
| SEV3 | Medium | 2 hours | 24 hours | Minor degradation, non-critical errors |
| SEV4 | Low | 8 hours | 1 week | Cosmetic issues, minor bugs |

## Incident Response Lifecycle

### 1. Discovery
- Automated alert from Prometheus
- User/customer report via support
- Dashboard anomaly detection
- Manual observation by engineer

### 2. Triage
1. Acknowledge alert (PagerDuty)
2. Assess severity and impact
3. Declare incident in #sporekart-incidents
4. Assign incident commander
5. Post initial status update

### 3. Response
1. Investigate using runbooks
2. Apply mitigation (rollback, scale, degrade)
3. Verify mitigation effectiveness
4. Update status every 15 minutes
5. Communicate to stakeholders

### 4. Resolution
1. Confirm service fully restored
2. Verify all health checks passing
3. Monitor for 15 minutes post-resolution
4. Document timeline and actions
5. Resolve alert in PagerDuty

### 5. Postmortem
1. Schedule postmortem within:
   - 24 hours for SEV1
   - 48 hours for SEV2
   - 1 week for SEV3
2. Complete postmortem template
3. Identify root cause
4. Create action items
5. Share findings with team

## Incident Commander Responsibilities

### During Incident
- Coordinate response effort
- Assign tasks to team members
- Communicate status every 15 minutes
- Make escalation decisions
- Document timeline
- Keep stakeholder channel updated

### Communication Template
```
Status: [INVESTIGATING / MITIGATED / RESOLVED]
Severity: SEV[X]
Services: [affected services]
Impact: [% users affected, error rate, latency]
Actions: [current actions being taken]
ETA: [estimated resolution time]
Next update: [time of next update]
```

## Communication Channels

| Channel | Purpose | Audience |
|---------|---------|----------|
| #sporekart-incidents | Incident coordination | Engineering team |
| #sporekart-sev1 | SEV1 real-time updates | All hands |
| #sporekart-sev2 | SEV2 status updates | Engineering leads |
| #sporekart-status | Customer-facing status | All stakeholders |
| PagerDuty | Critical alert notification | On-call engineers |
| Email | Postmortem distribution | Engineering team |

## On-Call Responsibilities

### Primary On-Call
- Respond to alerts within 15 minutes
- Acknowledge PagerDuty notifications
- Follow runbooks for known issues
- Escalate to Secondary if needed
- Document incident timeline
- Handoff to next shift clearly

### Secondary On-Call
- Backup for Primary
- Handle non-critical alerts
- Support Primary during SEV1
- Take over if Primary reaches escalation timeout

## Postmortem Requirements

### Mandatory Content
- Incident timeline with timestamps
- Root cause analysis
- Impact assessment
- Detection and response effectiveness
- Action items with owners and deadlines
- Lessons learned
- Appendices (logs, dashboards, alerts)

### Postmortem Distribution
- Engineering team (Slack)
- Management (email)
- Action items tracked in JIRA
- Blameless culture enforced

## Blameless Culture

Postmortems are blameless by design:
- Focus on system failures, not individual mistakes
- Identify process improvements, not personal errors
- Celebrate discoveries, not attribute blame
- All team members can contribute without fear

## Runbooks

### Available Runbooks
| Runbook | Location |
|---------|----------|
| Service Down | infrastructure/observability/incident/runbooks/generic-service-down.md |
| Database Failure | infrastructure/observability/incident/runbooks/database-failure.md |
| AI Provider Failure | infrastructure/observability/incident/runbooks/ai-provider-failure.md |

### Recovery Checklist
Refer to: infrastructure/observability/incident/recovery-checklist.md

## Training

### On-Call Training Requirements
- Complete on-call training before first shift
- Shadow current on-call for 1 week
- Demonstrate runbook execution
- Pass incident simulation
- Review past postmortems

### Incident Drills
- Quarterly: SEV1 simulation
- Monthly: Alert response practice
- Per new service: Runbook review
- Team rotation: Cross-training
