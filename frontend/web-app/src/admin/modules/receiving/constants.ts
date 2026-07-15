import type {
  ReceivingSection, ReceiptStatus, ReceivingRole, ReceivingPermission,
  ReceivingFilterState, EmptyStateConfig, SettingsSection,
  ReceivingMetric, QuickAction, RecentActivity,
  InspectionStatus,
} from './types';

export const RECEIVING_SECTIONS: ReceivingSection[] = [
  { id: 'overview', label: 'Overview', icon: 'layout', description: 'Receiving domain overview and quick access.' },
  { id: 'goods-receipt', label: 'Goods Receipt', icon: 'arrow-down', description: 'Goods receipt registry.' },
  { id: 'receiving-queue', label: 'Receiving Queue', icon: 'list', description: 'Inbound receiving queue.' },
  { id: 'inspection', label: 'Inspection', icon: 'clipboard', description: 'Quality inspection framework.' },
  { id: 'acceptance', label: 'Acceptance', icon: 'check-circle', description: 'Goods acceptance processing.' },
  { id: 'rejection', label: 'Rejection', icon: 'x-circle', description: 'Goods rejection processing.' },
  { id: 'pending', label: 'Pending Receipts', icon: 'clock', description: 'Receipts awaiting action.' },
  { id: 'allocation', label: 'Warehouse Allocation', icon: 'home', description: 'Warehouse location assignment.' },
  { id: 'batch', label: 'Batch Assignment', icon: 'layers', description: 'Batch and lot association.' },
  { id: 'timeline', label: 'Timeline', icon: 'activity', description: 'Receiving timeline and events.' },
  { id: 'validation', label: 'Validation', icon: 'check-square', description: 'Receipt validation and quality checks.' },
  { id: 'analytics', label: 'Analytics', icon: 'bar-chart', description: 'Receiving analytics and insights.' },
  { id: 'reports', label: 'Reports', icon: 'file-text', description: 'Receiving operational reports.' },
  { id: 'settings', label: 'Settings', icon: 'settings', description: 'Receiving preferences and configuration.' },
  { id: 'help', label: 'Help', icon: 'help-circle', description: 'Receiving documentation and support.' },
];

export const WORKSPACE_SECTIONS = RECEIVING_SECTIONS;

export const RECEIPT_STATUSES: { value: ReceiptStatus; label: string; variant: 'success' | 'warning' | 'danger' | 'info' | 'neutral' | 'default'; description: string }[] = [
  { value: 'draft', label: 'Draft', variant: 'neutral', description: 'Receipt not yet submitted.' },
  { value: 'pending', label: 'Pending', variant: 'warning', description: 'Awaiting processing.' },
  { value: 'receiving', label: 'Receiving', variant: 'info', description: 'Goods being received.' },
  { value: 'inspection', label: 'Inspection', variant: 'info', description: 'Under quality inspection.' },
  { value: 'approved', label: 'Approved', variant: 'success', description: 'Receipt approved.' },
  { value: 'rejected', label: 'Rejected', variant: 'danger', description: 'Receipt rejected.' },
  { value: 'completed', label: 'Completed', variant: 'success', description: 'Receipt completed.' },
  { value: 'cancelled', label: 'Cancelled', variant: 'neutral', description: 'Receipt cancelled.' },
  { value: 'warehouse_allocation_pending', label: 'Allocation Pending', variant: 'warning', description: 'Awaiting warehouse allocation.' },
  { value: 'batch_pending', label: 'Batch Pending', variant: 'warning', description: 'Awaiting batch assignment.' },
];

export const INSPECTION_STATUSES: { value: InspectionStatus; label: string; variant: string }[] = [
  { value: 'pending', label: 'Pending', variant: 'neutral' },
  { value: 'visual', label: 'Visual Inspection', variant: 'info' },
  { value: 'quality', label: 'Quality Inspection', variant: 'info' },
  { value: 'packaging', label: 'Packaging Inspection', variant: 'info' },
  { value: 'quantity_verification', label: 'Qty Verification', variant: 'info' },
  { value: 'documentation', label: 'Documentation Check', variant: 'info' },
  { value: 'temperature_check', label: 'Temperature Check', variant: 'info' },
  { value: 'humidity_check', label: 'Humidity Check', variant: 'info' },
  { value: 'laboratory_test', label: 'Lab Test', variant: 'info' },
  { value: 'passed', label: 'Passed', variant: 'success' },
  { value: 'failed', label: 'Failed', variant: 'danger' },
];

export const RECEIVING_ROLE_PERMISSIONS: Record<ReceivingRole, ReceivingPermission[]> = {
  viewer: ['view'],
  receiving_operator: ['view', 'create', 'reports'],
  warehouse_operator: ['view', 'create', 'reports'],
  quality_inspector: ['view', 'create', 'inspect', 'reports'],
  warehouse_manager: ['view', 'create', 'inspect', 'approve', 'reject', 'archive', 'reports', 'analytics'],
  inventory_manager: ['view', 'create', 'inspect', 'approve', 'reject', 'archive', 'reports', 'analytics', 'audit'],
  administrator: ['view', 'create', 'inspect', 'approve', 'reject', 'archive', 'reports', 'analytics', 'audit'],
};

