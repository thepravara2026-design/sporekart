# Performance Bug Report — QA Sprint 2 Part 9

**Release:** v1.0.0-rc1 | **Date:** 2026-07-17 | **Total Bugs:** 14

---

## Bug Summary

| Severity | Count | IDs |
|----------|-------|-----|
| 🔴 CRITICAL | 0 | — |
| 🟠 HIGH | 4 | BUG-PERF-001 through BUG-PERF-004 |
| 🟡 MEDIUM | 7 | BUG-PERF-005 through BUG-PERF-011 |
| 🔵 LOW | 3 | BUG-PERF-012 through BUG-PERF-014 |

---

## 🟠 HIGH Bugs

### BUG-PERF-001: No Data Virtualization in Admin Tables or Lists

| Field | Value |
|-------|-------|
| **Performance Area** | Rendering / Scalability |
| **Scenario** | Loading pages with 500+ data rows |
| **Environment** | All browsers, all pages with data tables |
| **Preconditions** | Admin navigates to Products, Orders, Inventory, Customers, or Students page |

**Steps:**
1. Navigate to any admin data page (e.g., `/admin/products`)
2. Observe table rendering

**Expected:** Table renders only visible rows (virtualized). Smooth scrolling at 60fps. Memory usage stable regardless of total row count.

**Actual:** All rows rendered eagerly. With 500+ rows, DOM node count exceeds 5000. Scrolling jank increases proportionally with data size.

**Metrics:** DOM nodes: unlimited (current), Render time: O(n) for n rows, Memory: O(n)

**Severity:** 🟠 HIGH | **Priority:** P1

**Recommendation:** Implement react-window FixedSizeList for all admin data tables. Estimated 5000 DOM nodes max per page.

---

### BUG-PERF-002: Timer Leak — setInterval in SessionTimeoutWarning Without Cleanup

| Field | Value |
|-------|-------|
| **Performance Area** | Memory |
| **Scenario** | Admin session timeout countdown |
| **Environment** | Admin workspace |
| **Preconditions** | Admin is logged in |

**Steps:**
1. Navigate to any admin page
2. Wait for session timeout warning to appear
3. Navigate away from admin page

**Expected:** On unmount, setInterval is cleared. No memory leak.

**Actual:** `setInterval` in `SessionTimeoutWarning.tsx:15` runs without cleanup. If component unmounts, interval continues firing, calling `setState` on unmounted component, and incrementing `secondsRemaining` indefinitely.

**Metrics:** Each interval: ~1 call per second indefinitely, leaked callback retains closure scope

**Severity:** 🟠 HIGH | **Priority:** P1

**Recommendation:** Add useEffect cleanup to clear interval:
```tsx
useEffect(() => {
  const interval = setInterval(() => { ... }, 1000);
  return () => clearInterval(interval);
}, []);
```

---

### BUG-PERF-003: No Pagination for Data Pages

| Field | Value |
|-------|-------|
| **Performance Area** | Scalability |
| **Scenario** | Loading all data at once |
| **Environment** | All data pages |
| **Preconditions** | Any user navigates to a data page |

**Steps:**
1. Navigate to OrdersDashboard, CourseLibrary, ProductsPage, or any data page
2. Observe data loading pattern

**Expected:** Data is paginated (10/25/50/100 per page). User can navigate between pages.

**Actual:** All data loaded and rendered at once. No pagination controls visible or wired to data source.

**Metrics:** DOM nodes: unbounded, Render time: O(n), User experience: degrades linearly with data

**Severity:** 🟠 HIGH | **Priority:** P1

**Recommendation:** Wire existing `DataGridPagination` component to all data pages. Add server-side pagination support.

---

### BUG-PERF-004: No HTTP Caching Strategy, Service Worker, or Offline Support

| Field | Value |
|-------|-------|
| **Performance Area** | Network |
| **Scenario** | All page loads / offline usage |
| **Environment** | All browsers |
| **Preconditions** | None |

**Steps:**
1. Load application for the first time
2. Reload the page
3. Go offline and navigate

**Expected:** Static assets should be cached (service worker). Second load should be instant. Offline should show cached content.

**Actual:** No service worker. No caching strategy. Every load is a full network request. Offline shows blank page.

**Metrics:** Cache hit rate: 0%, Offline usability: 0%, Second load time: same as first load

**Severity:** 🟠 HIGH | **Priority:** P1

**Recommendation:** Implement service worker with Workbox for precaching static assets. Cache-first strategy for JS/CSS. Network-first for data.

