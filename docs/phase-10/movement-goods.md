# Goods Receipt & Goods Issue

## Overview
Goods Receipt and Goods Issue are two core inbound/outbound movement operations with dedicated workspace pages.

## Goods Receipt

### GoodsReceiptRecord Structure
| Field | Type | Description |
|-------|------|-------------|
| `id` | string | Receipt ID (GRN-001, etc.) |
| `receiptNumber` | string | Reference number |
| `supplier` | string | Optional supplier name |
| `warehouse` | string | Receiving warehouse |
| `receivingZone` | string | Receiving zone |
| `inventoryItemId` | string | Linked inventory item |
| `product` | string | Product name |
| `acceptedQty` | number | Quantity accepted |
| `rejectedQty` | number | Quantity rejected |
| `pendingQty` | number | Quantity pending inspection |
| `status` | TransactionStatus | Current status |
| `createdAt` | string | ISO timestamp |
| `updatedAt` | string | ISO timestamp |

### GoodsReceiptPage
- Card-based layout showing each receipt
- Status badge with completed/success, other/warning variants
- Product, warehouse, accepted qty, rejected qty (if >0), supplier info

## Goods Issue

### GoodsIssueRecord Structure
| Field | Type | Description |
|-------|------|-------------|
| `id` | string | Issue ID (GIS-001, etc.) |
| `issueNumber` | string | Reference number |
| `warehouse` | string | Source warehouse |
| `destination` | string | Optional destination |
| `inventoryItemId` | string | Linked inventory item |
| `product` | string | Product name |
| `quantity` | number | Issue quantity |
| `reason` | string | Issue reason |
| `status` | TransactionStatus | Current status |
| `requestedBy` | string | Requester |
| `createdAt` | string | ISO timestamp |
| `updatedAt` | string | ISO timestamp |

### GoodsIssuePage
- Card-based layout showing each issue
- Status badge with completed/success, other/warning variants
- Product, warehouse, quantity, reason, destination (if present)
