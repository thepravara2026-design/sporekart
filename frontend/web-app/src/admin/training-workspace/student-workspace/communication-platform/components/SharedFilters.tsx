import { memo, useId } from 'react';
import { useCommunication } from '../state/CommunicationContext';
import { COMMUNICATION_TYPE_LABELS, PRIORITY_LABELS, COMMUNICATION_NAV_ITEMS } from '../types';
import type { CommunicationType, Priority } from '../types';

const SORT_OPTIONS = [
  { key: 'createdDate', label: 'Newest' },
  { key: 'createdDate|asc', label: 'Oldest' },
  { key: 'priority', label: 'Priority' },
  { key: 'receivedDate', label: 'Recently Read' },
  { key: 'readDate', label: 'Read Date' },
  { key: 'type', label: 'Type' },
  { key: 'title', label: 'Alphabetical' },
];

export const SharedFilters = memo(function SharedFilters({ currentPage, showSort }: { currentPage?: string; showSort?: boolean }) {
  const { filters, setSearch, setTypeFilter, setPriorityFilter, setCourseFilter, setBatchFilter, setDateFrom, setDateTo, setSort } = useCommunication();
  const searchId = useId();
  const navItem = COMMUNICATION_NAV_ITEMS.find((n) => n.id === currentPage);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
      {navItem && <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{navItem.description}</div>}
      <div style={{ display: 'flex', flexWrap: 'wrap', gap: 8, alignItems: 'center' }}>
        <div style={{ flex: 1, minWidth: 160, maxWidth: 240 }}>
          <label htmlFor={searchId} style={{ position: 'absolute', width: 1, height: 1, overflow: 'hidden', clip: 'rect(0 0 0 0)' }}>Search</label>
          <input id={searchId} type="search" value={filters.search} onChange={(e) => setSearch(e.target.value)} placeholder="Search by title..." style={{ width: '100%', padding: '6px 12px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', fontSize: 'var(--text-body-sm)', background: 'var(--color-bg-surface-default)' }} />
        </div>
        <select value={filters.type} onChange={(e) => setTypeFilter(e.target.value as CommunicationType | 'all')} style={{ padding: '6px 12px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', fontSize: 'var(--text-body-sm)', background: 'var(--color-bg-surface-default)' }}>
          <option value="all">All Types</option>
          {(Object.entries(COMMUNICATION_TYPE_LABELS) as [CommunicationType, string][]).map(([key, label]) => <option key={key} value={key}>{label}</option>)}
        </select>
        <select value={filters.priority} onChange={(e) => setPriorityFilter(e.target.value as Priority | 'all')} style={{ padding: '6px 12px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', fontSize: 'var(--text-body-sm)', background: 'var(--color-bg-surface-default)' }}>
          <option value="all">All Priorities</option>
          {(Object.entries(PRIORITY_LABELS) as [Priority, string][]).map(([key, label]) => <option key={key} value={key}>{label}</option>)}
        </select>
        <select value={filters.course} onChange={(e) => setCourseFilter(e.target.value)} style={{ padding: '6px 12px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', fontSize: 'var(--text-body-sm)', background: 'var(--color-bg-surface-default)' }}>
          <option value="all">All Courses</option>
          <option value="course-1">Full Stack Web Development</option>
          <option value="course-2">Data Science & Analytics</option>
          <option value="course-3">Cloud Architecture</option>
          <option value="course-4">Mobile App Development</option>
          <option value="course-5">DevOps Engineering</option>
        </select>
        <select value={filters.batch} onChange={(e) => setBatchFilter(e.target.value)} style={{ padding: '6px 12px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', fontSize: 'var(--text-body-sm)', background: 'var(--color-bg-surface-default)' }}>
          <option value="all">All Batches</option>
          <option value="batch-1">Batch A</option>
          <option value="batch-2">Batch B</option>
          <option value="batch-3">Batch C</option>
          <option value="batch-4">Batch D</option>
        </select>
        <input type="date" value={filters.dateFrom} onChange={(e) => setDateFrom(e.target.value)} title="From date" style={{ padding: '5px 8px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', fontSize: 'var(--text-body-sm)', background: 'var(--color-bg-surface-default)', maxWidth: 140 }} />
        <input type="date" value={filters.dateTo} onChange={(e) => setDateTo(e.target.value)} title="To date" style={{ padding: '5px 8px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', fontSize: 'var(--text-body-sm)', background: 'var(--color-bg-surface-default)', maxWidth: 140 }} />
        {showSort && (
          <select value={`${filters.sortKey}${filters.sortDirection === 'asc' ? '|asc' : ''}`} onChange={(e) => { const [k, dir] = e.target.value.split('|'); setSort(k as any); if (dir === 'asc' && filters.sortDirection !== 'asc') setSort(filters.sortKey); }} style={{ padding: '6px 12px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', fontSize: 'var(--text-body-sm)', background: 'var(--color-bg-surface-default)' }}>
            {SORT_OPTIONS.map((opt) => <option key={opt.key} value={opt.key}>{opt.label}</option>)}
          </select>
        )}
      </div>
    </div>
  );
});
