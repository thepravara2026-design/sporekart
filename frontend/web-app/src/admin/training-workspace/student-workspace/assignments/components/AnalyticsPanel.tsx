import { memo } from 'react';
import type { AssignmentAnalytics } from '../types';
import { ASSIGNMENT_TYPE_LABELS } from '../types';

interface AnalyticsPanelProps {
  analytics: AssignmentAnalytics;
}

export const AnalyticsPanel = memo(function AnalyticsPanel({ analytics }: AnalyticsPanelProps) {
  const maxDistCount = Math.max(...analytics.assignmentDistribution.map((d) => d.count), 1);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      {/* KPI Row */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(150px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Total Assignments</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)' }}>{analytics.totalAssignments}</div>
        </div>
        <div style={{ padding: 'var(--space-3)', background: '#f0fdf4', borderRadius: 'var(--radius-md)', border: '1px solid #16a34a' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Submission Rate</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: '#16a34a' }}>{analytics.submissionRate}%</div>
        </div>
        <div style={{ padding: 'var(--space-3)', background: '#eff6ff', borderRadius: 'var(--radius-md)', border: '1px solid #2563eb' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Completion Rate</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: '#2563eb' }}>{analytics.completionRate}%</div>
        </div>
        <div style={{ padding: 'var(--space-3)', background: '#fef2f2', borderRadius: 'var(--radius-md)', border: '1px solid #dc2626' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Late Submissions</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: '#dc2626' }}>{analytics.lateSubmissionPercent}%</div>
        </div>
        <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Avg Completion</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)' }}>{analytics.averageCompletionTimeDays}d</div>
        </div>
      </div>

      {/* Assignment Distribution */}
      <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
        <h3 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: '0 0 12px 0' }}>Assignment Distribution by Type</h3>
        <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
          {analytics.assignmentDistribution.map((d) => (
            <div key={d.type} style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
              <span style={{ width: 120, fontSize: 'var(--text-body-sm)', flexShrink: 0 }}>{ASSIGNMENT_TYPE_LABELS[d.type]}</span>
              <div style={{ flex: 1, height: 12, background: 'var(--color-bg-skeleton-base)', borderRadius: 6, overflow: 'hidden' }}>
                <div style={{ width: `${(d.count / maxDistCount) * 100}%`, height: '100%', background: '#2563eb', borderRadius: 6 }} />
              </div>
              <span style={{ width: 30, textAlign: 'right', fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)' }}>{d.count}</span>
            </div>
          ))}
        </div>
      </div>

      {/* Course Wise */}
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 'var(--space-component-gap)' }}>
        <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
          <h3 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: '0 0 12px 0' }}>Course Wise</h3>
          {analytics.courseWise.map((c) => (
            <div key={c.course} style={{ display: 'flex', justifyContent: 'space-between', padding: '4px 0', fontSize: 'var(--text-body-sm)' }}>
              <span>{c.course}</span>
              <span style={{ fontWeight: 'var(--weight-medium)' }}>{c.count}</span>
            </div>
          ))}
        </div>
        <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
          <h3 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: '0 0 12px 0' }}>Batch Wise</h3>
          {analytics.batchWise.map((b) => (
            <div key={b.batch} style={{ display: 'flex', justifyContent: 'space-between', padding: '4px 0', fontSize: 'var(--text-body-sm)' }}>
              <span>{b.batch}</span>
              <span style={{ fontWeight: 'var(--weight-medium)' }}>{b.count}</span>
            </div>
          ))}
        </div>
      </div>

      {/* Monthly Trend */}
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
