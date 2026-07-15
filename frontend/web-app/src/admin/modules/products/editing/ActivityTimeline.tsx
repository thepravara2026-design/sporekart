import React, { memo } from 'react';
import type { EditActivityEvent, EditActivityType } from './types';
import Card from '../../../../design-system/components/composite/Card';
import Icon from '../../../../design-system/icons/Icon';
import StatusBadge from '../../../components/status/StatusBadge';

export interface ActivityTimelineProps {
  events: EditActivityEvent[];
}

type BadgeVariant = 'default' | 'success' | 'warning' | 'danger' | 'info' | 'neutral';

const TYPE_LABELS: Record<EditActivityType, string> = {
  created: 'Created',
  edited: 'Edited',
  draft_saved: 'Draft Saved',
  review_requested: 'Review Requested',
  approved: 'Approved',
  rejected: 'Rejected',
  published: 'Published',
  scheduled: 'Scheduled',
  archived: 'Archived',
  restored: 'Restored',
  duplicated: 'Duplicated',
  deleted: 'Deleted',
  viewed: 'Viewed',
  comment: 'Comment',
};

const TYPE_ICONS: Record<EditActivityType, string> = {
  created: 'Check',
  edited: 'Edit',
  draft_saved: 'Edit',
  review_requested: 'Bell',
  approved: 'Check',
  rejected: 'X',
  published: 'Upload',
  scheduled: 'Clock',
  archived: 'Archive',
  restored: 'RefreshCw',
  duplicated: 'Copy',
  deleted: 'Trash',
  viewed: 'Eye',
  comment: 'Info',
};

function badgeVariant(type: EditActivityType): BadgeVariant {
  switch (type) {
    case 'published':
    case 'approved':
      return 'success';
    case 'archived':
      return 'neutral';
    case 'deleted':
      return 'danger';
    case 'review_requested':
    case 'scheduled':
      return 'info';
    case 'edited':
    case 'draft_saved':
      return 'warning';
    default:
      return 'default';
  }
}

function formatTimestamp(timestamp: string): string {
  const date = new Date(timestamp);
  if (Number.isNaN(date.getTime())) return timestamp;
  return date.toLocaleString();
}

const TimelineItem: React.FC<{ event: EditActivityEvent; isLast: boolean }> = ({ event, isLast }) => {
  const label = TYPE_LABELS[event.type];
  const iconName = TYPE_ICONS[event.type];
  const formatted = formatTimestamp(event.timestamp);
  const ariaLabel = `${label} by ${event.actor} on ${formatted}: ${event.message}`;

  return (
    <li
      role="listitem"
      aria-label={ariaLabel}
      style={{
        display: 'flex',
        gap: 'var(--space-3)',
        alignItems: 'stretch',
      }}
    >
      <div
        style={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          flexShrink: 0,
        }}
      >
        <span
          aria-hidden="true"
          style={{
            display: 'inline-flex',
            alignItems: 'center',
            justifyContent: 'center',
            width: 32,
            height: 32,
            borderRadius: 'var(--radius-full)',
            background: 'var(--color-bg-surface-raised)',
            border: 'var(--border-width-thin, 1px) solid var(--color-border-subtle)',
            color: 'var(--color-primary)',
            flexShrink: 0,
          }}
        >
          <Icon name={iconName} size={16} />
        </span>
        {!isLast && (
          <span
            aria-hidden="true"
            style={{
              flex: 1,
              width: 2,
              minHeight: 'var(--space-4)',
              background: 'var(--color-border)',
              marginTop: 'var(--space-1)',
            }}
          />
        )}
      </div>

      <div
        style={{
          display: 'flex',
          flexDirection: 'column',
          gap: 'var(--space-1)',
          paddingBottom: isLast ? 0 : 'var(--space-4)',
          minWidth: 0,
          flex: 1,
        }}
      >
        <div
          style={{
            display: 'flex',
            flexWrap: 'wrap',
            alignItems: 'center',
            gap: 'var(--space-2)',
          }}
        >
          <span
            style={{
              fontSize: 'var(--text-body-sm)',
              fontWeight: 'var(--weight-semibold)',
              color: 'var(--color-text-primary)',
            }}
          >
            {label}
          </span>
          <StatusBadge status={label} variant={badgeVariant(event.type)} />
        </div>

        <p
          style={{
            margin: 0,
            fontSize: 'var(--text-body-sm)',
            color: 'var(--color-text-primary)',
            wordBreak: 'break-word',
          }}
        >
          {event.message}
        </p>

        <div
          style={{
            display: 'flex',
            flexWrap: 'wrap',
            alignItems: 'center',
            gap: 'var(--space-2)',
            fontSize: 'var(--text-caption)',
            color: 'var(--color-text-secondary)',
          }}
        >
          <span style={{ fontWeight: 'var(--weight-medium)' }}>{event.actor}</span>
          <span aria-hidden="true">·</span>
          <time dateTime={event.timestamp}>{formatted}</time>
        </div>
      </div>
    </li>
  );
};

const ActivityTimeline: React.FC<ActivityTimelineProps> = ({ events }) => {
  return (
    <Card variant="outlined" padding="lg" aria-label="Activity Timeline">
      <div
        style={{
          display: 'flex',
          alignItems: 'center',
          gap: 'var(--space-2)',
          marginBottom: 'var(--space-4)',
        }}
      >
        <Icon name="Activity" size={18} />
        <h3
          style={{
            margin: 0,
            fontSize: 'var(--text-body)',
            fontWeight: 'var(--weight-semibold)',
            color: 'var(--color-text-primary)',
          }}
        >
          Activity Timeline
        </h3>
      </div>

      {events.length === 0 ? (
        <p
          style={{
            margin: 0,
            fontSize: 'var(--text-body-sm)',
            color: 'var(--color-text-secondary)',
          }}
        >
          No activity recorded yet.
        </p>
      ) : (
        <ol
          role="list"
          style={{
            listStyle: 'none',
            margin: 0,
            padding: 0,
            display: 'flex',
            flexDirection: 'column',
          }}
        >
          {events.map((event, index) => (
            <TimelineItem
              key={event.id}
              event={event}
              isLast={index === events.length - 1}
            />
          ))}
        </ol>
      )}
    </Card>
  );
};

ActivityTimeline.displayName = 'ActivityTimeline';

export default memo(ActivityTimeline);
