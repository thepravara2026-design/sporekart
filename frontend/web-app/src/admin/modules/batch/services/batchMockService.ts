import type { BatchRecord, LotRecord, BatchTimelineEvent, AnalyticsMetric } from '../types';

const PRODUCTS = [
  'White Button Mushroom', 'Shiitake Mushroom', 'Oyster Mushroom', 'Enoki Mushroom', 'King Oyster Mushroom',
  'Maitake Mushroom', 'Lion\'s Mane Mushroom', 'Chanterelle Mushroom', 'Portobello Mushroom', 'Cremini Mushroom',
];

const VARIANTS = [
  'Fresh Whole', 'Fresh Sliced', 'Dried Whole', 'Dried Powder', 'Organic Fresh',
  'Organic Dried', 'Premium Grade', 'Standard Grade', 'Value Pack', 'Bulk',
];

const WAREHOUSES = ['Main Warehouse - A', 'Cold Storage - B', 'Dry Storage - C', 'Distribution Center - D', 'Processing Facility - E'];

const SKUS = ['SKU-MSH-001', 'SKU-SHT-002', 'SKU-OYS-003', 'SKU-ENO-004', 'SKU-KOY-005', 'SKU-MAI-006', 'SKU-LIO-007', 'SKU-CHA-008', 'SKU-POR-009', 'SKU-CRE-010'];

const QUALITY_STATUSES = ['pending_inspection', 'under_review', 'approved', 'rejected', 'blocked', 'quarantined', 'returned', 'disposed'] as const;
const USERS = ['john@sporekart.com', 'jane@sporekart.com', 'quality@sporekart.com', 'warehouse@sporekart.com', 'admin@sporekart.com', 'inventory@sporekart.com'];

function randomItem<T>(arr: readonly T[]): T {
  return arr[Math.floor(Math.random() * arr.length)];
}

function randomInt(min: number, max: number): number {
  return Math.floor(Math.random() * (max - min + 1)) + min;
}

function randomDate(daysAgo: number, rangeDays: number): string {
  const d = new Date();
  d.setDate(d.getDate() - randomInt(0, daysAgo) + randomInt(0, rangeDays));
  return d.toISOString();
}

export function generateMockBatchRecords(count: number = 45): BatchRecord[] {
  const batches: BatchRecord[] = [];
  for (let i = 1; i <= count; i++) {
    const index = (i - 1) % PRODUCTS.length;
    const product = PRODUCTS[index];
    const variant = VARIANTS[index];
    const sku = SKUS[index];
    const warehouse = randomItem(WAREHOUSES);
    const prodDate = new Date();
    prodDate.setDate(prodDate.getDate() - randomInt(10, 365));
    const shelfLifeUnit = randomItem(['days', 'weeks', 'months', 'years'] as const);
    let shelfLife: number;
    switch (shelfLifeUnit) {
      case 'days': shelfLife = randomInt(7, 90); break;
      case 'weeks': shelfLife = randomInt(2, 52); break;
      case 'months': shelfLife = randomInt(1, 24); break;
      case 'years': shelfLife = randomInt(1, 5); break;
      default: shelfLife = randomInt(30, 365);
    }
    let shelfLifeDays: number;
    switch (shelfLifeUnit) {
      case 'days': shelfLifeDays = shelfLife; break;
      case 'weeks': shelfLifeDays = shelfLife * 7; break;
      case 'months': shelfLifeDays = shelfLife * 30; break;
      case 'years': shelfLifeDays = shelfLife * 365; break;
      default: shelfLifeDays = shelfLife;
    }
    const expiryDate = new Date(prodDate.getTime() + shelfLifeDays * 86400000);
    const now = new Date();
    const remainingDays = Math.ceil((expiryDate.getTime() - now.getTime()) / 86400000);
    let expiryStatus: string;
    if (remainingDays <= 0) expiryStatus = 'expired';
    else if (remainingDays <= 7) expiryStatus = 'critical';
    else if (remainingDays <= 30) expiryStatus = 'near_expiry';
    else if (remainingDays <= 60) expiryStatus = 'monitor';
    else if (remainingDays <= 90) expiryStatus = 'healthy';
    else expiryStatus = 'fresh';

    const status = remainingDays <= 0 ? 'expired' : remainingDays <= 30 ? 'near_expiry' : randomItem(['created', 'quality_review', 'approved', 'operational', 'blocked', 'archived'] as const);

    const batch: BatchRecord = {
      id: `BCH-${String(i).padStart(3, '0')}`,
      batchCode: `BATCH-${String(i).padStart(4, '0')}`,
      inventoryItemId: `ITEM-${String(index + 1).padStart(3, '0')}`,
      product,
      variant,
      sku,
      warehouseId: `WH-${(WAREHOUSES.indexOf(warehouse) + 1).toString().padStart(2, '0')}`,
      warehouse,
      productionDate: prodDate.toISOString(),
      expiryDate: expiryDate.toISOString(),
      bestBeforeDate: new Date(expiryDate.getTime() - 7 * 86400000).toISOString(),
      manufacturingDate: prodDate.toISOString(),
      receivedDate: new Date(prodDate.getTime() + randomInt(1, 5) * 86400000).toISOString(),
      shelfLife,
      shelfLifeUnit,
      status: status as BatchRecord['status'],
      qualityStatus: randomItem(QUALITY_STATUSES.slice(0, 4)),
      expiryStatus: expiryStatus as BatchRecord['expiryStatus'],
      storageConditions: {
        temperature: `${randomInt(2, 8)}°C`,
        humidity: `${randomInt(60, 90)}%`,
      },
      quantity: randomInt(100, 10000),
      lotCount: randomInt(1, 8),
      createdBy: randomItem(USERS),
      createdAt: randomDate(365, 0),
      updatedAt: randomDate(30, 0),
    };
    batches.push(batch);
  }
  return batches;
}

