# Risk Register — QA Sprint 4

**Date:** 2026-07-18
**Methodology:** Risks identified through automated test failures, manual exploratory testing, and static code analysis. Likelihood and Impact each scored 1–5. RPN = Likelihood × Impact.

---

## Risk Scoring Guide

| Score | Likelihood | Impact |
|-------|-----------|--------|
| 1 | Rare | Negligible |
| 2 | Unlikely | Minor |
| 3 | Possible | Moderate |
| 4 | Likely | Major |
| 5 | Almost Certain | Critical |

RPN Thresholds: Critical ≥ 20, High 12–19, Medium 6–11, Low 1–5

---

## Risk Register

| ID | Risk | Likelihood | Impact | RPN | Risk Level | Mitigation |
|----|------|-----------|--------|-----|------------|------------|
| R-01 | Auth flow broken on Firefox | 4 | 5 | 20 | **CRITICAL** | Fix Firefox-specific CSS `:has()` issue (BUG-AUTH-001); apply polyfill or replace with equivalent class-based selector |
| R-02 | No role switcher for RBAC testing | 4 | 4 | 16 | **HIGH** | Implement role switcher in Sprint D (BUG-S3-HIGH-003); add dev-only dropdown to header |
| R-03 | Admin routes accessible without auth | 4 | 5 | 20 | **CRITICAL** | Add route guards (`AuthGuard`/`ProtectedRoute`) to all 256 protected routes in Sprint D |
| R-04 | No real auth backend | 5 | 5 | 25 | **CRITICAL** | Replace mock auth (`MockAuthService`) with real provider (Supabase/Firebase/Auth0); currently all auth is simulated |
| R-05 | No security headers | 3 | 4 | 12 | **HIGH** | Add CSP, HSTS, X-Frame-Options, X-Content-Type-Options via middleware or server config |
| R-06 | Cart/checkout/payment not implemented | 5 | 5 | 25 | **CRITICAL** | Core purchase flow missing entirely; block RC1 until cart-to-payment pipeline is built |
| R-07 | Product catalog has no real data | 3 | 4 | 12 | **HIGH** | Add mock product data (images, pricing, descriptions) in Sprint D; plan real data integration for RC1 |
| R-08 | OTP flow breaks when navigating directly | 3 | 3 | 9 | **MEDIUM** | OTP page requires `location.state` from upstream navigation; add state persistence (sessionStorage fallback) or redirect to login (BUG-QA4-HIGH-003) |
| R-09 | Input/Checkbox style crash (FIXED) | 1 | 5 | 5 | **LOW** | Already fixed (BUG-QA4-CRIT-001); monitor for recurrence in component library updates |
| R-10 | AuthStore crash in SSR/Node (FIXED) | 1 | 3 | 4 | **LOW** | Already fixed (BUG-QA4-CRIT-002); monitor for recurrence if new server-side rendering contexts are introduced |

---

## Risk Breakdown

### Critical Risks (RPN ≥ 20)

| ID | Risk | RPN | Urgency |
|----|------|-----|---------|
| R-04 | No real auth backend | 25 | **Sprint D prerequisite** — Without a real auth provider, the app cannot go to production |
| R-06 | Cart/checkout/payment not implemented | 25 | **RC1 blocker** — E-commerce app without purchase flow is not shippable |
| R-01 | Auth flow broken on Firefox | 20 | **High urgency** — Firefox is ~15% of e-commerce traffic; broken auth excludes 1 in 7 users |
| R-03 | Admin routes accessible without auth | 20 | **Security risk** — Unauthenticated access to admin routes exposes all internal functionality |

### High Risks (RPN 12–19)

| ID | Risk | RPN | Urgency |
|----|------|-----|---------|
| R-02 | No role switcher for RBAC testing | 16 | Blocks admin console QA; cannot verify role-based behavior |
| R-05 | No security headers | 12 | Medium-term risk; exploitable in production but low immediate threat in dev |
| R-07 | Product catalog has no real data | 12 | Customer journey incomplete; RC1 requires at minimum mock product data |

### Medium Risks (RPN 6–11)

| ID | Risk | RPN | Urgency |
|----|------|-----|---------|
| R-08 | OTP flow breaks when navigating directly | 9 | Isolated to one page; workaround available (navigate from login) |

### Low Risks (RPN 1–5)

| ID | Risk | RPN | Urgency |
|----|------|-----|---------|
| R-09 | Input/Checkbox style crash (FIXED) | 5 | Fixed; monitor only |
| R-10 | AuthStore crash in SSR/Node (FIXED) | 4 | Fixed; monitor only |

---

## Risk Trend Since Sprint 3

| Sprint | Critical | High | Medium | Low | Total |
|--------|----------|------|--------|-----|-------|
| Sprint 1 | 3 | 2 | 2 | 1 | 8 |
| Sprint 2 | 3 | 2 | 3 | 1 | 9 |
| Sprint 3 | 5 | 3 | 2 | 2 | 12 |
| Sprint 4 | 4 | 3 | 1 | 2 | 10 |

**Trend:** Risk count has decreased from 12 (Sprint 3) to 10 (Sprint 4). Two critical risks (R-09, R-10) were resolved. No new high-severity risks introduced.

---

## Watched Items (Elevated During Sprint D)

The following items may become risks if not addressed:

- **Flaky tests (21):** Currently 4% of tests are flaky. If this grows beyond 5%, test reliability is compromised.
- **WebKit not tested:** Safari/iOS users completely untested. Should be added before RC1.
- **Build tooling:** Vite dev server is stable but production build has collapsed before (Sprint 3). Monitor build stability.

---

*End of Risk Register — QA Sprint 4*
