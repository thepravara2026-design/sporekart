import { memo } from 'react';
import { PLACEMENT_STATUS_LABELS, PLACEMENT_STATUS_VARIANTS } from '../types';

export const StatusBadge = memo(function StatusBadge({ status }: { status: string }) {
  const v = PLACEMENT_STATUS_VARIANTS[status] || { bg: '#f3f4f6', color: '#6b7280' };
  const label = PLACEMENT_STATUS_LABELS[status as keyof typeof PLACEMENT_STATUS_LABELS] || status;
  return (
    <span style={{
      display: 'inline-flex', alignItems: 'center', padding: '2px 8px', borderRadius: 'var(--radius-full)',
      fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)', background: v.bg, color: v.color,
    }}>
      {label}
    </span>
  );
});
