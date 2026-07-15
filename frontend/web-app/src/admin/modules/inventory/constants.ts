import type {
  InventorySection,
  InventoryStatus,
  StockStatus,
  InventoryUnit,
  InventoryRole,
  InventoryPermission,
  InventoryMetric,
  HealthMetric,
  QuickAction,
  RecentActivity,
  InventoryFilterOption,
  InventorySearchField,
  EmptyStateConfig,
  SettingsSection,
  StatusCount,
} from './types';

export const INVENTORY_SECTIONS: InventorySection[] = [
  { id: 'overview', label: 'Overview', icon: 'layout', href: '/admin/inventory', description: 'Inventory domain overview and quick access.' },
  { id: 'dashboard', label: 'Inventory Dashboard', icon: 'bar-chart', href: '/admin/inventory', description: 'Executive inventory metrics and health.' },
  { id: 'warehouses', label: 'Warehouses', icon: 'home', href: '/admin/inventory', description: 'Warehouse storage and capacity.' },
  { id: 'stock', label: 'Stock', icon: 'box', href: '/admin/inventory', description: 'Available, reserved and incoming stock.' },
  { id: 'items', label: 'Inventory Items', icon: 'package', href: '/admin/inventory', description: 'Manage inventory items and SKUs.' },
  { id: 'movements', label: 'Movements', icon: 'move', href: '/admin/inventory', description: 'Stock movements and ledger.' },
  { id: 'receiving', label: 'Receiving', icon: 'download', href: '/admin/inventory', description: 'Goods receiving and put-away.' },
  { id: 'transfers', label: 'Transfers', icon: 'git-branch', href: '/admin/inventory', description: 'Inter-warehouse transfers.' },
  { id: 'batch', label: 'Batch Management', icon: 'layers', href: '/admin/inventory', description: 'Batch, lot and expiry tracking.' },
  { id: 'adjustments', label: 'Adjustments', icon: 'sliders', href: '/admin/inventory', description: 'Stock adjustments and cycle counts.' },
  { id: 'analytics', label: 'Analytics', icon: 'trending-up', href: '/admin/inventory', description: 'Inventory analytics and insights.' },
  { id: 'validation', label: 'Validation', icon: 'check-circle', href: '/admin/inventory', description: 'Data validation and QA.' },
  { id: 'reports', label: 'Reports', icon: 'file-text', href: '/admin/inventory', description: 'Operational and audit reports.' },
  { id: 'settings', label: 'Settings', icon: 'settings', href: '/admin/inventory', description: 'Inventory preferences and configuration.' },
  { id: 'help', label: 'Help', icon: 'help-circle', href: '/admin/inventory', description: 'Documentation and support.' },
];

export const INVENTORY_STATUSES: { value: InventoryStatus; label: string; variant: 'success' | 'warning' | 'danger' | 'info' | 'neutral' | 'default' }[] = [
  { value: 'active', label: 'Active', variant: 'success' },
  { value: 'inactive', label: 'Inactive', variant: 'neutral' },
  { value: 'archived', label: 'Archived', variant: 'neutral' },
  { value: 'pending', label: 'Pending', variant: 'warning' },
  { value: 'draft', label: 'Draft', variant: 'info' },
  { value: 'verified', label: 'Verified', variant: 'success' },
  { value: 'future_approval', label: 'Approval', variant: 'info' },
];

export const STOCK_STATUSES: { value: StockStatus; label: string; variant: 'success' | 'warning' | 'danger' | 'info' | 'neutral' | 'default' }[] = [
  { value: 'available', label: 'Available', variant: 'success' },
  { value: 'reserved', label: 'Reserved', variant: 'info' },
  { value: 'incoming', label: 'Incoming', variant: 'info' },
  { value: 'low', label: 'Low', variant: 'warning' },
  { value: 'out_of_stock', label: 'Out of Stock', variant: 'danger' },
  { value: 'damaged', label: 'Damaged', variant: 'danger' },
  { value: 'expired', label: 'Expired', variant: 'danger' },
  { value: 'future', label: 'Future', variant: 'neutral' },
];

