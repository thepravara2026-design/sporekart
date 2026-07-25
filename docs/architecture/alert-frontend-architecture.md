# Alert Frontend Architecture

## Overview
Three React-based admin modules providing monitoring and management interfaces for the Alert Intelligence Engine.

## Module Structure
```
admin/modules/
├── alert-center/        # Alert management dashboard
├── risk-dashboard/      # Risk assessment console
└── timeline-view/       # Event timeline viewer
```

## Alert Center
- **Components:** AlertCenterDashboard (metric cards + alert table with action buttons)
- **Features:** Acknowledge/Resolve inline, severity color-coded badges
- **Mock service:** 15 seeded alerts across 5 categories

## Risk Dashboard
- **Components:** RiskDashboardView (summary cards + risk table with score bars)
- **Features:** Severity badges, likelihood-impact visualization, score bars
- **Mock service:** 10 risks across 10 categories with risk summary

## Timeline View
- **Components:** TimelineViewPage (vertical timeline with event cards)
- **Features:** Color-coded by severity, type badges, source labels
- **Mock service:** 9 timeline events across all types

## Registration
- **Navigation:** Sidebar items in adminNavigation.tsx
- **Routes:** Lazy-loaded in App.tsx (`/admin/alert-center`, `/admin/risk-dashboard`, `/admin/timeline-view`)
- **Roles:** Accessible to administrator, business_owner (defined in roleNavigation.ts)
- **KPIs:** Dashboard widgets registered in dashboardWidgets.ts (3 KPI groups)

## State & Service Pattern
- Each module follows: `types.ts` → `constants.ts` → `services/*MockService.ts` → `*Page.tsx` → `index.ts`
- All services are fully mocked; no production API integration
- Uses custom httpClient.ts infrastructure for real API calls when uncommented
