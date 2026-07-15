# Sprint 23 Part 6 — Enterprise Security, Permissions & Operational State Framework

**Phase:** 8
**Sprint:** 23
**Part:** 6
**Status:** ✅ Implemented

---

## What Was Built

### Permission-Aware UI
- `PermissionProvider` — Context provider wrapping role-based permission evaluation
- `usePermissions()` — Hook returning `can()`, `canAny()`, `canAll()`, `role`, `setRole`
- `PermissionGate` — Conditional render based on single action/resource
- `PermissionGateAll` — Conditional render requiring ALL specified actions
- `PermissionGateAny` — Conditional render requiring ANY of the specified actions
- `DEFAULT_PERMISSION_CONFIG` — Mock role definitions for `super_admin`, `administrator`, `manager`, `inventory_manager`, `viewer`
- Supports: view, create, update, delete, export, import, approve, publish, archive, bulk_actions

### Feature Flags
- `FeatureFlagProvider` — Context provider with config-driven flag state
- `useFeatureFlag()` — Hook returning `isEnabled()`, `getState()`, `getFlag()`, `isVisible()`, `setFlag()`
- `FeatureGate` — Conditional render by flag key with optional `requiredState` filter
- `DEFAULT_FEATURE_FLAGS` — 10 flags covering enabled, disabled, experimental, beta, coming_soon, hidden states
- Future-ready for remote configuration via `setFlag()` API

### Operational States
- `OperationalStateDisplay` — Unified component for all 12 operational states with loading spinner, icon, title, description, and action buttons
- `LoadingSkeleton` — 4 variants: text, card, table, sidebar with pulse animation
- `DEFAULT_STATE_CONFIGS` — Predefined copy for every state type
- Types: loading, empty, no_data, permission_denied, unauthorized, forbidden, offline, maintenance, system_updating, feature_disabled, server_unavailable, unexpected_error

### Error Boundaries
- `GlobalErrorBoundary` — Wraps entire application
- `ModuleErrorBoundary` — Wraps module-level sections, receives `moduleName`
- `PageErrorBoundary` — Wraps individual pages, receives `pageName`
- `ComponentErrorBoundary` — Wraps isolated widgets, receives `componentName` + optional `fallback`
- `BaseErrorBoundary` — Shared class component with retry/reload actions, configurable fallback, and `onError` callback

### System Status
- `useSystemStatus()` — Mock data for 8 services (database, API, payments, shipping, storage, notifications, email, background jobs)
- `SystemStatusPanel` — Full panel with service list, status indicators, overall status, last updated
- `SystemStatusIndicator` — Inline dot + label component (3 sizes)
- Status levels: operational, degraded, partial_outage, major_outage, maintenance

### Session Awareness
- `useSession()` — Tracks activity, fires timeout warning and expiry, saves/restores current page via sessionStorage
- `SessionTimeoutWarning` — Accessible dialog with countdown, "Stay signed in"/"Sign out" buttons
- `SessionExpired` — Full-screen state with re-authentication trigger and saved page indicator

### Offline Experience
- `useOnlineStatus()` — Tracks browser online/offline events
- `OfflineBanner` — Sticky warning bar with retry button
- `ReconnectNotice` — Success toast when connection restores
- Cached data placeholder and offline actions demo

### Audit-Aware Components
- `AuditInfo` — Full and compact metadata display (created by, updated by, version)
- `AuditTimeline` — Vertical timeline of audit entries with type-ahead truncation
- `VersionHistory` — Version list with restore button, current version indicator
- Types: `AuditEntry`, `AuditMeta`

### Preview Pages
| Route | Component | Description |
|---|---|---|
| `/preview/admin/security` | `SecurityPreview` | Master demo: role switcher, permission matrix, feature gates, all operational states, error boundaries, system status, session, offline, audit components |
| `/preview/admin/permissions` | `PermissionsPreview` | Interactive permission matrix (8 resources × 10 actions) with role switcher, PermissionGate demos |
| `/preview/admin/error-states` | `ErrorStatesPreview` | All 4 error boundary levels, 4 loading skeleton variants, all 12 operational states, states with action buttons |
| `/preview/admin/maintenance` | `MaintenancePreview` | Maintenance/updating/server-unavailable states, system status panel (full + compact), maintenance mode banner |
| `/preview/admin/offline` | `OfflinePreview` | Offline simulation, reconnect notice, session timeout/expired dialogs, cached data placeholder, offline actions |

### Documentation
- `docs/phase-8/sprint-23-part-6.md` — This file
- `docs/phase-8/permission-framework.md`
- `docs/phase-8/feature-flags.md`
- `docs/phase-8/operational-states.md`
- `docs/phase-8/error-boundaries.md`
- `docs/phase-8/maintenance-mode.md`
- `docs/phase-8/offline-support.md`
- `docs/phase-8/audit-aware-ui.md`

## Files Created
```
src/admin/
├── permissions/
│   ├── types.ts
│   ├── PermissionProvider.tsx
│   ├── PermissionGate.tsx
│   ├── usePermissions.ts
│   ├── permissionConfig.ts
│   └── index.ts
├── feature-flags/
│   ├── types.ts
│   ├── FeatureFlagProvider.tsx
│   ├── FeatureGate.tsx
│   ├── useFeatureFlag.ts
│   ├── config.ts
│   └── index.ts
├── operational-states/
│   ├── types.ts
│   ├── OperationalStateDisplay.tsx
│   ├── LoadingSkeleton.tsx
│   ├── defaultConfig.ts
│   └── index.ts
├── error-boundaries/
│   ├── BaseErrorBoundary.tsx
│   ├── GlobalErrorBoundary.tsx
│   ├── ModuleErrorBoundary.tsx
│   ├── PageErrorBoundary.tsx
│   ├── ComponentErrorBoundary.tsx
│   └── index.ts
├── system-status/
│   ├── types.ts
│   ├── useSystemStatus.ts
│   ├── SystemStatusPanel.tsx
│   ├── SystemStatusIndicator.tsx
│   └── index.ts
├── session/
│   ├── types.ts
│   ├── useSession.ts
│   ├── SessionTimeoutWarning.tsx
│   ├── SessionExpired.tsx
│   └── index.ts
├── offline/
│   ├── types.ts
│   ├── useOnlineStatus.ts
│   ├── OfflineBanner.tsx
│   ├── ReconnectNotice.tsx
│   └── index.ts
├── audit/
│   ├── types.ts
│   ├── AuditInfo.tsx
│   ├── AuditTimeline.tsx
│   ├── VersionHistory.tsx
│   └── index.ts
└── preview/part6/
    ├── SecurityPreview.tsx
    ├── PermissionsPreview.tsx
    ├── ErrorStatesPreview.tsx
    ├── MaintenancePreview.tsx
    └── OfflinePreview.tsx
```

## Route Integration
All Part 6 preview routes registered in `src/App.tsx` under `/preview/admin/*`.

## TypeScript
0 errors across all files.
