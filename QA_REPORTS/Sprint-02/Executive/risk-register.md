# Risk Register — SporeKart RC1

**QA Sprint 2 — Part 12** | **Date:** 2026-07-17

---

## Risk Scoring

| Score | Rating | Description |
|-------|--------|-------------|
| 20-25 | 🔴 CRITICAL | Production blocking, must resolve before any release |
| 15-19 | 🟠 HIGH | Significant business/technical impact |
| 8-14 | 🟡 MEDIUM | Manageable but should be addressed |
| 1-7 | 🟢 LOW | Acceptable risk, monitor |

---

## Business Risks

| # | Risk | L | I | Score | Rating | Mitigation |
|---|------|---|---|-------|--------|------------|
| BR-01 | No checkout flow — cannot process orders | 5 | 5 | 25 | 🔴 CRITICAL | Build cart and checkout in Bug Fix Sprint |
| BR-02 | No real payments — cannot accept money | 5 | 5 | 25 | 🔴 CRITICAL | Integrate Razorpay in Bug Fix Sprint |
| BR-03 | No search functionality — users cannot find products | 4 | 4 | 16 | 🟠 HIGH | Implement search-service controllers |
| BR-04 | No notification delivery — users cannot receive alerts | 4 | 3 | 12 | 🟡 MEDIUM | Implement notification-service logic |
| BR-05 | No mobile apps — no app store presence | 3 | 4 | 12 | 🟡 MEDIUM | Build native mobile apps in parallel |
| BR-06 | No content management — cannot publish content | 3 | 3 | 9 | 🟡 MEDIUM | Implement content-service |
| BR-07 | No customer support flow — users cannot create tickets | 3 | 3 | 9 | 🟡 MEDIUM | Implement support-service |
| BR-08 | No analytics dashboards — business cannot measure | 3 | 2 | 6 | 🟢 LOW | Connect analytics to real data sources |

---

## Technical Risks

| # | Risk | L | I | Score | Rating | Mitigation |
|---|------|---|---|-------|--------|------------|
| TR-01 | No auth boundary — anyone can access any page/API | 5 | 5 | 25 | 🔴 CRITICAL | Implement auth-first: route guards + API auth |
| TR-02 | No session management — no user identity tracking | 5 | 5 | 25 | 🔴 CRITICAL | Implement JWT-based session management |
| TR-03 | Default admin role — privilege escalation by default | 5 | 5 | 25 | 🔴 CRITICAL | Change default to guest immediately |
| TR-04 | IDOR on all controllers — any user can access any data | 5 | 4 | 20 | 🔴 CRITICAL | Add ownership verification |
| TR-05 | Mass assignment via Map<String,String> DTOs | 4 | 4 | 16 | 🟠 HIGH | Replace with typed DTOs |
| TR-06 | No error boundaries — white screen on any error | 4 | 4 | 16 | 🟠 HIGH | Add ErrorBoundary wrapper |
| TR-07 | Timer leaks — memory grows with session length | 3 | 3 | 9 | 🟡 MEDIUM | Fix all setInterval/setTimeout cleanup |
| TR-08 | Context re-render storms — performance degrades | 3 | 3 | 9 | 🟡 MEDIUM | Split AppContext into UI/Auth |
| TR-09 | No pagination — unbounded data loads | 3 | 3 | 9 | 🟡 MEDIUM | Add pagination to all data pages |

---

## Security Risks

