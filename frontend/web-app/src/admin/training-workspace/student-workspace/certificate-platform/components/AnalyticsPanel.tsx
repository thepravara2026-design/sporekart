import { memo } from 'react';
import type { CertificateAnalytics } from '../types';

interface AnalyticsPanelProps {
  analytics: CertificateAnalytics;
}

export const AnalyticsPanel = memo(function AnalyticsPanel({ analytics }: AnalyticsPanelProps) {
  const maxByCourse = Math.max(...analytics.certificatesByCourse.map((c) => c.count), 1);
  const maxByMonth = Math.max(...analytics.certificatesByMonth.map((m) => m.count), 1);
  const maxAch = Math.max(...analytics.achievementDistribution.map((a) => a.count), 1);
  const maxBadge = Math.max(...analytics.badgeDistribution.map((b) => b.count), 1);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Verification Rate</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)' }}>{analytics.verificationRate}%</div>
        </div>
        <div style={{ padding: 'var(--space-3)', background: '#f0fdf4', borderRadius: 'var(--radius-md)', border: '1px solid #16a34a' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Completion Rate</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: '#16a34a' }}>{analytics.completionRate}%</div>
        </div>
        <div style={{ padding: 'var(--space-3)', background: '#eff6ff', borderRadius: 'var(--radius-md)', border: '1px solid #2563eb' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Verification Requests</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: '#2563eb' }}>{analytics.verificationRequests}</div>
        </div>
        <div style={{ padding: 'var(--space-3)', background: '#fefce8', borderRadius: 'var(--radius-md)', border: '1px solid #ca8a04' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Badges Issued</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: '#ca8a04' }}>{analytics.totalBadges}</div>
        </div>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 'var(--space-component-gap)' }}>
        <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
          <h3 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: '0 0 12px 0' }}>Certificates by Course</h3>
          {analytics.certificatesByCourse.map((c) => (
            <div key={c.courseName} style={{ display: 'flex', alignItems: 'center', gap: 8, marginBottom: 6 }}>
              <span style={{ width: '40%', fontSize: 'var(--text-body-sm)', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>{c.courseName}</span>
              <div style={{ flex: 1, height: 10, background: 'var(--color-bg-skeleton-base)', borderRadius: 5, overflow: 'hidden' }}>
                <div style={{ width: `${(c.count / maxByCourse) * 100}%`, height: '100%', background: '#2563eb', borderRadius: 5 }} />
              </div>
              <span style={{ width: 30, textAlign: 'right', fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)' }}>{c.count}</span>
            </div>
          ))}
        </div>
        <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
          <h3 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: '0 0 12px 0' }}>Certificates by Month</h3>
          <div style={{ display: 'flex', gap: 8, alignItems: 'flex-end', height: 120 }}>
            {analytics.certificatesByMonth.map((m) => (
              <div key={m.month} style={{ flex: 1, display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 4 }}>
                <div style={{ width: '100%', maxWidth: 40, background: '#2563eb', borderRadius: '4px 4px 0 0', height: `${(m.count / maxByMonth) * 100}px`, minHeight: 20 }} />
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{m.month}</span>
                <span style={{ fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)' }}>{m.count}</span>
              </div>
            ))}
          </div>
        </div>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 'var(--space-component-gap)' }}>
        <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
          <h3 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: '0 0 12px 0' }}>Achievement Distribution</h3>
          {analytics.achievementDistribution.slice(0, 8).map((a) => (
            <div key={a.type} style={{ display: 'flex', alignItems: 'center', gap: 8, marginBottom: 6 }}>
              <span style={{ width: 120, fontSize: 'var(--text-body-sm)', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>{a.type}</span>
              <div style={{ flex: 1, height: 10, background: 'var(--color-bg-skeleton-base)', borderRadius: 5, overflow: 'hidden' }}>
                <div style={{ width: `${(a.count / maxAch) * 100}%`, height: '100%', background: '#ca8a04', borderRadius: 5 }} />
              </div>
              <span style={{ width: 30, textAlign: 'right', fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)' }}>{a.count}</span>
            </div>
          ))}
        </div>
        <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
          <h3 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: '0 0 12px 0' }}>Badge Distribution</h3>
          {analytics.badgeDistribution.slice(0, 8).map((b) => (
            <div key={b.type} style={{ display: 'flex', alignItems: 'center', gap: 8, marginBottom: 6 }}>
              <span style={{ width: 120, fontSize: 'var(--text-body-sm)', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>{b.type}</span>
              <div style={{ flex: 1, height: 10, background: 'var(--color-bg-skeleton-base)', borderRadius: 5, overflow: 'hidden' }}>
                <div style={{ width: `${(b.count / maxBadge) * 100}%`, height: '100%', background: '#0891b2', borderRadius: 5 }} />
              </div>
              <span style={{ width: 30, textAlign: 'right', fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)' }}>{b.count}</span>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
});
