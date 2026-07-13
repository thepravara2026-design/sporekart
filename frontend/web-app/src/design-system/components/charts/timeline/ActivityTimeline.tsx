import React from 'react';

export interface ActivityTimelineItem {
  id: string;
  user: {
    name: string;
    avatar?: string;
  };
  action: string;
  target?: string;
  timestamp: string | Date;
  type?: 'create' | 'update' | 'delete' | 'comment' | 'upload';
}

export interface ActivityTimelineProps {
  items: ActivityTimelineItem[];
  className?: string;
  style?: React.CSSProperties;
}

function getRelativeTime(date: string | Date): string {
  const d = date instanceof Date ? date : new Date(date);
  const now = new Date();
  const diffMs = now.getTime() - d.getTime();
  const mins = Math.floor(diffMs / 60000);
  if (mins < 1) return 'Just now';
  if (mins < 60) return `${mins}m ago`;
  const hrs = Math.floor(mins / 60);
  if (hrs < 24) return `${hrs}h ago`;
  const days = Math.floor(hrs / 24);
  if (days < 7) return `${days}d ago`;
  return d.toLocaleDateString();
}

const typeIcons: Record<string, string> = {
  create: '+',
  update: '\u270E',
  delete: '\u2716',
  comment: '\uD83D\uDCAC',
  upload: '\u2191',
};

const typeColors: Record<string, string> = {
  create: 'var(--color-success-500)',
  update: 'var(--color-info-500)',
  delete: 'var(--color-danger-500)',
  comment: 'var(--color-text-secondary)',
  upload: 'var(--color-warning-500)',
};

function getInitials(name: string): string {
  return name
    .split(' ')
    .map((p) => p[0])
    .join('')
    .toUpperCase()
    .slice(0, 2);
}

export const ActivityTimeline: React.FC<ActivityTimelineProps> = ({
  items,
  className = '',
  style,
}) => {
  const containerStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-4)',
    ...style,
  };

  const rowStyle: React.CSSProperties = {
    display: 'flex',
    gap: 'var(--space-3)',
    alignItems: 'flex-start',
  };

  const avatarStyle: React.CSSProperties = {
    width: 36,
    height: 36,
    borderRadius: 'var(--radius-avatar)',
    flexShrink: 0,
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    fontSize: 'var(--text-caption)',
    fontWeight: 'var(--weight-semibold)',
    color: 'var(--color-text-on-primary)',
    backgroundColor: 'var(--color-bg-primary-default)',
    overflow: 'hidden',
    backgroundSize: 'cover',
    backgroundPosition: 'center',
  };

  const contentStyle: React.CSSProperties = {
    flex: 1,
    minWidth: 0,
  };

  const textStyle: React.CSSProperties = {
    fontSize: 'var(--text-body-sm)',
    color: 'var(--color-text-primary)',
    lineHeight: 'var(--leading-normal)',
  };

  const actionSpan: React.CSSProperties = {
    fontWeight: 'var(--weight-semibold)',
  };

  const targetSpan: React.CSSProperties = {
    color: 'var(--color-text-secondary)',
  };

  const timeStyle: React.CSSProperties = {
    fontSize: 'var(--text-caption)',
    color: 'var(--color-text-disabled)',
    marginTop: 'var(--space-1)',
  };

  const iconDotStyle: React.CSSProperties = {
    width: 24,
    height: 24,
    borderRadius: 'var(--radius-full)',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    fontSize: 'var(--text-caption)',
    color: 'var(--color-text-on-primary)',
    flexShrink: 0,
    marginTop: 6,
  };

  return (
    <div
      className={`sk-activity-timeline ${className}`.trim()}
      style={containerStyle}
      role="list"
      aria-label="Activity timeline"
    >
      {items.map((item) => {
        const typeColor = typeColors[item.type || 'comment'] || 'var(--color-text-secondary)';
        const typeIcon = typeIcons[item.type || 'comment'] || '\u2022';

        return (
          <div
            key={item.id}
            style={rowStyle}
            role="listitem"
            aria-label={`${item.user.name} ${item.action}${item.target ? ` ${item.target}` : ''}`}
          >
            <div
              style={{
                ...iconDotStyle,
                backgroundColor: typeColor,
              }}
              aria-hidden="true"
            >
              {typeIcon}
            </div>
            <div style={contentStyle}>
              <div style={textStyle}>
                <span style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-2)' }}>
                  <span
                    style={{
                      ...avatarStyle,
                      ...(item.user.avatar
                        ? { backgroundImage: `url(${item.user.avatar})` }
                        : {}),
                    }}
                    aria-label={item.user.name}
                  >
                    {!item.user.avatar && getInitials(item.user.name)}
                  </span>
                  <span>
                    <span style={actionSpan}>{item.user.name}</span>
                    {' '}{item.action}
                    {item.target && <span style={targetSpan}> {item.target}</span>}
                  </span>
                </span>
              </div>
              <div style={timeStyle}>{getRelativeTime(item.timestamp)}</div>
            </div>
          </div>
        );
      })}
    </div>
  );
};

ActivityTimeline.displayName = 'ActivityTimeline';
