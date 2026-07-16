import { useState, useMemo } from 'react';
import { useAlumni } from '../state/AlumniContext';
import { SharedFilters } from '../components/SharedFilters';
import { DashboardWidget } from '../components/DashboardWidget';
import { MetricCard } from '../components/MetricCard';
import { EventCard } from '../components/EventCard';
import { EmptyState } from '../components/EmptyStates';
import Pagination from '../../../../components/navigation/Pagination';

export default function AlumniEvents() {
  const { events, getFilteredEvents, page, pageSize, setPage, setPageSize } = useAlumni();
  const [activeTab, setActiveTab] = useState<'all' | 'upcoming' | 'open' | 'completed'>('all');

  const filtered = useMemo(() => {
    let items = getFilteredEvents();
    if (activeTab === 'upcoming') items = items.filter((e) => e.status === 'announced' || e.status === 'open');
    else if (activeTab === 'open') items = items.filter((e) => e.status === 'open');
    else if (activeTab === 'completed') items = items.filter((e) => e.status === 'completed');
    return items;
  }, [getFilteredEvents, activeTab]);

  const displayItems = useMemo(() => filtered.slice((page - 1) * pageSize, page * pageSize), [filtered, page, pageSize]);

  const tabs = [
    { key: 'all', label: 'All', count: events.length },
    { key: 'upcoming', label: 'Upcoming', count: events.filter((e) => e.status === 'announced' || e.status === 'open').length },
    { key: 'open', label: 'Open', count: events.filter((e) => e.status === 'open').length },
    { key: 'completed', label: 'Completed', count: events.filter((e) => e.status === 'completed').length },
  ];

  return (
    <main style={{ padding: 'var(--space-3)', display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Alumni Events</h1>
        <SharedFilters currentPage="alumni/events" showSort />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(140px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <MetricCard label="Total Events" value={events.length} icon="📅" />
        <MetricCard label="Upcoming" value={events.filter((e) => e.status === 'announced' || e.status === 'open').length} icon="📅" color="#2563eb" />
        <MetricCard label="In Progress" value={events.filter((e) => e.status === 'in-progress').length} icon="⏳" color="#d97706" />
        <MetricCard label="Completed" value={events.filter((e) => e.status === 'completed').length} icon="✅" color="#16a34a" />
        <MetricCard label="Total Registrations" value={events.reduce((s, e) => s + e.registeredCount, 0)} icon="📝" color="#8b5cf6" />
      </div>

      <div style={{ display: 'flex', gap: 4, flexWrap: 'wrap' }}>
        {tabs.map((tab) => (
          <button key={tab.key} onClick={() => { setActiveTab(tab.key as typeof activeTab); setPage(1); }}
            style={{
              padding: '4px 12px', borderRadius: 'var(--radius-full)', border: '1px solid var(--color-border-default)',
              cursor: 'pointer', fontSize: 'var(--text-caption)', whiteSpace: 'nowrap',
              background: activeTab === tab.key ? 'var(--color-primary)' : 'transparent',
              color: activeTab === tab.key ? 'var(--color-text-on-primary)' : 'var(--color-text-secondary)',
              fontWeight: activeTab === tab.key ? 'var(--weight-semibold)' : 'var(--weight-normal)',
            }}
          >
            {tab.label} ({tab.count})
          </button>
        ))}
      </div>

      <DashboardWidget title="Events" subtitle={`${filtered.length} events found`}>
        {displayItems.length === 0 ? (
          <EmptyState type="noEvents" />
        ) : (
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(340px, 1fr))', gap: 8 }}>
            {displayItems.map((e) => <EventCard key={e.id} event={e} />)}
          </div>
        )}
        {filtered.length > pageSize && (
          <div style={{ marginTop: 12 }}>
            <Pagination page={page} pageSize={pageSize} total={filtered.length} onPageChange={setPage} onPageSizeChange={setPageSize} />
          </div>
        )}
      </DashboardWidget>
    </main>
  );
}
