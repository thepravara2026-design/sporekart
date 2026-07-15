import { memo } from 'react';
import { useIntelligenceData } from '../hooks';
import { ChartContainer } from '../components/ChartContainer';
import { BarChart } from '../components/BarChart';

export const ReceivingAnalyticsPage = memo(function ReceivingAnalyticsPage() {
  const { movement, loading } = useIntelligenceData();
  if (loading) return <div>Loading...</div>;
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 24 }}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2, 20px)', fontWeight: 700 }}>Receiving Analytics</h2>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))', gap: 12 }}>
        <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
          <div style={{ fontWeight: 600, fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Total Receipts</div>
          <div style={{ fontSize: 'var(--text-h2, 26px)', fontWeight: 700 }}>{movement.totalReceipts}</div>
        </div>
        <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
          <div style={{ fontWeight: 600, fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Transfers</div>
          <div style={{ fontSize: 'var(--text-h2, 26px)', fontWeight: 700 }}>{movement.totalTransfers}</div>
        </div>
        <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
          <div style={{ fontWeight: 600, fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Returns</div>
          <div style={{ fontSize: 'var(--text-h2, 26px)', fontWeight: 700 }}>{movement.totalReturns}</div>
        </div>
      </div>
      <ChartContainer title="Receiving Volume by Warehouse" subtitle="Movement count">
        <BarChart data={movement.byWarehouse.slice(0, 6).map((w) => ({ label: w.warehouse.split('-')[0].trim(), value: w.count }))} height={220} />
      </ChartContainer>
    </div>
  );
});
