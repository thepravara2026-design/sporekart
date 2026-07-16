import { useAnalytics } from '../state/AnalyticsContext';
import { SharedFilters } from '../components/SharedFilters';
import { DashboardWidget } from '../components/DashboardWidget';
import { MetricCard } from '../components/MetricCard';
import { BarChart } from '../components/BarChart';
import { ProgressRing } from '../components/ProgressRing';

export default function TrainerAnalytics() {
  const { trainerMetrics } = useAnalytics();

  return (
    <main style={{ padding: 'var(--space-3)', display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Trainer Analytics</h1>
        <SharedFilters currentPage="analytics/trainers" />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(220px, 1fr))', gap: 'var(--space-component-gap)' }}>
        {trainerMetrics.map((t) => (
          <div key={t.trainerId} style={{ padding: 'var(--space-3)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)', display: 'flex', flexDirection: 'column', gap: 6 }}>
            <h3 style={{ fontSize: 'var(--text-h5)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>{t.trainerName}</h3>
            <div style={{ display: 'flex', gap: 8, flexWrap: 'wrap' }}>
              <MetricCard label="Students" value={t.totalStudents} icon="👥" />
              <MetricCard label="Courses" value={t.coursesDelivered} icon="📚" />
            </div>
            <ProgressRing value={t.studentSuccessRate} size={56} color="#2563eb" label="Success Rate" />
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 4, fontSize: 'var(--text-caption)' }}>
              <span>Attendance: {t.averageAttendance}%</span>
              <span>Assessment: {t.averageAssessmentScore}%</span>
              <span>Completion: {t.completionPercent}%</span>
              <span>Certification: {t.certificationRate}%</span>
            </div>
          </div>
        ))}
      </div>

      <DashboardWidget title="Trainer Comparison" subtitle="Success rates and completion">
        <BarChart
          data={trainerMetrics.map((t) => ({ label: t.trainerName.split(' ')[0], value: t.studentSuccessRate }))}
          height={100}
          color="#2563eb"
        />
      </DashboardWidget>
    </main>
  );
}
