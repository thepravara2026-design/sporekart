import type {
  InventoryItemSection,
  ClassificationType,
  LifecycleStage,
  InventoryItemRole,
  InventoryItemPermission,
  InventoryItemMetric,
  HealthMetric,
  StatusCount,
  QuickAction,
  RecentActivity,
  InventoryItemFilterOption,
  InventoryItemSearchField,
  InventoryItemFilterState,
  EmptyStateConfig,
  SettingsSection,
  UnitConfig,
  ClassificationGrade,
} from './types';

export const INVENTORY_ITEM_SECTIONS: InventoryItemSection[] = [
  { id: 'overview', label: 'Overview', icon: 'layout', description: 'Inventory item domain overview and quick access.' },
  { id: 'items', label: 'Inventory Items', icon: 'package', description: 'Item registry — search, filter, view and manage.' },
  { id: 'products', label: 'Products', icon: 'shopping-bag', description: 'Product to inventory item mapping.' },
  { id: 'variants', label: 'Variants', icon: 'layers', description: 'Variant to inventory item mapping.' },
  { id: 'sku', label: 'SKU Mapping', icon: 'hash', description: 'SKU association with inventory items.' },
  { id: 'units', label: 'Units', icon: 'ruler', description: 'Measurement units and configurations.' },
  { id: 'classification', label: 'Classification', icon: 'bookmark', description: 'Item type, grade and taxonomy assignments.' },
  { id: 'lifecycle', label: 'Lifecycle', icon: 'activity', description: 'Item lifecycle stages and events.' },
  { id: 'validation', label: 'Validation', icon: 'check-circle', description: 'Data validation and quality checks.' },
  { id: 'reports', label: 'Reports', icon: 'file-text', description: 'Inventory item operational reports.' },
  { id: 'history', label: 'History', icon: 'clock', description: 'Audit log of item changes.' },
  { id: 'settings', label: 'Settings', icon: 'settings', description: 'Inventory item preferences and configuration.' },
];

export const CLASSIFICATION_TYPES: { value: ClassificationType; label: string; description: string }[] = [
  { value: 'raw_material', label: 'Raw Material', description: 'Unprocessed mushroom cultivation inputs' },
  { value: 'work_in_progress', label: 'Work in Progress', description: 'Materials in production or spawning phase' },
  { value: 'finished_good', label: 'Finished Good', description: 'Ready for sale or distribution' },
  { value: 'consumable', label: 'Consumable', description: 'Disposable supplies and consumables' },
  { value: 'asset', label: 'Asset', description: 'Long-term capital equipment and fixtures' },
  { value: 'packaging', label: 'Packaging', description: 'Packing materials, labels and containers' },
  { value: 'supplies', label: 'Supplies', description: 'General supplies and maintenance items' },
];

export const CLASSIFICATION_GRADES: { value: ClassificationGrade; label: string; description: string }[] = [
  { value: 'a_plus', label: 'A+', description: 'Premium grade — highest quality' },
  { value: 'a', label: 'A', description: 'Standard grade — meets all specifications' },
  { value: 'b', label: 'B', description: 'Economy grade — minor deviations acceptable' },
  { value: 'c', label: 'C', description: 'Utility grade — for processing or discounted channels' },
  { value: 'unclassified', label: 'Unclassified', description: 'Grade not yet assigned' },
];

export const LIFECYCLE_STAGES: { value: LifecycleStage; label: string; variant: 'success' | 'warning' | 'danger' | 'info' | 'neutral' | 'default'; description: string }[] = [
  { value: 'draft', label: 'Draft', variant: 'neutral', description: 'Item record created but not yet activated' },
  { value: 'pending_approval', label: 'Pending Approval', variant: 'warning', description: 'Awaiting review and approval' },
  { value: 'approved', label: 'Approved', variant: 'info', description: 'Approved for operations' },
  { value: 'active', label: 'Active', variant: 'success', description: 'Item is live in inventory operations' },
  { value: 'frozen', label: 'Frozen', variant: 'info', description: 'Temporarily frozen — no transactions allowed' },
  { value: 'suspended', label: 'Suspended', variant: 'warning', description: 'Suspended due to quality or compliance issues' },
  { value: 'discontinued', label: 'Discontinued', variant: 'default', description: 'No longer produced or procured' },
  { value: 'archived', label: 'Archived', variant: 'neutral', description: 'Historical record — no further transactions' },
];

