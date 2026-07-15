import { memo, type ReactNode, type CSSProperties } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';

interface ToolbarProps {
  title?: string;
  search?: ReactNode;
  filters?: ReactNode;
  actions?: ReactNode;
}

export const Toolbar = memo(function Toolbar({ title, search, filters, actions }: ToolbarProps) {
  return (
    <div style={{ display: 'flex', flexWrap: 'wrap', alignItems: 'center', gap: 12, padding: '12px 16px', background: 'var(--color-surface)', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)' }}>
      {title && <h2 style={{ margin: 0, fontSize: 'var(--text-body)', fontWeight: 600, color: 'var(--color-text-primary)', marginRight: 'auto' }}>{title}</h2>}
      {!title && <div style={{ flex: 1 }} />}
      {search}
      {filters}
      {actions}
    </div>
  );
});

interface ActionButton {
  id: string;
  label: string;
  icon?: string;
  onClick?: () => void;
  variant?: 'primary' | 'secondary' | 'danger';
}

interface ActionBarProps {
  actions: ActionButton[];
}

const variantStyle: Record<string, CSSProperties> = {
  primary: { background: 'var(--color-primary)', color: '#fff', border: '1px solid var(--color-primary)' },
  secondary: { background: 'var(--color-surface)', color: 'var(--color-text-primary)', border: '1px solid var(--color-border)' },
  danger: { background: 'var(--color-danger)', color: '#fff', border: '1px solid var(--color-danger)' },
};

export const ActionBar = memo(function ActionBar({ actions }: ActionBarProps) {
  return (
    <div style={{ display: 'flex', flexWrap: 'wrap', gap: 8 }} role="toolbar" aria-label="Context actions">
      {actions.map((a) => (
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

