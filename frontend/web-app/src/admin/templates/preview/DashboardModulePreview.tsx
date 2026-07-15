import { memo, type CSSProperties } from 'react';
import { ExampleKpiWidget } from '../WidgetTemplate';
import type { KPIData } from '../../dashboard/types';

const sampleKpis: KPIData[] = [
  { id: 'k1', title: 'Total Revenue', value: '$48.2k', trend: 'up', percentage: 12.5, comparison: 'vs last month', icon: 'trending-up', color: '#2f6f4f' },
  { id: 'k2', title: 'Active Users', value: '1,284', trend: 'up', percentage: 4.1, comparison: 'vs last week', icon: 'users', color: '#1d9bf0' },
  { id: 'k3', title: 'Pending Orders', value: '37', trend: 'down', percentage: 8.0, comparison: 'vs last week', icon: 'clipboard', color: '#d97706' },
  { id: 'k4', title: 'Errors', value: '3', trend: 'neutral', percentage: 0, comparison: 'vs last day', icon: 'alert-triangle', color: '#7c3aed' },
];

export const DashboardModulePreview = memo(function DashboardModulePreview() {
  const style: CSSProperties = { display: 'flex', flexDirection: 'column', gap: 16, padding: 24 };
  return (
    <div style={style}>
      <h1 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Dashboard Module</h1>
      <ExampleKpiWidget kpis={sampleKpis} />
    </div>
  );
});
