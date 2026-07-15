export type MovementType =
  | 'goods_receipt' | 'goods_issue' | 'warehouse_transfer' | 'stock_adjustment'
  | 'damage' | 'return' | 'quality_hold' | 'inspection' | 'reservation' | 'release'
  | 'consumption' | 'production_issue' | 'production_receipt' | 'cycle_count_adjustment'
  | 'manual_adjustment' | 'marketplace_allocation' | 'shipment' | 'manufacturing';

export type TransactionStatus =
  | 'draft' | 'pending' | 'approved' | 'rejected' | 'processing'
  | 'completed' | 'cancelled' | 'archived' | 'failed_validation' | 'blocked';

export type MovementRole = 'viewer' | 'inventory_operator' | 'warehouse_operator' | 'inventory_manager' | 'warehouse_manager' | 'administrator';

export type MovementPermission = 'view' | 'create' | 'approve' | 'reject' | 'archive' | 'restore' | 'reports' | 'analytics' | 'audit';

export interface MovementSection {
  id: string;
  label: string;
  icon: string;
  description: string;
}

export interface TransactionRecord {
  id: string;
  referenceNumber: string;
  movementType: MovementType;
  inventoryItemId: string;
  product: string;
  variant: string;
  sku: string;
  batchCode: string;
  warehouseId: string;
  warehouse: string;
  sourceLocation: string;
  destinationLocation: string;
  status: TransactionStatus;
  quantity: number;
  reasonCode?: string;
  notes?: string;
  requestedBy: string;
  approvedBy?: string;
  createdAt: string;
  updatedAt: string;
}

export interface TransferRecord {
  id: string;
  transferNumber: string;
  sourceWarehouse: string;
  destinationWarehouse: string;
  sourceZone: string;
  destinationZone: string;
  sourceBin: string;
  destinationBin: string;
  inventoryItemId: string;
  product: string;
  quantity: number;
  reason: string;
  status: TransactionStatus;
  createdAt: string;
  updatedAt: string;
}

export interface AdjustmentRecord {
  id: string;
  adjustmentNumber: string;
  type: 'positive' | 'negative' | 'damage' | 'expiry' | 'audit' | 'cycle_count' | 'manual';
  inventoryItemId: string;
  product: string;
  warehouse: string;
  quantity: number;
  reasonCode: string;
  notes: string;
  status: TransactionStatus;
  createdAt: string;
  updatedAt: string;
}

export interface GoodsReceiptRecord {
  id: string;
  receiptNumber: string;
  supplier?: string;
  warehouse: string;
  receivingZone: string;
  inventoryItemId: string;
  product: string;
  acceptedQty: number;
  rejectedQty: number;
  pendingQty: number;
  status: TransactionStatus;
  createdAt: string;
  updatedAt: string;
}

export interface GoodsIssueRecord {
  id: string;
  issueNumber: string;
  warehouse: string;
  destination?: string;
  inventoryItemId: string;
  product: string;
  quantity: number;
  reason: string;
  status: TransactionStatus;
  requestedBy: string;
  createdAt: string;
  updatedAt: string;
}

export interface MovementTimelineEvent {
  id: string;
  transactionId: string;
  event: string;
  fromStatus?: TransactionStatus;
  toStatus: TransactionStatus;
  user: string;
  timestamp: string;
  note?: string;
}

export interface AuditRecord {
  id: string;
  transactionId: string;
  action: string;
  field: string;
  oldValue: string;
  newValue: string;
  user: string;
  timestamp: string;
}

export interface MovementMetric {
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
}

export interface RecentActivity {
  id: string;
  action: string;
  detail: string;
  timestamp: string;
  user: string;
}

export interface MovementFilterState {
  movementType: string[];
  warehouse: string[];
  status: string[];
  dateRange: { start: string; end: string };
  product: string[];
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

export interface AnalyticsData {
  totalTransactions: number;
  transfers: number;
  receipts: number;
  issues: number;
  adjustments: number;
  returns: number;
  damages: number;
  pending: number;
  completed: number;
  warehouseDistribution: { warehouse: string; count: number }[];
}
