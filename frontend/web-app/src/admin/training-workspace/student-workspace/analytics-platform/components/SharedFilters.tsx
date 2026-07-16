import { memo, useId } from 'react';
import { useAnalytics } from '../state/AnalyticsContext';
import { ANALYTICS_NAV_ITEMS } from '../types';

export const SharedFilters = memo(function SharedFilters({ currentPage }: { currentPage?: string }) {
  const { setSearchTerm, setCourseFilter, setBatchFilter, setPerformanceFilter, searchTerm } = useAnalytics();
  const searchId = useId();

  const navItem = ANALYTICS_NAV_ITEMS.find((n) => n.id === currentPage);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
      {navItem && <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{navItem.description}</div>}
      <div style={{ display: 'flex', flexWrap: 'wrap', gap: 8, alignItems: 'center' }}>
        <div style={{ flex: 1, minWidth: 200, maxWidth: 320 }}>
          <label htmlFor={searchId} style={{ position: 'absolute', width: 1, height: 1, overflow: 'hidden', clip: 'rect(0 0 0 0)' }}>Search analytics</label>
          <input id={searchId} type="search" value={searchTerm} onChange={(e) => setSearchTerm(e.target.value)} placeholder="Search analytics..." style={{ width: '100%', padding: '6px 12px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', fontSize: 'var(--text-body-sm)', background: 'var(--color-bg-surface-default)' }} />
        </div>
        <select onChange={(e) => setCourseFilter(e.target.value)} style={{ padding: '6px 12px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', fontSize: 'var(--text-body-sm)', background: 'var(--color-bg-surface-default)' }}>
          <option value="all">All Courses</option>
          <option value="course-001">Data Science</option>
          <option value="course-002">Machine Learning</option>
          <option value="course-003">Python</option>
        </select>
        <select onChange={(e) => setBatchFilter(e.target.value)} style={{ padding: '6px 12px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', fontSize: 'var(--text-body-sm)', background: 'var(--color-bg-surface-default)' }}>
          <option value="all">All Batches</option>
          <option value="batch-001">Batch A</option>
          <option value="batch-002">Batch B</option>
        </select>
        <select onChange={(e) => setPerformanceFilter(e.target.value)} style={{ padding: '6px 12px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', fontSize: 'var(--text-body-sm)', background: 'var(--color-bg-surface-default)' }}>
          <option value="all">All Performance</option>
          <option value="excellent">Excellent</option>
          <option value="good">Good</option>
          <option value="average">Average</option>
          <option value="needs-attention">Needs Attention</option>
          <option value="critical">Critical</option>
        </select>
      </div>
    </div>
  );
});
