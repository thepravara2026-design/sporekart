# Risk Assessment — QA Sprint 3 Part 3 (Platform Reliability)

## Risk Scoring
L = Likelihood (1-5), I = Impact (1-5), Score = L × I (1-25)

## Risk Register

| # | Risk Description                                      | L | I | Score | Priority | Mitigation |
|---|-------------------------------------------------------|---|---|-------|----------|------------|
| R1 | Production build crash not resolved before RC         | 4 | 5 | **20** | **P0** | Assign senior front-end engineer; isolate infinite re-render cause |
| R2 | No observability infrastructure — undetectable failures| 4 | 4 | **16** | **P1** | Implement health checks, structured logging, metrics |
| R3 | No security headers — vulnerable to XSS/clickjacking  | 3 | 5 | **15** | **P1** | Add CSP, HSTS, X-Frame-Options at CDN/proxy layer |
| R4 | No real authentication — auth bypass risk              | 3 | 5 | **15** | **P1** | Replace stub auth with real JWT/Supabase |
| R5 | Service Worker absent — no offline capability          | 4 | 3 | **12** | **P2** | Implement SW with Workbox for caching |
| R6 | Duplicate toast implementations cause UX inconsistency | 3 | 2 | **6**  | **P2** | Consolidate to single ToastQueue provider |
| R7 | No performance monitoring — regression detection blind | 3 | 3 | **9**  | **P2** | Add Lighthouse CI + performance budgets |
| R8 | No 404 page — poor user experience on unknown routes   | 2 | 2 | **4**  | **P3** | Implement catch-all route with guidance |
| R9 | No CSRF protection — form-based attacks possible       | 2 | 4 | **8**  | **P2** | Add CSRF tokens to all state-changing forms |
| R10 | No APM — production incidents invisible               | 3 | 4 | **12** | **P1** | Deploy OpenTelemetry + Datadog/Grafana |
| R11 | No backup/DR procedures documented or tested          | 3 | 4 | **12** | **P1** | Create DR plan, test restore process |
| R12 | Stub auth means no real RBAC enforcement              | 3 | 4 | **12** | **P1** | Replace with real auth + server-side guards |

## Top 5 Risks

| Rank | Risk | Score | Action Owner     |
|------|------|-------|------------------|
| 1    | R1 — Build crash unresolved | 20 | Front-end lead  |
| 2    | R2 — No observability       | 16 | SRE / Platform  |
| 3    | R3 — No security headers    | 15 | DevOps / Infra  |
| 4    | R4 — No real auth           | 15 | Backend / Auth  |
| 5    | R10 — No APM                | 12 | SRE / Platform  |

## Risk Summary

| Severity | Count | Total Risks |
|----------|-------|-------------|
| P0 (17-25)  | 1  | R1  |
| P1 (12-16)  | 6  | R2, R3, R4, R10, R11, R12 |
| P2 (6-11)   | 3  | R5, R7, R9 |
| P3 (1-5)    | 2  | R6, R8 |

**Overall Risk Level**: **HIGH** — The combination of a critical build crash, absent observability, missing security controls, and no real authentication represents significant operational risk.
