import { useState, useMemo } from 'react';
import { useProgress } from '../state/LearningProgressContext';
import { CompetencyCard } from '../components/CompetencyCard';
import { EmptyState } from '../components/EmptyStates';
import { StudentSearchBar } from '../../components/StudentSearchFilter';
import { StudentPagination } from '../../components/StudentPagination';
import { COMPETENCY_CATEGORY_LABELS } from '../types';
import type { CompetencyCategory } from '../types';

export function CompetencyCenterPage() {
  const { competencies } = useProgress();
  const [searchTerm, setSearchTerm] = useState('');
  const [categoryFilter, setCategoryFilter] = useState<CompetencyCategory | 'all'>('all');
  const [page, setPage] = useState(1);
  const perPage = 12;

  const filtered = useMemo(() => {
    let result = competencies;
    if (categoryFilter !== 'all') result = result.filter((c) => c.category === categoryFilter);
    if (searchTerm) {
      const term = searchTerm.toLowerCase();
      result = result.filter((c) => c.name.toLowerCase().includes(term) || c.studentName.toLowerCase().includes(term) || c.courseName.toLowerCase().includes(term));
    }
    return result;
  }, [competencies, searchTerm, categoryFilter]);

  const totalPages = Math.ceil(filtered.length / perPage);
  const paginated = filtered.slice((page - 1) * perPage, page * perPage);

  const handleClearFilters = () => {
    setSearchTerm('');
    setCategoryFilter('all');
    setPage(1);
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Competency Center</h2>

      <div style={{ display: 'flex', gap: 'var(--space-component-gap)', flexWrap: 'wrap', alignItems: 'center' }}>
        <div style={{ flex: 1, minWidth: 200 }}>
          <StudentSearchBar searchQuery={searchTerm} onSearchChange={(v: string) => { setSearchTerm(v); setPage(1); }} />
        </div>
        <select
          aria-label="Filter by category"
          value={categoryFilter}
          onChange={(e) => { setCategoryFilter(e.target.value as CompetencyCategory | 'all'); setPage(1); }}
          style={{
            padding: '6px 12px', borderRadius: 'var(--radius-sm)',
            border: '1px solid var(--color-border-default)',
            fontSize: 'var(--text-body-sm)', background: 'var(--color-bg-surface-default)',
          }}
        >
          <option value="all">All Categories</option>
          {Object.entries(COMPETENCY_CATEGORY_LABELS).map(([key, label]) => (
            <option key={key} value={key}>{label}</option>
          ))}
        </select>
      </div>

      {filtered.length === 0 ? (
        <EmptyState type={searchTerm || categoryFilter !== 'all' ? 'noSearchResults' : 'noCompetencies'} onClearFilters={handleClearFilters} />
      ) : (
        <>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
            Showing {paginated.length} of {filtered.length} competencies
          </div>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: 'var(--space-component-gap)' }}>
            {paginated.map((comp) => (
              <CompetencyCard key={comp.id} competency={comp} />
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
