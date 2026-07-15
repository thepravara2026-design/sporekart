import type {
  StockSection, StockState, StockHealth, AvailabilityLevel, ReservationType,
  StockRole, StockPermission, StockMetric, HealthMetric, StatusCount,
  QuickAction, RecentActivity, StockFilterOption, StockSearchField,
  StockFilterState, EmptyStateConfig, SettingsSection,
} from './types';

export const STOCK_SECTIONS: StockSection[] = [
  { id: 'overview', label: 'Overview', icon: 'layout', description: 'Stock domain overview and quick access.' },
  { id: 'dashboard', label: 'Stock Dashboard', icon: 'bar-chart', description: 'Executive stock metrics and health.' },
  { id: 'registry', label: 'Stock Registry', icon: 'database', description: 'All stock records — search, filter, manage.' },
  { id: 'available', label: 'Available Stock', icon: 'check-circle', description: 'Stock available for use or sale.' },
  { id: 'reserved', label: 'Reserved Stock', icon: 'lock', description: 'Stock reserved for orders, transfers or production.' },
  { id: 'incoming', label: 'Incoming Stock', icon: 'truck', description: 'Stock expected from purchase orders or transfers.' },
  { id: 'allocated', label: 'Allocated Stock', icon: 'bookmark', description: 'Stock allocated but not yet picked.' },
  { id: 'damaged', label: 'Damaged Stock', icon: 'alert-triangle', description: 'Stock flagged as damaged.' },
  { id: 'expired', label: 'Expired Stock', icon: 'clock', description: 'Stock past its expiry date.' },
  { id: 'blocked', label: 'Blocked Stock', icon: 'slash', description: 'Stock blocked for quality or compliance reasons.' },
  { id: 'health', label: 'Stock Health', icon: 'heart', description: 'Health assessment across all stock.' },
  { id: 'timeline', label: 'Timeline', icon: 'activity', description: 'Global stock event timeline.' },
  { id: 'validation', label: 'Validation', icon: 'check-circle', description: 'Data validation and quality checks.' },
  { id: 'reports', label: 'Reports', icon: 'file-text', description: 'Stock operational reports.' },
  { id: 'settings', label: 'Settings', icon: 'settings', description: 'Stock preferences and configuration.' },
  { id: 'help', label: 'Help', icon: 'help-circle', description: 'Stock engine documentation and support.' },
];

export const STOCK_STATES: { value: StockState; label: string; priority: number; description: string }[] = [
  { value: 'available', label: 'Available', priority: 1, description: 'Stock ready for use, sale or transfer.' },
  { value: 'reserved', label: 'Reserved', priority: 2, description: 'Stock reserved for a specific purpose.' },
  { value: 'incoming', label: 'Incoming', priority: 3, description: 'Stock expected but not yet received.' },
  { value: 'allocated', label: 'Allocated', priority: 4, description: 'Stock allocated to orders.' },
  { value: 'damaged', label: 'Damaged', priority: 5, description: 'Stock physically damaged.' },
  { value: 'expired', label: 'Expired', priority: 6, description: 'Stock past expiry date.' },
  { value: 'blocked', label: 'Blocked', priority: 7, description: 'Stock blocked from operations.' },
  { value: 'quarantine', label: 'Quarantine', priority: 8, description: 'Stock under quarantine.' },
  { value: 'inspection', label: 'Inspection', priority: 9, description: 'Stock awaiting quality inspection.' },
  { value: 'returned', label: 'Returned', priority: 10, description: 'Stock returned by customers.' },
  { value: 'lost', label: 'Lost', priority: 11, description: 'Stock recorded as lost.' },
  { value: 'adjustment_pending', label: 'Adjustment Pending', priority: 12, description: 'Stock awaiting adjustment approval.' },
  { value: 'future_manufacturing', label: 'Future Mfg', priority: 13, description: 'Stock planned for manufacturing.' },
  { value: 'future_transit', label: 'Future Transit', priority: 14, description: 'Stock planned for inter-warehouse transit.' },
  { value: 'future_consignment', label: 'Future Consignment', priority: 15, description: 'Stock planned for consignment.' },
];

export const STOCK_HEALTH_LEVELS: { value: StockHealth; label: string; variant: 'success' | 'warning' | 'danger' | 'info' | 'neutral' | 'default'; description: string }[] = [
  { value: 'healthy', label: 'Healthy', variant: 'success', description: 'Stock levels are within optimal range.' },
  { value: 'low', label: 'Low', variant: 'warning', description: 'Stock is below optimal threshold.' },
  { value: 'critical', label: 'Critical', variant: 'danger', description: 'Stock is critically low.' },
  { value: 'out_of_stock', label: 'Out of Stock', variant: 'danger', description: 'No stock available.' },
  { value: 'overstock', label: 'Overstock', variant: 'info', description: 'Stock exceeds maximum threshold.' },
  { value: 'needs_inspection', label: 'Needs Inspection', variant: 'warning', description: 'Stock flagged for quality inspection.' },
  { value: 'near_expiry', label: 'Near Expiry', variant: 'warning', description: 'Stock approaching expiry date.' },
  { value: 'damaged', label: 'Damaged', variant: 'danger', description: 'Stock recorded as damaged.' },
];

