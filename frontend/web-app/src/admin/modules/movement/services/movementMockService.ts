import type { TransactionRecord, TransferRecord, AdjustmentRecord, GoodsReceiptRecord, GoodsIssueRecord, MovementTimelineEvent, AuditRecord, AnalyticsData } from '../types';

const PRODUCTS = ['White Button Mushroom', 'Shiitake Mushroom', 'Oyster Mushroom', 'Enoki Mushroom', 'King Oyster Mushroom', 'Maitake Mushroom'];
const WAREHOUSES = ['Main Warehouse - A', 'Cold Storage - B', 'Dry Storage - C', 'Distribution Center - D', 'Processing Facility - E'];
const USERS = ['inventory@sporekart.com', 'warehouse@sporekart.com', 'admin@sporekart.com', 'quality@sporekart.com'];
const STATUSES = ['draft', 'pending', 'approved', 'processing', 'completed', 'cancelled', 'archived', 'rejected', 'failed_validation', 'blocked'] as const;
const MOVEMENT_TYPES = ['goods_receipt', 'goods_issue', 'warehouse_transfer', 'stock_adjustment', 'damage', 'return', 'quality_hold', 'inspection', 'reservation', 'release', 'consumption', 'production_issue', 'production_receipt', 'cycle_count_adjustment', 'manual_adjustment', 'marketplace_allocation', 'shipment', 'manufacturing'] as const;

function randomItem<T>(arr: readonly T[]): T { return arr[Math.floor(Math.random() * arr.length)]; }
function randomInt(min: number, max: number): number { return Math.floor(Math.random() * (max - min + 1)) + min; }

function randomDate(daysAgo: number): string {
  const d = new Date(); d.setDate(d.getDate() - randomInt(0, daysAgo)); return d.toISOString();
}

export function generateMockTransactions(count: number = 65): TransactionRecord[] {
  const transactions: TransactionRecord[] = [];
  for (let i = 1; i <= count; i++) {
    const product = randomItem(PRODUCTS);
    const movementType = randomItem(MOVEMENT_TYPES);
    const src = randomItem(WAREHOUSES);
    let dst = randomItem(WAREHOUSES);
    while (dst === src && movementType === 'warehouse_transfer') dst = randomItem(WAREHOUSES);
    const status = randomItem(STATUSES);
    transactions.push({
      id: `TXN-${String(i).padStart(3, '0')}`,
      referenceNumber: `REF-${String(i).padStart(4, '0')}`,
      movementType,
      inventoryItemId: `ITEM-${String((i - 1) % 10 + 1).padStart(3, '0')}`,
      product,
      variant: 'Standard',
      sku: `SKU-${String((i - 1) % 10 + 1).padStart(3, '0')}`,
      batchCode: `BATCH-${String(i % 20 + 1).padStart(4, '0')}`,
      warehouseId: `WH-${(WAREHOUSES.indexOf(src) + 1).toString().padStart(2, '0')}`,
      warehouse: src,
      sourceLocation: src,
      destinationLocation: movementType === 'warehouse_transfer' ? dst : src,
      status,
      quantity: randomInt(10, 5000),
      reasonCode: randomItem(['REORDER', 'DAMAGE', 'TRANSFER', 'RETURN', 'ADJUSTMENT']),
      requestedBy: randomItem(USERS),
      createdAt: randomDate(90),
      updatedAt: randomDate(7),
    });
  }
  return transactions;
}

export function generateMockTransfers(transactions: TransactionRecord[]): TransferRecord[] {
  return transactions.filter((t) => t.movementType === 'warehouse_transfer').slice(0, 18).map((t, i) => ({
    id: `TRF-${String(i + 1).padStart(3, '0')}`,
    transferNumber: t.referenceNumber,
    sourceWarehouse: t.sourceLocation,
    destinationWarehouse: t.destinationLocation,
    sourceZone: `Zone-${randomInt(1, 5)}`,
    destinationZone: `Zone-${randomInt(1, 5)}`,
    sourceBin: `Bin-${randomInt(1, 20)}`,
    destinationBin: `Bin-${randomInt(1, 20)}`,
    inventoryItemId: t.inventoryItemId,
    product: t.product,
    quantity: t.quantity,
    reason: 'Stock redistribution',
    status: t.status,
    createdAt: t.createdAt,
    updatedAt: t.updatedAt,
  }));
}

export function generateMockAdjustments(transactions: TransactionRecord[]): AdjustmentRecord[] {
  const adjTypes = ['positive', 'negative', 'damage', 'expiry', 'audit', 'cycle_count', 'manual'] as const;
  return transactions.filter((t) => ['stock_adjustment', 'damage', 'cycle_count_adjustment', 'manual_adjustment'].includes(t.movementType)).slice(0, 9).map((t, i) => ({
    id: `ADJ-${String(i + 1).padStart(3, '0')}`,
    adjustmentNumber: t.referenceNumber,
    type: randomItem(adjTypes),
    inventoryItemId: t.inventoryItemId,
    product: t.product,
    warehouse: t.warehouse,
    quantity: t.quantity,
    reasonCode: t.reasonCode ?? 'ADJUSTMENT',
    notes: 'Stock adjustment recorded.',
    status: t.status,
    createdAt: t.createdAt,
    updatedAt: t.updatedAt,
  }));
}

