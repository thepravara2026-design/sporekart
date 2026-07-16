import { useState, useMemo } from 'react';
import { useAlumni } from '../state/AlumniContext';
import { SharedFilters } from '../components/SharedFilters';
import { DashboardWidget } from '../components/DashboardWidget';
import { MetricCard } from '../components/MetricCard';
import { CompanyCard } from '../components/CompanyCard';
import { EmptyState } from '../components/EmptyStates';
import Pagination from '../../../../components/navigation/Pagination';
import { TIER_LABELS } from '../types';
import type { CompanyPartnershipTier } from '../types';

export default function CompanyPartnerships() {
  const { companies, getFilteredCompanies, page, pageSize, setPage, setPageSize } = useAlumni();
  const [activeTab, setActiveTab] = useState<CompanyPartnershipTier | 'all'>('all');

  const filtered = useMemo(() => {
    let items = getFilteredCompanies();
    if (activeTab !== 'all') items = items.filter((c) => c.tier === activeTab);
    return items;
  }, [getFilteredCompanies, activeTab]);

  const displayItems = useMemo(() => filtered.slice((page - 1) * pageSize, page * pageSize), [filtered, page, pageSize]);

  const tabs = [
    { key: 'all', label: 'All', count: companies.length },
    ...Object.entries(TIER_LABELS).map(([key, label]) => ({
      key, label, count: companies.filter((c) => c.tier === key).length,
    })),
  ];

  return (
    <main style={{ padding: 'var(--space-3)', display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Company Partnerships</h1>
        <SharedFilters currentPage="alumni/companies" showSort />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(140px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <MetricCard label="Total Partners" value={companies.length} icon="🏢" />
        <MetricCard label="Active" value={companies.filter((c) => c.status === 'active').length} icon="✅" color="#16a34a" />
        <MetricCard label="Platinum" value={companies.filter((c) => c.tier === 'platinum').length} icon="💎" color="#059669" />
        <MetricCard label="Total Hires" value={companies.reduce((s, c) => s + c.totalHires, 0)} icon="👥" color="#2563eb" />
        <MetricCard label="Total Drives" value={companies.reduce((s, c) => s + c.totalPlacementDrives, 0)} icon="🚀" color="#8b5cf6" />
      </div>

      <div style={{ display: 'flex', gap: 4, flexWrap: 'wrap' }}>
        {tabs.map((tab) => (
          <button key={tab.key} onClick={() => { setActiveTab(tab.key as CompanyPartnershipTier | 'all'); setPage(1); }}
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

      <DashboardWidget title="Partner Companies" subtitle={`${filtered.length} companies found`}>
        {displayItems.length === 0 ? (
          <EmptyState type="noSearchResults" />
        ) : (
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: 8 }}>
            {displayItems.map((c) => <CompanyCard key={c.id} company={c} />)}
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
