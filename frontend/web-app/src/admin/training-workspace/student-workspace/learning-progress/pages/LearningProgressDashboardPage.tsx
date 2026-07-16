import { useProgress } from '../state/LearningProgressContext';
import { DashboardWidget } from '../components/DashboardWidget';
import { ProgressDashboardSkeleton } from '../components/Skeletons';

export function LearningProgressDashboardPage() {
  const { healthDashboard } = useProgress();

  if (!healthDashboard) return <ProgressDashboardSkeleton />;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Learning Dashboard</h2>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <DashboardWidget label="Total Students" value={healthDashboard.totalStudents} variant="default" subtitle="All time" />
        <DashboardWidget label="On Track" value={healthDashboard.studentsOnTrack} variant="success" subtitle={`${Math.round((healthDashboard.studentsOnTrack / healthDashboard.totalStudents) * 100)}% of total`} />
        <DashboardWidget label="Behind Schedule" value={healthDashboard.studentsBehind} variant="danger" subtitle="Needs intervention" />
        <DashboardWidget label="Average Progress" value={`${healthDashboard.averageProgress}%`} variant="info" subtitle="Across all courses" />
        <DashboardWidget label="Completion Rate" value={`${healthDashboard.completionPercent}%`} variant="success" subtitle="Course completion" />
        <DashboardWidget label="Attendance Health" value={`${healthDashboard.attendanceHealth}%`} variant={healthDashboard.attendanceHealth >= 75 ? 'success' : 'warning'} subtitle="Overall attendance" />
        <DashboardWidget label="Assignment Completion" value={`${healthDashboard.assignmentCompletion}%`} variant="info" subtitle="Submitted" />
        <DashboardWidget label="Assessment Completion" value={`${healthDashboard.assessmentCompletion}%`} variant={healthDashboard.assessmentCompletion >= 70 ? 'success' : 'warning'} subtitle="Evaluated" />
        <DashboardWidget label="Competency Coverage" value={`${healthDashboard.competencyCoverage}%`} variant="info" subtitle="Skills covered" />
        <DashboardWidget label="Total Learning Hours" value={healthDashboard.totalLearningHours} variant="default" subtitle="Cumulative" />
        <DashboardWidget label="At Risk" value={healthDashboard.atRiskCount} variant="danger" subtitle="Behind schedule" />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 'var(--space-component-gap)' }}>
        <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
          <h3 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: '0 0 12px 0' }}>Student Status Overview</h3>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 4 }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', padding: '4px 0', fontSize: 'var(--text-body-sm)', borderBottom: '1px solid var(--color-border-subtle)' }}>
              <span>On Track</span>
              <span style={{ fontWeight: 'var(--weight-medium)', color: '#16a34a' }}>{healthDashboard.studentsOnTrack}</span>
            </div>
            <div style={{ display: 'flex', justifyContent: 'space-between', padding: '4px 0', fontSize: 'var(--text-body-sm)', borderBottom: '1px solid var(--color-border-subtle)' }}>
              <span>Behind Schedule</span>
              <span style={{ fontWeight: 'var(--weight-medium)', color: '#dc2626' }}>{healthDashboard.studentsBehind}</span>
            </div>
            <div style={{ display: 'flex', justifyContent: 'space-between', padding: '4px 0', fontSize: 'var(--text-body-sm)', borderBottom: '1px solid var(--color-border-subtle)' }}>
              <span>At Risk</span>
              <span style={{ fontWeight: 'var(--weight-medium)', color: '#dc2626' }}>{healthDashboard.atRiskCount}</span>
            </div>
          </div>
        </div>
        <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
          <h3 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: '0 0 12px 0' }}>Health Metrics</h3>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 4 }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', padding: '4px 0', fontSize: 'var(--text-body-sm)', borderBottom: '1px solid var(--color-border-subtle)' }}>
              <span>Attendance Health</span>
              <span style={{ fontWeight: 'var(--weight-medium)' }}>{healthDashboard.attendanceHealth}%</span>
            </div>
            <div style={{ display: 'flex', justifyContent: 'space-between', padding: '4px 0', fontSize: 'var(--text-body-sm)', borderBottom: '1px solid var(--color-border-subtle)' }}>
              <span>Assignment Completion</span>
              <span style={{ fontWeight: 'var(--weight-medium)' }}>{healthDashboard.assignmentCompletion}%</span>
            </div>
            <div style={{ display: 'flex', justifyContent: 'space-between', padding: '4px 0', fontSize: 'var(--text-body-sm)', borderBottom: '1px solid var(--color-border-subtle)' }}>
              <span>Assessment Completion</span>
              <span style={{ fontWeight: 'var(--weight-medium)' }}>{healthDashboard.assessmentCompletion}%</span>
            </div>
            <div style={{ display: 'flex', justifyContent: 'space-between', padding: '4px 0', fontSize: 'var(--text-body-sm)', borderBottom: '1px solid var(--color-border-subtle)' }}>
              <span>Competency Coverage</span>
              <span style={{ fontWeight: 'var(--weight-medium)' }}>{healthDashboard.competencyCoverage}%</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
