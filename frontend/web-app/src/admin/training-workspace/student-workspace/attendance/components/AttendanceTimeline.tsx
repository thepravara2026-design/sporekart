import { memo } from 'react';
import type { AttendanceTimelineEvent } from '../types';

interface AttendanceTimelineProps {
  events: AttendanceTimelineEvent[];
}

const eventIcons: Record<string, string> = {
  'enrollment': '📋', 'first-session': '🎯', 'attendance-recorded': '✅',
  'leave': '🏥', 'attendance-milestone': '🏆', 'course-completion': '🎓',
};

export const AttendanceTimeline = memo(function AttendanceTimeline({ events }: AttendanceTimelineProps) {
  if (events.length === 0) {
    return <div style={{ padding: 'var(--space-6)', textAlign: 'center', color: 'var(--color-text-tertiary)' }}>No timeline events</div>;
  }

  return (
    <div>
      <h3 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', margin: '0 0 var(--space-3) 0' }}>Attendance Timeline</h3>
      {events.map((event, i) => (
        <div key={event.id} style={{ display: 'flex', gap: 12, paddingBottom: i < events.length - 1 ? 16 : 0, position: 'relative' }}>
          <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', width: 24, flexShrink: 0 }}>
            <div style={{
              width: 24, height: 24, borderRadius: '50%', display: 'flex', alignItems: 'center', justifyContent: 'center',
              fontSize: 12, background: event.completed ? 'var(--color-bg-primary-subtle)' : 'var(--color-bg-skeleton-base)',
              border: `2px solid ${event.completed ? 'var(--color-primary)' : 'var(--color-border-default)'}`,
            }}>
              {eventIcons[event.type] || '📌'}
            </div>
            {i < events.length - 1 && <div style={{ width: 2, flex: 1, minHeight: 16, background: event.completed ? 'var(--color-primary)' : 'var(--color-border-default)' }} />}
          </div>
          <div style={{ flex: 1, paddingTop: 2 }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <span style={{ fontWeight: 'var(--weight-medium)', fontSize: 'var(--text-body-sm)' }}>{event.label}</span>
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{event.date}</span>
            </div>
            <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', margin: '2px 0 0 0' }}>{event.description}</p>
          </div>
        </div>
      ))}
    </div>
  );
});
