import { memo } from 'react';
import { useNavigate } from 'react-router-dom';
import { Icon } from '../../../design-system/icons/Icon';
import type { QuickActionData } from '../types';

interface QuickActionsProps {
  actions: QuickActionData[];
  columns?: number;
}

export const QuickActions = memo(function QuickActions({ actions, columns = 4 }: QuickActionsProps) {
  const navigate = useNavigate();

  if (actions.length === 0) {
    return (
      <div style={{ textAlign: 'center', padding: '24px 16px', color: 'var(--color-text-tertiary)', fontSize: 'var(--text-body)' }}>
        No quick actions configured
      </div>
    );
  }

  return (
    <div
      style={{
        display: 'grid',
        gridTemplateColumns: `repeat(${columns}, 1fr)`,
        gap: 8,
      }}
    >
      {actions.map((action) => (
        <button
          key={action.id}
          onClick={() => navigate(action.path)}
          style={{
            display: 'flex',
            alignItems: 'center',
            gap: 8,
            padding: '10px 12px',
            border: '1px solid var(--color-border)',
            borderRadius: 'var(--radius-md)',
            background: 'var(--color-surface)',
            cursor: 'pointer',
            color: 'var(--color-text-primary)',
            fontSize: 'var(--text-body)',
            transition: 'border-color 0.15s, background 0.15s',
          }}
        >
          <div style={{ width: 28, height: 28, borderRadius: 'var(--radius-sm)', background: `${action.color}1A`, display: 'flex', alignItems: 'center', justifyContent: 'center', color: action.color, flexShrink: 0 }}>
            <Icon name={action.icon} size={14} />
          </div>
          <span>{action.label}</span>
        </button>
      ))}
    </div>
  );
});
