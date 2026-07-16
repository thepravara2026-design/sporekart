import { useState, useMemo } from 'react';
import { useAlumni } from '../state/AlumniContext';
import { SharedFilters } from '../components/SharedFilters';
import { DashboardWidget } from '../components/DashboardWidget';
import { MetricCard } from '../components/MetricCard';
import { JobCard } from '../components/JobCard';
import { EmptyState } from '../components/EmptyStates';
import Pagination from '../../../../components/navigation/Pagination';
import { JOB_TYPE_LABELS } from '../types';
import type { JobOpportunityType } from '../types';

export default function JobOpportunities() {
  const { jobOpportunities, getFilteredJobs, page, pageSize, setPage, setPageSize } = useAlumni();
  const [activeTab, setActiveTab] = useState<JobOpportunityType | 'all'>('all');

  const filtered = useMemo(() => {
    let items = getFilteredJobs();
    if (activeTab !== 'all') items = items.filter((j) => j.jobType === activeTab);
    return items;
  }, [getFilteredJobs, activeTab]);

  const displayItems = useMemo(() => filtered.slice((page - 1) * pageSize, page * pageSize), [filtered, page, pageSize]);

  const tabs = [
    { key: 'all', label: 'All', count: jobOpportunities.length },
    ...Object.entries(JOB_TYPE_LABELS).map(([key, label]) => ({
      key, label, count: jobOpportunities.filter((j) => j.jobType === key).length,
    })),
  ];

  return (
    <main style={{ padding: 'var(--space-3)', display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Job Opportunities</h1>
        <SharedFilters currentPage="alumni/jobs" showSort />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(140px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <MetricCard label="Total Jobs" value={jobOpportunities.length} icon="💼" />
        <MetricCard label="Active" value={jobOpportunities.filter((j) => j.status === 'active').length} icon="✅" color="#16a34a" />
        <MetricCard label="Full Time" value={jobOpportunities.filter((j) => j.jobType === 'full-time').length} icon="👔" color="#2563eb" />
        <MetricCard label="Internships" value={jobOpportunities.filter((j) => j.jobType === 'internship').length} icon="📋" color="#8b5cf6" />
        <MetricCard label="Total Positions" value={jobOpportunities.reduce((s, j) => s + j.totalPositions, 0)} icon="🎯" color="#059669" />
      </div>

      <div style={{ display: 'flex', gap: 4, flexWrap: 'wrap' }}>
        {tabs.filter((t) => t.count > 0).map((tab) => (
          <button key={tab.key} onClick={() => { setActiveTab(tab.key as JobOpportunityType | 'all'); setPage(1); }}
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

      <DashboardWidget title="Job Listings" subtitle={`${filtered.length} jobs found`}>
        {displayItems.length === 0 ? (
          <EmptyState type="noJobs" />
        ) : (
          <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
            {displayItems.map((j) => <JobCard key={j.id} job={j} />)}
            <Pagination page={page} pageSize={pageSize} total={filtered.length} onPageChange={setPage} onPageSizeChange={setPageSize} />
          </div>
        )}
      </DashboardWidget>
    </main>
  );
}
