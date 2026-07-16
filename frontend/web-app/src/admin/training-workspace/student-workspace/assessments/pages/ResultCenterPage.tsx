import { useState, useMemo } from 'react';
import { useAssessments } from '../state/AssessmentContext';
import { ResultCard } from '../components/ResultCard';
import { EmptyState } from '../components/EmptyStates';
import { StudentSearchBar } from '../../components/StudentSearchFilter';
import { StudentPagination } from '../../components/StudentPagination';

export function ResultCenterPage() {
  const { results, setSearchTerm, searchTerm } = useAssessments();
  const [page, setPage] = useState(1);
  const perPage = 10;

  const filtered = useMemo(() => {
    if (!searchTerm) return results;
    const term = searchTerm.toLowerCase();
    return results.filter(
      (r) => r.studentName.toLowerCase().includes(term) || r.assessmentTitle.toLowerCase().includes(term) || r.resultCode.toLowerCase().includes(term),
    );
  }, [results, searchTerm]);

  const totalPages = Math.ceil(filtered.length / perPage);
  const paginated = filtered.slice((page - 1) * perPage, page * perPage);

  const passed = results.filter((r) => r.resultStatus === 'pass').length;
  const failed = results.filter((r) => r.resultStatus === 'fail').length;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Result Center</h2>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Total Results</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)' }}>{results.length}</div>
        </div>
        <div style={{ padding: 'var(--space-3)', background: '#f0fdf4', borderRadius: 'var(--radius-md)', border: '1px solid #16a34a' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Passed</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: '#16a34a' }}>{passed}</div>
        </div>
        <div style={{ padding: 'var(--space-3)', background: '#fef2f2', borderRadius: 'var(--radius-md)', border: '1px solid #dc2626' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Failed</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: '#dc2626' }}>{failed}</div>
        </div>
      </div>

      <div style={{ maxWidth: 400 }}>
        <StudentSearchBar
          searchQuery={searchTerm}
          onSearchChange={(v: string) => { setSearchTerm(v); setPage(1); }}
        />
      </div>

      {filtered.length === 0 ? (
        <EmptyState type={searchTerm ? 'noSearchResults' : 'noResults'} />
      ) : (
        <>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
            Showing {paginated.length} of {filtered.length} results
          </div>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(340px, 1fr))', gap: 'var(--space-component-gap)' }}>
            {paginated.map((r) => (
              <ResultCard key={r.id} result={r} />
            ))}
          </div>
          {totalPages > 1 && (
            <StudentPagination page={page} totalPages={totalPages} totalFiltered={filtered.length} pageSize={perPage} onPageChange={setPage} onPageSizeChange={() => {}} pageSizeOptions={[10, 20, 50]} />
          )}
        </>
      )}
    </div>
  );
}
