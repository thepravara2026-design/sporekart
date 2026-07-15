export type InventoryStatus =
  | 'active'
  | 'inactive'
  | 'archived'
  | 'pending'
  | 'draft'
  | 'verified'
  | 'future_approval';

export type StockStatus =
  | 'available'
  | 'reserved'
  | 'incoming'
  | 'low'
  | 'out_of_stock'
  | 'damaged'
  | 'expired'
  | 'future';

export type InventoryUnit =
  | 'kg'
  | 'g'
  | 'pieces'
  | 'boxes'
  | 'packets'
  | 'bundles'
  | 'litres'
  | 'millilitres'
  | 'custom';

export type InventoryRole =
  | 'viewer'
  | 'inventory_operator'
  | 'inventory_manager'
  | 'warehouse_manager'
  | 'administrator';

export type InventoryPermission =
  | 'view'
  | 'create'
  | 'edit'
  | 'archive'
  | 'restore'
  | 'settings'
  | 'reports'
  | 'analytics'
  | 'transactions_future';

export interface InventoryItem {
  id: string;
  name: string;
  sku: string;
  warehouse: string;
  category: string;
  brand: string;
  status: InventoryStatus;
  stockStatus: StockStatus;
  stockLevel: number;
  reorderPoint: number;
  unit: InventoryUnit;
  location: string;
  batch?: string;
  inventoryId: string;
  createdAt: string;
  updatedAt: string;
}

export interface Warehouse {
  id: string;
  name: string;
  location: string;
  status: InventoryStatus;
  capacity: number;
  used: number;
}

export interface InventorySection {
  id: string;
  label: string;
  icon: string;
  description?: string;
  href?: string;
}

export interface InventoryMetric {
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

export interface QuickAction {
  id: string;
  label: string;
  icon: string;
  href: string;
  permission?: InventoryPermission;
}

export interface RecentActivity {
  id: string;
  icon: string;
  text: string;
  timestamp: string;
}

export interface StatusCount {
  id: string;
  label: string;
  count: number;
  variant: 'success' | 'warning' | 'danger' | 'info' | 'neutral' | 'default';
}

export interface InventoryFilterOption {
  id: string;
  label: string;
  options: { value: string; label: string }[];
  multi?: boolean;
  placeholder?: boolean;
}

export interface InventoryFilterState {
  warehouse: string[];
  stockStatus: string[];
  inventoryStatus: string[];
  category: string[];
  brand: string[];
  supplier: string[];
  location: string[];
  batchStatus: string[];
  savedFilters: string[];
  updatedDate?: string;
  createdDate?: string;
}

export interface InventorySearchField {
  id: string;
  label: string;
  placeholder?: boolean;
}

export interface InventorySearchState {
  query: string;
  fields: string[];
}

export interface SavedFilter {
  id: string;
  label: string;
  state: Partial<InventoryFilterState>;
}

export interface EmptyStateConfig {
  key: string;
  title: string;
  message: string;
  icon: string;
  actionLabel?: string;
}

export interface SettingsSection {
  id: string;
  label: string;
  description: string;
  icon: string;
  placeholder?: boolean;
}
