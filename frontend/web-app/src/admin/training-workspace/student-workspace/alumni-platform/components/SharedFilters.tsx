import { memo, useId } from 'react';
import { useAlumni } from '../state/AlumniContext';
import { ALUMNI_NAV_ITEMS, PLACEMENT_STATUS_LABELS, JOB_TYPE_LABELS, TIER_LABELS, ENGAGEMENT_LABELS, DRIVE_STAGE_LABELS } from '../types';
import type { PlacementStatus, JobOpportunityType, CompanyPartnershipTier, AlumniEngagementLevel, PlacementDriveStage } from '../types';

const SORT_OPTIONS = [
  { key: 'date', label: 'Newest' },
  { key: 'date|asc', label: 'Oldest' },
  { key: 'name', label: 'Name' },
  { key: 'tier', label: 'Tier' },
  { key: 'status', label: 'Status' },
  { key: 'company', label: 'Company' },
  { key: 'engagement', label: 'Engagement' },
];

export const SharedFilters = memo(function SharedFilters({ currentPage, showSort }: { currentPage?: string; showSort?: boolean }) {
  const {
    filters, setSearch, setStatusFilter, setTypeFilter,
    setTierFilter, setEngagementFilter, setDriveStageFilter, setDateFrom, setDateTo, setSort,
  } = useAlumni();
  const searchId = useId();
  const navItem = ALUMNI_NAV_ITEMS.find((n) => n.id === currentPage);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
      {navItem && <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{navItem.description}</div>}
      <div style={{ display: 'flex', flexWrap: 'wrap', gap: 8, alignItems: 'center' }}>
        <div style={{ flex: 1, minWidth: 160, maxWidth: 240 }}>
          <label htmlFor={searchId} style={{ position: 'absolute', width: 1, height: 1, overflow: 'hidden', clip: 'rect(0 0 0 0)' }}>Search</label>
          <input id={searchId} type="search" value={filters.search} onChange={(e) => setSearch(e.target.value)} placeholder="Search..." style={{ width: '100%', padding: '6px 12px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', fontSize: 'var(--text-body-sm)', background: 'var(--color-bg-surface-default)' }} />
        </div>
        {currentPage?.includes('placement') && (
          <select value={filters.driveStage} onChange={(e) => setDriveStageFilter(e.target.value as PlacementDriveStage | 'all')} style={{ padding: '6px 12px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', fontSize: 'var(--text-body-sm)', background: 'var(--color-bg-surface-default)' }}>
            <option value="all">All Stages</option>
            {(Object.entries(DRIVE_STAGE_LABELS) as [PlacementDriveStage, string][]).map(([key, label]) => <option key={key} value={key}>{label}</option>)}
          </select>
        )}
        {currentPage?.includes('jobs') && (
          <>
            <select value={filters.type} onChange={(e) => setTypeFilter(e.target.value as JobOpportunityType | 'all')} style={{ padding: '6px 12px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', fontSize: 'var(--text-body-sm)', background: 'var(--color-bg-surface-default)' }}>
              <option value="all">All Types</option>
              {(Object.entries(JOB_TYPE_LABELS) as [JobOpportunityType, string][]).map(([key, label]) => <option key={key} value={key}>{label}</option>)}
            </select>
            <select value={filters.tier} onChange={(e) => setTierFilter(e.target.value as CompanyPartnershipTier | 'all')} style={{ padding: '6px 12px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', fontSize: 'var(--text-body-sm)', background: 'var(--color-bg-surface-default)' }}>
              <option value="all">All Tiers</option>
              {(Object.entries(TIER_LABELS) as [CompanyPartnershipTier, string][]).map(([key, label]) => <option key={key} value={key}>{label}</option>)}
            </select>
          </>
        )}
        {currentPage?.includes('companies') && (
          <select value={filters.tier} onChange={(e) => setTierFilter(e.target.value as CompanyPartnershipTier | 'all')} style={{ padding: '6px 12px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', fontSize: 'var(--text-body-sm)', background: 'var(--color-bg-surface-default)' }}>
            <option value="all">All Tiers</option>
            {(Object.entries(TIER_LABELS) as [CompanyPartnershipTier, string][]).map(([key, label]) => <option key={key} value={key}>{label}</option>)}
          </select>
        )}
        {currentPage?.includes('directory') && (
          <select value={filters.engagement} onChange={(e) => setEngagementFilter(e.target.value as AlumniEngagementLevel | 'all')} style={{ padding: '6px 12px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', fontSize: 'var(--text-body-sm)', background: 'var(--color-bg-surface-default)' }}>
            <option value="all">All Engagement</option>
            {(Object.entries(ENGAGEMENT_LABELS) as [AlumniEngagementLevel, string][]).map(([key, label]) => <option key={key} value={key}>{label}</option>)}
          </select>
        )}
        {currentPage?.includes('career') && (
          <select value={filters.status} onChange={(e) => setStatusFilter(e.target.value as PlacementStatus | 'all')} style={{ padding: '6px 12px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', fontSize: 'var(--text-body-sm)', background: 'var(--color-bg-surface-default)' }}>
            <option value="all">All Statuses</option>
            {(Object.entries(PLACEMENT_STATUS_LABELS) as [PlacementStatus, string][]).map(([key, label]) => <option key={key} value={key}>{label}</option>)}
          </select>
        )}
        <input type="date" value={filters.dateFrom} onChange={(e) => setDateFrom(e.target.value)} title="From date" style={{ padding: '5px 8px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', fontSize: 'var(--text-body-sm)', background: 'var(--color-bg-surface-default)', maxWidth: 140 }} />
        <input type="date" value={filters.dateTo} onChange={(e) => setDateTo(e.target.value)} title="To date" style={{ padding: '5px 8px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', fontSize: 'var(--text-body-sm)', background: 'var(--color-bg-surface-default)', maxWidth: 140 }} />
        {showSort && (
          <select value={`${filters.sortKey}${filters.sortDirection === 'asc' ? '|asc' : ''}`} onChange={(e) => { const [k] = e.target.value.split('|'); setSort(k as any); }} style={{ padding: '6px 12px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', fontSize: 'var(--text-body-sm)', background: 'var(--color-bg-surface-default)' }}>
            {SORT_OPTIONS.map((opt) => <option key={opt.key} value={opt.key}>{opt.label}</option>)}
          </select>
        )}
      </div>
    </div>
  );
});
