import { createContext, useState, useCallback, useMemo } from 'react';
import { DEFAULT_PERMISSION_CONFIG } from './permissionConfig';
import type { PermissionAction, PermissionResource } from './types';

interface PermissionContextValue {
  role: string;
  setRole: (role: string) => void;
  can: (action: PermissionAction, resource: PermissionResource) => boolean;
}

export const PermissionContext = createContext<PermissionContextValue | null>(null);

interface PermissionProviderProps {
  children: React.ReactNode;
  initialRole?: string;
}

export function PermissionProvider({ children, initialRole = 'viewer' }: PermissionProviderProps) {
  const [role, setRole] = useState(initialRole);

  const roleConfig = DEFAULT_PERMISSION_CONFIG.find((c) => c.role === role);
  const grants = roleConfig?.grants ?? {};

  const can = useCallback(
    (action: PermissionAction, resource: PermissionResource) => {
      const allowed = grants[resource];
      if (!allowed) return false;
      return allowed.includes(action);
    },
    [grants]
  );

  const value = useMemo(() => ({ role, setRole, can }), [role, can]);

  return (
    <PermissionContext.Provider value={value}>
      {children}
    </PermissionContext.Provider>
  );
}
