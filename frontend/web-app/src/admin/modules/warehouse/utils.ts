import type {
  Warehouse,
  WarehouseType,
  WarehouseStatus,
  TemperatureType,
  ZoneType,
  WarehouseFilterState,
  WarehouseSortKey,
  SortDir,
} from './types';
import { WAREHOUSE_TYPES, WAREHOUSE_STATUSES, TEMPERATURE_TYPES, ZONE_TYPES } from './constants';

type Variant = 'success' | 'warning' | 'danger' | 'info' | 'neutral' | 'default';

export function formatNumber(n: number): string {
  return n.toLocaleString('en-US');
}

export function utilization(warehouse: Warehouse): number {
  if (warehouse.capacity === 0) return 0;
  return Math.round((warehouse.used / warehouse.capacity) * 100);
}

export function warehouseTypeLabel(type: WarehouseType): string {
  return WAREHOUSE_TYPES.find((t) => t.value === type)?.label ?? type;
}

export function warehouseStatusVariant(status: WarehouseStatus): Variant {
  return (WAREHOUSE_STATUSES.find((s) => s.value === status)?.variant as Variant) ?? 'default';
}

export function warehouseStatusLabel(status: WarehouseStatus): string {
  return WAREHOUSE_STATUSES.find((s) => s.value === status)?.label ?? status;
}

export function temperatureLabel(t: TemperatureType): string {
  return TEMPERATURE_TYPES.find((x) => x.value === t)?.label ?? t;
}

export function zoneTypeLabel(t: ZoneType): string {
  return ZONE_TYPES.find((x) => x.value === t)?.label ?? t;
}

export function filterWarehouses(items: Warehouse[], state: WarehouseFilterState): Warehouse[] {
  return items.filter((w) => {
    if (state.type.length && !state.type.includes(w.type)) return false;
    if (state.status.length && !state.status.includes(w.status)) return false;
    if (state.location.length && !state.location.includes(w.location.toLowerCase())) return false;
    if (state.temperatureType.length && !state.temperatureType.includes(w.temperatureType)) return false;
    if (state.capacity.length) {
      const u = utilization(w);
      const ok = state.capacity.some((c) => {
        if (c === 'lt_50') return u < 50;
        if (c === '50_80') return u >= 50 && u <= 80;
        if (c === 'gt_80') return u > 80;
        return false;
      });
      if (!ok) return false;
    }
    return true;
  });
}
export function sortWarehouses(items: Warehouse[], key: WarehouseSortKey, dir: SortDir): Warehouse[] {
  const sorted = [...items].sort((a, b) => {
    let av: string | number = a[key];
    let bv: string | number = b[key];
    if (typeof av === 'string') av = av.toLowerCase();
    if (typeof bv === 'string') bv = bv.toLowerCase();
    if (av < bv) return -1;
    if (av > bv) return 1;
    return 0;
  });
  return dir === 'desc' ? sorted.reverse() : sorted;
}

export function searchWarehouses(items: Warehouse[], query: string, fields: string[]): Warehouse[] {
  const q = query.trim().toLowerCase();
  if (!q) return items;
  return items.filter((w) => {
    const haystack: Record<string, string> = {
      name: w.name,
      code: w.code,
      location: w.location,
      status: w.status,
      type: w.type,
      manager: w.manager,
    };
    if (fields.length === 0) return Object.values(haystack).some((v) => v.toLowerCase().includes(q));
    return fields.some((f) => (haystack[f] ?? '').toLowerCase().includes(q));
  });
}


