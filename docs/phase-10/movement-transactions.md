# Movement Transactions

## Overview
The Transactions page (`/admin/movements` → "Transactions" section) provides the central registry for all inventory movement transactions. It supports sorting, pagination, multi-select, and bulk actions.

## TransactionRecord Structure
| Field | Type | Description |
|-------|------|-------------|
| `id` | string | Unique transaction ID (TXN-001, etc.) |
| `referenceNumber` | string | Business reference number (REF-0001, etc.) |
| `movementType` | MovementType | One of 17 movement types |
| `inventoryItemId` | string | Linked inventory item |
| `product` | string | Product name |
| `variant` | string | Product variant |
| `sku` | string | Stock-keeping unit |
| `batchCode` | string | Optional batch association |
| `warehouseId` | string | Warehouse identifier |
| `warehouse` | string | Warehouse name |
| `sourceLocation` | string | Source location/warehouse |
| `destinationLocation` | string | Destination location/warehouse |
| `status` | TransactionStatus | Current lifecycle status (10 states) |
| `quantity` | number | Movement quantity |
| `reasonCode` | string | Optional reason code |
| `notes` | string | Optional notes |
| `requestedBy` | string | User who created the transaction |
| `approvedBy` | string | User who approved (if applicable) |
| `createdAt` | string | ISO timestamp |
| `updatedAt` | string | ISO timestamp |

## TransactionTable Component
- **Sorting**: Click any column header to sort asc/desc (reference, type, product, warehouse, status, quantity, created date)
- **Pagination**: 10 rows per page, prev/next buttons, direct page number selection
- **Multi-Select**: Checkbox per row, select-all checkbox in header
- **Bulk Actions**: Update Status, Archive, Validate, Export (visible when items are selected)
- **Empty State**: "No transactions found." with centered message
- **Loading State**: SkeletonTable with 10 rows

## Movements Page (Grouped View)
Transactions are grouped by movement type. Each group shows a compact table with reference, product, warehouse, status, quantity, and date. Groups expand to "View all N records →" if they exceed 10 entries.

## Filtering
The `MovementFilterState` supports filtering by:
- Movement type (multi-select)
- Warehouse (multi-select)
- Status (multi-select)
- Product (multi-select)
- Date range (start/end)

The `useMovementFilters` hook provides `setFilter`, `resetFilters`, and `activeFilterCount`.
