import { useState, useMemo } from 'react';
import { useAlumni } from '../state/AlumniContext';
import { SharedFilters } from '../components/SharedFilters';
import { DashboardWidget } from '../components/DashboardWidget';
import { MetricCard } from '../components/MetricCard';
import { ContributionCard } from '../components/ContributionCard';
import { EmptyState } from '../components/EmptyStates';
import Pagination from '../../../../components/navigation/Pagination';
import { CONTRIBUTION_TYPE_LABELS } from '../types';
import type { AlumniContributionType } from '../types';

export default function AlumniContributions() {
  const { contributions, getFilteredContributions, page, pageSize, setPage, setPageSize } = useAlumni();
  const [activeTab, setActiveTab] = useState<AlumniContributionType | 'all'>('all');

  const filtered = useMemo(() => {
    let items = getFilteredContributions();
    if (activeTab !== 'all') items = items.filter((c) => c.type === activeTab);
    return items;
  }, [getFilteredContributions, activeTab]);

  const displayItems = useMemo(() => filtered.slice((page - 1) * pageSize, page * pageSize), [filtered, page, pageSize]);

  const tabs = [
    { key: 'all', label: 'All', count: contributions.length },
    ...Object.entries(CONTRIBUTION_TYPE_LABELS).map(([key, label]) => ({
      key, label, count: contributions.filter((c) => c.type === key).length,
    })),
  ];

  return (
    <main style={{ padding: 'var(--space-3)', display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Alumni Contributions</h1>
        <SharedFilters currentPage="alumni/contributions" showSort />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(140px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <MetricCard label="Total Contributions" value={contributions.length} icon="🎁" />
        <MetricCard label="Acknowledged" value={contributions.filter((c) => c.status === 'acknowledged').length} icon="✅" color="#16a34a" />
        <MetricCard label="Featured" value={contributions.filter((c) => c.status === 'featured').length} icon="⭐" color="#2563eb" />
        <MetricCard label="Pending" value={contributions.filter((c) => c.status === 'pending-acknowledgment').length} icon="⏳" color="#d97706" />
        <MetricCard label="Total Value" value={contributions.reduce((s, c) => s + parseInt(c.value.replace(/[^0-9]/g, '') || '0', 10), 0).toLocaleString()} icon="💰" color="#059669" />
      </div>

      <div style={{ display: 'flex', gap: 4, flexWrap: 'wrap' }}>
        {tabs.filter((t) => t.count > 0).slice(0, 8).map((tab) => (
          <button key={tab.key} onClick={() => { setActiveTab(tab.key as AlumniContributionType | 'all'); setPage(1); }}
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

      <DashboardWidget title="Contribution Records" subtitle={`${filtered.length} contributions found`}>
        {displayItems.length === 0 ? (
          <EmptyState type="noSearchResults" />
        ) : (
          <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
            {displayItems.map((c) => <ContributionCard key={c.id} contribution={c} />)}
            <Pagination page={page} pageSize={pageSize} total={filtered.length} onPageChange={setPage} onPageSizeChange={setPageSize} />
          </div>
        )}
      </DashboardWidget>
    </main>
  );
}
