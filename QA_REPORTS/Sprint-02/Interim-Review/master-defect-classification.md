# SporeKart QA Sprint 2 — Master Defect Classification

**Date:** 2026-07-17  
**Source:** Part 1 (Authentication) + Part 2 (Customer Journey)  
**Total Unique Defects:** 20 (after deduplication and merging)

---

## Deduplication Log

| Removed ID | Merged Into | Reason |
|------------|-------------|--------|
| BUG-CJ-004 | BUG-AUTH-001 | Same Firefox failure across both parts |
| BUG-AUTH-006+007 | BUG-020 | Same OTP input flakiness (WebKit + Mobile Safari) |
| BUG-AUTH-008 | BUG-019 | Same `networkidle` timeout root cause |
| BUG-CJ-014 | BUG-AUTH-009 | Same ENOENT artifact cleanup issue |

---

## Master Defect Matrix

| ID | Original IDs | Module | Priority | Classification | Business Impact | Feature Status | Likely Root Cause | Evidence Reference | Recommendation |
|----|-------------|--------|----------|---------------|----------------|----------------|-------------------|-------------------|----------------|
| BUG-001 | AUTH-001, CJ-004 | Authentication, Cross-Browser | P0 | Browser Compatibility | Business Critical: Firefox users cannot authenticate | Implemented with Defects | `page.route()` mock interception fails in Gecko/ Firefox | test-results/auth-validation-Part-1-*firefox*/traces (11 traces) | Investigate Gecko route interception; add Firefox-specific mock handlers |
| BUG-002 | CJ-001 | Product Catalog | P0 | Implementation Gap | Business Critical: Users cannot browse products | Not Yet Implemented | No product listing page built | /products shows category cards only | Build product listing with grid, pagination, and product cards |
| BUG-003 | CJ-002 | Product Details | P0 | Implementation Gap | Business Critical: Users cannot view product details | Not Yet Implemented | No product detail route exists | All /product/* routes return 404 | Build product detail page with gallery, specs, price, add-to-cart |
| BUG-004 | CJ-003 | Cart | P0 | Implementation Gap | Revenue Impact: Users cannot add items to cart | Not Yet Implemented | No cart page, state, or route built | /cart returns 404 | Build cart with React context, localStorage persistence, /cart route |
| BUG-005 | AUTH-005 | RBAC/Authorization | P1 | Implementation Gap | Security Impact: Role-based access control cannot be verified | Partially Implemented | No role switcher UI for QA/testing mode | Role switcher select not rendered | Add role switcher component enabled only in QA/mock mode |
| BUG-006 | CJ-005 | Search | P1 | Implementation Gap | Customer Blocking: Users cannot search for products | Not Yet Implemented | No product search implementation | /search only covers blog content | Implement product search with Elasticsearch or similar |
| BUG-007 | CJ-006 | Filters | P1 | Implementation Gap | Customer Blocking: Users cannot refine product discovery | Not Yet Implemented | No filter controls built for public catalog | Products page has no filter controls | Add category/price/availability/rating filters |
| BUG-008 | CJ-007 | Sorting | P1 | Implementation Gap | Customer Blocking: Users cannot sort product listings | Not Yet Implemented | No sort controls built for public catalog | Products page has no sort controls | Add price/newest/popularity/alphabetical sorting |
| BUG-009 | AUTH-002 | Accessibility — Login | P1 | Accessibility | Compliance Impact: WCAG 2.1 AA violations | Implemented with Defects | Form elements lack proper accessible labels | accessibility-Part-8-LoginPage trace | Add label associations and fix color contrast on login form |
| BUG-010 | AUTH-003 | Accessibility — Register | P1 | Accessibility | Compliance Impact: WCAG 2.1 AA violation | Implemented with Defects | Form elements lack proper accessible labels | accessibility-Part-8-RegisterPage trace | Fix identified axe-core violation on register form |
| BUG-011 | AUTH-004 | Accessibility — Keyboard | P1 | Accessibility | Usability Impact: Keyboard-only users cannot navigate auth forms | Implemented with Defects | Checkbox not reachable by keyboard Tab | accessibility-Part-8-tab-order trace | Fix tabindex order on auth form elements |
| BUG-012 | CJ-008 | Landing Page | P1 | UX | Usability Impact: Screen reader users cannot identify buttons | Implemented with Defects | Some icon-only buttons lack aria-label or visible text | customer-journey-a11y-perf trace | Add aria-label to all icon-only interactive elements |
| BUG-013 | CJ-009 | Cross-Browser | P1 | QA Infrastructure | Testing Impact: WebKit viewport testing incomplete | Partially Implemented | Comprehensive viewport iteration too slow for 40s timeout | cross-browser test trace | Reduce iteration count or increase timeout for WebKit |
| BUG-014 | CJ-010 | Landing Page | P2 | QA Infrastructure | Testing Impact: Link validation unreliable | Implemented with Defects | Dynamic content loading causes locator timeout | customer-journey-landing trace | Use waitForSelector before link iteration |
| BUG-015 | CJ-011 | Landing Page | P2 | UX | Usability Impact: Page scroll may be broken | Implemented with Defects | CSS `overflow: hidden` prevents page scroll | customer-journey-landing trace | Check body CSS for unintended overflow:hidden |
| BUG-016 | CJ-012 | Navigation | P2 | UX | Usability Impact: Minor URL inconsistency | Implemented with Defects | Trailing slash mismatch in URL comparison | customer-journey-navigation trace | Normalize URL comparison in tests (trivial) |
| BUG-017 | CJ-013 | Product Catalog | P2 | UX | Usability Impact: No loading state during page render | Not Yet Implemented | No skeleton/loading components built | customer-journey-visual trace | Add loading skeleton components |
| BUG-018 | AUTH-009, CJ-014 | Test Infrastructure | P3 | QA Infrastructure | Testing Impact: Artifact cleanup noisy | N/A (infrastructure) | Race condition in Playwright artifact collector | Multiple test traces | Monitor; low priority infrastructure issue |
| BUG-019 | AUTH-008, CJ-015 | Test Infrastructure | P3 | QA Infrastructure | Testing Impact: `networkidle` timeout false failures | Mock Limitation | Polling/SSE connections prevent `networkidle` | session-management + customer-journey traces | Use `load` or `domcontentloaded` instead of `networkidle` |
| BUG-020 | AUTH-006, AUTH-007 | OTP | P2 | QA Infrastructure | Usability Impact: OTP input flaky on iOS/WebKit | Implemented with Defects | `.fill()` timing sensitivity on individual OTP inputs | auth-validation mobile-safari/webkit traces | Add `{ force: true }` or use combined OTP input approach |

---

## Classification Summary

| Classification | Count | % |
|----------------|-------|---|
| Implementation Gap | 7 | 35% |
| QA Infrastructure | 4 | 20% |
| Accessibility | 3 | 15% |
| UX | 3 | 15% |
| Browser Compatibility | 1 | 5% |
| Mock Limitation | 1 | 5% |
| Security | 1 | 5% |

## Priority Distribution

| Priority | Count | % |
|----------|-------|---|
| P0 — Critical | 4 | 20% |
| P1 — High | 9 | 45% |
| P2 — Medium | 4 | 20% |
| P3 — Low | 3 | 15% |
