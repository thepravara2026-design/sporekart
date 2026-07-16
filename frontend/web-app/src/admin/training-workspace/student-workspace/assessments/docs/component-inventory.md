# Assessment Platform — Component Inventory

## Page Components

| Component | Sub-route | Description |
|-----------|-----------|-------------|
| `AssessmentIndex` | — | Wrapper with 8-tab sub-navigation |
| `AssessmentDashboardPage` | default | 8 widgets + course/batch performance + recent results |
| `AssessmentRegistryPage` | registry | Full list with search/filter/pagination, table/card toggle |
| `AssessmentBuilderPage` | builder | Assessment creation with templates and draft list |
| `QuestionBankPage` | question-bank | Question categories with stats and details |
| `ExaminationCenterPage` | examinations | Active/scheduled/completed exam sections |
| `ResultCenterPage` | results | Results with search, pass/fail stats, pagination |
| `AssessmentAnalyticsPage` | analytics | KPIs + difficulty distribution + course/batch + trends |
| `AssessmentArchivedPage` | archived | Archived assessments list |

## Domain Components

| Component | Reusable | Description |
|-----------|----------|-------------|
| `AssessmentStatusBadge` | Yes | 11-status color-coded badge |
| `AssessmentTable` | Yes | 8-column accessible table |
| `AssessmentCard` | Yes | Card view for assessments |
| `DashboardWidget` | Yes | Stat display widget |
| `EmptyState` | Yes | 7 typed empty states |
| `AssessmentTableSkeleton` | Yes | Row-based skeleton |
| `AssessmentDashboardSkeleton` | Yes | Dashboard skeleton |
| `AssessmentTimeline` | Yes | Vertical timeline with 8 stages |
| `AnalyticsPanel` | Yes | Full analytics with bars and distribution |
| `ResultCard` | Yes | Result with score, grade, performance band |
| `QuestionCategoryCard` | Yes | Category with outcomes and tags |

## Reused Enterprise Components

| Component | Source | Used In |
|-----------|--------|---------|
| `StudentSearchBar` | Part 1 | `AssessmentRegistryPage`, `ResultCenterPage` |
| `StudentPagination` | Part 1 | `AssessmentRegistryPage`, `ResultCenterPage` |
