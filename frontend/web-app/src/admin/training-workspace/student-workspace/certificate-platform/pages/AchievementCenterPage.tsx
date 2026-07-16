import { useState, useMemo } from 'react';
import { useCertificates } from '../state/CertificateContext';
import { AchievementCard } from '../components/AchievementCard';
import { DashboardWidget } from '../components/DashboardWidget';
import { EmptyState } from '../components/EmptyStates';
import { StudentSearchBar } from '../../components/StudentSearchFilter';
import { StudentPagination } from '../../components/StudentPagination';
import { ACHIEVEMENT_TYPE_LABELS } from '../types';
import type { AchievementType } from '../types';

export function AchievementCenterPage() {
  const { achievements } = useCertificates();
  const [searchTerm, setSearchTerm] = useState('');
  const [typeFilter, setTypeFilter] = useState<AchievementType | 'all'>('all');
  const [page, setPage] = useState(1);
  const perPage = 12;

  const totalPoints = useMemo(() => achievements.reduce((s, a) => s + a.points, 0), [achievements]);

  const filtered = useMemo(() => {
    let result = achievements;
    if (typeFilter !== 'all') result = result.filter((a) => a.achievementType === typeFilter);
    if (searchTerm) {
      const term = searchTerm.toLowerCase();
      result = result.filter((a) => a.name.toLowerCase().includes(term) || a.studentName.toLowerCase().includes(term) || a.courseName.toLowerCase().includes(term));
    }
    return result;
  }, [achievements, searchTerm, typeFilter]);

  const totalPages = Math.ceil(filtered.length / perPage);
  const paginated = filtered.slice((page - 1) * perPage, page * perPage);

  const handleClearFilters = () => { setSearchTerm(''); setTypeFilter('all'); setPage(1); };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Achievement Center</h2>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <DashboardWidget label="Total Achievements" value={achievements.length} variant="success" />
        <DashboardWidget label="Total Points" value={totalPoints} variant="info" subtitle="Cumulative" />
        <DashboardWidget label="Types" value={Object.keys(ACHIEVEMENT_TYPE_LABELS).length} variant="default" subtitle="Achievement categories" />
      </div>

      <div style={{ display: 'flex', gap: 'var(--space-component-gap)', flexWrap: 'wrap', alignItems: 'center' }}>
        <div style={{ flex: 1, minWidth: 200 }}>
          <StudentSearchBar searchQuery={searchTerm} onSearchChange={(v: string) => { setSearchTerm(v); setPage(1); }} />
        </div>
        <select aria-label="Filter by type" value={typeFilter} onChange={(e) => { setTypeFilter(e.target.value as AchievementType | 'all'); setPage(1); }}
          style={{ padding: '6px 12px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', fontSize: 'var(--text-body-sm)', background: 'var(--color-bg-surface-default)' }}>
          <option value="all">All Types</option>
          {Object.entries(ACHIEVEMENT_TYPE_LABELS).map(([k, v]) => (<option key={k} value={k}>{v}</option>))}
        </select>
      </div>

      {filtered.length === 0 ? (
        <EmptyState type={searchTerm || typeFilter !== 'all' ? 'noSearchResults' : 'noAchievements'} onClearFilters={handleClearFilters} />
      ) : (
        <>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Showing {paginated.length} of {filtered.length} achievements</div>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: 'var(--space-component-gap)' }}>
            {paginated.map((a) => (<AchievementCard key={a.id} achievement={a} />))}
          </div>
          {totalPages > 1 && (
            <StudentPagination page={page} totalPages={totalPages} totalFiltered={filtered.length} pageSize={perPage} onPageChange={setPage} onPageSizeChange={() => {}} pageSizeOptions={[12, 24, 48]} />
          )}
        </>
      )}
    </div>
  );
}
