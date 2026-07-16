import { memo } from 'react';
import type { LearningAnalytics } from '../types';

interface AnalyticsPanelProps {
  analytics: LearningAnalytics;
}

export const AnalyticsPanel = memo(function AnalyticsPanel({ analytics }: AnalyticsPanelProps) {
  const maxDistCount = Math.max(...analytics.progressDistribution.map((d) => d.count), 1);
  const maxActiveCount = Math.max(...analytics.monthlyActiveStudents.map((m) => m.count), 1);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(150px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Course Completion</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)' }}>{analytics.courseCompletionPercent}%</div>
        </div>
        <div style={{ padding: 'var(--space-3)', background: '#eff6ff', borderRadius: 'var(--radius-md)', border: '1px solid #2563eb' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Module Completion</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: '#2563eb' }}>{analytics.moduleCompletionPercent}%</div>
        </div>
        <div style={{ padding: 'var(--space-3)', background: '#f0fdf4', borderRadius: 'var(--radius-md)', border: '1px solid #16a34a' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Assessment Success</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: '#16a34a' }}>{analytics.assessmentSuccessRate}%</div>
        </div>
        <div style={{ padding: 'var(--space-3)', background: '#fefce8', borderRadius: 'var(--radius-md)', border: '1px solid #ca8a04' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Assignment Success</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: '#ca8a04' }}>{analytics.assignmentSuccessRate}%</div>
        </div>
        <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Avg Learning Hours</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)' }}>{analytics.averageLearningHours}h</div>
        </div>
        <div style={{ padding: 'var(--space-3)', background: '#f0fdf4', borderRadius: 'var(--radius-md)', border: '1px solid #16a34a' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Competency Growth</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: '#16a34a' }}>{analytics.competencyGrowth}%</div>
        </div>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 'var(--space-component-gap)' }}>
        <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
          <h3 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: '0 0 12px 0' }}>Progress Distribution</h3>
          {analytics.progressDistribution.map((d) => (
            <div key={d.range} style={{ display: 'flex', alignItems: 'center', gap: 8, marginBottom: 8 }}>
              <span style={{ width: 70, fontSize: 'var(--text-body-sm)', flexShrink: 0 }}>{d.range}</span>
              <div style={{ flex: 1, height: 12, background: 'var(--color-bg-skeleton-base)', borderRadius: 6, overflow: 'hidden' }}>
                <div style={{ width: `${(d.count / maxDistCount) * 100}%`, height: '100%', background: '#2563eb', borderRadius: 6 }} />
              </div>
              <span style={{ width: 30, textAlign: 'right', fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)' }}>{d.count}</span>
            </div>
          ))}
        </div>
        <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
          <h3 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: '0 0 12px 0' }}>Monthly Active Students</h3>
          <div style={{ display: 'flex', gap: 8, alignItems: 'flex-end', height: 120 }}>
            {analytics.monthlyActiveStudents.map((m) => (
              <div key={m.month} style={{ flex: 1, display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 4 }}>
                <div style={{ width: '100%', maxWidth: 40, background: '#2563eb', borderRadius: '4px 4px 0 0', height: `${(m.count / maxActiveCount) * 100}px`, minHeight: 20 }} />
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{m.month}</span>
                <span style={{ fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)' }}>{m.count}</span>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
});
