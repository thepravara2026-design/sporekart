import { memo } from 'react';
import { MetricCard, type WarehouseMetricDisplay } from './MetricCard';
import type { WarehouseMetric, HealthMetric, StatusCount } from '../types';

interface StatisticsGridProps {
  metrics: WarehouseMetric[];
  health?: HealthMetric[];
  statusCounts?: StatusCount[];
  loading?: boolean;
  metricCount?: number;
}

function HealthCard({ health, loading }: { health: HealthMetric; loading?: boolean }) {
  if (loading) {
    return <div style={{ background: 'var(--color-surface)', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', padding: 14, display: 'flex', flexDirection: 'column', gap: 8 }}><div style={{ height: 12, width: '50%', background: 'var(--color-surface-hover)', borderRadius: 4, animation: 'shimmer 1.5s infinite' }} /><div style={{ height: 8, background: 'var(--color-surface-hover)', borderRadius: 4, animation: 'shimmer 1.5s infinite' }} /></div>;
  }
  const score = health.score;
  const barColor = score >= 90 ? 'var(--color-success)' : score >= 75 ? 'var(--color-warning)' : 'var(--color-danger)';
  return (
    <div style={{ background: 'var(--color-surface)', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', padding: 14, display: 'flex', flexDirection: 'column', gap: 8 }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
        <span>{health.label}</span>
        <span style={{ fontWeight: 700, color: 'var(--color-text-primary)' }}>{score}</span>
      </div>
      <div style={{ height: 6, background: 'var(--color-surface-hover)', borderRadius: 4, overflow: 'hidden' }}>
        <div style={{ height: '100%', width: `${score}%`, background: barColor }} />
      </div>
    </div>
  );
}

function StatusCountCard({ status }: { status: StatusCount }) {
  return (
    <div style={{ background: 'var(--color-surface)', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', padding: 14, display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
      <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{status.label}</span>
      <span style={{ fontWeight: 700, color: 'var(--color-text-primary)', fontSize: 'var(--text-body)' }}>{status.count}</span>
    </div>
  );
}

export const StatisticsGrid = memo(function StatisticsGrid({ metrics, health = [], statusCounts = [], loading, metricCount = 8 }: StatisticsGridProps) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: 'var(--space-component-gap)' }}>
        {loading
          ? Array.from({ length: metricCount }).map((_, i) => <MetricCard key={i} metric={{ id: `s${i}`, value: '', trend: 'flat', icon: 'package', loading: true }} />)
          : (metrics as WarehouseMetricDisplay[]).map((m) => <MetricCard key={m.id} metric={m} />)}
      </div>
      {(health.length > 0 || statusCounts.length > 0) && (
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(180px, 1fr))', gap: 'var(--space-component-gap)' }}>
          {loading ? health.map((h, i) => <HealthCard key={h.id ?? i} health={h} loading />) : health.map((h) => <HealthCard key={h.id} health={h} />)}
          {loading ? statusCounts.map((s, i) => <StatusCountCard key={s.id ?? i} status={s} />) : statusCounts.map((s) => <StatusCountCard key={s.id} status={s} />)}
        </div>
      )}
    </div>
  );
});


