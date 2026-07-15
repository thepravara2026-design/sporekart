# Receiving Workflow

## Overview
The receiving workflow is an 8-step visual process that guides operators from receipt creation through to inventory activation. Each step has a status indicator (completed, active, pending).

## Workflow Steps
```
Step 1: Receipt Created       → Goods receipt initialized in the system
Step 2: Goods Arrived         → Physical goods arrive at warehouse dock
Step 3: Inspection            → Quality inspection in progress
Step 4: Acceptance/Rejection  → Accept or reject received goods
Step 5: Batch Assignment      → Link batch/lot to receipt
Step 6: Warehouse Allocation   → Assign storage location
Step 7: Inventory Activation   → Goods become operational stock
Step 8: Completed             → Receipt finalized
```

## Receiving Queue Page
Displays receipts in `pending` or `receiving` status with action buttons:
- **Start Receiving** — initiates the receiving process
- **Inspect** — routes to inspection
- **Skip** — bypasses this receipt

## Page Structure
| Section | Content | Actions |
|---------|---------|---------|
| Receiving Queue | Cards for pending/receiving | Start, Inspect, Skip |
| Goods Receipt | Full registry table | Sort, filter, paginate, select |
| Pending Receipts | Filtered view of pending/draft/batch_pending/allocation_pending | Status badges |

## Future Integration Points
- Purchase Order integration: auto-populate receipt from PO
- Supplier Portal: supplier submits ASN (Advanced Shipping Notice)
- Manufacturing Receipts: link to production orders
- Return Receipts: handle customer returns
