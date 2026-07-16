import { useState, useMemo } from 'react';
import { useCertificates } from '../state/CertificateContext';
import { CertificateTable } from '../components/CertificateTable';
import { CertificateCard } from '../components/CertificateCard';
import { EmptyState } from '../components/EmptyStates';
import { CertificateTableSkeleton } from '../components/Skeletons';
import { StudentSearchBar } from '../../components/StudentSearchFilter';
import { StudentPagination } from '../../components/StudentPagination';
import { CERTIFICATE_STATUS_LABELS, CERTIFICATE_TYPE_LABELS } from '../types';
import type { CertificateStatus, CertificateType } from '../types';

type ViewMode = 'table' | 'card';
type SortField = 'newest' | 'oldest' | 'certNumber' | 'student' | 'course' | 'status';

const COURSES = Array.from(new Set([
  'Full Stack Web Development', 'Data Science & Analytics', 'Cloud Architecture',
  'Mobile App Development', 'DevOps Engineering', 'UI/UX Design',
]));

export function CertificateRegistryPage() {
  const { certificates, setSearchTerm, setStatusFilter, setTypeFilter, searchTerm, statusFilter, typeFilter, getFilteredCertificates } = useCertificates();
  const [viewMode, setViewMode] = useState<ViewMode>('table');
  const [page, setPage] = useState(1);
  const [selectedId, setSelectedId] = useState<string | null>(null);
  const [courseFilter, setCourseFilter] = useState<string>('all');
  const [sortField, setSortField] = useState<SortField>('newest');
  const perPage = 10;

  const filtered = useMemo(() => {
    let result = getFilteredCertificates();
    if (courseFilter !== 'all') {
      result = result.filter((c) => c.courseName === courseFilter);
    }
    result = [...result].sort((a, b) => {
      switch (sortField) {
        case 'newest': return new Date(b.issueDate || '0').getTime() - new Date(a.issueDate || '0').getTime();
        case 'oldest': return new Date(a.issueDate || '0').getTime() - new Date(b.issueDate || '0').getTime();
        case 'certNumber': return a.certificateNumber.localeCompare(b.certificateNumber);
        case 'student': return a.studentName.localeCompare(b.studentName);
        case 'course': return a.courseName.localeCompare(b.courseName);
        case 'status': return a.status.localeCompare(b.status);
        default: return 0;
      }
    });
    return result;
  }, [getFilteredCertificates, courseFilter, sortField]);

  const totalPages = Math.ceil(filtered.length / perPage);
  const paginated = filtered.slice((page - 1) * perPage, page * perPage);

  const handleClearFilters = () => {
    setSearchTerm('');
    setStatusFilter('all');
    setTypeFilter('all');
    setCourseFilter('all');
    setSortField('newest');
    setPage(1);
  };

  if (!certificates) return <CertificateTableSkeleton />;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Certificate Registry</h2>

      <div style={{ display: 'flex', gap: 'var(--space-component-gap)', flexWrap: 'wrap', alignItems: 'center' }}>
        <div style={{ flex: 1, minWidth: 200 }}>
          <StudentSearchBar searchQuery={searchTerm} onSearchChange={(v: string) => { setSearchTerm(v); setPage(1); }} />
        </div>
        <select aria-label="Filter by status" value={statusFilter} onChange={(e) => { setStatusFilter(e.target.value as CertificateStatus | 'all'); setPage(1); }}
          style={{ padding: '6px 12px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', fontSize: 'var(--text-body-sm)', background: 'var(--color-bg-surface-default)' }}>
          <option value="all">All Statuses</option>
          {Object.entries(CERTIFICATE_STATUS_LABELS).map(([k, v]) => (<option key={k} value={k}>{v}</option>))}
        </select>
        <select aria-label="Filter by type" value={typeFilter} onChange={(e) => { setTypeFilter(e.target.value as CertificateType | 'all'); setPage(1); }}
          style={{ padding: '6px 12px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', fontSize: 'var(--text-body-sm)', background: 'var(--color-bg-surface-default)' }}>
          <option value="all">All Types</option>
          {Object.entries(CERTIFICATE_TYPE_LABELS).map(([k, v]) => (<option key={k} value={k}>{v}</option>))}
        </select>
        <select aria-label="Filter by course" value={courseFilter} onChange={(e) => { setCourseFilter(e.target.value); setPage(1); }}
          style={{ padding: '6px 12px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', fontSize: 'var(--text-body-sm)', background: 'var(--color-bg-surface-default)' }}>
          <option value="all">All Courses</option>
          {COURSES.map((c) => (<option key={c} value={c}>{c}</option>))}
        </select>
        <select aria-label="Sort by" value={sortField} onChange={(e) => { setSortField(e.target.value as SortField); setPage(1); }}
          style={{ padding: '6px 12px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', fontSize: 'var(--text-body-sm)', background: 'var(--color-bg-surface-default)' }}>
          <option value="newest">Newest</option>
          <option value="oldest">Oldest</option>
          <option value="certNumber">Cert Number</option>
          <option value="student">Student</option>
          <option value="course">Course</option>
          <option value="status">Status</option>
        </select>
        <div style={{ display: 'flex', gap: 4, borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', overflow: 'hidden' }} role="radiogroup" aria-label="View mode">
          <button onClick={() => setViewMode('table')} aria-pressed={viewMode === 'table'} style={{ padding: '6px 12px', border: 'none', cursor: 'pointer', background: viewMode === 'table' ? 'var(--color-bg-primary-subtle)' : 'transparent', color: viewMode === 'table' ? 'var(--color-primary)' : 'var(--color-text-secondary)', fontSize: 'var(--text-body-sm)' }}>Table</button>
          <button onClick={() => setViewMode('card')} aria-pressed={viewMode === 'card'} style={{ padding: '6px 12px', border: 'none', cursor: 'pointer', background: viewMode === 'card' ? 'var(--color-bg-primary-subtle)' : 'transparent', color: viewMode === 'card' ? 'var(--color-primary)' : 'var(--color-text-secondary)', fontSize: 'var(--text-body-sm)' }}>Card</button>
        </div>
      </div>

      {filtered.length === 0 ? (
        <EmptyState type={searchTerm ? 'noSearchResults' : 'noCertificates'} onClearFilters={handleClearFilters} />
      ) : (
        <>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Showing {paginated.length} of {filtered.length} certificates</div>
          {viewMode === 'table' ? (
            <CertificateTable certificates={paginated} onSelect={setSelectedId} selectedId={selectedId} />
          ) : (
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: 'var(--space-component-gap)' }}>
              {paginated.map((c) => (<CertificateCard key={c.id} certificate={c} onSelect={setSelectedId} selected={selectedId === c.id} />))}
            </div>
          )}
          {totalPages > 1 && (
            <StudentPagination page={page} totalPages={totalPages} totalFiltered={filtered.length} pageSize={perPage} onPageChange={setPage} onPageSizeChange={() => {}} pageSizeOptions={[10, 20, 50]} />
          )}
        </>
      )}
    </div>
  );
}
