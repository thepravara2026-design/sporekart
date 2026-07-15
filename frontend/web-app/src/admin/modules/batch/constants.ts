import type {
  BatchSection, BatchLifecycleState, ExpiryStatus, QualityStatus, ShelfLifeUnit,
  BatchRole, BatchPermission, BatchFilterOption, BatchSearchField,
  BatchFilterState, EmptyStateConfig, SettingsSection, BatchMetric, QuickAction, RecentActivity,
} from './types';

export const BATCH_SECTIONS: BatchSection[] = [
  { id: 'overview', label: 'Overview', icon: 'layout', description: 'Batch domain overview and quick access.' },
  { id: 'registry', label: 'Batch Registry', icon: 'database', description: 'All batches — search, filter, manage.' },
  { id: 'lots', label: 'Lot Registry', icon: 'layers', description: 'All lots associated with batches.' },
  { id: 'expiry', label: 'Expiry Tracking', icon: 'clock', description: 'Expiry monitoring and near-expiry alerts.' },
  { id: 'shelf-life', label: 'Shelf Life', icon: 'calendar', description: 'Shelf life configurations and storage conditions.' },
  { id: 'quality', label: 'Quality Status', icon: 'check-circle', description: 'Quality inspections and status tracking.' },
  { id: 'traceability', label: 'Traceability', icon: 'activity', description: 'End-to-end batch traceability timeline.' },
  { id: 'timeline', label: 'Batch Timeline', icon: 'activity', description: 'Global batch event timeline.' },
  { id: 'analytics', label: 'Analytics', icon: 'bar-chart', description: 'Batch analytics and distribution insights.' },
  { id: 'validation', label: 'Validation', icon: 'check-square', description: 'Data validation and quality checks.' },
  { id: 'reports', label: 'Reports', icon: 'file-text', description: 'Batch operational reports.' },
  { id: 'settings', label: 'Settings', icon: 'settings', description: 'Batch preferences and configuration.' },
  { id: 'help', label: 'Help', icon: 'help-circle', description: 'Batch engine documentation and support.' },
];

export const BATCH_LIFECYCLE_STATES: { value: BatchLifecycleState; label: string; priority: number; variant: 'success' | 'warning' | 'danger' | 'info' | 'neutral' | 'default'; description: string }[] = [
  { value: 'created', label: 'Created', priority: 1, variant: 'info', description: 'Batch record created.' },
  { value: 'quality_review', label: 'Quality Review', priority: 2, variant: 'warning', description: 'Under quality review.' },
  { value: 'approved', label: 'Approved', priority: 3, variant: 'success', description: 'Batch approved for operations.' },
  { value: 'operational', label: 'Operational', priority: 4, variant: 'success', description: 'Batch active and operational.' },
  { value: 'near_expiry', label: 'Near Expiry', priority: 5, variant: 'warning', description: 'Batch approaching expiry.' },
  { value: 'expired', label: 'Expired', priority: 6, variant: 'danger', description: 'Batch has expired.' },
  { value: 'returned', label: 'Returned', priority: 7, variant: 'warning', description: 'Batch returned from customer.' },
  { value: 'archived', label: 'Archived', priority: 8, variant: 'neutral', description: 'Batch archived.' },
  { value: 'rejected', label: 'Rejected', priority: 9, variant: 'danger', description: 'Batch rejected.' },
  { value: 'blocked', label: 'Blocked', priority: 10, variant: 'danger', description: 'Batch blocked from operations.' },
  { value: 'recalled', label: 'Recalled', priority: 11, variant: 'danger', description: 'Batch recalled.' },
  { value: 'disposed', label: 'Disposed', priority: 12, variant: 'neutral', description: 'Batch disposed.' },
];

export const BATCH_LIFECYCLE_TRANSITIONS: { from: BatchLifecycleState; to: BatchLifecycleState; label: string }[] = [
  { from: 'created', to: 'quality_review', label: 'Submit for Review' },
  { from: 'quality_review', to: 'approved', label: 'Approve' },
  { from: 'quality_review', to: 'rejected', label: 'Reject' },
  { from: 'approved', to: 'operational', label: 'Activate' },
  { from: 'operational', to: 'near_expiry', label: 'Near Expiry Warning' },
  { from: 'near_expiry', to: 'expired', label: 'Mark Expired' },
  { from: 'operational', to: 'expired', label: 'Mark Expired' },
  { from: 'operational', to: 'blocked', label: 'Block' },
  { from: 'blocked', to: 'operational', label: 'Unblock' },
  { from: 'operational', to: 'returned', label: 'Return' },
  { from: 'expired', to: 'archived', label: 'Archive' },
  { from: 'returned', to: 'archived', label: 'Archive' },
  { from: 'rejected', to: 'archived', label: 'Archive' },
  { from: 'blocked', to: 'archived', label: 'Archive' },
  { from: 'recalled', to: 'disposed', label: 'Dispose' },
  { from: 'expired', to: 'disposed', label: 'Dispose' },
  { from: 'archived', to: 'operational', label: 'Restore' },
];

