import { useCallback, memo } from 'react';
import { Icon } from '../../design-system/icons/Icon';
import { KPIGrid } from './kpi/KPIGrid';
import { WidgetGrid } from './widgets/WidgetGrid';
import { ActivityFeed } from './activity/ActivityFeed';
import { QuickActions } from './actions/QuickActions';
import { AnnouncementBanner } from './announcements/AnnouncementBanner';
import { SystemStatus } from './status/SystemStatus';
import type { KPIData, WidgetConfig, ActivityItemData, AnnouncementData, SystemStatusData, QuickActionData } from './types';

export interface DashboardData {
  kpis: KPIData[];
  widgets: WidgetConfig[];
  activity: ActivityItemData[];
  announcements: AnnouncementData[];
  systemStatus: SystemStatusData[];
  quickActions: QuickActionData[];
  userName?: string;
  userRole?: string;
}

interface DashboardLayoutProps {
  data: DashboardData;
  loading?: boolean;
  onPinWidget?: (id: string) => void;
  onRemoveWidget?: (id: string) => void;
}

export const DashboardLayout = memo(function DashboardLayout({ data, loading = false, onPinWidget, onRemoveWidget }: DashboardLayoutProps) {
  const handlePin = useCallback(
    (id: string) => {
      onPinWidget?.(id);
    },
    [onPinWidget]
  );

  const handleRemove = useCallback(
    (id: string) => {
      onRemoveWidget?.(id);
    },
    [onRemoveWidget]
  );

  const getGreeting = () => {
    const hour = new Date().getHours();
    if (hour < 12) return 'Good morning';
    if (hour < 18) return 'Good afternoon';
    return 'Good evening';
  };

  if (loading) {
    return <DashboardSkeleton />;
  }

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      {/* Welcome Area */}
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', margin: '0 0 4px', color: 'var(--color-text-primary)' }}>
          {getGreeting()}{data.userName ? `, ${data.userName}` : ''}
        </h1>
        <p style={{ margin: 0, color: 'var(--color-text-secondary)', fontSize: 'var(--text-body)' }}>
          {data.userRole ? `${data.userRole} — ` : ''}Here's your enterprise overview for today.
        </p>
      </div>

      {/* Announcements */}
      {data.announcements.length > 0 && (
        <AnnouncementBanner announcements={data.announcements} />
      )}

      {/* KPI Grid */}
      <section aria-label="Key Performance Indicators">
        <SectionHeader title="Key Metrics" icon="bar-chart" />
        <KPIGrid kpis={data.kpis} />
      </section>

      {/* Quick Actions */}
      <section aria-label="Quick Actions">
        <SectionHeader title="Quick Actions" icon="zap" />
        <QuickActions actions={data.quickActions} />
      </section>

      {/* Widget Grid */}
      <section aria-label="Dashboard Widgets">
        <WidgetGrid widgets={data.widgets} onPin={handlePin} onRemove={handleRemove} />
      </section>

      {/* Activity & System Status */}
      <div
        style={{
          display: 'grid',
          gridTemplateColumns: '1fr 1fr',
          gap: 'var(--space-component-gap)',
        }}
        className="dg-dashboard-bottom-row"
      >
        <section aria-label="Recent Activity" style={{ background: 'var(--color-surface)', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', padding: 16 }}>
          <SectionHeader title="Recent Activity" icon="clock" />
          <ActivityFeed items={data.activity} maxItems={8} />
        </section>
        <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
          <section aria-label="System Status" style={{ background: 'var(--color-surface)', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', padding: 16 }}>
            <SectionHeader title="System Status" icon="heart" />
            <SystemStatus services={data.systemStatus} columns={2} />
          </section>
          {/* AI Assistant Placeholder */}
          <div style={{ background: 'var(--color-surface)', border: '1px dashed var(--color-border)', borderRadius: 'var(--radius-lg)', padding: 16, textAlign: 'center' }}>
            <Icon name="cpu" size={24} />
            <p style={{ margin: '8px 0 0', fontSize: 'var(--text-body)', color: 'var(--color-text-tertiary)' }}>
              AI Assistant — Coming Soon
            </p>
          </div>
        </div>
      </div>
    </div>
  );
});

function SectionHeader({ title, icon }: { title: string; icon: string }) {
  return (
    <div style={{ display: 'flex', alignItems: 'center', gap: 6, marginBottom: 12 }}>
      <Icon name={icon} size={16} />
      <h2 style={{ margin: 0, fontSize: 'var(--text-h4)', fontWeight: 600, color: 'var(--color-text-primary)' }}>{title}</h2>
    </div>
  );
}

function DashboardSkeleton() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <div style={{ width: '40%', height: 28, background: 'var(--color-surface-hover)', borderRadius: 4, marginBottom: 8, animation: 'shimmer 1.5s infinite' }} />
        <div style={{ width: '60%', height: 16, background: 'var(--color-surface-hover)', borderRadius: 4, animation: 'shimmer 1.5s infinite' }} />
      </div>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: 'var(--space-component-gap)' }}>
        {Array.from({ length: 4 }).map((_, i) => (
          <div key={i} style={{ height: 120, background: 'var(--color-surface)', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', padding: 20 }}>
            <div style={{ width: '60%', height: 14, background: 'var(--color-surface-hover)', borderRadius: 4, marginBottom: 16, animation: 'shimmer 1.5s infinite' }} />
            <div style={{ width: '40%', height: 28, background: 'var(--color-surface-hover)', borderRadius: 4, marginBottom: 8, animation: 'shimmer 1.5s infinite' }} />
            <div style={{ width: '50%', height: 12, background: 'var(--color-surface-hover)', borderRadius: 4, animation: 'shimmer 1.5s infinite' }} />
          </div>
        ))}
      </div>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: 'var(--space-component-gap)' }}>
        {Array.from({ length: 4 }).map((_, i) => (
          <div key={i} style={{ height: 200, background: 'var(--color-surface)', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)' }} />
        ))}
      </div>
    </div>
  );
}
