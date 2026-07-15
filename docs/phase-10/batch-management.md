# Batch Management Architecture

## Domain Model

```
Batch Record
├── id, batchCode (unique identifiers)
├── inventoryItemId → Inventory Item (Part 3)
├── product, variant, sku (denormalized for performance)
├── warehouseId, warehouse (location)
├── productionDate, expiryDate, bestBeforeDate, manufacturingDate, receivedDate
├── shelfLife (number) + shelfLifeUnit (days/weeks/months/years/custom)
├── status (12 lifecycle states)
├── qualityStatus (8 quality states)
├── expiryStatus (9 expiry states)
├── storageConditions { temperature, humidity }
├── quantity (total units)
├── lotCount (number of sub-lots)
├── createdBy, createdAt, updatedAt (audit fields)
└── lots[] (associated Lot records)
```

## Key Relationships
- **Product → Inventory Item → Batch → Lot**: Strict hierarchy preserved
- **Batch → Warehouse**: Many-to-one (a batch is stored at one warehouse)
- **Batch → Timeline**: One-to-many (auditable event log)

## Architecture Principles
1. **Denormalization**: Product, variant, SKU, and warehouse data are stored on the Batch record for query performance, avoiding joins
2. **Separation of Concerns**: Batch status (lifecycle), quality status, and expiry status are tracked independently
3. **Extensibility**: All status enums are designed with future states in mind (e.g., recalled, disposed for lifecycle)
4. **Mock Mode**: All data is generated client-side — no backend dependency

## Future Integration Points
- Stock Engine (Part 4): Batches link to Stock Records via inventoryItemId
- Manufacturing: Batches track production output
- Procurement: Batches link to Purchase Orders
- Transfers: Batches track inter-warehouse movement
- Returns: Batches track returned goods
- Recall: Batches support recall workflows
- Disposal: Batches support disposal workflows
