import { memo } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import type { QuickAction } from '../types';

interface QuickActionCardProps {
  action: QuickAction;
  onClick?: (action: QuickAction) => void;
}

export const QuickActionCard = memo(function QuickActionCard({ action, onClick }: QuickActionCardProps) {
  return (
    <button
      onClick={() => onClick && onClick(action)}
      style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 10, textAlign: 'center', padding: '20px 16px', background: 'var(--color-surface)', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', cursor: 'pointer', color: 'var(--color-text-primary)', transition: 'box-shadow 0.2s, border-color 0.2s' }}
      onMouseEnter={(e) => { e.currentTarget.style.borderColor = 'var(--color-primary)'; }}
      onMouseLeave={(e) => { e.currentTarget.style.borderColor = 'var(--color-border)'; }}
    >
      <div style={{ width: 44, height: 44, borderRadius: 'var(--radius-md)', background: 'var(--color-primary-alpha)', color: 'var(--color-primary)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
        <Icon name={action.icon} size={22} />
      </div>
      <span style={{ fontSize: 'var(--text-body)', fontWeight: 500 }}>{action.label}</span>
    </button>
  );
});

