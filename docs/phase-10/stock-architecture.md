# Stock Management Engine — Domain Architecture

## Domain Model

```
Inventory Item (Sprint 25 Part 3)
      │
      ▼
  Warehouse Location (Sprint 25 Part 2)
      │
      ▼
   Stock Record (admin/modules/stock/)
      │
      ├── Stock Quantities (15 states)
      ├── Stock Health (5 levels)
      ├── Availability Level (6 levels)
      ├── Reservations (holds for future orders)
      └── Timeline (audit trail of transitions)
```

## Stock Record — Core Entity

A Stock Record represents the quantity of an Inventory Item at a specific Warehouse Location. Each record maintains quantities across 15 discrete states and derives health/availability from them.

### Fields
| Field | Type | Description |
|-------|------|-------------|
| `id` | string | Unique identifier |
| `sku` | string | SKU from Inventory Item (Part 3) |
| `inventoryItemId` | string | FK to Inventory Item |
| `warehouseId` | string | FK to Warehouse Location |
| `zoneId` | string | Zone within warehouse |
| `quantities` | object | 15 numeric state counters |
| `health` | StockHealth | Derived health level |
| `availability` | AvailabilityLevel | Derived availability |
| `batch` | string | Optional batch/lot number |
| `expiryDate` | string | Optional expiry |
| `reservations` | Reservation[] | Active soft-holds |
| `timeline` | StockTimelineEvent[] | Auditable transitions |
| `createdAt` | string | Creation timestamp |
| `updatedAt` | string | Last update timestamp |
| `updatedBy` | string | User identifier |

## Separation of Concerns

- **Inventory Item** (Part 3) defines WHAT exists (product mapping, SKU, classification).
- **Stock Record** (Part 4) defines HOW MUCH exists and in what STATE.
- **Warehouse Location** (Part 2) defines WHERE it is stored.
- **Transactions/Orders** (future) define HOW it moves.

This separation ensures each module has a single, well-defined responsibility.
