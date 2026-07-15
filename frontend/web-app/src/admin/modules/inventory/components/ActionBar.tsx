import { memo, type CSSProperties } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import { useInventoryPermissions } from '../hooks/useInventoryPermissions';
import type { InventoryPermission } from '../types';

interface PermissionActionButton {
  id: string;
  label: string;
  icon?: string;
  onClick?: () => void;
  permission: InventoryPermission;
  variant?: 'primary' | 'secondary' | 'danger';
}

interface ActionBarProps {
  actions: PermissionActionButton[];
}

const variantStyle: Record<string, CSSProperties> = {
  primary: { background: 'var(--color-primary)', color: '#fff', border: '1px solid var(--color-primary)' },
  secondary: { background: 'var(--color-surface)', color: 'var(--color-text-primary)', border: '1px solid var(--color-border)' },
  danger: { background: 'var(--color-danger)', color: '#fff', border: '1px solid var(--color-danger)' },
};

export const PermissionActionBar = memo(function PermissionActionBar({ actions }: ActionBarProps) {
  const { can } = useInventoryPermissions();
  const visible = actions.filter((a) => can(a.permission));
  if (visible.length === 0) return null;
  return (
    <div style={{ display: 'flex', flexWrap: 'wrap', gap: 8 }} role="toolbar" aria-label="Context actions">
      {visible.map((a) => (
        <button
          key={a.id}
          onClick={a.onClick}
          style={{ display: 'inline-flex', alignItems: 'center', gap: 6, padding: '8px 14px', borderRadius: 'var(--radius-md)', cursor: 'pointer', fontSize: 'var(--text-body)', fontWeight: 500, ...variantStyle[a.variant ?? 'secondary'] }}
        >
          {a.icon && <Icon name={a.icon} size={14} />}
          {a.label}
        </button>
      ))}
    </div>
  );
});



