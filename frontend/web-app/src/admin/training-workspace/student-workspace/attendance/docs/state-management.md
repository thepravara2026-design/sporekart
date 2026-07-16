# Attendance Platform — State Management

## Context Structure

`AttendanceContext` provides:

| State | Type | Description |
|-------|------|-------------|
| `records` | `AttendanceRecord[]` | All attendance records (~500) |
| `sessions` | `AttendanceSession[]` | Grouped sessions |
| `summary` | `AttendanceSummary` | Computed analytics |
| `policies` | `AttendancePolicy[]` | 4 default policies |
| `dashboardStats` | `AttendanceDashboardStats` | Today + overall stats |
| `searchTerm` | `string` | Search filter |
| `statusFilter` | `AttendanceStatus \| 'all'` | Status filter |

## Data Flow

1. `AttendanceProvider` initializes with all mock data synchronously
2. `getFilteredRecords()` returns filtered view based on searchTerm + statusFilter
3. Pages derive their views via `useMemo` from context state
4. Summary and dashboard stats are pre-computed in mock data layer
5. Calendar data is fetched on-demand from `getCalendarData(year, month)`

## No Duplicated State

All data lives in a single context. Pages read from the same `records` array. Filtering is centralized in `getFilteredRecords`. No component holds local data copies.
