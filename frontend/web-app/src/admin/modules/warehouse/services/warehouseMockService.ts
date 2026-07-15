import type {
  Warehouse,
  Zone,
  StorageNode,
  RecentActivity,
  WarehouseMetric,
  HealthMetric,
  StatusCount,
  WarehouseType,
  WarehouseStatus,
  TemperatureType,
} from '../types';
import { WAREHOUSE_MOCK_METRICS, WAREHOUSE_HEALTH, WAREHOUSE_STATUS_COUNTS, WAREHOUSE_RECENT_ACTIVITIES } from '../constants';

const TYPES: WarehouseType[] = ['main', 'distribution', 'retail', 'cold_storage', 'processing', 'spawn_production', 'fresh_mushroom', 'dry_mushroom', 'virtual', 'transit', 'custom'];
const STATUSES: WarehouseStatus[] = ['active', 'inactive', 'maintenance', 'planned', 'decommissioned'];
const TEMPS: TemperatureType[] = ['ambient', 'cold', 'frozen', 'controlled'];
const CITIES = ['Mumbai', 'Delhi', 'Pune', 'Bengaluru'];

function seed(n: number): number {
  const x = Math.sin(n * 12.9898) * 43758.5453;
  return x - Math.floor(x);
}

function generateWarehouses(count = 12): Warehouse[] {
  return Array.from({ length: count }, (_, i) => {
    const r = seed(i + 1);
    const capacity = 50000 + Math.floor(r * 150000);
    const used = Math.floor(capacity * (0.4 + seed(i + 9) * 0.55));
    const idNum = i + 1;
    return {
      id: `WH-${idNum}`,
      code: `WH${String(1000 + i).slice(1)}`,
      name: `${CITIES[i % CITIES.length]} ${TYPES[i % TYPES.length].replace('_', ' ').replace(/\b\w/g, (c) => c.toUpperCase())} ${idNum}`,
      type: TYPES[i % TYPES.length],
      location: CITIES[i % CITIES.length],
      status: STATUSES[i % STATUSES.length],
      capacity,
      used,
      utilization: Math.round((used / capacity) * 100),
      manager: ['A. Rao', 'S. Mehta', 'K. Nair', 'P. Iyer'][i % 4],
      address: `${100 + i}, Industrial Area, ${CITIES[i % CITIES.length]}`,
      contact: `+91 98${String(10000000 + i * 7).slice(0, 8)}`,
      operatingHours: '08:00 – 20:00',
      temperatureType: TEMPS[i % TEMPS.length],
      notes: 'Mock warehouse record.',
      createdAt: `2026-0${(i % 9) + 1}-15`,
      updatedAt: `2026-0${(i % 9) + 1}-${(i % 27) + 1}`,
    };
  });
}

function generateZones(warehouses: Warehouse[]): Zone[] {
  const zoneTypes = ['receiving', 'storage', 'packing', 'dispatch', 'returns', 'qc', 'damaged', 'cold', 'quarantine'] as const;
  const zones: Zone[] = [];
  warehouses.slice(0, 4).forEach((w, wi) => {
    zoneTypes.forEach((zt, zi) => {
      zones.push({
        id: `Z-${w.id}-${zi + 1}`,
        code: `${w.code}-Z${zi + 1}`,
        name: `${zt.replace(/\b\w/g, (c) => c.toUpperCase())} Zone ${zi + 1}`,
        type: zt,
        warehouseId: w.id,
        status: wi % 3 === 0 ? 'active' : 'planned',
        capacity: 200 + zi * 50,
      });
    });
  });
  return zones;
}

