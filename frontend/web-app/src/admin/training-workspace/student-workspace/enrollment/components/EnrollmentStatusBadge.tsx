import type { EnrollmentStatus } from '../types';
import { ENROLLMENT_STATUS_LABELS, ENROLLMENT_STATUS_VARIANTS } from '../types';

const variantColors: Record<string, string> = {
  'default': '#6b7280', 'success': '#16a34a', 'warning': '#ca8a04',
  'danger': '#dc2626', 'neutral': '#9ca3af', 'info': '#2563eb', 'primary': '#7c3aed',
};

interface EnrollmentStatusBadgeProps {
  status: EnrollmentStatus;
}

export function EnrollmentStatusBadge({ status }: EnrollmentStatusBadgeProps) {
  const variant = ENROLLMENT_STATUS_VARIANTS[status];
  return (
    <span
      className="enrollment-status-badge"
      style={{
        display: 'inline-flex', alignItems: 'center', gap: 4,
        padding: '2px 8px', borderRadius: 'var(--radius-full)',
        fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)',
        backgroundColor: variantColors[variant] + '18',
        color: variantColors[variant],
        whiteSpace: 'nowrap',
      }}
    >
      <span style={{ width: 6, height: 6, borderRadius: '50%', backgroundColor: variantColors[variant], flexShrink: 0 }} />
      {ENROLLMENT_STATUS_LABELS[status]}
    </span>
  );
}
