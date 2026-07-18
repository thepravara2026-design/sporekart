# Compatibility Bug Report — QA Sprint 2 Part 6

**Release:** v1.0.0-rc1 | **Date:** 2026-07-17

---

## Bug Summary

| Total | Critical | High | Medium | Low |
|-------|----------|------|--------|-----|
| 8 | 0 | 4 | 4 | 0 |

---

## 🟠 High Bugs

### BUG-COMP-001: Admin KPI Grid Collapses Poorly on Mobile

| Field | Value |
|-------|-------|
| **Page** | Admin Dashboard (`/admin/dashboard`) |
| **Browser** | All |
| **OS** | All |
| **Viewport** | ≤480px |
| **Preconditions** | Admin user navigates to dashboard on mobile |
| **Steps** | 1. Set viewport to 375x812<br>2. Navigate to `/admin/dashboard`<br>3. Observe KPI card layout |
| **Expected** | KPI cards display in 2-column grid or horizontal scrollable row |
| **Actual** | KPI cards stack vertically in single column with excessive whitespace between them. Only 1 card visible above the fold. |
| **Severity** | 🟠 HIGH |
| **Priority** | P1 |
| **Root Cause** | Missing responsive grid breakpoint for KPI container at mobile viewport |
| **Recommendation** | Add `grid-template-columns: repeat(2, 1fr)` at `max-width: 480px` for KPI grid |

### BUG-COMP-002: Admin Tables Overflow Without Horizontal Scroll

| Field | Value |
|-------|-------|
| **Page** | Admin Orders, Products, Students, Inventory tables |
| **Browser** | All |
| **OS** | All |
| **Viewport** | ≤480px |
| **Preconditions** | None |
| **Steps** | 1. Set viewport to 375x812<br>2. Navigate to `/admin/orders` or any admin table page<br>3. Observe table rendering |
| **Expected** | Table container has `overflow-x: auto` and horizontal scrollbar appears |
| **Actual** | Table columns overflow the viewport with no scrollbar. Content is cut off on the right. |
| **Severity** | 🟠 HIGH |
| **Priority** | P1 |
| **Root Cause** | No `overflow-x: auto` wrapper around table containers |
| **Recommendation** | Wrap tables in a div with `overflow-x: auto; -webkit-overflow-scrolling: touch` |

### BUG-COMP-003: Profile Page Buttons Overlap on Mobile

| Field | Value |
|-------|-------|
| **Page** | Customer Profile (`/dashboard/profile`) |
| **Browser** | All |
| **OS** | All |
| **Viewport** | ≤375px |
| **Preconditions** | None |
| **Steps** | 1. Set viewport to 375x667 (iPhone SE)<br>2. Navigate to `/dashboard/profile`<br>3. Observe Security, Edit Profile, and Settings buttons |
| **Expected** | Buttons stack vertically with adequate spacing |
| **Actual** | Buttons overlap and text wraps awkwardly due to insufficient container width |
| **Severity** | 🟠 HIGH |
| **Priority** | P1 |
| **Root Cause** | Profile action buttons use `flex-direction: row` without wrapping at small viewports |
| **Recommendation** | Add `flex-wrap: wrap` and reduce gap or use full-width stacked buttons at mobile |

### BUG-COMP-004: Sidebar Drawer Overlay Remains After Navigation

| Field | Value |
|-------|-------|
| **Page** | All pages with sidebar (admin, customer) |
| **Browser** | All |
| **OS** | All |
| **Viewport** | ≤767px |
| **Preconditions** | Mobile viewport |
| **Steps** | 1. Hamburger menu → opens sidebar drawer<br>2. Tap a navigation link<br>3. Observe page content |
| **Expected** | Drawer closes, sidebarOpen → false, overlay disappears |
| **Actual** | Page navigates but the dark overlay backdrop may remain, blocking interaction with page content |
| **Severity** | 🟠 HIGH |
| **Priority** | P1 |
| **Root Cause** | Timing issue in `handleSidebarNavigate`: state update race condition between navigation and `setSidebarOpen(false)` |
| **Recommendation** | Use `useNavigate()` callback or `useEffect` to close sidebar after navigation completes. Or close the sidebar immediately in the click handler before navigation. |

---

## 🟡 Medium Bugs

### BUG-COMP-005: Hamburger Button Below Touch Target Minimum