function generateStorageNodes(): StorageNode[] {
  const nodes: StorageNode[] = [];
  const root: StorageNode = { id: 'SN-WH1', parentId: null, level: 'warehouse', code: 'WH1', name: 'Mumbai Main Warehouse', status: 'active', description: 'Root warehouse node', capacity: 200000 };
  nodes.push(root);
  for (let b = 1; b <= 2; b++) {
    const bid = `SN-B${b}`;
    nodes.push({ id: bid, parentId: root.id, level: 'building', code: `B${b}`, name: `Building ${b}`, status: 'active', capacity: 100000 });
    for (let f = 1; f <= 2; f++) {
      const fid = `SN-B${b}-F${f}`;
      nodes.push({ id: fid, parentId: bid, level: 'floor', code: `F${f}`, name: `Floor ${f}`, status: 'active', capacity: 50000 });
      for (let z = 1; z <= 2; z++) {
        const zid = `SN-B${b}-F${f}-Z${z}`;
        nodes.push({ id: zid, parentId: fid, level: 'zone', code: `Z${z}`, name: `Zone ${z}`, status: 'active', capacity: 25000 });
        for (let r = 1; r <= 2; r++) {
          const rid = `SN-B${b}-F${f}-Z${z}-R${r}`;
          nodes.push({ id: rid, parentId: zid, level: 'rack', code: `R${r}`, name: `Rack ${r}`, status: 'active', capacity: 5000 });
          for (let s = 1; s <= 2; s++) {
            const sid = `SN-B${b}-F${f}-Z${z}-R${r}-S${s}`;
            nodes.push({ id: sid, parentId: rid, level: 'shelf', code: `S${s}`, name: `Shelf ${s}`, status: 'active', capacity: 1000 });
            for (let n = 1; n <= 2; n++) {
              nodes.push({ id: `${sid}-BIN${n}`, parentId: sid, level: 'bin', code: `BIN${n}`, name: `Bin ${n}`, status: 'active', capacity: 200 });
            }
          }
        }
      }
    }
  }
  return nodes;
}

let WAREHOUSES: Warehouse[] = generateWarehouses();
let ZONES: Zone[] = generateZones(WAREHOUSES);
const STORAGE_NODES: StorageNode[] = generateStorageNodes();

function delay<T>(data: T, ms = 500): Promise<T> {
  return new Promise((resolve) => setTimeout(() => resolve(data), ms));
}

export interface WarehouseInput {
  name: string;
  code: string;
  type: WarehouseType;
  temperatureType: TemperatureType;
  status: WarehouseStatus;
  region: string;
  city: string;
  country: string;
}

export const warehouseMockService = {
  fetchWarehouses: (): Promise<Warehouse[]> => delay([...WAREHOUSES]),
  fetchWarehouseDashboard: (): Promise<{ metrics: WarehouseMetric[]; health: HealthMetric[]; statusCounts: StatusCount[] }> =>
    delay({ metrics: WAREHOUSE_MOCK_METRICS, health: WAREHOUSE_HEALTH, statusCounts: WAREHOUSE_STATUS_COUNTS }),
  fetchRecentActivities: (): Promise<RecentActivity[]> => delay(WAREHOUSE_RECENT_ACTIVITIES),
  fetchZones: (): Promise<Zone[]> => delay([...ZONES]),
  fetchStorageNodes: (): Promise<StorageNode[]> => delay([...STORAGE_NODES]),

  createWarehouse: (input: WarehouseInput): Promise<Warehouse> => {
    const idNum = WAREHOUSES.length + 1;
    const warehouse: Warehouse = {
      id: `WH-${idNum}`,
      code: input.code || `WH${String(1000 + idNum).slice(1)}`,
      name: input.name,
      type: input.type,
      location: [input.city, input.country].filter(Boolean).join(', '),
      status: input.status,
      capacity: 50000,
      used: 0,
      utilization: 0,
      manager: 'Unassigned',
      address: '',
      contact: '',
      operatingHours: '08:00 – 20:00',
      temperatureType: input.temperatureType,
      notes: '',
      createdAt: '2026-07-15',
      updatedAt: '2026-07-15',
    };
    WAREHOUSES = [warehouse, ...WAREHOUSES];
    return delay(warehouse);
  },

  updateWarehouse: (id: string, patch: Partial<Warehouse>): Promise<Warehouse> => {
    WAREHOUSES = WAREHOUSES.map((w) => (w.id === id ? { ...w, ...patch, updatedAt: '2026-07-15' } : w));
    const updated = WAREHOUSES.find((w) => w.id === id);
    return delay(updated as Warehouse);
  },

  archiveWarehouse: (id: string): Promise<Warehouse> => {
    WAREHOUSES = WAREHOUSES.map((w) => (w.id === id ? { ...w, status: 'decommissioned', updatedAt: '2026-07-15' } : w));
    const updated = WAREHOUSES.find((w) => w.id === id);
    return delay(updated as Warehouse);
  },
};


