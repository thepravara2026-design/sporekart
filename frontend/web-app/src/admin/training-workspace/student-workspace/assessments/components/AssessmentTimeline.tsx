import { memo } from 'react';
import type { AssessmentTimelineEvent } from '../types';

interface AssessmentTimelineProps {
  events: AssessmentTimelineEvent[];
}

const eventIcons: Record<string, string> = {
  'created': '📝', 'published': '📢', 'scheduled': '📅',
  'attempt-started': '🚀', 'submitted': '📤', 'evaluated': '✅',
  'result-generated': '📊', 'certificate-eligible': '🎓',
};

export const AssessmentTimeline = memo(function AssessmentTimeline({ events }: AssessmentTimelineProps) {
  return (
    <div style={{ position: 'relative', paddingLeft: 32 }}>
      {events.map((event, i) => (
        <div key={event.id} style={{ position: 'relative', paddingBottom: 24 }}>
          <div style={{
            position: 'absolute', left: -24, top: 4,
            width: 16, height: 16, borderRadius: '50%',
            background: event.completed ? 'var(--color-primary)' : 'var(--color-bg-surface-default)',
            border: `2px solid ${event.completed ? 'var(--color-primary)' : 'var(--color-border-default)'}`,
            zIndex: 1,
          }} />
          {i < events.length - 1 && (
            <div style={{
              position: 'absolute', left: -17, top: 20, width: 2,
              height: 'calc(100% - 4px)',
              background: events[i + 1].completed ? 'var(--color-primary)' : 'var(--color-border-subtle)',
            }} />
          )}
          <div style={{ display: 'flex', alignItems: 'flex-start', gap: 8 }}>
            <span style={{ fontSize: 16 }}>{eventIcons[event.type] || '📌'}</span>
            <div style={{ flex: 1 }}>
              <div style={{
                fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)',
                color: event.completed ? 'var(--color-text-primary)' : 'var(--color-text-tertiary)',
              }}>
                {event.label}
              </div>
              <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
                {event.description}
              </div>
              <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', marginTop: 2 }}>
                {event.date}
              </div>
            </div>
          </div>
        </div>
      ))}
    </div>
  );
});
