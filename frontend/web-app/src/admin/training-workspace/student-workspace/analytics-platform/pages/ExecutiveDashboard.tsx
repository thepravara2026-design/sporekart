import { useAnalytics } from '../state/AnalyticsContext';
import { SharedFilters } from '../components/SharedFilters';
import { DashboardWidget } from '../components/DashboardWidget';
import { KPITile } from '../components/KPITile';
import { MetricCard } from '../components/MetricCard';
import { LineChart } from '../components/LineChart';

export default function ExecutiveDashboard() {
  const { executiveDashboard, kpis } = useAnalytics();

  return (
    <main style={{ padding: 'var(--space-3)', display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Executive Dashboard</h1>
        <SharedFilters currentPage="analytics" />
      </div>

      {/* KPI Row */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))', gap: 'var(--space-component-gap)' }}>
        {kpis.map((kpi) => <KPITile key={kpi.label} data={kpi} />)}
      </div>

      {/* Summary Metrics */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(140px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <MetricCard label="Active Students" value={executiveDashboard.activeStudents.toLocaleString()} icon="👥" />
        <MetricCard label="Total Courses" value={executiveDashboard.totalCourses} icon="📚" />
        <MetricCard label="Active Trainers" value={executiveDashboard.activeTrainers} icon="✅" />
        <MetricCard label="Active Batches" value={executiveDashboard.activeBatches} icon="🗂️" />
        <MetricCard label="Course Completion" value={`${executiveDashboard.courseCompletionPercent}%`} color="#16a34a" />
        <MetricCard label="Avg Attendance" value={`${executiveDashboard.averageAttendance}%`} color="#2563eb" />
        <MetricCard label="Avg Assessment" value={`${executiveDashboard.averageAssessmentScore}%`} color="#8b5cf6" />
        <MetricCard label="Certification Rate" value={`${executiveDashboard.certificationRate}%`} color="#f59e0b" />
      </div>

      {/* Trends */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(360px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <DashboardWidget title="Enrollment Trend" subtitle="Monthly enrollment growth">
          <LineChart
            data={executiveDashboard.enrollmentTrend.map((t) => ({ label: t.month, value: t.count }))}
            height={120}
            color="#2563eb"
          />
        </DashboardWidget>
        <DashboardWidget title="Completion Trend" subtitle="Monthly completions">
          <LineChart
            data={executiveDashboard.completionTrend.map((t) => ({ label: t.month, value: t.count }))}
            height={120}
            color="#16a34a"
          />
        </DashboardWidget>
      </div>

      {/* Recent Activity */}
      <DashboardWidget title="Recent Activity" subtitle="Platform activity snapshot">
        <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
          {executiveDashboard.recentActivity.slice(0, 10).map((a, i) => (
            <div key={i} style={{ display: 'flex', justifyContent: 'space-between', padding: '6px 0', borderBottom: '1px solid var(--color-border-subtle)', fontSize: 'var(--text-body-sm)' }}>
              <span style={{ color: 'var(--color-text-secondary)' }}>{a.event}</span>
              <span style={{ color: 'var(--color-text-tertiary)' }}>{a.count} events</span>
            </div>
          ))}
        </div>
      </DashboardWidget>
    </main>
  );
}