export const INVENTORY_ITEM_ROLES: InventoryItemRole[] = [
  'viewer',
  'inventory_operator',
  'inventory_manager',
  'warehouse_manager',
  'administrator',
];

export const INVENTORY_ITEM_PERMISSIONS: InventoryItemPermission[] = [
  'view',
  'create',
  'edit',
  'archive',
  'restore',
  'bulk_operations',
  'reports',
  'settings',
];

export const INVENTORY_ITEM_ROLE_PERMISSIONS: Record<InventoryItemRole, InventoryItemPermission[]> = {
  viewer: ['view'],
  inventory_operator: ['view', 'create', 'edit', 'reports'],
  inventory_manager: ['view', 'create', 'edit', 'archive', 'restore', 'bulk_operations', 'reports', 'settings'],
  warehouse_manager: ['view', 'create', 'edit', 'archive', 'reports'],
  administrator: ['view', 'create', 'edit', 'archive', 'restore', 'bulk_operations', 'reports', 'settings'],
};

export const INVENTORY_ITEM_MOCK_METRICS: InventoryItemMetric[] = [
  { id: 'total_items', title: 'Total Inventory Items', value: '14,820', trend: 'up', percentage: 6.3, comparison: 'vs last month', icon: 'package', color: 'var(--color-primary)' },
  { id: 'mapped_products', title: 'Mapped Products', value: '3,450', trend: 'up', percentage: 2.4, comparison: 'vs last month', icon: 'shopping-bag', color: 'var(--color-info)' },
  { id: 'variants', title: 'Variants', value: '8,120', trend: 'up', percentage: 4.7, comparison: 'vs last month', icon: 'layers', color: 'var(--color-info)' },
  { id: 'active_sku', title: 'Active SKUs', value: '12,400', trend: 'up', percentage: 3.2, comparison: 'vs last month', icon: 'hash', color: 'var(--color-success)' },
  { id: 'classified', title: 'Classified Items', value: '14,200', trend: 'up', percentage: 1.8, comparison: '95.8% coverage', icon: 'bookmark', color: 'var(--color-success)' },
  { id: 'active_lifecycle', title: 'Active Lifecycle', value: '11,340', trend: 'flat', percentage: 0, comparison: '76.5% of total', icon: 'activity', color: 'var(--color-info)' },
  { id: 'draft_items', title: 'Draft Items', value: '420', trend: 'down', percentage: 8.3, comparison: 'needs attention', icon: 'edit', color: 'var(--color-warning)' },
  { id: 'discontinued', title: 'Discontinued', value: '890', trend: 'up', percentage: 5.1, comparison: 'vs last month', icon: 'archive', color: 'var(--color-neutral)' },
  { id: 'frozen_items', title: 'Frozen Items', value: '67', trend: 'down', percentage: 12.0, comparison: 'resolving', icon: 'snowflake', color: 'var(--color-info)' },
  { id: 'validation_issues', title: 'Validation Issues', value: '23', trend: 'down', percentage: 15.0, comparison: 'needs review', icon: 'alert-triangle', color: 'var(--color-danger)' },
];

export const INVENTORY_ITEM_HEALTH: HealthMetric[] = [
  { id: 'mapping', label: 'Product Mapping', score: 98 },
  { id: 'classification', label: 'Classification', score: 91 },
  { id: 'lifecycle', label: 'Lifecycle Compliance', score: 87 },
  { id: 'validation', label: 'Data Validation', score: 93 },
];

export const INVENTORY_ITEM_STATUS_COUNTS: StatusCount[] = [
  { id: 'active', label: 'Active', count: 11340, variant: 'success' },
  { id: 'draft', label: 'Draft', count: 420, variant: 'warning' },
  { id: 'frozen', label: 'Frozen', count: 67, variant: 'info' },
  { id: 'discontinued', label: 'Discontinued', count: 890, variant: 'neutral' },
  { id: 'archived', label: 'Archived', count: 2103, variant: 'neutral' },
];

