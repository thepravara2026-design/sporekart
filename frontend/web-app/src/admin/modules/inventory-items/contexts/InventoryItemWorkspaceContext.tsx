import { createContext, useContext, useState, useCallback, useMemo, type ReactNode } from 'react';
import type { InventoryItemRole, InventoryItemPermission } from '../types';
import { INVENTORY_ITEM_ROLE_PERMISSIONS } from '../constants';

interface InventoryItemWorkspaceValue {
  role: InventoryItemRole;
  setRole: (role: InventoryItemRole) => void;
  activeSection: string;
  setActiveSection: (id: string) => void;
  searchQuery: string;
  setSearchQuery: (q: string) => void;
  can: (action: InventoryItemPermission) => boolean;
}

function roleGrants(role: InventoryItemRole): InventoryItemPermission[] {
  return INVENTORY_ITEM_ROLE_PERMISSIONS[role] ?? INVENTORY_ITEM_ROLE_PERMISSIONS.viewer;
}

export const InventoryItemWorkspaceContext = createContext<InventoryItemWorkspaceValue | null>(null);

interface ProviderProps {
  children: ReactNode;
  initialRole?: InventoryItemRole;
}

export function InventoryItemWorkspaceProvider({ children, initialRole = 'administrator' }: ProviderProps) {
  const [role, setRole] = useState<InventoryItemRole>(initialRole);
  const [activeSection, setActiveSection] = useState('overview');
  const [searchQuery, setSearchQuery] = useState('');

  const grants = useMemo(() => roleGrants(role), [role]);

  const can = useCallback((action: InventoryItemPermission) => grants.includes(action), [grants]);

  const value = useMemo<InventoryItemWorkspaceValue>(
    () => ({ role, setRole, activeSection, setActiveSection, searchQuery, setSearchQuery, can }),
    [role, activeSection, searchQuery, can],
  );

  return (
    <InventoryItemWorkspaceContext.Provider value={value}>
      {children}
    </InventoryItemWorkspaceContext.Provider>
  );
}

export function useInventoryItemWorkspace() {
  const ctx = useContext(InventoryItemWorkspaceContext);
  if (!ctx) throw new Error('useInventoryItemWorkspace must be used within InventoryItemWorkspaceProvider');
  return ctx;
}
