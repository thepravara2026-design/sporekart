# Transfers & Adjustments

## Overview
Two specialized movement types — warehouse transfers and stock adjustments — have dedicated pages with custom visualizations.

## Warehouse Transfers

### TransferRecord Structure
| Field | Type | Description |
|-------|------|-------------|
| `id` | string | Transfer ID (TRF-001, etc.) |
| `transferNumber` | string | Reference number (links to transaction) |
| `sourceWarehouse` | string | Origin warehouse |
| `destinationWarehouse` | string | Destination warehouse |
| `sourceZone` | string | Origin zone |
| `destinationZone` | string | Destination zone |
| `sourceBin` | string | Origin bin location |
| `destinationBin` | string | Destination bin location |
| `inventoryItemId` | string | Linked inventory item |
| `product` | string | Product name |
| `quantity` | number | Transfer quantity |
| `reason` | string | Transfer reason |
| `status` | TransactionStatus | Current status |
| `createdAt` | string | ISO timestamp |
| `updatedAt` | string | ISO timestamp |

### TransfersPage
- Card-based layout showing each transfer with source → destination visualization
- Status badge (completed/success, pending/warning)
- Product name + quantity display
- Date and reference number

## Stock Adjustments

### AdjustmentRecord Structure
| Field | Type | Description |
|-------|------|-------------|
| `id` | string | Adjustment ID (ADJ-001, etc.) |
| `adjustmentNumber` | string | Reference number |
| `type` | 'positive' \| 'negative' \| 'damage' \| 'expiry' \| 'audit' \| 'cycle_count' \| 'manual' | Adjustment type |
| `inventoryItemId` | string | Linked inventory item |
| `product` | string | Product name |
| `warehouse` | string | Warehouse name |
| `quantity` | number | Adjustment quantity |
| `reasonCode` | string | Reason for adjustment |
| `notes` | string | Optional notes |
| `status` | TransactionStatus | Current status |
| `createdAt` | string | ISO timestamp |
| `updatedAt` | string | ISO timestamp |

### AdjustmentsPage
- Full table with columns: Reference, Product, Warehouse, Type, Qty, Reason, Status
- Quantity displayed with color: green for positive/credit types, red for negative/damage types
- Status badge with appropriate variants

## Goods Receipt & Goods Issue
See dedicated document `movement-goods.md`.
