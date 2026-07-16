# Assessment Platform — State Management

## Context Structure

`AssessmentContext` provides:

| State | Type | Description |
|-------|------|-------------|
| `assessments` | `Assessment[]` | All assessments (30) |
| `questionCategories` | `QuestionCategory[]` | All question categories (10) |
| `results` | `Result[]` | All results (100+) |
| `timelineEvents` | `AssessmentTimelineEvent[]` | All timeline events (~240) |
| `dashboardStats` | `AssessmentDashboardStats` | Pre-computed dashboard stats |
| `analytics` | `AcademicAnalytics` | Pre-computed analytics |
| `searchTerm` | `string` | Search filter |
| `statusFilter` | `AssessmentStatus \| 'all'` | Status filter |
| `typeFilter` | `AssessmentType \| 'all'` | Type filter |

## Data Flow

1. `AssessmentProvider` initializes with all mock data synchronously
2. `getFilteredAssessments()` returns filtered view based on searchTerm + statusFilter + typeFilter
3. Pages derive their views via `useMemo` from context state
4. Dashboard stats and analytics are pre-computed in mock data layer

## No Duplicated State

All data lives in a single context. Pages read from the same arrays. Filtering is centralized in `getFilteredAssessments`. No component holds local data copies.
