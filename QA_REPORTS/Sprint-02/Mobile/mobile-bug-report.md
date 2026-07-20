# Mobile Bug Report — QA Sprint 2 Part 7

**Release:** v1.0.0-rc1 | **Date:** 2026-07-17 | **Total Bugs:** 14

---

## Bug Summary

| Severity | Count | IDs |
|----------|-------|-----|
| 🔴 CRITICAL | 0 | — |
| 🟠 HIGH | 6 | BUG-MOB-001 through BUG-MOB-006 |
| 🟡 MEDIUM | 6 | BUG-MOB-007 through BUG-MOB-012 |
| 🔵 LOW | 2 | BUG-MOB-013, BUG-MOB-014 |

---

## 🟠 HIGH Bugs

### BUG-MOB-001: Admin KPI Grid Collapses to Single Column on Mobile

| Field | Value |
|-------|-------|
| **Page** | Admin Dashboard (`/admin/dashboard`) |
| **Viewport** | 320-414px |
| **Device** | All mobile devices |
| **Orientation** | Portrait |
| **Browser** | All |
| **Component** | KPI stats grid (Card + Grid) |
| **Environment** | Web-app frontend |

**Steps:**
1. Set viewport to 375x812
2. Navigate to `/admin/dashboard`
3. Observe KPI statistics cards

**Expected:** Cards display in 2-column grid using `repeat(auto-fit, minmax(220px, 1fr))`

**Actual:** Cards stack vertically in single column with excessive whitespace. Only 1-2 cards visible above the fold.

**Root Cause:** Grid `minColumnWidth` of 220px exceeds available width at mobile viewports, causing auto-fit to collapse to single column.

**Recommendation:** Add explicit responsive override: `@media (max-width: 480px) { grid-template-columns: repeat(2, 1fr); }` or reduce `minColumnWidth` to 140px for mobile.

**Severity:** 🟠 HIGH | **Priority:** P1

---

### BUG-MOB-002: Admin Tables Overflow Viewport Without Horizontal Scroll

| Field | Value |
|-------|-------|
| **Page** | Admin Orders (`/admin/orders`), Admin Products, Admin Inventory, Admin Students |
| **Viewport** | 320-480px |
| **Device** | All mobile |
| **Orientation** | Any |
| **Browser** | All |
| **Component** | Table |

**Steps:**
1. Set viewport to 375x812
2. Navigate to any admin table page
3. Observe table rendering

**Expected:** Table container has `overflow-x: auto` with visible horizontal scrollbar

**Actual:** Table columns extend beyond viewport with no scrollbar. Content is completely cut off on the right side. Admin workflows are unusable on mobile.

**Root Cause:** No `overflow-x: auto` wrapper around admin table containers.

**Recommendation:** Wrap all admin tables in a div with `overflow-x: auto; -webkit-overflow-scrolling: touch`.

**Severity:** 🟠 HIGH | **Priority:** P1

---

### BUG-MOB-003: Profile Action Buttons Overlap on Small Mobile

| Field | Value |
|-------|-------|
| **Page** | Customer Profile (`/dashboard/profile`) |
| **Viewport** | 320-375px |
| **Device** | iPhone SE, Small Android |
| **Orientation** | Portrait |
| **Browser** | All |
| **Component** | Action buttons (Security, Edit Profile, Settings) |

**Steps:**
1. Set viewport to 375x667 (iPhone SE)
2. Navigate to `/dashboard/profile`
3. Observe Security, Edit Profile, and Settings buttons

**Expected:** Buttons stack vertically with adequate spacing

**Actual:** Buttons overlap and text wraps awkwardly due to insufficient container width

**Root Cause:** Action buttons use `flex-direction: row` without wrapping. Container width insufficient for 3 buttons.

**Recommendation:** Add `flex-wrap: wrap` and use full-width stacked buttons at mobile breakpoints.

**Severity:** 🟠 HIGH | **Priority:** P1

---

### BUG-MOB-004: Sidebar Drawer Overlay Persists After Navigation

| Field | Value |
|-------|-------|
| **Page** | All pages with sidebar (admin, customer) |
| **Viewport** | 320-767px |
| **Device** | All mobile/tablet |
| **Orientation** | Any |
| **Browser** | All |
| **Component** | Sidebar + Header |

**Steps:**
1. Open hamburger menu → sidebar drawer opens with dark overlay
2. Tap any navigation link
3. Observe page after navigation

