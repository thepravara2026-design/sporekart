import { memo } from 'react';
import { MetricCard } from './MetricCard';
import { HealthCard } from './HealthCard';
import { StatusCard } from './StatusCard';
import type { InventoryMetric, HealthMetric, StatusCount } from '../types';

interface StatisticsGridProps {
  metrics: InventoryMetric[];
  health?: HealthMetric[];
  statusCounts?: StatusCount[];
  loading?: boolean;
  metricCount?: number;
}

export const StatisticsGrid = memo(function StatisticsGrid({ metrics, health = [], statusCounts = [], loading, metricCount = 8 }: StatisticsGridProps) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))', gap: 'var(--space-component-gap)' }}>
        {loading
          ? Array.from({ length: metricCount }).map((_, i) => <MetricCard key={i} metric={{ id: `s${i}`, title: '', value: '', trend: 'flat', percentage: 0, comparison: '', icon: 'package', color: 'var(--color-primary)' }} loading />)
          : metrics.slice(0, metricCount).map((m) => <MetricCard key={m.id} metric={m} />)}
      </div>
      {(health.length > 0 || statusCounts.length > 0) && (
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(180px, 1fr))', gap: 'var(--space-component-gap)' }}>
          {loading
            ? health.map((h, i) => <HealthCard key={h.id ?? i} health={h} loading />)
            : health.map((h) => <HealthCard key={h.id} health={h} />)}
          {loading
            ? statusCounts.map((s, i) => <StatusCard key={s.id ?? i} status={s} />)
            : statusCounts.map((s) => <StatusCard key={s.id} status={s} />)}
        </div>
      )}
    </div>
  );
});
