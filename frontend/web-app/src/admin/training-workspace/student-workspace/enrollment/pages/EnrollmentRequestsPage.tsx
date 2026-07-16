import { useMemo, useState, useCallback } from 'react';
import { useEnrollment } from '../state/EnrollmentContext';
import { StudentSearchBar } from '../../components/StudentSearchFilter';
import { StudentPagination } from '../../components/StudentPagination';
import { EnrollmentTable } from '../components/EnrollmentTable';
import { EnrollmentCard } from '../components/EnrollmentCard';
import { EnrollmentStatusBadge } from '../components/EnrollmentStatusBadge';
import { EnrollmentTableSkeleton } from '../components/Skeletons';
import { EmptyState } from '../components/EmptyStates';
import { ENROLLMENT_STATUS_LABELS, ADMISSION_TYPE_LABELS } from '../types';
import type { EnrollmentStatus } from '../types';

const STATUS_OPTIONS: (EnrollmentStatus | 'all')[] = [
  'all', 'draft', 'submitted', 'under-review', 'pending-approval',
  'approved', 'rejected', 'seat-reserved', 'batch-assigned', 'enrolled', 'cancelled', 'archived',
];

export function EnrollmentRequestsPage() {
  const { requests, searchTerm, setSearchTerm, statusFilter, setStatusFilter, selectRequest, currentRequest, isLoading } = useEnrollment();
  const [viewMode, setViewMode] = useState<'table' | 'card'>('table');
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);

  const filtered = useMemo(() => {
    let result = requests;
    if (statusFilter !== 'all') {
      result = result.filter((r) => r.enrollmentStatus === statusFilter);
    }
    if (searchTerm) {
      const term = searchTerm.toLowerCase();
      result = result.filter(
        (r) =>
          r.applicationNumber.toLowerCase().includes(term) ||
          r.studentName.toLowerCase().includes(term) ||
          r.courseName.toLowerCase().includes(term) ||
          r.enrollmentId.toLowerCase().includes(term) ||
          r.studentId.toLowerCase().includes(term),
      );
    }
    return result;
  }, [requests, searchTerm, statusFilter]);

  const paginated = useMemo(() => {
    const start = (page - 1) * pageSize;
    return filtered.slice(start, start + pageSize);
  }, [filtered, page, pageSize]);

  const totalPages = Math.max(1, Math.ceil(filtered.length / pageSize));

  const handlePageChange = useCallback((p: number) => setPage(p), []);
  const handlePageSizeChange = useCallback((s: number) => { setPageSize(s); setPage(1); }, []);
  const clearFilters = useCallback(() => { setSearchTerm(''); setStatusFilter('all'); }, [setSearchTerm, setStatusFilter]);

  if (isLoading) return <EnrollmentTableSkeleton />;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Enrollment Requests</h1>
        <p style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)', margin: '4px 0 0 0' }}>
          Manage all student enrollment applications
        </p>
      </div>

      <div style={{ display: 'flex', alignItems: 'center', gap: 8, flexWrap: 'wrap' }}>
        <StudentSearchBar searchQuery={searchTerm} onSearchChange={setSearchTerm} />
        <select
          value={statusFilter}
          onChange={(e) => setStatusFilter(e.target.value as EnrollmentStatus | 'all')}
          aria-label="Filter by status"
          style={{
            padding: '6px 12px', borderRadius: 'var(--radius-sm)',
            border: '1px solid var(--color-border-default)',
            background: 'var(--color-bg-surface-default)', color: 'var(--color-text-primary)',
            fontSize: 'var(--text-body-sm)', height: 36,
          }}
        >
          {STATUS_OPTIONS.map((s) => (
            <option key={s} value={s}>{s === 'all' ? 'All Statuses' : ENROLLMENT_STATUS_LABELS[s]}</option>
          ))}
        </select>
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
          {filtered.length} result{filtered.length !== 1 ? 's' : ''}
        </span>
        <div style={{ flex: 1 }} />
        <div style={{ display: 'flex', gap: 4 }} role="radiogroup" aria-label="View mode">
          {(['table', 'card'] as const).map((mode) => (
            <button
              key={mode}
              type="button"
              onClick={() => setViewMode(mode)}
              aria-label={mode === 'table' ? 'Table view' : 'Card view'}
              aria-pressed={viewMode === mode}
              style={{
                display: 'inline-flex', alignItems: 'center', justifyContent: 'center',
                width: 32, height: 32, borderRadius: 'var(--radius-sm)',
                background: viewMode === mode ? 'var(--color-bg-primary-subtle)' : 'var(--color-bg-surface-default)',
                border: '1px solid var(--color-border-default)',
                cursor: 'pointer',
                color: viewMode === mode ? 'var(--color-primary)' : 'var(--color-text-secondary)',
              }}
            >
              {mode === 'table' ? '📋' : '📇'}
            </button>
          ))}
        </div>
      </div>

      {filtered.length === 0 ? (
        <EmptyState type="noSearchResults" onClearFilters={clearFilters} />
      ) : viewMode === 'table' ? (
        <div style={{
          padding: 'var(--space-3)', borderRadius: 'var(--radius-md)',
          border: '1px solid var(--color-border-default)',
          background: 'var(--color-bg-surface-default)',
        }}>
          <EnrollmentTable requests={paginated} onSelect={selectRequest} selectedId={currentRequest?.id ?? null} />
          <StudentPagination
            page={page} totalPages={totalPages} totalFiltered={filtered.length}
            pageSize={pageSize} onPageChange={handlePageChange} onPageSizeChange={handlePageSizeChange}
            pageSizeOptions={[10, 20, 50]}
          />
        </div>
      ) : (
        <>
          <div style={{
            display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))',
            gap: 'var(--space-component-gap)',
          }}>
            {paginated.map((req) => (
              <EnrollmentCard
                key={req.id}
                request={req}
                onSelect={selectRequest}
                selected={currentRequest?.id === req.id}
              />
            ))}
          </div>
          <StudentPagination
            page={page} totalPages={totalPages} totalFiltered={filtered.length}
            pageSize={pageSize} onPageChange={handlePageChange} onPageSizeChange={handlePageSizeChange}
            pageSizeOptions={[10, 20, 50]}
          />
        </>
      )}

      {currentRequest && (
        <div style={{
          padding: 'var(--space-3)', borderRadius: 'var(--radius-md)',
          border: '1px solid var(--color-border-default)',
          background: 'var(--color-bg-surface-default)',
        }}>
          <h3 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', margin: '0 0 var(--space-2) 0' }}>
            Enrollment Details — {currentRequest.applicationNumber}
          </h3>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))', gap: 8, fontSize: 'var(--text-body-sm)' }}>
            <div><span style={{ color: 'var(--color-text-tertiary)' }}>Student:</span> {currentRequest.studentName} ({currentRequest.studentId})</div>
            <div><span style={{ color: 'var(--color-text-tertiary)' }}>Course:</span> {currentRequest.courseName}</div>
            <div><span style={{ color: 'var(--color-text-tertiary)' }}>Type:</span> {ADMISSION_TYPE_LABELS[currentRequest.admissionType]}</div>
            <div><span style={{ color: 'var(--color-text-tertiary)' }}>Mode:</span> {currentRequest.trainingMode}</div>
            <div><span style={{ color: 'var(--color-text-tertiary)' }}>Status:</span> <EnrollmentStatusBadge status={currentRequest.enrollmentStatus} /></div>
            <div><span style={{ color: 'var(--color-text-tertiary)' }}>Source:</span> {currentRequest.enrollmentSource}</div>
            <div><span style={{ color: 'var(--color-text-tertiary)' }}>Date:</span> {currentRequest.applicationDate}</div>
            <div><span style={{ color: 'var(--color-text-tertiary)' }}>Batch:</span> {currentRequest.batchName || 'Not assigned'}</div>
          </div>
        </div>
      )}
    </div>
  );
}