export const INVENTORY_UNITS: { value: InventoryUnit; label: string; futureConversion?: boolean }[] = [
  { value: 'kg', label: 'Kilograms' },
  { value: 'g', label: 'Grams' },
  { value: 'pieces', label: 'Pieces' },
  { value: 'boxes', label: 'Boxes' },
  { value: 'packets', label: 'Packets' },
  { value: 'bundles', label: 'Bundles' },
  { value: 'litres', label: 'Litres' },
  { value: 'millilitres', label: 'Millilitres' },
  { value: 'custom', label: 'Custom Units', futureConversion: true },
];

export const INVENTORY_ROLES: InventoryRole[] = [
  'viewer',
  'inventory_operator',
  'inventory_manager',
  'warehouse_manager',
  'administrator',
];

export const INVENTORY_PERMISSIONS: InventoryPermission[] = [
  'view',
  'create',
  'edit',
  'archive',
  'restore',
  'settings',
  'reports',
  'analytics',
  'transactions_future',
];

export const INVENTORY_ROLE_PERMISSIONS: Record<InventoryRole, InventoryPermission[]> = {
  viewer: ['view', 'analytics'],
  inventory_operator: ['view', 'create', 'edit', 'analytics', 'reports'],
  inventory_manager: ['view', 'create', 'edit', 'archive', 'restore', 'settings', 'reports', 'analytics'],
  warehouse_manager: ['view', 'create', 'edit', 'archive', 'reports'],
  administrator: ['view', 'create', 'edit', 'archive', 'restore', 'settings', 'reports', 'analytics', 'transactions_future'],
};

export const INVENTORY_MOCK_METRICS: InventoryMetric[] = [
  { id: 'total_items', title: 'Total Inventory Items', value: '12,480', trend: 'up', percentage: 4.2, comparison: 'vs last month', icon: 'package', color: 'var(--color-primary)' },
  { id: 'warehouses', title: 'Warehouses', value: '8', trend: 'flat', percentage: 0, comparison: 'stable', icon: 'home', color: 'var(--color-info)' },
  { id: 'available_stock', title: 'Available Stock', value: '1.04M', trend: 'up', percentage: 2.1, comparison: 'vs last week', icon: 'box', color: 'var(--color-success)' },
  { id: 'reserved_stock', title: 'Reserved Stock', value: '86,200', trend: 'up', percentage: 1.4, comparison: 'vs last week', icon: 'lock', color: 'var(--color-info)' },
  { id: 'incoming_stock', title: 'Incoming Stock', value: '42,900', trend: 'up', percentage: 8.7, comparison: 'vs last week', icon: 'truck', color: 'var(--color-info)' },
  { id: 'low_stock', title: 'Low Stock', value: '318', trend: 'up', percentage: 6.3, comparison: 'needs attention', icon: 'alert-triangle', color: 'var(--color-warning)' },
  { id: 'out_of_stock', title: 'Out of Stock', value: '47', trend: 'down', percentage: 3.1, comparison: 'vs last week', icon: 'x-circle', color: 'var(--color-danger)' },
  { id: 'expired_items', title: 'Expired Items', value: '12', trend: 'down', percentage: 1.0, comparison: 'vs last week', icon: 'clock', color: 'var(--color-danger)' },
  { id: 'damaged_items', title: 'Damaged Items', value: '29', trend: 'flat', percentage: 0, comparison: 'stable', icon: 'alert-octagon', color: 'var(--color-danger)' },
  { id: 'transfers', title: 'Transfers', value: '156', trend: 'up', percentage: 5.5, comparison: 'this month', icon: 'git-branch', color: 'var(--color-primary)' },
  { id: 'pending_receipts', title: 'Pending Receipts', value: '73', trend: 'up', percentage: 4.8, comparison: 'awaiting', icon: 'download', color: 'var(--color-warning)' },
];

export const INVENTORY_HEALTH: HealthMetric[] = [
  { id: 'availability', label: 'Availability', score: 92 },
  { id: 'accuracy', label: 'Accuracy', score: 97 },
  { id: 'turnover', label: 'Turnover', score: 81 },
  { id: 'expiry_control', label: 'Expiry Control', score: 88 },
];

export const INVENTORY_STATUS_COUNTS: StatusCount[] = [
  { id: 'low', label: 'Low Stock', count: 318, variant: 'warning' },
  { id: 'out', label: 'Out of Stock', count: 47, variant: 'danger' },
  { id: 'expired', label: 'Expired', count: 12, variant: 'danger' },
  { id: 'damaged', label: 'Damaged', count: 29, variant: 'danger' },
];

