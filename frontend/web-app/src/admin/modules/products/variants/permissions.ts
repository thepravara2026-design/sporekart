import type { VariantRole } from './types';

export const CURRENT_VARIANT_ROLE: VariantRole = 'administrator';

export const VARIANT_ROLE_HIERARCHY: VariantRole[] = ['viewer', 'editor', 'manager', 'administrator'];

export const VARIANT_PERMISSIONS: Record<VariantRole, string[]> = {
  viewer: ['view'],
  editor: ['view', 'create', 'edit'],
  manager: ['view', 'create', 'edit', 'delete', 'duplicate', 'archive', 'restore'],
  administrator: ['view', 'create', 'edit', 'delete', 'duplicate', 'archive', 'restore', 'bulk_operations'],
};

export function canVariant(role: VariantRole, ...permissions: string[]): boolean {
  const userPerms = VARIANT_PERMISSIONS[role];
  return permissions.every((p) => userPerms.includes(p));
}

export function hasVariantRole(userRole: VariantRole, requiredRole: VariantRole): boolean {
  return VARIANT_ROLE_HIERARCHY.indexOf(userRole) >= VARIANT_ROLE_HIERARCHY.indexOf(requiredRole);
}
