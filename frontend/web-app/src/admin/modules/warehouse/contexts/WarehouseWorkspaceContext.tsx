import { createContext, useContext, useState, useCallback, useMemo, type ReactNode } from 'react';
import type { WarehouseRole, WarehousePermission } from '../types';
import { WAREHOUSE_ROLE_PERMISSIONS } from '../constants';

interface WarehouseWorkspaceValue {
  role: WarehouseRole;
  setRole: (role: WarehouseRole) => void;
  activeSection: string;
  setActiveSection: (id: string) => void;
  searchQuery: string;
  setSearchQuery: (q: string) => void;
  can: (action: WarehousePermission) => boolean;
}

function roleGrants(role: WarehouseRole): WarehousePermission[] {
  return WAREHOUSE_ROLE_PERMISSIONS[role] ?? WAREHOUSE_ROLE_PERMISSIONS.viewer;
}

export const WarehouseWorkspaceContext = createContext<WarehouseWorkspaceValue | null>(null);

interface ProviderProps {
  children: ReactNode;
  initialRole?: WarehouseRole;
}

export function WarehouseWorkspaceProvider({ children, initialRole = 'administrator' }: ProviderProps) {
  const [role, setRole] = useState<WarehouseRole>(initialRole);
  const [activeSection, setActiveSection] = useState('overview');
  const [searchQuery, setSearchQuery] = useState('');

  const grants = useMemo(() => roleGrants(role), [role]);

  const can = useCallback((action: WarehousePermission) => grants.includes(action), [grants]);

  const value = useMemo<WarehouseWorkspaceValue>(
    () => ({ role, setRole, activeSection, setActiveSection, searchQuery, setSearchQuery, can }),
    [role, activeSection, searchQuery, can],
  );

  return (
    <WarehouseWorkspaceContext.Provider value={value}>
      {children}
    </WarehouseWorkspaceContext.Provider>
  );
}

export function useWarehouseWorkspace() {
  const ctx = useContext(WarehouseWorkspaceContext);
  if (!ctx) throw new Error('useWarehouseWorkspace must be used within WarehouseWorkspaceProvider');
  return ctx;
}


