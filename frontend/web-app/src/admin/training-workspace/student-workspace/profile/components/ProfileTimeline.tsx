import type { TimelineEvent } from '../types';

interface ProfileTimelineProps {
  events: TimelineEvent[];
  max?: number;
}

const typeIcons: Record<string, string> = {
  registration: '👤',
  'profile-created': '📝',
  enrollment: '📚',
  attendance: '✅',
  assignment: '📄',
  assessment: '🎯',
  certificate: '⭐',
  placement: '💼',
  alumni: '🎓',
  'profile-update': '✏️',
  'document-upload': '📎',
  verification: '🔍',
};

export function ProfileTimeline({ events, max }: ProfileTimelineProps) {
  const displayed = max ? events.slice(0, max) : events;

  return (
    <div className="profile-timeline">
      <h3 className="profile-timeline__title">Activity Timeline</h3>
      <div className="profile-timeline__list">
        {displayed.map((event, index) => (
          <div
            key={event.id}
            className={`profile-timeline__item ${!event.completed ? 'profile-timeline__item--pending' : ''}`}
          >
            <div className="profile-timeline__marker">
              <div className={`profile-timeline__dot ${event.completed ? 'profile-timeline__dot--completed' : ''}`}>
                {typeIcons[event.type] || '📌'}
              </div>
              {index < displayed.length - 1 && <div className="profile-timeline__line" />}
            </div>
            <div className="profile-timeline__content">
              <div className="profile-timeline__content-header">
                <span className="profile-timeline__label">{event.label}</span>
                <span className="profile-timeline__date">{event.date}</span>
              </div>
              <p className="profile-timeline__description">{event.description}</p>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
