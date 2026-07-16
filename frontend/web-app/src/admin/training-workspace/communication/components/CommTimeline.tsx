import { memo } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import type { TimelineEvent, TimelineEventType } from '../data/communicationTypes';
import { formatDateTime } from '../data/communicationFormatters';
import type { ToneIntent } from '../data/communicationOptions';
import { toneTokens } from '../data/communicationFormatters';

export interface CommTimelineProps {
  events: TimelineEvent[];
}

const EVENT_ICON: Record<TimelineEventType, string> = {
  created: 'edit',
  scheduled: 'clock',
  published: 'send',
  archived: 'archive',
  viewed: 'eye',
  delivered: 'check-circle',
  failed: 'x-circle',
  retry: 'refresh-cw',
};

const EVENT_TONE: Record<TimelineEventType, ToneIntent> = {
  created: 'neutral',
  scheduled: 'info',
  published: 'success',
  archived: 'neutral',
  viewed: 'info',
  delivered: 'success',
  failed: 'danger',
  retry: 'warning',
};

const CommTimeline = memo(function CommTimeline({ events }: CommTimelineProps) {
  if (!events.length) {
    return (
      <p style={{ margin: 0, fontSize: 'var(--text-body-sm)', color: 'var(--color-text-muted)' }}>
        No timeline activity.
      </p>
    );
  }

  return (
    <ol style={{ listStyle: 'none', margin: 0, padding: 0, display: 'flex', flexDirection: 'column' }}>
      {events.map((ev, i) => {
        const tokens = toneTokens(EVENT_TONE[ev.type]);
        const last = i === events.length - 1;
        return (
          <li key={ev.id} style={{ display: 'flex', gap: 'var(--space-3)' }}>
            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
              <span
                aria-hidden
                style={{
                  display: 'inline-flex', alignItems: 'center', justifyContent: 'center',
                  width: 28, height: 28, borderRadius: '50%', flexShrink: 0,
                  background: tokens.bg, color: tokens.fg, border: `1px solid ${tokens.border}`,
                }}
              >
                <Icon name={EVENT_ICON[ev.type]} size={14} />
              </span>
              {!last && <span style={{ flex: 1, width: 2, background: 'var(--color-border-default)', minHeight: 16 }} />}
            </div>
            <div style={{ paddingBottom: last ? 0 : 'var(--space-3)', minWidth: 0 }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-2)', flexWrap: 'wrap' }}>
                <span style={{ fontSize: 'var(--text-body-sm)', fontWeight: 600, color: 'var(--color-text-primary)' }}>
                  {ev.label}
                </span>
                {ev.placeholder && (
                  <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-muted)', fontStyle: 'italic' }}>
                    (placeholder)
                  </span>
                )}
              </div>
              <p style={{ margin: '2px 0 0', fontSize: 'var(--text-caption)', color: 'var(--color-text-muted)' }}>
                {ev.actor} · {formatDateTime(ev.timestamp)}
              </p>
              {ev.detail && (
                <p style={{ margin: '2px 0 0', fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
                  {ev.detail}
                </p>
              )}
            </div>
          </li>
        );
      })}
    </ol>
  );
});

export default CommTimeline;
