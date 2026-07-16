# Assignment Platform — Component Inventory

## Page Components

| Component | Sub-route | Description |
|-----------|-----------|-------------|
| `AssignmentIndex` | — | Wrapper with 8-tab sub-navigation |
| `AssignmentDashboardPage` | default | 8 widgets + course/batch breakdown + deadlines |
| `AssignmentRegistryPage` | registry | Full list with search/filter/pagination, table/card toggle |
| `AssignmentProjectsPage` | projects | Project portfolio grid |
| `AssignmentSubmissionsPage` | submissions | Submission queue with stats |
| `AssignmentEvaluationsPage` | evaluations | Pending + completed evaluations |
| `AssignmentTimelinePage` | timeline | 7-stage lifecycle with assignment filter |
| `AssignmentAnalyticsPage` | analytics | KPIs + distribution + trends |
| `AssignmentArchivedPage` | archived | Archived assignments list |

## Domain Components

| Component | Reusable | Description |
|-----------|----------|-------------|
| `AssignmentStatusBadge` | Yes | 11-status color-coded badge |
| `AssignmentTable` | Yes | 7-column accessible table |
| `AssignmentCard` | Yes | Card view for assignments |
| `DashboardWidget` | Yes | Stat display widget |
| `EmptyState` | Yes | 8 typed empty states |
| `AssignmentTableSkeleton` | Yes | Row-based skeleton |
| `AssignmentDashboardSkeleton` | Yes | Dashboard skeleton |
| `AssignmentTimeline` | Yes | Vertical timeline with 7 stages |
| `AnalyticsPanel` | Yes | Full analytics with bars and distribution |
| `ProjectCard` | Yes | Project card with type badge |
| `SubmissionCard` | Yes | Submission with status and plagiarism |
| `EvaluationCard` | Yes | Evaluation with score, grade, feedback |

## Reused Enterprise Components

| Component | Source | Used In |
|-----------|--------|---------|
| `StudentSearchBar` | Part 1 | `AssignmentRegistryPage`, `AssignmentSubmissionsPage` |
| `StudentPagination` | Part 1 | `AssignmentRegistryPage`, `AssignmentSubmissionsPage` |
