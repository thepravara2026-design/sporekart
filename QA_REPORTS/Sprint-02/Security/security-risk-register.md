# Security Risk Register — QA Sprint 2 Part 10

**Release:** v1.0.0-rc1 | **Date:** 2026-07-17

---

## Risk Scoring Methodology

Risk = Likelihood × Impact (each 1-5)

| Score | Rating |
|-------|--------|
| 20-25 | 🔴 CRITICAL |
| 15-19 | 🟠 HIGH |
| 8-14 | 🟡 MEDIUM |
| 1-7 | 🟢 LOW |

---

## Risk Register

| # | Vulnerability | Category | L | I | Score | Rating | Status |
|---|--------------|----------|---|---|-------|--------|--------|
| 1 | No route guards on 256 protected routes | Access Control | 5 | 5 | 25 | 🔴 CRITICAL | Open |
| 2 | Default role is administrator | Authentication | 5 | 5 | 25 | 🔴 CRITICAL | Open |
| 3 | Auth is entirely mock-based | Authentication | 5 | 5 | 25 | 🔴 CRITICAL | Open |
| 4 | 82.6% API endpoints without auth | API Security | 5 | 5 | 25 | 🔴 CRITICAL | Open |
| 5 | IDOR on all controllers | Access Control | 4 | 5 | 20 | 🔴 CRITICAL | Open |
| 6 | No session management | Authentication | 4 | 5 | 20 | 🔴 CRITICAL | Open |
| 7 | Privilege escalation via UI dropdown | Authorization | 5 | 4 | 20 | 🔴 CRITICAL | Open |
| 8 | No CSP header | Misconfiguration | 4 | 4 | 16 | 🟠 HIGH | Open |
| 9 | No X-Frame-Options | Misconfiguration | 4 | 4 | 16 | 🟠 HIGH | Open |
| 10 | No HSTS header | Misconfiguration | 3 | 5 | 15 | 🟠 HIGH | Open |
| 11 | No input validation on API controllers | API Security | 4 | 4 | 16 | 🟠 HIGH | Open |
| 12 | No rate limiting on auth endpoints | Auth | 5 | 3 | 15 | 🟠 HIGH | Open |
| 13 | No CSRF protection | Web Security | 3 | 4 | 12 | 🟡 MEDIUM | Open |
| 14 | No security event logging | Logging | 3 | 4 | 12 | 🟡 MEDIUM | Open |
| 15 | No HTTP method restrictions | API Security | 3 | 3 | 9 | 🟡 MEDIUM | Open |
| 16 | Error details leak component info | Error Handling | 2 | 3 | 6 | 🟢 LOW | Open |
| 17 | console.log in production | Logging | 2 | 2 | 4 | 🟢 LOW | Open |
| 18 | No Subresource Integrity | Integrity | 2 | 2 | 4 | 🟢 LOW | Open |
| 19 | Vite dev server host:true | Config | 3 | 2 | 6 | 🟢 LOW | Open |

---

## Risk Distribution

```
CRITICAL (7 risks)
├── SEC-001  No route guards                   score: 25
├── SEC-002  Default admin role                 score: 25
├── SEC-003  Mock authentication                score: 25
├── SEC-004  82.6% unauthenticated APIs         score: 25
├── ──  IDOR on all controllers                score: 20
├── ──  No session management                  score: 20
└── ──  Privilege escalation via dropdown      score: 20

HIGH (5 risks)
├── SEC-006  No CSP header                      score: 16
├── SEC-007  No X-Frame-Options                score: 16
├── ──  No input validation on APIs            score: 16
├── ──  No HSTS header                         score: 15
└── ──  No rate limiting                       score: 15

MEDIUM (3 risks)
├── ──  No CSRF protection                     score: 12
├── ──  No security logging                    score: 12
└── ──  No HTTP method restrictions            score: 9

LOW (4 risks)
├── ──  Error info leakage                     score: 6
├── ──  Vite host:true                         score: 6
├── ──  No SRI hashes                          score: 4
└── ──  console.log in code                    score: 4
```

---

## Top 5 Risks by Score

| Rank | Risk | Score | Remediation |
|------|------|-------|-------------|
| 1 | No route guards | 25 | Implement ProtectedRoute component |
| 2 | Default admin role | 25 | Change default to guest |
| 3 | Mock authentication | 25 | Integrate real auth provider |
| 4 | 82.6% unauthenticated APIs | 25 | Add auth to all endpoints |
| 5 | IDOR on controllers | 20 | Add ownership verification |

---

## Remediation Priority

### Immediate (Sprint 3 — Week 1)
1. Change default role from `administrator` to `guest` (1 hour)
2. Implement route guard component (3 days)
3. Add security headers via Vite config (1 day)

### Short-term (Sprint 3 — Week 2-3)
4. Integrate real authentication (5 days)
5. Add authentication to API endpoints (5 days)
6. Add input validation to API controllers (3 days)

### Medium-term (Sprint 4)
7. Implement session management with JWT (3 days)
8. Add rate limiting (2 days)
9. Add CSRF protection (2 days)
10. Add ownership verification (5 days)

---

*End of Security Risk Register*
