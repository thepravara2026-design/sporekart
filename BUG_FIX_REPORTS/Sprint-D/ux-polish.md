# UX Polish Register — Bug Fix Sprint D

**Date:** 2026-07-18
**Source:** QA Sprint 4 Customer Journey, UX, and Navigation test results

---

## UX Issues Identified for Polish

| # | Issue | Severity | Current Behavior | Desired Behavior | Effort | Sprint D Fit |
|---|-------|----------|-----------------|------------------|--------|-------------|
| 1 | OTP navigation broken when arriving directly | HIGH | Page reads `location.state` which is never set; shows empty/error state | Redirect to login or persist state in sessionStorage; show user-friendly message | 0.5–1 day | ✅ Phase 2 |
| 2 | Dashboard shows placeholder instead of redirecting to login | MEDIUM | Unauthenticated users see empty dashboard shell | Redirect to /login with return URL | 0.5 day | ✅ Phase 3 |
| 3 | OTP form lacks inline validation messages | MEDIUM | No feedback for invalid OTP input | Show inline error messages (invalid format, expired code) | 0.5 day | ✅ Phase 3 |
| 4 | Search is case-sensitive | MEDIUM | `"SPORTS"` ≠ `"sports"` | Case-insensitive search: `toLowerCase()` on both query and data | 0.5 day | ✅ Phase 3 |
| 5 | Product detail pages show empty content | HIGH | Product detail has no images, pricing, or descriptions | Show mock product data (image placeholder, price, description) | 1 day | ✅ Phase 2 |
| 6 | Training page missing document title | LOW | Shows default "React App" in browser tab | Show "Training — SporeKart" | 0.1 day | ✅ Phase 3 |
| 7 | Firefox login page does not render | HIGH | White screen on Firefox due to `:has()` CSS | Login form renders correctly in all major browsers | 0.5–1 day | ✅ Phase 2 |
| 8 | No role indicator for authenticated users | HIGH | Users cannot see or switch their current role | Show role badge in header; add role switcher dropdown | 2–3 days | ✅ Phase 2 |

---

## UX Improvement Opportunities (Beyond Bug Fixes)

| # | Opportunity | Current State | Proposed Improvement | Effort | Priority |
|---|-------------|---------------|---------------------|--------|----------|
| A | Empty cart state is generic | "Your cart is empty" text | Add illustration + "Browse products" CTA | 0.5 day | Low |
| B | No loading states on auth forms | Form is immediately interactive | Add skeleton/loading spinner during OTP send/verify | 0.5 day | Low |
| C | No success animation after login | Instant redirect | Brief success toast/checkmark animation | 0.5 day | Low |
| D | 404 page is unstyled | Default Vite 404 | Branded 404 with navigation links | 1 day | Low |

---

## UX Testing Results (QA Sprint 4)

| Test Suite | Pass | Fail | Flaky | Score |
|-----------|------|------|-------|-------|
| Navigation & Routing | 15 | 3 | 1 | 83% |
| Form UX (validation, feedback) | 12 | 4 | 2 | 75% |
| Content Display | 10 | 3 | 0 | 77% |
| Cross-browser UX | 8 | 4 | 1 | 67% |

---

## Polish Work Items (Phase 3 — Week 4)

1. **Dashboard redirect** (MED-001) — ~4 hours
2. **OTP validation messages** (MED-002) — ~4 hours
3. **Case-insensitive search** (MED-003) — ~4 hours
4. **Training page title** (LOW-002) — ~1 hour
5. **ARIA labels on social login** (MED-004) — ~2 hours (also in a11y polish)
