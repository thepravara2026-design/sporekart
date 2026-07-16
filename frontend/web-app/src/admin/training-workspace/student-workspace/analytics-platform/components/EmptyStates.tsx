import { memo } from 'react';
import type { EmptyStateType } from '../types';

interface EmptyStateProps {
  type: EmptyStateType;
  onClearFilters?: () => void;
}

const config: Record<string, { icon: string; title: string; message: string }> = {
  noAnalytics: { icon: '📊', title: 'No Analytics', message: 'No analytics data available for the selected criteria.' },
  noReports: { icon: '📋', title: 'No Reports', message: 'No reports found matching your criteria.' },
  noDashboardData: { icon: '📉', title: 'No Dashboard Data', message: 'Dashboard data is not available.' },
  noLearningMetrics: { icon: '📈', title: 'No Learning Metrics', message: 'No learning metrics data available.' },
  noStudentActivity: { icon: '👤', title: 'No Student Activity', message: 'No student activity records found.' },
  noSearchResults: { icon: '🔍', title: 'No Results', message: 'Try adjusting your search or filters.' },
};

export const EmptyState = memo(function EmptyState({ type, onClearFilters }: EmptyStateProps) {
  const cfg = config[type];
  return (
    <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', padding: 'var(--space-8)', gap: 8, textAlign: 'center' }}>
      <div style={{ fontSize: 40 }}>{cfg.icon}</div>
      <h3 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>{cfg.title}</h3>
      <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-tertiary)', margin: 0, maxWidth: 360 }}>{cfg.message}</p>
      {onClearFilters && (
        <button onClick={onClearFilters} style={{ marginTop: 8, padding: '6px 16px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)', cursor: 'pointer', fontSize: 'var(--text-body-sm)' }}>
          Clear Filters
        </button>
      )}
    </div>
  );
});
