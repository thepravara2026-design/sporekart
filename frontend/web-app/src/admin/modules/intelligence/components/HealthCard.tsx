import { memo } from 'react';

interface HealthCardData {
  label: string; score: number; maxScore: number; status: 'healthy' | 'warning' | 'critical'; description: string; trend: 'up' | 'down' | 'neutral';
}

const STATUS_COLORS: Record<string, string> = { healthy: 'var(--color-success)', warning: 'var(--color-warning)', critical: 'var(--color-danger)' };
const TREND_ICONS: Record<string, string> = { up: '\u2191', down: '\u2193', neutral: '\u2192' };

export const HealthCard = memo(function HealthCard({ data }: { data: HealthCardData }) {
  const pct = Math.round((data.score / data.maxScore) * 100);
  return (
    <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16, display: 'flex', flexDirection: 'column', gap: 12 }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
        <div>
          <span style={{ fontWeight: 600, fontSize: 'var(--text-body)' }}>{data.label}</span>
          <div style={{ display: 'flex', gap: 6, alignItems: 'center', marginTop: 2 }}>
            <span style={{ width: 10, height: 10, borderRadius: '50%', background: STATUS_COLORS[data.status], display: 'inline-block' }} />
            <span style={{ fontSize: 'var(--text-caption)', color: STATUS_COLORS[data.status], textTransform: 'capitalize' }}>{data.status}</span>
          </div>
        </div>
        <div style={{ textAlign: 'right' }}>
          <div style={{ fontSize: 'var(--text-h1, 26px)', fontWeight: 700, color: 'var(--color-text-primary)' }}>{data.score}</div>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>/ {data.maxScore}</div>
        </div>
      </div>
      <div style={{ width: '100%', height: 8, background: 'var(--color-border)', borderRadius: 4, overflow: 'hidden' }}>
        <div style={{ width: `${pct}%`, height: '100%', background: STATUS_COLORS[data.status], borderRadius: 4, transition: 'width 0.3s' }} />
      </div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{data.description}</span>
        <span style={{ fontSize: 12, color: STATUS_COLORS[data.status] }}>{TREND_ICONS[data.trend]}</span>
      </div>
    </div>
  );
});

export const HealthCardGrid = memo(function HealthCardGrid({ data }: { data: HealthCardData[] }) {
  return (
    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: 12 }}>
      {data.map((d) => <HealthCard key={d.label} data={d} />)}
    </div>
  );
});
