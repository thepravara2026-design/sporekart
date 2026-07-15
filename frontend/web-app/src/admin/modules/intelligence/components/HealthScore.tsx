import { memo } from 'react';
import type { HealthMetric } from '../types';
import { getScoreVariant } from '../utils';
import { VARIANT_COLORS } from '../../../constants/variantColors';
const TREND_ICONS: Record<string, string> = { up: '\u2191', down: '\u2193', neutral: '\u2192' };

export const HealthScore = memo(function HealthScore({ metric }: { metric: HealthMetric }) {
  const pct = Math.round((metric.score / metric.maxScore) * 100);
  return (
    <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16, display: 'flex', alignItems: 'center', gap: 16 }}>
      <div style={{ position: 'relative', width: 60, height: 60, flexShrink: 0 }}>
        <svg width="60" height="60" viewBox="0 0 60 60">
          <circle cx="30" cy="30" r="26" fill="none" stroke="var(--color-border)" strokeWidth="6" />
          <circle cx="30" cy="30" r="26" fill="none" stroke={VARIANT_COLORS[getScoreVariant(pct)]} strokeWidth="6"
            strokeDasharray={`${(pct / 100) * 163} 163`} transform="rotate(-90 30 30)" strokeLinecap="round" />
        </svg>
        <span style={{ position: 'absolute', inset: 0, display: 'flex', alignItems: 'center', justifyContent: 'center', fontWeight: 700, fontSize: 14 }}>{pct}%</span>
      </div>
      <div style={{ flex: 1 }}>
        <span style={{ fontWeight: 600, fontSize: 'var(--text-body)' }}>{metric.label}</span>
        <div style={{ display: 'flex', gap: 6, alignItems: 'center', marginTop: 2 }}>
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{metric.score}/{metric.maxScore}</span>
          <span style={{ fontSize: 12, color: VARIANT_COLORS[getScoreVariant(pct)] }}>{TREND_ICONS[metric.trend]}</span>
        </div>
      </div>
    </div>
  );
});

export const HealthScoreGrid = memo(function HealthScoreGrid({ metrics }: { metrics: HealthMetric[] }) {
  return (
    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: 12 }}>
      {metrics.map((m) => <HealthScore key={m.label} metric={m} />)}
    </div>
  );
});
