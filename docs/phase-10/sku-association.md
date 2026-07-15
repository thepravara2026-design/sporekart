# SKU Association — Linking SKUs to Inventory Items

## Purpose
Every SKU from Sprint 24 is associated with exactly one Inventory Item record. This is the most granular level of item tracking and is the foundation for stock management, order fulfillment, and warehouse operations.

## Association Model
```typescript
interface SKUMapping {
  id: string;
  sku: string;                  // Composite: {ProductCode}-{VariantName}
  inventoryItemId: string;      // FK to InventoryItemRecord
  inventoryItemName: string;
  variantId: string;
  variantName: string;
  productId: string;
  productName: string;
  status: InventoryStatus;
  mappedAt: string;
}
```

## SKU Generation
SKU strings follow the pattern: `{productCode}-{variantName-sanitized}`
Example: `OYS-SPN-1-kg-Pack`

## Association Rules
1. One SKU → exactly one Inventory Item
2. One Inventory Item → exactly one SKU (1:1)
3. SKU status mirrors the Inventory Item status
4. SKU mapping is created when the Inventory Item is created

## UI
- `SKUMappingPage`: table showing all SKU associations
- Columns: SKU, Inventory Item, Variant, Product, Status, Mapped Date
- SKU displayed in monospace font with background highlight

## Displayed in Preview
- `/preview/inventory/items` → SKU tab → `PreviewSKUPage`

## Mock Data
18 SKU mappings generated from the inventory item records, each with a unique SKU string referencing the product code and variant name.
