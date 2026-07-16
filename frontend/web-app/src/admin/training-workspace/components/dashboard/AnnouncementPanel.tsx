import { memo } from 'react';
import { Card } from '../../../../design-system/components/composite/Card';
import { Badge } from '../../../../design-system/components/display/Badge';
import type { Announcement } from '../../data/mockData';

interface AnnouncementPanelProps {
  announcements: Announcement[];
}

const PRIORITY_VARIANT: Record<string, 'danger' | 'warning' | 'info'> = {
  high: 'danger',
  normal: 'warning',
  low: 'info',
};

export const AnnouncementPanel = memo(function AnnouncementPanel({
  announcements,
}: AnnouncementPanelProps) {
  return (
    <Card variant="default" padding="md" as="div">
      <h3 style={{
        margin: '0 0 var(--space-stack-sm)',
        fontSize: 'var(--text-h4)',
        fontWeight: 'var(--weight-semibold)',
        color: 'var(--color-text-primary)',
      }}>
        Announcements
      </h3>
      {announcements.length === 0 ? (
        <p style={{ color: 'var(--color-text-secondary)', fontSize: 'var(--text-caption)', margin: 0 }}>
          No announcements at this time.
        </p>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-sm)' }}>
          {announcements.map((ann) => (
            <div
              key={ann.id}
              style={{
                padding: 'var(--space-3)',
                background: 'var(--color-bg-background)',
                borderRadius: 'var(--radius-sm)',
                border: '1px solid var(--color-border-default)',
              }}
            >
              <div style={{
                display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start',
                marginBottom: 'var(--space-stack-xs)',
              }}>
                <span style={{
                  fontWeight: 'var(--weight-semibold)',
                  fontSize: 'var(--text-body-sm)',
                  color: 'var(--color-text-primary)',
                }}>
                  {ann.title}
                </span>
                <Badge variant={PRIORITY_VARIANT[ann.priority]} size="sm">
                  {ann.priority}
                </Badge>
              </div>
              <p style={{
                margin: '0 0 var(--space-stack-xs)',
                fontSize: 'var(--text-caption)',
                color: 'var(--color-text-secondary)',
                lineHeight: 'var(--leading-normal)',
              }}>
                {ann.content}
              </p>
              <span style={{
                fontSize: 'var(--text-caption)',
                color: 'var(--color-text-tertiary)',
              }}>
                {ann.date}
              </span>
            </div>
          ))}
        </div>
      )}
    </Card>
  );
});
