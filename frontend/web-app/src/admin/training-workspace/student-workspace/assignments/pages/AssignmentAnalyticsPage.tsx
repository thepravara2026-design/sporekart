import { useAssignments } from '../state/AssignmentContext';
import { AnalyticsPanel } from '../components/AnalyticsPanel';

export function AssignmentAnalyticsPage() {
  const { analytics } = useAssignments();

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Assignment Analytics</h2>
      <AnalyticsPanel analytics={analytics} />
    </div>
  );
}
