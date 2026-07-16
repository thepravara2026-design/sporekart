import { useState, useMemo } from 'react';
import { useAlumni } from '../state/AlumniContext';
import { SharedFilters } from '../components/SharedFilters';
import { DashboardWidget } from '../components/DashboardWidget';
import { MetricCard } from '../components/MetricCard';
import { MentorshipCard } from '../components/MentorshipCard';
import { EmptyState } from '../components/EmptyStates';

export default function AlumniMentorship() {
  const { mentorships, getFilteredMentorships, page, pageSize, setPage } = useAlumni();
  const [activeTab, setActiveTab] = useState<'all' | 'active' | 'completed' | 'paused'>('all');

  const filtered = useMemo(() => {
    let items = getFilteredMentorships();
    if (activeTab !== 'all') items = items.filter((m) => m.status === activeTab);
    return items;
  }, [getFilteredMentorships, activeTab]);

  const displayItems = useMemo(() => filtered.slice((page - 1) * pageSize, page * pageSize), [filtered, page, pageSize]);

  const tabs = [
    { key: 'all', label: 'All', count: mentorships.length },
    { key: 'active', label: 'Active', count: mentorships.filter((m) => m.status === 'active').length },
    { key: 'completed', label: 'Completed', count: mentorships.filter((m) => m.status === 'completed').length },
    { key: 'paused', label: 'Paused', count: mentorships.filter((m) => m.status === 'paused').length },
  ];

  return (
    <main style={{ padding: 'var(--space-3)', display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Alumni Mentorship</h1>
        <SharedFilters currentPage="alumni/mentorship" showSort />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(140px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <MetricCard label="Total Mentorships" value={mentorships.length} icon="🤝" />
        <MetricCard label="Active" value={mentorships.filter((m) => m.status === 'active').length} icon="✅" color="#16a34a" />
        <MetricCard label="Completed" value={mentorships.filter((m) => m.status === 'completed').length} icon="🎯" color="#2563eb" />
        <MetricCard label="Total Hours" value={mentorships.reduce((s, m) => s + m.totalHours, 0)} icon="⏱️" color="#8b5cf6" />
        <MetricCard label="Avg Rating" value={mentorships.length ? (mentorships.reduce((s, m) => s + m.rating, 0) / mentorships.length).toFixed(1) : 'N/A'} icon="⭐" color="#d97706" />
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

      <DashboardWidget title="Mentorship Pairs" subtitle={`${filtered.length} mentorship records`}>
        {displayItems.length === 0 ? (
          <EmptyState type="noSearchResults" />
        ) : (
          <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
            {displayItems.map((m) => <MentorshipCard key={m.id} mentorship={m} />)}
          </div>
        )}
      </DashboardWidget>
    </main>
  );
}
