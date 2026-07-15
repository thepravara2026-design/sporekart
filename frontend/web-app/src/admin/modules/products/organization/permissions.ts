import type { OrgPermissions, OrgRole } from './types';

export const ORG_ROLES: OrgRole[] = ['viewer', 'editor', 'manager', 'administrator'];

export const ORG_PERMISSION_MATRIX: Record<OrgRole, OrgPermissions[]> = {
  viewer: ['view'],
  editor: ['view', 'create', 'edit', 'assign'],
  manager: ['view', 'create', 'edit', 'delete', 'assign', 'archive', 'restore', 'bulk'],
  administrator: ['view', 'create', 'edit', 'delete', 'assign', 'archive', 'restore', 'bulk'],
};

export function canOrg(role: OrgRole, action: OrgPermissions): boolean {
  const grants = ORG_PERMISSION_MATRIX[role];
  if (!grants) return false;
  return grants.includes(action);
}

export const CURRENT_ORG_ROLE: OrgRole = 'manager';
