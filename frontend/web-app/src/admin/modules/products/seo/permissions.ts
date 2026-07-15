import type { SeoRole } from './types';

export const CURRENT_SEO_ROLE: SeoRole = 'administrator';
export const SEO_ROLE_HIERARCHY: SeoRole[] = ['viewer', 'seo_editor', 'marketing_manager', 'administrator'];
export const SEO_PERMISSIONS: Record<SeoRole, string[]> = {
  viewer: ['view'],
  seo_editor: ['view', 'edit_seo'],
  marketing_manager: ['view', 'edit_seo', 'publish', 'archive'],
  administrator: ['view', 'edit_seo', 'publish', 'archive', 'validate', 'preview'],
};

export function canSeo(role: SeoRole, ...permissions: string[]): boolean {
  return permissions.every((p) => SEO_PERMISSIONS[role].includes(p));
}

export function hasSeoRole(userRole: SeoRole, requiredRole: SeoRole): boolean {
  return SEO_ROLE_HIERARCHY.indexOf(userRole) >= SEO_ROLE_HIERARCHY.indexOf(requiredRole);
}
