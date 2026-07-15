import { memo, type ReactNode } from 'react';
import { useWarehousePermissions } from '../hooks/useWarehousePermissions';
import type { WarehousePermission } from '../types';
import { EmptyState } from '../../inventory/components';

interface PermissionPlaceholderProps {
  permission: WarehousePermission;
  children: ReactNode;
}

export const PermissionPlaceholder = memo(function PermissionPlaceholder({ permission, children }: PermissionPlaceholderProps) {
  const { can } = useWarehousePermissions();
  if (can(permission)) return <>{children}</>;
  return <EmptyState stateKey="permission_denied" actionLabel="Request Access" />;
});
