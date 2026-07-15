import type { MediaPermissions } from './types';

export const MEDIA_ROLES = ['viewer', 'uploader', 'editor', 'manager', 'administrator'] as const;

export type MediaRole = (typeof MEDIA_ROLES)[number];

export type MediaPermissionMatrix = Record<MediaRole, MediaPermissions[]>;

export const MEDIA_PERMISSION_MATRIX: MediaPermissionMatrix = {
  viewer: ['view'],
  uploader: ['view', 'upload'],
  editor: ['view', 'upload', 'edit', 'replace', 'organize'],
  manager: ['view', 'upload', 'edit', 'replace', 'organize', 'delete', 'publish', 'archive'],
  administrator: ['view', 'upload', 'edit', 'replace', 'organize', 'delete', 'publish', 'archive'],
};

export function canMedia(role: MediaRole, action: MediaPermissions): boolean {
  const grants = MEDIA_PERMISSION_MATRIX[role];
  if (!grants) return false;
  return grants.includes(action);
}

export const CURRENT_MEDIA_ROLE: MediaRole = 'administrator';
