import type { ProgressStatus } from '../types';
import { PROGRESS_STATUS_LABELS, PROGRESS_STATUS_VARIANTS } from '../types';

const variantColors: Record<string, string> = {
  'default': '#6b7280', 'success': '#16a34a', 'warning': '#ca8a04',
  'danger': '#dc2626', 'neutral': '#9ca3af', 'info': '#2563eb',
};

interface ProgressStatusBadgeProps {
  status: ProgressStatus;
}

export function ProgressStatusBadge({ status }: ProgressStatusBadgeProps) {
  const variant = PROGRESS_STATUS_VARIANTS[status];
  return (
    <span style={{
      display: 'inline-flex', alignItems: 'center', gap: 4,
      padding: '2px 8px', borderRadius: 'var(--radius-full)',
      fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)',
      backgroundColor: variantColors[variant] + '18',
      color: variantColors[variant], whiteSpace: 'nowrap',
    }}>
      <span style={{ width: 6, height: 6, borderRadius: '50%', backgroundColor: variantColors[variant], flexShrink: 0 }} />
      {PROGRESS_STATUS_LABELS[status]}
    </span>
  );
}
