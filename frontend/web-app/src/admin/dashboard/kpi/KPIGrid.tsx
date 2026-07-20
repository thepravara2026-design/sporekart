import { memo } from 'react';
import { KPICard } from './KPICard';
import type { KPIData } from '../types';

interface KPIGridProps {
  kpis: KPIData[];
  columns?: number;
}

export const KPIGrid = memo(function KPIGrid({ kpis, columns = 4 }: KPIGridProps) {
  /**
   * BUG-COMP-001 / BUG-MOB-001: the grid previously used a fixed
   * `repeat(columns, 1fr)` inline style, so on narrow viewports the KPI cards
   * overflowed or squashed instead of reflowing. The responsive behaviour now
   * lives in CSS (`.dg-kpi-grid`) with a fluid auto-fit column track and an
   * explicit single-column collapse on mobile.
   */
  return (
    <div
      className="dg-kpi-grid"
      data-columns={columns}
    >
      {kpis.map((kpi) => (
        <KPICard key={kpi.id} kpi={kpi} />
      ))}
    </div>
  );
});
