import type { GoodsReceiptRecord, InspectionRecord, ReceivingTimelineEvent, AuditRecord, AnalyticsData, AllocationRecord, BatchAssignmentRecord } from '../types';

const PRODUCTS = ['White Button Mushroom', 'Shiitake Mushroom', 'Oyster Mushroom', 'Enoki Mushroom', 'King Oyster Mushroom', 'Maitake Mushroom'];
const WAREHOUSES = ['Main Warehouse - A', 'Cold Storage - B', 'Dry Storage - C', 'Distribution Center - D', 'Processing Facility - E'];
const USERS = ['receiving@sporekart.com', 'quality@sporekart.com', 'warehouse@sporekart.com', 'inventory@sporekart.com', 'admin@sporekart.com'];
const SUPPLIERS = ['FreshFarm Supplies', 'Mushroom Growers Co-op', 'Organic Harvest Inc', 'Premium Spawn Labs', 'AgriPro Distributors', 'EcoFungi Partners'];
const ZONES = ['Receiving-A', 'Receiving-B', 'Receiving-C', 'Dock-1', 'Dock-2', 'Dock-3'];
const STORAGE_TYPES = ['ambient', 'cold_storage', 'dry', 'hazardous'] as const;

function randomItem<T>(arr: readonly T[]): T { return arr[Math.floor(Math.random() * arr.length)]; }
function randomInt(min: number, max: number): number { return Math.floor(Math.random() * (max - min + 1)) + min; }
function randomDate(daysAgo: number): string {
  const d = new Date(); d.setDate(d.getDate() - randomInt(0, daysAgo)); return d.toISOString();
}

export function generateMockReceipts(count: number = 45): GoodsReceiptRecord[] {
  const receipts: GoodsReceiptRecord[] = [];
  const STATUSES = ['draft', 'pending', 'receiving', 'inspection', 'approved', 'rejected', 'completed', 'cancelled', 'warehouse_allocation_pending', 'batch_pending'] as const;
  const INSP_STATUSES = ['pending', 'visual', 'quality', 'packaging', 'quantity_verification', 'documentation', 'passed', 'failed'] as const;
  const ACCEPT_STATUSES = ['fully_accepted', 'partially_accepted', 'conditionally_accepted', 'accepted_with_notes', 'awaiting_approval', 'not_yet_accepted'] as const;
  const REJECT_REASONS = ['fully_rejected', 'partially_rejected', 'damaged_goods', 'expired_goods', 'packaging_failure', 'quality_failure', 'wrong_item', 'wrong_quantity', 'missing_documents'] as const;
  const ALLOC_STATUSES = ['pending', 'allocating', 'allocated', 'failed', 'not_required'] as const;

  for (let i = 1; i <= count; i++) {
    const product = randomItem(PRODUCTS);
    const status = randomItem(STATUSES);
    const inspStatus = randomItem(INSP_STATUSES);
    const acceptStatus = randomItem(ACCEPT_STATUSES);
    const qty = randomInt(50, 5000);
    const rejectedQty = randomInt(0, Math.round(qty * 0.15));
    receipts.push({
      id: `GRN-${String(i).padStart(3, '0')}`,
      receiptNumber: `GRN-${String(i).padStart(4, '0')}`,
      referenceNumber: `PO-${String(i).padStart(5, '0')}`,
      supplier: randomItem(SUPPLIERS),
      warehouse: randomItem(WAREHOUSES),
      receivingZone: randomItem(ZONES),
      inventoryItemId: `ITEM-${String((i - 1) % 10 + 1).padStart(3, '0')}`,
      product,
      variant: 'Standard',
      batchCode: `BATCH-${String(i % 20 + 1).padStart(4, '0')}`,
      quantity: qty,
      acceptedQty: qty - rejectedQty,
      rejectedQty,
      inspectionQty: qty,
      receiptStatus: status,
      inspectionStatus: inspStatus,
      acceptanceStatus: acceptStatus,
      rejectionReason: randomItem(REJECT_REASONS),
      allocationStatus: randomItem(ALLOC_STATUSES),
      batchAssigned: Math.random() > 0.3,
      notes: `Receipt batch ${i} from ${randomItem(SUPPLIERS)}.`,
      createdBy: randomItem(USERS),
      createdAt: randomDate(90),
      updatedAt: randomDate(7),
    });
  }
  return receipts;
}

