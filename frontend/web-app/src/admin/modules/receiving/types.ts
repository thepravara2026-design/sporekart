export type ReceiptStatus =
  | 'draft' | 'pending' | 'receiving' | 'inspection'
  | 'approved' | 'rejected' | 'completed' | 'cancelled'
  | 'warehouse_allocation_pending' | 'batch_pending';

export type InspectionStatus =
  | 'pending' | 'visual' | 'quality' | 'packaging'
  | 'quantity_verification' | 'documentation' | 'temperature_check'
  | 'humidity_check' | 'laboratory_test' | 'passed' | 'failed';

export type AcceptanceStatus =
  | 'fully_accepted' | 'partially_accepted' | 'conditionally_accepted'
  | 'accepted_with_notes' | 'awaiting_approval' | 'not_yet_accepted';

export type RejectionReason =
  | 'fully_rejected' | 'partially_rejected' | 'damaged_goods'
  | 'expired_goods' | 'packaging_failure' | 'quality_failure'
  | 'wrong_item' | 'wrong_quantity' | 'missing_documents';

export type AllocationStatus =
  | 'pending' | 'allocating' | 'allocated' | 'failed' | 'not_required';

export type ReceivingRole =
  | 'viewer' | 'receiving_operator' | 'warehouse_operator'
  | 'quality_inspector' | 'warehouse_manager' | 'inventory_manager' | 'administrator';

export type ReceivingPermission =
  | 'view' | 'create' | 'inspect' | 'approve' | 'reject' | 'archive'
  | 'reports' | 'analytics' | 'audit';

export interface ReceivingSection {
  id: string;
  label: string;
  icon: string;
  description: string;
}

export interface GoodsReceiptRecord {
  id: string;
  receiptNumber: string;
  referenceNumber: string;
  supplier: string;
  warehouse: string;
  receivingZone: string;
  inventoryItemId: string;
  product: string;
  variant: string;
  batchCode: string;
  quantity: number;
  acceptedQty: number;
  rejectedQty: number;
  inspectionQty: number;
  receiptStatus: ReceiptStatus;
  inspectionStatus: InspectionStatus;
  acceptanceStatus: AcceptanceStatus;
  rejectionReason: RejectionReason;
  allocationStatus: AllocationStatus;
  batchAssigned: boolean;
  notes: string;
  createdBy: string;
  createdAt: string;
  updatedAt: string;
}

export interface InspectionRecord {
  id: string;
  receiptId: string;
  receiptNumber: string;
  product: string;
  warehouse: string;
  inspector: string;
  type: 'visual' | 'quality' | 'packaging' | 'quantity' | 'documentation' | 'temperature' | 'humidity' | 'laboratory';
  status: InspectionStatus;
  packagingQuality: boolean;
  productQuality: boolean;
  quantityMatch: boolean;
  labelVerified: boolean;
  expiryVerified: boolean;
  batchVerified: boolean;
  damageDetected: boolean;
  storageCompliant: boolean;
  certified: boolean;
  notes: string;
  startedAt: string;
  completedAt: string;
}

export interface ReceivingTimelineEvent {
  id: string;
  receiptId: string;
  event: string;
  fromStatus: string;
  toStatus: string;
  user: string;
  timestamp: string;
  note: string;
}

export interface AuditRecord {
  id: string;
  receiptId: string;
  action: string;
  field: string;
  oldValue: string;
  newValue: string;
  user: string;
  timestamp: string;
}

export interface ReceivingMetric {
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

export interface ReceivingFilterState {
  warehouse: string[];
  receiptStatus: string[];
  inspectionStatus: string[];
  acceptanceStatus: string[];
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
  totalReceipts: number;
  pendingReceipts: number;
  completedReceipts: number;
  rejectedReceipts: number;
  underInspection: number;
  awaitingApproval: number;
  acceptedGoods: number;
  rejectedGoods: number;
  warehousePending: number;
  batchPending: number;
  acceptanceRate: number;
  rejectionRate: number;
  warehouseDistribution: { warehouse: string; count: number }[];
}

export interface AllocationRecord {
  id: string;
  receiptId: string;
  warehouse: string;
  zone: string;
  rack: string;
  shelf: string;
  bin: string;
  storageType: 'ambient' | 'cold_storage' | 'dry' | 'hazardous';
  status: AllocationStatus;
  allocatedAt: string;
}

export interface BatchAssignmentRecord {
  id: string;
  receiptId: string;
  batchCode: string;
  lotCode: string;
  expiryDate: string;
  shelfLife: string;
  qualityStatus: string;
  assignedAt: string;
}

export interface ValidationRule {
  id: string;
  name: string;
  status: 'passed' | 'warning' | 'failed';
  description: string;
}
