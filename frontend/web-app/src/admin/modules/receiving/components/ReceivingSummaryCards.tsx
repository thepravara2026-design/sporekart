import { memo } from 'react';
import type { ReceivingMetric } from '../types';
import { VARIANT_COLORS } from '../../../constants/variantColors';
const TREND_ICONS: Record<string, string> = { up: '\u2191', down: '\u2193', neutral: '\u2192' };

export const ReceivingSummaryCards = memo(function ReceivingSummaryCards({ metrics }: { metrics: ReceivingMetric[] }) {
  return (
    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))', gap: 12 }}>
      {metrics.map((metric) => (
        <div key={metric.label} style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16, display: 'flex', flexDirection: 'column', gap: 4 }}>
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{metric.label}</span>
          <div style={{ display: 'flex', alignItems: 'baseline', gap: 8 }}>
            <span style={{ fontSize: 'var(--text-h1, 24px)', fontWeight: 700, color: 'var(--color-text-primary)' }}>{metric.value.toLocaleString()}{metric.label === 'Acceptance Rate' ? '%' : ''}</span>
            <span style={{ fontSize: 'var(--text-body, 14px)', color: VARIANT_COLORS[metric.variant] ?? 'var(--color-text-tertiary)' }}>{TREND_ICONS[metric.trend] ?? ''}</span>
          </div>
        </div>
      ))}
    </div>
  );
});
