# Learning Progress Platform — Architecture

## Overview

The Enterprise Learning Progress, Academic Journey & Competency Tracking Platform (Sprint 27 Part 7) extends the Student Workspace with comprehensive progress tracking, competency management, milestone achievements, learning timelines, certification readiness assessment, and learning analytics. Operates entirely in Mock Mode.

## Directory Structure

```
learning-progress/
  types.ts                                                — Domain model (10 statuses, 10 stages, 12 competency categories)
  data/mockData.ts                                        — 25 students × 6 courses (~150+ records across 8 generators)
  state/LearningProgressContext.tsx                        — Context with search/filter
  components/
    ProgressStatusBadge.tsx                                — Color-coded status indicator (10 variants)
    ProgressTable.tsx                                      — 6-column learning progress table
    ProgressCard.tsx                                       — Card view for progress records
    DashboardWidget.tsx                                    — Reusable stat widget (5 variants)
    EmptyStates.tsx                                        — 6 typed empty states
    Skeletons.tsx                                          — Table + dashboard skeletons
    LearningTimeline.tsx                                   — Vertical timeline with 9 event types
    CompetencyCard.tsx                                     — Competency display card
    MilestoneCard.tsx                                      — Milestone achievement card
    CertificationReadinessCard.tsx                         — Certification eligibility card
    AnalyticsPanel.tsx                                     — Progress distribution + monthly active students
  pages/
    LearningProgressIndex.tsx                              — Wrapper with sub-navigation tabs
    LearningProgressDashboardPage.tsx                       — Health dashboard (11 widgets + overview)
    StudentProgressPage.tsx                                 — Individual student progress with search/filter/pagination
    CourseProgressPage.tsx                                  — Course-level progress grouped by course
    ModuleProgressPage.tsx                                  — Module-level progress grouped by module
    CompetencyCenterPage.tsx                                — Competency grid with category filter
    MilestoneCenterPage.tsx                                 — Milestone grid with type/status filters
    LearningTimelinePage.tsx                                — Academic journey timeline
    CertificationReadinessPage.tsx                          — Certification eligibility with readiness filter
    AnalyticsPage.tsx                                       — Learning analytics panel
  docs/                                                    — 15 documentation files
```

## Key Design Decisions

- **Mock Mode Only**: No backend, no APIs, no database, no AI engine
- **Reused Components**: StudentSearchBar, StudentPagination from Part 1's shared component library
- **Context-Based State**: Single LearningProgressContext provides all data and filtering
- **Deterministic Mock Data**: Generated once at module load, consistent across renders
- **WCAG 2.2 AA**: All interactive elements keyboard-accessible, proper ARIA attributes
- **Responsive**: Works down to 320px viewport width
