# Enterprise Student Workspace Architecture

## Overview

The Student Workspace is the central Student Information System (SIS) foundation
for the SporeKart Enterprise Learning Management Platform. It establishes the
master student registry that serves as the authoritative source of truth for
every learner across the ecosystem.

## Architecture Principles

- **Domain-Driven Design**: Student domain is isolated from other domains
- **Feature-First**: Features are self-contained with types, state, mock data
- **Dependency Inversion**: Student workspace depends on abstractions (design system)
- **Composition over Inheritance**: UI is composed from design system components
- **Co-location**: Types, state, mock data live with the feature

## Folder Structure

```
student-workspace/
├── StudentWorkspaceRoute.tsx      # Route wrapper with provider
├── StudentWorkspaceLayout.tsx      # Layout with nav rail + mobile support
├── types.ts                        # Student domain model
├── data/
│   ├── navigation.ts               # Nav rail configuration
│   └── mockData.ts                 # Mock student data (50 records)
├── state/
│   ├── WorkspaceContext.tsx         # React context provider
│   └── workspaceState.ts           # State management hook
├── pages/
│   ├── StudentDashboardPage.tsx     # Overview dashboard with widgets
│   ├── StudentRegistryPage.tsx      # Registry with table/card/compact views
│   ├── StudentDirectoryPage.tsx     # Directory with grouping + views
│   └── StudentArchivedPage.tsx      # Archived students view
├── components/
│   ├── StudentStatusBadge.tsx       # Reusable status badge
│   ├── StudentCard.tsx              # Card + compact card components
│   ├── StudentTable.tsx             # Table view with sorting
│   ├── StudentSearchFilter.tsx      # Search bar + filter panel
│   ├── StudentPagination.tsx        # Pagination controls
│   ├── StudentQuickActions.tsx      # Quick action toolbar
│   ├── StudentEmptyStates.tsx        # Empty state variants
│   └── StudentSkeletons.tsx         # Skeleton loaders
└── docs/
    └── architecture.md              # This file
```

## Component Hierarchy

```
StudentWorkspaceRoute
 └── StudentWorkspaceProvider
      └── StudentWorkspaceLayout
           ├── Nav Rail (navigation groups)
           └── Outlet
                ├── StudentDashboardPage
                │    ├── StatWidget (x6)
                │    ├── StudentQuickActions
                │    ├── Students by Course (Card)
                │    ├── Students by Language (Card)
                │    ├── Student Categories (Card)
                │    └── Recently Registered / Learning Mode
                │
                ├── StudentRegistryPage
                │    ├── StudentQuickActions
                │    ├── StudentSearchBar
                │    ├── View Mode Toggle
                │    ├── StudentFilterPanel
                │    ├── StudentTable | StudentCard | Compact List
                │    └── StudentPagination
                │
                ├── StudentDirectoryPage
                │    ├── StudentSearchBar
                │    ├── Group By / View Mode Toggle
                │    ├── Grouped / Flat Student Cards
                │    └── StudentPagination
                │
                └── StudentArchivedPage
                     ├── StudentSearchBar
                     ├── StudentFilterPanel
                     ├── StudentTable
                     └── StudentPagination
```

## State Architecture

All state is managed through React Context + custom hooks:

```
useStudentWorkspaceState() → StudentWorkspaceContextValue
 ├── students: Student[]           # All mock students
 ├── stats: DashboardStats         # Aggregated stats
 ├── searchQuery: string           # Search input
 ├── filters: StudentFilters       # Multi-faceted filters
 ├── sort: SortConfig              # Sort field + direction
 ├── viewMode: ViewMode            # table/card/compact
 ├── page / pageSize               # Pagination
 ├── selectedIds: Set<string>      # Row selection
 ├── paginatedStudents: Student[]  # Filtered + sorted + paginated
 └── totalFiltered: number         # Count after filtering
```

## Mock Data Standards

- 50 diverse student records
- Realistic Indian names, states, districts, villages
- All 11 lifecycle statuses represented
- Multiple languages, courses, categories, learning modes
- Tags for additional metadata
- Deterministic generation via seeded random functions

## Future Extension Interfaces

### Attendance Integration
```typescript
interface AttendanceRecord {
  studentId: string;
  batchId: string;
  date: string;
  status: 'present' | 'absent' | 'late' | 'excused';
}
```

### Assignment Integration
```typescript
interface AssignmentSubmission {
  studentId: string;
  assignmentId: string;
  submittedAt: string;
  grade?: number;
  status: 'pending' | 'submitted' | 'graded' | 'overdue';
}
```

### Certificate Integration
```typescript
interface StudentCertificate {
  studentId: string;
  certificateId: string;
  courseId: string;
  issuedAt: string;
  status: 'active' | 'revoked' | 'expired';
}
```

### Analytics Integration
```typescript
interface StudentAnalytics {
  studentId: string;
  courseId: string;
  completionRate: number;
  averageScore: number;
  timeSpent: number;
  lastActivity: string;
}
```

## Routes

| Route | Page | Description |
|-------|------|-------------|
| `/admin/training/student-workspace/overview` | StudentDashboardPage | Dashboard with stats |
| `/admin/training/student-workspace/registry` | StudentRegistryPage | Master registry |
| `/admin/training/student-workspace/directory` | StudentDirectoryPage | Browse directory |
| `/admin/training/student-workspace/archived` | StudentArchivedPage | Archived students |

## Design System Reuse

| Component | Source | Usage |
|-----------|--------|-------|
| Card | design-system/composite/Card | Widgets, student cards |
| Avatar | design-system/display/Avatar | Student avatars |
| Badge | design-system/display/Badge | Status badges |
| Chip | design-system/display/Chip | Filter chips, tags |
| Skeleton | design-system/display/Skeleton | Loading states |
| EmptyState | design-system/display/EmptyState | Empty states |
| Icon | design-system/icons/Icon | All icons |
| Button | design-system/core/Button | Quick actions |
