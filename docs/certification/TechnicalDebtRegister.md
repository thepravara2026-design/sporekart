# Technical Debt Register

## Scope
Registered technical debt items across all Sprint 2 Part 1 enterprise intelligence services (Ch4-Ch6), with priority, impact, and remediation plan.

## Debt Register

| ID | Severity | Area | Description | Impact | Remediation | Effort |
|---|---|---|---|---|---|---|
| TD-001 | Low | Analytics Engine (Ch1-2) | In-memory ConcurrentHashMap storage; no persistence | Data lost on restart; no audit trail | Implement PostgreSQL/Redis adapters | 2 sprints |
| TD-002 | Low | Executive Dashboard (Ch3) | Hardcoded seed KPIs and insights in engine classes | Cannot customize per tenant | Move to configurable provider | 1 sprint |
| TD-003 | Low | Decision Intelligence (Ch4) | Mock recommendation engine with deterministic outcomes | No ML-based decision intelligence | Integrate with AI inference service | 3 sprints |
| TD-004 | Low | Predictive Intelligence (Ch4) | Forecast engine returns predetermined seed data | No real predictive modelling | Integrate with ML forecasting service | 3 sprints |
| TD-005 | Low | Alert Intelligence (Ch5) | Alert severity scoring is rule-based with hardcoded thresholds | No adaptive or ML-based severity | Implement dynamic threshold learning | 2 sprints |
| TD-006 | Low | Alert Intelligence (Ch5) | Timeline events are static seed data | Cannot ingest real-time events | Integrate with event bus/message queue | 2 sprints |
| TD-007 | Low | Reporting Service (Ch6) | BI reports return mock aggregated data | No real data warehouse integration | Connect to data warehouse/OLAP | 3 sprints |
| TD-008 | Low | Reporting Service (Ch6) | Export service produces placeholder content (PDF/CSV/Excel/HTML) | Non-functional export output | Implement real export rendering | 1 sprint |
| TD-009 | Low | Reporting Service (Ch6) | Scheduler service is a mock with no actual cron execution | Scheduled reports do not auto-generate | Implement Quartz/Spring Scheduler | 1 sprint |
| TD-010 | Low | All services | No integration tests against real infrastructure | CI/CD validation limited | Add integration test suite | 2 sprints |
| TD-011 | Low | All services | No OpenAPI/Swagger documentation auto-generation | API docs need manual maintenance | Configure springdoc-openapi | 1 sprint |
| TD-012 | Low | All services | No container healthcheck endpoints beyond Actuator defaults | Limited Kubernetes liveness/readiness | Custom health indicators per service | 1 sprint |
| TD-013 | Medium | Frontend | Report builder UI is placeholder; no drag-drop template editing | Users cannot customize reports visually | Implement drag-drop template builder | 2 sprints |
| TD-014 | Medium | Frontend | Alert acknowledgment UI is client-only; no real-time WebSocket push | Users must refresh to see alert updates | Add WebSocket/SSE push | 2 sprints |
| TD-015 | Low | Frontend | No responsive mobile layout for intelligence dashboards | Poor UX on mobile devices | Add responsive breakpoints | 1 sprint |

## Summary

| Priority | Count | Effort (sprints) |
|---|---|---|
| Critical | 0 | — |
| High | 0 | — |
| Medium | 2 | 4 |
| Low | 13 | 23 |
| **Total** | **15** | **27** |

## Decision
✅ **ACCEPTED** — All items are intentional Phase 0 mock/placeholder debt. No items block RC4 release. Remediation will be scheduled in post-RC4 production-hardening sprints.
