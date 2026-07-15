import { paginate } from '../../utils';
import type { InventoryItemRecord, InventoryItemFilterState, InventoryItemSortKey, SortDir } from './types';

export function filterInventoryItems(
  items: InventoryItemRecord[],
  query: string,
  filters: InventoryItemFilterState,
): InventoryItemRecord[] {
  return items.filter((item) => {
    if (query) {
      const q = query.toLowerCase();
      const match =
        item.name.toLowerCase().includes(q) ||
        item.code.toLowerCase().includes(q) ||
        item.sku.toLowerCase().includes(q) ||
        item.productName.toLowerCase().includes(q) ||
        item.variantName.toLowerCase().includes(q) ||
        item.brand.toLowerCase().includes(q) ||
        item.category.toLowerCase().includes(q);
      if (!match) return false;
    }

    if (filters.status.length && !filters.status.includes(item.status)) return false;
    if (filters.classification.length && !filters.classification.includes(item.classification.type)) return false;
    if (filters.lifecycle.length && !filters.lifecycle.includes(item.lifecycle)) return false;
    if (filters.category.length && !filters.category.includes(item.category)) return false;
    if (filters.brand.length && !filters.brand.includes(item.brand)) return false;
    if (filters.unit.length && !filters.unit.includes(item.unit)) return false;
    if (filters.grade.length && !filters.grade.includes(item.classification.grade)) return false;

    return true;
  });
}

export function sortInventoryItems(
  items: InventoryItemRecord[],
  key: InventoryItemSortKey,
  dir: SortDir,
): InventoryItemRecord[] {
  return [...items].sort((a, b) => {
    const aVal = String(a[key] ?? '');
    const bVal = String(b[key] ?? '');
    const cmp = aVal.localeCompare(bVal);
    return dir === 'asc' ? cmp : -cmp;
  });
}

export const paginateItems = <T,>(items: T[], page: number, perPage: number): T[] => paginate(items, page, perPage).items;

export function getLifecycleVariant(stage: string): 'success' | 'warning' | 'danger' | 'info' | 'neutral' | 'default' {
  const map: Record<string, 'success' | 'warning' | 'danger' | 'info' | 'neutral' | 'default'> = {
    draft: 'neutral',
    pending_approval: 'warning',
    approved: 'info',
    active: 'success',
    frozen: 'info',
    suspended: 'warning',
    discontinued: 'default',
    archived: 'neutral',
  };
  return map[stage] ?? 'neutral';
}

export function getStatusVariant(status: string): 'success' | 'warning' | 'danger' | 'info' | 'neutral' | 'default' {
  const map: Record<string, 'success' | 'warning' | 'danger' | 'info' | 'neutral' | 'default'> = {
    active: 'success',
    inactive: 'neutral',
    archived: 'neutral',
    pending: 'warning',
    draft: 'info',
    verified: 'success',
  };
  return map[status] ?? 'default';
}
