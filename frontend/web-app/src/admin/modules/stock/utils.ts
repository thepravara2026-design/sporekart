import { paginate } from '../../utils';
import type { StockRecord, StockFilterState, StockSortKey, SortDir, StockState, StockHealth, AvailabilityLevel } from './types';
import { STATE_VARIANTS, STOCK_HEALTH_LEVELS, AVAILABILITY_LEVELS, LOW_STOCK_THRESHOLDS } from './constants';

export function filterStockRecords(items: StockRecord[], query: string, filters: StockFilterState): StockRecord[] {
  return items.filter((item) => {
    if (query) {
      const q = query.toLowerCase();
      const match =
        item.inventoryItemName.toLowerCase().includes(q) ||
        item.code.toLowerCase().includes(q) ||
        item.sku.toLowerCase().includes(q) ||
        item.warehouseName.toLowerCase().includes(q) ||
        item.productName.toLowerCase().includes(q);
      if (!match) return false;
    }

    if (filters.warehouse.length && !filters.warehouse.includes(item.warehouseId)) return false;
    if (filters.stockState.length) {
      const hasState = filters.stockState.some((s) => (item.quantities[s as StockState] ?? 0) > 0);
      if (!hasState) return false;
    }
    if (filters.health.length && !filters.health.includes(item.health)) return false;
    if (filters.availability.length && !filters.availability.includes(item.availability)) return false;
    if (filters.status.length && !filters.status.includes(item.status)) return false;

    return true;
  });
}

export function sortStockRecords(items: StockRecord[], key: StockSortKey, dir: SortDir): StockRecord[] {
  return [...items].sort((a, b) => {
    const aVal = String(a[key] ?? '');
    const bVal = String(b[key] ?? '');
    const cmp = aVal.localeCompare(bVal);
    return dir === 'asc' ? cmp : -cmp;
  });
}

export const paginateItems = <T,>(items: T[], page: number, perPage: number): T[] => paginate(items, page, perPage).items;

export function getStateVariant(state: StockState): 'success' | 'warning' | 'danger' | 'info' | 'neutral' | 'default' {
  return STATE_VARIANTS[state] ?? 'default';
}

export function getHealthVariant(health: StockHealth): 'success' | 'warning' | 'danger' | 'info' | 'neutral' | 'default' {
  return STOCK_HEALTH_LEVELS.find((h) => h.value === health)?.variant ?? 'default';
}

export function getAvailabilityVariant(level: AvailabilityLevel): 'success' | 'warning' | 'danger' | 'info' | 'neutral' | 'default' {
  return AVAILABILITY_LEVELS.find((a) => a.value === level)?.variant ?? 'default';
}

export function getHealthScore(record: StockRecord): number {
  const total = Object.values(record.quantities).reduce((a, b) => a + b, 0);
  if (total === 0) return 0;
  const damaged = record.quantities.damaged ?? 0;
  const expired = record.quantities.expired ?? 0;
  const blocked = record.quantities.blocked ?? 0;
  const bad = damaged + expired + blocked;
  return Math.round(((total - bad) / total) * 100);
}

export function computeHealth(record: StockRecord): StockHealth {
  const available = record.quantities.available ?? 0;
  const total = Object.values(record.quantities).reduce((a, b) => a + b, 0);
  if (total === 0) return 'out_of_stock';
  if (available <= LOW_STOCK_THRESHOLDS.criticalThreshold) return 'critical';
  if (available <= LOW_STOCK_THRESHOLDS.lowThreshold) return 'low';
  if (available >= LOW_STOCK_THRESHOLDS.overstockThreshold) return 'overstock';
  if ((record.quantities.damaged ?? 0) > 0 || (record.quantities.expired ?? 0) > 0) return 'damaged';
  return 'healthy';
}

export function computeAvailability(record: StockRecord): AvailabilityLevel {
  const available = record.quantities.available ?? 0;
  if (available > 0) return 'available';
  if ((record.quantities.incoming ?? 0) > 0) return 'pre_order';
  if ((record.quantities.reserved ?? 0) > 0) return 'limited';
  return 'unavailable';
}

export function totalStock(record: StockRecord): number {
  return Object.values(record.quantities).reduce((a, b) => a + b, 0);
}

export function getStatusVariant(status: string): 'success' | 'warning' | 'danger' | 'info' | 'neutral' | 'default' {
  const map: Record<string, 'success' | 'warning' | 'danger' | 'info' | 'neutral' | 'default'> = {
    active: 'success', inactive: 'neutral', archived: 'neutral',
    pending: 'warning', draft: 'info', verified: 'success',
  };
  return map[status] ?? 'default';
}
