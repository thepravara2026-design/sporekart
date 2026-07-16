import { memo } from 'react';

interface EmptyStateProps {
  type: 'noApplications' | 'noPendingApprovals' | 'noApprovedStudents' | 'noActiveBatches' | 'noCapacity' | 'noSearchResults' | 'noArchived' | 'noTimeline';
  onClearFilters?: () => void;
  title?: string;
  message?: string;
}

const config: Record<string, { icon: string; title: string; message: string }> = {
  noApplications: { icon: '📋', title: 'No Applications', message: 'No enrollment applications found matching your criteria.' },
  noPendingApprovals: { icon: '⏳', title: 'No Pending Approvals', message: 'All applications have been reviewed.' },
  noApprovedStudents: { icon: '✅', title: 'No Approved Students', message: 'No approved enrollments at this time.' },
  noActiveBatches: { icon: '📦', title: 'No Active Batches', message: 'No active batches found.' },
  noCapacity: { icon: '📊', title: 'No Capacity Data', message: 'No capacity information available.' },
  noSearchResults: { icon: '🔍', title: 'No Results', message: 'Try adjusting your search or filters.' },
  noArchived: { icon: '📁', title: 'No Archived Enrollments', message: 'No archived enrollment records.' },
  noTimeline: { icon: '🕐', title: 'No Timeline Events', message: 'No activity recorded for this enrollment.' },
};

export const EmptyState = memo(function EmptyState({ type, onClearFilters }: EmptyStateProps) {
  const cfg = config[type];
  return (
    <div className="enrollment-empty-state" style={{
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
