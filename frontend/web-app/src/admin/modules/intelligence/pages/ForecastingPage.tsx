import { memo } from 'react';
import { useIntelligenceData } from '../hooks';
import { ChartContainer } from '../components/ChartContainer';
import { BarChart } from '../components/BarChart';
import { LineChart } from '../components/LineChart';

export const ForecastingPage = memo(function ForecastingPage() {
  const { forecast, loading } = useIntelligenceData();
  if (loading) return <div>Loading...</div>;
  const periodData = forecast.months.map((m, i) => ({ label: m, value: forecast.demandForecast[i] }));
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 24 }}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2, 20px)', fontWeight: 700 }}>Forecasting</h2>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(140px, 1fr))', gap: 12 }}>
        {periodData.slice(0, 6).map((f) => (
          <div key={f.label} style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
            <div style={{ fontWeight: 600, fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{f.label}</div>
            <div style={{ fontSize: 'var(--text-h2, 26px)', fontWeight: 700 }}>{f.value.toLocaleString()}</div>
          </div>
        ))}
      </div>
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16 }}>
        <ChartContainer title="Demand Forecast" subtitle="Next 12 months">
          <LineChart data={periodData} height={220} />
        </ChartContainer>
        <ChartContainer title="Forecast by Period" subtitle="Bar view">
          <BarChart data={periodData} height={220} />
        </ChartContainer>
      </div>
    </div>
  );
});
