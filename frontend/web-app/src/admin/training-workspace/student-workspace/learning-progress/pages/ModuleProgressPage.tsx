import { useMemo } from 'react';
import { useProgress } from '../state/LearningProgressContext';
import { ProgressCard } from '../components/ProgressCard';
import { DashboardWidget } from '../components/DashboardWidget';
import { EmptyState } from '../components/EmptyStates';
import { ProgressTableSkeleton } from '../components/Skeletons';

export function ModuleProgressPage() {
  const { progressRecords } = useProgress();

  const moduleGroups = useMemo(() => {
    const groups: Record<string, typeof progressRecords> = {};
    progressRecords.forEach((rec) => {
      if (!groups[rec.moduleId]) groups[rec.moduleId] = [];
      groups[rec.moduleId].push(rec);
    });
    return groups;
  }, [progressRecords]);

  if (!progressRecords) return <ProgressTableSkeleton />;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Module Progress</h2>

      {Object.entries(moduleGroups).length === 0 ? (
        <EmptyState type="noProgress" />
      ) : (
        Object.entries(moduleGroups).map(([moduleId, records]) => {
          const avg = Math.round(records.reduce((s, r) => s + r.progressPercent, 0) / records.length);
          const completed = records.filter((r) => r.status === 'completed' || r.status === 'certified').length;
          return (
            <div key={moduleId} style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-component-gap)', flexWrap: 'wrap' }}>
                <h3 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: 0, flex: 1 }}>{records[0]?.moduleName}</h3>
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{records.length} students</span>
              </div>
              <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(140px, 1fr))', gap: 'var(--space-component-gap)' }}>
                <DashboardWidget label="Average Progress" value={`${avg}%`} variant="info" />
                <DashboardWidget label="Completed" value={completed} variant="success" />
                <DashboardWidget label="Total Enrolled" value={records.length} variant="default" />
              </div>
              <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: 'var(--space-component-gap)' }}>
                {records.map((rec) => (
                  <ProgressCard key={rec.id} record={rec} />
                ))}
              </div>
            </div>
          );
        })
      )}
    </div>
  );
}
