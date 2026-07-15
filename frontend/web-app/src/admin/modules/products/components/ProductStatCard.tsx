import React from 'react';
import { Card } from '../../../../design-system/components/composite/Card';
import { Icon } from '../../../../design-system/icons/Icon';
import type { KPIData } from '../../../dashboard/types';

interface ProductStatCardProps {
  kpi: KPIData;
}

const trendColor: Record<KPIData['trend'], string> = {
  up: 'var(--color-success)',
  down: 'var(--color-danger)',
  neutral: 'var(--color-text-secondary)',
};

const trendIcon: Record<KPIData['trend'], string> = {
  up: 'trending-up',
  down: 'trending-down',
  neutral: 'activity',
};

export const ProductStatCard = React.memo(function ProductStatCard({ kpi }: ProductStatCardProps) {
  return (
    <Card variant="elevated" padding="md" as="article" aria-label={kpi.title} style={{ minWidth: 0 }}>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', gap: 'var(--space-inline-xs)' }}>
        <span style={{ display: 'inline-flex', alignItems: 'center', justifyContent: 'center', width: 36, height: 36, borderRadius: 'var(--radius-md)', background: kpi.color, color: '#fff' }}>
          <Icon name={kpi.icon} size={18} />
        </span>
        <span style={{ display: 'inline-flex', alignItems: 'center', gap: 4, color: trendColor[kpi.trend], fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-semibold)' }}>
          <Icon name={trendIcon[kpi.trend]} size={14} />
          {kpi.percentage > 0 ? `+${kpi.percentage}` : kpi.percentage}%
        </span>
      </div>
      <h3 style={{ fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)', margin: 'var(--space-stack-xs) 0 2px', fontWeight: 'var(--weight-bold)' }}>{kpi.value}</h3>
      <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: 0 }}>{kpi.title}</p>
      <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', margin: '2px 0 0' }}>{kpi.comparison}</p>
    </Card>
  );
});

export default ProductStatCard;
