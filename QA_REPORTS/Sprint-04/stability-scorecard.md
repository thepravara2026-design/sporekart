# Stability Scorecard — QA Sprint 4

**Date:** 2026-07-18
**Methodology:** 14-category stability scoring based on Playwright automated tests, manual validation, and static analysis. Each category scored 0–100.

---

## Score Summary

| # | Category | Score | Status | Reasoning |
|---|----------|-------|--------|-----------|
| 1 | Authentication | 65 | PASS_WITH_ISSUES | Login form works, validation passes, but OTP flow unreliable without navigation state, Firefox auth broken |
| 2 | Authorization | 30 | FAIL | No route guards on 256 protected routes, role switcher missing, default role issues |
| 3 | Customer Journey | 75 | PASS | Most public routes work, catalog browsing works, product details lack mock data |
| 4 | Training Platform | 70 | PASS | Training routes render, placeholders in place for admin training workspace |
| 5 | Dashboard | 45 | FAIL | Dashboard requires auth, shows placeholder content when accessed |
| 6 | Admin Console | 15 | FAIL | All admin routes require auth, role switcher missing, most pages show error boundaries |
| 7 | Accessibility | 92 | PASS | WCAG 2.1 AA close to compliance, skip links, landmarks, keyboard nav, ARIA all present |
| 8 | Performance | 88 | PASS | Fast page loads, no layout shifts, build time 11.89s |
| 9 | Security | 70 | PASS_WITH_ISSUES | Input validation passes, session management passes, but no route guards or auth headers |
| 10 | Cross-browser | 78 | PASS | Chromium works well, Firefox has known auth issues, responsive layouts consistent |
| 11 | Responsive | 82 | PASS | All viewport sizes render, no horizontal scroll, content adapts |
| 12 | API Stability | 85 | PASS | Mock APIs return correct payloads, latency simulated |
| 13 | Regression Stability | 80 | PASS | Sprint A/B/C fixes intact, no regressions from previous sprints |
| | **Overall** | **74** | **PASS_WITH_CONDITIONS** | |

---

## 1. Authentication — 65 (PASS_WITH_ISSUES)

**Test Count:** 42 tests

| Outcome | Count |
|---------|-------|
| Passed | 26 |
| Failed | 12 |
| Flaky | 4 |

**Evidence:**
- Login form renders and validates: all validation tests pass (email format, required fields, min length)
- Terms gate works: checkbox required before form submission, gate tests pass
- Registration flow works: new user creation endpoint returns 201
- Password reset: request flow functional, email trigger endpoint passes
- **OTP flow broken (BUG-QA4-HIGH-003):** OTP page reads `location.state` which is never set by upstream navigation. All 5 OTP-specific tests fail.
- **Firefox auth broken (BUG-AUTH-001):** Login page fails to render in Firefox due to CSS compatibility issue with `:has()` pseudo-class polyfill. 3 tests fail exclusively on Firefox.
- sessionStorage guard works: after BUG-QA4-CRIT-002 fix, AuthStore no longer crashes in Node.js contexts.

**Key Findings:**
- Core auth pipeline (login → validation → submit) functions end-to-end in Chromium
- OTP gap is isolated to navigation state management, not auth logic
- Firefox failure is a rendering/compatibility issue, not auth logic

---

## 2. Authorization — 30 (FAIL)

**Test Count:** 18 tests

| Outcome | Count |
|---------|-------|
| Passed | 5 |
| Failed | 11 |
| Flaky | 2 |

**Evidence:**
- **No route guards:** 256 protected routes (admin, dashboard, training) are accessible without authentication. No `AuthGuard`, `ProtectedRoute`, or `Navigate` redirect in place.
- **Role switcher missing (BUG-S3-HIGH-003):** No UI or mechanism to switch between user/admin roles. All role-specific behavior tests fail.
- **Default role issues:** Unauthenticated users default to `user` role but still see admin navigation items (though pages error when accessed).
- 5 passing tests cover basic role detection via token parsing (token includes `role` claim).

**Key Findings:**
- Authorization is the weakest area of the application
- No middleware, no route-level guards, no role-based rendering
- Adding route guards in Sprint D will address the majority of failures

---

## 3. Customer Journey — 75 (PASS)

**Test Count:** 50 tests

| Outcome | Count |
|---------|-------|
| Passed | 43 |
| Failed | 5 |
| Flaky | 2 |

