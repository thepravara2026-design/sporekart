import { useState, useMemo } from 'react';
import { useAssignments } from '../state/AssignmentContext';
import { AssignmentTimeline } from '../components/AssignmentTimeline';
import { EmptyState } from '../components/EmptyStates';

export function AssignmentTimelinePage() {
  const { timelineEvents, assignments } = useAssignments();
  const [selectedAssignmentId, setSelectedAssignmentId] = useState<string | null>(null);

  const filtered = useMemo(() => {
    if (!selectedAssignmentId) return timelineEvents;
    return timelineEvents.filter((e) => e.assignmentId === selectedAssignmentId);
  }, [timelineEvents, selectedAssignmentId]);

  const upcomingAssignments = assignments.filter((a) => a.status !== 'archived' && a.status !== 'cancelled');

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Assignment Timeline</h2>

      <div style={{ display: 'flex', gap: 'var(--space-component-gap)', flexWrap: 'wrap', alignItems: 'center' }}>
        <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-tertiary)' }}>Filter by assignment:</span>
        <select
          aria-label="Select assignment"
          value={selectedAssignmentId || ''}
          onChange={(e) => setSelectedAssignmentId(e.target.value || null)}
          style={{
            padding: '6px 12px', borderRadius: 'var(--radius-sm)',
            border: '1px solid var(--color-border-default)',
            fontSize: 'var(--text-body-sm)', background: 'var(--color-bg-surface-default)', minWidth: 250,
          }}
        >
          <option value="">All Assignments</option>
          {upcomingAssignments.map((a) => (
            <option key={a.id} value={a.id}>
              {a.assignmentCode} - {a.title}
            </option>
          ))}
        </select>
      </div>

      {filtered.length === 0 ? (
        <EmptyState type="noTimelineEvents" />
      ) : (
        <div style={{
          padding: 'var(--space-4)', background: 'var(--color-bg-surface-default)',
          borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)',
        }}>
          <AssignmentTimeline events={filtered} />
        </div>
      )}
    </div>
  );
}
