import { useState, useMemo } from 'react';
import { useProgress } from '../state/LearningProgressContext';
import { LearningTimeline } from '../components/LearningTimeline';
import { EmptyState } from '../components/EmptyStates';
import { StudentSearchBar } from '../../components/StudentSearchFilter';

export function LearningTimelinePage() {
  const { timelineEvents } = useProgress();
  const [searchTerm, setSearchTerm] = useState('');
  const [completedFilter, setCompletedFilter] = useState<'all' | 'completed' | 'pending'>('all');

  const filtered = useMemo(() => {
    let result = timelineEvents;
    if (completedFilter === 'completed') result = result.filter((e) => e.completed);
    if (completedFilter === 'pending') result = result.filter((e) => !e.completed);
    if (searchTerm) {
      const term = searchTerm.toLowerCase();
      result = result.filter((e) => e.label.toLowerCase().includes(term) || e.description.toLowerCase().includes(term) || e.studentId.toLowerCase().includes(term));
    }
    return result;
  }, [timelineEvents, searchTerm, completedFilter]);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Learning Timeline</h2>
      <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-tertiary)', margin: 0 }}>Academic journey timeline across all students</p>

      <div style={{ display: 'flex', gap: 'var(--space-component-gap)', flexWrap: 'wrap', alignItems: 'center' }}>
        <div style={{ flex: 1, minWidth: 200 }}>
          <StudentSearchBar searchQuery={searchTerm} onSearchChange={setSearchTerm} />
        </div>
        <select
          aria-label="Filter by status"
          value={completedFilter}
          onChange={(e) => setCompletedFilter(e.target.value as 'all' | 'completed' | 'pending')}
          style={{
            padding: '6px 12px', borderRadius: 'var(--radius-sm)',
            border: '1px solid var(--color-border-default)',
            fontSize: 'var(--text-body-sm)', background: 'var(--color-bg-surface-default)',
          }}
        >
          <option value="all">All Events</option>
          <option value="completed">Completed</option>
          <option value="pending">Pending</option>
        </select>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 'var(--space-section-gap)' }}>
        <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
          <h3 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: '0 0 12px 0' }}>Timeline</h3>
          {filtered.length === 0 ? (
            <EmptyState type={searchTerm ? 'noSearchResults' : 'noTimeline'} />
          ) : (
            <LearningTimeline events={filtered} />
          )}
        </div>
        <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
          <h3 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: '0 0 12px 0' }}>Summary</h3>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 4 }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', padding: '4px 0', fontSize: 'var(--text-body-sm)', borderBottom: '1px solid var(--color-border-subtle)' }}>
              <span>Total Events</span>
              <span style={{ fontWeight: 'var(--weight-medium)' }}>{timelineEvents.length}</span>
            </div>
            <div style={{ display: 'flex', justifyContent: 'space-between', padding: '4px 0', fontSize: 'var(--text-body-sm)', borderBottom: '1px solid var(--color-border-subtle)' }}>
              <span>Completed</span>
              <span style={{ fontWeight: 'var(--weight-medium)', color: '#16a34a' }}>{timelineEvents.filter((e) => e.completed).length}</span>
            </div>
            <div style={{ display: 'flex', justifyContent: 'space-between', padding: '4px 0', fontSize: 'var(--text-body-sm)', borderBottom: '1px solid var(--color-border-subtle)' }}>
              <span>Pending</span>
              <span style={{ fontWeight: 'var(--weight-medium)', color: '#ca8a04' }}>{timelineEvents.filter((e) => !e.completed).length}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
