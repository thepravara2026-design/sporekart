import { useContext, useCallback } from 'react';
import { PermissionContext } from './PermissionProvider';
import type { PermissionAction, PermissionResource } from './types';

export function usePermissions() {
  const ctx = useContext(PermissionContext);
  if (!ctx) throw new Error('usePermissions must be used within PermissionProvider');

  const can = useCallback(
    (action: PermissionAction, resource: PermissionResource) => ctx.can(action, resource),
    [ctx]
  );

  const canAny = useCallback(
    (actions: PermissionAction[], resource: PermissionResource) => actions.some((a) => ctx.can(a, resource)),
    [ctx]
  );

  const canAll = useCallback(
    (actions: PermissionAction[], resource: PermissionResource) => actions.every((a) => ctx.can(a, resource)),
    [ctx]
  );

  return { can, canAny, canAll, role: ctx.role, setRole: ctx.setRole };
}