export function generateMockLots(batches: BatchRecord[]): LotRecord[] {
  const lots: LotRecord[] = [];
  for (const batch of batches) {
    const count = batch.lotCount;
    for (let j = 1; j <= count; j++) {
      const lot: LotRecord = {
        id: `LOT-${batch.id.substring(4)}-${String(j).padStart(2, '0')}`,
        lotCode: `LOT-${batch.batchCode.substring(6)}-${String(j).padStart(3, '0')}`,
        batchId: batch.id,
        batchCode: batch.batchCode,
        inventoryItemId: batch.inventoryItemId,
        product: batch.product,
        warehouseId: batch.warehouseId,
        warehouse: batch.warehouse,
        quantity: Math.round(batch.quantity / count),
        status: batch.status,
        createdAt: batch.createdAt,
        updatedAt: batch.updatedAt,
      };
      lots.push(lot);
    }
  }
  return lots;
}

export function generateMockTimeline(batches: BatchRecord[]): BatchTimelineEvent[] {
  const events: BatchTimelineEvent[] = [];
  for (const batch of batches) {
    let seq = 0;
    const nextId = () => { seq++; return `EVT-${batch.id}-${String(seq).padStart(3, '0')}`; };

    events.push({
      id: nextId(),
      batchId: batch.id,
      batchCode: batch.batchCode,
      event: 'Batch Created',
      toStatus: 'created',
      user: batch.createdBy,
      timestamp: batch.createdAt,
      note: 'Batch record initialized.',
    });

    if (batch.status !== 'created') {
      events.push({
        id: nextId(),
        batchId: batch.id,
        batchCode: batch.batchCode,
        event: 'Submitted for Quality Review',
        fromStatus: 'created',
        toStatus: 'quality_review',
        user: randomItem(USERS),
        timestamp: new Date(new Date(batch.createdAt).getTime() + 86400000).toISOString(),
        note: 'Quality review requested.',
      });
    }

    if (batch.status === 'approved' || batch.status === 'operational' || batch.status === 'near_expiry' || batch.status === 'expired') {
      events.push({
        id: nextId(),
        batchId: batch.id,
        batchCode: batch.batchCode,
        event: 'Quality Approved',
        fromStatus: 'quality_review',
        toStatus: 'approved',
        user: 'quality@sporekart.com',
        timestamp: new Date(new Date(batch.createdAt).getTime() + 2 * 86400000).toISOString(),
        note: 'All quality checks passed.',
      });
    }

    if (batch.status === 'operational' || batch.status === 'near_expiry' || batch.status === 'expired') {
      events.push({
        id: nextId(),
        batchId: batch.id,
        batchCode: batch.batchCode,
        event: 'Batch Activated',
        fromStatus: 'approved',
        toStatus: 'operational',
        user: randomItem(USERS),
        timestamp: new Date(new Date(batch.createdAt).getTime() + 3 * 86400000).toISOString(),
      });

      if (batch.status === 'near_expiry' || batch.status === 'expired') {
        events.push({
          id: nextId(),
          batchId: batch.id,
          batchCode: batch.batchCode,
          event: 'Near Expiry Warning',
          fromStatus: 'operational',
          toStatus: 'near_expiry',
          user: 'system@sporekart.com',
          timestamp: new Date(new Date(batch.expiryDate).getTime() - 30 * 86400000).toISOString(),
          note: 'Batch approaching expiry date.',
        });
      }

      if (batch.status === 'expired') {
        events.push({
          id: nextId(),
          batchId: batch.id,
          batchCode: batch.batchCode,
          event: 'Batch Expired',
          fromStatus: 'near_expiry',
          toStatus: 'expired',
          user: 'system@sporekart.com',
          timestamp: batch.expiryDate,
          note: 'Batch has passed expiry date.',
        });
      }
    }

    if (batch.status === 'rejected') {
      events.push({
        id: nextId(),
        batchId: batch.id,
        batchCode: batch.batchCode,
        event: 'Batch Rejected',
        fromStatus: 'quality_review',
        toStatus: 'rejected',
        user: 'quality@sporekart.com',
        timestamp: new Date(new Date(batch.createdAt).getTime() + 2 * 86400000).toISOString(),
        note: 'Failed quality inspection.',
      });
    }

    if (batch.status === 'blocked') {
      events.push({
        id: nextId(),
        batchId: batch.id,
        batchCode: batch.batchCode,
        event: 'Batch Blocked',
        fromStatus: 'operational',
        toStatus: 'blocked',
        user: randomItem(USERS),
        timestamp: new Date(new Date(batch.createdAt).getTime() + randomInt(10, 60) * 86400000).toISOString(),
        note: 'Blocked for compliance review.',
      });
    }

    if (batch.status === 'archived') {
      events.push({
        id: nextId(),
        batchId: batch.id,
        batchCode: batch.batchCode,
        event: 'Batch Archived',
        fromStatus: 'expired',
        toStatus: 'archived',
        user: randomItem(USERS),
        timestamp: new Date(new Date(batch.expiryDate).getTime() + 30 * 86400000).toISOString(),
        note: 'Batch archived after expiry.',
      });
    }
  }
  return events;
}