export function generateMockInspections(receipts: GoodsReceiptRecord[]): InspectionRecord[] {
  return receipts.slice(0, 30).map((r, i) => ({
    id: `INSP-${String(i + 1).padStart(3, '0')}`,
    receiptId: r.id,
    receiptNumber: r.receiptNumber,
    product: r.product,
    warehouse: r.warehouse,
    inspector: randomItem(USERS),
    type: randomItem(['visual', 'quality', 'packaging', 'quantity', 'documentation', 'temperature', 'humidity', 'laboratory'] as const),
    status: r.inspectionStatus,
    packagingQuality: Math.random() > 0.15,
    productQuality: Math.random() > 0.1,
    quantityMatch: Math.random() > 0.1,
    labelVerified: Math.random() > 0.05,
    expiryVerified: Math.random() > 0.05,
    batchVerified: Math.random() > 0.1,
    damageDetected: Math.random() > 0.85,
    storageCompliant: Math.random() > 0.1,
    certified: Math.random() > 0.2,
    notes: `Inspection ${i + 1} for ${r.product}.`,
    startedAt: r.createdAt,
    completedAt: randomDate(3),
  }));
}

export function generateMockTimeline(receipts: GoodsReceiptRecord[]): ReceivingTimelineEvent[] {
  const events: ReceivingTimelineEvent[] = [];
  for (const r of receipts) {
    events.push({ id: `EVT-${r.id}-001`, receiptId: r.id, event: 'Receipt Created', fromStatus: '', toStatus: 'draft', user: r.createdBy, timestamp: r.createdAt, note: 'Receipt initialized.' });
    if (r.receiptStatus !== 'draft') {
      events.push({ id: `EVT-${r.id}-002`, receiptId: r.id, event: 'Goods Arrived', fromStatus: 'draft', toStatus: 'pending', user: r.createdBy, timestamp: new Date(new Date(r.createdAt).getTime() + 3600000).toISOString(), note: 'Goods arrived at warehouse.' });
    }
    if (r.receiptStatus === 'receiving' || r.receiptStatus === 'inspection' || r.receiptStatus === 'approved' || r.receiptStatus === 'rejected' || r.receiptStatus === 'completed') {
      events.push({ id: `EVT-${r.id}-003`, receiptId: r.id, event: 'Receiving Started', fromStatus: 'pending', toStatus: 'receiving', user: randomItem(USERS), timestamp: new Date(new Date(r.createdAt).getTime() + 7200000).toISOString(), note: 'Goods receiving in progress.' });
    }
    if (r.receiptStatus === 'inspection' || r.receiptStatus === 'approved' || r.receiptStatus === 'completed') {
      events.push({ id: `EVT-${r.id}-004`, receiptId: r.id, event: 'Inspection Started', fromStatus: 'receiving', toStatus: 'inspection', user: randomItem(USERS), timestamp: new Date(new Date(r.createdAt).getTime() + 10800000).toISOString(), note: 'Quality inspection initiated.' });
    }
    if (r.receiptStatus === 'inspection') {
      events.push({ id: `EVT-${r.id}-005`, receiptId: r.id, event: 'Inspection In Progress', fromStatus: 'inspection', toStatus: 'inspection', user: randomItem(USERS), timestamp: new Date(new Date(r.createdAt).getTime() + 14400000).toISOString(), note: `${r.inspectionStatus.replace(/_/g, ' ')} check completed.` });
    }
    if (r.receiptStatus === 'approved') {
      events.push({ id: `EVT-${r.id}-006`, receiptId: r.id, event: 'Accepted', fromStatus: 'inspection', toStatus: 'approved', user: randomItem(USERS), timestamp: new Date(new Date(r.createdAt).getTime() + 18000000).toISOString(), note: 'Goods accepted.' });
    }
    if (r.receiptStatus === 'rejected') {
      events.push({ id: `EVT-${r.id}-006`, receiptId: r.id, event: 'Rejected', fromStatus: 'inspection', toStatus: 'rejected', user: randomItem(USERS), timestamp: new Date(new Date(r.createdAt).getTime() + 18000000).toISOString(), note: `Rejected: ${r.rejectionReason.replace(/_/g, ' ')}.` });
    }
    if (r.receiptStatus === 'completed') {
      events.push({ id: `EVT-${r.id}-007`, receiptId: r.id, event: 'Completed', fromStatus: 'approved', toStatus: 'completed', user: randomItem(USERS), timestamp: new Date(new Date(r.createdAt).getTime() + 21600000).toISOString(), note: 'Receipt completed successfully.' });
    }
  }
  return events;
}

