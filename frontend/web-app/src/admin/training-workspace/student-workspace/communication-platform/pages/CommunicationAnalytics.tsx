import { useCommunication } from '../state/CommunicationContext';
import { SharedFilters } from '../components/SharedFilters';
import { DashboardWidget } from '../components/DashboardWidget';
import { MetricCard } from '../components/MetricCard';
import { COMMUNICATION_TYPE_LABELS, PRIORITY_LABELS } from '../types';
import type { CommunicationType, Priority } from '../types';

export default function CommunicationAnalytics() {
  const { analytics } = useCommunication();

  return (
    <main style={{ padding: 'var(--space-3)', display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Communication Analytics</h1>
        <SharedFilters currentPage="communication/analytics" />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(140px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <MetricCard label="Total Sent" value={analytics.totalSent} icon="📨" />
        <MetricCard label="Delivered" value={analytics.totalDelivered} icon="✅" color="#16a34a" />
        <MetricCard label="Read" value={analytics.totalRead} icon="👁️" color="#2563eb" />
        <MetricCard label="Failed" value={analytics.totalFailed} icon="❌" color="#dc2626" />
        <MetricCard label="Read Rate" value={`${analytics.readRate}%`} icon="📈" color="#8b5cf6" />
        <MetricCard label="Engagement" value={`${analytics.engagementRate}%`} icon="📊" color="#f59e0b" />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(360px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <DashboardWidget title="Distribution by Type" subtitle="Notifications per communication type">
          <div style={{ display: 'flex', flexDirection: 'column', gap: 6 }}>
            {analytics.distributionByType.map((d) => (
              <div key={d.type} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', fontSize: 'var(--text-body-sm)' }}>
                <span style={{ color: 'var(--color-text-secondary)' }}>{COMMUNICATION_TYPE_LABELS[d.type as CommunicationType] || d.type}</span>
                <div style={{ display: 'flex', alignItems: 'center', gap: 8, flex: 1, maxWidth: 120 }}>
                  <div style={{ flex: 1, height: 6, background: 'var(--color-bg-skeleton-base)', borderRadius: 3, overflow: 'hidden' }}>
                    <div style={{ width: `${(d.count / analytics.totalSent) * 100}%`, height: '100%', background: '#2563eb', borderRadius: 3 }} />
                  </div>
                  <span style={{ fontWeight: 'var(--weight-semibold)', minWidth: 24, textAlign: 'right' }}>{d.count}</span>
                </div>
              </div>
            ))}
          </div>
        </DashboardWidget>

        <DashboardWidget title="Distribution by Priority" subtitle="Messages per priority level">
          <div style={{ display: 'flex', flexDirection: 'column', gap: 6 }}>
            {analytics.distributionByPriority.map((d) => (
              <div key={d.priority} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', fontSize: 'var(--text-body-sm)' }}>
                <span style={{ color: 'var(--color-text-secondary)', textTransform: 'capitalize' }}>{PRIORITY_LABELS[d.priority as Priority] || d.priority}</span>
                <div style={{ display: 'flex', alignItems: 'center', gap: 8, flex: 1, maxWidth: 120 }}>
                  <div style={{ flex: 1, height: 6, background: 'var(--color-bg-skeleton-base)', borderRadius: 3, overflow: 'hidden' }}>
                    <div style={{ width: `${(d.count / analytics.totalSent) * 100}%`, height: '100%', background: d.priority === 'critical' ? '#dc2626' : d.priority === 'high' ? '#ca8a04' : '#2563eb', borderRadius: 3 }} />
                  </div>
                  <span style={{ fontWeight: 'var(--weight-semibold)', minWidth: 24, textAlign: 'right' }}>{d.count}</span>
                </div>
              </div>
            ))}
          </div>
        </DashboardWidget>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(360px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <DashboardWidget title="Course Notifications" subtitle="Notifications per course">
          <div style={{ display: 'flex', flexDirection: 'column', gap: 6 }}>
            {analytics.courseNotifications.map((c) => (
              <div key={c.courseName} style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-body-sm)' }}>
                <span style={{ color: 'var(--color-text-secondary)' }}>{c.courseName}</span>
                <span style={{ fontWeight: 'var(--weight-semibold)' }}>{c.count}</span>
              </div>
            ))}
          </div>
        </DashboardWidget>
        <DashboardWidget title="Batch Notifications" subtitle="Notifications per batch">
          <div style={{ display: 'flex', flexDirection: 'column', gap: 6 }}>
            {analytics.batchNotifications.map((b) => (
              <div key={b.batchName} style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-body-sm)' }}>
                <span style={{ color: 'var(--color-text-secondary)' }}>{b.batchName}</span>
                <span style={{ fontWeight: 'var(--weight-semibold)' }}>{b.count}</span>
              </div>
            ))}
          </div>
        </DashboardWidget>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(360px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <DashboardWidget title="Announcement Trends" subtitle="Monthly announcement count">
          <div style={{ display: 'flex', gap: 4, alignItems: 'flex-end', height: 80 }}>
            {analytics.announcementTrends.map((t, i) => (
              <div key={i} style={{ flex: 1, display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 2 }}>
                <div style={{ width: '100%', maxWidth: 24, background: '#8b5cf6', borderRadius: '3px 3px 0 0', height: `${Math.max(4, (t.count / Math.max(...analytics.announcementTrends.map((x) => x.count), 1)) * 56)}px`, minHeight: 4 }} />
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{t.month}</span>
              </div>
            ))}
          </div>
        </DashboardWidget>
        <DashboardWidget title="Notification Trends" subtitle="Monthly notification volume">
          <div style={{ display: 'flex', gap: 4, alignItems: 'flex-end', height: 80 }}>
            {analytics.notificationTrends.map((t, i) => (
              <div key={i} style={{ flex: 1, display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 2 }}>
                <div style={{ width: '100%', maxWidth: 24, background: '#2563eb', borderRadius: '3px 3px 0 0', height: `${Math.max(4, (t.count / Math.max(...analytics.notificationTrends.map((x) => x.count), 1)) * 56)}px`, minHeight: 4 }} />
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{t.month}</span>
              </div>
            ))}
          </div>
        </DashboardWidget>
      </div>
    </main>
  );
}