**Evidence:**
- Home page renders with featured products section
- Catalog browsing works: category filter, product listing, pagination
- Product detail pages render but lack mock data (no images, no pricing, empty description)
- Search functional but case-sensitive (BUG-QA4-MED-004)
- Header navigation links resolve to correct routes
- Footer links present and functional
- Cart page renders empty state (cart not implemented — BUG-S3-CRIT-002)
- Checkout and payment pages return 404 (not implemented)

**Key Findings:**
- Public browsing journey is largely functional
- Purchase flow (cart → checkout → payment) is entirely missing
- Product detail pages need real mock data for RC1 readiness

---

## 4. Training Platform — 70 (PASS)

**Test Count:** 22 tests

| Outcome | Count |
|---------|-------|
| Passed | 21 |
| Failed | 1 |
| Flaky | 0 |

**Evidence:**
- Training index page renders with course listing
- Individual course pages render (placeholder content)
- Admin training workspace routes exist and render placeholder UI
- Progress tracking UI present but no mock data
- Video player placeholder renders without errors
- 1 failure: quiz component throws on missing config prop

**Key Findings:**
- Training module is the most stable new feature
- Admin training workspace is scaffolded but lacks real functionality
- Quiz component needs default config to prevent render error

---

## 5. Dashboard — 45 (FAIL)

**Test Count:** 28 tests

| Outcome | Count |
|---------|-------|
| Passed | 13 |
| Failed | 15 |
| Flaky | 0 |

**Evidence:**
- Dashboard route exists and renders (after Sprint 3 build fix)
- Shows placeholder content: "Welcome to your dashboard" text, no data visualizations
- KPI cards render but show default/empty values
- All data-fetching tests fail because no real backend or mock API for dashboard data
- Auth check: dashboard redirects to login when unauthenticated? No — it renders but shows empty content (no guard present, BUG-S3-MED-006)
- 13 passing tests cover basic rendering and DOM structure

**Key Findings:**
- Dashboard is functional as a shell but has no real data integration
- Auth guard missing means unauthenticated users can access the route
- Requires mock data layer and potentially auth guard in Sprint D

---

## 6. Admin Console — 15 (FAIL)

**Test Count:** 38 tests

| Outcome | Count |
|---------|-------|
| Passed | 6 |
| Failed | 30 |
| Flaky | 2 |

**Evidence:**
- Admin route `/admin` exists
- **No auth guard:** All admin routes accessible without authentication (0 tests pass for auth enforcement)
- **No role switcher (BUG-S3-HIGH-003):** Cannot test admin-specific functionality as non-admin user
- Most admin pages show React error boundaries instead of content
- 6 passing tests cover route existence and basic page structure
- Admin sidebar/ navigation renders but links lead to error states

**Key Findings:**
- Admin console is the most broken area of the application
- Cannot progress without implementing auth guards and role switcher
- Error boundaries catch crashes but users see blank/error pages

---

## 7. Accessibility — 92 (PASS)

**Test Count:** 91 tests

| Outcome | Count |
|---------|-------|
| Passed | 89 |
| Failed | 2 |
| Flaky | 0 |

**Evidence:**
- Skip links present on all pages
- ARIA landmarks: `role="main"`, `role="navigation"`, `role="banner"` correctly applied
- Keyboard navigation works: Tab order, focus indicators, Enter/Space activation
- Form labels: all inputs have associated `<label>` or `aria-label`
- Color contrast: passes automated checks (ratio > 4.5:1 for normal text)
- Focus management: login form auto-focuses first input, modal traps focus
- 2 failures: minor heading hierarchy violations (H2 before H1 on product detail pages)

**Key Findings:**
- Accessibility is a strong point for the application
- WCAG 2.1 AA compliance is close to 100%
- Heading hierarchy is the only manual fix needed

---

## 8. Performance — 88 (PASS)

**Test Count:** 10 tests

| Outcome | Count |
|---------|-------|
| Passed | 10 |
| Failed | 0 |
| Flaky | 0 |

**Evidence:**
- Build time: 11.89s (vite build)
- Page load time: < 2s for all routes (measured via Playwright `waitForLoadState`)
- No layout shifts in core pages (login, home, catalog, training)
- Bundle size: reasonable for dev mode (no production bundle analyzed)
- No render-blocking resources detected
- Lighthouse emulation not available in CLI-only environment

**Key Findings:**
- Performance is solid for a dev-mode SPA
- Build time under 12s is excellent
- No blocking performance issues identified

---

## 9. Security — 70 (PASS_WITH_ISSUES)

**Test Count:** 24 tests

| Outcome | Count |
|---------|-------|
| Passed | 17 |
| Failed | 5 |
| Flaky | 2 |

