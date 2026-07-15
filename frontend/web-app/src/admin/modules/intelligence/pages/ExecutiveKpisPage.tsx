import { memo } from 'react';
import { useIntelligenceData } from '../hooks';
import { MetricCardGrid } from '../components/MetricCard';
import { ChartContainer } from '../components/ChartContainer';
import { BarChart } from '../components/BarChart';
import { LineChart } from '../components/LineChart';

export const ExecutiveKpisPage = memo(function ExecutiveKpisPage() {
  const { kpiMetrics, loading } = useIntelligenceData();
  if (loading) return <div>Loading...</div>;
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 24 }}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2, 20px)', fontWeight: 700 }}>Executive KPIs</h2>
      <MetricCardGrid metrics={kpiMetrics} />
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16 }}>
        <ChartContainer title="KPI Metrics Overview" subtitle="Current values">
          <BarChart data={kpiMetrics.map((m) => ({ label: m.label, value: parseInt(m.value.replace(/[^0-9]/g, '')) || 0, color: 'var(--color-primary)' }))} height={200} />
        </ChartContainer>
        <ChartContainer title="Performance Trend" subtitle="Rolling">
          <LineChart data={kpiMetrics.map((m) => ({ label: m.label, value: parseInt(m.value.replace(/[^0-9]/g, '')) || 0 }))} height={200} />
        </ChartContainer>
      </div>
    </div>
  );
});