| # | Risk | L | I | Score | Rating | Mitigation |
|---|------|---|---|-------|--------|------------|
| SR-01 | No route guards on 256 protected routes | 5 | 5 | 25 | 🔴 CRITICAL | Create ProtectedRoute component |
| SR-02 | Mock authentication accepts any credentials | 5 | 5 | 25 | 🔴 CRITICAL | Integrate real auth provider |
| SR-03 | 128/155 API endpoints have no authentication | 5 | 5 | 25 | 🔴 CRITICAL | Add auth to all endpoints |
| SR-04 | No CSP header — XSS possible | 4 | 4 | 16 | 🟠 HIGH | Add CSP via Vite plugin |
| SR-05 | No X-Frame-Options — clickjacking possible | 4 | 4 | 16 | 🟠 HIGH | Add X-Frame-Options: DENY |
| SR-06 | No HSTS — downgrade attacks possible | 3 | 5 | 15 | 🟠 HIGH | Add HSTS header |
| SR-07 | No rate limiting — brute force possible | 5 | 3 | 15 | 🟠 HIGH | Add rate limiting to auth endpoints |
| SR-08 | No CSRF protection — cross-site request forgery | 3 | 4 | 12 | 🟡 MEDIUM | Add CSRF tokens |
| SR-09 | No input validation on API controllers | 4 | 3 | 12 | 🟡 MEDIUM | Add @Valid annotations |
| SR-10 | No security event logging | 3 | 3 | 9 | 🟡 MEDIUM | Add security audit logging |

---

## Performance Risks

| # | Risk | L | I | Score | Rating | Mitigation |
|---|------|---|---|-------|--------|------------|
| PR-01 | No service worker — 0% cache hit rate, blank offline | 4 | 4 | 16 | 🟠 HIGH | Add Workbox service worker |
| PR-02 | No data virtualization — DOM grows unbounded | 3 | 4 | 12 | 🟡 MEDIUM | Add react-window virtualization |
| PR-03 | No performance budget — bundle can grow unchecked | 3 | 3 | 9 | 🟡 MEDIUM | Set budgets in CI pipeline |
| PR-04 | No CDN caching — every load is full network request | 4 | 2 | 8 | 🟡 MEDIUM | Configure CDN caching strategy |

---

## Deployment Risks

| # | Risk | L | I | Score | Rating | Mitigation |
|---|------|---|---|-------|--------|------------|
| DR-01 | No CI/CD pipeline configured | 4 | 4 | 16 | 🟠 HIGH | Set up GitHub Actions |
| DR-02 | No staging environment | 3 | 4 | 12 | 🟡 MEDIUM | Provision staging environment |
| DR-03 | No monitoring or observability | 4 | 3 | 12 | 🟡 MEDIUM | Add logging, metrics, alerting |
| DR-04 | No database schema migrations | 3 | 3 | 9 | 🟡 MEDIUM | Set up Flyway/Liquibase |
| DR-05 | No backup/restore procedures | 3 | 4 | 12 | 🟡 MEDIUM | Document and test backup strategy |

---

## Risk Heat Map

```
Impact →
  5 | SR-01 SR-02 SR-03  | BR-01 BR-02 TR-01
    | TR-02 TR-03        | TR-02
  4 | SR-04 SR-05 TR-04  | PR-01 DR-01
    |                     |
  3 | SR-07 TR-06         | BR-03 DR-04 SR-06
    |                     |
  2 |                     | BR-08 PR-04
    |                     |
  1 |                     |
    +---------------------+------------------>
     1     2     3     4     5     Likelihood →
```

---

## Top 10 Risks

| Rank | ID | Risk | Score | Category |
|------|----|------|-------|----------|
| 1 | BR-01 | No checkout flow | 25 | Business |
| 2 | BR-02 | No real payments | 25 | Business |
| 3 | TR-01 | No auth boundary | 25 | Technical |
| 4 | TR-02 | No session management | 25 | Technical |
| 5 | TR-03 | Default admin role | 25 | Technical |
| 6 | SR-01 | No route guards | 25 | Security |
| 7 | SR-02 | Mock authentication | 25 | Security |
| 8 | SR-03 | 82.6% unauthenticated APIs | 25 | Security |
| 9 | TR-04 | IDOR on all controllers | 20 | Technical |
| 10 | PR-01 | No caching/offline | 16 | Performance |

---

*End of Risk Register — 28 risks identified (10 critical, 9 high, 8 medium, 1 low)*
