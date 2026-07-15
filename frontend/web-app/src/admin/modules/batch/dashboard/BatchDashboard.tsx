import { memo } from 'react';
import { SectionHeader } from '../../inventory/components';
import { useBatchData } from '../hooks/useBatchData';
import { BatchSummaryCards } from '../components/BatchSummaryCards';
import { MOCK_BATCH_METRICS, MOCK_QUICK_ACTIONS, MOCK_RECENT_ACTIVITY } from '../constants';

export const BatchDashboard = memo(function BatchDashboard() {
  const { analytics } = useBatchData();

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap, 16px)' }}>
      <SectionHeader title="Batch Management Overview" description="Enterprise Batch, Lot & Expiry Management Platform" />

      <BatchSummaryCards metrics={MOCK_BATCH_METRICS} />

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(240px, 1fr))', gap: 'var(--space-component-gap, 16px)' }}>
        <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
          {[
            { label: 'Total Batches', value: analytics.totalBatches },
            { label: 'Total Lots', value: analytics.totalLots },
            { label: 'Near Expiry', value: analytics.nearExpiry },
            { label: 'Expired', value: analytics.expired },
            { label: 'Approved', value: analytics.approved },
            { label: 'Rejected', value: analytics.rejected },
          ].map((m) => (
            <div key={m.label} style={{ display: 'flex', justifyContent: 'space-between', padding: '8px 0', borderBottom: '1px solid var(--color-border)', fontSize: 'var(--text-body)' }}>
              <span style={{ color: 'var(--color-text-secondary)' }}>{m.label}</span>
              <span style={{ fontWeight: 600, color: 'var(--color-text-primary)' }}>{m.value}</span>
            </div>
          ))}
        </div>

        <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
          <h3 style={{ margin: '0 0 12px', fontSize: 'var(--text-h3, 16px)', color: 'var(--color-text-primary)' }}>Quick Actions</h3>
          {MOCK_QUICK_ACTIONS.map((a) => (
            <div key={a.id} style={{ display: 'flex', alignItems: 'center', gap: 12, padding: '10px 12px', background: 'var(--color-surface-hover)', borderRadius: 'var(--radius-md)', marginBottom: 8, cursor: 'pointer' }}>
              <div style={{ fontWeight: 500, fontSize: 'var(--text-body)' }}>{a.label}</div>
              <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', marginLeft: 'auto' }}>{a.description}</div>
            </div>
          ))}
        </div>

        <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
          <h3 style={{ margin: '0 0 12px', fontSize: 'var(--text-h3, 16px)', color: 'var(--color-text-primary)' }}>Warehouse Distribution</h3>
          {analytics.warehouseDistribution.map((w) => (
            <div key={w.warehouse} style={{ display: 'flex', justifyContent: 'space-between', padding: '6px 0', borderBottom: '1px solid var(--color-border)', fontSize: 'var(--text-body, 14px)' }}>
              <span style={{ color: 'var(--color-text-secondary)' }}>{w.warehouse}</span>
              <span style={{ fontWeight: 600, color: 'var(--color-text-primary)' }}>{w.count} batches</span>
            </div>
          ))}
        </div>

        <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
          <h3 style={{ margin: '0 0 12px', fontSize: 'var(--text-h3, 16px)', color: 'var(--color-text-primary)' }}>Recent Activity</h3>
          {MOCK_RECENT_ACTIVITY.map((a) => (
            <div key={a.id} style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', padding: '6px 0', borderBottom: '1px solid var(--color-border)' }}>
              <strong>{a.action}</strong> {a.detail}
              <div>{new Date(a.timestamp).toLocaleDateString()}</div>
            </div>
          ))}
        </div>
      </div>

      <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
        <h3 style={{ margin: '0 0 12px', fontSize: 'var(--text-h3, 16px)', color: 'var(--color-text-primary)' }}>Quality Summary</h3>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(140px, 1fr))', gap: 8 }}>
          {analytics.qualitySummary.map((q) => (
            <div key={q.status} style={{ padding: '8px 12px', background: 'var(--color-surface-hover)', borderRadius: 'var(--radius-md)', fontSize: 'var(--text-body, 14px)' }}>
              <span style={{ color: 'var(--color-text-secondary)' }}>{q.status.replace(/_/g, ' ')}</span>
              <div style={{ fontWeight: 700, fontSize: 'var(--text-h3, 16px)', color: 'var(--color-text-primary)' }}>{q.count}</div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
});
