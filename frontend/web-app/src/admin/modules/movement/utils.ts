export { paginate } from '../../utils';
import type { TransactionRecord, TransferRecord, AdjustmentRecord, MovementTimelineEvent, MovementFilterState, TransactionStatus } from './types';
import { TRANSACTION_STATUSES, MOVEMENT_TYPES } from './constants';

export function filterTransactions(transactions: TransactionRecord[], filters: MovementFilterState): TransactionRecord[] {
  return transactions.filter((t) => {
    if (filters.movementType.length > 0 && !filters.movementType.includes(t.movementType)) return false;
    if (filters.warehouse.length > 0 && !filters.warehouse.includes(t.warehouse)) return false;
    if (filters.status.length > 0 && !filters.status.includes(t.status)) return false;
    if (filters.product.length > 0 && !filters.product.includes(t.product)) return false;
    if (filters.dateRange.start && new Date(t.createdAt) < new Date(filters.dateRange.start)) return false;
    if (filters.dateRange.end && new Date(t.createdAt) > new Date(filters.dateRange.end)) return false;
    return true;
  });
}

export function searchTransactions(transactions: TransactionRecord[], query: string): TransactionRecord[] {
  if (!query) return transactions;
  const q = query.toLowerCase();
  return transactions.filter((t) =>
    t.id.toLowerCase().includes(q) ||
    t.referenceNumber.toLowerCase().includes(q) ||
    t.product.toLowerCase().includes(q) ||
    t.sku.toLowerCase().includes(q) ||
    t.batchCode.toLowerCase().includes(q) ||
    t.warehouse.toLowerCase().includes(q)
  );
}

export function filterTransfers(transfers: TransferRecord[], query: string): TransferRecord[] {
  if (!query) return transfers;
  const q = query.toLowerCase();
  return transfers.filter((t) =>
    t.transferNumber.toLowerCase().includes(q) ||
    t.product.toLowerCase().includes(q) ||
    t.sourceWarehouse.toLowerCase().includes(q) ||
    t.destinationWarehouse.toLowerCase().includes(q)
  );
}

export function filterAdjustments(adjustments: AdjustmentRecord[], query: string): AdjustmentRecord[] {
  if (!query) return adjustments;
  const q = query.toLowerCase();
  return adjustments.filter((a) =>
    a.adjustmentNumber.toLowerCase().includes(q) ||
    a.product.toLowerCase().includes(q) ||
    a.reasonCode.toLowerCase().includes(q)
  );
}

export function filterTimeline(events: MovementTimelineEvent[], query: string): MovementTimelineEvent[] {
  if (!query) return events;
  const q = query.toLowerCase();
  return events.filter((e) =>
    e.event.toLowerCase().includes(q) ||
    e.user.toLowerCase().includes(q) ||
    (e.note && e.note.toLowerCase().includes(q))
  );
}

export function sortTransactions<T extends TransactionRecord>(items: T[], key: keyof T, dir: 'asc' | 'desc'): T[] {
  return [...items].sort((a, b) => {
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

export function getStatusVariant(status: TransactionStatus): 'success' | 'warning' | 'danger' | 'info' | 'neutral' | 'default' {
  return TRANSACTION_STATUSES.find((s) => s.value === status)?.variant ?? 'default';
}

export function getMovementTypeLabel(type: string): string {
  return MOVEMENT_TYPES.find((m) => m.value === type)?.label ?? type;
}

export function generateTransactionValidationErrors(transactions: TransactionRecord[]): { field: string; message: string; severity: 'error' | 'warning' | 'info' }[] {
  const errors: { field: string; message: string; severity: 'error' | 'warning' | 'info' }[] = [];
  const seen = new Set<string>();
  for (const t of transactions) {
    if (seen.has(t.referenceNumber)) errors.push({ field: 'referenceNumber', message: `Duplicate reference: ${t.referenceNumber}`, severity: 'error' });
    seen.add(t.referenceNumber);
    if (!t.warehouse) errors.push({ field: 'warehouse', message: `Transaction ${t.referenceNumber} missing warehouse`, severity: 'error' });
    if (!t.inventoryItemId) errors.push({ field: 'inventoryItem', message: `Transaction ${t.referenceNumber} missing inventory item`, severity: 'error' });
    if (t.sourceLocation === t.destinationLocation && t.movementType === 'warehouse_transfer')
      errors.push({ field: 'location', message: `Transfer ${t.referenceNumber} has same source and destination`, severity: 'warning' });
  }
  return errors;
}
