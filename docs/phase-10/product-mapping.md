# Product Mapping — Product to Inventory Item

## Purpose
Link every Product from Sprint 24's Product Catalog to one or more Inventory Item records. This establishes the foundation for all future inventory transactions.

## Mapping Model
```typescript
interface ProductMapping {
  id: string;              // PM-{productNumber}
  productId: string;       // FK to Sprint 24 Product
  productName: string;
  productCode: string;
  productType: string;     // spawn, compost, supplements, tools, packaging, consumables
  inventoryCount: number;  // Number of linked inventory items
  status: InventoryStatus;
  mappedAt: string;
}
```

## Mapping Rules
1. One Product → many Inventory Items (one per variant)
2. A Product can have 0 inventory items (not yet mapped)
3. Product type determines default classification type
4. Inventory count is derived from number of variants × SKUs

## UI
- `ProductsMappingPage`: card grid showing all product mappings
- Each card displays product name, code, type, inventory item count, status, and mapped date
- "View Items" button (placeholder, navigates to filtered registry view)

## Displayed in Preview
- `/preview/inventory/items` → Mapping tab → `PreviewMappingPage`

## Mock Data
12 products with 1–3 variants each, generating 25 inventory items. Each product mapping shows the count of inventory items derived from its variants.