export const AVAILABILITY_LEVELS: { value: AvailabilityLevel; label: string; variant: 'success' | 'warning' | 'danger' | 'info' | 'neutral' | 'default' }[] = [
  { value: 'available', label: 'Available', variant: 'success' },
  { value: 'limited', label: 'Limited', variant: 'warning' },
  { value: 'unavailable', label: 'Unavailable', variant: 'danger' },
  { value: 'pre_order', label: 'Pre-order', variant: 'info' },
  { value: 'backorder', label: 'Backorder', variant: 'info' },
];

export const RESERVATION_TYPES: { value: ReservationType; label: string; description: string }[] = [
  { value: 'reserved_for_orders', label: 'Orders', description: 'Reserved for customer orders.' },
  { value: 'reserved_for_transfers', label: 'Transfers', description: 'Reserved for inter-warehouse transfer.' },
  { value: 'reserved_for_production', label: 'Production', description: 'Reserved for manufacturing.' },
  { value: 'reserved_for_qc', label: 'Quality Check', description: 'Reserved for quality inspection.' },
  { value: 'reserved_for_returns', label: 'Returns', description: 'Reserved for expected returns.' },
];

export const STOCK_ROLES: StockRole[] = ['viewer', 'inventory_operator', 'warehouse_operator', 'inventory_manager', 'administrator'];

export const STOCK_PERMISSIONS: StockPermission[] = ['view', 'edit', 'archive', 'reports', 'validation', 'settings', 'future_transactions'];

export const STOCK_ROLE_PERMISSIONS: Record<StockRole, StockPermission[]> = {
  viewer: ['view'],
  inventory_operator: ['view', 'edit', 'reports'],
  warehouse_operator: ['view', 'edit', 'reports'],
  inventory_manager: ['view', 'edit', 'archive', 'reports', 'validation', 'settings'],
  administrator: ['view', 'edit', 'archive', 'reports', 'validation', 'settings', 'future_transactions'],
};

export const STOCK_MOCK_METRICS: StockMetric[] = [
  { id: 'total_records', title: 'Total Stock Records', value: '18,240', trend: 'up', percentage: 5.8, comparison: 'vs last month', icon: 'database', color: 'var(--color-primary)' },
  { id: 'available', title: 'Available Stock', value: '1.24M', trend: 'up', percentage: 3.2, comparison: 'vs last week', icon: 'check-circle', color: 'var(--color-success)' },
  { id: 'reserved', title: 'Reserved Stock', value: '186,400', trend: 'up', percentage: 2.1, comparison: 'vs last week', icon: 'lock', color: 'var(--color-info)' },
  { id: 'incoming', title: 'Incoming Stock', value: '52,800', trend: 'up', percentage: 8.7, comparison: 'vs last week', icon: 'truck', color: 'var(--color-info)' },
  { id: 'allocated', title: 'Allocated Stock', value: '94,200', trend: 'up', percentage: 4.5, comparison: 'vs last week', icon: 'bookmark', color: 'var(--color-info)' },
  { id: 'low_stock', title: 'Low Stock Items', value: '412', trend: 'up', percentage: 6.3, comparison: 'needs attention', icon: 'alert-triangle', color: 'var(--color-warning)' },
  { id: 'out_of_stock', title: 'Out of Stock', value: '67', trend: 'down', percentage: 3.1, comparison: 'vs last week', icon: 'x-circle', color: 'var(--color-danger)' },
  { id: 'damaged', title: 'Damaged Stock', value: '2,340', trend: 'down', percentage: 1.8, comparison: 'vs last month', icon: 'alert-triangle', color: 'var(--color-danger)' },
  { id: 'expired', title: 'Expired Stock', value: '890', trend: 'down', percentage: 2.4, comparison: 'vs last month', icon: 'clock', color: 'var(--color-danger)' },
  { id: 'blocked', title: 'Blocked Stock', value: '156', trend: 'flat', percentage: 0, comparison: 'stable', icon: 'slash', color: 'var(--color-warning)' },
  { id: 'healthy_pct', title: 'Healthy Stock %', value: '87.3%', trend: 'up', percentage: 1.2, comparison: 'vs last month', icon: 'heart', color: 'var(--color-success)' },
  { id: 'warehouses', title: 'Warehouses Covered', value: '12', trend: 'flat', percentage: 0, comparison: 'all active', icon: 'home', color: 'var(--color-info)' },
];

