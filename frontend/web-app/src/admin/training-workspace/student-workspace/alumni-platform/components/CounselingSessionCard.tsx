import { memo } from 'react';
import type { CareerCounselingSession } from '../types';

const STATUS_COLORS: Record<string, { bg: string; color: string }> = {
  scheduled: { bg: '#eff6ff', color: '#2563eb' },
  completed: { bg: '#f0fdf4', color: '#16a34a' },
  cancelled: { bg: '#f3f4f6', color: '#6b7280' },
  'no-show': { bg: '#fef2f2', color: '#dc2626' },
};

export const CounselingSessionCard = memo(function CounselingSessionCard({ session }: { session: CareerCounselingSession }) {
  const sc = STATUS_COLORS[session.status] || { bg: '#f3f4f6', color: '#6b7280' };
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 6, padding: 12, borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 4 }}>
        <span style={{ fontWeight: 'var(--weight-semibold)', fontSize: 'var(--text-body)' }}>{session.topic}</span>
        <span style={{ padding: '1px 6px', borderRadius: 'var(--radius-full)', fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)', background: sc.bg, color: sc.color }}>{session.status}</span>
      </div>
      <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>{session.studentName} with {session.counselorName}</span>
      <div style={{ display: 'flex', gap: 12, fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
        <span>{session.sessionType.replace(/-/g, ' ')}</span>
        <span>{session.sessionDate}</span>
        <span>Rating: {'★'.repeat(session.rating)}{'☆'.repeat(5 - session.rating)}</span>
      </div>
    </div>
  );
});
