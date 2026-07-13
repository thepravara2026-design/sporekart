import React from 'react';

export interface TrainingSession {
  id: string;
  title: string;
  date: string | Date;
  duration: string;
  instructor?: string;
  status: 'upcoming' | 'in-progress' | 'completed';
  progress?: number;
}

export interface TrainingTimelineProps {
  sessions: TrainingSession[];
  className?: string;
  style?: React.CSSProperties;
}

const statusColors: Record<string, string> = {
  upcoming: 'var(--color-info-500)',
  'in-progress': 'var(--color-warning-500)',
  completed: 'var(--color-success-500)',
};

const statusBgColors: Record<string, string> = {
  upcoming: 'var(--color-info-50)',
  'in-progress': 'var(--color-warning-50)',
  completed: 'var(--color-success-50)',
};

export const TrainingTimeline: React.FC<TrainingTimelineProps> = ({
  sessions,
  className = '',
  style,
}) => {
  const containerStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-4)',
    ...style,
  };

  const cardStyle: React.CSSProperties = {
    display: 'flex',
    gap: 'var(--space-4)',
    padding: 'var(--space-4)',
    borderRadius: 'var(--radius-card)',
    backgroundColor: 'var(--color-bg-surface-default)',
    border: '1px solid var(--color-border-default)',
    alignItems: 'flex-start',
    position: 'relative',
    transition: 'box-shadow var(--duration-fast) var(--easing-standard)',
  };

  const dateBadgeStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center',
    minWidth: 56,
    padding: 'var(--space-2)',
    borderRadius: 'var(--radius-sm)',
    backgroundColor: 'var(--color-bg-background)',
    flexShrink: 0,
  };

  const dateDayStyle: React.CSSProperties = {
    fontSize: 'var(--text-h4)',
    fontWeight: 'var(--weight-bold)',
    color: 'var(--color-text-primary)',
    lineHeight: 1,
  };

  const dateMonthStyle: React.CSSProperties = {
    fontSize: 'var(--text-caption)',
    color: 'var(--color-text-secondary)',
    textTransform: 'uppercase' as const,
    fontWeight: 'var(--weight-medium)',
  };

  const contentStyle: React.CSSProperties = {
    flex: 1,
    minWidth: 0,
  };

  const titleStyle: React.CSSProperties = {
    fontSize: 'var(--text-body)',
    fontWeight: 'var(--weight-semibold)',
    color: 'var(--color-text-primary)',
    marginBottom: 'var(--space-1)',
  };

  const metaStyle: React.CSSProperties = {
    display: 'flex',
    flexWrap: 'wrap',
    gap: 'var(--space-3)',
    fontSize: 'var(--text-body-sm)',
    color: 'var(--color-text-secondary)',
    marginTop: 'var(--space-1)',
  };

  const metaItemStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    gap: 'var(--space-1)',
  };

  const badgeStyle: React.CSSProperties = {
    display: 'inline-flex',
    alignItems: 'center',
    padding: '2px var(--space-2)',
    borderRadius: 'var(--radius-badge)',
    fontSize: 'var(--text-caption)',
    fontWeight: 'var(--weight-medium)',
  };

  const progressTrackStyle: React.CSSProperties = {
    height: 4,
    borderRadius: 'var(--radius-full)',
    backgroundColor: 'var(--color-neutral-200)',
    marginTop: 'var(--space-2)',
    overflow: 'hidden',
  };

  const progressFillStyle: React.CSSProperties = {
    height: '100%',
    borderRadius: 'var(--radius-full)',
    transition: 'width var(--duration-slow) var(--easing-standard)',
  };

  return (
    <div
      className={`sk-training-timeline ${className}`.trim()}
      style={containerStyle}
      role="list"
      aria-label="Training sessions timeline"
    >
      {sessions.map((session) => {
        const dt = session.date instanceof Date ? session.date : new Date(session.date);
        const day = dt.getDate();
        const month = dt.toLocaleDateString('en-US', { month: 'short' });
        const sc = statusColors[session.status] || 'var(--color-text-secondary)';
        const sbg = statusBgColors[session.status] || 'var(--color-neutral-100)';
        const isInProgress = session.status === 'in-progress';

        return (
          <div
            key={session.id}
            style={cardStyle}
            role="listitem"
            aria-label={`${session.title} - ${session.status}`}
          >
            <div style={dateBadgeStyle}>
              <span style={dateDayStyle}>{day}</span>
              <span style={dateMonthStyle}>{month}</span>
            </div>
            <div style={contentStyle}>
              <div style={titleStyle}>{session.title}</div>
              <div style={metaStyle}>
                <span style={metaItemStyle}>
                  {'\u23F1'}
                  {session.duration}
                </span>
                {session.instructor && (
                  <span style={metaItemStyle}>
                    {'\uD83D\uDC64'}
                    {session.instructor}
                  </span>
                )}
                <span
                  style={{
                    ...badgeStyle,
                    backgroundColor: sbg,
                    color: sc,
                  }}
                >
                  {session.status}
                </span>
              </div>
              {session.progress !== undefined && isInProgress && (
                <div style={progressTrackStyle}>
                  <div
                    style={{
                      ...progressFillStyle,
                      width: `${session.progress}%`,
                      backgroundColor: sc,
                    }}
                  />
                </div>
              )}
              {session.progress !== undefined && !isInProgress && (
                <div style={progressTrackStyle}>
                  <div
                    style={{
                      ...progressFillStyle,
                      width: `${session.progress}%`,
                      backgroundColor: sc,
                    }}
                  />
                </div>
              )}
            </div>
          </div>
        );
      })}
    </div>
  );
};

TrainingTimeline.displayName = 'TrainingTimeline';