---

## 🟡 Medium Bugs

### BUG-PERF-005: AppContext Causes Excessive Re-renders

| Field | Value |
|-------|-------|
| **Performance Area** | Rendering |
| **Scenario** | Toggling sidebar or palette |
| **Environment** | All pages |
| **Preconditions** | None |

**Steps:**
1. Open React DevTools Profiler
2. Click hamburger menu button
3. Observe re-render flame graph

**Expected:** Only components that depend on sidebarOpen re-render.

**Actual:** `AppContext.Provider` combines `activeRole`, `paletteOpen`, and `sidebarOpen`. Changing any one value causes ALL context consumers (~15+ components) to re-render, including deeply nested components that only need other parts of the context.

**Metrics:** Re-rendered components per toggle: ~15+, Unnecessary re-renders: ~80%

**Severity:** 🟡 MEDIUM | **Priority:** P2

**Recommendation:** Split AppContext into `UIContext` (paletteOpen, sidebarOpen) and `AuthContext` (activeRole):
```tsx
const UIContext = createContext({ paletteOpen: false, sidebarOpen: false, ... });
const AuthContext = createContext({ activeRole: 'administrator', ... });
```

---

### BUG-PERF-006: Inline Styles in JSX Recreate Objects on Every Render

| Field | Value |
|-------|-------|
| **Performance Area** | Rendering |
| **Scenario** | All component renders |
| **Environment** | All pages |
| **Preconditions** | None |

**Steps:**
1. Inspect any component that uses inline styles (200+ locations)
2. Observe style object creation

**Expected:** Stable style references (CSS classes, styled-components, or extracted objects).

**Actual:** `style={{ ... }}` creates a new object reference on every render. React reconciliation compares objects by reference, causing unnecessary diffing overhead.

**Metrics:** New style objects per render: 200+ (estimated across all components)

**Severity:** 🟡 MEDIUM | **Priority:** P2

**Recommendation:** Extract static inline styles to CSS classes or const objects outside component:
```tsx
// Before
<div style={{ display: 'flex', gap: '16px' }}>

// After
<div className="flex-row gap-md">
```

---

### BUG-PERF-007: Toast Timeout Without Proper Cleanup Refs

| Field | Value |
|-------|-------|
| **Performance Area** | Memory |
| **Scenario** | Rapid page navigation while toasts are visible |
| **Environment** | All pages with toast notifications |
| **Preconditions** | Trigger a toast notification |

**Steps:**
1. Trigger a toast (e.g., download invoice)
2. Immediately navigate to a different page before toast auto-dismisses
3. Repeat multiple times

**Expected:** Toast timer is cleaned up on unmount. No setState on unmounted component.

**Actual:** 15+ components use `setTimeout(() => setToastMessage(null), 3000)` without saving the timer ref. On rapid navigation, these timers continue to fire, calling `setState` on unmounted components.

**Metrics:** Accumulated timer callbacks per navigation cycle: up to 15+ leaked callbacks

**Severity:** 🟡 MEDIUM | **Priority:** P2

**Recommendation:** Create a centralized ToastProvider that manages timers with proper cleanup:
```tsx
const timerRef = useRef<ReturnType<typeof setTimeout>>();
timerRef.current = setTimeout(() => hideToast(id), 3000);
return () => clearTimeout(timerRef.current);
```

---

### BUG-PERF-008: No Preload/Prefetch Hints for Critical Routes

| Field | Value |
|-------|-------|
| **Performance Area** | Network / Startup |
| **Scenario** | Initial page load |
| **Environment** | index.html |
| **Preconditions** | None |

**Steps:**
1. Load application for the first time
2. Observe network waterfall in DevTools

**Expected:** Critical route chunks are preloaded via `<link rel="modulepreload">` or `<link rel="preload">`.

**Actual:** No preload/prefetch hints in index.html. Chunks are loaded lazily when the route is first navigated to.

**Metrics:** Load time for first navigation: full latency for chunk fetch

**Severity:** 🟡 MEDIUM | **Priority:** P2

**Recommendation:** Add preload hints for critical routes in index.html:
```html
<link rel="modulepreload" href="/assets/login-chunk.js">
<link rel="modulepreload" href="/assets/dashboard-chunk.js">
```

---

### BUG-PERF-009: Layout Thrashing in ResizableDrawer

| Field | Value |
|-------|-------|
| **Performance Area** | Rendering |
| **Scenario** | Resizing drawer |
| **Environment** | Admin pages with ResizableDrawer |
| **Preconditions** | Open a resizable drawer |

