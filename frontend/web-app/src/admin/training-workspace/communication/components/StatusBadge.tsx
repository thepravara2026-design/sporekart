import { memo } from 'react';
import type { CommunicationStatus } from '../data/communicationTypes';
import { COMMUNICATION_STATUS_LABELS } from '../data/communicationTypes';
import { STATUS_TONE } from '../data/communicationOptions';
import { toneTokens } from '../data/communicationFormatters';

export interface StatusBadgeProps {
  status: CommunicationStatus;
  size?: 'sm' | 'md';
}

const StatusBadge = memo(function StatusBadge({ status, size = 'sm' }: StatusBadgeProps) {
  const tokens = toneTokens(STATUS_TONE[status]);
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
      {COMMUNICATION_STATUS_LABELS[status]}
    </span>
  );
});

export default StatusBadge;
