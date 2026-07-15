import type { InventoryStatus } from '../inventory/types';

export type StockState =
  | 'available'
  | 'reserved'
  | 'incoming'
  | 'allocated'
  | 'damaged'
  | 'expired'
  | 'blocked'
  | 'quarantine'
  | 'inspection'
  | 'returned'
  | 'lost'
  | 'adjustment_pending'
  | 'future_manufacturing'
  | 'future_transit'
  | 'future_consignment';

export type StockHealth =
  | 'healthy'
  | 'low'
  | 'critical'
  | 'out_of_stock'
  | 'overstock'
  | 'needs_inspection'
  | 'near_expiry'
  | 'damaged';

export type AvailabilityLevel =
  | 'available'
  | 'limited'
  | 'unavailable'
  | 'pre_order'
  | 'backorder';

export type ReservationType =
  | 'reserved_for_orders'
  | 'reserved_for_transfers'
  | 'reserved_for_production'
  | 'reserved_for_qc'
  | 'reserved_for_returns';

export interface Reservation {
  id: string;
  type: ReservationType;
  quantity: number;
  reference?: string;
  createdAt: string;
  expiresAt?: string;
  notes?: string;
}

export interface StockTimelineEvent {
  id: string;
  action: string;
  fromState?: StockState;
  toState: StockState;
  quantity: number;
  timestamp: string;
  user: string;
  note?: string;
}

export interface StockRecord {
  id: string;
  code: string;
  inventoryItemId: string;
  inventoryItemName: string;
  productName: string;
  variantName: string;
  sku: string;
  warehouseId: string;
  warehouseName: string;
  quantities: Record<StockState, number>;
  health: StockHealth;
  healthScore: number;
  availability: AvailabilityLevel;
  reservations: Reservation[];
  timeline: StockTimelineEvent[];
  reorderPoint: number;
  safetyStock: number;
  minStock: number;
  maxStock: number;
  status: InventoryStatus;
  createdAt: string;
  updatedAt: string;
}

export type StockRole =
  | 'viewer'
  | 'inventory_operator'
  | 'warehouse_operator'
  | 'inventory_manager'
  | 'administrator';

export type StockPermission =
  | 'view'
  | 'edit'
  | 'archive'
  | 'reports'
  | 'validation'
  | 'settings'
  | 'future_transactions';

export interface StockSection {
  id: string;
  label: string;
  icon: string;
  description?: string;
  href?: string;
}

export interface StockMetric {
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
  permission?: StockPermission;
}

export interface RecentActivity {
  id: string;
  icon: string;
  text: string;
  timestamp: string;
}

export interface StockFilterState {
  warehouse: string[];
  stockState: string[];
  health: string[];
  availability: string[];
  category: string[];
  brand: string[];
  status: string[];
  savedFilters: string[];
  updatedDate?: string;
  createdDate?: string;
}

export type StockSortKey = 'code' | 'inventoryItemName' | 'sku' | 'warehouseName' | 'health' | 'status' | 'updatedAt';

export type SortDir = 'asc' | 'desc';

export interface StockSearchField {
  id: string;
  label: string;
  placeholder?: boolean;
}

export interface StockFilterOption {
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
