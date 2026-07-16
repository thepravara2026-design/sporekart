import { useAnalytics } from '../state/AnalyticsContext';
import { SharedFilters } from '../components/SharedFilters';
import { DashboardWidget } from '../components/DashboardWidget';
import { BarChart } from '../components/BarChart';
import { LineChart } from '../components/LineChart';
import { ProgressRing } from '../components/ProgressRing';

export default function CourseAnalytics() {
  const { courseMetrics } = useAnalytics();

  return (
    <main style={{ padding: 'var(--space-3)', display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Course Analytics</h1>
        <SharedFilters currentPage="analytics/courses" />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(220px, 1fr))', gap: 'var(--space-component-gap)' }}>
        {courseMetrics.map((c) => (
          <div key={c.courseId} style={{ padding: 'var(--space-3)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)', display: 'flex', flexDirection: 'column', gap: 6 }}>
            <h3 style={{ fontSize: 'var(--text-h5)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>{c.courseName}</h3>
            <div style={{ display: 'flex', gap: 12, flexWrap: 'wrap', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>
              <span>📋 {c.enrollmentCount} enrolled</span>
              <span>✅ {c.completionCount} completed</span>
            </div>
            <ProgressRing value={c.completionPercent} size={56} color="#2563eb" />
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 4, fontSize: 'var(--text-caption)' }}>
              <span>Attendance: {c.averageAttendance}%</span>
              <span>Assignment: {c.averageAssignmentScore}%</span>
              <span>Assessment: {c.averageAssessmentScore}%</span>
              <span>Certification: {c.certificationPercent}%</span>
            </div>
          </div>
        ))}
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(360px, 1fr))', gap: 'var(--space-component-gap)' }}>
        {courseMetrics.slice(0, 4).map((c) => (
          <DashboardWidget key={c.courseId} title={c.courseName} subtitle="Enrollment vs Completions">
              <LineChart
              data={c.enrollmentTrend.map((t) => ({ label: t.month, value: t.count }))}
              height={100}
              color="#2563eb"
            />
          </DashboardWidget>
        ))}
      </div>

      <DashboardWidget title="Assessment Results Distribution">
        {courseMetrics.slice(0, 3).map((c) => (
          <div key={c.courseId} style={{ marginBottom: 12 }}>
            <h4 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: '0 0 8px' }}>{c.courseName}</h4>
            <BarChart
              data={c.assessmentResults.map((r) => ({ label: r.range, value: r.count }))}
              height={80}
              color="#8b5cf6"
            />
          </div>
        ))}
      </DashboardWidget>
    </main>
  );
}
