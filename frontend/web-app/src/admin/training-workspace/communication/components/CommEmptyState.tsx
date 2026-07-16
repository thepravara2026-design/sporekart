import { memo } from 'react';
import type { ReactNode } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';

export interface CommEmptyStateProps {
  icon?: string;
  title: string;
  description?: string;
  action?: ReactNode;
}

const CommEmptyState = memo(function CommEmptyState({
  icon = 'message-square',
  title,
  description,
  action,
}: CommEmptyStateProps) {
  return (
    <div
      role="status"
      style={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        gap: 'var(--space-3)',
        padding: 'var(--space-6) var(--space-4)',
        textAlign: 'center',
        background: 'var(--color-bg-surface-muted)',
        border: '1px dashed var(--color-border-default)',
        borderRadius: 'var(--radius-lg)',
        color: 'var(--color-text-secondary)',
      }}
    >
      <span
        style={{
          display: 'inline-flex',
          alignItems: 'center',
          justifyContent: 'center',
          width: 48,
          height: 48,
          borderRadius: '50%',
          background: 'var(--color-bg-surface-raised)',
          color: 'var(--color-text-muted)',
        }}
      >
        <Icon name={icon} size={24} />
      </span>
      <div>
        <p style={{ margin: 0, fontSize: 'var(--text-body)', fontWeight: 600, color: 'var(--color-text-primary)' }}>
          {title}
        </p>
        {description && (
          <p style={{ margin: '4px 0 0', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-muted)' }}>
            {description}
          </p>
        )}
      </div>
      {action}
    </div>
  );
});

export default CommEmptyState;
