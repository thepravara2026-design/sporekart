import { useAnalytics } from '../state/AnalyticsContext';
import { SharedFilters } from '../components/SharedFilters';
import { DashboardWidget } from '../components/DashboardWidget';
import { MetricCard } from '../components/MetricCard';
import { ProgressRing } from '../components/ProgressRing';
import { DataTable } from '../components/DataTable';
import { EmptyState } from '../components/EmptyStates';
import { PERFORMANCE_LABELS } from '../types';
import { useMemo } from 'react';

export default function StudentAnalytics() {
  const { studentAnalytics, getFilteredStudents, setSearchTerm, setCourseFilter, setBatchFilter, setPerformanceFilter } = useAnalytics();

  const filtered = useMemo(() => getFilteredStudents(), [getFilteredStudents]);

  const avgAttendance = useMemo(() => Math.round(studentAnalytics.reduce((s, a) => s + a.attendanceScore, 0) / studentAnalytics.length), [studentAnalytics]);
  const avgAssignment = useMemo(() => Math.round(studentAnalytics.reduce((s, a) => s + a.assignmentScore, 0) / studentAnalytics.length), [studentAnalytics]);
  const avgAssessment = useMemo(() => Math.round(studentAnalytics.reduce((s, a) => s + a.assessmentScore, 0) / studentAnalytics.length), [studentAnalytics]);
  const avgProgress = useMemo(() => Math.round(studentAnalytics.reduce((s, a) => s + a.learningProgress, 0) / studentAnalytics.length), [studentAnalytics]);

  const clearFilters = () => { setSearchTerm(''); setCourseFilter('all'); setBatchFilter('all'); setPerformanceFilter('all'); };

  const columns = [
    { key: 'studentName', label: 'Student', sortable: true },
    { key: 'courseName', label: 'Course', sortable: true },
    { key: 'attendanceScore', label: 'Attendance', sortable: true, render: (v: unknown) => `${String(v)}%` },
    { key: 'assignmentScore', label: 'Assignment', sortable: true, render: (v: unknown) => `${String(v)}%` },
    { key: 'assessmentScore', label: 'Assessment', sortable: true, render: (v: unknown) => `${String(v)}%` },
    { key: 'learningProgress', label: 'Progress', sortable: true, render: (v: unknown) => `${String(v)}%` },
    { key: 'performanceLevel', label: 'Performance', sortable: true, render: (v: unknown) => PERFORMANCE_LABELS[v as keyof typeof PERFORMANCE_LABELS] || String(v) },
    { key: 'riskIndicator', label: 'Risk', render: (v: unknown) => (
      <span style={{ color: v === 'high' ? '#dc2626' : v === 'medium' ? '#ca8a04' : '#16a34a', fontWeight: 'var(--weight-medium)', textTransform: 'capitalize' }}>{String(v)}</span>
    )},
  ];

  return (
    <main style={{ padding: 'var(--space-3)', display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Student Analytics</h1>
        <SharedFilters currentPage="analytics/students" />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(140px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <MetricCard label="Avg Attendance" value={`${avgAttendance}%`} color="#2563eb" />
        <MetricCard label="Avg Assignment" value={`${avgAssignment}%`} color="#8b5cf6" />
        <MetricCard label="Avg Assessment" value={`${avgAssessment}%`} color="#f59e0b" />
        <MetricCard label="Avg Progress" value={`${avgProgress}%`} color="#16a34a" />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(120px, 1fr))', gap: 'var(--space-component-gap)' }}>
        {filtered.slice(0, 6).map((s) => (
          <ProgressRing key={s.id} value={s.learningProgress} label={s.studentName} size={70} color={s.learningProgress >= 80 ? '#16a34a' : s.learningProgress >= 50 ? '#2563eb' : '#dc2626'} />
        ))}
      </div>

      <DashboardWidget title="Performance Table" subtitle={`${filtered.length} students`}>
        {filtered.length === 0 ? (
          <EmptyState type="noSearchResults" onClearFilters={clearFilters} />
        ) : (
          <DataTable columns={columns} data={filtered as unknown as Record<string, unknown>[]} />
        )}
      </DashboardWidget>
    </main>
  );
}
