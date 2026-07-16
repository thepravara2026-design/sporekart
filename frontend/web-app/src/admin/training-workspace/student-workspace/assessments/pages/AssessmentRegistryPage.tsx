import { useState, useMemo } from 'react';
import { useAssessments } from '../state/AssessmentContext';
import { AssessmentTable } from '../components/AssessmentTable';
import { AssessmentCard } from '../components/AssessmentCard';
import { EmptyState } from '../components/EmptyStates';
import { AssessmentTableSkeleton } from '../components/Skeletons';
import { StudentSearchBar } from '../../components/StudentSearchFilter';
import { StudentPagination } from '../../components/StudentPagination';
import { ASSESSMENT_STATUS_LABELS, ASSESSMENT_TYPE_LABELS } from '../types';
import type { AssessmentStatus, AssessmentType } from '../types';

type ViewMode = 'table' | 'card';

export function AssessmentRegistryPage() {
  const { assessments, setSearchTerm, setStatusFilter, setTypeFilter, searchTerm, statusFilter, typeFilter } = useAssessments();
  const [viewMode, setViewMode] = useState<ViewMode>('table');
  const [page, setPage] = useState(1);
  const [selectedId, setSelectedId] = useState<string | null>(null);
  const perPage = 10;

  const filtered = useMemo(() => {
    let result = assessments;
    if (statusFilter !== 'all') result = result.filter((a) => a.status === statusFilter);
    if (typeFilter !== 'all') result = result.filter((a) => a.assessmentType === typeFilter);
    if (searchTerm) {
      const term = searchTerm.toLowerCase();
      result = result.filter((a) => a.title.toLowerCase().includes(term) || a.assessmentCode.toLowerCase().includes(term) || a.courseName.toLowerCase().includes(term));
    }
    return result;
  }, [assessments, searchTerm, statusFilter, typeFilter]);

  const totalPages = Math.ceil(filtered.length / perPage);
  const paginated = filtered.slice((page - 1) * perPage, page * perPage);

  const handleClearFilters = () => {
    setSearchTerm('');
    setStatusFilter('all');
    setTypeFilter('all');
  };

  if (!assessments) return <AssessmentTableSkeleton />;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Assessment Registry</h2>

      <div style={{ display: 'flex', gap: 'var(--space-component-gap)', flexWrap: 'wrap', alignItems: 'center' }}>
        <div style={{ flex: 1, minWidth: 200 }}>
          <StudentSearchBar
            searchQuery={searchTerm}
            onSearchChange={(v: string) => { setSearchTerm(v); setPage(1); }}
          />
        </div>
        <select
          aria-label="Filter by status"
          value={statusFilter}
          onChange={(e) => { setStatusFilter(e.target.value as AssessmentStatus | 'all'); setPage(1); }}
          style={{
            padding: '6px 12px', borderRadius: 'var(--radius-sm)',
            border: '1px solid var(--color-border-default)',
            fontSize: 'var(--text-body-sm)', background: 'var(--color-bg-surface-default)',
          }}
        >
          <option value="all">All Statuses</option>
          {Object.entries(ASSESSMENT_STATUS_LABELS).map(([key, label]) => (
            <option key={key} value={key}>{label}</option>
          ))}
        </select>
        <select
          aria-label="Filter by type"
          value={typeFilter}
          onChange={(e) => { setTypeFilter(e.target.value as AssessmentType | 'all'); setPage(1); }}
          style={{
            padding: '6px 12px', borderRadius: 'var(--radius-sm)',
            border: '1px solid var(--color-border-default)',
            fontSize: 'var(--text-body-sm)', background: 'var(--color-bg-surface-default)',
          }}
        >
          <option value="all">All Types</option>
          {Object.entries(ASSESSMENT_TYPE_LABELS).map(([key, label]) => (
            <option key={key} value={key}>{label}</option>
          ))}
        </select>
        <div style={{ display: 'flex', gap: 4, borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', overflow: 'hidden' }} role="radiogroup" aria-label="View mode">
          <button onClick={() => setViewMode('table')} aria-pressed={viewMode === 'table'} style={{ padding: '6px 12px', border: 'none', cursor: 'pointer', background: viewMode === 'table' ? 'var(--color-bg-primary-subtle)' : 'transparent', color: viewMode === 'table' ? 'var(--color-primary)' : 'var(--color-text-secondary)', fontSize: 'var(--text-body-sm)' }}>Table</button>
          <button onClick={() => setViewMode('card')} aria-pressed={viewMode === 'card'} style={{ padding: '6px 12px', border: 'none', cursor: 'pointer', background: viewMode === 'card' ? 'var(--color-bg-primary-subtle)' : 'transparent', color: viewMode === 'card' ? 'var(--color-primary)' : 'var(--color-text-secondary)', fontSize: 'var(--text-body-sm)' }}>Card</button>
        </div>
      </div>

      {filtered.length === 0 ? (
        <EmptyState type={searchTerm ? 'noSearchResults' : 'noAssessments'} onClearFilters={handleClearFilters} />
      ) : (
        <>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
            Showing {paginated.length} of {filtered.length} assessments
          </div>
          {viewMode === 'table' ? (
            <AssessmentTable assessments={paginated} onSelect={setSelectedId} selectedId={selectedId} />
          ) : (
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: 'var(--space-component-gap)' }}>
              {paginated.map((a) => (
                <AssessmentCard key={a.id} assessment={a} onSelect={setSelectedId} selected={selectedId === a.id} />
              ))}
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