export function generateMockGoodsReceipts(transactions: TransactionRecord[]): GoodsReceiptRecord[] {
  return transactions.filter((t) => t.movementType === 'goods_receipt').slice(0, 14).map((t, i) => ({
    id: `GRN-${String(i + 1).padStart(3, '0')}`,
    receiptNumber: t.referenceNumber,
    supplier: `Supplier ${randomInt(1, 10)}`,
    warehouse: t.warehouse,
    receivingZone: `Receiving-${randomInt(1, 3)}`,
    inventoryItemId: t.inventoryItemId,
    product: t.product,
    acceptedQty: t.quantity - randomInt(0, Math.round(t.quantity * 0.1)),
    rejectedQty: randomInt(0, Math.round(t.quantity * 0.05)),
    pendingQty: randomInt(0, Math.round(t.quantity * 0.05)),
    status: t.status,
    createdAt: t.createdAt,
    updatedAt: t.updatedAt,
  }));
}

export function generateMockGoodsIssues(transactions: TransactionRecord[]): GoodsIssueRecord[] {
  return transactions.filter((t) => t.movementType === 'goods_issue').slice(0, 12).map((t, i) => ({
    id: `GIS-${String(i + 1).padStart(3, '0')}`,
    issueNumber: t.referenceNumber,
    warehouse: t.warehouse,
    destination: 'Production Floor',
    inventoryItemId: t.inventoryItemId,
    product: t.product,
    quantity: t.quantity,
    reason: randomItem(['Production', 'Order Fulfillment', 'Sample', 'Transfer']),
    status: t.status,
    requestedBy: t.requestedBy,
    createdAt: t.createdAt,
    updatedAt: t.updatedAt,
  }));
}

export function generateMockTimeline(transactions: TransactionRecord[]): MovementTimelineEvent[] {
  const events: MovementTimelineEvent[] = [];
  for (const t of transactions) {
    events.push({ id: `EVT-${t.id}-001`, transactionId: t.id, event: 'Transaction Created', toStatus: 'draft', user: t.requestedBy, timestamp: t.createdAt, note: 'Transaction initialized.' });
    if (t.status !== 'draft') {
      events.push({ id: `EVT-${t.id}-002`, transactionId: t.id, event: 'Submitted for Approval', fromStatus: 'draft', toStatus: 'pending', user: t.requestedBy, timestamp: new Date(new Date(t.createdAt).getTime() + 3600000).toISOString() });
    }
    if (t.status === 'approved' || t.status === 'processing' || t.status === 'completed') {
      events.push({ id: `EVT-${t.id}-003`, transactionId: t.id, event: 'Approved', fromStatus: 'pending', toStatus: 'approved', user: randomItem(USERS), timestamp: new Date(new Date(t.createdAt).getTime() + 7200000).toISOString() });
    }
    if (t.status === 'processing' || t.status === 'completed') {
      events.push({ id: `EVT-${t.id}-004`, transactionId: t.id, event: 'Processing Started', fromStatus: 'approved', toStatus: 'processing', user: randomItem(USERS), timestamp: new Date(new Date(t.createdAt).getTime() + 10800000).toISOString() });
    }
    if (t.status === 'completed') {
      events.push({ id: `EVT-${t.id}-005`, transactionId: t.id, event: 'Completed', fromStatus: 'processing', toStatus: 'completed', user: randomItem(USERS), timestamp: new Date(new Date(t.createdAt).getTime() + 14400000).toISOString(), note: 'Transaction completed successfully.' });
    }
    if (t.status === 'rejected') {
      events.push({ id: `EVT-${t.id}-003`, transactionId: t.id, event: 'Rejected', fromStatus: 'pending', toStatus: 'rejected', user: randomItem(USERS), timestamp: new Date(new Date(t.createdAt).getTime() + 7200000).toISOString(), note: 'Transaction rejected.' });
    }
    if (t.status === 'cancelled') {
      events.push({ id: `EVT-${t.id}-003`, transactionId: t.id, event: 'Cancelled', fromStatus: 'pending', toStatus: 'cancelled', user: t.requestedBy, timestamp: new Date(new Date(t.createdAt).getTime() + 7200000).toISOString() });
    }
  }
  return events;
}

export function generateMockAuditRecords(transactions: TransactionRecord[]): AuditRecord[] {
  const audits: AuditRecord[] = [];
  for (const t of transactions.slice(0, 30)) {
    audits.push({ id: `AUD-${t.id}`, transactionId: t.id, action: 'Created', field: 'status', oldValue: '', newValue: t.status, user: t.requestedBy, timestamp: t.createdAt });
    audits.push({ id: `AUD-${t.id}-2`, transactionId: t.id, action: 'Modified', field: 'quantity', oldValue: '0', newValue: String(t.quantity), user: t.requestedBy, timestamp: t.updatedAt });
  }
  return audits;
}

function generateAnalytics(): AnalyticsData {
  return {
    totalTransactions: 65,
    transfers: 18,
    receipts: 14,
    issues: 12,
    adjustments: 9,
    returns: 5,
    damages: 3,
    pending: 7,
    completed: 48,
    warehouseDistribution: WAREHOUSES.map((w) => ({ warehouse: w, count: randomInt(5, 20) })),
  };
}

export const MOCK_TRANSACTIONS = generateMockTransactions(65);
export const MOCK_TRANSFERS = generateMockTransfers(MOCK_TRANSACTIONS);
export const MOCK_ADJUSTMENTS = generateMockAdjustments(MOCK_TRANSACTIONS);
export const MOCK_GOODS_RECEIPTS = generateMockGoodsReceipts(MOCK_TRANSACTIONS);
export const MOCK_GOODS_ISSUES = generateMockGoodsIssues(MOCK_TRANSACTIONS);
export const MOCK_TIMELINE = generateMockTimeline(MOCK_TRANSACTIONS);
export const MOCK_AUDIT = generateMockAuditRecords(MOCK_TRANSACTIONS);
export const MOCK_ANALYTICS = generateAnalytics();
