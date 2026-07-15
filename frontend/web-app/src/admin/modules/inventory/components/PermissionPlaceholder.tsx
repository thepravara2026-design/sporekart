import { memo, type ReactNode } from 'react';
import { useInventoryPermissions } from '../hooks/useInventoryPermissions';
import type { InventoryPermission } from '../types';
import { EmptyState } from './EmptyState';

interface PermissionPlaceholderProps {
  permission: InventoryPermission;
  children: ReactNode;
}

export const PermissionPlaceholder = memo(function PermissionPlaceholder({ permission, children }: PermissionPlaceholderProps) {
  const { can } = useInventoryPermissions();
  if (can(permission)) return <>{children}</>;
  return <EmptyState stateKey="permission_denied" actionLabel="Request Access" />;
});
