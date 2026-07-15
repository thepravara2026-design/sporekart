import { memo } from 'react';
import type { KpiMetric } from '../types';

export const KpiCard = memo(function KpiCard({ metric }: { metric: KpiMetric }) {
  const variantColor = metric.variant === 'success' ? 'var(--color-success)' : metric.variant === 'warning' ? 'var(--color-warning)' : metric.variant === 'danger' ? 'var(--color-danger)' : 'var(--color-info)';
  const trendIcon = metric.trend === 'up' ? '\u2191' : metric.trend === 'down' ? '\u2193' : '\u2192';
  return (
    <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 20, display: 'flex', flexDirection: 'column', gap: 8 }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{metric.label}</span>
        <span style={{ fontSize: 14, color: variantColor }}>{trendIcon}</span>
      </div>
      <div style={{ fontSize: 'var(--text-h1, 26px)', fontWeight: 700, color: 'var(--color-text-primary)' }}>
        {metric.value}
      </div>
      <div style={{ display: 'flex', alignItems: 'center', gap: 6 }}>
        {metric.icon && <span style={{ fontSize: 14, color: variantColor }}>{metric.icon}</span>}
        {metric.subtitle && <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{metric.subtitle}</span>}
      </div>
    </div>
  );
});

export const KpiCardGrid = memo(function KpiCardGrid({ metrics }: { metrics: KpiMetric[] }) {
  return (
    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(220px, 1fr))', gap: 12 }}>
      {metrics.map((m) => <KpiCard key={m.label} metric={m} />)}
    </div>
  );
});
