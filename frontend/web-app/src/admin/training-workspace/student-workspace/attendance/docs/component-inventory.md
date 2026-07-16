# Attendance Platform — Component Inventory

## Page Components

| Component | Sub-route | Description |
|-----------|-----------|-------------|
| `AttendanceIndex` | — | Wrapper with 6-tab sub-navigation |
| `AttendanceDashboardPage` | default | 6 widgets + low-attendance alerts + analytics panel |
| `AttendanceRegisterPage` | register | Full record list with search/filter/pagination, table/card toggle |
| `AttendanceCalendarPage` | calendar | Monthly calendar with color-coded attendance heatmap |
| `AttendanceAnalyticsPage` | analytics | Course/batch/monthly stats with distribution bar + alerts |
| `AttendanceTimelinePage` | timeline | 6-stage attendance lifecycle timeline |
| `PolicyCenterPage` | policies | 4 policy cards with rules and consequences |

## Domain Components

| Component | Reusable | Description |
|-----------|----------|-------------|
| `AttendanceStatusBadge` | Yes | 9-status color-coded badge |
| `AttendanceTable` | Yes | 7-column accessible table |
| `AttendanceCard` | Yes | Card view for records |
| `DashboardWidget` | Yes | Stat display widget |
| `EmptyState` | Yes | 7 typed empty states |
| `AttendanceTableSkeleton` | Yes | Row-based skeleton |
| `AttendanceDashboardSkeleton` | Yes | Dashboard skeleton |
| `CalendarView` | No | Monthly calendar with heatmap colors |
| `AttendanceTimeline` | Yes | Vertical timeline component |
| `AnalyticsPanel` | Yes | Full analytics with bars and distribution |
| `PolicyCard` | Yes | Policy display card |

## Reused Enterprise Components

| Component | Source | Used In |
|-----------|--------|---------|
| `StudentSearchBar` | Part 1 | `AttendanceRegisterPage` |
| `StudentPagination` | Part 1 | `AttendanceRegisterPage` |
