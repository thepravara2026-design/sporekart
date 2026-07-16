# Enrollment Platform — Architecture

## Overview

The Enterprise Enrollment Platform (Sprint 27 Part 3) extends the Student Workspace & Registry with a complete Admission, Enrollment Lifecycle, and Batch Allocation system. It operates entirely in Mock Mode with no backend, APIs, or database dependencies.

## Directory Structure

```
enrollment/
  types.ts                         — Domain model types and constants
  data/mockData.ts                 — Mock enrollment data generator (40 requests, 9 batches)
  state/EnrollmentContext.tsx       — React Context for enrollment state
  components/
    EnrollmentStatusBadge.tsx       — Color-coded status badge
    EnrollmentTable.tsx             — Table view for enrollment requests
    EnrollmentCard.tsx              — Card view for enrollment requests
    EnrollmentTimeline.tsx          — Sequential enrollment activity timeline
    EnrollmentSearchFilter.tsx      — Search + status filter bar
    EnrollmentPagination.tsx        — Page navigation controls
    BatchCard.tsx                   — Batch display with capacity bar
    CapacityGauge.tsx               — SVG circular gauge for capacity
    ApprovalActionsPanel.tsx        — Approve/Reject/RequestInfo buttons
    DashboardWidget.tsx             — Reusable stat widget
    EmptyStates.tsx                 — Typed empty states (8 types)
    Skeletons.tsx                   — Skeleton loaders (table, dashboard, batch)
  pages/
    EnrollmentIndex.tsx             — Wrapper with sub-navigation tab bar
    EnrollmentDashboardPage.tsx     — Executive dashboard with stats, pipeline, recent items
    EnrollmentRequestsPage.tsx      — Full request list with view toggle, search, filter, pagination
    EnrollmentApprovalQueuePage.tsx — Pending approvals with action panel and timeline
    EnrollmentBatchManagementPage.tsx — Batch list grouped by slot with capacity bars
    EnrollmentCapacityDashboardPage.tsx — SVG gauges per batch + utilization summary
    EnrollmentTimelinePage.tsx      — Timeline browser with student selector
    EnrollmentArchivedPage.tsx      — Archived records with search and pagination
  docs/
    architecture.md
    accessibility.md
    responsive.md
    performance.md
```

## State Management

`EnrollmentContext` provides:
- `requests` — all enrollment requests
- `batches` — all training batches
- `capacityInfo` — capacity per batch
- `dashboardStats` — computed dashboard stats
- `pendingApprovals` / `archivedEnrollments` — filtered subsets
- `currentRequest` — currently selected request
- `searchTerm` / `statusFilter` — filtering state

## Data Flow

1. `EnrollmentProvider` initializes by loading all mock data in its initial state
2. Pages consume context and derive filtered/paginated views via `useMemo`
3. Sub-navigation switches between pages client-side via `activeSection` state
4. Data is never mutated — all operations are log-only in Mock Mode

## Routing

- App.tsx: `/admin/training/student-workspace/enrollment` → `EnrollmentIndex`
- Navigation: promoted from future items to main nav group
- Sub-navigation: 7 tabs (Dashboard, Requests, Approval, Batches, Capacity, Timeline, Archived)

## Domain Model

Key types:
- `EnrollmentRequest` — full application with 12 statuses, 10 admission types, approval workflow
- `Batch` — 9 batches across morning/evening/weekend/corporate/government/institution/online/offline/hybrid slots
- `CapacityInfo` — per-batch capacity tracking with utilization percentage
- `EnrollmentTimelineEvent` — 11 event types tracking the complete lifecycle
- `AdmissionPipelineStage` — 10-stage pipeline from prospective to activated