export const EXPIRY_STATUSES: { value: ExpiryStatus; label: string; priority: number; variant: 'success' | 'warning' | 'danger' | 'info' | 'neutral' | 'default'; description: string }[] = [
  { value: 'fresh', label: 'Fresh', priority: 1, variant: 'success', description: 'Recently produced, full shelf life remaining.' },
  { value: 'healthy', label: 'Healthy', priority: 2, variant: 'success', description: 'Within optimal shelf life range.' },
  { value: 'monitor', label: 'Monitor', priority: 3, variant: 'info', description: 'Approaching mid-life, monitor regularly.' },
  { value: 'near_expiry', label: 'Near Expiry', priority: 4, variant: 'warning', description: 'Close to expiry date.' },
  { value: 'critical', label: 'Critical', priority: 5, variant: 'danger', description: 'Critically close to expiry.' },
  { value: 'expired', label: 'Expired', priority: 6, variant: 'danger', description: 'Past expiry date.' },
  { value: 'blocked', label: 'Blocked', priority: 7, variant: 'danger', description: 'Expiry blocked for compliance.' },
  { value: 'disposed', label: 'Disposed', priority: 8, variant: 'neutral', description: 'Disposed after expiry.' },
  { value: 'recalled', label: 'Recalled', priority: 9, variant: 'danger', description: 'Recalled due to expiry concerns.' },
];

export const QUALITY_STATUSES: { value: QualityStatus; label: string; priority: number; variant: 'success' | 'warning' | 'danger' | 'info' | 'neutral' | 'default'; description: string }[] = [
  { value: 'pending_inspection', label: 'Pending Inspection', priority: 1, variant: 'warning', description: 'Awaiting quality inspection.' },
  { value: 'under_review', label: 'Under Review', priority: 2, variant: 'info', description: 'Quality review in progress.' },
  { value: 'approved', label: 'Approved', priority: 3, variant: 'success', description: 'Quality approved.' },
  { value: 'rejected', label: 'Rejected', priority: 4, variant: 'danger', description: 'Quality rejected.' },
  { value: 'blocked', label: 'Blocked', priority: 5, variant: 'danger', description: 'Quality blocked for compliance.' },
  { value: 'quarantined', label: 'Quarantined', priority: 6, variant: 'danger', description: 'Quarantined for investigation.' },
  { value: 'returned', label: 'Returned', priority: 7, variant: 'warning', description: 'Returned from customer.' },
  { value: 'disposed', label: 'Disposed', priority: 8, variant: 'neutral', description: 'Disposed after quality failure.' },
];

export const SHELF_LIFE_UNITS: { value: ShelfLifeUnit; label: string }[] = [
  { value: 'days', label: 'Days' },
  { value: 'weeks', label: 'Weeks' },
  { value: 'months', label: 'Months' },
  { value: 'years', label: 'Years' },
  { value: 'custom', label: 'Custom' },
];

export const BATCH_ROLE_PERMISSIONS: Record<BatchRole, BatchPermission[]> = {
  viewer: ['view'],
  inventory_operator: ['view', 'create', 'edit', 'reports'],
  quality_operator: ['view', 'create', 'edit', 'approve', 'reject', 'reports'],
  warehouse_manager: ['view', 'create', 'edit', 'archive', 'restore', 'reports'],
  inventory_manager: ['view', 'create', 'edit', 'approve', 'reject', 'archive', 'restore', 'reports', 'settings', 'recall'],
  administrator: ['view', 'create', 'edit', 'approve', 'reject', 'archive', 'restore', 'reports', 'settings', 'recall'],
};

export const BATCH_ROLES: BatchRole[] = ['viewer', 'inventory_operator', 'quality_operator', 'warehouse_manager', 'inventory_manager', 'administrator'];

export const BATCH_FILTERS: BatchFilterOption[] = [
  { id: 'warehouse', label: 'Warehouse', options: [] },
  { id: 'product', label: 'Product', options: [] },
  { id: 'status', label: 'Status', options: BATCH_LIFECYCLE_STATES.map((s) => ({ value: s.value, label: s.label })) },
  { id: 'qualityStatus', label: 'Quality Status', options: QUALITY_STATUSES.map((s) => ({ value: s.value, label: s.label })) },
  { id: 'expiryStatus', label: 'Expiry Status', options: EXPIRY_STATUSES.map((s) => ({ value: s.value, label: s.label })) },
];

export const BATCH_SEARCH_FIELDS: BatchSearchField[] = [
  { id: 'batchId', label: 'Batch ID', placeholder: 'Search by batch ID...' },
  { id: 'batchCode', label: 'Batch Code', placeholder: 'Search by batch code...' },
  { id: 'lotId', label: 'Lot ID', placeholder: 'Search by lot ID...' },
  { id: 'lotCode', label: 'Lot Code', placeholder: 'Search by lot code...' },
  { id: 'product', label: 'Product', placeholder: 'Search by product...' },
  { id: 'sku', label: 'SKU', placeholder: 'Search by SKU...' },
  { id: 'warehouse', label: 'Warehouse', placeholder: 'Search by warehouse...' },
];

