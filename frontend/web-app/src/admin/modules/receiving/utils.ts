export { paginate } from '../../utils';
import type { GoodsReceiptRecord, ReceivingFilterState, ReceiptStatus } from './types';
import { RECEIPT_STATUSES, INSPECTION_STATUSES } from './constants';

export function filterReceipts(receipts: GoodsReceiptRecord[], filters: ReceivingFilterState): GoodsReceiptRecord[] {
  return receipts.filter((r) => {
    if (filters.warehouse.length > 0 && !filters.warehouse.includes(r.warehouse)) return false;
    if (filters.receiptStatus.length > 0 && !filters.receiptStatus.includes(r.receiptStatus)) return false;
    if (filters.inspectionStatus.length > 0 && !filters.inspectionStatus.includes(r.inspectionStatus)) return false;
    if (filters.acceptanceStatus.length > 0 && !filters.acceptanceStatus.includes(r.acceptanceStatus)) return false;
    if (filters.product.length > 0 && !filters.product.includes(r.product)) return false;
    if (filters.dateRange.start && new Date(r.createdAt) < new Date(filters.dateRange.start)) return false;
    if (filters.dateRange.end && new Date(r.createdAt) > new Date(filters.dateRange.end)) return false;
    return true;
  });
}

export function searchReceipts(receipts: GoodsReceiptRecord[], query: string): GoodsReceiptRecord[] {
  if (!query) return receipts;
  const q = query.toLowerCase();
  return receipts.filter((r) =>
    r.receiptNumber.toLowerCase().includes(q) ||
    r.referenceNumber.toLowerCase().includes(q) ||
    r.supplier.toLowerCase().includes(q) ||
    r.product.toLowerCase().includes(q) ||
    r.warehouse.toLowerCase().includes(q) ||
    r.batchCode.toLowerCase().includes(q)
  );
}

export function sortReceipts<T extends GoodsReceiptRecord>(items: T[], key: keyof T, dir: 'asc' | 'desc'): T[] {
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

export function getStatusVariant(status: ReceiptStatus): 'success' | 'warning' | 'danger' | 'info' | 'neutral' | 'default' {
  return RECEIPT_STATUSES.find((s) => s.value === status)?.variant ?? 'default';
}

export function getInspectionVariant(status: string): string {
  return INSPECTION_STATUSES.find((s) => s.value === status)?.variant ?? 'neutral';
}

export function generateReceiptValidationErrors(receipts: GoodsReceiptRecord[]): { field: string; message: string; severity: 'error' | 'warning' | 'info' }[] {
  const errors: { field: string; message: string; severity: 'error' | 'warning' | 'info' }[] = [];
  const seen = new Set<string>();
  for (const r of receipts) {
    if (seen.has(r.receiptNumber)) errors.push({ field: 'receiptNumber', message: `Duplicate receipt: ${r.receiptNumber}`, severity: 'error' });
    seen.add(r.receiptNumber);
    if (!r.warehouse) errors.push({ field: 'warehouse', message: `Receipt ${r.receiptNumber} missing warehouse`, severity: 'error' });
    if (!r.product) errors.push({ field: 'product', message: `Receipt ${r.receiptNumber} missing product`, severity: 'error' });
    if (!r.inventoryItemId) errors.push({ field: 'inventoryItem', message: `Receipt ${r.receiptNumber} missing inventory item`, severity: 'error' });
    if (r.receiptStatus === 'completed' && !r.batchAssigned)
      errors.push({ field: 'batch', message: `Receipt ${r.receiptNumber} completed without batch assignment`, severity: 'warning' });
    if (r.receiptStatus === 'completed' && r.allocationStatus !== 'allocated')
      errors.push({ field: 'allocation', message: `Receipt ${r.receiptNumber} completed without warehouse allocation`, severity: 'warning' });
  }
  return errors;
}
