import { useState, useMemo } from 'react';
import { useAssignments } from '../state/AssignmentContext';
import { SubmissionCard } from '../components/SubmissionCard';
import { EmptyState } from '../components/EmptyStates';
import { StudentSearchBar } from '../../components/StudentSearchFilter';
import { StudentPagination } from '../../components/StudentPagination';

export function AssignmentSubmissionsPage() {
  const { submissions, setSearchTerm, searchTerm } = useAssignments();
  const [page, setPage] = useState(1);
  const perPage = 10;

  const filtered = useMemo(() => {
    if (!searchTerm) return submissions;
    const term = searchTerm.toLowerCase();
    return submissions.filter(
      (s) => s.studentName.toLowerCase().includes(term) || s.assignmentTitle.toLowerCase().includes(term) || s.submissionCode.toLowerCase().includes(term),
    );
  }, [submissions, searchTerm]);

  const totalPages = Math.ceil(filtered.length / perPage);
  const paginated = filtered.slice((page - 1) * perPage, page * perPage);

  const pendingCount = submissions.filter((s) => s.status === 'submitted' || s.status === 'late').length;
  const reviewedCount = submissions.filter((s) => s.status === 'reviewed' || s.status === 'accepted' || s.status === 'rejected').length;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Submission Queue</h2>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Total Submissions</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)' }}>{submissions.length}</div>
        </div>
        <div style={{ padding: 'var(--space-3)', background: '#fefce8', borderRadius: 'var(--radius-md)', border: '1px solid #ca8a04' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Pending Review</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: '#ca8a04' }}>{pendingCount}</div>
        </div>
        <div style={{ padding: 'var(--space-3)', background: '#f0fdf4', borderRadius: 'var(--radius-md)', border: '1px solid #16a34a' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Reviewed</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: '#16a34a' }}>{reviewedCount}</div>
        </div>
      </div>

      <div style={{ maxWidth: 400 }}>
        <StudentSearchBar
          searchQuery={searchTerm}
          onSearchChange={(v: string) => { setSearchTerm(v); setPage(1); }}
        />
      </div>

      {filtered.length === 0 ? (
        <EmptyState type={searchTerm ? 'noSearchResults' : 'noSubmissions'} />
      ) : (
        <>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
            Showing {paginated.length} of {filtered.length} submissions
          </div>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: 'var(--space-component-gap)' }}>
            {paginated.map((s) => (
              <SubmissionCard key={s.id} submission={s} />
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
