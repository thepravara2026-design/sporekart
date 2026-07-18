# SporeKart QA Sprint 2 — Bug Register: Part 2 Customer Journey

**Date:** 2026-07-17  
**RC:** v1.0.0-rc1  
**Total Bugs:** 15 (4 Critical, 5 High, 4 Medium, 2 Low)

---

## Critical

### BUG-CJ-001 — No product catalog/listing page
- **Severity:** Critical
- **Area:** Phase 3 — Product Catalog
- **Description:** `/products` shows category cards only, with no actual product listing, grid, or pagination. Users cannot browse products.
- **Business Impact:** Core e-commerce experience is broken. Users cannot discover products.

### BUG-CJ-002 — No product detail page
- **Severity:** Critical
- **Area:** Phase 7 — Product Details
- **Description:** No product detail route exists. All product URL patterns return 404.
- **Business Impact:** Users cannot view product details, images, descriptions, or prices.

### BUG-CJ-003 — No shopping cart
- **Severity:** Critical
- **Area:** Phase 8 — Cart
- **Description:** No `/cart` route, no cart page, no cart state management. Add to Cart buttons have no store wired.
- **Business Impact:** Users cannot add items to cart or proceed to checkout. Complete e-commerce block.

### BUG-CJ-004 — Firefox: Complete test failure
- **Severity:** Critical
- **Area:** Cross-browser
- **Description:** All cross-browser tests fail on Firefox (timeout at 30s). Same root cause as Part 1.
- **Business Impact:** Firefox users cannot use the application.

---

## High

### BUG-CJ-005 — No product search
- **Severity:** High
- **Area:** Phase 4 — Search
- **Description:** `/search` only covers blog content. No product search exists.
- **Business Impact:** Users cannot search for products.

### BUG-CJ-006 — No product filters
- **Severity:** High
- **Area:** Phase 5 — Filters
- **Description:** No filter controls on public product pages (category, price, availability, rating).
- **Business Impact:** Users cannot refine product discovery.

### BUG-CJ-007 — No product sorting
- **Severity:** High
- **Area:** Phase 6 — Sorting
- **Description:** No sort controls on public product pages (price, newest, popularity, alphabetical).
- **Business Impact:** Users cannot sort product listings.

### BUG-CJ-008 — Some interactive elements lack accessible labels
- **Severity:** High
- **Area:** Phase 11 — Accessibility
- **Description:** Some buttons/links on homepage lack `aria-label` or visible text content.
- **Business Impact:** Screen reader users cannot identify interactive elements.

### BUG-CJ-009 — WebKit: Key pages viewport test times out
- **Severity:** High
- **Area:** Cross-browser
- **Description:** The comprehensive viewport iteration across 6 routes × 3 viewports times out at 40s on WebKit.
- **Business Impact:** WebKit viewport rendering not fully verified.

---

## Medium

### BUG-CJ-010 — Broken links test times out
- **Severity:** Medium
- **Area:** Phase 1 — Landing Page
- **Description:** The link validation on homepage times out due to dynamic content loading.
- **Evidence:** customer-journey-landing test trace

### BUG-CJ-011 — Scroll functionality test fails
- **Severity:** Medium
- **Area:** Phase 1 — Landing Page
- **Description:** `window.scrollTo(0, document.body.scrollHeight)` evaluates to scrollY=0, likely due to CSS `overflow: hidden`.
- **Evidence:** customer-journey-landing test trace

### BUG-CJ-012 — Logo link trailing slash mismatch
- **Severity:** Medium
- **Area:** Phase 2 — Navigation
- **Description:** Clicking logo navigates to `/` but test expects `http://localhost:5174` (no trailing slash).
- **Evidence:** customer-journey-navigation test trace

### BUG-CJ-013 — Loading skeletons not present
- **Severity:** Medium
- **Area:** Phase 12 — Visual Review
- **Description:** No skeleton/placeholder elements found on products page (only category cards render directly).
- **Evidence:** customer-journey-visual test trace

---

## Low

### BUG-CJ-014 — ENOENT artifact cleanup failures
- **Severity:** Low
- **Area:** Infrastructure
- **Description:** Several tests report ENOENT errors during Playwright artifact cleanup (ZIP file not found).
- **Impact:** Test results are still valid, but cleanup is noisy.

### BUG-CJ-015 — `networkidle` timeout patterns
- **Severity:** Low
- **Area:** Infrastructure
- **Description:** `waitForLoadState('networkidle')` occasionally times out on dashboard and training pages, possibly due to polling or SSE connections.
- **Recommendation:** Use `load` or `domcontentloaded` instead of `networkidle` for pages with active connections.
