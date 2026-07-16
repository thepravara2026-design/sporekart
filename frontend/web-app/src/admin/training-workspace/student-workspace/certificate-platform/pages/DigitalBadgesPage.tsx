import { useState, useMemo } from 'react';
import { useCertificates } from '../state/CertificateContext';
import { BadgeCard } from '../components/BadgeCard';
import { DashboardWidget } from '../components/DashboardWidget';
import { EmptyState } from '../components/EmptyStates';
import { StudentSearchBar } from '../../components/StudentSearchFilter';
import { StudentPagination } from '../../components/StudentPagination';
import { BADGE_TYPE_LABELS } from '../types';
import type { BadgeType } from '../types';

export function DigitalBadgesPage() {
  const { badges } = useCertificates();
  const [searchTerm, setSearchTerm] = useState('');
  const [typeFilter, setTypeFilter] = useState<BadgeType | 'all'>('all');
  const [page, setPage] = useState(1);
  const perPage = 12;

  const nftCount = useMemo(() => badges.filter((b) => b.isNft).length, [badges]);

  const filtered = useMemo(() => {
    let result = badges;
    if (typeFilter !== 'all') result = result.filter((b) => b.badgeType === typeFilter);
    if (searchTerm) {
      const term = searchTerm.toLowerCase();
      result = result.filter((b) => b.name.toLowerCase().includes(term) || b.studentName.toLowerCase().includes(term));
    }
    return result;
  }, [badges, searchTerm, typeFilter]);

  const totalPages = Math.ceil(filtered.length / perPage);
  const paginated = filtered.slice((page - 1) * perPage, page * perPage);

  const handleClearFilters = () => { setSearchTerm(''); setTypeFilter('all'); setPage(1); };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Digital Badges</h2>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <DashboardWidget label="Total Badges" value={badges.length} variant="default" subtitle="All badges" />
        <DashboardWidget label="Types" value={Object.keys(BADGE_TYPE_LABELS).length} variant="info" subtitle="Badge categories" />
        <DashboardWidget label="NFT Badges" value={nftCount} variant={nftCount > 0 ? 'warning' : 'default'} subtitle="Blockchain ready" />
      </div>

      <div style={{ display: 'flex', gap: 'var(--space-component-gap)', flexWrap: 'wrap', alignItems: 'center' }}>
        <div style={{ flex: 1, minWidth: 200 }}>
          <StudentSearchBar searchQuery={searchTerm} onSearchChange={(v: string) => { setSearchTerm(v); setPage(1); }} />
        </div>
        <select aria-label="Filter by type" value={typeFilter} onChange={(e) => { setTypeFilter(e.target.value as BadgeType | 'all'); setPage(1); }}
          style={{ padding: '6px 12px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', fontSize: 'var(--text-body-sm)', background: 'var(--color-bg-surface-default)' }}>
          <option value="all">All Types</option>
          {Object.entries(BADGE_TYPE_LABELS).map(([k, v]) => (<option key={k} value={k}>{v}</option>))}
        </select>
      </div>

      {filtered.length === 0 ? (
        <EmptyState type={searchTerm || typeFilter !== 'all' ? 'noSearchResults' : 'noBadges'} onClearFilters={handleClearFilters} />
      ) : (
        <>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Showing {paginated.length} of {filtered.length} badges</div>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: 'var(--space-component-gap)' }}>
            {paginated.map((b) => (<BadgeCard key={b.id} badge={b} />))}
          </div>
          {totalPages > 1 && (
            <StudentPagination page={page} totalPages={totalPages} totalFiltered={filtered.length} pageSize={perPage} onPageChange={setPage} onPageSizeChange={() => {}} pageSizeOptions={[12, 24, 48]} />
          )}
        </>
      )}
    </div>
  );
}
