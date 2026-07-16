import { useState, useMemo } from 'react';
import { useAlumni } from '../state/AlumniContext';
import { SharedFilters } from '../components/SharedFilters';
import { DashboardWidget } from '../components/DashboardWidget';
import { MetricCard } from '../components/MetricCard';
import { AlumniProfileCard } from '../components/AlumniProfileCard';
import { EmptyState } from '../components/EmptyStates';
import Pagination from '../../../../components/navigation/Pagination';
import type { AlumniEngagementLevel } from '../types';

export default function AlumniDirectory() {
  const { alumni, getFilteredAlumni, page, pageSize, setPage, setPageSize } = useAlumni();
  const [activeTab, setActiveTab] = useState<AlumniEngagementLevel | 'all'>('all');

  const filtered = useMemo(() => {
    let items = getFilteredAlumni();
    if (activeTab !== 'all') items = items.filter((a) => a.engagementLevel === activeTab);
    return items;
  }, [getFilteredAlumni, activeTab]);

  const displayItems = useMemo(() => filtered.slice((page - 1) * pageSize, page * pageSize), [filtered, page, pageSize]);

  const tabs = [
    { key: 'all', label: 'All', count: alumni.length },
    { key: 'active', label: 'Active', count: alumni.filter((a) => a.engagementLevel === 'active').length },
    { key: 'ambassador', label: 'Ambassadors', count: alumni.filter((a) => a.isAmbassador).length },
    { key: 'mentor', label: 'Mentors', count: alumni.filter((a) => a.mentorshipStatus !== 'unavailable').length },
    { key: 'donor', label: 'Donors', count: alumni.filter((a) => a.isDonor).length },
  ];

  return (
    <main style={{ padding: 'var(--space-3)', display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Alumni Directory</h1>
        <SharedFilters currentPage="alumni/directory" showSort />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(140px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <MetricCard label="Total Alumni" value={alumni.length} icon="👥" />
        <MetricCard label="Active" value={alumni.filter((a) => a.engagementLevel === 'active').length} icon="✅" color="#16a34a" />
        <MetricCard label="Ambassadors" value={alumni.filter((a) => a.isAmbassador).length} icon="⭐" color="#2563eb" />
        <MetricCard label="Mentors" value={alumni.filter((a) => a.mentorshipStatus !== 'unavailable').length} icon="🤝" color="#8b5cf6" />
        <MetricCard label="Donors" value={alumni.filter((a) => a.isDonor).length} icon="❤️" color="#dc2626" />
      </div>

      <div style={{ display: 'flex', gap: 4, flexWrap: 'wrap' }}>
        {tabs.map((tab) => (
          <button key={tab.key} onClick={() => { setActiveTab(tab.key as AlumniEngagementLevel | 'all'); setPage(1); }}
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

      <DashboardWidget title="Alumni Network" subtitle={`${filtered.length} alumni found`}>
        {displayItems.length === 0 ? (
          <EmptyState type="noAlumni" />
        ) : (
          <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
            {displayItems.map((a) => <AlumniProfileCard key={a.id} alumni={a} />)}
            <Pagination page={page} pageSize={pageSize} total={filtered.length} onPageChange={setPage} onPageSizeChange={setPageSize} />
          </div>
        )}
      </DashboardWidget>
    </main>
  );
}