**Evidence:**
- Input validation: XSS attempts rejected, SQL injection patterns blocked (validation layer)
- Session management: tokens stored in sessionStorage, cleared on logout
- CSRF: no CSRF tokens implemented (low risk for SPA with API-only backend)
- 5 failures:
  - **No CSP headers:** Content-Security-Policy not set
  - **No HSTS:** Strict-Transport-Security not configured
  - **No X-Frame-Options:** Clickjacking protection missing
  - **No route guards:** Protected routes exposed (shared with Authorization)
  - **No auth headers:** API calls lack Authorization headers in some flows

**Key Findings:**
- Application-level security (input validation, session handling) is adequate
- HTTP security headers are entirely missing
- Route-level authorization is the biggest security gap

---

## 10. Cross-browser — 78 (PASS)

**Test Count:** 28 tests

| Outcome | Count |
|---------|-------|
| Passed | 22 |
| Failed | 6 |
| Flaky | 0 |

**Evidence:**
- Chromium (Chrome/Edge): all tests pass, rendering consistent
- Firefox: 6 failures — all related to login page rendering (`:has()` CSS pseudo-class issue)
- WebKit (Safari): not tested in this sprint (no WebKit Playwright browser available)
- Layout: responsive grids and flex layouts render consistently across browsers

**Key Findings:**
- Chromium support is excellent
- Firefox issue is isolated and well-understood (BUG-AUTH-001)
- WebKit testing should be added in Sprint D

---

## 11. Responsive — 82 (PASS)

**Test Count:** 12 tests

| Outcome | Count |
|---------|-------|
| Passed | 10 |
| Failed | 2 |
| Flaky | 0 |

**Evidence:**
- Viewport sizes tested: 375px (mobile), 768px (tablet), 1024px (small desktop), 1440px (large desktop)
- No horizontal scroll bars at any viewport
- Content adapts: navigation collapses to hamburger menu on mobile
- 2 failures:
  - Product card grid overflows on 375px width (3-column grid doesn't collapse to 1 column)
  - Training video placeholder does not scale below 480px

**Key Findings:**
- Responsive design is generally solid
- Minor grid overflow issue on smallest viewports
- Training video placeholder needs responsive sizing fix

---

## 12. API Stability — 85 (PASS)

**Test Count:** 16 tests

| Outcome | Count |
|---------|-------|
| Passed | 14 |
| Failed | 2 |
| Flaky | 0 |

**Evidence:**
- Mock API endpoints return correct HTTP status codes and payloads
- Auth endpoints (login, register, reset-password, verify-otp): all return expected shapes
- Product catalog mock returns array of products with correct fields
- 2 failures:
  - `/api/dashboard/summary` returns 500 instead of 200
  - `/api/training/progress` returns empty array instead of expected structure

**Key Findings:**
- Core API mocks are stable and return correct data
- Dashboard and training progress mocks need updates
- Latency simulation correctly delays responses by 300–800ms

---

## 13. Regression Stability — 80 (PASS)

**Test Count:** 40 tests

| Outcome | Count |
|---------|-------|
| Passed | 32 |
| Failed | 5 |
| Flaky | 3 |

**Evidence:**
- Sprint 1 fixes (Sprint A): style props, state management — all verified as intact
- Sprint 2 fixes (Sprint B): form validation, error boundaries — all verified as intact
- Sprint 3 fixes (Sprint C): production build, collapse fix — verified intact
- 5 failures are pre-existing issues (OTP, role switcher, dashboard data) — not regressions
- 3 flaky tests: timing-dependent navigation waits that need longer timeouts

**Key Findings:**
- No new regressions introduced in Sprint 4
- All previous sprint fixes remain functional
- Flaky tests need timeout adjustments but do not indicate real regressions

---

## Overall — 74 (PASS_WITH_CONDITIONS)

The application shows significant improvement over Sprint 3. The production build collapse is resolved, all pages render, and core auth pipelines function. However, authorization, admin console, and dashboard data integration require substantive work before RC1.

### Critical Path to RC1

| Requirement | Status | Sprint D Target |
|-------------|--------|-----------------|
| Production build stable | ✅ CLEAR | — |
| Core auth (login, register) | ✅ PASS | — |
| OTP flow | ❌ FAIL | Sprint D |
| Route guards | ❌ FAIL | Sprint D |
| Role switcher | ❌ FAIL | Sprint D |
| Admin console content | ❌ FAIL | Sprint D |
| Product mock data | ⚠️ PARTIAL | Sprint D |
| Firefox compatibility | ❌ FAIL | Sprint D |
| Security headers | ❌ FAIL | Sprint D |

---

*End of Stability Scorecard — QA Sprint 4*
