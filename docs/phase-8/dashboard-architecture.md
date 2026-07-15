# Enterprise Dashboard Architecture

## Overview

The Enterprise Admin Dashboard is a composable framework of reusable components that form the foundation for all future operational dashboard pages.

## Architecture Diagram

```
DashboardLayout
├── Welcome Area (time-based greeting + user role)
├── AnnouncementBanner (priority-based cards, dismissible)
├── KPIGrid
│   └── KPICard[] (icon, value, trend, comparison)
├── QuickActions (configurable action grid)
├── WidgetGrid (4-column CSS Grid)
│   └── WidgetCard[]
│       └── WidgetContent (per-type implementation)
├── ActivityFeed (timeline with expandable details)
└── SystemStatus (service health grid)
```

## Data Flow

```
MockData (typed interfaces)
  └── DashboardData
        ├── kpis: KPIData[]
        ├── widgets: WidgetConfig[]
        ├── activity: ActivityItemData[]
        ├── announcements: AnnouncementData[]
        ├── systemStatus: SystemStatusData[]
        └── quickActions: QuickActionData[]
              │
              ▼
        DashboardLayout (presentation only, no data fetching)
              │
              ▼
        Individual child components (receive slices of data)
```

## Design Principles

1. **Presentation-only**: No data fetching, no business logic. Components receive data via props.
2. **Composable**: Every section is independently usable outside DashboardLayout.
3. **Mock-data ready**: Typed interfaces enable seamless future API integration.
4. **Responsive-first**: CSS Grid layout adapts to viewport via breakpoints.
5. **Loading/empty states**: Every section handles loading, empty, and populated states.

## File Structure

| Path | Purpose |
|------|---------|
| `types.ts` | Shared TypeScript interfaces |
| `DashboardLayout.tsx` | Main layout composing all sections |
| `kpi/` | KPI card and grid |
| `widgets/` | Widget framework and implementations |
| `activity/` | Activity timeline |
| `actions/` | Quick action grid |
| `announcements/` | Announcement cards |
| `status/` | System service status |
| `mock/` | Structured mock data |

## Future Integration

```tsx
// Step 1: Replace mock data with API calls
const { data, loading, error } = useDashboardAPI();

// Step 2: Pass to DashboardLayout
<DashboardLayout data={data} loading={loading} />
```

No DashboardLayout component changes needed — data contract is typed via `DashboardData` interface.
