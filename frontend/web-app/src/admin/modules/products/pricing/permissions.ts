import type { PricingRole } from './types';

export const CURRENT_PRICING_ROLE: PricingRole = 'administrator';

export const PRICING_ROLE_HIERARCHY: PricingRole[] = ['viewer', 'pricing_editor', 'manager', 'finance', 'administrator'];

export const PRICING_PERMISSIONS: Record<PricingRole, string[]> = {
  viewer: ['view'],
  pricing_editor: ['view', 'edit', 'schedule'],
  manager: ['view', 'edit', 'approve', 'schedule', 'bulk_update'],
  finance: ['view', 'edit', 'approve', 'archive', 'restore'],
  administrator: ['view', 'edit', 'approve', 'schedule', 'bulk_update', 'archive', 'restore'],
};

export function canPricing(role: PricingRole, ...permissions: string[]): boolean {
  const userPerms = PRICING_PERMISSIONS[role];
  return permissions.every((p) => userPerms.includes(p));
}

export function hasRole(userRole: PricingRole, requiredRole: PricingRole): boolean {
  return PRICING_ROLE_HIERARCHY.indexOf(userRole) >= PRICING_ROLE_HIERARCHY.indexOf(requiredRole);
}
