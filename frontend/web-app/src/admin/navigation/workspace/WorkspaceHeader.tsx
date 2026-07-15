import { memo } from 'react';
import { Icon } from '../../../design-system/icons/Icon';
import type { WorkspaceConfig } from '../types';

interface WorkspaceHeaderProps {
  config: WorkspaceConfig;
  sticky?: boolean;
}

export const WorkspaceHeader = memo(function WorkspaceHeader({ config, sticky }: WorkspaceHeaderProps) {
  return (
    <div
      style={{
        display: 'flex',
        alignItems: 'flex-start',
        justifyContent: 'space-between',
        gap: 16,
        padding: '16px 0',
        ...(sticky ? { position: 'sticky', top: 0, zIndex: 10, background: 'var(--color-surface)' } : {}),
      }}
    >
      <div style={{ display: 'flex', alignItems: 'flex-start', gap: 12 }}>
        {config.icon && (
          <div style={{ width: 40, height: 40, borderRadius: 'var(--radius-md)', background: 'var(--color-primary-alpha)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: 'var(--color-primary)', flexShrink: 0 }}>
            <Icon name={config.icon} size={20} />
          </div>
        )}
        <div>
          <h1 style={{ margin: 0, fontSize: 'var(--text-h2)', fontWeight: 700, color: 'var(--color-text-primary)' }}>{config.title}</h1>
          {config.description && (
            <p style={{ margin: '4px 0 0', fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)' }}>{config.description}</p>
          )}
        </div>
      </div>
      {config.actions && config.actions.length > 0 && (
        <div style={{ display: 'flex', gap: 8 }}>
          {config.actions.map((action) => (
            <button
              key={action.id}
              onClick={action.onClick}
              style={{
                display: 'flex', alignItems: 'center', gap: 6,
                padding: '8px 14px', border: '1px solid var(--color-border)',
                borderRadius: 'var(--radius-md)', background: 'var(--color-surface)',
                cursor: 'pointer', color: 'var(--color-text-primary)',
                fontSize: 'var(--text-body)', fontWeight: 500,
              }}
            >
              {action.icon && <Icon name={action.icon} size={14} />}
              {action.label}
            </button>
          ))}
        </div>
      )}
    </div>
  );
});
