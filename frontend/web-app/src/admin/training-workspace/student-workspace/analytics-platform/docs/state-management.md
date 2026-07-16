# Analytics Platform — State Management

## Architecture

AnalyticsProvider wraps the AnalyticsIndexWrapper and provides global state via React Context.

## State Shape

```typescript
interface AnalyticsState {
  studentAnalytics: StudentAnalytics[];
  courseMetrics: CourseMetrics[];
  trainerMetrics: TrainerMetrics[];
  batchMetrics: BatchMetrics[];
  executiveDashboard: ExecutiveDashboard;
  kpis: KPIData[];
  insights: AcademicInsights;
  intelligence: LearningIntelligence;
  searchTerm: string;
  courseFilter: string;
  batchFilter: string;
  performanceFilter: string;
}
```

## Actions

| Method | Description |
|---|---|
| setSearchTerm | Filters student data by name or course |
| setCourseFilter | Filters by course ID |
| setBatchFilter | Filters by batch ID |
| setPerformanceFilter | Filters by performance level |
| getFilteredStudents | Returns filtered student analytics array |

## Data Flow

1. On mount, all mock data is loaded from 10 generators
2. Context provides both raw and filtered data accessors
3. Each page consumes only the slices it needs
4. Filters are shared across all tabs via SharedFilters component
5. Changes to any filter trigger recalculation of getFilteredStudents
