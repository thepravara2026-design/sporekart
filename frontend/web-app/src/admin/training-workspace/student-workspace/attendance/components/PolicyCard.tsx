import { memo } from 'react';
import type { AttendancePolicy } from '../types';

interface PolicyCardProps {
  policy: AttendancePolicy;
}

export const PolicyCard = memo(function PolicyCard({ policy }: PolicyCardProps) {
  return (
    <div style={{
      padding: 'var(--space-3)', borderRadius: 'var(--radius-md)',
      border: '1px solid var(--color-border-default)',
      background: policy.isActive ? 'var(--color-bg-surface-default)' : '#f9fafb',
      display: 'flex', flexDirection: 'column', gap: 8,
      opacity: policy.isActive ? 1 : 0.6,
    }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div style={{ fontWeight: 'var(--weight-semibold)', fontSize: 'var(--text-body-sm)' }}>{policy.policyName}</div>
        <span style={{
          padding: '2px 8px', borderRadius: 'var(--radius-full)', fontSize: 'var(--text-caption)',
          fontWeight: 'var(--weight-medium)',
          background: policy.isActive ? '#f0fdf4' : '#f3f4f6',
          color: policy.isActive ? '#16a34a' : '#6b7280',
        }}>
          {policy.isActive ? 'Active' : 'Inactive'}
        </span>
      </div>
      <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>{policy.description}</div>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(140px, 1fr))', gap: 4, fontSize: 'var(--text-caption)' }}>
        <div><span style={{ color: 'var(--color-text-tertiary)' }}>Min Attendance:</span> <strong>{policy.minAttendancePercent}%</strong></div>
        <div><span style={{ color: 'var(--color-text-tertiary)' }}>Grace Period:</span> <strong>{policy.gracePeriodMinutes} min</strong></div>
        <div><span style={{ color: 'var(--color-text-tertiary)' }}>Late Threshold:</span> <strong>{policy.lateThresholdMinutes} min</strong></div>
        <div><span style={{ color: 'var(--color-text-tertiary)' }}>Half-day:</span> <strong>{policy.halfDayThresholdMinutes} min</strong></div>
      </div>
      <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
        <span style={{ color: 'var(--color-text-secondary)' }}>Applies to:</span> {policy.appliesTo.join(', ')}
      </div>
      <div style={{ fontSize: 'var(--text-caption)', padding: '6px 8px', borderRadius: 'var(--radius-sm)', background: '#fefce8', color: '#ca8a04' }}>
        {policy.consequences}
      </div>
    </div>
  );
});
