# Goods Receipt Architecture

## Overview
The Goods Receipt domain manages inbound inventory from suppliers, manufacturing, and returns. Every receipt creates a `GoodsReceiptRecord` with full status tracking, inspection linkage, warehouse allocation, and batch assignment.

## Architecture Layers
```
┌─────────────────────────────────────────────────────────────┐
│              Receiving Workspace (UI)                        │
│  15-section sidebar · Header/search · 14 pages              │
├─────────────────────────────────────────────────────────────┤
│              Receiving Data Layer                            │
│  ReceivingWorkspaceContext · 5 hooks · Mock Service · Utils  │
├─────────────────────────────────────────────────────────────┤
│              Receiving Components                           │
│  ReceivingTable · ReceivingTimeline · ReceivingSummaryCards  │
├─────────────────────────────────────────────────────────────┤
│              Receiving Preview App                           │
│  6-tab preview · viewport/theme/role controls                │
├─────────────────────────────────────────────────────────────┤
│              Receiving Domain Model                          │
│  GoodsReceiptRecord → InspectionRecord → AllocationRecord    │
│  BatchAssignmentRecord → ReceivingTimelineEvent → AuditRecord│
└─────────────────────────────────────────────────────────────┘
```

## GoodsReceiptRecord Structure
| Field | Type | Description |
|-------|------|-------------|
| `receiptNumber` | string | Business identifier (GRN-0001) |
| `referenceNumber` | string | PO reference (PO-00001) |
| `supplier` | string | Supplier name |
| `warehouse` | string | Receiving warehouse |
| `receivingZone` | string | Dock/receiving zone |
| `inventoryItemId` | string | Linked inventory item |
| `product` | string | Product name |
| `quantity` | number | Total receipt quantity |
| `acceptedQty` | number | Quantity accepted |
| `rejectedQty` | number | Quantity rejected |
| `inspectionQty` | number | Quantity inspected |
| `receiptStatus` | ReceiptStatus | 10-stage lifecycle |
| `inspectionStatus` | InspectionStatus | 11-status checks |
| `acceptanceStatus` | AcceptanceStatus | 6 acceptance outcomes |
| `rejectionReason` | RejectionReason | 9 rejection reasons |
| `allocationStatus` | AllocationStatus | 5 allocation states |
| `batchAssigned` | boolean | Batch link flag |

## Status Lifecycle
```
Draft → Pending → Receiving → Inspection → Approved/Rejected → Completed
                                          ↘ Cancelled
```
Additional intermediate states: `warehouse_allocation_pending`, `batch_pending`

## Data Flow
1. User creates receipt in workspace (mock generates 45 records)
2. Receipt appears in Pending Receipts / Receiving Queue
3. Operator starts receiving → status moves to `receiving`
4. Inspector conducts quality checks → status moves to `inspection`
5. Acceptance or Rejection decision made
6. Batch assigned to receipt
7. Warehouse location allocated
8. Receipt completed → goods ready for inventory activation
