import { memo } from 'react';
import { useIntelligenceData } from '../hooks';
import { ChartContainer } from '../components/ChartContainer';
import { BarChart } from '../components/BarChart';
import { DonutChart } from '../components/DonutChart';

const STATUS_COLOR: Record<string, string> = { available: 'var(--color-success)', reserved: 'var(--color-info)', incoming: 'var(--color-primary)', blocked: 'var(--color-neutral)', damaged: 'var(--color-warning)', expired: 'var(--color-danger)' };

export const StockHealthPage = memo(function StockHealthPage() {
  const { stock, loading } = useIntelligenceData();
  if (loading) return <div>Loading...</div>;
  const stockDist = Object.entries(STATUS_COLOR).map(([k, color]) => ({ label: k.replace(/^./, (s) => s.toUpperCase()), value: (stock as unknown as Record<string, number>)[k] || 0, color }));
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 24 }}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2, 20px)', fontWeight: 700 }}>Stock Health Analytics</h2>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))', gap: 12 }}>
        {stockDist.map((s) => (
          <div key={s.label} style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
            <div style={{ fontWeight: 600, fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{s.label}</div>
            <div style={{ fontSize: 'var(--text-h2, 26px)', fontWeight: 700 }}>{s.value}</div>
          </div>
        ))}
      </div>
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16 }}>
        <ChartContainer title="Stock Levels" subtitle="By status">
          <BarChart data={stockDist} height={220} />
        </ChartContainer>
        <ChartContainer title="Stock Health" subtitle="Distribution">
          <DonutChart data={stockDist} size={160} />
        </ChartContainer>
      </div>
    </div>
  );
});
