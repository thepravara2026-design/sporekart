import { memo } from 'react';
import { useIntelligenceData } from '../hooks';
import { ChartContainer } from '../components/ChartContainer';
import { BarChart } from '../components/BarChart';
import { DonutChart } from '../components/DonutChart';

export const BatchHealthPage = memo(function BatchHealthPage() {
  const { batch, loading } = useIntelligenceData();
  if (loading) return <div>Loading...</div>;
  const qualityDist = [
    { label: 'Approved', value: batch.qualityApproved, color: 'var(--color-success)' },
    { label: 'Rejected', value: batch.qualityRejected, color: 'var(--color-danger)' },
    { label: 'Pending', value: batch.qualityPending, color: 'var(--color-warning)' },
  ];
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 24 }}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2, 20px)', fontWeight: 700 }}>Batch Analytics</h2>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(180px, 1fr))', gap: 12 }}>
        <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
          <div style={{ fontWeight: 600, fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Total Batches</div>
          <div style={{ fontSize: 'var(--text-h2, 26px)', fontWeight: 700 }}>{batch.totalBatches}</div>
        </div>
        <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
          <div style={{ fontWeight: 600, fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Total Lots</div>
          <div style={{ fontSize: 'var(--text-h2, 26px)', fontWeight: 700 }}>{batch.totalLots}</div>
        </div>
        <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
          <div style={{ fontWeight: 600, fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Near Expiry</div>
          <div style={{ fontSize: 'var(--text-h2, 26px)', fontWeight: 700 }}>{batch.nearExpiry}</div>
        </div>
        <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
          <div style={{ fontWeight: 600, fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Traceability</div>
          <div style={{ fontSize: 'var(--text-h2, 26px)', fontWeight: 700 }}>{batch.traceabilityCoverage}%</div>
        </div>
      </div>
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16 }}>
        <ChartContainer title="Quality Distribution" subtitle="By status">
          <BarChart data={qualityDist} height={220} />
        </ChartContainer>
        <ChartContainer title="Quality Breakdown" subtitle="By status">
          <DonutChart data={qualityDist} size={160} />
        </ChartContainer>
      </div>
    </div>
  );
});
