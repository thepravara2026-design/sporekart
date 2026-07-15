# Inventory Item Architecture

## Overview
The Inventory Item is the central domain entity in Sprint 25 Part 3. Every Product + Variant + SKU combination from Sprint 24 becomes an Inventory Item record. All future inventory transactions (stock movements, warehouse operations, procurement, sales orders, returns) will reference these records.

## Core Type
```typescript
interface InventoryItemRecord {
  id: string;              // INV-XXXX format
  code: string;            // Auto-generated
  productId: string;       // FK to Sprint 24 Product
  productName: string;
  variantId: string;       // FK to Sprint 24 Variant
  variantName: string;
  sku: string;             // FK to Sprint 24 SKU
  name: string;            // Composite: "{Product} - {Variant}"
  description?: string;
  status: InventoryStatus;
  classification: Classification;
  lifecycle: LifecycleStage;
  lifecycleEvents: LifecycleEvent[];
  unit: string;
  brand: string;
  category: string;
  createdAt: string;
  updatedAt: string;
}
```

## Data Flow
```
Sprint 24 Product ──► Product Mapping ──┐
                                         ├──► Inventory Item Record
Sprint 24 Variant ──► Variant Mapping ──┤
                                         │
Sprint 24 SKU ──────► SKU Association ──┘
```

## Module Dependencies
- Reuses `InventoryStatus` type from Sprint 25 Part 1 (`admin/modules/inventory/types.ts`)
- Reuses UI components from Part 1: `SectionHeader`, `MetricCard`, `StatisticsGrid`, `SummaryCard`, `QuickActionCard`, `RecentActivityCard`, `WorkspaceBanner`, `StatusBadge`, `Icon`
- Reuses patterns from Part 2 (Warehouse): local workspace provider, permissions hook, layout pattern

## Mock Data
25 inventory items generated from 12 products and 24 variants. Each item receives:
- A composite name
- A SKU string based on product code and variant name
- Random classification type and grade
- A lifecycle with 3–4 event records
- A unit, brand, and category

## Future
- Sprint 25 Part 4: Stock levels, quantities, warehouse assignments
- Sprint 25 Part 5: Procurement and purchase order integration
- Sprint 25 Part 6: Sales order and fulfillment linking
