import { memo } from 'react';
import { useIntelligenceData } from '../hooks';
import { ChartContainer } from '../components/ChartContainer';
import { BarChart } from '../components/BarChart';
import { DonutChart } from '../components/DonutChart';

export const ReportsPage = memo(function ReportsPage() {
  const { warehouse, stock, loading } = useIntelligenceData();
  if (loading) return <div>Loading...</div>;
  const stockDist = [
    { label: 'Available', value: stock.available, color: 'var(--color-success)' },
    { label: 'Reserved', value: stock.reserved, color: 'var(--color-info)' },
    { label: 'Incoming', value: stock.incoming, color: 'var(--color-primary)' },
    { label: 'Blocked', value: stock.blocked, color: 'var(--color-neutral)' },
    { label: 'Damaged', value: stock.damaged, color: 'var(--color-warning)' },
    { label: 'Expired', value: stock.expired, color: 'var(--color-danger)' },
  ];
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 24 }}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2, 20px)', fontWeight: 700 }}>Reports</h2>
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16 }}>
        <ChartContainer title="Warehouse Utilization Report" subtitle="Current period">
          <BarChart data={warehouse.topWarehouses.map((w) => ({ label: w.name.split('-')[0].trim(), value: w.utilization }))} height={220} />
        </ChartContainer>
        <ChartContainer title="Stock Level Report" subtitle="By status">
          <BarChart data={stockDist} height={220} />
        </ChartContainer>
      </div>
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16 }}>
        <ChartContainer title="Stock Distribution" subtitle="By category">
          <DonutChart data={stockDist} size={160} />
        </ChartContainer>
      </div>
    </div>
  );
});
