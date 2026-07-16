# Enrollment Platform — Performance

## Rendering Strategy

| Technique | Application |
|-----------|------------|
| Lazy loading | `EnrollmentIndex` imported via `React.lazy()` in App.tsx |
| Skeleton states | `EnrollmentTableSkeleton`, `EnrollmentDashboardSkeleton`, `BatchCardSkeleton` shown during loading |
| Memoization | All components use `memo()`, all derived data uses `useMemo()` with proper dependency arrays |
| Context isolation | `EnrollmentContext` is local to the enrollment module, unmounts when navigating away |

## Data Flow Optimizations

1. **Initialization**: Context initializes with all mock data synchronously (no async waterfall)
2. **Filtering**: `useMemo` with dependency tracking — filters recompute only when search/filter/page changes
3. **Pagination**: Only visible rows are rendered (10–50 per page), not the entire 40-item dataset
4. **Detail panel**: `currentRequest` is a single object reference — renders only when selection changes

## Component Optimization

- **EnrollmentTable**: `memo()` prevents re-render when parent re-renders but data hasn't changed
- **EnrollmentCard**: Same pattern with full memoization
- **DashboardPage**: Stat widgets are simple presentational components — no expensive computation
- **CapacityGauge**: SVG rendering is minimal — only 1 circle + 1 text element per gauge

## Bundle Size

- Enrollment code is split into a separate chunk via `React.lazy()`
- No duplicate dependencies from Part 1 or Part 2 — all enrollment components are self-contained
- Mock data module (1.2KB gzipped estimated) is tree-shakeable

## Avoided Anti-patterns

- No `useEffect` for synchronous mock data loading
- No inline function declarations in render (all via `useCallback`)
- No prop drilling beyond 2 levels
- No nested context providers within the module
- No expensive array operations in render (chained `.filter().map().sort()` etc. all in `useMemo`)

## Future Optimizations

- Virtual scrolling for 10,000+ enrollment requests (use `react-window`)
- Server-side search/filter/pagination when API is available
- Debounced search input for large datasets
- Web Worker for completeness/stat calculations on million-scale data
