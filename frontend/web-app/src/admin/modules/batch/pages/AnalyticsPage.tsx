import { memo } from 'react';
import { SectionHeader } from '../../inventory/components';
import { useBatchData } from '../hooks/useBatchData';
import { BatchSummaryCards } from '../components/BatchSummaryCards';
import { MOCK_BATCH_METRICS } from '../constants';

export const AnalyticsPage = memo(function AnalyticsPage() {
  const { analytics } = useBatchData();

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap, 16px)' }}>
      <SectionHeader title="Analytics" description="Batch analytics and distribution insights." />
      <BatchSummaryCards metrics={MOCK_BATCH_METRICS} />
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: 16 }}>
        <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
          <h3 style={{ margin: '0 0 12px', fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)' }}>Warehouse Distribution</h3>
          {analytics.warehouseDistribution.map((w) => (
            <div key={w.warehouse} style={{ display: 'flex', justifyContent: 'space-between', padding: '6px 0', borderBottom: '1px solid var(--color-border)', fontSize: 'var(--text-body)' }}>
              <span style={{ color: 'var(--color-text-secondary)' }}>{w.warehouse}</span>
              <span style={{ fontWeight: 600, color: 'var(--color-text-primary)' }}>{w.count}</span>
            </div>
          ))}
        </div>
        <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
          <h3 style={{ margin: '0 0 12px', fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)' }}>Product Distribution</h3>
          {analytics.productDistribution.slice(0, 8).map((p) => (
            <div key={p.product} style={{ display: 'flex', justifyContent: 'space-between', padding: '6px 0', borderBottom: '1px solid var(--color-border)', fontSize: 'var(--text-body)' }}>
              <span style={{ color: 'var(--color-text-secondary)' }}>{p.product}</span>
              <span style={{ fontWeight: 600, color: 'var(--color-text-primary)' }}>{p.count}</span>
            </div>
          ))}
        </div>
        <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
          <h3 style={{ margin: '0 0 12px', fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)' }}>Quality Summary</h3>
          {analytics.qualitySummary.map((q) => (
            <div key={q.status} style={{ display: 'flex', justifyContent: 'space-between', padding: '6px 0', borderBottom: '1px solid var(--color-border)', fontSize: 'var(--text-body)' }}>
              <span style={{ color: 'var(--color-text-secondary)' }}>{q.status.replace(/_/g, ' ')}</span>
              <span style={{ fontWeight: 600, color: 'var(--color-text-primary)' }}>{q.count}</span>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
});

