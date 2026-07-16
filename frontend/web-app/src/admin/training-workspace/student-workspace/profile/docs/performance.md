# Student Profile — Performance

## Rendering Strategy

| Technique | Application |
|-----------|------------|
| Lazy loading | `ProfileIndex` imported via `React.lazy()` in App.tsx |
| Skeleton states | `ProfileHeaderSkeleton`, `ProfileCompletenessSkeleton`, `ProfileInfoCardSkeleton` shown during loading |
| State isolation | `ProfileContext` is local to `ProfileIndex`, unmounts when navigating away from profile |
| No re-render cascading | Context value memoized with `useMemo`; callbacks wrapped in `useCallback` |

## Bundle Size

- Profile code is split into a separate chunk by `React.lazy()`
- No Design System or Registry components re-imported — all profile components are standalone
- Mock data module is tree-shakeable when replaced with real API calls

## Data Flow

1. User navigates to `/admin/training/student-workspace/profile`
2. `ProfileIndex` mounts → `ProfileProvider` initializes with empty state
3. Profile overview renders `ProfileMainPage` with current profile from context
4. Selecting a student via `selectProfile(studentId)` updates context → re-renders affected components only

## Avoided Anti-patterns

- No `useEffect` fetching on every render
- No inline function declarations in render (all via `useCallback`)
- No prop drilling beyond 2 levels
- No image assets (placeholder initials only)

## Future Optimizations

- Virtualize timeline for students with 50+ events
- Memoize completeness calculation for large datasets
- Debounce search/filter when registry-level profile browsing is added
