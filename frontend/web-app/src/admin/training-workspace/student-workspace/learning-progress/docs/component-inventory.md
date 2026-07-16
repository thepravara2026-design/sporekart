# Learning Progress Platform — Component Inventory

## Components

| Component | Type | Inputs | Purpose |
|-----------|------|--------|---------|
| `ProgressStatusBadge` | Display | `status: ProgressStatus` | Color-coded status badge (10 variants) |
| `ProgressTable` | Display | `records, onSelect?, selectedId?` | 6-column tabular view of progress records |
| `ProgressCard` | Display | `record, onSelect?, selected?` | Card view for individual progress record |
| `DashboardWidget` | Display | `label, value, variant?, subtitle?` | Reusable stat widget (default/success/warning/danger/info) |
| `EmptyStates` | Display | `type: EmptyStateType, onClearFilters?` | Typed empty state placeholders (6 types) |
| `Skeletons` | Display | (none) | Loading skeletons for table and dashboard |
| `LearningTimeline` | Display | `events: LearningTimelineEvent[]` | Vertical timeline with 9 event types |
| `CompetencyCard` | Display | `competency: Competency` | Competency display card with level/percentage |
| `MilestoneCard` | Display | `milestone: Milestone` | Milestone achievement card with status |
| `CertificationReadinessCard` | Display | `readiness: CertificationReadiness` | Certification eligibility card with progress bars |
| `AnalyticsPanel` | Display | `analytics: LearningAnalytics` | Analytics with progress distribution + monthly bar chart |

## Pages

| Page | Route Section | Purpose |
|------|--------------|---------|
| `LearningProgressIndex` | (wrapper) | Tab-based navigation between all sections |
| `LearningProgressDashboardPage` | `learning-progress` (default) | Health dashboard with 11 widgets |
| `StudentProgressPage` | `learning-progress/students` | Individual student progress with search/filter |
| `CourseProgressPage` | `learning-progress/courses` | Course-level progress grouped by course |
| `ModuleProgressPage` | `learning-progress/modules` | Module-level progress grouped by module |
| `CompetencyCenterPage` | `learning-progress/competencies` | Competency grid with category filter |
| `MilestoneCenterPage` | `learning-progress/milestones` | Milestone grid with type/status filters |
| `LearningTimelinePage` | `learning-progress/timeline` | Academic journey timeline |
| `CertificationReadinessPage` | `learning-progress/certification` | Certification eligibility with readiness filter |
| `AnalyticsPage` | `learning-progress/analytics` | Learning analytics panel |