**Expected:** Drawer closes, `sidebarOpen = false`, overlay disappears, page content is interactive

**Actual:** Page navigates but the dark overlay backdrop may remain, blocking interaction with page content. User is stuck.

**Root Cause:** Race condition in state update: navigation may trigger before `setSidebarOpen(false)` completes. The `onClick={onNavigate}` fires `onNavigate` which is `() => setSidebarOpen(false)` in parent. If React batches the state update but navigation occurs in the same tick, the overlay state may be stale.

**Recommendation:** Close sidebar synchronously in the click handler (not in a state update that may be batched). Use `useNavigate()` callback to close after navigation or close immediately before navigating.

**Severity:** 🟠 HIGH | **Priority:** P1

---

### BUG-MOB-005: Mobile Checkout Flow Does Not Exist

| Field | Value |
|-------|-------|
| **Page** | Cart / Checkout |
| **Viewport** | All mobile |
| **Device** | All |
| **Orientation** | Any |
| **Browser** | All |
| **Component** | Checkout flow |

**Steps:**
1. Add items to cart (not implemented yet)
2. Navigate to checkout (not implemented yet)
3. Attempt to complete order

**Expected:** A responsive checkout flow that works on mobile: cart review → address → payment → order review → confirm

**Actual:** No checkout page exists. Cart page not implemented. Payment is placeholder only. Orders are managed via mock data in the Orders dashboard.

**Root Cause:** Checkout is Phase 0/placeholder. No cart, address, payment, or order confirmation pages built yet.

**Recommendation:** Build responsive checkout flow with mobile-first design. Ensure touch targets ≥44px, single-column layout, and progressive disclosure of form sections.

**Severity:** 🟠 HIGH | **Priority:** P1

---

### BUG-MOB-006: Touch Targets Below WCAG Minimum Size

| Field | Value |
|-------|-------|
| **Page** | All pages (header, navigation, modals) |
| **Viewport** | 320-480px |
| **Device** | All mobile |
| **Orientation** | Any |
| **Browser** | All |
| **Component** | header, nav buttons, icon buttons |

**Steps:**
1. Inspect hamburger menu button in header
2. Measure clickable area dimensions
3. Check all icon-only buttons in header

**Expected:** All touch targets at least 44x44px (WCAG 2.5.8 Success Criterion)

**Actual:**
- Hamburger menu: ~36x36px (82% of required)
- Close button (modals): ~20x20px icon (45% of required)
- Header action icons: ~36x36px (82% of required)
- Tab buttons: ~30px height (68% of required)
- Product card action buttons: ~36px height (82% of required)

**Root Cause:** No explicit `min-height: 44px; min-width: 44px` on interactive elements in the design system.

**Recommendation:** Add global CSS: `button, a, [role="button"], input[type="submit"] { min-height: 44px; min-width: 44px; }` for mobile. Override with `padding` to maintain visual proportions while meeting touch target requirements.

**Severity:** 🟠 HIGH | **Priority:** P1

---

## 🟡 Medium Bugs

### BUG-MOB-007: ResponsiveModal Shows Desktop Variant on Some Mobile Viewports

| Field | Value |
|-------|-------|
| **Page** | All pages using ResponsiveModal/ResponsiveDialog |
| **Viewport** | 375x812 |
| **Device** | Various |
| **Orientation** | Any |
| **Browser** | Chromium |
| **Component** | ResponsiveModal, ResponsiveDialog |

**Steps:**
1. Set viewport to 375x812
2. Trigger session timeout or any modal
3. Observe modal rendering

**Expected:** Bottom sheet variant (aligned to bottom, full width, rounded top corners, slide-up animation)

**Actual:** Centered desktop modal shown — may overflow viewport, requiring user to scroll to see content.

**Root Cause:** `window.matchMedia('(max-width: 767px)')` initial value may be incorrect on first render before the effect hook runs. Initial state `isMobile = false` causes desktop variant to render briefly.

**Recommendation:** Initialize `isMobile` with `window.innerWidth <= 767` as fallback:
```typescript
const [isMobile, setIsMobile] = useState(() => 
  typeof window !== 'undefined' && window.innerWidth <= 767
);
```

**Severity:** 🟡 MEDIUM | **Priority:** P2

---

### BUG-MOB-008: Breakpoint System Mismatch Between CSS and React

