import { memo } from 'react';
import { useIntelligenceData } from '../hooks';
import { HealthScoreGrid } from '../components/HealthScore';
import { ChartContainer } from '../components/ChartContainer';
import { BarChart } from '../components/BarChart';
import { DonutChart } from '../components/DonutChart';
import { generateDistributionData } from '../utils';

const EXPIRY_STATUS_COLORS: Record<string, string> = { fresh: 'var(--color-success)', healthy: 'var(--color-info)', monitor: 'var(--color-warning)', nearExpiry: 'var(--color-warning)', critical: 'var(--color-danger)', expired: 'var(--color-danger)', blocked: 'var(--color-neutral)', disposed: 'var(--color-neutral)' };

export const InventoryHealthPage = memo(function InventoryHealthPage() {
  const { healthMetrics, expiry, loading } = useIntelligenceData();
  if (loading) return <div>Loading...</div>;
  const dist = generateDistributionData(healthMetrics);
  const expiryDist = Object.entries(expiry).filter(([k]) => k in EXPIRY_STATUS_COLORS).map(([k, v]) => ({ label: k.replace(/([A-Z])/g, ' $1').replace(/^./, (s) => s.toUpperCase()), value: v as number, color: EXPIRY_STATUS_COLORS[k] }));
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 24 }}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2, 20px)', fontWeight: 700 }}>Inventory Health</h2>
      <HealthScoreGrid metrics={healthMetrics} />
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16 }}>
        <ChartContainer title="Health Distribution" subtitle="By category">
          <BarChart data={dist.map((d) => ({ label: d.label, value: d.value, color: d.color }))} height={200} />
        </ChartContainer>
        <ChartContainer title="Expiry Risk Breakdown" subtitle="By status">
          <DonutChart data={expiryDist} size={160} />
        </ChartContainer>
      </div>
    </div>
  );
});
