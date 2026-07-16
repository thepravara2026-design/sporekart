import { useAnalytics } from '../state/AnalyticsContext';
import { SharedFilters } from '../components/SharedFilters';
import { DashboardWidget } from '../components/DashboardWidget';
import { MetricCard } from '../components/MetricCard';
import { BarChart } from '../components/BarChart';
import { ProgressRing } from '../components/ProgressRing';

export default function BatchAnalytics() {
  const { batchMetrics } = useAnalytics();

  return (
    <main style={{ padding: 'var(--space-3)', display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Batch Analytics</h1>
        <SharedFilters currentPage="analytics/batches" />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(220px, 1fr))', gap: 'var(--space-component-gap)' }}>
        {batchMetrics.map((b) => (
          <div key={b.batchId} style={{ padding: 'var(--space-3)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)', display: 'flex', flexDirection: 'column', gap: 6 }}>
            <h3 style={{ fontSize: 'var(--text-h5)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>{b.batchName}</h3>
            <div style={{ display: 'flex', gap: 8, flexWrap: 'wrap' }}>
              <MetricCard label="Total" value={b.totalStudents} icon="👥" />
              <MetricCard label="Active" value={b.activeStudents} icon="✅" />
              <MetricCard label="At Risk" value={b.atRiskCount} color="#dc2626" />
            </div>
            <ProgressRing value={b.completionPercent} size={56} color="#2563eb" label="Completion" />
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 4, fontSize: 'var(--text-caption)' }}>
              <span>Attendance: {b.averageAttendance}%</span>
              <span>Assignment: {b.averageAssignmentScore}%</span>
              <span>Assessment: {b.averageAssessmentScore}%</span>
              <span>Certification: {b.certificationRate}%</span>
            </div>
          </div>
        ))}
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(360px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <DashboardWidget title="Batch Comparison" subtitle="Completion rates across batches">
          <BarChart
            data={batchMetrics.map((b) => ({ label: b.batchName, value: b.completionPercent }))}
            height={100}
            color="#8b5cf6"
          />
        </DashboardWidget>
        <DashboardWidget title="At-Risk Students" subtitle="Students needing attention">
          <BarChart
            data={batchMetrics.map((b) => ({ label: b.batchName, value: b.atRiskCount }))}
            height={100}
            color="#dc2626"
          />
        </DashboardWidget>
      </div>
    </main>
  );
}
