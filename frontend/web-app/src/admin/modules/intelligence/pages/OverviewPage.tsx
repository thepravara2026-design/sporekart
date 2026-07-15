import { memo } from 'react';
import { useIntelligenceData } from '../hooks';
import { MetricCardGrid } from '../components/MetricCard';
import { HealthScoreGrid } from '../components/HealthScore';
import type { KpiMetric } from '../types';

export const OverviewPage = memo(function OverviewPage() {
  const { executiveKpis, healthMetrics, loading } = useIntelligenceData();
  if (loading) return <div>Loading...</div>;
  const kpis: KpiMetric[] = [
    { label: 'Inventory Items', value: String(executiveKpis.inventoryItems), trend: 'up', variant: 'success', subtitle: '+12% vs last month' },
    { label: 'Warehouses', value: String(executiveKpis.warehouses), trend: 'neutral', variant: 'info', subtitle: '3 active, 2 standby' },
    { label: 'Stock Records', value: String(executiveKpis.stockRecords), trend: 'up', variant: 'success', subtitle: '+8% this quarter' },
    { label: 'Inventory Value', value: executiveKpis.inventoryValue, trend: 'up', variant: 'info', subtitle: 'Estimated value' },
    { label: 'Near Expiry', value: String(executiveKpis.nearExpiry), trend: 'up', variant: 'warning', subtitle: 'Within 30 days' },
    { label: 'Expired', value: String(executiveKpis.expiredCount), trend: 'down', variant: 'danger', subtitle: '-3 from last week' },
    { label: 'Batch Count', value: String(executiveKpis.batchCount), trend: 'up', variant: 'success', subtitle: 'Across all products' },
    { label: 'Warehouse Capacity', value: `${executiveKpis.warehouseCapacity}%`, trend: 'up', variant: 'warning', subtitle: `${100 - executiveKpis.warehouseCapacity}% remaining` },
  ];
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 24 }}>
      <div>
        <h2 style={{ margin: '0 0 8px', fontSize: 'var(--text-h2, 20px)', fontWeight: 700 }}>Executive KPIs</h2>
        <p style={{ margin: 0, color: 'var(--color-text-secondary)' }}>High-level operational metrics at a glance</p>
      </div>
      <MetricCardGrid metrics={kpis} />
      <div>
        <h3 style={{ margin: '0 0 12px', fontSize: 'var(--text-h3, 18px)', fontWeight: 600 }}>Inventory Health</h3>
      </div>
      <HealthScoreGrid metrics={healthMetrics} />
    </div>
  );
});
