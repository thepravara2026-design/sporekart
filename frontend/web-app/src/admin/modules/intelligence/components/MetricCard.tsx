import { memo } from 'react';
import type { KpiMetric } from '../types';
import { VARIANT_COLORS } from '../../../constants/variantColors';
const TREND_ICONS: Record<string, string> = { up: '\u2191', down: '\u2193', neutral: '\u2192' };

export const MetricCard = memo(function MetricCard({ metric }: { metric: KpiMetric }) {
  return (
    <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 20, display: 'flex', flexDirection: 'column', gap: 4 }}>
      <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{metric.label}</span>
      <div style={{ display: 'flex', alignItems: 'baseline', gap: 8 }}>
        <span style={{ fontSize: 'var(--text-h1, 26px)', fontWeight: 700, color: 'var(--color-text-primary)' }}>{metric.value}</span>
        <span style={{ fontSize: 16, color: VARIANT_COLORS[metric.variant] }}>{TREND_ICONS[metric.trend]}</span>
      </div>
      {metric.subtitle && <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{metric.subtitle}</span>}
    </div>
  );
});

export const MetricCardGrid = memo(function MetricCardGrid({ metrics }: { metrics: KpiMetric[] }) {
  return (
    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(220px, 1fr))', gap: 12 }}>
      {metrics.map((m) => <MetricCard key={m.label} metric={m} />)}
    </div>
  );
});
