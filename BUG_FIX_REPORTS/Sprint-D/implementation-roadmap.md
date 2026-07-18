# Implementation Roadmap — Bug Fix Sprint D

**Date:** 2026-07-18
**Note:** Roadmap assumes Option A (expanded scope). See escalation-report.md.

---

## Overview

| Phase | Name | Issues | Est. Days | Dependencies | Risk |
|-------|------|--------|-----------|--------------|------|
| 1 | Critical — Security & E-commerce Core | 3 | 30–42 | None | High — largest effort, highest impact |
| 2 | High — UX, Cross-browser, Content | 4 | 4–6 | Phase 1 (route guards for role switcher) | Low — well-understood, small changes |
| 3 | Polish — Medium/Low Items | 7 | 4 | Phase 2 (OTP fix before OTP validation) | Very Low — isolated, cosmetic |

---

## Roadmap Phases

### Phase 1: Critical (Weeks 1–2)

| Week | Day | Focus | Issue(s) | Deliverable |
|------|-----|-------|----------|-------------|
| W1 | 1–2 | Route guards | BUG-S3-CRIT-001 | AuthGuard/ProtectedRoute component, applied to all 256 routes |
| W1 | 3–7 | Cart scaffolding | BUG-S3-CRIT-002 | Cart page with add/remove/quantity, checkout page shell, payment page shell |
| W2 | 1–5 | Admin console | BUG-S3-CRIT-003 | Route guards for admin, functional dashboard, user management, settings |
| W2 | 6–7 | Buffer / testing | All Phase 1 | Integration tests, regression suite execution |

### Phase 2: High (Week 3)

| Day | Focus | Issue(s) | Deliverable |
|-----|-------|----------|-------------|
| 1 | Firefox auth fix | BUG-AUTH-001 | CSS polyfill or `:has()` replacement |
| 2 | OTP navigation fix | BUG-QA4-HIGH-003 | sessionStorage fallback for state |
| 3 | Product mock data | BUG-QA4-HIGH-004 | Product images, pricing, descriptions |
| 4–5 | Role switcher | BUG-S3-HIGH-003 | Role dropdown in header, context provider |

### Phase 3: Polish (Week 4)

| Day | Focus | Issue(s) | Deliverable |
|-----|-------|----------|-------------|
| 1 | Dashboard + OTP validation | MED-001, MED-002 | Redirect on dashboard, validation messages |
| 2 | Search + ARIA | MED-003, MED-004 | Case-insensitive search, ARIA labels |
| 3 | Flaky tests | MED-005 | Stabilize 4 flaky tests |
| 4 | Low items | LOW-001, LOW-002, LOW-003 | Contrast fix, page title, deprecated prop |

---

## Milestones

| Milestone | Date (approx) | Criteria |
|-----------|---------------|----------|
| M1: Auth gates in place | End of W1 Day 2 | All 256 routes protected; 0 unauthorized access |
| M2: Purchase pipeline scaffolded | End of W1 Day 7 | Cart page functional, checkout/payment pages render |
| M3: Admin console operational | End of W2 Day 7 | Admin dashboard, user management, settings all functional |
| M4: Cross-browser parity | End of W3 Day 1 | Login works in Firefox; all auth tests pass on Chromium, Firefox, Edge |
| M5: Content complete | End of W3 Day 7 | Product details show data; roles switchable |
| M6: Polish complete | End of W4 Day 7 | All 14 issues resolved; flake rate below 2%; all tests passing |
| M7: RC1 qualification | End of W4 | Run full QA Sprint 5 regression — 0 Critical/High, minimum 85% stability |

---

## Risk Register for Roadmap

| Risk | Impact | Mitigation |
|------|--------|------------|
| Cart/checkout/payment effort underestimated (15–20 days) | Phase 1 exceeds 2 weeks | Cut scope: ship cart + basic checkout, defer payment integration |
| Route guards break existing navigation | AuthZ regressions | Run full authZ test suite after guard deployment |
| Admin console scope too large | Phase 1 exceeds 2 weeks | Ship minimum viable admin (dashboard + user list); defer settings/permissions |
| Team capacity insufficient | All phases slip | Parallelize: Phase 1 (dev team), Phase 2 (second dev), Phase 3 (QA/intern) |