export const RECEIVING_ROLES: ReceivingRole[] = ['viewer', 'receiving_operator', 'warehouse_operator', 'quality_inspector', 'warehouse_manager', 'inventory_manager', 'administrator'];

export const DEFAULT_FILTER_STATE: ReceivingFilterState = {
  warehouse: [],
  receiptStatus: [],
  inspectionStatus: [],
  acceptanceStatus: [],
  dateRange: { start: '', end: '' },
  product: [],
};

export const EMPTY_STATES: Record<string, EmptyStateConfig> = {
  noReceipts: { title: 'No Receipts Found', message: 'No receipts match your current filters.', icon: 'arrow-down', actionLabel: 'Clear Filters' },
  noInspection: { title: 'No Inspections Found', message: 'No inspections match your criteria.', icon: 'clipboard', actionLabel: 'Clear Filters' },
  noQueue: { title: 'Queue Empty', message: 'No pending receipts in the queue.', icon: 'list' },
  noResults: { title: 'No Results', message: 'Your search returned no results.', icon: 'search', actionLabel: 'Clear Search' },
  permissionDenied: { title: 'Access Denied', message: 'You do not have permission to access this section.', icon: 'shield-off' },
  offline: { title: 'Connection Offline', message: 'Unable to load receiving data.', icon: 'wifi-off' },
  maintenance: { title: 'Under Maintenance', message: 'The receiving system is undergoing maintenance.', icon: 'tool' },
  configurationRequired: { title: 'Configuration Required', message: 'Receiving settings must be configured before use.', icon: 'settings', actionLabel: 'Configure' },
};

export const SETTINGS_SECTIONS: SettingsSection[] = [
  { id: 'general', label: 'General', icon: 'settings', description: 'General receiving preferences.' },
  { id: 'inspection', label: 'Inspection Defaults', icon: 'clipboard', description: 'Default inspection parameters.' },
  { id: 'allocation', label: 'Allocation Rules', icon: 'home', description: 'Warehouse allocation rules.' },
  { id: 'notifications', label: 'Notifications', icon: 'bell', description: 'Receiving event notifications.' },
];

export const MOCK_METRICS: ReceivingMetric[] = [
  { label: 'Pending Receipts', value: 12, trend: 'up', variant: 'warning' },
  { label: 'Completed Today', value: 8, trend: 'up', variant: 'success' },
  { label: 'Under Inspection', value: 5, trend: 'neutral', variant: 'info' },
  { label: 'Rejected', value: 2, trend: 'down', variant: 'danger' },
  { label: 'Awaiting Allocation', value: 3, trend: 'neutral', variant: 'warning' },
  { label: 'Acceptance Rate', value: 85, trend: 'up', variant: 'success' },
];

export const MOCK_QUICK_ACTIONS: QuickAction[] = [
  { id: 'new-receipt', label: 'New Receipt', icon: 'arrow-down', description: 'Create goods receipt' },
  { id: 'start-inspection', label: 'Start Inspection', icon: 'clipboard', description: 'Begin quality inspection' },
  { id: 'allocate-warehouse', label: 'Allocate Warehouse', icon: 'home', description: 'Assign warehouse location' },
  { id: 'assign-batch', label: 'Assign Batch', icon: 'layers', description: 'Link batch/lot to receipt' },
  { id: 'view-audit', label: 'View Audit Trail', icon: 'activity', description: 'View receiving audit trail' },
];

export const MOCK_RECENT_ACTIVITY: RecentActivity[] = [
  { id: '1', action: 'Receipt Created', detail: 'Receipt GRN-001 created for White Button Mushroom', timestamp: new Date(Date.now() - 1800000).toISOString(), user: 'receiving@sporekart.com' },
  { id: '2', action: 'Inspection Passed', detail: 'Shiitake Mushroom GRN-002 passed quality check', timestamp: new Date(Date.now() - 3600000).toISOString(), user: 'quality@sporekart.com' },
  { id: '3', action: 'Batch Assigned', detail: 'Batch BATCH-001 assigned to GRN-003', timestamp: new Date(Date.now() - 5400000).toISOString(), user: 'inventory@sporekart.com' },
  { id: '4', action: 'Receipt Rejected', detail: 'GRN-004 rejected due to packaging failure', timestamp: new Date(Date.now() - 7200000).toISOString(), user: 'quality@sporekart.com' },
  { id: '5', action: 'Allocation Complete', detail: 'GRN-005 allocated to Cold Storage Zone C', timestamp: new Date(Date.now() - 9000000).toISOString(), user: 'warehouse@sporekart.com' },
];
