import { useAnalytics } from '../state/AnalyticsContext';
import { SharedFilters } from '../components/SharedFilters';
import { DashboardWidget } from '../components/DashboardWidget';
import { MetricCard } from '../components/MetricCard';
import { BarChart } from '../components/BarChart';
import { LineChart } from '../components/LineChart';
import { DataTable } from '../components/DataTable';
import { PERFORMANCE_LABELS } from '../types';

export default function AcademicIntelligence() {
  const { insights } = useAnalytics();

  const studentColumns = [
    { key: 'studentName', label: 'Student' },
    { key: 'courseName', label: 'Course' },
    { key: 'attendanceScore', label: 'Attendance', render: (v: unknown) => `${String(v)}%` },
    { key: 'assignmentScore', label: 'Assignment', render: (v: unknown) => `${String(v)}%` },
    { key: 'assessmentScore', label: 'Assessment', render: (v: unknown) => `${String(v)}%` },
    { key: 'learningProgress', label: 'Progress', render: (v: unknown) => `${String(v)}%` },
    { key: 'performanceLevel', label: 'Level', render: (v: unknown) => PERFORMANCE_LABELS[v as keyof typeof PERFORMANCE_LABELS] || String(v) },
  ];

  return (
    <main style={{ padding: 'var(--space-3)', display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Academic Intelligence</h1>
        <SharedFilters currentPage="analytics/academic" />
      </div>

      {/* Summary */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <MetricCard label="Top Students" value={insights.topStudents.length} icon="🏆" color="#16a34a" />
        <MetricCard label="High Performers" value={insights.highPerformers.length} icon="⭐" color="#2563eb" />
        <MetricCard label="Needs Attention" value={insights.lowPerformers.length} icon="⚠️" color="#ca8a04" />
        <MetricCard label="Attendance Risks" value={insights.attendanceRisks.length} icon="🚨" color="#dc2626" />
        <MetricCard label="Completion Risks" value={insights.completionRisks.length} icon="⚠️" color="#f59e0b" />
      </div>

      {/* Top & Low Performers */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(360px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <DashboardWidget title="Top Performers" subtitle="Students exceeding expectations">
          <DataTable columns={studentColumns} data={insights.highPerformers.slice(0, 5) as unknown as Record<string, unknown>[]} />
        </DashboardWidget>
        <DashboardWidget title="Needs Attention" subtitle="Students requiring intervention">
          <DataTable columns={studentColumns} data={insights.lowPerformers.slice(0, 5) as unknown as Record<string, unknown>[]} />
        </DashboardWidget>
      </div>

      {/* Trends & Learning Gaps */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(360px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <DashboardWidget title="Achievement Trends" subtitle="Monthly achievement completions">
          <LineChart
            data={insights.achievementTrends.map((t) => ({ label: t.month, value: t.count }))}
            height={100}
            color="#16a34a"
          />
        </DashboardWidget>
        <DashboardWidget title="Learning Gaps" subtitle="Skills needing improvement">
          <BarChart
            data={insights.learningGaps.map((g) => ({ label: g.skill, value: g.studentsAffected }))}
            height={120}
            color="#dc2626"
          />
        </DashboardWidget>
      </div>

      {/* Risk Tables */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(360px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <DashboardWidget title="Attendance Risks" subtitle="Students with low attendance">
          <DataTable columns={studentColumns.slice(0, 4)} data={insights.attendanceRisks.slice(0, 5) as unknown as Record<string, unknown>[]} />
        </DashboardWidget>
        <DashboardWidget title="Completion Risks" subtitle="Students falling behind">
          <DataTable columns={studentColumns.slice(0, 4)} data={insights.completionRisks.slice(0, 5) as unknown as Record<string, unknown>[]} />
        </DashboardWidget>
      </div>
    </main>
  );
}
