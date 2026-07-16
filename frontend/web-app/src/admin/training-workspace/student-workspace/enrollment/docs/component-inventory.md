# Enrollment Platform — Component Inventory

## Page Components

| Component | Route/Sub-route | Description | State Dependencies |
|-----------|----------------|-------------|-------------------|
| `EnrollmentIndex` | `/enrollment` | Wrapper with sub-navigation tabs, lazy-loaded | `activeSection` |
| `EnrollmentDashboardPage` | `enrollment` (default) | Executive dashboard with 8 stat widgets, pipeline, recent items, active batches | `dashboardStats`, `requests`, `batches` |
| `EnrollmentRequestsPage` | `enrollment/requests` | Full request list, table/card toggle, search, status filter, pagination | `requests`, `searchTerm`, `statusFilter` |
| `EnrollmentApprovalQueuePage` | `enrollment/approval` | Pending approvals list + detail panel with workflow actions + timeline | `pendingApprovals`, `currentRequest`, `batches` |
| `EnrollmentBatchManagementPage` | `enrollment/batches` | Batch list grouped by slot (morning/evening/etc.), capacity bars per batch | `batches` |
| `EnrollmentCapacityDashboardPage` | `enrollment/capacity` | Batch utilization summary + SVG capacity gauges per batch | `capacityInfo`, `batches` |
| `EnrollmentTimelinePage` | `enrollment/timeline` | Student selector + enrollment timeline viewer | `requests` |
| `EnrollmentArchivedPage` | `enrollment/archived` | Archived records with search and pagination | `archivedEnrollments` |

## Domain-Level Components

| Component | Reusable | Description |
|-----------|----------|-------------|
| `EnrollmentStatusBadge` | Yes | Color-coded badge for any `EnrollmentStatus` value |
| `EnrollmentTable` | Yes | 7-column sortable table with keyboard-navigable rows |
| `EnrollmentCard` | Yes | Card view for enrollment requests with status, type, mode, priority |
| `EnrollmentTimeline` | Yes | Vertical timeline of `EnrollmentTimelineEvent` items |
| `BatchCard` | No | Batch display with capacity bar and slot/mode/date info |
| `CapacityGauge` | Yes | SVG circular gauge showing utilization percentage |
| `ApprovalActionsPanel` | No | Workflow-specific approve/reserve/assign/reject action buttons |
| `DashboardWidget` | Yes | Stat display widget with label, value, icon, variant color |
| `EmptyState` | Yes | Typed empty state with icon, title, message, optional clear button |
| `EnrollmentTableSkeleton` | Yes | Row-based skeleton for table loading |
| `EnrollmentDashboardSkeleton` | Yes | Dashboard skeleton with widget + content placeholders |
| `BatchCardSkeleton` | Yes | Batch card skeleton with title/status/capacity placeholders |

## Reused Enterprise Components (Part 1)

| Enterprise Component | Used In |
|---------------------|---------|
| `StudentSearchBar` | `EnrollmentRequestsPage`, `EnrollmentArchivedPage` |
| `StudentPagination` | `EnrollmentRequestsPage`, `EnrollmentApprovalQueuePage`, `EnrollmentArchivedPage` |

## Component Hierarchy

```
EnrollmentIndex
  ├── EnrollmentDashboardPage
  │     ├── DashboardWidget (×8)
  │     ├── AdmissionPipeline bar
  │     ├── EnrollmentTable (recent)
  │     └── Active Batch list
  ├── EnrollmentRequestsPage
  │     ├── StudentSearchBar
  │     ├── StatusFilter dropdown
  │     ├── EnrollmentTable / EnrollmentCard
  │     ├── StudentPagination
  │     └── Enrollment Detail panel
  ├── EnrollmentApprovalQueuePage
  │     ├── EnrollmentTable
  │     ├── StudentPagination
  │     ├── ApprovalActionsPanel
  │     └── EnrollmentTimeline
  ├── EnrollmentBatchManagementPage
  │     ├── Stats bar
  │     └── BatchCard (×N)
  ├── EnrollmentCapacityDashboardPage
  │     ├── DashboardWidget (×4)
  │     ├── Utilization summary
  │     └── CapacityGauge (×N)
  ├── EnrollmentTimelinePage
  │     ├── Student selector list
  │     └── EnrollmentTimeline
  └── EnrollmentArchivedPage
        ├── StudentSearchBar
        ├── EnrollmentTable
        └── StudentPagination
```
