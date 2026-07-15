# Audit Trail, Timeline & Analytics

## Overview
Three interconnected features provide full visibility into inventory movement history: the Movement Timeline, Audit Trail, and Analytics Dashboard.

## Movement Timeline

### MovementTimelineEvent Structure
| Field | Type | Description |
|-------|------|-------------|
| `id` | string | Event ID |
| `transactionId` | string | Related transaction |
| `event` | string | Event name (e.g. "Transaction Created", "Submitted for Approval", "Approved", "Completed", "Rejected") |
| `fromStatus` | TransactionStatus | Previous status (optional) |
| `toStatus` | TransactionStatus | New status |
| `user` | string | Acting user |
| `timestamp` | string | ISO timestamp |
| `note` | string | Optional note |

### MovementTimelineComponent
- Vertical timeline with dot + connecting line layout
- Filterable by event type via button chips
- Shows status transitions: `fromStatus → toStatus`
- Displays user attribution and optional notes
- Configurable max height with scroll
- Empty state: "No timeline events available."

### HistoryPage
- Wraps `MovementTimelineComponent` with full event list
- Count badge: "Movement History (N events)"

## Audit Trail

### AuditRecord Structure
| Field | Type | Description |
|-------|------|-------------|
| `id` | string | Audit record ID |
| `transactionId` | string | Related transaction |
| `action` | string | Action performed (Created, Modified, etc.) |
| `field` | string | Field that changed (status, quantity, etc.) |
| `oldValue` | string | Previous value |
| `newValue` | string | New value |
| `user` | string | Acting user |
| `timestamp` | string | ISO timestamp |

### AuditPage
- Full table with columns: Timestamp, User, Action, Field, Old Value, New Value
- StatusBadge for action column (info variant)
- Old/new values truncated with text-overflow ellipsis
- Empty state: "No audit records found."

## Analytics

### AnalyticsData Structure
| Field | Type | Description |
|-------|------|-------------|
| `totalTransactions` | number | Total movement transactions |
| `transfers` | number | Transfer count |
| `receipts` | number | Receipt count |
| `issues` | number | Issue count |
| `adjustments` | number | Adjustment count |
| `returns` | number | Return count |
| `damages` | number | Damage count |
| `pending` | number | Pending transactions |
| `completed` | number | Completed transactions |
| `warehouseDistribution` | array | Per-warehouse transaction counts |

### AnalyticsPage
- 7 KPI cards in responsive grid layout
- Categories: Total Transactions, Transfers, Receipts, Issues, Adjustments, Pending, Completed
- Each card shows: large value number, label, description
- Data sourced from `AnalyticsData` in mock service
