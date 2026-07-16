import { useState, useMemo } from 'react';
import { useProgress } from '../state/LearningProgressContext';
import { MilestoneCard } from '../components/MilestoneCard';
import { EmptyState } from '../components/EmptyStates';
import { StudentSearchBar } from '../../components/StudentSearchFilter';
import { StudentPagination } from '../../components/StudentPagination';
import { MILESTONE_TYPE_LABELS } from '../types';
import type { MilestoneType } from '../types';

export function MilestoneCenterPage() {
  const { milestones } = useProgress();
  const [searchTerm, setSearchTerm] = useState('');
  const [typeFilter, setTypeFilter] = useState<MilestoneType | 'all'>('all');
  const [achievedFilter, setAchievedFilter] = useState<'all' | 'achieved' | 'pending'>('all');
  const [page, setPage] = useState(1);
  const perPage = 12;

  const filtered = useMemo(() => {
    let result = milestones;
    if (typeFilter !== 'all') result = result.filter((m) => m.type === typeFilter);
    if (achievedFilter === 'achieved') result = result.filter((m) => m.achieved);
    if (achievedFilter === 'pending') result = result.filter((m) => !m.achieved);
    if (searchTerm) {
      const term = searchTerm.toLowerCase();
      result = result.filter((m) => m.label.toLowerCase().includes(term) || m.studentName.toLowerCase().includes(term) || m.courseName.toLowerCase().includes(term));
    }
    return result;
  }, [milestones, searchTerm, typeFilter, achievedFilter]);

  const totalPages = Math.ceil(filtered.length / perPage);
  const paginated = filtered.slice((page - 1) * perPage, page * perPage);

  const handleClearFilters = () => {
    setSearchTerm('');
    setTypeFilter('all');
    setAchievedFilter('all');
    setPage(1);
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Milestone Center</h2>

      <div style={{ display: 'flex', gap: 'var(--space-component-gap)', flexWrap: 'wrap', alignItems: 'center' }}>
        <div style={{ flex: 1, minWidth: 200 }}>
          <StudentSearchBar searchQuery={searchTerm} onSearchChange={(v: string) => { setSearchTerm(v); setPage(1); }} />
        </div>
        <select
          aria-label="Filter by type"
          value={typeFilter}
          onChange={(e) => { setTypeFilter(e.target.value as MilestoneType | 'all'); setPage(1); }}
          style={{
            padding: '6px 12px', borderRadius: 'var(--radius-sm)',
            border: '1px solid var(--color-border-default)',
            fontSize: 'var(--text-body-sm)', background: 'var(--color-bg-surface-default)',
          }}
        >
          <option value="all">All Types</option>
          {Object.entries(MILESTONE_TYPE_LABELS).map(([key, label]) => (
            <option key={key} value={key}>{label}</option>
          ))}
        </select>
        <select
          aria-label="Filter by status"
          value={achievedFilter}
          onChange={(e) => { setAchievedFilter(e.target.value as 'all' | 'achieved' | 'pending'); setPage(1); }}
          style={{
            padding: '6px 12px', borderRadius: 'var(--radius-sm)',
            border: '1px solid var(--color-border-default)',
            fontSize: 'var(--text-body-sm)', background: 'var(--color-bg-surface-default)',
          }}
        >
          <option value="all">All Status</option>
          <option value="achieved">Achieved</option>
          <option value="pending">Pending</option>
        </select>
      </div>

      {filtered.length === 0 ? (
        <EmptyState type={searchTerm || typeFilter !== 'all' || achievedFilter !== 'all' ? 'noSearchResults' : 'noMilestones'} onClearFilters={handleClearFilters} />
      ) : (
        <>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
            Showing {paginated.length} of {filtered.length} milestones
          </div>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: 'var(--space-component-gap)' }}>
            {paginated.map((mil) => (
              <MilestoneCard key={mil.id} milestone={mil} />
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
