import type {
  WarehouseSection,
  WarehouseType,
  WarehouseStatus,
  TemperatureType,
  ZoneType,
  WarehouseRole,
  WarehousePermission,
  WarehouseMetric,
  HealthMetric,
  StatusCount,
  QuickAction,
  RecentActivity,
  WarehouseFilterState,
  WarehouseFilterOption,
  WarehouseSearchField,
  SettingsSection,
} from './types';

export const WAREHOUSE_SECTIONS: WarehouseSection[] = [
  { id: 'overview', label: 'Overview', icon: 'layout', description: 'Warehouse domain overview and quick access.' },
  { id: 'warehouses', label: 'Warehouses', icon: 'home', description: 'Warehouse directory and profiles.' },
  { id: 'locations', label: 'Locations', icon: 'map-pin', description: 'Geographic warehouse locations.' },
  { id: 'zones', label: 'Zones', icon: 'grid', description: 'Receiving, storage, packing and dispatch zones.' },
  { id: 'storage', label: 'Storage', icon: 'layers', description: 'Storage hierarchy: building, floor, zone, rack, shelf, bin.' },
  { id: 'capacity', label: 'Capacity', icon: 'database', description: 'Capacity and utilization tracking.' },
  { id: 'cold_storage', label: 'Cold Storage', icon: 'snowflake', description: 'Temperature-controlled storage.' },
  { id: 'virtual_warehouses', label: 'Virtual Warehouses', icon: 'box', description: 'Logical aggregation of physical stock.' },
  { id: 'operations', label: 'Operations', icon: 'activity', description: 'Put-away, picking and replenishment.' },
  { id: 'analytics', label: 'Analytics', icon: 'trending-up', description: 'Warehouse analytics and insights.' },
  { id: 'reports', label: 'Reports', icon: 'file-text', description: 'Operational and audit reports.' },
  { id: 'settings', label: 'Settings', icon: 'settings', description: 'Warehouse preferences and configuration.' },
  { id: 'audit', label: 'Audit', icon: 'shield', description: 'Audit logging and retention.' },
  { id: 'help', label: 'Help', icon: 'help-circle', description: 'Documentation and support.' },
];

export const WAREHOUSE_TYPES: { value: WarehouseType; label: string }[] = [
  { value: 'main', label: 'Main Warehouse' },
  { value: 'distribution', label: 'Distribution Center' },
  { value: 'retail', label: 'Retail Warehouse' },
  { value: 'cold_storage', label: 'Cold Storage' },
  { value: 'processing', label: 'Processing Center' },
  { value: 'spawn_production', label: 'Spawn Production Facility' },
  { value: 'fresh_mushroom', label: 'Fresh Mushroom Facility' },
  { value: 'dry_mushroom', label: 'Dry Mushroom Storage' },
  { value: 'virtual', label: 'Virtual Warehouse' },
  { value: 'transit', label: 'Transit Warehouse' },
  { value: 'custom', label: 'Custom Warehouse Type' },
];

export const WAREHOUSE_STATUSES: { value: WarehouseStatus; label: string; variant: 'success' | 'warning' | 'danger' | 'info' | 'neutral' | 'default' }[] = [
  { value: 'active', label: 'Active', variant: 'success' },
  { value: 'inactive', label: 'Inactive', variant: 'neutral' },
  { value: 'maintenance', label: 'Maintenance', variant: 'warning' },
  { value: 'planned', label: 'Planned', variant: 'info' },
  { value: 'decommissioned', label: 'Decommissioned', variant: 'default' },
];

export const TEMPERATURE_TYPES: { value: TemperatureType; label: string }[] = [
  { value: 'ambient', label: 'Ambient' },
  { value: 'cold', label: 'Cold' },
  { value: 'frozen', label: 'Frozen' },
  { value: 'controlled', label: 'Controlled' },
];

export const ZONE_TYPES: { value: ZoneType; label: string }[] = [
  { value: 'receiving', label: 'Receiving Zone' },
  { value: 'storage', label: 'Storage Zone' },
  { value: 'packing', label: 'Packing Zone' },
  { value: 'dispatch', label: 'Dispatch Zone' },
  { value: 'returns', label: 'Returns Zone' },
  { value: 'qc', label: 'Quality Check Zone' },
  { value: 'damaged', label: 'Damaged Goods Zone' },
  { value: 'cold', label: 'Cold Storage Zone' },
  { value: 'quarantine', label: 'Quarantine Zone' },
  { value: 'custom', label: 'Custom Zone' },
];

export const WAREHOUSE_ROLES: WarehouseRole[] = [
  'viewer',
  'warehouse_operator',
  'warehouse_manager',
  'inventory_manager',
  'administrator',
];

export const WAREHOUSE_PERMISSIONS: WarehousePermission[] = [
  'view',
  'create',
  'edit',
  'archive',
  'restore',
  'settings',
  'reports',
  'analytics',
  'operations_future',
];

