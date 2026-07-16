import { memo } from 'react';
import type { InterviewPreparation } from '../types';

const STATUS_COLORS: Record<string, { bg: string; color: string }> = {
  upcoming: { bg: '#eff6ff', color: '#2563eb' },
  completed: { bg: '#f0fdf4', color: '#16a34a' },
  cancelled: { bg: '#f3f4f6', color: '#6b7280' },
  rescheduled: { bg: '#fefce8', color: '#ca8a04' },
};

const RESULT_COLORS: Record<string, { bg: string; color: string }> = {
  cleared: { bg: '#f0fdf4', color: '#16a34a' },
  failed: { bg: '#fef2f2', color: '#dc2626' },
  awaiting: { bg: '#fefce8', color: '#ca8a04' },
};

export const InterviewPrepCard = memo(function InterviewPrepCard({ prep }: { prep: InterviewPreparation }) {
  const sc = STATUS_COLORS[prep.status] || { bg: '#f3f4f6', color: '#6b7280' };
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 6, padding: 12, borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 4 }}>
        <span style={{ fontWeight: 'var(--weight-semibold)', fontSize: 'var(--text-body)' }}>{prep.companyName} • Round {prep.roundNumber}</span>
        <span style={{ padding: '1px 6px', borderRadius: 'var(--radius-full)', fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)', background: sc.bg, color: sc.color }}>{prep.status}</span>
      </div>
      <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>{prep.jobTitle} • {prep.roundType.replace(/-/g, ' ')} • {prep.studentName}</span>
      <div style={{ display: 'flex', gap: 12, fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', alignItems: 'center' }}>
        <span>Date: {prep.scheduledDate}</span>
        {prep.result && (
          <span style={{ padding: '1px 6px', borderRadius: 'var(--radius-full)', background: RESULT_COLORS[prep.result]?.bg || '#f3f4f6', color: RESULT_COLORS[prep.result]?.color || '#6b7280' }}>
            {prep.result}
          </span>
        )}
      </div>
    </div>
  );
});
