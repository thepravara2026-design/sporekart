export type WarehouseType =
  | 'main'
  | 'distribution'
  | 'retail'
  | 'cold_storage'
  | 'processing'
  | 'spawn_production'
  | 'fresh_mushroom'
  | 'dry_mushroom'
  | 'virtual'
  | 'transit'
  | 'custom';

export type WarehouseStatus =
  | 'active'
  | 'inactive'
  | 'maintenance'
  | 'planned'
  | 'decommissioned';

export type TemperatureType = 'ambient' | 'cold' | 'frozen' | 'controlled';

export type ZoneType =
  | 'receiving'
  | 'storage'
  | 'packing'
  | 'dispatch'
  | 'returns'
  | 'qc'
  | 'damaged'
  | 'cold'
  | 'quarantine'
  | 'custom';

export type StorageLevel = 'warehouse' | 'building' | 'floor' | 'zone' | 'rack' | 'shelf' | 'bin';

export type StorageNodeStatus = 'active' | 'inactive' | 'maintenance' | 'planned' | 'reserved';

export type WarehouseRole =
  | 'viewer'
  | 'warehouse_operator'
  | 'warehouse_manager'
  | 'inventory_manager'
  | 'administrator';

export type WarehousePermission =
  | 'view'
  | 'create'
  | 'edit'
  | 'archive'
  | 'restore'
  | 'settings'
  | 'reports'
  | 'analytics'
  | 'operations_future';

export interface Warehouse {
  id: string;
  code: string;
  name: string;
  type: WarehouseType;
  location: string;
  status: WarehouseStatus;
  capacity: number;
  used: number;
  utilization: number;
  manager: string;
  address: string;
  contact: string;
  operatingHours: string;
  temperatureType: TemperatureType;
  notes: string;
  createdAt: string;
  updatedAt: string;
}

export interface Zone {
  id: string;
  code: string;
  name: string;
  type: ZoneType;
  warehouseId: string;
  status: StorageNodeStatus;
  capacity: number;
}

export interface Rack {
  id: string;
  code: string;
  type: string;
  maxCapacity: number;
  status: StorageNodeStatus;
  zoneId: string;
}

export interface Shelf {
  id: string;
  code: string;
  level: number;
  capacity: number;
  weightLimit: number;
  status: StorageNodeStatus;
  rackId: string;
}

export interface Bin {
  id: string;
  code: string;
  type: string;
  capacity: number;
  availability: 'available' | 'reserved' | 'full';
  status: StorageNodeStatus;
  shelfId: string;
}

export interface StorageNode {
  id: string;
  parentId: string | null;
  level: StorageLevel;
  code: string;
  name: string;
  status: StorageNodeStatus;
  description?: string;
  capacity?: number;
}

export interface WarehouseSection {
  id: string;
  label: string;
  icon: string;
  description?: string;
  href?: string;
}

export interface WarehouseMetric {
  id: string;
  title: string;
  value: string;
  trend: 'up' | 'down' | 'flat';
  percentage: number;
  comparison: string;
  icon: string;
  color: string;
}

export interface HealthMetric {
  id: string;
  label: string;
  score: number;
}

export interface StatusCount {
  id: string;
  label: string;
  count: number;
  variant: 'success' | 'warning' | 'danger' | 'info' | 'neutral' | 'default';
}

export interface QuickAction {
  id: string;
  label: string;
  icon: string;
  href: string;
  permission?: WarehousePermission;
}

export interface RecentActivity {
  id: string;
  icon: string;
  text: string;
  timestamp: string;
}

export interface WarehouseFilterState {
  type: string[];
  location: string[];
  status: string[];
  temperatureType: string[];
  zone: string[];
  capacity: string[];
  createdDate?: string;
  updatedDate?: string;
}

export type WarehouseSortKey = 'name' | 'code' | 'capacity' | 'used' | 'utilization' | 'updatedAt';
export type SortDir = 'asc' | 'desc';

export interface WarehouseSearchField {
  id: string;
  label: string;
  placeholder?: boolean;
}

export interface WarehouseFilterOption {
  id: string;
  label: string;
  options: { value: string; label: string }[];
  multi?: boolean;
  placeholder?: boolean;
}

export interface SettingsSection {
  id: string;
  label: string;
  description: string;
  icon: string;
  placeholder?: boolean;
}

export interface EmptyStateConfig {
  key: string;
  title: string;
  message: string;
  icon: string;
  actionLabel?: string;
}


