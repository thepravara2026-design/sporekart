export { paginate } from '../../utils';
import type { BatchRecord, LotRecord, BatchTimelineEvent, BatchLifecycleState, ExpiryStatus, QualityStatus, BatchFilterState } from './types';
import { BATCH_LIFECYCLE_STATES, EXPIRY_STATUSES, QUALITY_STATUSES } from './constants';

export function filterBatches(batches: BatchRecord[], filters: BatchFilterState): BatchRecord[] {
  return batches.filter((b) => {
    if (filters.warehouse.length > 0 && !filters.warehouse.includes(b.warehouse)) return false;
    if (filters.product.length > 0 && !filters.product.includes(b.product)) return false;
    if (filters.status.length > 0 && !filters.status.includes(b.status)) return false;
    if (filters.qualityStatus.length > 0 && !filters.qualityStatus.includes(b.qualityStatus)) return false;
    if (filters.expiryStatus.length > 0 && !filters.expiryStatus.includes(b.expiryStatus)) return false;
    if (filters.dateRange.start && new Date(b.createdAt) < new Date(filters.dateRange.start)) return false;
    if (filters.dateRange.end && new Date(b.createdAt) > new Date(filters.dateRange.end)) return false;
    if (filters.shelfLife.min > 0 && b.shelfLife < filters.shelfLife.min) return false;
    if (filters.shelfLife.max > 0 && b.shelfLife > filters.shelfLife.max) return false;
    return true;
  });
}

export function filterLots(lots: LotRecord[], query: string): LotRecord[] {
  if (!query) return lots;
  const q = query.toLowerCase();
  return lots.filter((l) =>
    l.lotCode.toLowerCase().includes(q) ||
    l.batchCode.toLowerCase().includes(q) ||
    l.product.toLowerCase().includes(q) ||
    l.warehouse.toLowerCase().includes(q)
  );
}

export function filterTimeline(events: BatchTimelineEvent[], query: string): BatchTimelineEvent[] {
  if (!query) return events;
  const q = query.toLowerCase();
  return events.filter((e) =>
    e.event.toLowerCase().includes(q) ||
    e.batchCode.toLowerCase().includes(q) ||
    e.user.toLowerCase().includes(q) ||
    (e.note && e.note.toLowerCase().includes(q))
  );
}

export function searchBatches(batches: BatchRecord[], query: string): BatchRecord[] {
  if (!query) return batches;
  const q = query.toLowerCase();
  return batches.filter((b) =>
    b.id.toLowerCase().includes(q) ||
    b.batchCode.toLowerCase().includes(q) ||
    b.product.toLowerCase().includes(q) ||
    b.sku.toLowerCase().includes(q) ||
    b.variant.toLowerCase().includes(q) ||
    b.warehouse.toLowerCase().includes(q)
  );
}

export function sortBatches<T extends BatchRecord>(batches: T[], key: keyof T, dir: 'asc' | 'desc'): T[] {
  return [...batches].sort((a, b) => {
    const aVal = a[key] ?? '';
    const bVal = b[key] ?? '';
    if (typeof aVal === 'string' && typeof bVal === 'string') {
      return dir === 'asc' ? aVal.localeCompare(bVal) : bVal.localeCompare(aVal);
    }
    if (typeof aVal === 'number' && typeof bVal === 'number') {
      return dir === 'asc' ? aVal - bVal : bVal - aVal;
    }
    return 0;
  });
}

export function getLifecycleVariant(state: BatchLifecycleState): 'success' | 'warning' | 'danger' | 'info' | 'neutral' | 'default' {
  return BATCH_LIFECYCLE_STATES.find((s) => s.value === state)?.variant ?? 'default';
}

export function getExpiryVariant(status: ExpiryStatus): 'success' | 'warning' | 'danger' | 'info' | 'neutral' | 'default' {
  return EXPIRY_STATUSES.find((s) => s.value === status)?.variant ?? 'default';
}

export function getQualityVariant(status: QualityStatus): 'success' | 'warning' | 'danger' | 'info' | 'neutral' | 'default' {
  return QUALITY_STATUSES.find((s) => s.value === status)?.variant ?? 'default';
}

export function getLifecycleLabel(state: BatchLifecycleState): string {
  return BATCH_LIFECYCLE_STATES.find((s) => s.value === state)?.label ?? state;
}

export function getExpiryLabel(status: ExpiryStatus): string {
  return EXPIRY_STATUSES.find((s) => s.value === status)?.label ?? status;
}

export function getQualityLabel(status: QualityStatus): string {
  return QUALITY_STATUSES.find((s) => s.value === status)?.label ?? status;
}

export function computeRemainingDays(expiryDate: string): number {
  const now = new Date();
  const expiry = new Date(expiryDate);
  const diff = expiry.getTime() - now.getTime();
  return Math.max(0, Math.ceil(diff / (1000 * 60 * 60 * 24)));
}

export function computeExpiryStatus(expiryDate: string): ExpiryStatus {
  const remaining = computeRemainingDays(expiryDate);
  if (remaining <= 0) return 'expired';
  if (remaining <= 7) return 'critical';
  if (remaining <= 30) return 'near_expiry';
  if (remaining <= 60) return 'monitor';
  if (remaining <= 90) return 'healthy';
  return 'fresh';
}

export function generateValidationErrors(batches: BatchRecord[]): { field: string; message: string; severity: 'error' | 'warning' | 'info' }[] {
  const errors: { field: string; message: string; severity: 'error' | 'warning' | 'info' }[] = [];
  const seen = new Set<string>();
  for (const b of batches) {
    if (seen.has(b.batchCode)) errors.push({ field: 'batchCode', message: `Duplicate batch code: ${b.batchCode}`, severity: 'error' });
    seen.add(b.batchCode);
    if (!b.productionDate || !b.expiryDate) errors.push({ field: 'dates', message: `Batch ${b.batchCode} missing production or expiry date`, severity: 'error' });
    if (b.shelfLife <= 0) errors.push({ field: 'shelfLife', message: `Batch ${b.batchCode} has invalid shelf life`, severity: 'error' });
    if (b.lotCount === 0) errors.push({ field: 'lots', message: `Batch ${b.batchCode} has no associated lots`, severity: 'warning' });
  }
  return errors;
}
