# Effort Estimation — Bug Fix Sprint D

**Date:** 2026-07-18
**Methodology:** T-shirt sizing based on bug complexity, codebase familiarity assumptions, and test coverage requirements. All estimates in person-days (8 hours).

---

## Effort by Issue

| Issue | T-Shirt Size | Person-Days | Confidence | Rationale |
|-------|-------------|-------------|------------|-----------|
| BUG-S3-CRIT-001 — Route guards | XL | 5–7 | Medium | 256 routes to audit; need AuthGuard component, route config refactor, test suite |
| BUG-S3-CRIT-002 — Cart/checkout/payment | XXL | 15–20 | Low | Entire feature from scratch; cart state management, checkout flow, payment gateway integration |
| BUG-S3-CRIT-003 — Admin console | XL | 10–15 | Low | Unknown admin requirements; dashboard, user management, settings |
| BUG-AUTH-001 — Firefox CSS | S | 0.5–1 | High | Isolated CSS fix; polyfill or selector replacement |
| BUG-S3-HIGH-003 — Role switcher | M | 2–3 | Medium | Auth context changes; dropdown UI, role state management, test updates |
| BUG-QA4-HIGH-003 — OTP nav state | S | 0.5–1 | High | sessionStorage fallback; well-understood pattern |
| BUG-QA4-HIGH-004 — Product mock data | S | 1 | Medium | Data file creation; needs placeholder images, pricing, descriptions |
| BUG-QA4-MED-001 — Dashboard redirect | XS | 0.5 | High | Single redirect condition in AuthGuard |
| BUG-QA4-MED-002 — OTP validation | XS | 0.5 | Medium | Form validation message addition |
| BUG-QA4-MED-003 — Case-insensitive search | XS | 0.5 | High | String comparison change (`toLowerCase`) |
| BUG-QA4-MED-004 — ARIA labels | XS | 0.25 | High | Attribute addition |
| BUG-QA4-MED-005 — Flaky tests | M | 2 | Low | Root cause analysis needed; may involve test timeouts, async timing |
| BUG-QA4-LOW-001 — Footer contrast | XS | 0.25 | High | CSS color value change |
| BUG-QA4-LOW-002 — Training title | XS | 0.1 | High | Single `document.title` or helmet update |
| BUG-QA4-LOW-003 — Deprecated prop | XS | 0.1 | High | Prop rename |

---

## Summary by Phase (Option A — Expanded Scope)

| Phase | Issues | Min Days | Max Days | Best Case | Likely Case | Worst Case |
|-------|--------|----------|----------|-----------|-------------|------------|
| Phase 1: Critical | 3 | 30 | 42 | 30 | 36 | 42 |
| Phase 2: High | 4 | 4 | 6 | 4 | 5 | 6 |
| Phase 3: Polish | 7 | 4 | 4 | 3.5 | 4 | 4.5 |
| **Total** | **14** | **38** | **52** | **37.5** | **45** | **52.5** |

### Team Capacity Check

| Team Size | Weeks Required (Likely) | Feasible? |
|-----------|------------------------|-----------|
| 1 developer | 9 weeks | No — single sprint |
| 2 developers (parallel) | 4.5 weeks | Tight — could fit expanded Sprint D |
| 3 developers (parallel) | 3 weeks | Yes — comfortable fit |

**Recommendation:** Sprint D requires at least **2 developers** working in parallel for 4 weeks, or 3 developers for 3 weeks.

---

## Confidence Assessment

| Confidence Level | Count of Issues | Total Effort Range |
|-----------------|-----------------|-------------------|
| High (well-understood) | 8 | 2.7–4.5 days |
| Medium (some unknowns) | 4 | 4–5.5 days |
| Low (significant unknowns) | 3 | 27–40 days |

The **low confidence** items represent 71–77% of total effort. This is the primary schedule risk.
