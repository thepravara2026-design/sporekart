import { useAssessments } from '../state/AssessmentContext';
import { DashboardWidget } from '../components/DashboardWidget';
import { AssessmentDashboardSkeleton } from '../components/Skeletons';

export function AssessmentDashboardPage() {
  const { dashboardStats } = useAssessments();

  if (!dashboardStats) return <AssessmentDashboardSkeleton />;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Assessment Dashboard</h2>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <DashboardWidget label="Total Assessments" value={dashboardStats.totalAssessments} variant="default" subtitle="All time" />
        <DashboardWidget label="Upcoming" value={dashboardStats.upcomingAssessments} variant="info" subtitle="Scheduled & open" />
        <DashboardWidget label="Completed" value={dashboardStats.completedAssessments} variant="success" subtitle="Fully evaluated" />
        <DashboardWidget label="Average Score" value={`${dashboardStats.averageScore}%`} variant="info" subtitle="Across all results" />
        <DashboardWidget label="Highest Score" value={`${dashboardStats.highestScore}%`} variant="success" subtitle="Best performance" />
        <DashboardWidget label="Lowest Score" value={`${dashboardStats.lowestScore}%`} variant="danger" subtitle="Needs improvement" />
        <DashboardWidget label="Pass Rate" value={`${dashboardStats.passPercent}%`} variant="success" subtitle="Passed assessments" />
        <DashboardWidget label="Fail Rate" value={`${dashboardStats.failPercent}%`} variant="warning" subtitle="Failed assessments" />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 'var(--space-component-gap)' }}>
        <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
          <h3 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: '0 0 12px 0' }}>Course Performance</h3>
          {dashboardStats.coursePerformance.map((c) => (
            <div key={c.course} style={{ display: 'flex', justifyContent: 'space-between', padding: '4px 0', fontSize: 'var(--text-body-sm)', borderBottom: '1px solid var(--color-border-subtle)' }}>
              <span style={{ flex: 1 }}>{c.course}</span>
              <span style={{ fontWeight: 'var(--weight-medium)', color: c.avgScore >= 70 ? '#16a34a' : c.avgScore >= 50 ? '#ca8a04' : '#dc2626' }}>{c.avgScore}%</span>
            </div>
          ))}
        </div>
        <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
          <h3 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: '0 0 12px 0' }}>Batch Performance</h3>
          {dashboardStats.batchPerformance.map((b) => (
            <div key={b.batch} style={{ display: 'flex', justifyContent: 'space-between', padding: '4px 0', fontSize: 'var(--text-body-sm)', borderBottom: '1px solid var(--color-border-subtle)' }}>
              <span style={{ flex: 1 }}>{b.batch}</span>
              <span style={{ fontWeight: 'var(--weight-medium)', color: b.avgScore >= 70 ? '#16a34a' : b.avgScore >= 50 ? '#ca8a04' : '#dc2626' }}>{b.avgScore}%</span>
            </div>
          ))}
        </div>
      </div>

      {/* Recent Results */}
      <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
        <h3 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: '0 0 12px 0' }}>Recent Results</h3>
        {dashboardStats.recentResults.length === 0 ? (
          <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-tertiary)', padding: '12px 0' }}>No results available.</div>
        ) : (
          <div style={{ display: 'flex', flexDirection: 'column', gap: 4 }}>
            {dashboardStats.recentResults.map((r) => (
              <div key={r.id} style={{ display: 'flex', justifyContent: 'space-between', padding: '8px 0', fontSize: 'var(--text-body-sm)', borderBottom: '1px solid var(--color-border-subtle)' }}>
                <div>
                  <span style={{ fontWeight: 'var(--weight-medium)' }}>{r.studentName}</span>
                  <span style={{ color: 'var(--color-text-tertiary)', marginLeft: 8 }}>{r.assessmentTitle}</span>
                </div>
                <span style={{ fontWeight: 'var(--weight-medium)', color: r.resultStatus === 'pass' ? '#16a34a' : '#dc2626' }}>
                  {r.scoredMarks}/{r.maxMarks} ({r.percentage}%)
                </span>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