export const INVENTORY_RECENT_ACTIVITIES: RecentActivity[] = [
  { id: 'a1', icon: 'package', text: 'Inventory item INV-2041 updated', timestamp: '2m ago' },
  { id: 'a2', icon: 'truck', text: 'Inbound shipment WH-3 received', timestamp: '14m ago' },
  { id: 'a3', icon: 'git-branch', text: 'Transfer TR-882 completed', timestamp: '38m ago' },
  { id: 'a4', icon: 'alert-triangle', text: 'Low stock alert: SKU-7741', timestamp: '1h ago' },
  { id: 'a5', icon: 'check-circle', text: 'Cycle count validated WH-1', timestamp: '3h ago' },
];

export const INVENTORY_QUICK_ACTIONS: QuickAction[] = [
  { id: 'new_item', label: 'New Item', icon: 'plus', href: '/admin/inventory' },
  { id: 'receive', label: 'Receive Stock', icon: 'download', href: '/admin/inventory' },
  { id: 'transfer', label: 'Transfer', icon: 'git-branch', href: '/admin/inventory' },
  { id: 'adjust', label: 'Adjust', icon: 'sliders', href: '/admin/inventory' },
  { id: 'new_warehouse', label: 'New Warehouse', icon: 'home', href: '/admin/inventory' },
  { id: 'validate', label: 'Run Validation', icon: 'check-circle', href: '/admin/inventory' },
];

export const INVENTORY_FILTER_OPTIONS: InventoryFilterOption[] = [
  {
    id: 'warehouse', label: 'Warehouse', multi: true,
    options: [
      { value: 'wh-1', label: 'Warehouse A' }, { value: 'wh-2', label: 'Warehouse B' },
      { value: 'wh-3', label: 'Warehouse C' }, { value: 'wh-4', label: 'Warehouse D' },
    ],
  },
  {
    id: 'stockStatus', label: 'Stock Status', multi: true,
    options: STOCK_STATUSES.map((s) => ({ value: s.value, label: s.label })),
  },
  {
    id: 'inventoryStatus', label: 'Inventory Status', multi: true,
    options: INVENTORY_STATUSES.map((s) => ({ value: s.value, label: s.label })),
  },
  {
    id: 'category', label: 'Category', multi: true,
    options: [
      { value: 'seeds', label: 'Seeds' }, { value: 'fertilizer', label: 'Fertilizer' },
      { value: 'tools', label: 'Tools' }, { value: 'packaging', label: 'Packaging' },
    ],
  },
  {
    id: 'brand', label: 'Brand', multi: true,
    options: [
      { value: 'sporekart', label: 'SporeKart' }, { value: 'agrigrow', label: 'AgriGrow' },
      { value: 'greenline', label: 'GreenLine' },
    ],
  },
  {
    id: 'supplier', label: 'Supplier', multi: false, placeholder: true,
    options: [{ value: 'tbd', label: 'Coming soon' }],
  },
  {
    id: 'location', label: 'Location', multi: false,
    options: [
      { value: 'aisle-a', label: 'Aisle A' }, { value: 'aisle-b', label: 'Aisle B' },
      { value: 'aisle-c', label: 'Aisle C' },
    ],
  },
  {
    id: 'batchStatus', label: 'Batch Status', multi: true,
    options: [
      { value: 'fresh', label: 'Fresh' }, { value: 'near_expiry', label: 'Near Expiry' },
      { value: 'expired', label: 'Expired' },
    ],
  },
  {
    id: 'savedFilters', label: 'Saved Filters', multi: true,
    options: [
      { value: 'my-low', label: 'My Low Stock' }, { value: 'qc-flagged', label: 'QC Flagged' },
    ],
  },
];

export const SMART_FILTER_FUTURE = { id: 'smart', label: 'Smart Filters (coming soon)', placeholder: true };

export const INVENTORY_SAVED_FILTERS: { id: string; label: string; description?: string }[] = [
  { id: 'my-low', label: 'My Low Stock', description: 'Items below reorder point assigned to you.' },
  { id: 'qc-flagged', label: 'QC Flagged', description: 'Items flagged during quality control.' },
  { id: 'near-expiry', label: 'Near Expiry', description: 'Batches approaching expiry window.' },
  { id: 'inbound-today', label: 'Inbound Today', description: 'Receipts expected today.' },
];

