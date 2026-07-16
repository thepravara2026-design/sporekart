import { useCommunication } from '../state/CommunicationContext';
import { SharedFilters } from '../components/SharedFilters';
import { DashboardWidget } from '../components/DashboardWidget';
import { MetricCard } from '../components/MetricCard';
import { MessageCard } from '../components/MessageCard';
import { AnnouncementCard } from '../components/AnnouncementCard';

export default function EngagementDashboard() {
  const { dashboard, messages } = useCommunication();
  const criticalCount = messages.filter((m) => m.priority === 'critical').length;
  const unreadCount = messages.filter((m) => m.readStatus !== 'read').length;

  return (
    <main style={{ padding: 'var(--space-3)', display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Engagement Dashboard</h1>
        <SharedFilters currentPage="communication" />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(140px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <MetricCard label="Notifications Sent" value={dashboard.notificationsSent} icon="📨" />
        <MetricCard label="Announcements" value={dashboard.announcementsCount} icon="📢" />
        <MetricCard label="Unread Messages" value={dashboard.unreadMessages} icon="🔴" color="#dc2626" />
        <MetricCard label="Read %" value={`${dashboard.readPercentage}%`} icon="👁️" color="#16a34a" />
        <MetricCard label="Engagement" value={`${dashboard.studentEngagement}%`} icon="📊" color="#2563eb" />
        <MetricCard label="Open Rate" value={`${dashboard.openRate}%`} icon="📈" color="#8b5cf6" />
        <MetricCard label="Active Templates" value={dashboard.activeTemplates} icon="📋" />
        <MetricCard label="System Alerts" value={dashboard.systemAlerts} icon="🔔" color={dashboard.systemAlerts > 0 ? '#f59e0b' : undefined} />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(360px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <DashboardWidget title="Recent Messages" subtitle="Latest notifications">
          <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
            {dashboard.recentMessages.slice(0, 4).map((m) => <MessageCard key={m.id} message={m} />)}
          </div>
        </DashboardWidget>
        <DashboardWidget title="Recent Announcements" subtitle="Latest announcements">
          <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
            {dashboard.recentAnnouncements.slice(0, 4).map((a) => <AnnouncementCard key={a.id} announcement={a} />)}
          </div>
        </DashboardWidget>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <DashboardWidget title="Priority Distribution" subtitle="Messages by priority">
          <div style={{ display: 'flex', flexDirection: 'column', gap: 6 }}>
            {dashboard.priorityDistribution.map((p) => (
              <div key={p.priority} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', fontSize: 'var(--text-body-sm)' }}>
                <span style={{ textTransform: 'capitalize', color: 'var(--color-text-secondary)' }}>{p.priority}</span>
                <span style={{ fontWeight: 'var(--weight-semibold)' }}>{p.count}</span>
              </div>
            ))}
          </div>
        </DashboardWidget>
        <DashboardWidget title="Quick Stats" subtitle="Key metrics at a glance">
          <div style={{ display: 'flex', flexDirection: 'column', gap: 8, fontSize: 'var(--text-body-sm)' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between' }}><span>Course Notifications</span><strong>{dashboard.courseNotifications}</strong></div>
            <div style={{ display: 'flex', justifyContent: 'space-between' }}><span>Batch Notifications</span><strong>{dashboard.batchNotifications}</strong></div>
            <div style={{ display: 'flex', justifyContent: 'space-between' }}><span>Critical Messages</span><strong style={{ color: '#dc2626' }}>{criticalCount}</strong></div>
            <div style={{ display: 'flex', justifyContent: 'space-between' }}><span>Unread</span><strong style={{ color: '#dc2626' }}>{unreadCount}</strong></div>
            <div style={{ display: 'flex', justifyContent: 'space-between' }}><span>Active Templates</span><strong>{dashboard.activeTemplates}</strong></div>
          </div>
        </DashboardWidget>
      </div>
    </main>
  );
}
