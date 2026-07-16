import { memo } from 'react';
import { Card } from '../../../../design-system/components/composite/Card';
import { Icon } from '../../../../design-system/icons/Icon';
import type { ActivityItem } from '../../data/mockData';

interface ActivityTimelineProps {
  activities: ActivityItem[];
}

const ACTIVITY_ICONS: Record<string, string> = {
  course: 'book-open',
  batch: 'calendar',
  trainer: 'user-check',
  student: 'user-plus',
  resource: 'folder',
  assessment: 'target',
  certificate: 'award',
  announcement: 'message-circle',
};

export const ActivityTimeline = memo(function ActivityTimeline({
  activities,
}: ActivityTimelineProps) {
  return (
    <Card variant="default" padding="md" as="div">
      <h3 style={{
        margin: '0 0 var(--space-stack-sm)',
        fontSize: 'var(--text-h4)',
        fontWeight: 'var(--weight-semibold)',
        color: 'var(--color-text-primary)',
      }}>
        Recent Activity
      </h3>
      {activities.length === 0 ? (
        <p style={{ color: 'var(--color-text-secondary)', fontSize: 'var(--text-caption)', margin: 0 }}>
          No recent activity to display.
        </p>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column' }}>
          {activities.map((activity, i) => (
            <div
              key={activity.id}
              style={{
                display: 'flex', gap: 'var(--space-inline-sm)',
                padding: 'var(--space-2) 0',
                position: 'relative',
              }}
            >
              <div style={{
                display: 'flex', flexDirection: 'column', alignItems: 'center',
                width: 28, flexShrink: 0,
              }}>
                <span style={{
                  width: 28, height: 28, borderRadius: 'var(--radius-full)',
                  background: 'var(--color-bg-primary-subtle)',
                  color: 'var(--color-primary)',
                  display: 'flex', alignItems: 'center', justifyContent: 'center',
                }}>
                  <Icon name={ACTIVITY_ICONS[activity.type] || 'activity'} size={14} color="currentColor" />
                </span>
                {i < activities.length - 1 && (
                  <span style={{
                    width: 1, flex: 1,
                    background: 'var(--color-border-default)',
                    marginTop: 4,
                  }} />
                )}
              </div>
              <div style={{
                flex: 1, paddingBottom: i < activities.length - 1 ? 'var(--space-2)' : 0,
                minWidth: 0,
              }}>
                <p style={{
                  margin: '0 0 2px', fontSize: 'var(--text-body-sm)',
                  color: 'var(--color-text-primary)', lineHeight: 'var(--leading-normal)',
                }}>
                  {activity.message}
                </p>
                <div style={{
                  display: 'flex', gap: 'var(--space-inline-sm)',
                  fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)',
                }}>
                  <span>{activity.timestamp}</span>
                  <span>by {activity.user}</span>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </Card>
  );
});
