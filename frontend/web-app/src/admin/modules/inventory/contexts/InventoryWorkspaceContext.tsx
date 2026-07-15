import { createContext, useContext, useState, useCallback, useMemo, type ReactNode } from 'react';
import type { InventoryRole, InventoryPermission } from '../types';
import { INVENTORY_ROLE_PERMISSIONS } from '../constants';

interface InventoryWorkspaceValue {
  role: InventoryRole;
  setRole: (role: InventoryRole) => void;
  activeSection: string;
  setActiveSection: (id: string) => void;
  searchQuery: string;
  setSearchQuery: (q: string) => void;
  can: (action: InventoryPermission) => boolean;
}

export const InventoryWorkspaceContext = createContext<InventoryWorkspaceValue | null>(null);

interface ProviderProps {
  children: ReactNode;
  initialRole?: InventoryRole;
}

export function InventoryWorkspaceProvider({ children, initialRole = 'administrator' }: ProviderProps) {
  const [role, setRole] = useState<InventoryRole>(initialRole);
  const [activeSection, setActiveSection] = useState('overview');
  const [searchQuery, setSearchQuery] = useState('');

  const grants = useMemo(() => INVENTORY_ROLE_PERMISSIONS[role] ?? INVENTORY_ROLE_PERMISSIONS.viewer, [role]);

  const can = useCallback(
    (action: InventoryPermission) => grants.includes(action),
    [grants],
  );

  const value = useMemo<InventoryWorkspaceValue>(
    () => ({ role, setRole, activeSection, setActiveSection, searchQuery, setSearchQuery, can }),
    [role, activeSection, searchQuery, can],
  );

  return (
    <InventoryWorkspaceContext.Provider value={value}>
      {children}
    </InventoryWorkspaceContext.Provider>
  );
}

export function useInventoryWorkspace() {
  const ctx = useContext(InventoryWorkspaceContext);
  if (!ctx) throw new Error('useInventoryWorkspace must be used within InventoryWorkspaceProvider');
  return ctx;
}
