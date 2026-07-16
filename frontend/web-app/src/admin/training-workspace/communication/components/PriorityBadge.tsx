import { memo } from 'react';
import type { CommunicationPriority } from '../data/communicationTypes';
import { PRIORITY_LABELS } from '../data/communicationTypes';
import { PRIORITY_TONE } from '../data/communicationOptions';
import { toneTokens } from '../data/communicationFormatters';

export interface PriorityBadgeProps {
  priority: CommunicationPriority;
  size?: 'sm' | 'md';
}

const PriorityBadge = memo(function PriorityBadge({ priority, size = 'sm' }: PriorityBadgeProps) {
  const tokens = toneTokens(PRIORITY_TONE[priority]);
  return (
    <span
      style={{
        display: 'inline-flex',
        alignItems: 'center',
        gap: 'var(--space-1)',
        padding: size === 'sm' ? '2px 8px' : '4px 10px',
        borderRadius: 'var(--radius-sm)',
        fontSize: size === 'sm' ? 'var(--text-caption)' : 'var(--text-body-sm)',
        fontWeight: 600,
        color: tokens.fg,
        background: tokens.bg,
        border: `1px solid ${tokens.border}`,
        lineHeight: 1.4,
        whiteSpace: 'nowrap',
      }}
    >
      {PRIORITY_LABELS[priority]}
    </span>
  );
});

export default PriorityBadge;