function generateAnalyticsGeneric(): AnalyticsMetric {
  const productDistribution = PRODUCTS.map((p) => ({ product: p, count: randomInt(1, 10) }));
  const qualitySummary = [
    { status: 'pending_inspection' as const, count: randomInt(1, 5) },
    { status: 'under_review' as const, count: randomInt(1, 3) },
    { status: 'approved' as const, count: randomInt(20, 35) },
    { status: 'rejected' as const, count: randomInt(1, 5) },
    { status: 'blocked' as const, count: randomInt(0, 3) },
    { status: 'quarantined' as const, count: randomInt(0, 2) },
    { status: 'returned' as const, count: randomInt(0, 2) },
    { status: 'disposed' as const, count: randomInt(0, 2) },
  ];
  return {
    totalBatches: 45,
    totalLots: 120,
    nearExpiry: 8,
    expired: 3,
    approved: 32,
    rejected: 4,
    pendingReview: 6,
    warehouseDistribution: WAREHOUSES.map((w) => ({ warehouse: w, count: randomInt(3, 15) })),
    productDistribution,
    qualitySummary,
  };
}

export const MOCK_BATCHES = generateMockBatchRecords(45);
export const MOCK_LOTS = generateMockLots(MOCK_BATCHES);
export const MOCK_TIMELINE = generateMockTimeline(MOCK_BATCHES);
export const MOCK_ANALYTICS = generateAnalyticsGeneric();

