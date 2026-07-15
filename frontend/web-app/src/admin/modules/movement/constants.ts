import type {
  MovementSection, MovementType, TransactionStatus, MovementRole, MovementPermission,
  MovementFilterState, EmptyStateConfig, SettingsSection, MovementMetric, QuickAction, RecentActivity,
} from './types';

export const MOVEMENT_SECTIONS: MovementSection[] = [
  { id: 'overview', label: 'Overview', icon: 'layout', description: 'Movement domain overview and quick access.' },
  { id: 'transactions', label: 'Transactions', icon: 'list', description: 'Inventory transaction registry.' },
  { id: 'movements', label: 'Movements', icon: 'arrow-right', description: 'Movement type registry.' },
  { id: 'transfers', label: 'Transfers', icon: 'truck', description: 'Warehouse transfer requests.' },
  { id: 'adjustments', label: 'Adjustments', icon: 'edit', description: 'Stock adjustments and corrections.' },
  { id: 'goods-receipt', label: 'Goods Receipt', icon: 'arrow-down', description: 'Goods receipt processing.' },
  { id: 'goods-issue', label: 'Goods Issue', icon: 'arrow-up', description: 'Goods issue processing.' },
  { id: 'history', label: 'History', icon: 'clock', description: 'Inventory movement history.' },
  { id: 'validation', label: 'Validation', icon: 'check-square', description: 'Transaction validation and quality checks.' },
  { id: 'analytics', label: 'Analytics', icon: 'bar-chart', description: 'Movement analytics and insights.' },
  { id: 'reports', label: 'Reports', icon: 'file-text', description: 'Movement operational reports.' },
  { id: 'audit', label: 'Audit Trail', icon: 'activity', description: 'Full transaction audit trail.' },
  { id: 'settings', label: 'Settings', icon: 'settings', description: 'Movement preferences and configuration.' },
  { id: 'help', label: 'Help', icon: 'help-circle', description: 'Movement engine documentation and support.' },
];

export const WORKSPACE_SECTIONS = MOVEMENT_SECTIONS;

export const MOVEMENT_TYPES: { value: MovementType; label: string; category: string }[] = [
  { value: 'goods_receipt', label: 'Goods Receipt', category: 'Receipt' },
  { value: 'goods_issue', label: 'Goods Issue', category: 'Issue' },
  { value: 'warehouse_transfer', label: 'Warehouse Transfer', category: 'Transfer' },
  { value: 'stock_adjustment', label: 'Stock Adjustment', category: 'Adjustment' },
  { value: 'damage', label: 'Damage', category: 'Adjustment' },
  { value: 'return', label: 'Return', category: 'Receipt' },
  { value: 'quality_hold', label: 'Quality Hold', category: 'Hold' },
  { value: 'inspection', label: 'Inspection', category: 'Quality' },
  { value: 'reservation', label: 'Reservation', category: 'Allocation' },
  { value: 'release', label: 'Release', category: 'Allocation' },
  { value: 'consumption', label: 'Consumption', category: 'Usage' },
  { value: 'production_issue', label: 'Production Issue', category: 'Manufacturing' },
  { value: 'production_receipt', label: 'Production Receipt', category: 'Manufacturing' },
  { value: 'cycle_count_adjustment', label: 'Cycle Count Adjustment', category: 'Adjustment' },
  { value: 'manual_adjustment', label: 'Manual Adjustment', category: 'Adjustment' },
  { value: 'marketplace_allocation', label: 'Marketplace Allocation', category: 'Allocation' },
  { value: 'shipment', label: 'Shipment', category: 'Shipping' },
  { value: 'manufacturing', label: 'Manufacturing', category: 'Manufacturing' },
];

export const TRANSACTION_STATUSES: { value: TransactionStatus; label: string; variant: 'success' | 'warning' | 'danger' | 'info' | 'neutral' | 'default'; description: string }[] = [
  { value: 'draft', label: 'Draft', variant: 'neutral', description: 'Transaction not yet submitted.' },
  { value: 'pending', label: 'Pending', variant: 'warning', description: 'Awaiting approval.' },
  { value: 'approved', label: 'Approved', variant: 'success', description: 'Transaction approved.' },
  { value: 'rejected', label: 'Rejected', variant: 'danger', description: 'Transaction rejected.' },
  { value: 'processing', label: 'Processing', variant: 'info', description: 'Transaction in progress.' },
  { value: 'completed', label: 'Completed', variant: 'success', description: 'Transaction completed successfully.' },
  { value: 'cancelled', label: 'Cancelled', variant: 'neutral', description: 'Transaction cancelled.' },
  { value: 'archived', label: 'Archived', variant: 'neutral', description: 'Transaction archived.' },
  { value: 'failed_validation', label: 'Failed Validation', variant: 'danger', description: 'Transaction failed validation.' },
  { value: 'blocked', label: 'Blocked', variant: 'danger', description: 'Transaction blocked from processing.' },
];