**Steps:**
1. Open a resizable drawer
2. Start dragging the resize handle
3. Observe performance

**Expected:** Smooth resize at 60fps. No forced reflows.

**Actual:** During resize, `handleMouseMove` reads layout values and writes in the same JavaScript frame. Each pixel of drag may trigger a forced reflow.

**Metrics:** Reflows per resize: 1 per mousemove event

**Severity:** 🟡 MEDIUM | **Priority:** P2

**Recommendation:** Batch read/write operations and throttle with requestAnimationFrame:
```tsx
const handleMouseMove = useCallback((e: MouseEvent) => {
  rafRef.current = requestAnimationFrame(() => {
    // Read + Write batched in same frame
  });
}, []);
```

---

### BUG-PERF-010: No Bundle Analysis or Performance Budget Configured

| Field | Value |
|-------|-------|
| **Performance Area** | Monitoring |
| **Scenario** | CI/CD pipeline |
| **Environment** | Build time |
| **Preconditions** | None |

**Steps:**
1. Run `npm run build`
2. Check for bundle size reports or warnings

**Expected:** Build output includes bundle size report with warnings for oversized chunks.

**Actual:** No `build.chunkSizeWarningLimit` configured. No `vite build --report` used. No performance budget file exists. No CI checks for bundle size regressions.

**Metrics:** Bundle size visibility: 0%, Performance budget compliance: N/A

**Severity:** 🟡 MEDIUM | **Priority:** P2

**Recommendation:** Configure vite.config.ts with chunk size warnings and add bundle analysis to CI.

---

### BUG-PERF-011: Auth Client Has Hardcoded 900ms Latency

| Field | Value |
|-------|-------|
| **Performance Area** | Network |
| **Scenario** | All auth operations |
| **Environment** | Auth pages |
| **Preconditions** | None |

**Steps:**
1. Attempt to send OTP
2. Observe response time

**Expected:** Configurable latency based on network conditions.

**Actual:** AuthClient has `const LATENCY = 900` (line 28) which adds a fixed 900ms delay to every operation. No way to override. No timeout configuration.

**Metrics:** Fixed latency: 900ms regardless of network quality

**Severity:** 🟡 MEDIUM | **Priority:** P2

**Recommendation:** Make latency configurable via environment variable or remove in production build:
```typescript
const LATENCY = Number(import.meta.env.VITE_MOCK_LATENCY_MS) || 0;
```

---

## 🔵 Low Bugs

### BUG-PERF-012: No React.memo on Frequently Re-rendered List Items

| Field | Value |
|-------|-------|
| **Performance Area** | Rendering |
| **Scenario** | Filtering or sorting data lists |
| **Environment** | All list/card views |
| **Preconditions** | None |

**Details:** `EnterpriseOrderCard`, product cards, course cards, and other list item components do not use `React.memo`. When parent state changes (e.g., search term), all items re-render even though their props haven't changed.

**Severity:** 🔵 LOW | **Priority:** P3

**Recommendation:** Wrap list item components with `React.memo`.

---

### BUG-PERF-013: No useDeferredValue for Search/Filter Inputs

| Field | Value |
|-------|-------|
| **Performance Area** | UI Performance |
| **Scenario** | Typing in search/filter inputs |
| **Environment** | All search inputs |
| **Preconditions** | None |

**Details:** Search inputs update state synchronously on every keystroke. With large datasets, the filtering operation blocks the main thread. `useDeferredValue` would allow the input to remain responsive while deferring the filtering work.

**Severity:** 🔵 LOW | **Priority:** P3

**Recommendation:** Use `useDeferredValue` for search inputs that filter large datasets.

---

### BUG-PERF-014: Non-Passive Touch Event Listeners in ResizableDrawer

| Field | Value |
|-------|-------|
| **Performance Area** | Rendering |
| **Scenario** | Touch resize on mobile |
| **Environment** | ResizableDrawer on touch devices |
| **Preconditions** | None |

**Details:** `document.addEventListener('touchmove', handleTouchMove)` (line 70) does not have `{ passive: true }` option. This may block scrolling on mobile devices during touch events.

**Severity:** 🔵 LOW | **Priority:** P3

**Recommendation:** Add `{ passive: true }` if `preventDefault()` is not needed, or keep default for touch events that may need cancellation.

---

*End of Performance Bug Report — 14 bugs (0 critical, 4 high, 7 medium, 3 low)*
