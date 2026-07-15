import { memo } from 'react';
import { KPICard } from './KPICard';
import type { KPIData } from '../types';

interface KPIGridProps {
  kpis: KPIData[];
  columns?: number;
}

export const KPIGrid = memo(function KPIGrid({ kpis, columns = 4 }: KPIGridProps) {
  return (
    <div
      style={{
        display: 'grid',
        gridTemplateColumns: `repeat(${columns}, 1fr)`,
        gap: 'var(--space-component-gap)',
      }}
      className="dg-kpi-grid"
    >
      {kpis.map((kpi) => (
        <KPICard key={kpi.id} kpi={kpi} />
      ))}
    </div>
  );
});
