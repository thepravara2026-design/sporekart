# Lot Management Framework

## Overview
Lots are sub-divisions of a Batch. Each batch can have multiple lots representing production runs, received quantities, or sub-batches. Lots serve as the granular unit for future stock transactions and traceability.

## Lot Record Structure
| Field | Type | Description |
|-------|------|-------------|
| `id` | string | Unique lot identifier (LOT-{batchId}-{sequence}) |
| `lotCode` | string | Human-readable lot code |
| `batchId` | string | Parent batch FK |
| `batchCode` | string | Parent batch code (denormalized) |
| `inventoryItemId` | string | FK to Inventory Item |
| `product` | string | Product name (denormalized) |
| `warehouseId`, `warehouse` | string | Location (denormalized) |
| `quantity` | number | Lot quantity |
| `status` | string | Inherits from parent batch |
| `createdAt`, `updatedAt` | string | Audit timestamps |

## Key Design Decisions
1. **Denormalized fields**: Product, warehouse, and batch code are stored on each lot for query performance
2. **Status inheritance**: Lots inherit their status from the parent batch by default, with future support for independent lot-level status
3. **Future stock linkage**: Each lot maps to future Stock Records (Part 4) — a single lot can be split across multiple stock records

## Lot Registry Features
- Searchable by lot code, batch code, product, warehouse
- Paginated display (15 per page)
- Quantity display with locale formatting
- Sortable columns
