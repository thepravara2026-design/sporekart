import { useCommunication } from '../state/CommunicationContext';
import { SharedFilters } from '../components/SharedFilters';
import { DashboardWidget } from '../components/DashboardWidget';
import { AnnouncementCard } from '../components/AnnouncementCard';
import { MetricCard } from '../components/MetricCard';
import { EmptyState } from '../components/EmptyStates';

export default function AnnouncementCenter() {
  const { announcements } = useCommunication();

  return (
    <main style={{ padding: 'var(--space-3)', display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Announcement Center</h1>
        <SharedFilters currentPage="communication/announcements" />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(140px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <MetricCard label="Total" value={announcements.length} icon="📢" />
        <MetricCard label="Active" value={announcements.filter((a) => a.status === 'sent' || a.status === 'delivered').length} icon="✅" color="#16a34a" />
        <MetricCard label="Scheduled" value={announcements.filter((a) => a.status === 'scheduled').length} icon="📅" color="#2563eb" />
        <MetricCard label="Expired" value={announcements.filter((a) => a.status === 'expired').length} icon="⏰" color="#6b7280" />
      </div>

      <DashboardWidget title="All Announcements" subtitle={`${announcements.length} announcements`}>
        {announcements.length === 0 ? <EmptyState type="noAnnouncements" /> : (
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: 8 }}>
            {announcements.map((a) => <AnnouncementCard key={a.id} announcement={a} />)}
          </div>
        )}
      </DashboardWidget>
    </main>
  );
}
