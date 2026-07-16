# Attendance Platform — Architecture

## Overview

The Enterprise Attendance & Learning Presence Management Platform (Sprint 27 Part 4) extends the Student Workspace with comprehensive attendance tracking, calendar visualization, analytics, and policy management. Operates entirely in Mock Mode.

## Directory Structure

```
attendance/
  types.ts                                     — Domain model (9 statuses, 6 pages)
  data/mockData.ts                             — 25 students × 10–30 sessions (~500 records)
  state/AttendanceContext.tsx                   — Context with search/filter
  components/
    AttendanceStatusBadge.tsx                   — Color-coded status indicator
    AttendanceTable.tsx                         — 7-column attendance register table
    AttendanceCard.tsx                          — Card view for attendance records
    DashboardWidget.tsx                         — Reusable stat widget
    EmptyStates.tsx                             — 7 typed empty states
    Skeletons.tsx                               — Table + dashboard skeletons
    CalendarView.tsx                            — Monthly calendar with attendance heatmap
    AttendanceTimeline.tsx                      — Student attendance lifecycle timeline
    AnalyticsPanel.tsx                          — Comprehensive analytics (course/batch/monthly)
    PolicyCard.tsx                              — Policy display card
  pages/
    AttendanceIndex.tsx                         — Wrapper with sub-navigation tabs
    AttendanceDashboardPage.tsx                  — Executive dashboard with alerts
    AttendanceRegisterPage.tsx                   — Full attendance register with search/filter/pagination
    AttendanceCalendarPage.tsx                   — Monthly calendar with color-coded days
    AttendanceAnalyticsPage.tsx                  — Detailed analytics with low-attendance monitoring
    AttendanceTimelinePage.tsx                   — Attendance lifecycle timeline
    PolicyCenterPage.tsx                         — Policy rules display
  docs/
    architecture.md
    component-inventory.md
    state-management.md
    responsive.md
    accessibility.md
    performance.md
    future-integration-readiness.md
    sprint27-part4-completion.md
```

## Data Model

- **AttendanceRecord**: 500+ records across 25 students
- **AttendanceSession**: Auto-grouped from records by batch+date
- **CalendarMonth**: Computed per month with training/holiday days
- **AttendanceSummary**: Course-wise, batch-wise, and monthly trends
- **AttendancePolicy**: 4 default policies (Standard, Corporate, Government, Medical)
