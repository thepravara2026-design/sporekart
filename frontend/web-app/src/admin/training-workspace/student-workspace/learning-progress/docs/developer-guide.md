# Learning Progress Platform — Developer Guide

## Adding a New Progress Status

1. Add to `ProgressStatus` union in `types.ts`
2. Add entry to `PROGRESS_STATUS_LABELS` and `PROGRESS_STATUS_VARIANTS`
3. Update `ProgressStatusBadge` if special styling needed
4. Add handling in `getFilteredProgress` in context if needed for filtering

## Adding a New Competency Category

1. Add to `CompetencyCategory` union in `types.ts`
2. Add entry to `COMPETENCY_CATEGORY_LABELS`
3. Update mock data generator if needed

## Adding a New Milestone Type

1. Add to `MilestoneType` union in `types.ts`
2. Add entry to `MILESTONE_TYPE_LABELS`
3. Update `MilestoneCard` if special styling needed
4. Update mock data `types` array in generator

## Adding a New Timeline Event Type

1. Add to `LearningTimelineEvent['type']` union in `types.ts`
2. Add icon entry in `LearningTimeline.tsx` `eventIcons` map
3. Update mock data generator

## Connecting to a Real API

1. Replace mock data imports in `LearningProgressContext.tsx` with async fetch calls
2. Replace deterministic generation with API responses
3. Add loading/error states
4. Remove `mockData.ts` dependency

## Testing

- No test framework configured — visual verification in browser
- Verify all 9 tabs render correctly
- Test search/filter/pagination on StudentProgress and CompetencyCenter pages
- Verify responsive layout at 320px, 768px, 1024px+
- Run `npm run typecheck` for TypeScript validation
