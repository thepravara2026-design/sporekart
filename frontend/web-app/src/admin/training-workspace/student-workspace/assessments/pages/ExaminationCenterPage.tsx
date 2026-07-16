import { useMemo } from 'react';
import { useAssessments } from '../state/AssessmentContext';
import { AssessmentCard } from '../components/AssessmentCard';
import { EmptyState } from '../components/EmptyStates';

export function ExaminationCenterPage() {
  const { assessments } = useAssessments();

  const activeExams = useMemo(() => assessments.filter((a) => a.status === 'active' || a.status === 'open'), [assessments]);
  const scheduledExams = useMemo(() => assessments.filter((a) => a.status === 'scheduled' || a.status === 'published'), [assessments]);
  const completedExams = useMemo(() => assessments.filter((a) => a.status === 'completed' || a.status === 'evaluated'), [assessments]);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Examination Center</h2>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <div style={{ padding: 'var(--space-3)', background: '#f0fdf4', borderRadius: 'var(--radius-md)', border: '1px solid #16a34a' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Active Exams</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: '#16a34a' }}>{activeExams.length}</div>
        </div>
        <div style={{ padding: 'var(--space-3)', background: '#eff6ff', borderRadius: 'var(--radius-md)', border: '1px solid #2563eb' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Scheduled</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: '#2563eb' }}>{scheduledExams.length}</div>
        </div>
        <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Completed</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)' }}>{completedExams.length}</div>
        </div>
      </div>

      {activeExams.length > 0 && (
        <div>
          <h3 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: '0 0 12px 0' }}>Active Examinations ({activeExams.length})</h3>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: 'var(--space-component-gap)' }}>
            {activeExams.map((a) => <AssessmentCard key={a.id} assessment={a} />)}
          </div>
        </div>
      )}

      {scheduledExams.length > 0 && (
        <div>
          <h3 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: '0 0 12px 0' }}>Upcoming Examinations ({scheduledExams.length})</h3>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: 'var(--space-component-gap)' }}>
            {scheduledExams.map((a) => <AssessmentCard key={a.id} assessment={a} />)}
          </div>
        </div>
      )}

      {activeExams.length === 0 && scheduledExams.length === 0 && (
        <EmptyState type="noScheduledExams" />
      )}

      {completedExams.length > 0 && (
        <div>
          <h3 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: '0 0 12px 0' }}>Completed Examinations ({completedExams.length})</h3>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: 'var(--space-component-gap)' }}>
            {completedExams.map((a) => <AssessmentCard key={a.id} assessment={a} />)}
          </div>
        </div>
      )}
    </div>
  );
}
