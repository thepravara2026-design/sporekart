# Developer Documentation — Assessment Platform

## Adding a New Assessment

1. Add entry to `titles` array in `generateAssessments()` in `data/mockData.ts`
2. The mock layer handles the rest — type, status, batch, course are auto-assigned

## Adding a New Assessment Type

1. Add to `AssessmentType` union in `types.ts`
2. Add entry to `ASSESSMENT_TYPE_LABELS` in `types.ts`
3. Add to `ASSESSMENT_TYPES` array in `data/mockData.ts`

## Adding a New Status

1. Add to `AssessmentStatus` union in `types.ts`
2. Add entry to `ASSESSMENT_STATUS_LABELS` in `types.ts`
3. Add entry to `ASSESSMENT_STATUS_VARIANTS` in `types.ts`
4. Add to `ASSESSMENT_STATUSES` array in `data/mockData.ts`

## Adding a New Question Type

1. Add to `QuestionType` union in `types.ts`
2. Add entry to `QUESTION_TYPE_LABELS` in `types.ts`

## Adding a New Page

1. Create page component in `pages/`
2. Add nav item to `ASSESSMENT_NAV_ITEMS` in `types.ts`
3. Add case to `renderSection` in `pages/AssessmentIndex.tsx`

## Conventions

- All components use `memo()` for performance
- All interactive elements have keyboard handlers
- No inline styles outside component files (using CSS vars)
- No hardcoded strings — use label maps from `types.ts`
- No duplicate logic — reuse `StudentSearchBar` and `StudentPagination`
