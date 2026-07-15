export type BatchLifecycleState = 'created' | 'quality_review' | 'approved' | 'operational' | 'near_expiry' | 'expired' | 'returned' | 'archived' | 'rejected' | 'blocked' | 'recalled' | 'disposed';

export type ExpiryStatus = 'fresh' | 'healthy' | 'monitor' | 'near_expiry' | 'critical' | 'expired' | 'blocked' | 'disposed' | 'recalled';

export type QualityStatus = 'pending_inspection' | 'under_review' | 'approved' | 'rejected' | 'blocked' | 'quarantined' | 'returned' | 'disposed';

export type ShelfLifeUnit = 'days' | 'weeks' | 'months' | 'years' | 'custom';

export type BatchRole = 'viewer' | 'inventory_operator' | 'quality_operator' | 'warehouse_manager' | 'inventory_manager' | 'administrator';

export type BatchPermission = 'view' | 'create' | 'edit' | 'approve' | 'reject' | 'archive' | 'restore' | 'reports' | 'settings' | 'recall';

export interface BatchSection {
  id: string;
  label: string;
  icon: string;
  description: string;
}

export interface BatchRecord {
  id: string;
  batchCode: string;
  inventoryItemId: string;
  product: string;
  variant: string;
  sku: string;
  warehouseId: string;
  warehouse: string;
  productionDate: string;
  expiryDate: string;
  bestBeforeDate?: string;
  manufacturingDate?: string;
  receivedDate?: string;
  shelfLife: number;
  shelfLifeUnit: ShelfLifeUnit;
  status: BatchLifecycleState;
  qualityStatus: QualityStatus;
  expiryStatus: ExpiryStatus;
  storageConditions: { temperature?: string; humidity?: string };
  quantity: number;
  lotCount: number;
  createdBy: string;
  createdAt: string;
  updatedAt: string;
}

export interface LotRecord {
  id: string;
  lotCode: string;
  batchId: string;
  batchCode: string;
  inventoryItemId: string;
  product: string;
  warehouseId: string;
  warehouse: string;
  quantity: number;
  status: string;
  createdAt: string;
  updatedAt: string;
}

export interface BatchTimelineEvent {
  id: string;
  batchId: string;
  batchCode: string;
  event: string;
  fromStatus?: BatchLifecycleState;
  toStatus: BatchLifecycleState;
  user: string;
  timestamp: string;
  note?: string;
}

export interface BatchMetric {
  label: string;
  value: number;
  trend: 'up' | 'down' | 'neutral';
  variant: 'success' | 'warning' | 'danger' | 'info' | 'neutral';
}

export interface QuickAction {
  id: string;
  label: string;
  icon: string;
  description: string;
  action: string;
}

export interface RecentActivity {
  id: string;
  action: string;
  detail: string;
  timestamp: string;
  user: string;
}

export interface BatchFilterOption {
  id: string;
  label: string;
  options: { value: string; label: string }[];
}

export interface BatchSearchField {
  id: string;
  label: string;
  placeholder: string;
}

export interface BatchFilterState {
  warehouse: string[];
  product: string[];
  status: string[];
  qualityStatus: string[];
  expiryStatus: string[];
  dateRange: { start: string; end: string };
  shelfLife: { min: number; max: number };
}

export interface EmptyStateConfig {
  title: string;
  message: string;
  icon: string;
  actionLabel?: string;
}

export interface SettingsSection {
  id: string;
  label: string;
  icon: string;
  description: string;
}

export interface ValidationError {
  field: string;
  message: string;
  severity: 'error' | 'warning' | 'info';
}

export interface AnalyticsMetric {
  totalBatches: number;
  totalLots: number;
  nearExpiry: number;
  expired: number;
  approved: number;
  rejected: number;
  pendingReview: number;
  warehouseDistribution: { warehouse: string; count: number }[];
  productDistribution: { product: string; count: number }[];
  qualitySummary: { status: QualityStatus; count: number }[];
}
