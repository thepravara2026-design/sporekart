import type { AssignmentStatus } from '../types';
import { ASSIGNMENT_STATUS_LABELS, ASSIGNMENT_STATUS_VARIANTS } from '../types';

const variantColors: Record<string, string> = {
  'default': '#6b7280', 'success': '#16a34a', 'warning': '#ca8a04',
  'danger': '#dc2626', 'neutral': '#9ca3af', 'info': '#2563eb',
};

interface AssignmentStatusBadgeProps {
  status: AssignmentStatus;
}

export function AssignmentStatusBadge({ status }: AssignmentStatusBadgeProps) {
  const variant = ASSIGNMENT_STATUS_VARIANTS[status];
  return (
    <span style={{
      display: 'inline-flex', alignItems: 'center', gap: 4,
      padding: '2px 8px', borderRadius: 'var(--radius-full)',
      fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)',
      backgroundColor: variantColors[variant] + '18',
      color: variantColors[variant], whiteSpace: 'nowrap',
    }}>
      <span style={{ width: 6, height: 6, borderRadius: '50%', backgroundColor: variantColors[variant], flexShrink: 0 }} />
      {ASSIGNMENT_STATUS_LABELS[status]}
    </span>
  );
}
