import { useProgress } from '../state/LearningProgressContext';
import { AnalyticsPanel } from '../components/AnalyticsPanel';
import { ProgressDashboardSkeleton } from '../components/Skeletons';

export function AnalyticsPage() {
  const { analytics } = useProgress();

  if (!analytics) return <ProgressDashboardSkeleton />;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Learning Analytics</h2>
      <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-tertiary)', margin: 0 }}>
        Detailed analytics on learning progress, completion rates, and student engagement
      </p>
      <AnalyticsPanel analytics={analytics} />
    </div>
  );
}
