import { useAssignments } from '../state/AssignmentContext';
import { EvaluationCard } from '../components/EvaluationCard';
import { EmptyState } from '../components/EmptyStates';

export function AssignmentEvaluationsPage() {
  const { evaluations } = useAssignments();

  const pendingEvals = evaluations.filter((e) => e.evaluationStatus === 'pending' || e.evaluationStatus === 'in-progress');
  const completedEvals = evaluations.filter((e) => e.evaluationStatus === 'completed');

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Evaluation Queue</h2>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Total Evaluations</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)' }}>{evaluations.length}</div>
        </div>
        <div style={{ padding: 'var(--space-3)', background: '#fefce8', borderRadius: 'var(--radius-md)', border: '1px solid #ca8a04' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Pending</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: '#ca8a04' }}>{pendingEvals.length}</div>
        </div>
        <div style={{ padding: 'var(--space-3)', background: '#f0fdf4', borderRadius: 'var(--radius-md)', border: '1px solid #16a34a' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Completed</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: '#16a34a' }}>{completedEvals.length}</div>
        </div>
      </div>

      {pendingEvals.length > 0 && (
        <div>
          <h3 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: '0 0 12px 0' }}>Pending Evaluations ({pendingEvals.length})</h3>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: 'var(--space-component-gap)' }}>
            {pendingEvals.map((e) => (
              <EvaluationCard key={e.id} evaluation={e} />
            ))}
          </div>
        </div>
      )}

      {completedEvals.length > 0 && (
        <div>
          <h3 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: '0 0 12px 0' }}>Completed Evaluations ({completedEvals.length})</h3>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: 'var(--space-component-gap)' }}>
            {completedEvals.map((e) => (
              <EvaluationCard key={e.id} evaluation={e} />
            ))}
          </div>
        </div>
      )}

      {evaluations.length === 0 && <EmptyState type="noEvaluations" />}
    </div>
  );
}
