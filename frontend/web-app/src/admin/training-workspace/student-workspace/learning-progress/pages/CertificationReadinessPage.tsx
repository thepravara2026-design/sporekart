import { useState, useMemo } from 'react';
import { useProgress } from '../state/LearningProgressContext';
import { CertificationReadinessCard } from '../components/CertificationReadinessCard';
import { DashboardWidget } from '../components/DashboardWidget';
import { EmptyState } from '../components/EmptyStates';
import { StudentSearchBar } from '../../components/StudentSearchFilter';
import { StudentPagination } from '../../components/StudentPagination';

export function CertificationReadinessPage() {
  const { certificationReadiness } = useProgress();
  const [searchTerm, setSearchTerm] = useState('');
  const [readyFilter, setReadyFilter] = useState<'all' | 'ready' | 'not-ready'>('all');
  const [page, setPage] = useState(1);
  const perPage = 12;

  const filtered = useMemo(() => {
    let result = certificationReadiness;
    if (readyFilter === 'ready') result = result.filter((r) => r.isReady);
    if (readyFilter === 'not-ready') result = result.filter((r) => !r.isReady);
    if (searchTerm) {
      const term = searchTerm.toLowerCase();
      result = result.filter((r) => r.studentName.toLowerCase().includes(term) || r.courseName.toLowerCase().includes(term) || r.batchName.toLowerCase().includes(term));
    }
    return result;
  }, [certificationReadiness, searchTerm, readyFilter]);

  const totalPages = Math.ceil(filtered.length / perPage);
  const paginated = filtered.slice((page - 1) * perPage, page * perPage);

  const readyCount = certificationReadiness.filter((r) => r.isReady).length;
  const notReadyCount = certificationReadiness.filter((r) => !r.isReady).length;
  const avgEligibility = Math.round(certificationReadiness.reduce((s, r) => s + r.overallEligibilityPercent, 0) / certificationReadiness.length);

  const handleClearFilters = () => {
    setSearchTerm('');
    setReadyFilter('all');
    setPage(1);
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Certification Readiness</h2>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <DashboardWidget label="Total Students" value={certificationReadiness.length} variant="default" subtitle="Assessed" />
        <DashboardWidget label="Ready for Certification" value={readyCount} variant="success" subtitle={`${Math.round((readyCount / certificationReadiness.length) * 100)}% eligible`} />
        <DashboardWidget label="Not Ready" value={notReadyCount} variant={notReadyCount > 0 ? 'warning' : 'default'} subtitle="Has pending requirements" />
        <DashboardWidget label="Avg Eligibility" value={`${avgEligibility}%`} variant={avgEligibility >= 70 ? 'success' : 'warning'} subtitle="Across all students" />
      </div>

      <div style={{ display: 'flex', gap: 'var(--space-component-gap)', flexWrap: 'wrap', alignItems: 'center' }}>
        <div style={{ flex: 1, minWidth: 200 }}>
          <StudentSearchBar searchQuery={searchTerm} onSearchChange={(v: string) => { setSearchTerm(v); setPage(1); }} />
        </div>
        <select
          aria-label="Filter by readiness"
          value={readyFilter}
          onChange={(e) => { setReadyFilter(e.target.value as 'all' | 'ready' | 'not-ready'); setPage(1); }}
          style={{
            padding: '6px 12px', borderRadius: 'var(--radius-sm)',
            border: '1px solid var(--color-border-default)',
            fontSize: 'var(--text-body-sm)', background: 'var(--color-bg-surface-default)',
          }}
        >
          <option value="all">All Students</option>
          <option value="ready">Ready for Certification</option>
          <option value="not-ready">Not Ready</option>
        </select>
      </div>

      {filtered.length === 0 ? (
        <EmptyState type={searchTerm || readyFilter !== 'all' ? 'noSearchResults' : 'noProgress'} onClearFilters={handleClearFilters} />
      ) : (
        <>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
            Showing {paginated.length} of {filtered.length} students
          </div>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(360px, 1fr))', gap: 'var(--space-component-gap)' }}>
            {paginated.map((r) => (
              <CertificationReadinessCard key={r.id} readiness={r} />
            ))}
          </div>
          {totalPages > 1 && (
            <StudentPagination page={page} totalPages={totalPages} totalFiltered={filtered.length} pageSize={perPage} onPageChange={setPage} onPageSizeChange={() => {}} pageSizeOptions={[12, 24, 48]} />
          )}
        </>
      )}
    </div>
  );
}
