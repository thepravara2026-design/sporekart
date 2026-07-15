import type { InventoryItemRecord, ProductMapping, VariantMapping, SKUMapping, InventoryItemMetric, RecentActivity, LifecycleEvent } from '../types';
import { MOCK_PRODUCTS, MOCK_VARIANTS, LIFECYCLE_STAGES, CLASSIFICATION_TYPES, CLASSIFICATION_GRADES, INVENTORY_ITEM_MOCK_METRICS, INVENTORY_ITEM_RECENT_ACTIVITIES } from '../constants';

function randomItem<T>(arr: T[]): T {
  return arr[Math.floor(Math.random() * arr.length)];
}

function pad(n: number, width: number = 4): string {
  return String(n).padStart(width, '0');
}

function dateSub(days: number): string {
  const d = new Date();
  d.setDate(d.getDate() - days);
  return d.toISOString();
}

const STATUSES: string[] = ['active', 'active', 'active', 'active', 'inactive', 'draft', 'verified', 'verified'];
const BRANDS: string[] = ['SporeKart', 'AgriGrow', 'GreenLine', 'Mycelium Pro', 'SporeKart', 'SporeKart'];
const UNITS: string[] = ['kg', 'g', 'pieces', 'boxes', 'packets'];

function generateLifecycleEvents(): LifecycleEvent[] {
  const events: LifecycleEvent[] = [];
  const stages = LIFECYCLE_STAGES;
  const now = Date.now();
  let offset = 0;

  const activeEvents = [stages[0], stages[1], stages[2], stages[3]];
  for (const stage of activeEvents) {
    offset += Math.floor(Math.random() * 7 * 86400000) + 86400000;
    events.push({
      id: `lev-${events.length + 1}`,
      stage: stage.value,
      timestamp: new Date(now - offset).toISOString(),
      user: randomItem(['Admin User', 'Inventory Manager', 'System', 'Operator']),
      note: `Stage changed to ${stage.label.toLowerCase()}`,
    });
  }
  return events;
}

function generateInventoryItems(): InventoryItemRecord[] {
  const items: InventoryItemRecord[] = [];

  let itemCounter = 1000;
  for (let i = 0; i < MOCK_PRODUCTS.length; i++) {
    const product = MOCK_PRODUCTS[i];
    const productVariants = MOCK_VARIANTS.filter((v) => v.productId === product.id);

    for (let j = 0; j < Math.min(productVariants.length, 3); j++) {
      const variant = productVariants[j];
      const classificationType = randomItem(CLASSIFICATION_TYPES);
      const grade = randomItem(CLASSIFICATION_GRADES);
      const events = generateLifecycleEvents();
      const currentStage = events[events.length - 1]?.stage ?? 'draft';
      const created = dateSub(30 + Math.floor(Math.random() * 200));

      items.push({
        id: `INV-${pad(itemCounter)}`,
        code: `INV-${pad(itemCounter)}`,
        productId: product.id,
        productName: product.name,
        variantId: variant.id,
        variantName: variant.name,
        sku: `${product.code}-${variant.name.replace(/[^a-zA-Z0-9]/g, '-')}`,
        name: `${product.name} - ${variant.name}`,
        description: `Inventory record for ${product.name} in ${variant.name} configuration.`,
        status: randomItem(STATUSES) as InventoryItemRecord['status'],
        classification: {
          type: classificationType.value,
          category: product.type,
          subcategory: classificationType.label,
          class: product.name,
          grade: grade.value,
        },
        lifecycle: currentStage,
        lifecycleEvents: events,
        unit: randomItem(UNITS),
        brand: randomItem(BRANDS),
        category: product.type,
        inventoryId: `INV-${pad(itemCounter)}`,
        createdAt: created,
        updatedAt: dateSub(Math.floor(Math.random() * 14)),
      });

      itemCounter++;
    }
  }

  return items;
}

function generateProductMappings(): ProductMapping[] {
  return MOCK_PRODUCTS.map((p) => ({
    id: `PM-${p.id.replace('PRD-', '')}`,
    productId: p.id,
    productName: p.name,
    productCode: p.code,
    productType: p.type,
    inventoryCount: MOCK_VARIANTS.filter((v) => v.productId === p.id).length * 2,
    status: 'active' as const,
    mappedAt: dateSub(Math.floor(Math.random() * 60)),
  }));
}

function generateVariantMappings(): VariantMapping[] {
  return MOCK_VARIANTS.map((v) => {
    const product = MOCK_PRODUCTS.find((p) => p.id === v.productId);
    return {
      id: `VM-${v.id.replace('VAR-', '')}`,
      variantId: v.id,
      variantName: v.name,
      productId: v.productId,
      productName: product?.name ?? '',
      skuCount: Math.floor(Math.random() * 3) + 1,
      inventoryCount: Math.floor(Math.random() * 4) + 1,
      status: 'active' as const,
      mappedAt: dateSub(Math.floor(Math.random() * 45)),
    };
  });
}

function generateSKUMappings(): SKUMapping[] {
  const items = generateInventoryItems().slice(0, 18);
  return items.map((item, i) => ({
    id: `SKU-${pad(i + 1)}`,
    sku: item.sku,
    inventoryItemId: item.id,
    inventoryItemName: item.name,
    variantId: item.variantId,
    variantName: item.variantName,
    productId: item.productId,
    productName: item.productName,
    status: item.status,
    mappedAt: dateSub(Math.floor(Math.random() * 30)),
  }));
}

export function getInventoryItemMetrics(): InventoryItemMetric[] {
  return INVENTORY_ITEM_MOCK_METRICS.filter((m) =>
    ['total_items', 'mapped_products', 'variants', 'active_sku', 'classified', 'draft_items'].includes(m.id),
  );
}

export function getInventoryItemActivities(): RecentActivity[] {
  return INVENTORY_ITEM_RECENT_ACTIVITIES;
}

let cachedItems: InventoryItemRecord[] | null = null;
let cachedProductMappings: ProductMapping[] | null = null;
let cachedVariantMappings: VariantMapping[] | null = null;
let cachedSKUMappings: SKUMapping[] | null = null;

export function getItems(): InventoryItemRecord[] {
  if (!cachedItems) cachedItems = generateInventoryItems();
  return cachedItems;
}

export function getProductMappings(): ProductMapping[] {
  if (!cachedProductMappings) cachedProductMappings = generateProductMappings();
  return cachedProductMappings;
}

export function getVariantMappings(): VariantMapping[] {
  if (!cachedVariantMappings) cachedVariantMappings = generateVariantMappings();
  return cachedVariantMappings;
}

export function getSKUMappings(): SKUMapping[] {
  if (!cachedSKUMappings) cachedSKUMappings = generateSKUMappings();
  return cachedSKUMappings;
}

export function getItemById(id: string): InventoryItemRecord | undefined {
  return getItems().find((i) => i.id === id || i.code === id);
}
