import { memo } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';

export interface WarehouseMetricDisplay {
  id: string;
  title?: string;
  label?: string;
  value: string;
  trend: 'up' | 'down' | 'flat' | 'neutral';
  percentage?: number;
  comparison?: string;
  icon: string;
  color?: string;
  loading?: boolean;
}

const TREND_ICON: Record<WarehouseMetricDisplay['trend'], string> = {
  up: 'arrow-up-right', down: 'arrow-down-right', flat: 'minus', neutral: 'minus',
};
const TREND_COLOR: Record<WarehouseMetricDisplay['trend'], string> = {
  up: 'var(--color-success)', down: 'var(--color-danger)', flat: 'var(--color-text-tertiary)', neutral: 'var(--color-text-tertiary)',
};

export const MetricCard = memo(function MetricCard({ metric }: { metric: WarehouseMetricDisplay }) {
  if (metric.loading) {
    return (
      <div style={{ background: 'var(--color-surface)', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', padding: 16, display: 'flex', flexDirection: 'column', gap: 10 }}>
        <div style={{ width: 36, height: 36, borderRadius: 'var(--radius-md)', background: 'var(--color-surface-hover)', animation: 'shimmer 1.5s infinite' }} />
        <div style={{ height: 12, width: '60%', background: 'var(--color-surface-hover)', borderRadius: 4, animation: 'shimmer 1.5s infinite' }} />
        <div style={{ height: 20, width: '40%', background: 'var(--color-surface-hover)', borderRadius: 4, animation: 'shimmer 1.5s infinite' }} />
      </div>
    );
  }

  const title = metric.title ?? metric.label ?? '';
  const color = metric.color ?? 'var(--color-primary)';

  return (
    <div style={{ background: 'var(--color-surface)', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', padding: 16, display: 'flex', flexDirection: 'column', gap: 10 }}>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', fontWeight: 500 }}>{title}</span>
        <span style={{ width: 36, height: 36, borderRadius: 'var(--radius-md)', background: 'var(--color-primary-alpha)', color, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
          <Icon name={metric.icon} size={18} />
        </span>
      </div>
      <div style={{ fontSize: 'var(--text-h1)', fontWeight: 700, color: 'var(--color-text-primary)', lineHeight: 1.1 }}>{metric.value}</div>
      <div style={{ display: 'flex', alignItems: 'center', gap: 6, fontSize: 'var(--text-caption)' }}>
        {metric.percentage !== undefined && (
          <span style={{ display: 'inline-flex', alignItems: 'center', gap: 2, color: TREND_COLOR[metric.trend], fontWeight: 600 }}>
            <Icon name={TREND_ICON[metric.trend]} size={12} /> {metric.percentage > 0 ? '+' : ''}{metric.percentage}%
          </span>
        )}
        {metric.comparison && <span style={{ color: 'var(--color-text-tertiary)' }}>{metric.comparison}</span>}
      </div>
    </div>
  );
});


