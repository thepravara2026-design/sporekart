import { memo } from 'react';
import { Icon } from '../../../design-system/icons/Icon';

interface NavEmptyStateProps {
  icon: string;
  title: string;
  description?: string;
  action?: { label: string; onClick: () => void };
}

export const NavEmptyState = memo(function NavEmptyState({ icon, title, description, action }: NavEmptyStateProps) {
  return (
    <div
      role="status"
      style={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        padding: '32px 16px',
        textAlign: 'center',
        color: 'var(--color-text-tertiary)',
      }}
    >
      <Icon name={icon} size={32} style={{ marginBottom: 12 }} />
      <h3 style={{ margin: '0 0 4px', fontSize: 'var(--text-body)', fontWeight: 600, color: 'var(--color-text-secondary)' }}>{title}</h3>
      {description && <p style={{ margin: 0, fontSize: 'var(--text-body)', maxWidth: 240 }}>{description}</p>}
      {action && (
        <button
          onClick={action.onClick}
          style={{
            marginTop: 12,
            padding: '6px 16px',
            border: '1px solid var(--color-primary)',
            borderRadius: 'var(--radius-md)',
            background: 'var(--color-primary)',
            color: '#fff',
            cursor: 'pointer',
            fontSize: 'var(--text-body)',
          }}
        >
          {action.label}
        </button>
      )}
    </div>
  );
});
