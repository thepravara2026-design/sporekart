# Developer Documentation — Assignment Platform

## Adding a New Assignment

1. Add entry to `titles` array in `generateAssignments()` in `data/mockData.ts`
2. The mock layer handles the rest — status, type, batch, course are auto-assigned

## Adding a New Assignment Type

1. Add to `AssignmentType` union in `types.ts`
2. Add entry to `ASSIGNMENT_TYPE_LABELS` in `types.ts`
3. Add to `ASSIGNMENT_TYPES` array in `data/mockData.ts`

## Adding a New Status

1. Add to `AssignmentStatus` union in `types.ts`
2. Add entry to `ASSIGNMENT_STATUS_LABELS` in `types.ts`
3. Add entry to `ASSIGNMENT_STATUS_VARIANTS` in `types.ts`
4. Add to `ASSIGNMENT_STATUSES` array in `data/mockData.ts`

## Adding a New Page

1. Create page component in `pages/`
2. Add nav item to `ASSIGNMENT_NAV_ITEMS` in `types.ts`
3. Add case to `renderSection` in `pages/AssignmentIndex.tsx`

## Conventions

- All components use `memo()` for performance
- All interactive elements have keyboard handlers
- No inline styles outside component files (using CSS vars)
- No hardcoded strings — use label maps from `types.ts`
- No duplicate logic — reuse `StudentSearchBar` and `StudentPagination`
