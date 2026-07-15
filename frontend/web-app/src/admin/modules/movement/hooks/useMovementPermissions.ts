import { useState, useMemo } from 'react';
import type { MovementRole, MovementPermission } from '../types';
import { MOVEMENT_ROLE_PERMISSIONS } from '../constants';

export function useMovementPermissions(initialRole: MovementRole = 'administrator') {
  const [role, setRole] = useState<MovementRole>(initialRole);
  const permissions = useMemo(() => MOVEMENT_ROLE_PERMISSIONS[role], [role]);
  const can = (permission: MovementPermission) => permissions.includes(permission);
  return { role, setRole, permissions, can };
}
