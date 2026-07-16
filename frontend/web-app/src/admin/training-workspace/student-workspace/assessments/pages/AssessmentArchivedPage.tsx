import { useMemo } from 'react';
import { useAssessments } from '../state/AssessmentContext';
import { AssessmentCard } from '../components/AssessmentCard';
import { EmptyState } from '../components/EmptyStates';

export function AssessmentArchivedPage() {
  const { assessments } = useAssessments();

  const archived = useMemo(() => assessments.filter((a) => a.status === 'archived'), [assessments]);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Archived Assessments</h2>

      {archived.length === 0 ? (
        <EmptyState type="noAssessments" />
      ) : (
        <>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
            {archived.length} archived assessment(s)
          </div>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: 'var(--space-component-gap)' }}>
            {archived.map((a) => (
              <AssessmentCard key={a.id} assessment={a} />
            ))}
          </div>
        </>
      )}
    </div>
  );
}
