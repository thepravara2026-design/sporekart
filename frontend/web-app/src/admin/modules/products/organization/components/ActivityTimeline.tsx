import React from 'react';
import Icon from '../../../../../design-system/icons/Icon';
import type { OrgActivityEvent } from '../types';

interface ActivityTimelineProps {
  events: OrgActivityEvent[];
  maxItems?: number;
}

const TYPE_ICONS: Record<string, string> = {
  created: 'plus-circle', edited: 'edit', moved: 'move', merged: 'git-merge',
  assigned: 'link', removed: 'trash-2', archived: 'archive', restored: 'rotate-ccw', viewed: 'eye',
};

const TYPE_COLORS: Record<string, string> = {
  created: 'var(--color-success)', edited: 'var(--color-accent-blue)', moved: 'var(--color-accent-orange)',
  merged: 'var(--color-accent-purple)', assigned: 'var(--color-accent-cyan)', removed: 'var(--color-text-danger)',
  archived: 'var(--color-accent-yellow)', restored: 'var(--color-accent-green)', viewed: 'var(--color-text-tertiary)',
};

function formatTimestamp(iso: string): string {
  const d = new Date(iso);
  const now = new Date();
  const diff = now.getTime() - d.getTime();
  if (diff < 3600000) return `${Math.floor(diff / 60000)}m ago`;
  if (diff < 86400000) return `${Math.floor(diff / 3600000)}h ago`;
  return d.toLocaleDateString('en-IN', { day: 'numeric', month: 'short' });
}

export const ActivityTimeline = React.memo(function ActivityTimeline({ events, maxItems = 10 }: ActivityTimelineProps) {
  const displayed = events.slice(0, maxItems);

  if (displayed.length === 0) {
    return (
      <div style={{ padding: 'var(--space-8)', textAlign: 'center', color: 'var(--color-text-tertiary)', fontSize: 'var(--text-body-sm)' }}>
        No activity recorded yet.
      </div>
    );
  }

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 0 }}>
      {displayed.map((event, idx) => {
        const isLast = idx === displayed.length - 1;
        return (
          <div key={event.id} style={{ display: 'flex', gap: 12, position: 'relative', paddingBottom: isLast ? 0 : 16 }}>
            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', width: 28 }}>
              <div style={{
                width: 28, height: 28, borderRadius: 'var(--radius-full)',
                display: 'flex', alignItems: 'center', justifyContent: 'center',
                background: TYPE_COLORS[event.type] || 'var(--color-bg-surface-raised)',
                color: '#fff', flexShrink: 0, zIndex: 1,
              }}>
                <Icon name={TYPE_ICONS[event.type] || 'circle'} size={14} />
              </div>
              {!isLast && <div style={{ width: 2, flex: 1, background: 'var(--color-border-default)', marginTop: 2 }} />}
            </div>
            <div style={{ flex: 1, minWidth: 0, paddingBottom: isLast ? 0 : 4 }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: 6, flexWrap: 'wrap' }}>
                <span style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-medium)', color: 'var(--color-text-primary)' }}>{event.actor}</span>
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{formatTimestamp(event.timestamp)}</span>
              </div>
              <p style={{ margin: '2px 0 0', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', lineHeight: 'var(--leading-relaxed)' }}>{event.message}</p>
            </div>
          </div>
        );
      })}
    </div>
  );
});

export default ActivityTimeline;
