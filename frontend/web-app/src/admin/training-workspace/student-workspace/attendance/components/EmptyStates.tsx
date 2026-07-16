import { memo } from 'react';

interface EmptyStateProps {
  type: 'noAttendance' | 'noSessions' | 'noBatch' | 'noStudents' | 'noSearchResults' | 'noAttendanceToday' | 'noPolicies';
  onClearFilters?: () => void;
}

const config: Record<string, { icon: string; title: string; message: string }> = {
  noAttendance: { icon: '📋', title: 'No Attendance Records', message: 'No attendance records found matching your criteria.' },
  noSessions: { icon: '📅', title: 'No Sessions', message: 'No training sessions scheduled for this period.' },
  noBatch: { icon: '📦', title: 'No Batch Data', message: 'No batch attendance data available.' },
  noStudents: { icon: '👤', title: 'No Students', message: 'No students assigned to this batch.' },
  noSearchResults: { icon: '🔍', title: 'No Results', message: 'Try adjusting your search or filters.' },
  noAttendanceToday: { icon: '📭', title: 'No Attendance Today', message: 'No sessions scheduled for today.' },
  noPolicies: { icon: '🛡️', title: 'No Policies', message: 'No attendance policies configured.' },
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
