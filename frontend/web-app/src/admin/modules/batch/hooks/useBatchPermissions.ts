import { useState, useMemo } from 'react';
import type { BatchRole, BatchPermission } from '../types';
import { BATCH_ROLE_PERMISSIONS } from '../constants';

export function useBatchPermissions(initialRole: BatchRole = 'administrator') {
  const [role, setRole] = useState<BatchRole>(initialRole);

  const permissions = useMemo(() => BATCH_ROLE_PERMISSIONS[role], [role]);

  const can = (permission: BatchPermission) => permissions.includes(permission);

  return { role, setRole, permissions, can };
}