export const WAREHOUSE_ROLE_PERMISSIONS: Record<WarehouseRole, WarehousePermission[]> = {
  viewer: ['view', 'analytics'],
  warehouse_operator: ['view', 'create', 'edit', 'analytics', 'operations_future'],
  warehouse_manager: ['view', 'create', 'edit', 'archive', 'restore', 'settings', 'reports', 'analytics', 'operations_future'],
  inventory_manager: ['view', 'create', 'edit', 'archive', 'restore', 'settings', 'reports', 'analytics'],
  administrator: ['view', 'create', 'edit', 'archive', 'restore', 'settings', 'reports', 'analytics', 'operations_future'],
};

export const WAREHOUSE_MOCK_METRICS: WarehouseMetric[] = [
  { id: 'total', title: 'Total Warehouses', value: '12', trend: 'up', percentage: 9.1, comparison: 'vs last quarter', icon: 'home', color: 'var(--color-primary)' },
  { id: 'active', title: 'Active Warehouses', value: '9', trend: 'flat', percentage: 0, comparison: 'stable', icon: 'check-circle', color: 'var(--color-success)' },
  { id: 'inactive', title: 'Inactive Warehouses', value: '3', trend: 'down', percentage: 2.0, comparison: 'vs last month', icon: 'x-circle', color: 'var(--color-neutral)' },
  { id: 'zones', title: 'Storage Zones', value: '86', trend: 'up', percentage: 4.4, comparison: 'vs last month', icon: 'grid', color: 'var(--color-info)' },
  { id: 'racks', title: 'Rack Count', value: '1,240', trend: 'up', percentage: 3.1, comparison: 'vs last month', icon: 'columns', color: 'var(--color-info)' },
  { id: 'shelves', title: 'Shelf Count', value: '8,760', trend: 'up', percentage: 2.7, comparison: 'vs last month', icon: 'book-open', color: 'var(--color-info)' },
  { id: 'bins', title: 'Bin Count', value: '52,400', trend: 'up', percentage: 5.0, comparison: 'vs last month', icon: 'package', color: 'var(--color-info)' },
  { id: 'capacity', title: 'Storage Capacity', value: '1.2M', trend: 'up', percentage: 1.8, comparison: 'units', icon: 'database', color: 'var(--color-primary)' },
  { id: 'util', title: 'Utilization', value: '74%', trend: 'up', percentage: 3.6, comparison: 'vs last week', icon: 'trending-up', color: 'var(--color-warning)' },
  { id: 'cold', title: 'Cold Storage Units', value: '4', trend: 'flat', percentage: 0, comparison: 'stable', icon: 'snowflake', color: 'var(--color-info)' },
  { id: 'virtual', title: 'Virtual Warehouses', value: '2', trend: 'up', percentage: 12.0, comparison: 'vs last month', icon: 'box', color: 'var(--color-primary)' },
  { id: 'future', title: 'Future KPI', value: '—', trend: 'flat', percentage: 0, comparison: 'coming soon', icon: 'sparkles', color: 'var(--color-text-tertiary)' },
];

export const WAREHOUSE_HEALTH: HealthMetric[] = [
  { id: 'warehouse', label: 'Warehouse Health', score: 94 },
  { id: 'storage', label: 'Storage Health', score: 89 },
  { id: 'capacity', label: 'Capacity Usage', score: 74 },
  { id: 'maintenance', label: 'Maintenance', score: 81 },
];

export const WAREHOUSE_STATUS_COUNTS: StatusCount[] = [
  { id: 'active', label: 'Active', count: 9, variant: 'success' },
  { id: 'inactive', label: 'Inactive', count: 3, variant: 'neutral' },
  { id: 'maintenance', label: 'Maintenance', count: 2, variant: 'warning' },
  { id: 'planned', label: 'Planned', count: 1, variant: 'info' },
];

export const WAREHOUSE_RECENT_ACTIVITIES: RecentActivity[] = [
  { id: 'w1', icon: 'home', text: 'Warehouse WH-3 capacity updated', timestamp: '3m ago' },
  { id: 'w2', icon: 'grid', text: 'Zone Z-12 created in WH-1', timestamp: '21m ago' },
  { id: 'w3', icon: 'columns', text: 'Rack R-204 maintenance flagged', timestamp: '47m ago' },
  { id: 'w4', icon: 'snowflake', text: 'Cold storage temperature check passed', timestamp: '1h ago' },
  { id: 'w5', icon: 'box', text: 'Virtual warehouse VW-2 synced', timestamp: '2h ago' },
];

