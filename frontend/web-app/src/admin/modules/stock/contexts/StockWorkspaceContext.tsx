import { createContext, useContext, useState, useCallback, useMemo, type ReactNode } from 'react';
import type { StockRole, StockPermission } from '../types';
import { STOCK_ROLE_PERMISSIONS } from '../constants';

interface StockWorkspaceValue {
  role: StockRole;
  setRole: (role: StockRole) => void;
  activeSection: string;
  setActiveSection: (id: string) => void;
  searchQuery: string;
  setSearchQuery: (q: string) => void;
  can: (action: StockPermission) => boolean;
}

function roleGrants(role: StockRole): StockPermission[] {
  return STOCK_ROLE_PERMISSIONS[role] ?? STOCK_ROLE_PERMISSIONS.viewer;
}

export const StockWorkspaceContext = createContext<StockWorkspaceValue | null>(null);

interface ProviderProps { children: ReactNode; initialRole?: StockRole; }

export function StockWorkspaceProvider({ children, initialRole = 'administrator' }: ProviderProps) {
  const [role, setRole] = useState<StockRole>(initialRole);
  const [activeSection, setActiveSection] = useState('overview');
  const [searchQuery, setSearchQuery] = useState('');

  const grants = useMemo(() => roleGrants(role), [role]);
  const can = useCallback((action: StockPermission) => grants.includes(action), [grants]);

  const value = useMemo<StockWorkspaceValue>(
    () => ({ role, setRole, activeSection, setActiveSection, searchQuery, setSearchQuery, can }),
    [role, activeSection, searchQuery, can],
  );

  return <StockWorkspaceContext.Provider value={value}>{children}</StockWorkspaceContext.Provider>;
}

export function useStockWorkspace() {
  const ctx = useContext(StockWorkspaceContext);
  if (!ctx) throw new Error('useStockWorkspace must be used within StockWorkspaceProvider');
  return ctx;
}
