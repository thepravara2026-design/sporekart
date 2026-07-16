import { memo } from 'react';
import type { AlumniMentorship } from '../types';

const STATUS_COLORS: Record<string, { bg: string; color: string }> = {
  active: { bg: '#f0fdf4', color: '#16a34a' },
  completed: { bg: '#f3f4f6', color: '#6b7280' },
  paused: { bg: '#fefce8', color: '#ca8a04' },
  cancelled: { bg: '#fef2f2', color: '#dc2626' },
};

export const MentorshipCard = memo(function MentorshipCard({ mentorship }: { mentorship: AlumniMentorship }) {
  const sc = STATUS_COLORS[mentorship.status] || { bg: '#f3f4f6', color: '#6b7280' };
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 6, padding: 12, borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 4 }}>
        <span style={{ fontWeight: 'var(--weight-semibold)', fontSize: 'var(--text-body)' }}>{mentorship.mentorName}</span>
        <span style={{ padding: '1px 6px', borderRadius: 'var(--radius-full)', fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)', background: sc.bg, color: sc.color }}>{mentorship.status}</span>
      </div>
      <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>Mentoring {mentorship.menteeStudentName}</span>
      <div style={{ display: 'flex', gap: 12, fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
        <span>{mentorship.focusArea.replace(/-/g, ' ')}</span>
        <span>Sessions: {mentorship.sessionsCompleted}</span>
        <span>Hours: {mentorship.totalHours}</span>
      </div>
      <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
        Started: {mentorship.startDate} | Rating: {'★'.repeat(mentorship.rating)}{'☆'.repeat(5 - mentorship.rating)}
      </div>
    </div>
  );
});
