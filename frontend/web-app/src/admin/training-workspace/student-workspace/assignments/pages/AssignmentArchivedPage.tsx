import { useMemo } from 'react';
import { useAssignments } from '../state/AssignmentContext';
import { AssignmentCard } from '../components/AssignmentCard';
import { EmptyState } from '../components/EmptyStates';

export function AssignmentArchivedPage() {
  const { assignments } = useAssignments();

  const archived = useMemo(() => assignments.filter((a) => a.status === 'archived'), [assignments]);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Archived Assignments</h2>

      {archived.length === 0 ? (
        <EmptyState type="noAssignments" />
      ) : (
        <>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
            {archived.length} archived assignment(s)
          </div>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: 'var(--space-component-gap)' }}>
            {archived.map((a) => (
              <AssignmentCard key={a.id} assignment={a} />
            ))}
          </div>
        </>
      )}
    </div>
  );
}
