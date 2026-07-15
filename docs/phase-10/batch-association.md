# Batch Association

## Overview
Batch association links received goods to existing or new batches, establishing the connection between inbound receipts and the batch/lot tracking system (Sprint 25 Part 5).

## Assignment Flow
```
Receipt Arrives
  ↓
Check existing batches for matching product
  ↓
Assign to existing batch → OR → Create new batch
  ↓
Record lot code, expiry date, shelf life
  ↓
Update quality status
  ↓
Receipt progresses to completion
```

## BatchAssignmentRecord Fields
| Field | Description |
|-------|-------------|
| `receiptId` | Links back to the goods receipt |
| `batchCode` | Existing or new batch identifier |
| `lotCode` | Lot sub-division |
| `expiryDate` | Product expiry date |
| `shelfLife` | Duration (e.g. "12 months") |
| `qualityStatus` | From batch quality framework |
| `assignedAt` | Assignment timestamp |

## Key Design Decisions
- **Flexible Assignment**: Receipts can be assigned to existing batches or trigger new batch creation
- **Quality Status Inheritance**: Batch quality status is inherited from the inspection results
- **Lot-Level Tracking**: Each batch assignment creates a lot record for finer granularity
- **Expiry Propagation**: Batch expiry dates are propagated from receipt data

## Batch Assignment Page
- Card-based layout showing 20 mock assignments
- Each card: batch code (bold), lot code, expiry date, shelf life, quality status badge
- Receipt ID and assignment date in footer

## Future Integration
- Auto-batch creation from receipt metadata
- Expiry date validation against product shelf life
- Lot genealogy and traceability
- Quality hold propagation to linked batches
- Integration with manufacturing batch creation
