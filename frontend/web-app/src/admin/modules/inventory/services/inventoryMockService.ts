import type { InventoryItem, Warehouse, InventoryMetric, HealthMetric, RecentActivity } from '../types';
import { INVENTORY_MOCK_METRICS, INVENTORY_HEALTH, INVENTORY_RECENT_ACTIVITIES } from '../constants';

const WAREHOUSES = ['Warehouse A', 'Warehouse B', 'Warehouse C', 'Warehouse D'];
const CATEGORIES = ['Seeds', 'Fertilizer', 'Tools', 'Packaging'];
const BRANDS = ['SporeKart', 'AgriGrow', 'GreenLine'];
const STATUSES = ['active', 'inactive', 'pending', 'draft', 'verified'] as const;
const STOCK = ['available', 'reserved', 'incoming', 'low', 'out_of_stock', 'damaged', 'expired'] as const;
const UNITS = ['kg', 'g', 'pieces', 'boxes', 'packets', 'bundles', 'litres', 'millilitres'] as const;

function seedDeterministic(index: number): number {
  const x = Math.sin(index * 999.13) * 10000;
  return x - Math.floor(x);
}

export function generateInventoryItems(count = 60): InventoryItem[] {
  return Array.from({ length: count }, (_, i) => {
    const r = seedDeterministic(i + 1);
    const stockStatus = STOCK[Math.floor(r * STOCK.length)];
    const stockLevel = Math.floor(seedDeterministic(i + 7) * 1200);
    const idNum = 2000 + i;
    return {
      id: `INV-${idNum}`,
      inventoryId: `INV-${idNum}`,
      name: `${CATEGORIES[i % CATEGORIES.length]} Item ${i + 1}`,
      sku: `SKU-${String(100000 + i).slice(1)}`,
      warehouse: WAREHOUSES[i % WAREHOUSES.length],
      category: CATEGORIES[i % CATEGORIES.length],
      brand: BRANDS[i % BRANDS.length],
      status: STATUSES[i % STATUSES.length],
      stockStatus,
      stockLevel,
      reorderPoint: Math.floor(seedDeterministic(i + 11) * 100) + 10,
      unit: UNITS[i % UNITS.length],
      location: `Aisle ${(i % 20) + 1}, Shelf ${(i % 10) + 1}`,
      batch: `B-${(i % 50) + 1}`,
      createdAt: `2026-0${(i % 9) + 1}-${String((i % 28) + 1).padStart(2, '0')}`,
      updatedAt: `2026-0${(i % 9) + 1}-${String((i % 28) + 1).padStart(2, '0')}`,
    };
  });
}

export function generateWarehouses(): Warehouse[] {
  return WAREHOUSES.map((name, i) => ({
    id: `WH-${i + 1}`,
    name,
    location: ['Mumbai', 'Delhi', 'Pune', 'Bengaluru'][i],
    status: 'active',
    capacity: 100000,
    used: Math.floor(seedDeterministic(i + 3) * 100000),
  }));
}

function delay<T>(data: T, ms = 500): Promise<T> {
  return new Promise((resolve) => setTimeout(() => resolve(data), ms));
}

export const inventoryMockService = {
  fetchInventoryItems: (): Promise<InventoryItem[]> => delay(generateInventoryItems()),
  fetchWarehouses: (): Promise<Warehouse[]> => delay(generateWarehouses()),
  fetchDashboardMetrics: (): Promise<{ metrics: InventoryMetric[]; health: HealthMetric[] }> =>
    delay({ metrics: INVENTORY_MOCK_METRICS, health: INVENTORY_HEALTH }),
  fetchRecentActivities: (): Promise<RecentActivity[]> => delay(INVENTORY_RECENT_ACTIVITIES),
};
