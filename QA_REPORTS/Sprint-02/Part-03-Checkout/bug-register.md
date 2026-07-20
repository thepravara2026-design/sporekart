# SporeKart QA Sprint 2 — Bug Register (Checkout)

**Date:** 2026-07-17  
**Scope:** New defects identified during Part 3 (Checkout) execution  

---

## New Bugs

### BUG-CHK-001 — Dashboard Routes Accessible Without Authentication

| Field | Value |
|-------|-------|
| **Severity** | Critical |
| **Priority** | P0 — Critical |
| **Classification** | Security |
| **Module** | Checkout — Protected Routes |
| **Browser** | ALL (Chromium, WebKit, Mobile Chrome, Mobile Safari) |
| **Device** | ALL (Desktop, Mobile) |
| **Business Impact** | Business Critical — Sensitive customer order data (order IDs, prices, payment methods, customer names, SKUs) exposed without authentication |
| **Preconditions** | Navigate to any /dashboard/* or /admin/* route without logging in |
| **Steps to Reproduce** | 1. Open fresh browser session (no cookies) 2. Navigate to http://localhost:5174/dashboard/orders |
| **Expected Result** | Redirect to /login or /auth page |
| **Actual Result** | Full authenticated Orders Dashboard renders with 4 real orders, customer data, payment info, and "Logout" button visible |
| **Root Cause** | Mock mode default role is "Administrator" which bypasses all auth guards. No authentication enforcement on protected routes. |
| **Evidence** | Screenshots, DOM snapshots, and error context in: `checkout-validation-Checko-e3307-ss-Shipping-Unauth-redirect-*/`, `checkout-security-a11y-per-0c08e-auth-admin-orders-redirects-*/`, `checkout-security-a11y-per-ba37b-shboard-addresses-redirects-*/` |
| **Affected Routes** | `/dashboard/orders`, `/dashboard/addresses`, `/admin/orders` (and potentially all /dashboard/* and /admin/* routes) |
| **Suggested Fix** | 1. Add Route Guard component that checks authentication state 2. Set default mock role to "Guest" instead of "Administrator" 3. Implement proper redirect to /login for unauthenticated users 4. Test with role selector set to "Guest" |

---

### BUG-CHK-002 — ARIA Nav Landmark Hidden on Mobile Viewports

| Field | Value |
|-------|-------|
| **Severity** | Medium |
| **Priority** | P2 — Medium |
| **Classification** | Accessibility |
| **Module** | Checkout — Navigation |
| **Browser** | Mobile Chrome, Mobile Safari |
| **Device** | Mobile (375×667 viewport) |
| **Business Impact** | Usability Impact — Screen reader users on mobile cannot navigate via landmark navigation |
| **Preconditions** | Open homepage on mobile viewport (375×667) |
| **Steps to Reproduce** | 1. Set viewport to 375×667 2. Navigate to / 3. Check `<nav aria-label="Primary">` visibility |
| **Expected Result** | Navigation landmark should be visible to assistive technology |
| **Actual Result** | `<nav aria-label="Primary" class="sk-public-nav">` resolves but is hidden via CSS (`visibility: hidden`) |
| **Root Cause** | Responsive design collapses mobile navigation visually but navigation element remains in DOM with `visibility: hidden` |
| **Evidence** | `checkout-security-a11y-per-261ba--Performance-ARIA-landmarks-mobile-chrome/`, `checkout-security-a11y-per-261ba--Performance-ARIA-landmarks-mobile-safari/` |
| **Suggested Fix** | Replace CSS `visibility: hidden` with conditional rendering or use `aria-hidden="true"` when nav is collapsed |

---

## Previously Registered Bugs (Relevant to Checkout)

From Master Bug Register (`QA_REPORTS/Sprint-02/Governance/master-bug-register.md`):

| Bug ID | Title | Severity | Status |
|--------|-------|----------|--------|
| BUG-001 | Firefox: Complete test failure (mock API) | P0 — Critical | Accepted Limitation |
| BUG-009 | LoginPage: WCAG 2.1 AA violations | P1 — High | Open |
| BUG-010 | RegisterPage: WCAG 2.1 AA violation | P1 — High | Open |
| BUG-011 | Keyboard Tab order: checkbox not focusable | P1 — High | Open |
| BUG-012 | Interactive elements lack accessible labels | P1 — High | Open |
| BUG-015 | Page scroll may be prevented by CSS | P2 — Medium | Open |
| BUG-016 | Logo link trailing slash URL inconsistency | P2 — Medium | Open |

---

## Bug Summary

| Priority | Count | Bug IDs |
|----------|-------|---------|
| P0 — Critical | 1 | BUG-CHK-001 |
| P1 — High | 0 | — |
| P2 — Medium | 1 | BUG-CHK-002 |
| **Total New** | **2** | |
| **Legacy Bugs (from Master Register)** | **7** | BUG-001, BUG-009, BUG-010, BUG-011, BUG-012, BUG-015, BUG-016 |
| **Grand Total** | **9** | |

---

## Notes

- BUG-CHK-001 is the most critical finding — all /dashboard/* and /admin/* routes are vulnerable
- Cart/Checkout 404 test failures are NOT bugs — they are reclassified as Implementation Gaps (routes now render placeholder pages)
- No new browser-specific bugs found — all issues are cross-browser consistent
