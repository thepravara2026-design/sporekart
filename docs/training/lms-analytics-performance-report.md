# LMS Analytics — Performance Report (Sprint 26 · Part 9)

## Code Splitting
- Every analytics page is a **lazy** chunk (`React.lazy`) registered in `App.tsx`.
- The sub-layout (`AnalyticsWorkspaceRoute`) is lazy-loaded independently from
  each section page, so switching tabs only loads the requested page chunk.

## Rendering
- All page components and widgets are wrapped in `React.memo`.
- Derived datasets (distributions, trends, funnels, popularity) are memoized
  with `useMemo`, keyed on `filteredCourses`.
- KPIs are computed once per filter change via `useAnalyticsState` and memoized.
- Filter setters use `useCallback` to keep referential stability.

## Data Generation
- Mock time series use a deterministic **seeded PRNG**, so charts are stable
  across renders (no per-render randomness → no needless re-paints).
- No network requests, timers, or polling are introduced.

## Chart Efficiency
- `ResponsiveChart` observes size with a single `ResizeObserver` per widget and
  disconnects on unmount; falls back to a debounced `window.resize` listener only
  when `ResizeObserver` is unavailable.
- Charts receive concrete numeric `width`/`height`, avoiding layout thrash.

## Bundle Notes
- No new heavy dependencies added; all charts reuse existing DS SVG components.
- Wrappers are thin (composition only), adding negligible bundle weight.

## Verification
- `tsc -b --noEmit` passes for the whole project (0 errors).
- Pre-existing, out-of-scope `vite build` CSS gaps in Phase 7 files
  (`ProfileDashboard.tsx`, `DashboardPage.tsx`) are unrelated to this feature.
