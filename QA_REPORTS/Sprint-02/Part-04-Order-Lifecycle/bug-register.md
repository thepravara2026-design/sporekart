# SporeKart QA Sprint 2 — Bug Register (Order Lifecycle)

**Date:** 2026-07-17  
**Scope:** New defects identified during Part 4 execution  

---

## New Bugs

### BUG-ORD-001 — Timeline Component Lacks Descriptive Label

| Field | Value |
|-------|-------|
| **Severity** | Low |
| **Priority** | P3 — Low |
| **Classification** | UX / Accessibility |
| **Module** | Order Details — Timeline |
| **Browser** | ALL |
| **Device** | ALL |
| **Business Impact** | Minor — Visual timeline functions correctly but lacks text label |
| **Preconditions** | Navigate to any order detail page with a timeline |
| **Steps** | Check for "Timeline" or "milestone" text in page body |
| **Expected** | Timeline section should have a heading or label like "Order Timeline" |
| **Actual** | Timeline renders as visual component without "Timeline" or "milestone" text |
| **Evidence** | error-context files across all browsers |

---

### BUG-ORD-002 — Order Cards May Not All Render in Initial DOM

| Field | Value |
|-------|-------|
| **Severity** | Medium |
| **Priority** | P2 — Medium |
| **Classification** | Rendering |
| **Module** | Order History — Order List |
| **Browser** | ALL (WebKit intermittent) |
| **Device** | ALL |
| **Business Impact** | Medium — Users may not see all orders without scrolling |
| **Preconditions** | Navigate to `/dashboard/orders` |
| **Steps** | Check body text for all 4 order IDs |
| **Expected** | All 4 order IDs should appear in rendered DOM |
| **Actual** | Some order IDs not present in body text on initial load (lazy rendering) |
| **Evidence** | `order-lifecycle-Phase-3-—--c84fc-ay-order-IDs-status-pricing-*` |

---

### BUG-ORD-003 — Admin Orders Page Returns Empty Page

| Field | Value |
|-------|-------|
| **Severity** | Critical |
| **Priority** | P0 — Critical |
| **Classification** | Functionality |
| **Module** | Admin — Orders |
| **Browser** | ALL |
| **Device** | ALL |
| **Business Impact** | Business Critical — Admin users cannot view or manage orders |
| **Preconditions** | Logged in (default Administrator role) |
| **Steps** | 1. Navigate to `/admin/orders` 2. Wait for network idle |
| **Expected** | DataGrid with 50 mock orders, columns, search, and export |
| **Actual** | Body text length = 0. Empty page renders. |
| **Root Cause** | Likely PermissionGate or FeatureGate blocking rendering in test environment |
| **Evidence** | Screenshots across all 4 browsers showing blank page |

---

### BUG-ORD-004 — Guest Role Not Restricted on Dashboard Orders

| Field | Value |
|-------|-------|
| **Severity** | High |
| **Priority** | P1 — High |
| **Classification** | Security |
| **Module** | Order Lifecycle — Access Control |
| **Browser** | ALL |
| **Device** | ALL |
| **Business Impact** | High — Unauthenticated users (Guest role) can view all customer order data |
| **Preconditions** | Set role to "Guest" via role selector |
| **Steps** | 1. Select "Guest" role 2. Navigate to `/dashboard/orders` |
| **Expected** | Show "Access restricted" or redirect |
| **Actual** | Full orders dashboard renders with all order data |
| **Evidence** | `order-lifecycle-Phase-7-—--b8c26-tricted-for-customer-orders-*` |

---

### BUG-ORD-005 — H1 Font Size 16px on Orders Dashboard (WebKit)

| Field | Value |
|-------|-------|
| **Severity** | Low |
| **Priority** | P3 — Low |
| **Classification** | CSS / Visual |
| **Module** | Order History — Typography |
| **Browser** | WebKit |
| **Device** | Desktop |
| **Business Impact** | Minor — H1 heading at minimum font size |
| **Preconditions** | Navigate to `/dashboard/orders` on WebKit |
| **Steps** | Check computed font-size of h1 element |
| **Expected** | Font size > 16px for proper visual hierarchy |
| **Actual** | Font size is exactly 16px |
| **Evidence** | `order-lifecycle-Phase-12-—-8ac11-d-has-consistent-typography-webkit-*` |

---

### BUG-ORD-006 — Horizontal Scroll on Mobile Orders Dashboard

| Field | Value |
|-------|-------|
| **Severity** | Medium |
| **Priority** | P2 — Medium |
| **Classification** | Responsive Design |
| **Module** | Order History — Layout |
| **Browser** | Mobile Chrome, Mobile Safari |
| **Device** | Mobile (375×667) |
| **Business Impact** | Medium — Users must scroll horizontally to see full content |
| **Preconditions** | Open orders dashboard on mobile viewport |
| **Steps** | Check for horizontal overflow |
| **Expected** | No horizontal scroll (content fits viewport width) |
| **Actual** | Horizontal scroll detected on mobile viewport |
| **Evidence** | `order-lifecycle-Phase-12-—-7d406--scroll-on-orders-dashboard-mobile-*` |

---

## Previously Registered Bugs (Relevant)

| Bug ID | Title | Severity |
|--------|-------|----------|
| BUG-001 | Firefox mock API failure | P0 |
| BUG-CHK-001 | Dashboard routes accessible without auth | P0 |
| BUG-CHK-002 | ARIA nav hidden on mobile | P2 |

---

## Bug Summary

| Priority | New | Legacy | Total |
|----------|-----|--------|-------|
| P0 — Critical | 1 (BUG-ORD-003) | 2 (BUG-001, BUG-CHK-001) | 3 |
| P1 — High | 1 (BUG-ORD-004) | 0 | 1 |
| P2 — Medium | 2 (BUG-ORD-002, BUG-ORD-006) | 1 (BUG-CHK-002) | 3 |
| P3 — Low | 2 (BUG-ORD-001, BUG-ORD-005) | 0 | 2 |
| **Total** | **6** | **3** | **9** |
