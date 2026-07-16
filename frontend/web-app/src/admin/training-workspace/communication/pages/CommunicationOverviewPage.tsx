import { memo } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import KpiCard from '../../analytics/components/KpiCard';
import WidgetGrid from '../../analytics/components/WidgetGrid';
import WidgetCard from '../../analytics/components/WidgetCard';
import { MOCK_ANNOUNCEMENTS, MOCK_NOTIFICATIONS, MOCK_STATS } from '../data/communicationMockData';
import { formatCount } from '../data/communicationFormatters';
import { AnnouncementCard } from '../components';

const CommunicationOverviewPage = memo(function CommunicationOverviewPage() {
  const stats = MOCK_STATS;
  const recent = MOCK_ANNOUNCEMENTS
    .filter((a) => a.status === 'published')
    .slice(0, 3);
  const unread = MOCK_NOTIFICATIONS.filter((n) => !n.read).length;

  return (
    <section id="panel-overview" aria-labelledby="tab-overview" style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-4)' }}>
      <div
        style={{
          display: 'grid',
          gridTemplateColumns: 'repeat(auto-fill, minmax(220px, 1fr))',
          gap: 'var(--space-3)',
        }}
      >
        <KpiCard title="Total Announcements" value={stats.totalAnnouncements} subtitle={`${stats.published} published`} icon={<Icon name="speaker" size={18} />} />
        <KpiCard title="Scheduled" value={stats.pending} subtitle="Awaiting delivery" icon={<Icon name="clock" size={18} />} />
        <KpiCard title="Notifications" value={stats.totalNotifications} subtitle={`${unread} unread`} icon={<Icon name="bell" size={18} />} />
        <KpiCard title="Active Templates" value={stats.templates} subtitle="Reusable" icon={<Icon name="file" size={18} />} />
        <KpiCard title="Audience Reach" value={formatCount(stats.audienceReachPlaceholder)} subtitle="Placeholder metric" icon={<Icon name="users" size={18} />} />
        <KpiCard title="Drafts" value={stats.draft} subtitle="In progress" icon={<Icon name="edit" size={18} />} />
      </div>

      <WidgetGrid minColWidth={360}>
        <WidgetCard title="Recent Announcements" subtitle="Latest published items" span={2}>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-3)' }}>
            {recent.map((a) => <AnnouncementCard key={a.id} announcement={a} />)}
          </div>
        </WidgetCard>

        <WidgetCard title="Status Breakdown" subtitle="Announcements by status">
          <ul style={{ listStyle: 'none', margin: 0, padding: 0, display: 'flex', flexDirection: 'column', gap: 'var(--space-2)' }}>
            {([
              ['Published', stats.published],
              ['Scheduled', stats.scheduled],
              ['Draft', stats.draft],
              ['Archived', stats.archived],
              ['Expired', stats.expired],
            ] as Array<[string, number]>).map(([label, count]) => {
              const pct = stats.totalAnnouncements ? Math.round((count / stats.totalAnnouncements) * 100) : 0;
              return (
                <li key={label} style={{ display: 'flex', flexDirection: 'column', gap: 4 }}>
                  <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>
                    <span>{label}</span>
                    <span>{count} · {pct}%</span>
                  </div>
                  <div style={{ height: 6, borderRadius: 3, background: 'var(--color-bg-surface-muted)', overflow: 'hidden' }}>
                    <div style={{ width: `${pct}%`, height: '100%', background: 'var(--color-primary)' }} />
                  </div>
                </li>
              );
            })}
          </ul>
        </WidgetCard>
      </WidgetGrid>

      <p style={{ margin: 0, fontSize: 'var(--text-caption)', color: 'var(--color-text-muted)' }}>
        All metrics and reach figures are illustrative placeholders (Mock Mode). No messages are sent.
      </p>
    </section>
  );
});

export default CommunicationOverviewPage;