export function generateMockAudit(receipts: GoodsReceiptRecord[]): AuditRecord[] {
  const audits: AuditRecord[] = [];
  for (const r of receipts.slice(0, 25)) {
    audits.push({ id: `AUD-${r.id}`, receiptId: r.id, action: 'Created', field: 'receiptStatus', oldValue: '', newValue: r.receiptStatus, user: r.createdBy, timestamp: r.createdAt });
    audits.push({ id: `AUD-${r.id}-2`, receiptId: r.id, action: 'Modified', field: 'inspectionStatus', oldValue: 'pending', newValue: r.inspectionStatus, user: randomItem(USERS), timestamp: r.updatedAt });
  }
  return audits;
}

export function generateMockAllocations(receipts: GoodsReceiptRecord[]): AllocationRecord[] {
  const allocated = receipts.filter((r) => r.allocationStatus !== 'not_required');
  return allocated.slice(0, 20).map((r, i) => ({
    id: `ALC-${String(i + 1).padStart(3, '0')}`,
    receiptId: r.id,
    warehouse: r.warehouse,
    zone: `Zone-${randomInt(1, 5)}`,
    rack: `Rack-${randomInt(1, 15)}`,
    shelf: `Shelf-${String.fromCharCode(65 + randomInt(0, 5))}`,
    bin: `Bin-${String(randomInt(1, 30)).padStart(2, '0')}`,
    storageType: randomItem(STORAGE_TYPES),
    status: r.allocationStatus,
    allocatedAt: randomDate(5),
  }));
}

export function generateMockBatchAssignments(receipts: GoodsReceiptRecord[]): BatchAssignmentRecord[] {
  const batched = receipts.filter((r) => r.batchAssigned);
  return batched.slice(0, 20).map((r, i) => ({
    id: `BA-${String(i + 1).padStart(3, '0')}`,
    receiptId: r.id,
    batchCode: r.batchCode,
    lotCode: `LOT-${String(i + 1).padStart(4, '0')}`,
    expiryDate: new Date(Date.now() + randomInt(30, 365) * 86400000).toISOString(),
    shelfLife: `${randomInt(3, 24)} months`,
    qualityStatus: randomItem(['approved', 'pending_inspection', 'under_review']),
    assignedAt: randomDate(5),
  }));
}

function generateAnalytics(receipts: GoodsReceiptRecord[]): AnalyticsData {
  return {
    totalReceipts: receipts.length,
    pendingReceipts: receipts.filter((r) => r.receiptStatus === 'pending' || r.receiptStatus === 'draft').length,
    completedReceipts: receipts.filter((r) => r.receiptStatus === 'completed').length,
    rejectedReceipts: receipts.filter((r) => r.receiptStatus === 'rejected').length,
    underInspection: receipts.filter((r) => r.receiptStatus === 'inspection').length,
    awaitingApproval: receipts.filter((r) => r.acceptanceStatus === 'awaiting_approval').length,
    acceptedGoods: receipts.reduce((sum, r) => sum + r.acceptedQty, 0),
    rejectedGoods: receipts.reduce((sum, r) => sum + r.rejectedQty, 0),
    warehousePending: receipts.filter((r) => r.allocationStatus === 'pending' || r.allocationStatus === 'allocating').length,
    batchPending: receipts.filter((r) => !r.batchAssigned).length,
    acceptanceRate: Math.round((receipts.filter((r) => r.receiptStatus === 'approved' || r.receiptStatus === 'completed').length / Math.max(1, receipts.length)) * 100),
    rejectionRate: Math.round((receipts.filter((r) => r.receiptStatus === 'rejected').length / Math.max(1, receipts.length)) * 100),
    warehouseDistribution: WAREHOUSES.map((w) => ({ warehouse: w, count: receipts.filter((r) => r.warehouse === w).length })),
  };
}

export const MOCK_RECEIPTS = generateMockReceipts(45);
export const MOCK_INSPECTIONS = generateMockInspections(MOCK_RECEIPTS);
export const MOCK_TIMELINE = generateMockTimeline(MOCK_RECEIPTS);
export const MOCK_AUDIT = generateMockAudit(MOCK_RECEIPTS);
export const MOCK_ALLOCATIONS = generateMockAllocations(MOCK_RECEIPTS);
export const MOCK_BATCH_ASSIGNMENTS = generateMockBatchAssignments(MOCK_RECEIPTS);
export const MOCK_ANALYTICS = generateAnalytics(MOCK_RECEIPTS);
