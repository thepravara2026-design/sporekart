import { useState, useMemo } from 'react';
import type { ReceivingRole, ReceivingPermission } from '../types';
import { RECEIVING_ROLE_PERMISSIONS } from '../constants';

export function useReceivingPermissions(initialRole: ReceivingRole = 'administrator') {
  const [role, setRole] = useState<ReceivingRole>(initialRole);
  const permissions = useMemo(() => RECEIVING_ROLE_PERMISSIONS[role], [role]);
  const can = (permission: ReceivingPermission) => permissions.includes(permission);
  return { role, setRole, permissions, can };
}
