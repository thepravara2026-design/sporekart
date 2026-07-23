import React from 'react';

interface TimelineEvent {
  id: string;
  type: 'message' | 'handoff' | 'collaboration' | 'session' | 'error';
  title: string;
  description?: string;
  copilotName?: string;
  copilotId?: string;
  timestamp: string;
}

interface ActivityTimelineProps {
  events: TimelineEvent[];
  maxItems?: number;
  style?: React.CSSProperties;
}

const EVENT_ICONS: Record<string, string> = {
  message: '💬',
  handoff: '↪',
  collaboration: '🤝',
  session: '🔌',
  error: '⚠️',
};

function formatRelativeTime(ts: string): string {
  try {
    const diff = Date.now() - new Date(ts).getTime();
    const mins = Math.floor(diff / 60000);
    if (mins < 1) return 'just now';
    if (mins < 60) return `${mins}m ago`;
    const hours = Math.floor(mins / 60);
    if (hours < 24) return `${hours}h ago`;
    const days = Math.floor(hours / 24);
    return `${days}d ago`;
  } catch {
    return '';
  }
}

export default function ActivityTimeline({
  events,
  maxItems = 50,
  style,
}: ActivityTimelineProps) {
  const display = events.slice(0, maxItems);

  return (
    <div style={{
      padding: '16px',
      display: 'flex',
      flexDirection: 'column',
      gap: '0',
      ...style,
    }}>
      <div style={{ fontWeight: 600, fontSize: '14px', marginBottom: '12px', color: 'var(--cp-text, #1e293b)' }}>
        Activity Timeline
      </div>

      {display.length === 0 ? (
        <div style={{ padding: '24px', textAlign: 'center', color: 'var(--cp-text-secondary, #64748b)', fontSize: '13px' }}>
          No recent activity
        </div>
      ) : (
        <div style={{ position: 'relative' }}>
          <div style={{
            position: 'absolute',
            left: '15px',
            top: '8px',
            bottom: '8px',
            width: '2px',
            background: 'var(--cp-border, #e2e8f0)',
          }} />
          {display.map((event, i) => (
            <div
              key={event.id}
              style={{
                display: 'flex',
                gap: '12px',
                paddingBottom: i < display.length - 1 ? '12px' : '0',
                position: 'relative',
              }}
            >
              <div style={{
                width: '32px',
                height: '32px',
                borderRadius: '50%',
                background: 'var(--cp-surface-2, #f1f5f9)',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                fontSize: '14px',
                flexShrink: 0,
                zIndex: 1,
              }}>
                {EVENT_ICONS[event.type] ?? '•'}
              </div>
              <div style={{ flex: 1, minWidth: 0, paddingTop: '4px' }}>
                <div style={{
                  display: 'flex',
                  justifyContent: 'space-between',
                  alignItems: 'flex-start',
                }}>
                  <div style={{ fontWeight: 500, fontSize: '13px', color: 'var(--cp-text, #1e293b)' }}>
                    {event.title}
                    {event.copilotName && (
                      <span style={{ color: 'var(--cp-text-secondary, #64748b)', fontWeight: 400 }}>
                        {' '}· {event.copilotName}
                      </span>
                    )}
                  </div>
                  <div style={{ fontSize: '11px', color: 'var(--cp-text-secondary, #94a3b8)', flexShrink: 0, marginLeft: '8px' }}>
                    {formatRelativeTime(event.timestamp)}
                  </div>
                </div>
                {event.description && (
                  <div style={{
                    fontSize: '12px',
                    color: 'var(--cp-text-secondary, #64748b)',
                    marginTop: '2px',
                    overflow: 'hidden',
                    textOverflow: 'ellipsis',
                    whiteSpace: 'nowrap',
                  }}>
                    {event.description}
                  </div>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
