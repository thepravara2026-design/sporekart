import type { PermissionConfig } from './types';

export const DEFAULT_PERMISSION_CONFIG: PermissionConfig[] = [
  {
    role: 'super_admin',
    grants: {
      dashboard: ['view', 'export'],
      orders: ['view', 'create', 'update', 'delete', 'export', 'import', 'approve', 'publish', 'archive', 'bulk_actions'],
      products: ['view', 'create', 'update', 'delete', 'export', 'import', 'approve', 'publish', 'archive', 'bulk_actions'],
      customers: ['view', 'create', 'update', 'delete', 'export', 'import', 'approve', 'bulk_actions'],
      users: ['view', 'create', 'update', 'delete', 'export', 'approve', 'bulk_actions'],
      settings: ['view', 'create', 'update', 'delete'],
      reports: ['view', 'create', 'export', 'import', 'bulk_actions'],
      audit: ['view', 'export'],
    },
  },
  {
    role: 'administrator',
    grants: {
      dashboard: ['view', 'export'],
      orders: ['view', 'create', 'update', 'export', 'approve', 'bulk_actions'],
      products: ['view', 'create', 'update', 'export', 'import', 'publish', 'archive', 'bulk_actions'],
      customers: ['view', 'create', 'update', 'export', 'bulk_actions'],
      users: ['view', 'create', 'update', 'export'],
      settings: ['view', 'update'],
      reports: ['view', 'create', 'export', 'bulk_actions'],
      audit: ['view'],
    },
  },
  {
    role: 'manager',
    grants: {
      dashboard: ['view'],
      orders: ['view', 'create', 'update', 'export', 'approve'],
      products: ['view', 'create', 'update', 'export'],
      customers: ['view', 'create', 'update', 'export'],
      reports: ['view', 'create', 'export'],
    },
  },
  {
    role: 'inventory_manager',
    grants: {
      dashboard: ['view'],
      products: ['view', 'update', 'export', 'import'],
      orders: ['view'],
      reports: ['view', 'export'],
    },
  },
  {
    role: 'viewer',
    grants: {
      dashboard: ['view'],
      orders: ['view'],
      products: ['view'],
      customers: ['view'],
      reports: ['view'],
    },
  },
];
