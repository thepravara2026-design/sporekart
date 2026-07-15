import { memo } from 'react';
import { Icon } from '../../../design-system/icons/Icon';
import type { KPIData } from '../types';

interface KPICardProps {
  kpi: KPIData;
}

export const KPICard = memo(function KPICard({ kpi }: KPICardProps) {
  if (kpi.loading) {
    return (
      <div
        style={{
          background: 'var(--color-surface)',
          border: '1px solid var(--color-border)',
          borderRadius: 'var(--radius-lg)',
          padding: '20px 24px',
        }}
      >
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: 16 }}>
          <div style={{ width: 80, height: 14, background: 'var(--color-surface-hover)', borderRadius: 4, animation: 'shimmer 1.5s infinite' }} />
          <div style={{ width: 36, height: 36, background: 'var(--color-surface-hover)', borderRadius: 'var(--radius-md)', animation: 'shimmer 1.5s infinite' }} />
        </div>
        <div style={{ width: 120, height: 28, background: 'var(--color-surface-hover)', borderRadius: 4, marginBottom: 8, animation: 'shimmer 1.5s infinite' }} />
        <div style={{ width: 100, height: 12, background: 'var(--color-surface-hover)', borderRadius: 4, animation: 'shimmer 1.5s infinite' }} />
      </div>
    );
  }

  const trendIcon = kpi.trend === 'up' ? 'trending-up' : kpi.trend === 'down' ? 'trending-down' : 'minus';
  const trendColor = kpi.trend === 'up' ? 'var(--color-success)' : kpi.trend === 'down' ? 'var(--color-error)' : 'var(--color-text-tertiary)';

  return (
    <div
      style={{
        background: 'var(--color-surface)',
        border: '1px solid var(--color-border)',
        borderRadius: 'var(--radius-lg)',
        padding: '20px 24px',
        transition: 'box-shadow 0.2s, transform 0.2s',
      }}
    >
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: 16 }}>
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', fontWeight: 500, textTransform: 'uppercase', letterSpacing: '0.05em' }}>
          {kpi.title}
        </span>
        <div
          style={{
            width: 36,
            height: 36,
            borderRadius: 'var(--radius-md)',
            background: `${kpi.color}1A`,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            color: kpi.color,
          }}
        >
          <Icon name={kpi.icon} size={18} />
        </div>
      </div>
      <div style={{ fontSize: 'var(--text-h2)', fontWeight: 700, color: 'var(--color-text-primary)', marginBottom: 8, lineHeight: 1 }}>
        {kpi.value}
      </div>
      <div style={{ display: 'flex', alignItems: 'center', gap: 6 }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 2, color: trendColor }}>
          <Icon name={trendIcon} size={14} />
          <span style={{ fontWeight: 600, fontSize: 'var(--text-body)' }}>
            {kpi.percentage > 0 ? '+' : ''}{kpi.percentage}%
          </span>
        </div>
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
          {kpi.comparison}
        </span>
      </div>
    </div>
  );
});