| Field | Value |
|-------|-------|
| **Page** | Global |
| **Viewport** | 400-479px |
| **Device** | All |
| **Orientation** | Any |
| **Browser** | All |
| **Component** | Global |

**Steps:**
1. Set viewport to 400px (between 400-479px)
2. Observe layout behavior

**Expected:** Consistent layout behavior between CSS and React components

**Actual:** CSS breakpoint `--bp-xs: 480px` means at 400px the CSS applies desktop styles (because 400px < 480px, which falls below --bp-xs). But React context `isMobile` reports `true` (xs = 0-479px). Components using React context will render mobile variants, while CSS renders desktop variants.

**Affected Range:** 400-479px

**Affected Components:** All components that rely on CSS breakpoints and React breakpoint context separately:
- Sidebar variant (CSS may show desktop, React may show mobile)
- Grid layouts
- ResponsiveModal/ResponsiveDialog
- Layout components

**Root Cause:** CSS `--bp-xs: 480px` means "start of xs breakpoint starts at 480px". React context `xs: { minWidth: 0, maxWidth: 479 }` means xs is 0-479px. CSS interprets any width < 480px as below-xs (no mobile styles). React interprets any width 0-479px as xs (mobile).

**Recommendation:** Align CSS `--bp-xs` with React's xs range or update React context to match CSS. **Recommended:** Change CSS `--bp-xs: 480px` to `--bp-xs: 640px` to align with React's sm (480-639px).

**Severity:** 🟡 MEDIUM | **Priority:** P2

---

### BUG-MOB-009: No KeyboardAvoidingView in Mobile Apps

| Field | Value |
|-------|-------|
| **Page** | Customer App LoginScreen |
| **Viewport** | All mobile |
| **Device** | All native mobile |
| **Orientation** | Portrait |
| **Browser** | React Native |
| **Component** | LoginScreen |

**Steps:**
1. Open Customer App
2. Tap phone input field
3. Observe phone keyboard appearance

**Expected:** Input stays visible above keyboard. Form scrolls to keep active field visible.

**Actual:** With no `KeyboardAvoidingView` wrapper, the keyboard may overlap and hide the input field. User cannot see what they are typing.

**Root Cause:** All mobile app screens lack `KeyboardAvoidingView` and/or `KeyboardAwareScrollView`.

**Recommendation:** Wrap all form screens with `<KeyboardAvoidingView behavior={Platform.OS === 'ios' ? 'padding' : 'height'}>` and use `ScrollView` for scrollable form content.

**Severity:** 🟡 MEDIUM | **Priority:** P2

---

### BUG-MOB-010: No Orientation Handling in Mobile Apps or Web-App

| Field | Value |
|-------|-------|
| **Page** | Global |
| **Viewport** | All |
| **Device** | All |
| **Orientation** | All |
| **Browser** | All |
| **Component** | Global |

**Steps:**
1. Open app on mobile device in portrait
2. Rotate to landscape
3. Observe layout behavior

**Expected:** Layout adapts to landscape orientation. Content remains usable.

**Actual:** No landscape-specific styles or orientation change handlers exist. Mobile apps may render incorrectly in landscape. Web-app may show desktop breakpoint content which is not ideal for landscape phone.

**Root Cause:** No `@media (orientation: landscape)` CSS. No `screen.orientation` API usage. No `expo-screen-orientation` in mobile apps.

**Recommendation:** Add landscape-specific layouts for key pages. Consider locking certain screens to portrait (login, OTP). Add `expo-screen-orientation` to mobile apps.

**Severity:** 🟡 MEDIUM | **Priority:** P2

---

### BUG-MOB-011: Swipe Gestures Not Supported

| Field | Value |
|-------|-------|
| **Page** | All pages |
| **Viewport** | All mobile |
| **Device** | All |
| **Orientation** | Any |
| **Browser** | All |
| **Component** | Global |

**Steps:**
1. Open mobile app or web-app on mobile
2. Attempt to swipe open sidebar
3. Attempt to swipe-to-delete an order
4. Attempt to swipe-to-dismiss a modal

**Expected:** Common mobile gestures work:
- Swipe right to open sidebar
- Swipe left to delete/reveal actions
- Swipe down to dismiss modal

**Actual:** None of these gestures work. All interactions require precise button taps.

**Root Cause:** No swipe gesture handlers implemented anywhere. `react-native-gesture-handler` is a dependency but not used.

