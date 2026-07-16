# Assignment Platform — Performance

## Optimizations

| Technique | Application |
|-----------|-------------|
| Lazy loading | `AssignmentIndex` via `React.lazy()` |
| Memoization | All components use `memo()` |
| Context isolation | `AssignmentContext` unmounts when navigating away |
| Filter memoization | `useMemo` for filtered/paginated lists |
| Pre-computed data | Dashboard stats and analytics computed in mock data layer |

## Data Volume

- 33 assignments loaded synchronously in context init
- Filtering is O(n) over arrays — negligible cost at 33 items
- Pagination limits render to 10 items per page
- Timeline shows 7 events per assignment, filterable by assignment
- Analytics shows pre-computed summaries only

## Bundle Size

- Assignment code is a separate lazy-loaded chunk
- No duplicate components — reuses `StudentSearchBar` and `StudentPagination`
- All components are self-contained; no design system dependencies beyond CSS vars

## Avoided Anti-patterns

- No `useEffect` for data loading
- No inline render functions
- No prop drilling beyond 2 levels
- No nested context providers
