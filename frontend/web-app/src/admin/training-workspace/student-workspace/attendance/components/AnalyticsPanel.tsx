import { memo } from 'react';
import type { AttendanceSummary } from '../types';

interface AnalyticsPanelProps {
  summary: AttendanceSummary;
}

export const AnalyticsPanel = memo(function AnalyticsPanel({ summary }: AnalyticsPanelProps) {
  const total = summary.totalPresent + summary.totalAbsent + summary.totalLate + summary.totalHalfDay + summary.totalExcused;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(140px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <div style={{ padding: 'var(--space-3)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Overall</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: summary.overallPercent >= 75 ? '#16a34a' : '#dc2626' }}>{summary.overallPercent}%</div>
        </div>
        <div style={{ padding: 'var(--space-3)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Present</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: '#16a34a' }}>{summary.totalPresent}</div>
        </div>
        <div style={{ padding: 'var(--space-3)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Absent</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: '#dc2626' }}>{summary.totalAbsent}</div>
        </div>
        <div style={{ padding: 'var(--space-3)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Late</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: '#ca8a04' }}>{summary.totalLate}</div>
        </div>
        <div style={{ padding: 'var(--space-3)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Half Day</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: '#ca8a04' }}>{summary.totalHalfDay}</div>
        </div>
        <div style={{ padding: 'var(--space-3)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Excused</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: '#2563eb' }}>{summary.totalExcused}</div>
        </div>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 'var(--space-component-gap)', alignItems: 'start' }}>
        <div style={{ padding: 'var(--space-3)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)' }}>
          <h3 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', margin: '0 0 var(--space-2) 0' }}>Course-wise Attendance</h3>
          {summary.courseWise.map((c) => (
            <div key={c.course} style={{ marginBottom: 8 }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-caption)', marginBottom: 2 }}>
                <span>{c.course}</span>
                <span style={{ fontWeight: 'var(--weight-semibold)', color: c.percent >= 75 ? '#16a34a' : '#dc2626' }}>{c.percent}%</span>
              </div>
              <div style={{ height: 6, background: 'var(--color-bg-skeleton-base)', borderRadius: 3, overflow: 'hidden' }}>
                <div style={{ height: '100%', borderRadius: 3, background: c.percent >= 75 ? '#16a34a' : '#dc2626', width: `${c.percent}%`, transition: 'width var(--duration-normal)' }} />
              </div>
            </div>
          ))}
        </div>

        <div style={{ padding: 'var(--space-3)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)' }}>
          <h3 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', margin: '0 0 var(--space-2) 0' }}>Monthly Trend</h3>
          {summary.monthlyTrend.map((m) => (
            <div key={m.month} style={{ marginBottom: 8 }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-caption)', marginBottom: 2 }}>
                <span>{m.month}</span>
                <span style={{ fontWeight: 'var(--weight-semibold)' }}>{m.percent}%</span>
              </div>
              <div style={{ height: 6, background: 'var(--color-bg-skeleton-base)', borderRadius: 3, overflow: 'hidden' }}>
                <div style={{ height: '100%', borderRadius: 3, background: '#2563eb', width: `${m.percent}%`, transition: 'width var(--duration-normal)' }} />
              </div>
            </div>
          ))}
        </div>
      </div>

      <div style={{ padding: 'var(--space-3)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)' }}>
        <h3 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', margin: '0 0 var(--space-2) 0' }}>Attendance Distribution</h3>
        <div style={{ display: 'flex', gap: 0, height: 24, borderRadius: 'var(--radius-sm)', overflow: 'hidden' }}>
          {total > 0 && (
            <>
              <div style={{ flex: summary.totalPresent / total, background: '#16a34a', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 10, color: '#fff', minWidth: 30 }}>Present {Math.round((summary.totalPresent / total) * 100)}%</div>
              <div style={{ flex: summary.totalAbsent / total, background: '#dc2626', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 10, color: '#fff', minWidth: 30 }}>Absent {Math.round((summary.totalAbsent / total) * 100)}%</div>
              <div style={{ flex: summary.totalLate / total, background: '#ca8a04', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 10, color: '#fff', minWidth: 30 }}>Late {Math.round((summary.totalLate / total) * 100)}%</div>
              <div style={{ flex: (summary.totalHalfDay + summary.totalExcused) / total, background: '#2563eb', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 10, color: '#fff', minWidth: 30 }}>Other {Math.round(((summary.totalHalfDay + summary.totalExcused) / total) * 100)}%</div>
            </>
          )}
        </div>
      </div>

      <div style={{ padding: 'var(--space-3)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)' }}>
        <h3 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', margin: '0 0 var(--space-2) 0' }}>Batch-wise Attendance</h3>
        {summary.batchWise.map((b) => (
          <div key={b.batch} style={{ marginBottom: 8 }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-caption)', marginBottom: 2 }}>
              <span>{b.batch}</span>
              <span style={{ fontWeight: 'var(--weight-semibold)' }}>{b.percent}%</span>
            </div>
            <div style={{ height: 6, background: 'var(--color-bg-skeleton-base)', borderRadius: 3, overflow: 'hidden' }}>
              <div style={{ height: '100%', borderRadius: 3, background: '#7c3aed', width: `${b.percent}%`, transition: 'width var(--duration-normal)' }} />
            </div>
          </div>
        ))}
      </div>
    </div>
  );
});
