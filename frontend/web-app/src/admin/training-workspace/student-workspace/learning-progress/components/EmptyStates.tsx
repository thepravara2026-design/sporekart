import { memo } from 'react';
import type { EmptyStateType } from '../types';

interface EmptyStateProps {
  type: EmptyStateType;
  onClearFilters?: () => void;
}

const config: Record<string, { icon: string; title: string; message: string }> = {
  noProgress: { icon: '📊', title: 'No Learning Progress', message: 'No progress records found matching your criteria.' },
  noMilestones: { icon: '🏁', title: 'No Milestones', message: 'No milestone achievements found.' },
  noCompetencies: { icon: '🎯', title: 'No Competencies', message: 'No competency records available.' },
  noLearningHours: { icon: '⏰', title: 'No Learning Hours', message: 'No learning hours data available.' },
  noTimeline: { icon: '📅', title: 'No Timeline', message: 'No timeline events available.' },
  noSearchResults: { icon: '🔍', title: 'No Results', message: 'Try adjusting your search or filters.' },
};

export const EmptyState = memo(function EmptyState({ type, onClearFilters }: EmptyStateProps) {
  const cfg = config[type];
  return (
    <div style={{
      display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center',
      padding: 'var(--space-8)', gap: 8, textAlign: 'center',
    }}>
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
