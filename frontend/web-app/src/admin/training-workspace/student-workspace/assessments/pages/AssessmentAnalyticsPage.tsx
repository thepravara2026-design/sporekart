import { useAssessments } from '../state/AssessmentContext';
import { AnalyticsPanel } from '../components/AnalyticsPanel';

export function AssessmentAnalyticsPage() {
  const { analytics } = useAssessments();

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Academic Analytics</h2>
      <AnalyticsPanel analytics={analytics} />
    </div>
  );
}
