import { useState, useCallback } from 'react';
import type { InventoryItemRole } from '../types';
import { useInventoryItemWorkspace } from '../contexts/InventoryItemWorkspaceContext';

export function useInventoryItemPermissions() {
  const { role, setRole } = useInventoryItemWorkspace();
  const [localRole, setLocalRole] = useState<InventoryItemRole>(role);

  const handleRoleChange = useCallback((newRole: InventoryItemRole) => {
    setLocalRole(newRole);
    setRole(newRole);
  }, [setRole]);

  return { role: localRole, setRole: handleRoleChange };
}
