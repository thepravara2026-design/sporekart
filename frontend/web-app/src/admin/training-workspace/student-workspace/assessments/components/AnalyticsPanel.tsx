import { memo } from 'react';
import type { AcademicAnalytics } from '../types';
import { DIFFICULTY_LABELS } from '../types';

interface AnalyticsPanelProps {
  analytics: AcademicAnalytics;
}

export const AnalyticsPanel = memo(function AnalyticsPanel({ analytics }: AnalyticsPanelProps) {
  const maxDiffCount = Math.max(...analytics.questionDifficultyDistribution.map((d) => d.count), 1);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(150px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Total Assessments</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)' }}>{analytics.totalAssessments}</div>
        </div>
        <div style={{ padding: 'var(--space-3)', background: '#eff6ff', borderRadius: 'var(--radius-md)', border: '1px solid #2563eb' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Average Score</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: '#2563eb' }}>{analytics.averageScore}%</div>
        </div>
        <div style={{ padding: 'var(--space-3)', background: '#f0fdf4', borderRadius: 'var(--radius-md)', border: '1px solid #16a34a' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Pass Rate</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: '#16a34a' }}>{analytics.passPercent}%</div>
        </div>
        <div style={{ padding: 'var(--space-3)', background: '#fef2f2', borderRadius: 'var(--radius-md)', border: '1px solid #dc2626' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Fail Rate</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: '#dc2626' }}>{analytics.failPercent}%</div>
        </div>
        <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Highest Score</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)' }}>{analytics.highestScore}%</div>
        </div>
        <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Completion</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)' }}>{analytics.assessmentCompletionPercent}%</div>
        </div>
      </div>

      <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
        <h3 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: '0 0 12px 0' }}>Question Difficulty Distribution</h3>
        <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
          {analytics.questionDifficultyDistribution.map((d) => (
            <div key={d.difficulty} style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
              <span style={{ width: 100, fontSize: 'var(--text-body-sm)', flexShrink: 0 }}>{DIFFICULTY_LABELS[d.difficulty]}</span>
              <div style={{ flex: 1, height: 12, background: 'var(--color-bg-skeleton-base)', borderRadius: 6, overflow: 'hidden' }}>
                <div style={{ width: `${(d.count / maxDiffCount) * 100}%`, height: '100%', background: '#2563eb', borderRadius: 6 }} />
              </div>
              <span style={{ width: 30, textAlign: 'right', fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)' }}>{d.count}</span>
            </div>
          ))}
        </div>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 'var(--space-component-gap)' }}>
        <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
          <h3 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: '0 0 12px 0' }}>Course Performance</h3>
          {analytics.coursePerformance.map((c) => (
            <div key={c.course} style={{ display: 'flex', justifyContent: 'space-between', padding: '4px 0', fontSize: 'var(--text-body-sm)', borderBottom: '1px solid var(--color-border-subtle)' }}>
              <span style={{ flex: 1 }}>{c.course}</span>
              <span style={{ fontWeight: 'var(--weight-medium)', color: c.avgScore >= 70 ? '#16a34a' : c.avgScore >= 50 ? '#ca8a04' : '#dc2626' }}>{c.avgScore}%</span>
            </div>
          ))}
        </div>
        <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
          <h3 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: '0 0 12px 0' }}>Batch Performance</h3>
          {analytics.batchPerformance.map((b) => (
            <div key={b.batch} style={{ display: 'flex', justifyContent: 'space-between', padding: '4px 0', fontSize: 'var(--text-body-sm)', borderBottom: '1px solid var(--color-border-subtle)' }}>
              <span style={{ flex: 1 }}>{b.batch}</span>
              <span style={{ fontWeight: 'var(--weight-medium)', color: b.avgScore >= 70 ? '#16a34a' : b.avgScore >= 50 ? '#ca8a04' : '#dc2626' }}>{b.avgScore}%</span>
            </div>
          ))}
        </div>
      </div>

      <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
        <h3 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: '0 0 12px 0' }}>Monthly Trend</h3>
        <div style={{ display: 'flex', gap: 8, alignItems: 'flex-end', height: 120 }}>
          {analytics.monthlyTrend.map((m) => (
            <div key={m.month} style={{ flex: 1, display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 4 }}>
              <div style={{
                width: '100%', maxWidth: 40, background: '#2563eb', borderRadius: '4px 4px 0 0',
                height: `${(m.count / Math.max(...analytics.monthlyTrend.map((x) => x.count), 1)) * 100}px`,
                minHeight: 20,
              }} />
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{m.month}</span>
              <span style={{ fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)' }}>{m.count}</span>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
});