export const INVENTORY_SEARCH_FIELDS: InventorySearchField[] = [
  { id: 'name', label: 'Product Name' },
  { id: 'sku', label: 'SKU' },
  { id: 'warehouse', label: 'Warehouse' },
  { id: 'batch', label: 'Batch' },
  { id: 'category', label: 'Category' },
  { id: 'brand', label: 'Brand' },
  { id: 'status', label: 'Status' },
  { id: 'location', label: 'Location' },
  { id: 'inventory_id', label: 'Inventory ID' },
  { id: 'barcode', label: 'Future Barcode', placeholder: true },
  { id: 'qr', label: 'Future QR', placeholder: true },
];

export const INVENTORY_EMPTY_STATES: Record<string, EmptyStateConfig> = {
  no_inventory: { key: 'no_inventory', title: 'No Inventory', message: 'Inventory items have not been configured yet. Add your first item to get started.', icon: 'package', actionLabel: 'Add Item' },
  no_warehouses: { key: 'no_warehouses', title: 'No Warehouses', message: 'No warehouses are defined. Create a warehouse to begin tracking stock.', icon: 'home', actionLabel: 'New Warehouse' },
  no_storage: { key: 'no_storage', title: 'No Storage', message: 'No storage hierarchy has been configured for this warehouse.', icon: 'layers', actionLabel: 'Add Storage' },
  no_zones: { key: 'no_zones', title: 'No Zones', message: 'No zones are defined. Add a receiving, storage or packing zone to continue.', icon: 'grid', actionLabel: 'New Zone' },
  no_results: { key: 'no_results', title: 'No Results', message: 'No inventory matches your current search and filters.', icon: 'search', actionLabel: 'Clear Filters' },
  permission_denied: { key: 'permission_denied', title: 'Permission Denied', message: 'You do not have access to this inventory area. Contact an administrator for access.', icon: 'lock', actionLabel: 'Request Access' },
  no_permission: { key: 'no_permission', title: 'Permission Denied', message: 'You do not have access to this area with the current role. Switch role to preview gated features.', icon: 'lock', actionLabel: 'Switch Role' },
  offline: { key: 'offline', title: 'You are Offline', message: 'Inventory data cannot be synced while offline. Reconnect to continue.', icon: 'wifi-off', actionLabel: 'Retry' },
  maintenance: { key: 'maintenance', title: 'Under Maintenance', message: 'The inventory module is temporarily under maintenance.', icon: 'tool', actionLabel: 'View Status' },
  configuration_required: { key: 'configuration_required', title: 'Configuration Required', message: 'Complete the inventory setup to enable this section.', icon: 'settings', actionLabel: 'Configure' },
  mock_data_missing: { key: 'mock_data_missing', title: 'Mock Data Missing', message: 'Mock inventory data is not available in this environment.', icon: 'database', actionLabel: 'Reload' },
};

export const INVENTORY_SETTINGS_SECTIONS: SettingsSection[] = [
  { id: 'general', label: 'General', description: 'Organization and regional inventory defaults.', icon: 'settings' },
  { id: 'inventory_preferences', label: 'Inventory Preferences', description: 'Display and default behaviour for inventory.', icon: 'sliders' },
  { id: 'warehouse_preferences', label: 'Warehouse Preferences', description: 'Default warehouse and storage rules.', icon: 'home' },
  { id: 'stock_preferences', label: 'Stock Preferences', description: 'Reorder points, safety stock and thresholds.', icon: 'box' },
  { id: 'units', label: 'Units', description: 'Measurement units and conversions.', icon: 'ruler' },
  { id: 'measurement', label: 'Measurement', description: 'Dimension and weight capture.', icon: 'move' },
  { id: 'notifications', label: 'Notifications', description: 'Alerts and notifications.', icon: 'bell', placeholder: true },
  { id: 'security', label: 'Security', description: 'Access and security controls.', icon: 'lock', placeholder: true },
  { id: 'audit', label: 'Audit', description: 'Audit logging and retention.', icon: 'file-text', placeholder: true },
  { id: 'integrations', label: 'Future Integrations', description: 'ERP, WMS and 3PL connectors.', icon: 'link', placeholder: true },
];

export const DEFAULT_FILTER_STATE = {
  warehouse: [],
  stockStatus: [],
  inventoryStatus: [],
  category: [],
  brand: [],
  supplier: [],
  location: [],
  batchStatus: [],
  savedFilters: [],
};
