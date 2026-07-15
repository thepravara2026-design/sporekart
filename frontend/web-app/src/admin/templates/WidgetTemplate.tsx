import { memo, type CSSProperties } from 'react';
import { KPIGrid } from '../dashboard/kpi/KPIGrid';
import { Icon } from '../../design-system/icons/Icon';
import type { KPIData } from '../dashboard/types';

export const ExampleKpiWidget = memo(function ExampleKpiWidget({ kpis }: { kpis: KPIData[] }) {
  return <KPIGrid kpis={kpis} columns={4} />;
});

const trendColor: Record<KPIData['trend'], string> = {
  up: '#2f6f4f',
  down: '#ef4444',
  neutral: '#1d9bf0',
};

export const ExampleStatCard = memo(function ExampleStatCard({ kpi }: { kpi: KPIData }) {
  const cardStyle: CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-2)',
    padding: 'var(--space-5)',
    borderRadius: 'var(--radius-lg)',
    backgroundColor: 'var(--color-bg-surface-default)',
    border: '1px solid var(--color-border-default)',
    borderLeft: `4px solid ${kpi.color}`,
  };
  const trendGlyph = kpi.trend === 'up' ? '▲' : kpi.trend === 'down' ? '▼' : '–';
  return (
    <div style={cardStyle}>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{kpi.title}</span>
        <Icon name={kpi.icon} size={20} color={kpi.color} />
      </div>
      <div style={{ fontSize: 'var(--text-h1)', color: 'var(--color-text-primary)', fontWeight: 'var(--weight-semibold)' }}>
        {kpi.value}
      </div>
      <div style={{ fontSize: 'var(--text-caption)', color: trendColor[kpi.trend] }}>
        {trendGlyph} {kpi.percentage}% {kpi.comparison}
      </div>
    </div>
  );
});
