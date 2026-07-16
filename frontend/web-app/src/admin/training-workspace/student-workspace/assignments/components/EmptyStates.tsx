import { memo } from 'react';
import type { EmptyStateType } from '../types';

interface EmptyStateProps {
  type: EmptyStateType;
  onClearFilters?: () => void;
}

const config: Record<string, { icon: string; title: string; message: string }> = {
  noAssignments: { icon: '📋', title: 'No Assignments', message: 'No assignments found matching your criteria.' },
  noProjects: { icon: '📁', title: 'No Projects', message: 'No projects found matching your criteria.' },
  noSubmissions: { icon: '📭', title: 'No Submissions', message: 'No pending submissions found.' },
  noDrafts: { icon: '📝', title: 'No Drafts', message: 'No draft assignments available.' },
  noActiveAssignments: { icon: '📄', title: 'No Active Assignments', message: 'No active assignments found.' },
  noSearchResults: { icon: '🔍', title: 'No Results', message: 'Try adjusting your search or filters.' },
  noEvaluations: { icon: '✅', title: 'No Pending Evaluations', message: 'All submissions have been evaluated.' },
  noTimelineEvents: { icon: '⏰', title: 'No Timeline Events', message: 'No timeline events available.' },
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
        <button
          onClick={onClearFilters}
          style={{
            marginTop: 8, padding: '6px 16px', borderRadius: 'var(--radius-sm)',
            border: '1px solid var(--color-border-default)',
            background: 'var(--color-bg-surface-default)', cursor: 'pointer',
            fontSize: 'var(--text-body-sm)',
          }}
        >
          Clear Filters
        </button>
      )}
    </div>
  );
});