export const MOVEMENT_ROLE_PERMISSIONS: Record<MovementRole, MovementPermission[]> = {
  viewer: ['view'],
  inventory_operator: ['view', 'create', 'reports'],
  warehouse_operator: ['view', 'create', 'reports'],
  inventory_manager: ['view', 'create', 'approve', 'reject', 'archive', 'restore', 'reports', 'analytics', 'audit'],
  warehouse_manager: ['view', 'create', 'approve', 'reject', 'archive', 'restore', 'reports', 'analytics'],
  administrator: ['view', 'create', 'approve', 'reject', 'archive', 'restore', 'reports', 'analytics', 'audit'],
};

export const MOVEMENT_ROLES: MovementRole[] = ['viewer', 'inventory_operator', 'warehouse_operator', 'inventory_manager', 'warehouse_manager', 'administrator'];

export const DEFAULT_MOVEMENT_FILTER_STATE: MovementFilterState = {
  movementType: [],
  warehouse: [],
  status: [],
  dateRange: { start: '', end: '' },
  product: [],
};

export const MOVEMENT_EMPTY_STATES: Record<string, EmptyStateConfig> = {
  noTransactions: { title: 'No Transactions Found', message: 'No transactions match your current filters.', icon: 'list', actionLabel: 'Clear Filters' },
  noTransfers: { title: 'No Transfers Found', message: 'No warehouse transfers match your criteria.', icon: 'truck', actionLabel: 'Clear Filters' },
  noAdjustments: { title: 'No Adjustments Found', message: 'No stock adjustments match your criteria.', icon: 'edit', actionLabel: 'Clear Filters' },
  noHistory: { title: 'No History Found', message: 'No movement history available for the selected criteria.', icon: 'clock' },
  noResults: { title: 'No Results', message: 'Your search returned no results.', icon: 'search', actionLabel: 'Clear Search' },
  permissionDenied: { title: 'Access Denied', message: 'You do not have permission to access this section.', icon: 'shield-off' },
  offline: { title: 'Connection Offline', message: 'Unable to load movement data.', icon: 'wifi-off' },
  maintenance: { title: 'Under Maintenance', message: 'The movement system is undergoing maintenance.', icon: 'tool' },
  configurationRequired: { title: 'Configuration Required', message: 'Movement settings must be configured before use.', icon: 'settings', actionLabel: 'Configure' },
};

export const MOVEMENT_SETTINGS_SECTIONS: SettingsSection[] = [
  { id: 'general', label: 'General', icon: 'settings', description: 'General movement preferences.' },
  { id: 'approvals', label: 'Approvals', icon: 'check-circle', description: 'Approval workflow configuration.' },
  { id: 'reason-codes', label: 'Reason Codes', icon: 'list', description: 'Movement reason code management.' },
  { id: 'transfers', label: 'Transfers', icon: 'truck', description: 'Transfer default settings.' },
  { id: 'notifications', label: 'Notifications', icon: 'bell', description: 'Movement event notifications.' },
];

export const MOCK_MOVEMENT_METRICS: MovementMetric[] = [
  { label: 'Total Transactions', value: 65, trend: 'up', variant: 'info' },
  { label: 'Transfers', value: 18, trend: 'up', variant: 'info' },
  { label: 'Receipts', value: 14, trend: 'up', variant: 'success' },
  { label: 'Issues', value: 12, trend: 'down', variant: 'warning' },
  { label: 'Adjustments', value: 9, trend: 'neutral', variant: 'warning' },
  { label: 'Returns', value: 5, trend: 'down', variant: 'info' },
  { label: 'Pending', value: 7, trend: 'neutral', variant: 'warning' },
  { label: 'Completed', value: 48, trend: 'up', variant: 'success' },
];

export const MOCK_QUICK_ACTIONS: QuickAction[] = [
  { id: 'create-transfer', label: 'Create Transfer', icon: 'truck', description: 'Initiate warehouse transfer' },
  { id: 'create-adjustment', label: 'Stock Adjustment', icon: 'edit', description: 'Record stock adjustment' },
  { id: 'receipt', label: 'Goods Receipt', icon: 'arrow-down', description: 'Process goods receipt' },
  { id: 'issue', label: 'Goods Issue', icon: 'arrow-up', description: 'Process goods issue' },
  { id: 'audit', label: 'Audit Trail', icon: 'activity', description: 'View audit trail' },
];

export const MOCK_RECENT_ACTIVITY: RecentActivity[] = [
  { id: '1', action: 'Transfer Created', detail: 'Transfer TRF-001 from WH-A to WH-B', timestamp: new Date(Date.now() - 3600000).toISOString(), user: 'inventory@sporekart.com' },
  { id: '2', action: 'Goods Receipt', detail: 'Receipt GRN-001 for 500 units', timestamp: new Date(Date.now() - 7200000).toISOString(), user: 'warehouse@sporekart.com' },
  { id: '3', action: 'Adjustment', detail: 'Adjustment ADJ-001 for damaged stock', timestamp: new Date(Date.now() - 10800000).toISOString(), user: 'inventory@sporekart.com' },
  { id: '4', action: 'Transfer Completed', detail: 'Transfer TRF-002 completed successfully', timestamp: new Date(Date.now() - 14400000).toISOString(), user: 'system@sporekart.com' },
  { id: '5', action: 'Goods Issue', detail: 'Issue GIS-001 for 200 units', timestamp: new Date(Date.now() - 18000000).toISOString(), user: 'warehouse@sporekart.com' },
];