| Field | Value |
|-------|-------|
| **Page** | All pages (header) |
| **Browser** | All |
| **OS** | All |
| **Viewport** | ≤767px |
| **Preconditions** | Mobile viewport |
| **Steps** | 1. Inspect hamburger menu button in header<br>2. Measure clickable area |
| **Expected** | Touch target at least 44x44px (WCAG 2.5.8) |
| **Actual** | Hamburger button is approximately 36x36px |
| **Severity** | 🟡 MEDIUM |
| **Priority** | P2 |
| **Recommendation** | Add `min-height: 44px; min-width: 44px; padding: 4px` to `.sk-header__menu` |

### BUG-COMP-006: ResponsiveModal Shows Desktop Variant on Some Mobile Viewports

| Field | Value |
|-------|-------|
| **Page** | All pages using ResponsiveModal/ResponsiveDialog |
| **Browser** | Chromium |
| **Viewport** | 375x812 |
| **Preconditions** | Trigger a modal dialog |
| **Steps** | 1. Set viewport to 375x812<br>2. Trigger session timeout or any modal<br>3. Observe modal rendering |
| **Expected** | Bottom sheet variant (aligned to bottom, full width, rounded top corners) |
| **Actual** | Centered desktop modal shown — may overflow viewport |
| **Severity** | 🟡 MEDIUM |
| **Priority** | P2 |
| **Root Cause** | `window.matchMedia('(max-width: 767px)')` may evaluate to false on initial render in some mobile browsers. Media query listener may not fire on page load. |
| **Recommendation** | Initialize `isMobile` state with `window.innerWidth <= 767` as fallback. Add CSS media query as primary detection mechanism. |

### BUG-COMP-007: Breakpoint System Mismatch

| Field | Value |
|-------|-------|
| **Page** | Global |
| **Browser** | All |
| **Viewport** | 0-479px |
| **Preconditions** | Viewport width between 0-479px |
| **Steps** | 1. Set viewport to 400px<br>2. Observe layout behavior |
| **Expected** | Consistent layout behavior between CSS and React components |
| **Actual** | CSS breakpoint `--bp-xs: 480px` means desktop styles apply at 400px. But React context `isMobile` (0-639px) reports mobile. Component behavior may differ from CSS layout at 400-479px. |
| **Severity** | 🟡 MEDIUM |
| **Priority** | P2 |
| **Recommendation** | Align CSS `--bp-xs` to match React context `isMobile` range (0-639px) or add explicit CSS breakpoint for 0-479px range |

### BUG-COMP-008: Firefox CSS Grid Rendering Differences

| Field | Value |
|-------|-------|
| **Page** | Admin Grid, KPI Cards |
| **Browser** | Firefox |
| **OS** | All |
| **Viewport** | All |
| **Preconditions** | None |
| **Steps** | 1. Open app in Firefox<br>2. Navigate to admin dashboard<br>3. Compare with Chromium rendering |
| **Expected** | Identical layout to Chromium |
| **Actual** | Admin grid content misalignment due to CSS Grid `gap` property rendering differences between Firefox and Chromium |
| **Severity** | 🟡 MEDIUM |
| **Priority** | P2 |
| **Recommendation** | Test grid layouts in Firefox specifically. Consider using `margin` instead of `gap` for Firefox compatibility, or apply Firefox-specific grid styles. |

---

## Bug Distribution

| Page/Aspect | Bugs |
|-------------|------|
| Admin Dashboard | BUG-COMP-001, BUG-COMP-008 |
| Admin Tables | BUG-COMP-002 |
| Customer Profile | BUG-COMP-003 |
| Sidebar/Navigation | BUG-COMP-004 |
| Header | BUG-COMP-005 |
| Modals | BUG-COMP-006 |
| Global Breakpoints | BUG-COMP-007 |
| Firefox Specific | BUG-COMP-008 |

---

## Blocker Assessment

No **critical** bugs found. However, **4 high-severity bugs** affect mobile usability significantly:

| Bug | Impact |
|-----|--------|
| BUG-COMP-002 | Admin tables unreadable on mobile |
| BUG-COMP-004 | Navigation broken on mobile (overlay blocks interaction) |
| BUG-COMP-003 | Profile page unusable on small devices |
| BUG-COMP-001 | Admin dashboard KPI data not scannable on mobile |

**These 4 high bugs must be resolved before declaring mobile readiness.**

---

*End of Compatibility Bug Report — 8 bugs (0 critical, 4 high, 4 medium, 0 low)*
