import { memo } from 'react';
import { useIntelligenceData } from '../hooks';
import { ChartContainer } from '../components/ChartContainer';
import { BarChart } from '../components/BarChart';

export const MovementAnalyticsPage = memo(function MovementAnalyticsPage() {
  const { movement, loading } = useIntelligenceData();
  if (loading) return <div>Loading...</div>;
  const types = [
    { type: 'Transfers', count: movement.totalTransfers },
    { type: 'Receipts', count: movement.totalReceipts },
    { type: 'Issues', count: movement.totalIssues },
    { type: 'Adjustments', count: movement.totalAdjustments },
    { type: 'Returns', count: movement.totalReturns },
  ];
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 24 }}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2, 20px)', fontWeight: 700 }}>Movement Analytics</h2>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(180px, 1fr))', gap: 12 }}>
        {types.map((t) => (
          <div key={t.type} style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
            <div style={{ fontWeight: 600, fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{t.type}</div>
            <div style={{ fontSize: 'var(--text-h2, 26px)', fontWeight: 700 }}>{t.count}</div>
          </div>
        ))}
        <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
          <div style={{ fontWeight: 600, fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Daily Avg</div>
          <div style={{ fontSize: 'var(--text-h2, 26px)', fontWeight: 700 }}>{movement.dailyAvg}</div>
        </div>
        <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
          <div style={{ fontWeight: 600, fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Monthly Total</div>
          <div style={{ fontSize: 'var(--text-h2, 26px)', fontWeight: 700 }}>{movement.monthlyTotal}</div>
        </div>
      </div>
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16 }}>
        <ChartContainer title="Movement Volume" subtitle="By type">
          <BarChart data={types.map((t) => ({ label: t.type, value: t.count }))} height={220} />
        </ChartContainer>
        <ChartContainer title="By Warehouse" subtitle="Movement count">
          <BarChart data={movement.byWarehouse.map((w) => ({ label: w.warehouse.split('-')[0].trim(), value: w.count }))} height={220} />
        </ChartContainer>
      </div>
    </div>
  );
});
