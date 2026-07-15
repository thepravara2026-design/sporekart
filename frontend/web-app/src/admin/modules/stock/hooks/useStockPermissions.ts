import { useState, useCallback } from 'react';
import type { StockRole } from '../types';
import { useStockWorkspace } from '../contexts/StockWorkspaceContext';

export function useStockPermissions() {
  const { role, setRole } = useStockWorkspace();
  const [localRole, setLocalRole] = useState<StockRole>(role);

  const handleRoleChange = useCallback((newRole: StockRole) => {
    setLocalRole(newRole);
    setRole(newRole);
  }, [setRole]);

  return { role: localRole, setRole: handleRoleChange };
}
