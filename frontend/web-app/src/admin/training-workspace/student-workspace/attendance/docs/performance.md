# Attendance Platform — Performance

## Optimizations

| Technique | Application |
|-----------|-------------|
| Lazy loading | `AttendanceIndex` via `React.lazy()` |
| Memoization | All components use `memo()` |
| Context isolation | `AttendanceContext` unmounts when navigating away |
| Filter memoization | `useMemo` for filtered/paginated lists |
| Calendar caching | Calendar data computed only on month/year change |

## Data Volume

- 500 attendance records loaded synchronously in context init
- Filtering is O(n) over records array — negligible cost at 500 items
- Pagination limits render to 10–50 rows per page
- Calendar only renders 31–42 cells per month

## Bundle Size

- Attendance code is a separate lazy-loaded chunk
- No duplicate components — reuses `StudentSearchBar` and `StudentPagination`
- All components are self-contained; no design system dependencies beyond CSS vars

## Avoided Anti-patterns

- No `useEffect` for data loading
- No inline render functions
- No prop drilling beyond 2 levels
- No nested context providers
