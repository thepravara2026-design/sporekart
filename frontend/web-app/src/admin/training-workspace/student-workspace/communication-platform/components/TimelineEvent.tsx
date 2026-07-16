import { memo } from 'react';
import type { TimelineEvent as TLEvent } from '../types';

const STAGE_ICONS: Record<string, string> = {
  enrollment: '📝', 'course-assigned': '📚', 'assignment-reminder': '📄',
  'attendance-alert': '✅', 'assessment-reminder': '📝', achievement: '🏆',
  'certificate-issued': '🎓', completion: '🎉', alumni: '🌟',
};

export const TimelineEvent = memo(function TimelineEvent({ event, isLast }: { event: TLEvent; isLast: boolean }) {
  return (
    <div style={{ display: 'flex', gap: 12, position: 'relative', paddingBottom: isLast ? 0 : 16 }}>
      <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', flexShrink: 0 }}>
        <div style={{ width: 32, height: 32, borderRadius: '50%', background: '#eff6ff', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 14, border: '2px solid #2563eb' }}>
          {STAGE_ICONS[event.stage] || '📌'}
        </div>
        {!isLast && <div style={{ width: 2, flex: 1, background: 'var(--color-border-subtle)', minHeight: 16 }} />}
      </div>
      <div style={{ flex: 1, paddingBottom: isLast ? 0 : 12 }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 4 }}>
          <h4 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>{event.title}</h4>
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{new Date(event.date).toLocaleDateString()}</span>
        </div>
        <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '2px 0 0' }}>{event.description}</p>
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{event.studentName}</span>
      </div>
    </div>
  );
});
