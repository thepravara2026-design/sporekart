import { useCallback, useMemo } from 'react';
import type { WarehousePermission, WarehouseRole } from '../types';
import { WAREHOUSE_ROLE_PERMISSIONS } from '../constants';
import { useWarehouseWorkspace } from '../contexts/WarehouseWorkspaceContext';

export interface WarehousePermissions {
  can: (action: WarehousePermission) => boolean;
  canAny: (actions: WarehousePermission[]) => boolean;
  canAll: (actions: WarehousePermission[]) => boolean;
  role: WarehouseRole;
  setRole: (role: WarehouseRole) => void;
}

export function useWarehousePermissions(): WarehousePermissions {
  const { role, setRole } = useWarehouseWorkspace();
  const allowed = useMemo(
    () => WAREHOUSE_ROLE_PERMISSIONS[role] ?? WAREHOUSE_ROLE_PERMISSIONS.viewer,
    [role],
  );

  const can = useCallback((action: WarehousePermission) => allowed.includes(action), [allowed]);
  const canAny = useCallback(
    (actions: WarehousePermission[]) => actions.some((a) => allowed.includes(a)),
    [allowed],
  );
  const canAll = useCallback(
    (actions: WarehousePermission[]) => actions.every((a) => allowed.includes(a)),
    [allowed],
  );

  return { can, canAny, canAll, role, setRole };
}


