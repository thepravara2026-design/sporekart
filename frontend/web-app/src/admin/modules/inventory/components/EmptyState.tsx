import { memo } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import { INVENTORY_EMPTY_STATES } from '../constants';

interface EmptyStateProps {
  stateKey: keyof typeof INVENTORY_EMPTY_STATES;
  onAction?: () => void;
  icon?: string;
  title?: string;
  message?: string;
  actionLabel?: string;
}

export const EmptyState = memo(function EmptyState({ stateKey, onAction, icon, title, message, actionLabel }: EmptyStateProps) {
  const config = INVENTORY_EMPTY_STATES[stateKey];
  const resolvedIcon = icon ?? config.icon;
  const resolvedTitle = title ?? config.title;
  const resolvedMessage = message ?? config.message;
  const resolvedAction = actionLabel ?? config.actionLabel;

  return (
    <div role="status" style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', textAlign: 'center', padding: '48px 24px', gap: 12, border: '2px dashed var(--color-border)', borderRadius: 'var(--radius-lg)', background: 'var(--color-surface)', minHeight: 280 }}>
      <div style={{ width: 56, height: 56, borderRadius: '50%', background: 'var(--color-surface-hover)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: 'var(--color-text-tertiary)' }}>
        <Icon name={resolvedIcon} size={28} />
      </div>
      <h3 style={{ margin: 0, fontSize: 'var(--text-body)', color: 'var(--color-text-primary)' }}>{resolvedTitle}</h3>
      <p style={{ margin: 0, fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)', maxWidth: 420 }}>{resolvedMessage}</p>
      {resolvedAction && (
        <button
          onClick={onAction}
          style={{ marginTop: 8, padding: '8px 16px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)', background: 'var(--color-surface)', cursor: 'pointer', color: 'var(--color-primary)', fontWeight: 600, fontSize: 'var(--text-body)' }}
        >
          {resolvedAction}
        </button>
      )}
    </div>
  );
});

