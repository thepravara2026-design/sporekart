import { memo, type CSSProperties } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';

export type WarehouseActionId = 'edit' | 'delete' | 'export' | 'archive';

interface PermissionActionBarProps {
  permissions: {
    canEdit?: boolean;
    canDelete?: boolean;
    canExport?: boolean;
    canArchive?: boolean;
  };
  selectedCount: number;
  onAction: (action: WarehouseActionId) => void;
  onClear: () => void;
}

const variantStyle: Record<'primary' | 'secondary' | 'danger', CSSProperties> = {
  primary: { background: 'var(--color-primary)', color: 'var(--color-primary-contrast)', border: '1px solid var(--color-primary)' },
  secondary: { background: 'var(--color-surface)', color: 'var(--color-text-primary)', border: '1px solid var(--color-border)' },
  danger: { background: 'var(--color-danger)', color: '#fff', border: '1px solid var(--color-danger)' },
};

export const PermissionActionBar = memo(function PermissionActionBar({ permissions, selectedCount, onAction, onClear }: PermissionActionBarProps) {
  const buttons: { id: WarehouseActionId; label: string; icon: string; variant: 'primary' | 'secondary' | 'danger' }[] = [];
  if (permissions.canExport) buttons.push({ id: 'export', label: 'Export', icon: 'download', variant: 'secondary' });
  if (permissions.canEdit) buttons.push({ id: 'edit', label: 'Edit', icon: 'edit', variant: 'secondary' });
  if (permissions.canArchive) buttons.push({ id: 'archive', label: 'Archive', icon: 'archive', variant: 'primary' });
  if (permissions.canDelete) buttons.push({ id: 'delete', label: 'Delete', icon: 'trash', variant: 'danger' });

  if (buttons.length === 0) return null;

  return (
    <div style={{ display: 'flex', flexWrap: 'wrap', alignItems: 'center', gap: 8, padding: '12px 16px', background: 'var(--color-surface)', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)' }} role="toolbar" aria-label="Selection actions">
      <span style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)', fontWeight: 600 }}>{selectedCount} selected</span>
      <div style={{ display: 'flex', gap: 8, marginLeft: 'auto' }}>
        {buttons.map((b) => (
          <button key={b.id} onClick={() => onAction(b.id)} style={{ display: 'inline-flex', alignItems: 'center', gap: 6, padding: '8px 14px', borderRadius: 'var(--radius-md)', cursor: 'pointer', fontSize: 'var(--text-body)', fontWeight: 500, ...variantStyle[b.variant] }}>
            <Icon name={b.icon} size={14} /> {b.label}
          </button>
        ))}
        <button onClick={onClear} style={{ display: 'inline-flex', alignItems: 'center', gap: 6, padding: '8px 14px', borderRadius: 'var(--radius-md)', cursor: 'pointer', fontSize: 'var(--text-body)', fontWeight: 500, background: 'var(--color-surface)', color: 'var(--color-text-primary)', border: '1px solid var(--color-border)' }}>
          Clear
        </button>
      </div>
    </div>
  );
});

