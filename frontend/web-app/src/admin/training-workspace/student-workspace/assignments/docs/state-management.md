# Assignment Platform — State Management

## Context Structure

`AssignmentContext` provides:

| State | Type | Description |
|-------|------|-------------|
| `assignments` | `Assignment[]` | All assignments (33) |
| `projects` | `Project[]` | All projects (12) |
| `submissions` | `Submission[]` | All submissions (60+) |
| `evaluations` | `Evaluation[]` | All evaluations (25+) |
| `timelineEvents` | `AssignmentTimelineEvent[]` | All timeline events (~231) |
| `dashboardStats` | `AssignmentDashboardStats` | Pre-computed dashboard stats |
| `analytics` | `AssignmentAnalytics` | Pre-computed analytics |
| `searchTerm` | `string` | Search filter |
| `statusFilter` | `AssignmentStatus \| 'all'` | Status filter |
| `typeFilter` | `AssignmentType \| 'all'` | Type filter |

## Data Flow

1. `AssignmentProvider` initializes with all mock data synchronously
2. `getFilteredAssignments()` returns filtered view based on searchTerm + statusFilter + typeFilter
3. `getFilteredSubmissions()` returns filtered view based on searchTerm
4. Pages derive their views via `useMemo` from context state
5. Dashboard stats and analytics are pre-computed in mock data layer

## No Duplicated State

All data lives in a single context. Pages read from the same arrays. Filtering is centralized in `getFilteredAssignments` and `getFilteredSubmissions`. No component holds local data copies.