export const INVENTORY_ITEM_RECENT_ACTIVITIES: RecentActivity[] = [
  { id: 'i1', icon: 'package', text: 'Item INV-8241 lifecycle changed to Active', timestamp: '1m ago' },
  { id: 'i2', icon: 'bookmark', text: 'Classification updated for spawn product line', timestamp: '12m ago' },
  { id: 'i3', icon: 'hash', text: 'SKU map SKU-4421 linked to item INV-2291', timestamp: '31m ago' },
  { id: 'i4', icon: 'shopping-bag', text: 'Product PRD-312 mapped to 14 inventory items', timestamp: '55m ago' },
  { id: 'i5', icon: 'check-circle', text: 'Bulk validation passed for 230 items', timestamp: '2h ago' },
];

export const INVENTORY_ITEM_QUICK_ACTIONS: QuickAction[] = [
  { id: 'new_item', label: 'New Inventory Item', icon: 'plus', href: '/admin/inventory-items', permission: 'create' },
  { id: 'map_product', label: 'Map Product', icon: 'shopping-bag', href: '/admin/inventory-items', permission: 'create' },
  { id: 'bulk_classify', label: 'Bulk Classify', icon: 'bookmark', href: '/admin/inventory-items', permission: 'bulk_operations' },
  { id: 'bulk_update_lifecycle', label: 'Bulk Lifecycle Update', icon: 'activity', href: '/admin/inventory-items', permission: 'bulk_operations' },
  { id: 'validate_all', label: 'Run Validation', icon: 'check-circle', href: '/admin/inventory-items', permission: 'edit' },
  { id: 'export_report', label: 'Export Report', icon: 'file-text', href: '/admin/inventory-items', permission: 'reports' },
];

export const INVENTORY_ITEM_FILTER_OPTIONS: InventoryItemFilterOption[] = [
  {
    id: 'status', label: 'Status', multi: true,
    options: [
      { value: 'active', label: 'Active' }, { value: 'inactive', label: 'Inactive' },
      { value: 'archived', label: 'Archived' }, { value: 'pending', label: 'Pending' },
      { value: 'draft', label: 'Draft' }, { value: 'verified', label: 'Verified' },
    ],
  },
  {
    id: 'classification', label: 'Classification', multi: true,
    options: CLASSIFICATION_TYPES.map((c) => ({ value: c.value, label: c.label })),
  },
  {
    id: 'lifecycle', label: 'Lifecycle Stage', multi: true,
    options: LIFECYCLE_STAGES.map((l) => ({ value: l.value, label: l.label })),
  },
  {
    id: 'category', label: 'Category', multi: true,
    options: [
      { value: 'spawn', label: 'Spawn' }, { value: 'compost', label: 'Compost' },
      { value: 'supplements', label: 'Supplements' }, { value: 'tools', label: 'Tools' },
      { value: 'packaging', label: 'Packaging' }, { value: 'consumables', label: 'Consumables' },
    ],
  },
  {
    id: 'brand', label: 'Brand', multi: true,
    options: [
      { value: 'sporekart', label: 'SporeKart' }, { value: 'agrigrow', label: 'AgriGrow' },
      { value: 'greenline', label: 'GreenLine' }, { value: 'mycelium', label: 'Mycelium Pro' },
    ],
  },
  {
    id: 'unit', label: 'Unit', multi: true,
    options: [
      { value: 'kg', label: 'Kilograms' }, { value: 'g', label: 'Grams' },
      { value: 'pieces', label: 'Pieces' }, { value: 'boxes', label: 'Boxes' },
      { value: 'packets', label: 'Packets' },
    ],
  },
  {
    id: 'grade', label: 'Grade', multi: true,
    options: CLASSIFICATION_GRADES.map((g) => ({ value: g.value, label: g.label })),
  },
];

export const INVENTORY_ITEM_SEARCH_FIELDS: InventoryItemSearchField[] = [
  { id: 'name', label: 'Item Name' },
  { id: 'code', label: 'Item Code' },
  { id: 'sku', label: 'SKU' },
  { id: 'productName', label: 'Product Name' },
  { id: 'variantName', label: 'Variant Name' },
  { id: 'category', label: 'Category' },
  { id: 'brand', label: 'Brand' },
  { id: 'status', label: 'Status' },
];