**Recommendation:** 
- Web: Add touch event listeners for swipe gestures on sidebar, modals, and lists
- Mobile: Implement `react-native-gesture-handler` with `Swipeable` component for lists, `PanResponder` for sidebar

**Severity:** 🟡 MEDIUM | **Priority:** P2

---

### BUG-MOB-012: Long-Press Context Menu Not Implemented

| Field | Value |
|-------|-------|
| **Page** | Orders, Products, Training |
| **Viewport** | All |
| **Device** | All |
| **Orientation** | Any |
| **Browser** | All |
| **Component** | Cards, list items |

**Steps:**
1. Navigate to orders page on mobile
2. Long-press an order card
3. Observe context menu

**Expected:** Context menu appears with quick actions: View Details, Track, Cancel, Support

**Actual:** No long-press interaction defined. Only tap (navigate) is available.

**Root Cause:** No `onContextMenu` (web) or long-press gesture handler (mobile) implemented.

**Recommendation:** Implement long-press context menu for product cards, order cards, and training items with relevant quick actions.

**Severity:** 🟡 MEDIUM | **Priority:** P2

---

## 🔵 Low Bugs

### BUG-MOB-013: Mobile Native Apps in Phase 0 — Placeholder Screens Only

| Field | Value |
|-------|-------|
| **Page** | All mobile app screens |
| **Viewport** | All |
| **Device** | All native mobile |
| **Browser** | React Native |
| **Component** | All mobile app components |

**Details:** All four mobile apps (Customer, Dealer, Grower, Admin Companion) are in Phase 0 with only placeholder screens:
- **Customer App:** 2 screens of 15+ (13% complete)
- **Dealer App:** 1 screen of 10+ (10% complete)
- **Grower App:** 1 screen of 10+ (10% complete)
- **Admin Companion:** 0 screens of 10+ (0% complete)

**Severity:** 🔵 LOW | **Priority:** P3

---

### BUG-MOB-014: Admin Companion App Has Zero Source Files

| Field | Value |
|-------|-------|
| **Page** | Admin Companion App |
| **Viewport** | All |
| **Device** | Native mobile |
| **Browser** | React Native |
| **Component** | All |

**Details:** The Admin Companion mobile app has `package.json` and `app.json` but zero source TypeScript files. No screens, components, or services exist in the `src/` directory.

**Severity:** 🔵 LOW | **Priority:** P3

---

## Bug Distribution by Page/Component

| Page/Component | Bugs |
|----------------|------|
| Admin Dashboard | BUG-MOB-001 |
| Admin Tables | BUG-MOB-002 |
| Customer Profile | BUG-MOB-003 |
| Sidebar/Navigation | BUG-MOB-004, BUG-MOB-011 |
| Checkout/Cart | BUG-MOB-005 |
| Header/Global UI | BUG-MOB-006 |
| ResponsiveModal/ResponsiveDialog | BUG-MOB-007 |
| Global Breakpoints | BUG-MOB-008 |
| Mobile Apps (Keyboard) | BUG-MOB-009 |
| Mobile Apps (Orientation) | BUG-MOB-010 |
| Mobile Apps (Gestures) | BUG-MOB-011, BUG-MOB-012 |
| Mobile Apps (Phase 0) | BUG-MOB-013, BUG-MOB-014 |

---

## Blocker Assessment

**No critical bugs** found. However, **6 high-severity bugs** block mobile readiness:

| Bug | Impact | Stop Condition? |
|-----|--------|-----------------|
| BUG-MOB-002 | Admin tables unusable on mobile | ❌ (admin workflows blocked) |
| BUG-MOB-004 | Navigation broken — overlay blocks interaction | ❌ (navigation inaccessible) |
| BUG-MOB-005 | No checkout flow exists | ❌ (e-commerce blocked) |
| BUG-MOB-006 | WCAG violation — touch targets too small | ❌ (accessibility failure) |
| BUG-MOB-001 | Admin KPI data not scannable | ⚠️ |
| BUG-MOB-003 | Profile page unusable on small devices | ⚠️ |

**Stop Conditions Triggered:**
- ❌ Navigation inaccessible? **YES** (BUG-MOB-004 — sidebar overlay bug blocks interaction)
- ❌ All other stop conditions: **NO** (checkout doesn't exist yet so can't be "unusable")

---

*End of Mobile Bug Report — 14 bugs (0 critical, 6 high, 6 medium, 2 low)*
