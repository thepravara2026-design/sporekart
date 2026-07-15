import { memo } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import type { InventoryMetric } from '../types';

interface MetricCardProps {
  metric?: InventoryMetric;
  loading?: boolean;
}

export const MetricCard = memo(function MetricCard({ metric, loading }: MetricCardProps) {
  if (loading) {
    return (
      <div
        aria-busy="true"
        style={{
          background: 'var(--color-surface)', border: '1px solid var(--color-border)',
          borderRadius: 'var(--radius-lg)', padding: '20px 24px',
        }}
      >
        <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 16 }}>
          <div style={{ width: 80, height: 14, background: 'var(--color-surface-hover)', borderRadius: 4, animation: 'shimmer 1.5s infinite' }} />
          <div style={{ width: 36, height: 36, background: 'var(--color-surface-hover)', borderRadius: 'var(--radius-md)', animation: 'shimmer 1.5s infinite' }} />
        </div>
        <div style={{ width: 120, height: 28, background: 'var(--color-surface-hover)', borderRadius: 4, marginBottom: 8, animation: 'shimmer 1.5s infinite' }} />
        <div style={{ width: 100, height: 12, background: 'var(--color-surface-hover)', borderRadius: 4, animation: 'shimmer 1.5s infinite' }} />
      </div>
    );
  }

  if (!metric) return null;

  const trendIcon = metric.trend === 'up' ? 'trending-up' : metric.trend === 'down' ? 'trending-down' : 'minus';
  const trendColor = metric.trend === 'up' ? 'var(--color-success)' : metric.trend === 'down' ? 'var(--color-error)' : 'var(--color-text-tertiary)';

  return (
    <div
      style={{
        background: 'var(--color-surface)', border: '1px solid var(--color-border)',
        borderRadius: 'var(--radius-lg)', padding: '20px 24px',
        transition: 'box-shadow 0.2s, transform 0.2s',
      }}
    >
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: 16 }}>
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', fontWeight: 500, textTransform: 'uppercase', letterSpacing: '0.05em' }}>
          {metric.title}
        </span>
        <div style={{ width: 36, height: 36, borderRadius: 'var(--radius-md)', background: `${metric.color}1A`, display: 'flex', alignItems: 'center', justifyContent: 'center', color: metric.color }}>
          <Icon name={metric.icon} size={18} />
        </div>
      </div>
      <div style={{ fontSize: 'var(--text-h2)', fontWeight: 700, color: 'var(--color-text-primary)', marginBottom: 8, lineHeight: 1 }}>
        {metric.value}
      </div>
      <div style={{ display: 'flex', alignItems: 'center', gap: 6 }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 2, color: trendColor }}>
          <Icon name={trendIcon} size={14} />
          <span style={{ fontWeight: 600, fontSize: 'var(--text-body)' }}>{formatPercentLocal(metric.percentage)}</span>
        </div>
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{metric.comparison}</span>
      </div>
    </div>
  );
});

function formatPercentLocal(n: number): string {
  return `${n > 0 ? '+' : ''}${n.toFixed(1)}%`;
}

