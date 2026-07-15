import { memo } from 'react';

interface TrendCardData {
  label: string; value: string; trend: 'up' | 'down' | 'neutral'; change: string; subtitle?: string;
}

const TREND_ICONS: Record<string, string> = { up: '\u2191', down: '\u2193', neutral: '\u2192' };
const TREND_COLORS: Record<string, string> = { up: 'var(--color-success)', down: 'var(--color-danger)', neutral: 'var(--color-neutral)' };

export const TrendCard = memo(function TrendCard({ data }: { data: TrendCardData }) {
  return (
    <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 20, display: 'flex', flexDirection: 'column', gap: 8 }}>
      <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{data.label}</span>
      <div style={{ display: 'flex', alignItems: 'baseline', gap: 10 }}>
        <span style={{ fontSize: 'var(--text-h1, 26px)', fontWeight: 700, color: 'var(--color-text-primary)' }}>{data.value}</span>
        <span style={{ fontSize: 14, color: TREND_COLORS[data.trend], display: 'flex', alignItems: 'center', gap: 2 }}>
          {TREND_ICONS[data.trend]} {data.change}
        </span>
      </div>
      {data.subtitle && <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{data.subtitle}</span>}
      <div style={{ display: 'flex', gap: 4, marginTop: 4 }}>
        {[1, 2, 3, 4, 5, 6, 7].map((i) => (
          <div key={i} style={{ flex: 1, height: 4, borderRadius: 2, background: data.trend === 'up' ? 'var(--color-success)' : data.trend === 'down' ? 'var(--color-danger)' : 'var(--color-neutral)', opacity: i <= 5 ? 1 : 0.3 }} />
        ))}
      </div>
    </div>
  );
});

export const TrendCardGrid = memo(function TrendCardGrid({ data }: { data: TrendCardData[] }) {
  return (
    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(220px, 1fr))', gap: 12 }}>
      {data.map((d) => <TrendCard key={d.label} data={d} />)}
    </div>
  );
});
