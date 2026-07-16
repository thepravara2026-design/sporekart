import { useState, useMemo } from 'react';
import { useAlumni } from '../state/AlumniContext';
import { SharedFilters } from '../components/SharedFilters';
import { DashboardWidget } from '../components/DashboardWidget';
import { MetricCard } from '../components/MetricCard';
import { JobCard } from '../components/JobCard';
import { EmptyState } from '../components/EmptyStates';
import Pagination from '../../../../components/navigation/Pagination';

export default function InternshipCenter() {
  const { jobOpportunities, getFilteredJobs, page, pageSize, setPage, setPageSize } = useAlumni();
  const [activeTab, setActiveTab] = useState<'all' | 'internship' | 'apprenticeship' | 'fellowship'>('all');

  const internships = useMemo(() => jobOpportunities.filter((j) => j.jobType === 'internship' || j.jobType === 'apprenticeship' || j.jobType === 'fellowship'), [jobOpportunities]);

  const filtered = useMemo(() => {
    let items = getFilteredJobs().filter((j) => j.jobType === 'internship' || j.jobType === 'apprenticeship' || j.jobType === 'fellowship');
    if (activeTab !== 'all') items = items.filter((j) => j.jobType === activeTab);
    return items;
  }, [getFilteredJobs, activeTab]);

  const displayItems = useMemo(() => filtered.slice((page - 1) * pageSize, page * pageSize), [filtered, page, pageSize]);

  return (
    <main style={{ padding: 'var(--space-3)', display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Internship Center</h1>
        <SharedFilters currentPage="alumni/internships" showSort />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(140px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <MetricCard label="Total Internships" value={internships.length} icon="📋" />
        <MetricCard label="Active" value={internships.filter((j) => j.status === 'active').length} icon="✅" color="#16a34a" />
        <MetricCard label="Apprenticeships" value={internships.filter((j) => j.jobType === 'apprenticeship').length} icon="🔧" color="#2563eb" />
        <MetricCard label="Fellowships" value={internships.filter((j) => j.jobType === 'fellowship').length} icon="🎓" color="#8b5cf6" />
      </div>

      <div style={{ display: 'flex', gap: 4, flexWrap: 'wrap' }}>
        {[
          { key: 'all', label: 'All', count: internships.length },
          { key: 'internship', label: 'Internships', count: internships.filter((j) => j.jobType === 'internship').length },
          { key: 'apprenticeship', label: 'Apprenticeships', count: internships.filter((j) => j.jobType === 'apprenticeship').length },
          { key: 'fellowship', label: 'Fellowships', count: internships.filter((j) => j.jobType === 'fellowship').length },
        ].map((tab) => (
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

      <DashboardWidget title="Internship Opportunities" subtitle={`${filtered.length} opportunities found`}>
        {displayItems.length === 0 ? (
          <EmptyState type="noInternships" />
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