export const DEFAULT_BATCH_FILTER_STATE: BatchFilterState = {
  warehouse: [],
  product: [],
  status: [],
  qualityStatus: [],
  expiryStatus: [],
  dateRange: { start: '', end: '' },
  shelfLife: { min: 0, max: 0 },
};

export const BATCH_EMPTY_STATES: Record<string, EmptyStateConfig> = {
  noBatches: { title: 'No Batches Found', message: 'No batches match your current filters. Try adjusting your search criteria.', icon: 'database', actionLabel: 'Clear Filters' },
  noLots: { title: 'No Lots Found', message: 'No lots are associated with this batch yet.', icon: 'layers', actionLabel: 'Add Lot' },
  noExpiryData: { title: 'No Expiry Data', message: 'No expiry data available for the selected criteria.', icon: 'clock' },
  noResults: { title: 'No Results', message: 'Your search returned no results.', icon: 'search', actionLabel: 'Clear Search' },
  offline: { title: 'Connection Offline', message: 'Unable to load batch data. Please check your connection.', icon: 'wifi-off' },
  maintenance: { title: 'Under Maintenance', message: 'The batch management system is undergoing maintenance.', icon: 'tool', actionLabel: 'Check Status' },
  permissionDenied: { title: 'Access Denied', message: 'You do not have permission to access this section.', icon: 'shield-off' },
  configurationRequired: { title: 'Configuration Required', message: 'Batch settings must be configured before use.', icon: 'settings', actionLabel: 'Configure' },
};

export const BATCH_SETTINGS_SECTIONS: SettingsSection[] = [
  { id: 'general', label: 'General', icon: 'settings', description: 'General batch preferences.' },
  { id: 'expiry', label: 'Expiry Thresholds', icon: 'clock', description: 'Near expiry and critical thresholds.' },
  { id: 'quality', label: 'Quality Rules', icon: 'check-circle', description: 'Quality inspection rules.' },
  { id: 'traceability', label: 'Traceability', icon: 'activity', description: 'Traceability configuration.' },
  { id: 'notifications', label: 'Notifications', icon: 'bell', description: 'Batch event notifications.' },
];

export const MOCK_BATCH_METRICS: BatchMetric[] = [
  { label: 'Total Batches', value: 45, trend: 'up', variant: 'info' },
  { label: 'Total Lots', value: 120, trend: 'up', variant: 'info' },
  { label: 'Near Expiry', value: 8, trend: 'down', variant: 'warning' },
  { label: 'Expired', value: 3, trend: 'down', variant: 'danger' },
  { label: 'Approved', value: 32, trend: 'up', variant: 'success' },
  { label: 'Rejected', value: 4, trend: 'down', variant: 'danger' },
  { label: 'Pending Review', value: 6, trend: 'neutral', variant: 'warning' },
  { label: 'Quality Issues', value: 5, trend: 'down', variant: 'danger' },
];

export const MOCK_QUICK_ACTIONS: QuickAction[] = [
  { id: 'create-batch', label: 'Create Batch', icon: 'plus', description: 'Register a new batch', action: 'create' },
  { id: 'inspect', label: 'Quality Inspection', icon: 'check-circle', description: 'Start quality inspection', action: 'inspect' },
  { id: 'expiry-report', label: 'Expiry Report', icon: 'file-text', description: 'Generate expiry report', action: 'report' },
  { id: 'trace', label: 'Trace Batch', icon: 'activity', description: 'Trace batch history', action: 'trace' },
  { id: 'bulk-update', label: 'Bulk Update', icon: 'edit', description: 'Update multiple batches', action: 'bulk' },
];

export const MOCK_RECENT_ACTIVITY: RecentActivity[] = [
  { id: '1', action: 'Batch Created', detail: 'Batch BCH-001 created for Product A', timestamp: new Date(Date.now() - 3600000).toISOString(), user: 'john@sporekart.com' },
  { id: '2', action: 'Quality Approved', detail: 'Batch BCH-002 approved after inspection', timestamp: new Date(Date.now() - 7200000).toISOString(), user: 'quality@sporekart.com' },
  { id: '3', action: 'Expiry Warning', detail: 'Batch BCH-003 approaching expiry (7 days)', timestamp: new Date(Date.now() - 10800000).toISOString(), user: 'system@sporekart.com' },
  { id: '4', action: 'Batch Rejected', detail: 'Batch BCH-004 rejected during quality review', timestamp: new Date(Date.now() - 14400000).toISOString(), user: 'quality@sporekart.com' },
  { id: '5', action: 'Lot Added', detail: 'Lot LOT-012 added to Batch BCH-001', timestamp: new Date(Date.now() - 18000000).toISOString(), user: 'warehouse@sporekart.com' },
];