export const STOCK_HEALTH_METRICS: HealthMetric[] = [
  { id: 'overall', label: 'Overall Stock Health', score: 87 },
  { id: 'availability', label: 'Availability Score', score: 92 },
  { id: 'freshness', label: 'Freshness Score', score: 84 },
  { id: 'accuracy', label: 'Accuracy Score', score: 91 },
  { id: 'utilization', label: 'Utilization Score', score: 78 },
];

export const STOCK_STATUS_COUNTS: StatusCount[] = [
  { id: 'healthy', label: 'Healthy', count: 15230, variant: 'success' },
  { id: 'low', label: 'Low', count: 412, variant: 'warning' },
  { id: 'critical', label: 'Critical', count: 67, variant: 'danger' },
  { id: 'overstock', label: 'Overstock', count: 1280, variant: 'info' },
  { id: 'damaged', label: 'Damaged', count: 2340, variant: 'danger' },
  { id: 'expired', label: 'Expired', count: 890, variant: 'danger' },
  { id: 'blocked', label: 'Blocked', count: 156, variant: 'warning' },
];

export const STOCK_RECENT_ACTIVITIES: RecentActivity[] = [
  { id: 's1', icon: 'database', text: 'Stock record STK-10241 updated to Reserved', timestamp: '2m ago' },
  { id: 's2', icon: 'truck', text: 'Incoming shipment expected for WH-3', timestamp: '12m ago' },
  { id: 's3', icon: 'alert-triangle', text: 'Low stock alert for SKU-4418 (Oyster Spawn)', timestamp: '28m ago' },
  { id: 's4', icon: 'check-circle', text: 'Stock health check completed — 87.3% healthy', timestamp: '1h ago' },
  { id: 's5', icon: 'clock', text: 'Batch near-expiry flagged for SKU-2291', timestamp: '2h ago' },
];

export const STOCK_QUICK_ACTIONS: QuickAction[] = [
  { id: 'new_record', label: 'New Stock Record', icon: 'plus', href: '/admin/stock', permission: 'edit' },
  { id: 'validate', label: 'Run Validation', icon: 'check-circle', href: '/admin/stock', permission: 'validation' },
  { id: 'health_check', label: 'Health Check', icon: 'heart', href: '/admin/stock', permission: 'view' },
  { id: 'export', label: 'Export Report', icon: 'file-text', href: '/admin/stock', permission: 'reports' },
  { id: 'bulk_update', label: 'Bulk Update', icon: 'edit', href: '/admin/stock', permission: 'edit' },
  { id: 'settings', label: 'Configure', icon: 'settings', href: '/admin/stock', permission: 'settings' },
];

export const STOCK_FILTER_OPTIONS: StockFilterOption[] = [
  {
    id: 'warehouse', label: 'Warehouse', multi: true,
    options: [
      { value: 'wh-1', label: 'Mumbai Main' }, { value: 'wh-2', label: 'Delhi Hub' },
      { value: 'wh-3', label: 'Pune Cold' }, { value: 'wh-4', label: 'Bengaluru DC' },
    ],
  },
  {
    id: 'stockState', label: 'Stock State', multi: true,
    options: STOCK_STATES.map((s) => ({ value: s.value, label: s.label })),
  },
  {
    id: 'health', label: 'Health', multi: true,
    options: STOCK_HEALTH_LEVELS.map((h) => ({ value: h.value, label: h.label })),
  },
  {
    id: 'availability', label: 'Availability', multi: true,
    options: AVAILABILITY_LEVELS.map((a) => ({ value: a.value, label: a.label })),
  },
  {
    id: 'category', label: 'Category', multi: true,
    options: [
      { value: 'spawn', label: 'Spawn' }, { value: 'compost', label: 'Compost' },
      { value: 'supplements', label: 'Supplements' }, { value: 'tools', label: 'Tools' },
    ],
  },
  {
    id: 'brand', label: 'Brand', multi: true,
    options: [
      { value: 'SporeKart', label: 'SporeKart' }, { value: 'AgriGrow', label: 'AgriGrow' },
      { value: 'GreenLine', label: 'GreenLine' },
    ],
  },
  {
    id: 'status', label: 'Status', multi: true,
    options: [
      { value: 'active', label: 'Active' }, { value: 'inactive', label: 'Inactive' },
      { value: 'archived', label: 'Archived' },
    ],
  },
];

export const STOCK_SEARCH_FIELDS: StockSearchField[] = [
  { id: 'inventoryItemName', label: 'Inventory Item' },
  { id: 'code', label: 'Stock ID' },
  { id: 'sku', label: 'SKU' },
  { id: 'warehouseName', label: 'Warehouse' },
  { id: 'category', label: 'Category' },
  { id: 'brand', label: 'Brand' },
  { id: 'status', label: 'Status' },
  { id: 'health', label: 'Health' },
];

