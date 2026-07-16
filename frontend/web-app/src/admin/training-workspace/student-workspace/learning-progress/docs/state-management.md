# Learning Progress Platform — State Management

## Architecture

Single React Context (`LearningProgressContext`) provides all data and filtering operations to all pages and components.

## State Shape

```typescript
interface ProgressState {
  progressRecords: LearningProgress[];
  competencies: Competency[];
  skillMatrices: SkillMatrix[];
  milestones: Milestone[];
  timelineEvents: LearningTimelineEvent[];
  certificationReadiness: CertificationReadiness[];
  healthDashboard: LearningHealthDashboard;
  analytics: LearningAnalytics;
  searchTerm: string;
  statusFilter: ProgressStatus | 'all';
}
```

## Provided Values

| Value | Type | Purpose |
|-------|------|---------|
| All data arrays | `T[]` | Raw data for all 8 entity types |
| searchTerm | `string` | Current search query |
| statusFilter | `ProgressStatus \| 'all'` | Current status filter |
| setSearchTerm | `(term: string) => void` | Update search term |
| setStatusFilter | `(status) => void` | Update status filter |
| getFilteredProgress | `() => LearningProgress[]` | Computed filtered progress list |

## Performance

- Context value memoized with `useMemo`
- State setters wrapped with `useCallback`
- Filter computation memoized in consuming components
- Mock data generated once at module load (deterministic)
