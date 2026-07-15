import type { StockRecord, StockTimelineEvent, Reservation, StockMetric, RecentActivity, StockState } from '../types';
import { getItems } from '../../inventory-items/services/inventoryItemMockService';
import { WAREHOUSE_NAMES, STOCK_MOCK_METRICS, STOCK_RECENT_ACTIVITIES } from '../constants';

function pad(n: number, width: number = 4): string { return String(n).padStart(width, '0'); }

function dateSub(days: number): string {
  const d = new Date(); d.setDate(d.getDate() - days); return d.toISOString();
}

function randomItem<T>(arr: T[]): T { return arr[Math.floor(Math.random() * arr.length)]; }

function randInt(min: number, max: number): number {
  return Math.floor(Math.random() * (max - min + 1)) + min;
}

function generateTimeline(recordId: string): StockTimelineEvent[] {
  const events: StockTimelineEvent[] = [];
  const now = Date.now();
  let offset = 0;
  const actions = ['Stock Record Created', 'Quantity Updated', 'State Changed', 'Health Recalculated', 'Reservation Added'];
  const states: StockState[] = ['available', 'available', 'reserved', 'incoming', 'available', 'allocated'];

  for (let i = 0; i < 4; i++) {
    offset += Math.floor(Math.random() * 7 * 86400000) + 86400000;
    events.push({
      id: `ste-${recordId}-${i}`,
      action: randomItem(actions),
      toState: randomItem([...states]) as StockTimelineEvent['toState'],
      quantity: randInt(10, 500),
      timestamp: new Date(now - offset).toISOString(),
      user: randomItem(['Admin User', 'Inventory Manager', 'System', 'Warehouse Operator']),
      note: `Action performed on stock record.`,
    });
  }
  return events;
}

function generateReservations(recordId: string): Reservation[] {
  const res: Reservation[] = [];
  const types = ['reserved_for_orders', 'reserved_for_transfers', 'reserved_for_production'] as const;
  const count = randInt(0, 3);
  for (let i = 0; i < count; i++) {
    res.push({
      id: `res-${recordId}-${i}`,
      type: randomItem([...types]),
      quantity: randInt(5, 200),
      reference: `REF-${pad(randInt(1000, 9999))}`,
      createdAt: dateSub(randInt(1, 14)),
      expiresAt: dateSub(randInt(-7, 14)),
    });
  }
  return res;
}

let cachedRecords: StockRecord[] | null = null;

export function getStockRecords(): StockRecord[] {
  if (cachedRecords) return cachedRecords;

  const items = getItems();
  const records: StockRecord[] = [];
  let counter = 10000;

  for (let i = 0; i < items.length; i++) {
    const item = items[i];
    const warehouseIdx = i % WAREHOUSE_NAMES.length;
    const warehouseId = `wh-${warehouseIdx + 1}`;
    const available = randInt(0, 2000);
    const reserved = randInt(0, 500);
    const incoming = randInt(0, 300);
    const allocated = randInt(0, 400);
    const damaged = randInt(0, 50);
    const expired = randInt(0, 30);
    const blocked = randInt(0, 20);

    const record: StockRecord = {
      id: `STK-${pad(counter)}`,
      code: `STK-${pad(counter)}`,
      inventoryItemId: item.id,
      inventoryItemName: item.name,
      productName: item.productName,
      variantName: item.variantName,
      sku: item.sku,
      warehouseId,
      warehouseName: WAREHOUSE_NAMES[warehouseIdx],
      quantities: {
        available, reserved, incoming, allocated, damaged, expired, blocked,
        quarantine: randInt(0, 10),
        inspection: randInt(0, 15),
        returned: randInt(0, 20),
        lost: randInt(0, 5),
        adjustment_pending: randInt(0, 10),
        future_manufacturing: randInt(0, 100),
        future_transit: randInt(0, 80),
        future_consignment: randInt(0, 50),
      },
      health: 'healthy',
      healthScore: randInt(60, 100),
      availability: available > 0 ? 'available' : incoming > 0 ? 'pre_order' : 'unavailable',
      reservations: generateReservations(`STK-${pad(counter)}`),
      timeline: generateTimeline(`STK-${pad(counter)}`),
      reorderPoint: randInt(50, 200),
      safetyStock: randInt(20, 100),
      minStock: randInt(10, 50),
      maxStock: randInt(500, 2000),
      status: 'active',
      createdAt: dateSub(randInt(30, 90)),
      updatedAt: dateSub(randInt(0, 14)),
    };

    const total = available + reserved + incoming + allocated + damaged + expired + blocked;
    record.healthScore = total > 0 ? Math.round(((total - damaged - expired - blocked) / total) * 100) : 0;
    record.health =
      total === 0 ? 'out_of_stock' :
      available <= 20 ? 'critical' :
      available <= 100 ? 'low' :
      available >= 1000 ? 'overstock' :
      damaged > 0 || expired > 0 ? 'damaged' : 'healthy';

    records.push(record);
    counter++;
  }

  cachedRecords = records;
  return records;
}

export function getStockMetrics(): StockMetric[] {
  return STOCK_MOCK_METRICS.filter((m) =>
    ['total_records', 'available', 'reserved', 'incoming', 'low_stock', 'out_of_stock'].includes(m.id),
  );
}

export function getStockActivities(): RecentActivity[] {
  return STOCK_RECENT_ACTIVITIES;
}

function getStockRecordById(id: string): StockRecord | undefined {
  return getStockRecords().find((r) => r.id === id || r.code === id);
}



export { getStockRecordById };
