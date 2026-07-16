import { useState, useMemo } from 'react';
import { useProgress } from '../state/LearningProgressContext';
import { ProgressTable } from '../components/ProgressTable';
import { ProgressCard } from '../components/ProgressCard';
import { EmptyState } from '../components/EmptyStates';
import { ProgressTableSkeleton } from '../components/Skeletons';
import { StudentSearchBar } from '../../components/StudentSearchFilter';
import { StudentPagination } from '../../components/StudentPagination';
import { PROGRESS_STATUS_LABELS } from '../types';
import type { ProgressStatus } from '../types';

type ViewMode = 'table' | 'card';

export function StudentProgressPage() {
  const { progressRecords, setSearchTerm, setStatusFilter, searchTerm, statusFilter, getFilteredProgress } = useProgress();
  const [viewMode, setViewMode] = useState<ViewMode>('table');
  const [page, setPage] = useState(1);
  const [selectedId, setSelectedId] = useState<string | null>(null);
  const perPage = 10;

  const filtered = useMemo(() => getFilteredProgress(), [getFilteredProgress]);
  const totalPages = Math.ceil(filtered.length / perPage);
  const paginated = filtered.slice((page - 1) * perPage, page * perPage);

  const handleClearFilters = () => {
    setSearchTerm('');
    setStatusFilter('all');
    setPage(1);
  };

  if (!progressRecords) return <ProgressTableSkeleton />;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Student Progress</h2>

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
          onChange={(e) => { setStatusFilter(e.target.value as ProgressStatus | 'all'); setPage(1); }}
          style={{
            padding: '6px 12px', borderRadius: 'var(--radius-sm)',
            border: '1px solid var(--color-border-default)',
            fontSize: 'var(--text-body-sm)', background: 'var(--color-bg-surface-default)',
          }}
        >
          <option value="all">All Statuses</option>
          {Object.entries(PROGRESS_STATUS_LABELS).map(([key, label]) => (
            <option key={key} value={key}>{label}</option>
          ))}
        </select>
        <div style={{ display: 'flex', gap: 4, borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', overflow: 'hidden' }} role="radiogroup" aria-label="View mode">
          <button onClick={() => setViewMode('table')} aria-pressed={viewMode === 'table'} style={{ padding: '6px 12px', border: 'none', cursor: 'pointer', background: viewMode === 'table' ? 'var(--color-bg-primary-subtle)' : 'transparent', color: viewMode === 'table' ? 'var(--color-primary)' : 'var(--color-text-secondary)', fontSize: 'var(--text-body-sm)' }}>Table</button>
          <button onClick={() => setViewMode('card')} aria-pressed={viewMode === 'card'} style={{ padding: '6px 12px', border: 'none', cursor: 'pointer', background: viewMode === 'card' ? 'var(--color-bg-primary-subtle)' : 'transparent', color: viewMode === 'card' ? 'var(--color-primary)' : 'var(--color-text-secondary)', fontSize: 'var(--text-body-sm)' }}>Card</button>
        </div>
      </div>

      {filtered.length === 0 ? (
        <EmptyState type={searchTerm ? 'noSearchResults' : 'noProgress'} onClearFilters={handleClearFilters} />
      ) : (
        <>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
            Showing {paginated.length} of {filtered.length} progress records
          </div>
          {viewMode === 'table' ? (
            <ProgressTable records={paginated} onSelect={setSelectedId} selectedId={selectedId} />
          ) : (
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: 'var(--space-component-gap)' }}>
              {paginated.map((rec) => (
                <ProgressCard key={rec.id} record={rec} onSelect={setSelectedId} selected={selectedId === rec.id} />
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
