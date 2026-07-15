import { memo } from 'react';
import { StatusBadge } from '../../../components/status/StatusBadge';
import type { StatusCount } from '../types';

interface StatusCardProps {
  status: StatusCount;
}

export const StatusCard = memo(function StatusCard({ status }: StatusCardProps) {
  return (
    <div style={{ background: 'var(--color-surface)', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', padding: '16px 20px', display: 'flex', alignItems: 'center', justifyContent: 'space-between', gap: 12 }}>
      <div>
        <div style={{ fontSize: 'var(--text-h2)', fontWeight: 700, color: 'var(--color-text-primary)', lineHeight: 1 }}>{status.count}</div>
        <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', marginTop: 4 }}>{status.label}</div>
      </div>
      <StatusBadge status={status.label} variant={status.variant} />
    </div>
  );
});
