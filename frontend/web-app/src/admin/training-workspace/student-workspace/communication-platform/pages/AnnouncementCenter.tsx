import { useMemo, useState } from 'react';
import { useCommunication } from '../state/CommunicationContext';
import { SharedFilters } from '../components/SharedFilters';
import { DashboardWidget } from '../components/DashboardWidget';
import { AnnouncementCard } from '../components/AnnouncementCard';
import { MetricCard } from '../components/MetricCard';
import { EmptyState } from '../components/EmptyStates';
import { ANNOUNCEMENT_CATEGORY_LABELS } from '../types';
import type { AnnouncementCategory } from '../types';
import Pagination from '../../../../components/navigation/Pagination';

export default function AnnouncementCenter() {
  const { announcements, page, pageSize, setPage, setPageSize } = useCommunication();
  const [selectedCat, setSelectedCat] = useState<AnnouncementCategory | 'all'>('all');

  const categories = [...new Set(announcements.map((a) => a.category))] as AnnouncementCategory[];
  const filtered = useMemo(() => selectedCat === 'all' ? announcements : announcements.filter((a) => a.category === selectedCat), [announcements, selectedCat]);
  const displayItems = filtered.slice((page - 1) * pageSize, page * pageSize);

  return (
    <main style={{ padding: 'var(--space-3)', display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Announcement Center</h1>
        <SharedFilters currentPage="communication/announcements" showSort />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(140px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <MetricCard label="Total" value={announcements.length} icon="📢" />
        <MetricCard label="Active" value={announcements.filter((a) => a.status === 'sent' || a.status === 'delivered').length} icon="✅" color="#16a34a" />
        <MetricCard label="Scheduled" value={announcements.filter((a) => a.status === 'scheduled').length} icon="📅" color="#2563eb" />
        <MetricCard label="Expired" value={announcements.filter((a) => a.status === 'expired').length} icon="⏰" color="#6b7280" />
      </div>

      <div style={{ display: 'flex', gap: 4, flexWrap: 'wrap' }}>
        <button onClick={() => { setSelectedCat('all'); setPage(1); }} style={{
          padding: '6px 14px', borderRadius: 'var(--radius-sm)', border: 'none', cursor: 'pointer',
          background: selectedCat === 'all' ? 'var(--color-bg-primary-subtle)' : 'transparent',
          color: selectedCat === 'all' ? 'var(--color-primary)' : 'var(--color-text-secondary)',
          fontWeight: selectedCat === 'all' ? 'var(--weight-semibold)' : 'var(--weight-normal)',
          fontSize: 'var(--text-body-sm)',
        }}>All ({announcements.length})</button>
        {categories.map((cat) => (
          <button key={cat} onClick={() => { setSelectedCat(cat); setPage(1); }} style={{
            padding: '6px 14px', borderRadius: 'var(--radius-sm)', border: 'none', cursor: 'pointer',
            background: selectedCat === cat ? 'var(--color-bg-primary-subtle)' : 'transparent',
            color: selectedCat === cat ? 'var(--color-primary)' : 'var(--color-text-secondary)',
            fontWeight: selectedCat === cat ? 'var(--weight-semibold)' : 'var(--weight-normal)',
            fontSize: 'var(--text-body-sm)', textTransform: 'capitalize',
          }}>{ANNOUNCEMENT_CATEGORY_LABELS[cat] || cat.replace(/-/g, ' ')} ({announcements.filter((a) => a.category === cat).length})</button>
        ))}
      </div>

      <DashboardWidget title="Announcements" subtitle={`${filtered.length} announcements`}>
        {displayItems.length === 0 ? <EmptyState type="noAnnouncements" /> : (
          <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: 8 }}>
              {displayItems.map((a) => <AnnouncementCard key={a.id} announcement={a} />)}
            </div>
            <Pagination page={page} pageSize={pageSize} total={filtered.length} onPageChange={setPage} onPageSizeChange={setPageSize} />
          </div>
        )}
      </DashboardWidget>
    </main>
  );
}
