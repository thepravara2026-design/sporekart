import { memo } from 'react';
import { useNavigate } from 'react-router-dom';
import { useWorkspaceState } from '../state/workspaceState';
import { StatWidget } from '../components/dashboard/StatWidget';
import { QuickActionPanel } from '../components/dashboard/QuickActionPanel';
import { ActivityTimeline } from '../components/dashboard/ActivityTimeline';
import { UpcomingTrainingPanel } from '../components/dashboard/UpcomingTrainingPanel';
import { AnnouncementPanel } from '../components/dashboard/AnnouncementPanel';
import { Card } from '../../../design-system/components/composite/Card';
import { Icon } from '../../../design-system/icons/Icon';

const TrainingDashboardPage = memo(function TrainingDashboardPage() {
  const {
    data,
    filteredStats,
    filteredActivities,
    filteredUpcoming,
  } = useWorkspaceState();
  const navigate = useNavigate();

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div style={{
        background: 'linear-gradient(135deg, var(--color-primary) 0%, var(--color-primary-dark, #1a5c3a) 100%)',
        borderRadius: 'var(--radius-card)',
        padding: 'var(--space-stack-lg) var(--space-page-x)',
        color: 'var(--color-text-on-primary)',
      }}>
        <h2 style={{ margin: '0 0 var(--space-stack-xs)', fontSize: 'var(--text-h2)' }}>
          Training Workspace
        </h2>
        <p style={{ margin: 0, opacity: 0.85, fontSize: 'var(--text-body)' }}>
          Enterprise Learning Management Platform — manage courses, batches, students, trainers, and certifications.
        </p>
      </div>

      <div style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))',
        gap: 'var(--space-component-gap)',
      }}>
        {filteredStats.slice(0, 8).map((stat) => (
          <StatWidget
            key={stat.id}
            label={stat.label}
            value={stat.value}
            trend={stat.trend}
            icon={stat.icon}
          />
        ))}
      </div>

      <QuickActionPanel actions={data.quickActions} />

      <div style={{
        display: 'grid',
        gridTemplateColumns: '1fr 1fr',
        gap: 'var(--space-component-gap)',
      }}>
        <ActivityTimeline activities={filteredActivities} />
        <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
          <UpcomingTrainingPanel trainings={filteredUpcoming.slice(0, 4)} />
          <AnnouncementPanel announcements={data.announcements} />
        </div>
      </div>

      <Card variant="outlined" padding="md" as="div">
        <div style={{
          display: 'flex', alignItems: 'center', justifyContent: 'space-between',
          flexWrap: 'wrap', gap: 'var(--space-inline-md)',
        }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-inline-sm)' }}>
            <Icon name="info" size={20} color="var(--color-primary)" />
            <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>
              More dashboard widgets coming soon — including charts, attendance heatmaps, and batch performance metrics.
            </span>
          </div>
          <button
            type="button"
            onClick={() => navigate('/admin/training/analytics')}
            style={{
              display: 'flex', alignItems: 'center', gap: 'var(--space-inline-xs)',
              padding: '6px 12px', borderRadius: 'var(--radius-input)',
              background: 'var(--color-bg-primary-default)',
              color: 'var(--color-text-on-primary)',
              border: 'none', cursor: 'pointer', fontSize: 'var(--text-caption)',
            }}
          >
            <Icon name="trending-up" size={14} color="currentColor" />
            View Analytics
          </button>
        </div>
      </Card>
    </div>
  );
});

export default TrainingDashboardPage;
