import { memo } from 'react';
import { useIntelligenceData } from '../hooks';
import { ChartContainer } from '../components/ChartContainer';
import { BarChart } from '../components/BarChart';
import { DonutChart } from '../components/DonutChart';

export const WarehouseHealthPage = memo(function WarehouseHealthPage() {
  const { warehouse, loading } = useIntelligenceData();
  if (loading) return <div>Loading...</div>;
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 24 }}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2, 20px)', fontWeight: 700 }}>Warehouse Health Analytics</h2>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))', gap: 12 }}>
        <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
          <div style={{ fontWeight: 600, marginBottom: 4 }}>Total Warehouses</div>
          <div style={{ fontSize: 'var(--text-h2, 26px)', fontWeight: 700 }}>{warehouse.totalWarehouses}</div>
        </div>
        <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
          <div style={{ fontWeight: 600, marginBottom: 4 }}>Total Zones</div>
          <div style={{ fontSize: 'var(--text-h2, 26px)', fontWeight: 700 }}>{warehouse.totalZones}</div>
        </div>
        <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
          <div style={{ fontWeight: 600, marginBottom: 4 }}>Total Bins</div>
          <div style={{ fontSize: 'var(--text-h2, 26px)', fontWeight: 700 }}>{warehouse.totalBins}</div>
        </div>
        <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
          <div style={{ fontWeight: 600, marginBottom: 4 }}>Utilization Rate</div>
          <div style={{ fontSize: 'var(--text-h2, 26px)', fontWeight: 700 }}>{warehouse.utilizationRate}%</div>
        </div>
      </div>
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16 }}>
        <ChartContainer title="Warehouse Utilization" subtitle="By warehouse">
          <BarChart data={warehouse.topWarehouses.map((w) => ({ label: w.name.split('-')[0].trim(), value: w.utilization }))} height={220} />
        </ChartContainer>
        <ChartContainer title="Item Distribution" subtitle="By warehouse">
          <DonutChart data={warehouse.topWarehouses.map((w) => ({ label: w.name.split('-')[0].trim(), value: w.items, color: '' }))} size={160} />
        </ChartContainer>
      </div>
    </div>
  );
});
