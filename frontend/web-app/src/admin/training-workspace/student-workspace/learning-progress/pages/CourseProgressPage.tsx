import { useMemo } from 'react';
import { useProgress } from '../state/LearningProgressContext';
import { ProgressTable } from '../components/ProgressTable';
import { DashboardWidget } from '../components/DashboardWidget';
import { EmptyState } from '../components/EmptyStates';
import { ProgressTableSkeleton } from '../components/Skeletons';

export function CourseProgressPage() {
  const { progressRecords } = useProgress();

  const courseGroups = useMemo(() => {
    const groups: Record<string, typeof progressRecords> = {};
    progressRecords.forEach((rec) => {
      if (!groups[rec.courseId]) groups[rec.courseId] = [];
      groups[rec.courseId].push(rec);
    });
    return groups;
  }, [progressRecords]);

  const courseSummaries = useMemo(() => {
    return Object.entries(courseGroups).map(([courseId, records]) => {
      const avg = Math.round(records.reduce((s, r) => s + r.progressPercent, 0) / records.length);
      const completed = records.filter((r) => r.status === 'completed' || r.status === 'certified').length;
      const onTrack = records.filter((r) => r.status === 'on-track' || r.status === 'in-progress').length;
      const behind = records.filter((r) => r.status === 'behind-schedule' || r.status === 'paused').length;
      return { courseId, courseName: records[0]?.courseName ?? 'Unknown', total: records.length, avg, completed, onTrack, behind };
    });
  }, [courseGroups]);

  if (!progressRecords) return <ProgressTableSkeleton />;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Course Progress</h2>

      {Object.entries(courseGroups).length === 0 ? (
        <EmptyState type="noProgress" />
      ) : (
        Object.entries(courseGroups).map(([courseId, records]) => {
          const summary = courseSummaries.find((s) => s.courseId === courseId);
          return (
            <div key={courseId} style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-component-gap)', flexWrap: 'wrap' }}>
                <h3 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: 0, flex: 1 }}>{records[0]?.courseName}</h3>
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{records.length} students</span>
              </div>
              {summary && (
                <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(140px, 1fr))', gap: 'var(--space-component-gap)' }}>
                  <DashboardWidget label="Average Progress" value={`${summary.avg}%`} variant="info" />
                  <DashboardWidget label="Completed" value={summary.completed} variant="success" />
                  <DashboardWidget label="On Track" value={summary.onTrack} variant="default" />
                  <DashboardWidget label="Behind" value={summary.behind} variant={summary.behind > 0 ? 'danger' : 'default'} />
                </div>
              )}
              <ProgressTable records={records} />
            </div>
          );
        })
      )}
    </div>
  );
}
