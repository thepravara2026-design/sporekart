import type { InventoryStatus } from '../inventory/types';

export type ClassificationType =
  | 'raw_material'
  | 'work_in_progress'
  | 'finished_good'
  | 'consumable'
  | 'asset'
  | 'packaging'
  | 'supplies';

export type ClassificationGrade = 'a_plus' | 'a' | 'b' | 'c' | 'unclassified';

export interface Classification {
  type: ClassificationType;
  category: string;
  subcategory: string;
  class: string;
  grade: ClassificationGrade;
}

export type LifecycleStage =
  | 'draft'
  | 'pending_approval'
  | 'approved'
  | 'active'
  | 'frozen'
  | 'suspended'
  | 'discontinued'
  | 'archived';

export interface LifecycleEvent {
  id: string;
  stage: LifecycleStage;
  timestamp: string;
  user: string;
  note?: string;
}

export type InventoryItemRole =
  | 'viewer'
  | 'inventory_operator'
  | 'inventory_manager'
  | 'warehouse_manager'
  | 'administrator';

export type InventoryItemPermission =
  | 'view'
  | 'create'
  | 'edit'
  | 'archive'
  | 'restore'
  | 'bulk_operations'
  | 'reports'
  | 'settings';

export interface InventoryItemRecord {
  id: string;
  code: string;
  productId: string;
  productName: string;
  variantId: string;
  variantName: string;
  sku: string;
  name: string;
  description?: string;
  status: InventoryStatus;
  classification: Classification;
  lifecycle: LifecycleStage;
  lifecycleEvents: LifecycleEvent[];
  unit: string;
  brand: string;
  category: string;
  inventoryId: string;
  productImage?: string;
  createdAt: string;
  updatedAt: string;
}

export interface ProductMapping {
  id: string;
  productId: string;
  productName: string;
  productCode: string;
  productType: string;
  inventoryCount: number;
  status: InventoryStatus;
  mappedAt: string;
}

export interface VariantMapping {
  id: string;
  variantId: string;
  variantName: string;
  productId: string;
  productName: string;
  skuCount: number;
  inventoryCount: number;
  status: InventoryStatus;
  mappedAt: string;
}

export interface SKUMapping {
  id: string;
  sku: string;
  inventoryItemId: string;
  inventoryItemName: string;
  variantId: string;
  variantName: string;
  productId: string;
  productName: string;
  status: InventoryStatus;
  mappedAt: string;
}

export interface UnitConfig {
  id: string;
  name: string;
  symbol: string;
  category: 'weight' | 'volume' | 'quantity' | 'length' | 'custom';
  baseUnit?: string;
  conversionFactor?: number;
}

export interface InventoryItemSection {
  id: string;
  label: string;
  icon: string;
  description?: string;
  href?: string;
}

export interface InventoryItemMetric {
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
  permission?: InventoryItemPermission;
}

export interface RecentActivity {
  id: string;
  icon: string;
  text: string;
  timestamp: string;
}

export interface InventoryItemFilterState {
  status: string[];
  classification: string[];
  lifecycle: string[];
  category: string[];
  brand: string[];
  unit: string[];
  grade: string[];
  savedFilters: string[];
  updatedDate?: string;
  createdDate?: string;
}

export type InventoryItemSortKey = 'code' | 'name' | 'productName' | 'sku' | 'category' | 'status' | 'lifecycle' | 'updatedAt';

export type SortDir = 'asc' | 'desc';

export interface InventoryItemSearchField {
  id: string;
  label: string;
  placeholder?: boolean;
}

export interface InventoryItemFilterOption {
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
