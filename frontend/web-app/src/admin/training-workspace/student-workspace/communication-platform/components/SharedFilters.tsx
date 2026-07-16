import { memo, useId } from 'react';
import { useCommunication } from '../state/CommunicationContext';
import { COMMUNICATION_TYPE_LABELS, PRIORITY_LABELS, COMMUNICATION_NAV_ITEMS } from '../types';
import type { CommunicationType, Priority } from '../types';

export const SharedFilters = memo(function SharedFilters({ currentPage }: { currentPage?: string }) {
  const { filters, setSearch, setTypeFilter, setPriorityFilter, setCourseFilter, setBatchFilter } = useCommunication();
  const searchId = useId();
  const navItem = COMMUNICATION_NAV_ITEMS.find((n) => n.id === currentPage);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
      {navItem && <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{navItem.description}</div>}
      <div style={{ display: 'flex', flexWrap: 'wrap', gap: 8, alignItems: 'center' }}>
        <div style={{ flex: 1, minWidth: 180, maxWidth: 280 }}>
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
      </div>
    </div>
  );
});
