# SporeKart QA Sprint 2 — Risk Register (Interim Review)

**Date:** 2026-07-17

---

## Technical Risks

| # | Risk | Probability | Impact | Mitigation | Owner |
|---|------|-------------|--------|------------|-------|
| T1 | Mock API interception incompatible with Firefox (Gecko) | High | Critical — Firefox users cannot authenticate or use app | Investigate `page.route()` vs `page.routeFromHAR()` for Firefox; consider using WebSocket mock instead | QA Engineering |
| T2 | Product catalog implementation may have significant scope creep | High | Critical — 5+ features depend on it | Define MVP product catalog scope (listing + grid + pagination only) before adding filters/sorting | Product Management |
| T3 | Cart state management complexity underestimated | Medium | High — cart persistence, guest→logged-in merge, multi-tab sync | Start with simple localStorage-based cart; add complexity iteratively | Engineering |
| T4 | OTP input timing sensitivity on iOS/WebKit may require framework change | Low | Medium — poor UX on 2 of 5 target browsers | Consider replacing 6 individual inputs with single masked input | Engineering |
| T5 | `networkidle` timeouts indicate polling/SSE may cause performance issues | Medium | Medium — persistent connections affect resource usage | Audit dashboard/training pages for unnecessary polling | Engineering |

## Business Risks

| # | Risk | Probability | Impact | Mitigation | Owner |
|---|------|-------------|--------|------------|-------|
| B1 | E-commerce journey non-functional for v1.0-rc1 | Certain | Business Critical — cannot sell products online | Scope reduction: launch with catalog + cart MVP; defer filters/sorting | Product Management |
| B2 | Firefox users locked out of application | High | Business Critical — ~5-10% user segment lost | Prioritize Firefox fix before production launch | Engineering |
| B3 | WCAG non-compliance may cause legal exposure | Medium | Compliance Impact — accessibility lawsuits | Fix all P1 accessibility defects before GA | Engineering + Legal |
| B4 | Role switcher missing prevents QA security validation | Medium | Security Impact — role-based access unverified | Add QA-only role switcher component | Engineering |

## Release Risks

| # | Risk | Probability | Impact | Mitigation | Owner |
|---|------|-------------|--------|------------|-------|
| R1 | Release to production with current defect profile | Unacceptable | Reputation damage, user churn, legal liability | Do NOT release until P0+P1 defects resolved | Release Management |
| R2 | Part 3 QA (Checkout) will find additional critical gaps | High | High — cumulative defect count may exceed acceptable threshold | Set clear quality gates before Part 3; no Part 3 until Part 2 gaps assessed | Product Quality |
| R3 | No address management, shipping configuration, or payment integration exists | Certain | Release Blocking — checkout requires all three | Confirm scope of Part 3 and Part 4; may need to extend Sprint 2 | Program Management |

## Testing Risks

| # | Risk | Probability | Impact | Mitigation | Owner |
|---|------|-------------|--------|------------|-------|
| X1 | Firefox mock API issue will affect ALL remaining QA Sprint 2 parts | Certain | High — every Part will have incomplete Firefox coverage | Establish Firefox bypass strategy: use HAR files or dedicated Firefox mock server | QA Engineering |
| X2 | Flaky tests reduce confidence in results | Medium | Medium — flaky pass/fail patterns mask real issues | Stabilize flaky tests (OTP, networkidle, ENOENT) in parallel with feature work | SDET |
| X3 | Test evidence storage growing rapidly (581 files in 2 parts) | Medium | Low — storage cost manageable but CI pipeline may be impacted | Implement evidence retention policy; archive after release | QA Engineering |
