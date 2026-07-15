import { useCallback, useMemo } from 'react';
import type { InventoryPermission, InventoryRole } from '../types';
import { INVENTORY_ROLE_PERMISSIONS } from '../constants';
import { useInventoryWorkspace } from '../contexts/InventoryWorkspaceContext';

export interface InventoryPermissions {
  can: (action: InventoryPermission) => boolean;
  canAny: (actions: InventoryPermission[]) => boolean;
  canAll: (actions: InventoryPermission[]) => boolean;
  role: InventoryRole;
  setRole: (role: InventoryRole) => void;
}

export function useInventoryPermissions(): InventoryPermissions {
  const { role, setRole } = useInventoryWorkspace();
  const allowed = useMemo(
    () => INVENTORY_ROLE_PERMISSIONS[role] ?? INVENTORY_ROLE_PERMISSIONS.viewer,
    [role]
  );

  const can = useCallback((action: InventoryPermission) => allowed.includes(action), [allowed]);
  const canAny = useCallback(
    (actions: InventoryPermission[]) => actions.some((a) => allowed.includes(a)),
    [allowed]
  );
  const canAll = useCallback(
    (actions: InventoryPermission[]) => actions.every((a) => allowed.includes(a)),
    [allowed]
  );

  return { can, canAny, canAll, role, setRole };
}