export const INVENTORY_ITEM_EMPTY_STATES: Record<string, EmptyStateConfig> = {
  no_items: { key: 'no_items', title: 'No Inventory Items', message: 'No inventory items have been created yet. Map products or create items to get started.', icon: 'package', actionLabel: 'Create Item' },
  no_products: { key: 'no_products', title: 'No Products Mapped', message: 'No products have been mapped to inventory items yet. Start mapping to link product data.', icon: 'shopping-bag', actionLabel: 'Map Product' },
  no_variants: { key: 'no_variants', title: 'No Variants Mapped', message: 'No variants are linked to inventory items. Map variants to enable SKU-level tracking.', icon: 'layers', actionLabel: 'Map Variant' },
  no_sku: { key: 'no_sku', title: 'No SKU Associations', message: 'No SKU associations exist. Link SKUs to inventory items for granular tracking.', icon: 'hash', actionLabel: 'Associate SKU' },
  no_results: { key: 'no_results', title: 'No Results', message: 'No inventory items match your current search and filters.', icon: 'search', actionLabel: 'Clear Filters' },
  permission_denied: { key: 'permission_denied', title: 'Permission Denied', message: 'You do not have access to this area. Contact an administrator for access.', icon: 'lock', actionLabel: 'Request Access' },
  offline: { key: 'offline', title: 'You are Offline', message: 'Data cannot be loaded while offline. Reconnect to continue.', icon: 'wifi-off', actionLabel: 'Retry' },
  maintenance: { key: 'maintenance', title: 'Under Maintenance', message: 'The inventory items module is temporarily under maintenance.', icon: 'tool', actionLabel: 'View Status' },
  configuration_required: { key: 'configuration_required', title: 'Configuration Required', message: 'Complete the setup to enable this section.', icon: 'settings', actionLabel: 'Configure' },
};

export const INVENTORY_ITEM_SETTINGS_SECTIONS: SettingsSection[] = [
  { id: 'general', label: 'General', description: 'Item naming conventions and defaults.', icon: 'settings' },
  { id: 'classification_defaults', label: 'Classification Defaults', description: 'Default classification rules and auto-assignment.', icon: 'bookmark' },
  { id: 'lifecycle_rules', label: 'Lifecycle Rules', description: 'Automatic lifecycle progression and transitions.', icon: 'activity' },
  { id: 'validation_rules', label: 'Validation Rules', description: 'Data validation thresholds and requirements.', icon: 'check-circle' },
  { id: 'units', label: 'Units', description: 'Measurement units and conversion factors.', icon: 'ruler' },
  { id: 'security', label: 'Security', description: 'Access and security controls.', icon: 'lock', placeholder: true },
  { id: 'audit', label: 'Audit', description: 'Audit logging and retention.', icon: 'file-text', placeholder: true },
  { id: 'notifications', label: 'Notifications', description: 'Alerts and notifications.', icon: 'bell', placeholder: true },
  { id: 'integrations', label: 'Future Integrations', description: 'ERP, WMS and 3PL connectors.', icon: 'link', placeholder: true },
];

export const INVENTORY_ITEM_SAVED_FILTERS: { id: string; label: string; description?: string }[] = [
  { id: 'unmapped', label: 'Unmapped Items', description: 'Items not yet linked to a product.' },
  { id: 'needs-classification', label: 'Needs Classification', description: 'Items without classification assigned.' },
  { id: 'draft-pending', label: 'Draft Items', description: 'Items still in draft lifecycle stage.' },
  { id: 'recently-frozen', label: 'Recently Frozen', description: 'Items frozen in the last 7 days.' },
];

export const DEFAULT_INVENTORY_ITEM_FILTER_STATE: InventoryItemFilterState = {
  status: [],
  classification: [],
  lifecycle: [],
  category: [],
  brand: [],
  unit: [],
  grade: [],
  savedFilters: [],
};

