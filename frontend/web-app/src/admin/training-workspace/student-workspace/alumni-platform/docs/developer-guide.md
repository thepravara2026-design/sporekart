# Developer Guide — Alumni Platform

## Getting Started
1. The platform is auto-wired via `App.tsx` → lazy import → `AlumniIndex.tsx` → `AlumniProvider` + tabbed navigation
2. No API endpoints required — all data comes from `data/mockData.ts`

## Adding a New Page
1. Create page in `pages/` following existing pattern
2. Add to `ALUMNI_NAV_ITEMS` in `types.ts`
3. Add case in `AlumniIndex.tsx` renderSection

## Adding a New Component
1. Create component in `components/`
2. Import types from `../types`
3. Use context via `useAlumni()` hook from `../state/AlumniContext`

## Modifying Mock Data
1. Edit generators in `data/mockData.ts`
2. All generators are deterministic (seeded by array order)
3. Add new export function and wire it in `AlumniContext.tsx`

## Data Flow
```
App.tsx (lazy import)
  → AlumniIndex.tsx (AlumniProvider)
    → Each page (useAlumni() hook)
      → Context filters → client-side pagination → render
```

## Build & Verify
- `npm run typecheck` — Must pass with zero new errors
- All 15 pre-existing errors in `StudentCard.tsx`, `StudentSearchFilter.tsx`, `StudentTable.tsx`, `StudentDashboardPage.tsx`, `StudentDirectoryPage.tsx` are from Sprint 27 Parts 1–3 and must not be fixed
