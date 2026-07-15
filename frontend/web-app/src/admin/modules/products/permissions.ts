import type { ProductPermissions } from './types';

export const PRODUCT_ROLES = ['viewer', 'editor', 'manager', 'administrator'] as const;

export type ProductRole = (typeof PRODUCT_ROLES)[number];

export type ProductPermissionMatrix = Record<ProductRole, ProductPermissions[]>;

export const PRODUCT_PERMISSION_MATRIX: ProductPermissionMatrix = {
  viewer: ['view', 'export'],
  editor: ['view', 'export', 'create', 'update'],
  manager: ['view', 'export', 'create', 'update', 'delete', 'publish', 'archive', 'approve', 'restore', 'import', 'bulk_actions'],
  administrator: ['view', 'export', 'create', 'update', 'delete', 'publish', 'archive', 'approve', 'restore', 'import', 'bulk_actions'],
};

export function canProduct(role: ProductRole, action: ProductPermissions): boolean {
  const grants = PRODUCT_PERMISSION_MATRIX[role];
  if (!grants) return false;
  return grants.includes(action);
}
