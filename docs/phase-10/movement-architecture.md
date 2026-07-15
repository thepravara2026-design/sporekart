# Movement Domain Architecture

## Overview
The Movement Domain provides the transaction layer on top of Inventory Items (Part 3), Stock Records (Part 4), and Batches (Part 5). Every inventory change — whether receipt, issue, transfer, adjustment, or quality event — is recorded as a Movement with an immutable Transaction record.

## Architecture Layers

```
┌─────────────────────────────────────────────────────────────────┐
│                    Movement Workspace (UI)                       │
│  14-section sidebar · Header/search · Dashboard · 13 pages      │
├─────────────────────────────────────────────────────────────────┤
│                    Movement Data Layer                           │
│  MovementWorkspaceContext · 5 hooks · Mock Service · Utils      │
├─────────────────────────────────────────────────────────────────┤
│                    Movement Components                          │
│  TransactionTable · MovementTimeline · MovementSummaryCards      │
├─────────────────────────────────────────────────────────────────┤
│                    Movement Preview App                          │
│  6-tab preview · viewport/theme/role controls                   │
├─────────────────────────────────────────────────────────────────┤
│                    Movement Domain Model                         │
│  TransactionRecord → TransferRecord → AdjustmentRecord          │
│  GoodsReceiptRecord → GoodsIssueRecord                          │
│  MovementTimelineEvent → AuditRecord → AnalyticsData            │
└─────────────────────────────────────────────────────────────────┘
```

## Data Flow
1. User interacts with workspace page
2. `useMovementData` hook returns mock data from `movementMockService`
3. Page components render data via `TransactionTable`, `MovementTimeline`, etc.
4. `useMovementFilters` and `useMovementSearch` provide client-side filtering
5. Activity is recorded in `MovementTimelineEvent` and `AuditRecord` arrays

## 17 Movement Types
| Category | Types |
|----------|-------|
| Receipt | `goods_receipt`, `return`, `production_receipt` |
| Issue | `goods_issue`, `production_issue`, `consumption` |
| Transfer | `warehouse_transfer` |
| Adjustment | `stock_adjustment`, `cycle_count_adjustment`, `manual_adjustment` |
| Quality | `quality_hold`, `inspection` |
| Allocation | `reservation`, `release`, `marketplace_allocation` |
| Damage | `damage` |
| Shipping | `shipment` |
| Manufacturing | `manufacturing` |

## 10 Transaction Statuses
draft → pending → approved → processing → completed | rejected | cancelled | archived | failed_validation | blocked

## Key Design Decisions
- **Immutable Transactions**: Once created, a transaction's core fields are never modified. Corrections create new adjustment transactions.
- **Separate Audit Trail**: Field-level changes are recorded in `AuditRecord` entries, keeping the `TransactionRecord` clean.
- **Timeline as First-Class Entity**: Every status transition generates a `MovementTimelineEvent`, providing full traceability.
- **Mock Service Isolation**: All mock data lives in `movementMockService.ts`, making it easy to swap in real API calls later.