export const INVENTORY_ITEM_UNITS: UnitConfig[] = [
  { id: 'kg', name: 'Kilogram', symbol: 'kg', category: 'weight' },
  { id: 'g', name: 'Gram', symbol: 'g', category: 'weight', baseUnit: 'kg', conversionFactor: 0.001 },
  { id: 'pieces', name: 'Pieces', symbol: 'pc', category: 'quantity' },
  { id: 'boxes', name: 'Boxes', symbol: 'bx', category: 'quantity' },
  { id: 'packets', name: 'Packets', symbol: 'pkt', category: 'quantity' },
  { id: 'bundles', name: 'Bundles', symbol: 'bdl', category: 'quantity' },
  { id: 'litres', name: 'Litres', symbol: 'L', category: 'volume' },
  { id: 'millilitres', name: 'Millilitres', symbol: 'mL', category: 'volume', baseUnit: 'litres', conversionFactor: 0.001 },
];

export const MOCK_PRODUCTS = [
  { id: 'PRD-001', name: 'Organic Oyster Spawn', code: 'OYS-SPN', type: 'spawn' },
  { id: 'PRD-002', name: 'Shiitake Sawdust Spawn', code: 'SHT-SPN', type: 'spawn' },
  { id: 'PRD-003', name: 'Button Mushroom Compost', code: 'BTN-CMP', type: 'compost' },
  { id: 'PRD-004', name: 'Premium Spawn Supplement', code: 'SPN-SUP', type: 'supplements' },
  { id: 'PRD-005', name: 'Grow Bag Kit Standard', code: 'GBK-STD', type: 'tools' },
  { id: 'PRD-006', name: 'Mushroom Harvest Box', code: 'MHB-001', type: 'packaging' },
  { id: 'PRD-007', name: 'Casing Soil Premium', code: 'CSP-001', type: 'consumables' },
  { id: 'PRD-008', name: 'pH Testing Kit', code: 'PHK-001', type: 'tools' },
  { id: 'PRD-009', name: 'Humidity Controller Pro', code: 'HCP-001', type: 'tools' },
  { id: 'PRD-010', name: 'SporeKart Label Pack', code: 'SKL-001', type: 'packaging' },
  { id: 'PRD-011', name: 'Lion\'s Mane Liquid Culture', code: 'LMC-001', type: 'spawn' },
  { id: 'PRD-012', name: 'Enoki Mushroom Substrate', code: 'EMS-001', type: 'compost' },
];

export const MOCK_VARIANTS = [
  { id: 'VAR-001', name: '1 kg Pack', productId: 'PRD-001' },
  { id: 'VAR-002', name: '5 kg Bag', productId: 'PRD-001' },
  { id: 'VAR-003', name: '10 kg Bulk', productId: 'PRD-001' },
  { id: 'VAR-004', name: '2 kg Block', productId: 'PRD-002' },
  { id: 'VAR-005', name: '5 kg Block', productId: 'PRD-002' },
  { id: 'VAR-006', name: '15 kg Tray', productId: 'PRD-003' },
  { id: 'VAR-007', name: '25 kg Tray', productId: 'PRD-003' },
  { id: 'VAR-008', name: '500 g Bottle', productId: 'PRD-004' },
  { id: 'VAR-009', name: '1 L Bottle', productId: 'PRD-004' },
  { id: 'VAR-010', name: 'Standard Kit', productId: 'PRD-005' },
  { id: 'VAR-011', name: 'Deluxe Kit', productId: 'PRD-005' },
  { id: 'VAR-012', name: 'Small Box (250 g)', productId: 'PRD-006' },
  { id: 'VAR-013', name: 'Medium Box (500 g)', productId: 'PRD-006' },
  { id: 'VAR-014', name: 'Large Box (1 kg)', productId: 'PRD-006' },
  { id: 'VAR-015', name: '5 kg Bag', productId: 'PRD-007' },
  { id: 'VAR-016', name: '10 kg Bag', productId: 'PRD-007' },
  { id: 'VAR-017', name: 'Standard Kit', productId: 'PRD-008' },
  { id: 'VAR-018', name: 'Pro Unit', productId: 'PRD-009' },
  { id: 'VAR-019', name: '250 Labels Roll', productId: 'PRD-010' },
  { id: 'VAR-020', name: '500 Labels Roll', productId: 'PRD-010' },
  { id: 'VAR-021', name: '10 ml Syringe', productId: 'PRD-011' },
  { id: 'VAR-022', name: '20 ml Syringe', productId: 'PRD-011' },
  { id: 'VAR-023', name: '5 kg Block', productId: 'PRD-012' },
  { id: 'VAR-024', name: '10 kg Block', productId: 'PRD-012' },
];
