import type { InventoryStatus, StockStatus, InventoryFilterState, InventoryItem, InventoryUnit } from './types';
import { STOCK_STATUSES, INVENTORY_STATUSES, INVENTORY_UNITS } from './constants';

type StatusVariant = 'success' | 'warning' | 'danger' | 'info' | 'neutral' | 'default';

export function statusVariant(status: InventoryStatus): StatusVariant {
  return (INVENTORY_STATUSES.find((s) => s.value === status)?.variant as StatusVariant) ?? 'default';
}

export function stockStatusVariant(status: StockStatus): StatusVariant {
  return (STOCK_STATUSES.find((s) => s.value === status)?.variant as StatusVariant) ?? 'default';
}

export function statusLabel(status: InventoryStatus): string {
  return INVENTORY_STATUSES.find((s) => s.value === status)?.label ?? status;
}

export function stockStatusLabel(status: StockStatus): string {
  return STOCK_STATUSES.find((s) => s.value === status)?.label ?? status;
}

export function unitLabel(unit: InventoryUnit): string {
  return INVENTORY_UNITS.find((u) => u.value === unit)?.label ?? unit;
}

export function formatNumber(n: number): string {
  return n.toLocaleString('en-US');
}

export function formatPercent(n: number): string {
  return `${n > 0 ? '+' : ''}${n.toFixed(1)}%`;
}

export function getHealthColor(score: number): string {
  if (score >= 90) return 'var(--color-success)';
  if (score >= 75) return 'var(--color-warning)';
  return 'var(--color-danger)';
}

export function getHealthVariant(score: number): StatusVariant {
  if (score >= 90) return 'success';
  if (score >= 75) return 'warning';
  return 'danger';
}

export function filterInventoryItems(items: InventoryItem[], state: InventoryFilterState): InventoryItem[] {
  return items.filter((item) => {
    if (state.warehouse.length && !state.warehouse.includes(item.warehouse)) return false;
    if (state.stockStatus.length && !state.stockStatus.includes(item.stockStatus)) return false;
    if (state.inventoryStatus.length && !state.inventoryStatus.includes(item.status)) return false;
    if (state.category.length && !state.category.includes(item.category)) return false;
    if (state.brand.length && !state.brand.includes(item.brand)) return false;
    if (state.location.length && !state.location.includes(item.location)) return false;
    if (state.batchStatus.length) {
      const batch = (item.batch ?? 'fresh').toLowerCase();
      const allowed = state.batchStatus.some((b) => batch.includes(b.replace('_', ' ')));
      if (!allowed) return false;
    }
    return true;
  });
}

export function searchInventoryItems(
  items: InventoryItem[],
  query: string,
  fields: string[],
): InventoryItem[] {
  const q = query.trim().toLowerCase();
  if (!q) return items;
  return items.filter((item) => {
    const haystack: Record<string, string> = {
      name: item.name,
      sku: item.sku,
      warehouse: item.warehouse,
      batch: item.batch ?? '',
      category: item.category,
      brand: item.brand,
      status: item.status,
      location: item.location,
      inventory_id: item.inventoryId,
    };
    if (fields.length === 0) {
      return Object.values(haystack).some((v) => v.toLowerCase().includes(q));
    }
    return fields.some((f) => (haystack[f] ?? '').toLowerCase().includes(q));
  });
}