export const WAREHOUSE_QUICK_ACTIONS: QuickAction[] = [
  { id: 'new_warehouse', label: 'New Warehouse', icon: 'home', href: '/admin/warehouse', permission: 'create' },
  { id: 'new_zone', label: 'New Zone', icon: 'grid', href: '/admin/warehouse', permission: 'create' },
  { id: 'receive', label: 'Receive Stock', icon: 'download', href: '/admin/warehouse', permission: 'operations_future' },
  { id: 'transfer', label: 'Transfer', icon: 'git-branch', href: '/admin/warehouse', permission: 'operations_future' },
  { id: 'adjust', label: 'Adjust', icon: 'sliders', href: '/admin/warehouse', permission: 'edit' },
  { id: 'validate', label: 'Run Validation', icon: 'check-circle', href: '/admin/warehouse', permission: 'edit' },
];

export const WAREHOUSE_FILTER_OPTIONS: WarehouseFilterOption[] = [
  {
    id: 'type', label: 'Warehouse Type', multi: true,
    options: WAREHOUSE_TYPES.map((t) => ({ value: t.value, label: t.label })),
  },
  {
    id: 'status', label: 'Status', multi: true,
    options: WAREHOUSE_STATUSES.map((s) => ({ value: s.value, label: s.label })),
  },
  {
    id: 'location', label: 'Location', multi: true,
    options: [
      { value: 'mumbai', label: 'Mumbai' }, { value: 'delhi', label: 'Delhi' },
      { value: 'pune', label: 'Pune' }, { value: 'bengaluru', label: 'Bengaluru' },
    ],
  },
  {
    id: 'temperatureType', label: 'Storage Type', multi: true,
    options: TEMPERATURE_TYPES.map((t) => ({ value: t.value, label: t.label })),
  },
  {
    id: 'zone', label: 'Zone', multi: false, placeholder: true,
    options: [{ value: 'tbd', label: 'Coming soon' }],
  },
  {
    id: 'capacity', label: 'Capacity', multi: false,
    options: [
      { value: 'lt_50', label: 'Under 50%' }, { value: '50_80', label: '50–80%' },
      { value: 'gt_80', label: 'Over 80%' },
    ],
  },
];

export const WAREHOUSE_SEARCH_FIELDS: WarehouseSearchField[] = [
  { id: 'name', label: 'Warehouse Name' },
  { id: 'code', label: 'Warehouse Code' },
  { id: 'location', label: 'Location' },
  { id: 'status', label: 'Status' },
  { id: 'type', label: 'Type' },
  { id: 'manager', label: 'Manager' },
  { id: 'zone', label: 'Zone', placeholder: true },
  { id: 'rack', label: 'Rack', placeholder: true },
  { id: 'shelf', label: 'Shelf', placeholder: true },
  { id: 'bin', label: 'Bin', placeholder: true },
];

export const WAREHOUSE_SETTINGS_SECTIONS: SettingsSection[] = [
  { id: 'general', label: 'General', description: 'Organization and regional warehouse defaults.', icon: 'settings' },
  { id: 'warehouse_preferences', label: 'Warehouse Preferences', description: 'Default warehouse and storage rules.', icon: 'home' },
  { id: 'storage_preferences', label: 'Storage Preferences', description: 'Hierarchy and binning behaviour.', icon: 'layers' },
  { id: 'capacity_rules', label: 'Capacity Rules', description: 'Utilization thresholds and alerts.', icon: 'database' },
  { id: 'units', label: 'Units', description: 'Measurement units and conversions.', icon: 'ruler' },
  { id: 'security', label: 'Security', description: 'Access and security controls.', icon: 'lock', placeholder: true },
  { id: 'audit', label: 'Audit', description: 'Audit logging and retention.', icon: 'shield', placeholder: true },
  { id: 'notification', label: 'Notifications', description: 'Alerts and notifications.', icon: 'bell', placeholder: true },
  { id: 'integrations', label: 'Future Integrations', description: 'WMS, ERP and 3PL connectors.', icon: 'link', placeholder: true },
];

export const WAREHOUSE_SAVED_FILTERS: { id: string; label: string; description?: string }[] = [
  { id: 'active-cold', label: 'Active Cold Storage', description: 'Active cold storage warehouses.' },
  { id: 'low-util', label: 'Low Utilization', description: 'Warehouses under 50% capacity.' },
  { id: 'maintenance', label: 'In Maintenance', description: 'Warehouses flagged for maintenance.' },
];

export const DEFAULT_WAREHOUSE_FILTER_STATE: WarehouseFilterState = {
  type: [],
  location: [],
  status: [],
  temperatureType: [],
  zone: [],
  capacity: [],
};

export const WAREHOUSE_UNITS: { value: string; label: string; futureConversion?: boolean }[] = [
  { value: 'pallet', label: 'Pallets' },
  { value: 'carton', label: 'Cartons' },
  { value: 'bin', label: 'Bins' },
  { value: 'kg', label: 'Kilograms' },
  { value: 'custom', label: 'Custom Units', futureConversion: true },
];


