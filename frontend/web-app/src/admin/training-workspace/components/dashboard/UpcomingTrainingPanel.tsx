import { memo } from 'react';
import { Card } from '../../../../design-system/components/composite/Card';
import { Badge } from '../../../../design-system/components/display/Badge';
import { Icon } from '../../../../design-system/icons/Icon';
import type { UpcomingTraining } from '../../data/mockData';

interface UpcomingTrainingPanelProps {
  trainings: UpcomingTraining[];
}

const STATUS_VARIANT: Record<string, 'info' | 'warning' | 'success' | 'danger'> = {
  scheduled: 'info',
  'in-progress': 'warning',
  completed: 'success',
  cancelled: 'danger',
};

const MODE_ICONS: Record<string, string> = {
  online: 'monitor',
  offline: 'home',
  hybrid: 'layers',
};

export const UpcomingTrainingPanel = memo(function UpcomingTrainingPanel({
  trainings,
}: UpcomingTrainingPanelProps) {
  return (
    <Card variant="default" padding="md" as="div">
      <h3 style={{
        margin: '0 0 var(--space-stack-sm)',
        fontSize: 'var(--text-h4)',
        fontWeight: 'var(--weight-semibold)',
        color: 'var(--color-text-primary)',
      }}>
        Upcoming Training Sessions
      </h3>
      {trainings.length === 0 ? (
        <p style={{ color: 'var(--color-text-secondary)', fontSize: 'var(--text-caption)', margin: 0 }}>
          No upcoming training sessions.
        </p>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-sm)' }}>
          {trainings.map((training) => (
            <div
              key={training.id}
              style={{
                display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-xs)',
                padding: 'var(--space-3)',
                background: 'var(--color-bg-background)',
                borderRadius: 'var(--radius-sm)',
                border: '1px solid var(--color-border-default)',
              }}
            >
              <div style={{
                display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start',
                gap: 'var(--space-inline-sm)',
              }}>
                <div style={{ minWidth: 0, flex: 1 }}>
                  <div style={{
                    fontWeight: 'var(--weight-semibold)',
                    fontSize: 'var(--text-body-sm)',
                    color: 'var(--color-text-primary)',
                    marginBottom: 2,
                    whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis',
                  }}>
                    {training.title}
                  </div>
                  <div style={{
                    fontSize: 'var(--text-caption)',
                    color: 'var(--color-text-secondary)',
                  }}>
                    {training.batch} &middot; {training.trainer}
                  </div>
                </div>
                <Badge variant={STATUS_VARIANT[training.status]} size="sm">
                  {training.status === 'in-progress' ? 'In Progress' : training.status.charAt(0).toUpperCase() + training.status.slice(1)}
                </Badge>
              </div>
              <div style={{
                display: 'flex', flexWrap: 'wrap', gap: 'var(--space-inline-md)',
                fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)',
              }}>
                <span style={{ display: 'flex', alignItems: 'center', gap: 4 }}>
                  <Icon name={MODE_ICONS[training.mode]} size={12} color="currentColor" />
                  {training.mode.charAt(0).toUpperCase() + training.mode.slice(1)}
                </span>
                <span style={{ display: 'flex', alignItems: 'center', gap: 4 }}>
                  <Icon name="calendar" size={12} color="currentColor" />
                  {training.date}
                </span>
                <span style={{ display: 'flex', alignItems: 'center', gap: 4 }}>
                  <Icon name="users" size={12} color="currentColor" />
                  {training.seatsRemaining}/{training.capacity} seats
                </span>
                <span style={{ display: 'flex', alignItems: 'center', gap: 4 }}>
                  <Icon name="map-pin" size={12} color="currentColor" />
                  {training.venue}
                </span>
              </div>
            </div>
          ))}
        </div>
      )}
    </Card>
  );
});
