import { memo } from 'react';
import { useBatchData } from '../hooks/useBatchData';
import { BatchSummaryCards } from '../components/BatchSummaryCards';
import { MOCK_BATCH_METRICS, MOCK_QUICK_ACTIONS, MOCK_RECENT_ACTIVITY } from '../constants';
import { SectionHeader } from '../../inventory/components';

export const BatchOverviewPage = memo(function BatchOverviewPage() {
  const { analytics } = useBatchData();
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap, 16px)' }}>
      <SectionHeader title="Overview" description="Batch domain overview and quick access." />
      <BatchSummaryCards metrics={MOCK_BATCH_METRICS} />
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: 'var(--space-component-gap, 16px)' }}>
        <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
          <h3 style={{ margin: '0 0 12px', fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)' }}>Quick Actions</h3>
          {MOCK_QUICK_ACTIONS.map((a) => (
            <div key={a.id} style={{ display: 'flex', alignItems: 'center', gap: 12, padding: '10px 12px', background: 'var(--color-surface-hover)', borderRadius: 'var(--radius-md)', marginBottom: 8, cursor: 'pointer' }}>
              <div>{a.label}</div>
              <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{a.description}</div>
            </div>
          ))}
        </div>
        <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
          <h3 style={{ margin: '0 0 12px', fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)' }}>Recent Activity</h3>
          {MOCK_RECENT_ACTIVITY.map((a) => (
            <div key={a.id} style={{ display: 'flex', justifyContent: 'space-between', padding: '8px 0', borderBottom: '1px solid var(--color-border)', fontSize: 'var(--text-body)' }}>
              <div><strong>{a.action}</strong>: {a.detail}</div>
              <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{new Date(a.timestamp).toLocaleDateString()}</div>
            </div>
          ))}
        </div>
      </div>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))', gap: 8 }}>
        {analytics.warehouseDistribution.map((w) => (
          <div key={w.warehouse} style={{ padding: 12, background: 'var(--color-surface)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', fontSize: 'var(--text-body)' }}>
            <span style={{ color: 'var(--color-text-secondary)' }}>{w.warehouse}</span>
            <div style={{ fontWeight: 600, color: 'var(--color-text-primary)' }}>{w.count} batches</div>
          </div>
        ))}
      </div>
    </div>
  );
});