export const STOCK_EMPTY_STATES: Record<string, EmptyStateConfig> = {
  no_stock: { key: 'no_stock', title: 'No Stock Records', message: 'No stock records exist. Inventory items must have stock records to begin tracking.', icon: 'database', actionLabel: 'Create Record' },
  no_warehouse: { key: 'no_warehouse', title: 'No Warehouse Selected', message: 'Select a warehouse to view its stock records.', icon: 'home', actionLabel: 'Select Warehouse' },
  no_results: { key: 'no_results', title: 'No Results', message: 'No stock records match your current search and filters.', icon: 'search', actionLabel: 'Clear Filters' },
  permission_denied: { key: 'permission_denied', title: 'Permission Denied', message: 'You do not have access to this stock area.', icon: 'lock', actionLabel: 'Request Access' },
  offline: { key: 'offline', title: 'You are Offline', message: 'Stock data cannot be synced while offline.', icon: 'wifi-off', actionLabel: 'Retry' },
  maintenance: { key: 'maintenance', title: 'Under Maintenance', message: 'The stock module is temporarily under maintenance.', icon: 'tool', actionLabel: 'View Status' },
  configuration_required: { key: 'configuration_required', title: 'Configuration Required', message: 'Complete the stock setup to enable this section.', icon: 'settings', actionLabel: 'Configure' },
  no_timeline: { key: 'no_timeline', title: 'No Timeline Events', message: 'No stock events have been recorded for this record.', icon: 'activity', actionLabel: 'Refresh' },
};

export const STOCK_SETTINGS_SECTIONS: SettingsSection[] = [
  { id: 'general', label: 'General', description: 'Organization and regional stock defaults.', icon: 'settings' },
  { id: 'state_config', label: 'State Configuration', description: 'Stock state behaviour and transitions.', icon: 'layers' },
  { id: 'health_rules', label: 'Health Rules', description: 'Stock health thresholds and auto-classification.', icon: 'heart' },
  { id: 'low_stock', label: 'Low Stock Rules', description: 'Low stock, critical and overstock thresholds.', icon: 'alert-triangle' },
  { id: 'reservation', label: 'Reservation Rules', description: 'Reservation expiry and auto-release.', icon: 'lock' },
  { id: 'availability', label: 'Availability Rules', description: 'Availability calculation rules.', icon: 'check-circle' },
  { id: 'timeline', label: 'Timeline Settings', description: 'Event retention and display preferences.', icon: 'activity' },
  { id: 'security', label: 'Security', description: 'Access and security controls.', icon: 'lock', placeholder: true },
  { id: 'notifications', label: 'Notifications', description: 'Alerts and notifications.', icon: 'bell', placeholder: true },
  { id: 'integrations', label: 'Future Integrations', description: 'WMS, ERP and 3PL connectors.', icon: 'link', placeholder: true },
];

export const STOCK_SAVED_FILTERS: { id: string; label: string; description?: string }[] = [
  { id: 'low-health', label: 'Low Health', description: 'Stock records with low or critical health.' },
  { id: 'needs-attention', label: 'Needs Attention', description: 'Damaged, expired or blocked stock.' },
  { id: 'incoming-today', label: 'Incoming Today', description: 'Stock expected to arrive today.' },
  { id: 'near-expiry', label: 'Near Expiry', description: 'Stock approaching expiry window.' },
  { id: 'overstock', label: 'Overstock', description: 'Stock exceeding maximum thresholds.' },
];

export const DEFAULT_STOCK_FILTER_STATE: StockFilterState = {
  warehouse: [],
  stockState: [],
  health: [],
  availability: [],
  category: [],
  brand: [],
  status: [],
  savedFilters: [],
};

export const STATE_VARIANTS: Record<StockState, 'success' | 'warning' | 'danger' | 'info' | 'neutral' | 'default'> = {
  available: 'success',
  reserved: 'info',
  incoming: 'info',
  allocated: 'info',
  damaged: 'danger',
  expired: 'danger',
  blocked: 'warning',
  quarantine: 'warning',
  inspection: 'warning',
  returned: 'neutral',
  lost: 'default',
  adjustment_pending: 'warning',
  future_manufacturing: 'info',
  future_transit: 'info',
  future_consignment: 'info',
};

export const LOW_STOCK_THRESHOLDS = {
  lowThreshold: 100,
  criticalThreshold: 20,
  overstockThreshold: 1000,
};

export const WAREHOUSE_NAMES: string[] = [
  'Mumbai Main Warehouse', 'Delhi Hub', 'Pune Cold Storage',
  'Bengaluru DC', 'Chennai Depot', 'Kolkata Facility',
  'Hyderabad Center', 'Ahmedabad Warehouse', 'Jaipur Store',
  'Lucknow Depot', 'Chandigarh Hub', 'Bhopal Facility',
];
