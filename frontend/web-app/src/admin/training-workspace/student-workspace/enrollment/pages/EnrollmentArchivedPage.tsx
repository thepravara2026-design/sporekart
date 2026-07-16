import { useMemo, useState, useCallback } from 'react';
import { useEnrollment } from '../state/EnrollmentContext';
import { EnrollmentTable } from '../components/EnrollmentTable';
import { EnrollmentPagination } from '../components/EnrollmentPagination';
import { EnrollmentSearchFilter } from '../components/EnrollmentSearchFilter';
import { EmptyState } from '../components/EmptyStates';
import { EnrollmentTableSkeleton } from '../components/Skeletons';

export function EnrollmentArchivedPage() {
  const { archivedEnrollments, selectRequest, currentRequest, isLoading } = useEnrollment();
  const [searchTerm, setSearchTerm] = useState('');
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);

  const filtered = useMemo(() => {
    if (!searchTerm) return archivedEnrollments;
    const term = searchTerm.toLowerCase();
    return archivedEnrollments.filter(
      (r) =>
        r.studentName.toLowerCase().includes(term) ||
        r.applicationNumber.toLowerCase().includes(term) ||
        r.courseName.toLowerCase().includes(term),
    );
  }, [archivedEnrollments, searchTerm]);

  const paginated = useMemo(() => {
    const start = (page - 1) * pageSize;
    return filtered.slice(start, start + pageSize);
  }, [filtered, page, pageSize]);

  const totalPages = Math.max(1, Math.ceil(filtered.length / pageSize));

  const clearFilters = useCallback(() => { setSearchTerm(''); }, []);

  if (isLoading) return <EnrollmentTableSkeleton />;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Archived Enrollments</h1>
        <p style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)', margin: '4px 0 0 0' }}>
          Previously archived enrollment records
        </p>
      </div>

      <EnrollmentSearchFilter
        searchTerm={searchTerm}
        onSearchChange={setSearchTerm}
        statusFilter="all"
        onStatusFilterChange={() => {}}
        totalResults={filtered.length}
      />

      {filtered.length === 0 ? (
        <EmptyState type="noArchived" onClearFilters={clearFilters} />
      ) : (
        <div style={{
          padding: 'var(--space-3)', borderRadius: 'var(--radius-md)',
          border: '1px solid var(--color-border-default)',
          background: 'var(--color-bg-surface-default)',
        }}>
          <EnrollmentTable requests={paginated} onSelect={selectRequest} selectedId={currentRequest?.id ?? null} />
          <EnrollmentPagination
            page={page} totalPages={totalPages} totalFiltered={filtered.length}
            pageSize={pageSize} onPageChange={setPage} onPageSizeChange={setPageSize}
          />
        </div>
      )}
    </div>
  );
}
