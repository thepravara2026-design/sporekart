# Assignment Platform — Architecture

## Overview

The Enterprise Assignment & Project Management Platform (Sprint 27 Part 5) extends the Student Workspace with comprehensive assignment lifecycle management, project management, submission workflow, evaluation tracking, and analytics. Operates entirely in Mock Mode.

## Directory Structure

```
assignments/
  types.ts                                     — Domain model (11 statuses, 11 types, 8 project types)
  data/mockData.ts                             — 33 assignments, 12 projects, 60+ submissions, 25+ evaluations
  state/AssignmentContext.tsx                   — Context with search/filter
  components/
    AssignmentStatusBadge.tsx                   — Color-coded status indicator (11 statuses)
    AssignmentTable.tsx                         — 7-column assignment registry table
    AssignmentCard.tsx                          — Card view for assignments
    DashboardWidget.tsx                         — Reusable stat widget
    EmptyStates.tsx                             — 8 typed empty states
    Skeletons.tsx                               — Table + dashboard skeletons
    AssignmentTimeline.tsx                      — 7-stage assignment lifecycle timeline
    AnalyticsPanel.tsx                          — Comprehensive analytics (distribution, course/batch, monthly)
    ProjectCard.tsx                             — Project display card
    SubmissionCard.tsx                          — Submission status card with plagiarism indicator
    EvaluationCard.tsx                          — Evaluation result card with grade and comments
  pages/
    AssignmentIndex.tsx                         — Wrapper with 8-tab sub-navigation
    AssignmentDashboardPage.tsx                  — Executive dashboard with 8 widgets
    AssignmentRegistryPage.tsx                   — Full assignment list with search/filter/pagination
    AssignmentProjectsPage.tsx                   — Project portfolio view
    AssignmentSubmissionsPage.tsx                — Submission queue with review statistics
    AssignmentEvaluationsPage.tsx                — Evaluation queue with pending/completed sections
    AssignmentTimelinePage.tsx                   — 7-stage assignment lifecycle timeline
    AssignmentAnalyticsPage.tsx                  — Detailed analytics with KPIs and distributions
    AssignmentArchivedPage.tsx                   — Archived assignments view
  docs/
    architecture.md
    submission-lifecycle.md
    evaluation-architecture.md
    project-architecture.md
    folder-structure.md
    component-inventory.md
    state-management.md
    responsive.md
    accessibility.md
    performance.md
    future-integration-readiness.md
    developer-documentation.md
    sprint27-part5-completion.md
```

## Data Model

- **33 assignments** across 6 courses, 6 batches, 11 types
- **12 projects** across 8 project types
- **60+ submissions** with 9 submission statuses
- **25+ evaluations** with scored marks and grades
- **~231 timeline events** across 33 assignments × 7 stages
