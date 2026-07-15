import { usePermissions } from './usePermissions';
import type { PermissionAction, PermissionResource } from './types';

interface PermissionGateProps {
  action: PermissionAction;
  resource: PermissionResource;
  fallback?: React.ReactNode;
  children: React.ReactNode;
}

export function PermissionGate({ action, resource, fallback = null, children }: PermissionGateProps) {
  const { can } = usePermissions();
  if (!can(action, resource)) return <>{fallback}</>;
  return <>{children}</>;
}

interface PermissionGateAllProps {
  actions: PermissionAction[];
  resource: PermissionResource;
  fallback?: React.ReactNode;
  children: React.ReactNode;
}

export function PermissionGateAll({ actions, resource, fallback = null, children }: PermissionGateAllProps) {
  const { canAll } = usePermissions();
  if (!canAll(actions, resource)) return <>{fallback}</>;
  return <>{children}</>;
}

interface PermissionGateAnyProps {
  actions: PermissionAction[];
  resource: PermissionResource;
  fallback?: React.ReactNode;
  children: React.ReactNode;
}

export function PermissionGateAny({ actions, resource, fallback = null, children }: PermissionGateAnyProps) {
  const { canAny } = usePermissions();
  if (!canAny(actions, resource)) return <>{fallback}</>;
  return <>{children}</>;
}
