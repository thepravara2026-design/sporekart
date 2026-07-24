# SporeKart Service Level Objectives (SLO) & Service Level Indicators (SLI)

## Overview

This document defines the Service Level Objectives, Service Level Indicators, error budgets, and compliance tracking for the SporeKart platform. SLOs are the contractual quality targets that every service must meet.

## Service Level Indicators (SLIs)

### Platform SLIs

| SLI | Measurement | Source | Collection |
|-----|-------------|--------|------------|
| Availability | Probe success rate | Health endpoint | Prometheus |
| API Latency | HTTP request duration P95 | http.server.requests | Micrometer |
| Error Rate | 5xx / total requests | http.server.requests | Micrometer |
| Throughput | Requests per second | http.server.requests | Micrometer |
| Saturation | CPU/Memory utilization | process_cpu_usage, jvm.memory | Micrometer |

### Business SLIs

| SLI | Measurement | Source |
|-----|-------------|--------|
| Order Success Rate | Completed / Created | BusinessMetrics |
| Payment Success Rate | Processed / (Processed + Failed) | BusinessMetrics |
| Notification Delivery | Delivered / Sent | NotificationMetrics |

### AI SLIs

| SLI | Measurement | Source |
|-----|-------------|--------|
| Completion Success | Completions / Requests | AIMetrics |
| Provider Availability | Successful / Total provider calls | AIMetrics |
| Knowledge Retrieval Latency | P95 of retrieval time | AIMetrics |
| AI Cost per Request | Cost distribution | AIMetrics |

### Infrastructure SLIs

| SLI | Measurement | Source |
|-----|-------------|--------|
| Container Uptime | Container running seconds | K8s API |
| DB Connection Availability | Successful queries / Total | pg_stat |
| Cache Hit Ratio | Hits / (Hits + Misses) | Micrometer |

## Service Level Objectives (SLOs)

### Platform SLOs

| SLO | Target | Window | Error Budget | Measurement |
|-----|--------|--------|-------------|------------|
| Platform Availability | 99.9% | 30 days | 43m 50s/month | probe_success_rate |
| API Latency (P95) | < 200ms | 7 days | N/A | http.server.requests |
| API Latency (P99) | < 500ms | 7 days | N/A | http.server.requests |
| Authentication (P95) | < 100ms | 7 days | N/A | auth.duration |
| Gateway Latency (P95) | < 100ms | 7 days | N/A | gateway.duration |
| Error Rate | < 0.1% | 7 days | N/A | error_rate |

### Business SLOs

| SLO | Target | Window | Error Budget | Measurement |
|-----|--------|--------|-------------|------------|
| Order Success | 99.5% | 30 days | 3h 39m/month | order.completion.rate |
| Payment Success | 99.9% | 30 days | 43m 50s/month | payment.success.rate |

### AI SLOs

| SLO | Target | Window | Error Budget | Measurement |
|-----|--------|--------|-------------|------------|
| AI Completion Success | 99.0% | 7 days | 1h 40m/week | ai.completion.rate |
| AI Response Time (P95) | < 3s | 7 days | N/A | ai.completion.time |
| Provider Availability | 99.5% | 30 days | 3h 39m/month | ai.provider.availability |
| Knowledge Retrieval (P95) | < 200ms | 7 days | N/A | ai.knowledge.time |

### Database SLOs

| SLO | Target | Window | Error Budget | Measurement |
|-----|--------|--------|-------------|------------|
| DB Query Latency (P95) | < 50ms | 7 days | N/A | db.query.duration |
| Connection Availability | 99.99% | 30 days | 4.3 min/month | db.connection.success |

## Error Budgets

### Calculation
```
Error Budget = (1 - SLO Target) × Total Events
Budget Consumption = (1 - Actual SLI) × Total Events
Remaining Budget = Error Budget - Budget Consumption
```

### Burn Rate Alerts
| Rate | Window | Multiplier | Severity | Action |
|------|--------|-----------|----------|--------|
| Critical | 1 hour | 10x | SEV1 | Immediate investigation |
| High | 6 hours | 5x | SEV2 | Investigation required |
| Medium | 3 days | 2x | SEV3 | Review required |

### When Error Budget is Exhausted
1. All non-critical deployments are frozen
2. Only SEV1/SEV2 fixes allowed
3. Team focuses on reliability improvements
4. Budget review with VP Engineering
5. Rollback plan for risky features
6. Postmortem for budget exhaustion

## SLO Tracking

### Measurement Windows
- **7-day window**: Fast feedback, weekly review
- **30-day window**: Monthly compliance, error budget
- **Quarterly**: Trend analysis, SLO adjustment

### Burn Rate Alert Conditions
```
Critical: (1 - SLI_1h) < (1 - SLO * 10) for 5 minutes
High: (1 - SLI_6h) < (1 - SLO * 5) for 10 minutes
Medium: (1 - SLI_3d) < (1 - SLO * 2) for 30 minutes
```

## Compliance

### Monthly Report
- SLO achievement percentage
- Error budget consumption
- Number of SLO violations
- Top contributors to budget consumption
- Improvement recommendations

### Violation Response
| Violation Type | Response | Documentation |
|---------------|----------|--------------|
| SLO Miss | Postmortem required | Reliability improvement plan |
| Burn Rate Critical | Immediate incident | Incident report |
| Error Budget Exhausted | Deploy freeze | Budget review document |

## SLO Review Cycle

- **Weekly**: SLO achievement review in SRE weekly
- **Monthly**: Error budget report to leadership
- **Quarterly**: SLO target adjustment based on trends
- **Annually**: SLO framework evaluation and improvement
