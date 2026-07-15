import { memo, useState } from 'react';
import { SectionHeader } from '../../inventory/components';
import { useBatchData } from '../hooks/useBatchData';
import { BatchSummaryCards } from '../components/BatchSummaryCards';
import { MOCK_BATCH_METRICS } from '../constants';

const REPORT_TABS = ['Batch Summary', 'Expiry Report', 'Shelf Life Report', 'Quality Report', 'Warehouse Report', 'Product Report', 'Future Recall Report', 'Future Export Placeholder'];

export const ReportsPage = memo(function ReportsPage() {
  const [activeTab, setActiveTab] = useState('Batch Summary');
  const { batches, analytics } = useBatchData();

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap, 16px)' }}>
      <SectionHeader title="Reports" description="Batch operational reports." />
      <div style={{ display: 'flex', gap: 6, flexWrap: 'wrap', borderBottom: '1px solid var(--color-border)', paddingBottom: 8 }}>
        {REPORT_TABS.map((tab) => (
          <button key={tab} onClick={() => setActiveTab(tab)} style={{ padding: '8px 16px', borderRadius: 'var(--radius-md)', border: 'none', background: activeTab === tab ? 'var(--color-primary)' : 'transparent', color: activeTab === tab ? '#fff' : 'var(--color-text-secondary)', cursor: 'pointer', fontSize: 'var(--text-body)', fontWeight: activeTab === tab ? 600 : 400 }}>{tab}</button>
        ))}
      </div>
      <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
        {activeTab === 'Batch Summary' && (
          <div>
            <h3 style={{ margin: '0 0 12px', fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)' }}>Batch Summary Report</h3>
            <BatchSummaryCards metrics={MOCK_BATCH_METRICS} />
            <div style={{ marginTop: 16, fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)' }}>
              <p>Total Batches: {analytics.totalBatches}</p>
              <p>Total Lots: {analytics.totalLots}</p>
              <p>Approved: {analytics.approved} | Rejected: {analytics.rejected} | Pending: {analytics.pendingReview}</p>
              <p>Near Expiry: {analytics.nearExpiry} | Expired: {analytics.expired}</p>
            </div>
          </div>
        )}
        {activeTab === 'Expiry Report' && (
          <div>
            <h3 style={{ margin: '0 0 12px', fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)' }}>Expiry Report</h3>
            {batches.filter((b) => b.expiryStatus === 'near_expiry' || b.expiryStatus === 'critical' || b.expiryStatus === 'expired').map((b) => (
              <div key={b.id} style={{ display: 'flex', justifyContent: 'space-between', padding: '8px 0', borderBottom: '1px solid var(--color-border)', fontSize: 'var(--text-body)' }}>
                <span>{b.batchCode} — {b.product}</span>
                <span style={{ fontWeight: 600, color: 'var(--color-danger)' }}>{b.expiryStatus.replace(/_/g, ' ')} — {new Date(b.expiryDate).toLocaleDateString()}</span>
              </div>
            ))}
          </div>
        )}
        {activeTab === 'Shelf Life Report' && (
          <div>
            <h3 style={{ margin: '0 0 12px', fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)' }}>Shelf Life Report</h3>
            <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 'var(--text-body)' }}>
              <thead><tr style={{ borderBottom: '1px solid var(--color-border)' }}><th style={{ padding: 8, textAlign: 'left' }}>Batch</th><th style={{ padding: 8, textAlign: 'right' }}>Shelf Life</th><th style={{ padding: 8, textAlign: 'left' }}>Unit</th><th style={{ padding: 8, textAlign: 'left' }}>Expiry</th></tr></thead>
              <tbody>{batches.slice(0, 15).map((b) => (
                <tr key={b.id} style={{ borderBottom: '1px solid var(--color-border)' }}>
                  <td style={{ padding: 8 }}>{b.batchCode}</td>
                  <td style={{ padding: 8, textAlign: 'right' }}>{b.shelfLife}</td>
                  <td style={{ padding: 8 }}>{b.shelfLifeUnit}</td>
                  <td style={{ padding: 8 }}>{new Date(b.expiryDate).toLocaleDateString()}</td>
                </tr>
              ))}</tbody>
            </table>
          </div>
        )}
        {activeTab === 'Quality Report' && (
          <div>
            <h3 style={{ margin: '0 0 12px', fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)' }}>Quality Report</h3>
            {analytics.qualitySummary.map((q) => (
              <div key={q.status} style={{ display: 'flex', justifyContent: 'space-between', padding: '8px 0', borderBottom: '1px solid var(--color-border)', fontSize: 'var(--text-body)' }}>
                <span style={{ color: 'var(--color-text-secondary)' }}>{q.status.replace(/_/g, ' ')}</span>
                <span style={{ fontWeight: 600 }}>{q.count}</span>
              </div>
            ))}
          </div>
        )}
        {activeTab === 'Warehouse Report' && (
          <div>
            <h3 style={{ margin: '0 0 12px', fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)' }}>Warehouse Batch Report</h3>
            {analytics.warehouseDistribution.map((w) => (
              <div key={w.warehouse} style={{ display: 'flex', justifyContent: 'space-between', padding: '8px 0', borderBottom: '1px solid var(--color-border)', fontSize: 'var(--text-body)' }}>
                <span style={{ color: 'var(--color-text-secondary)' }}>{w.warehouse}</span>
                <span style={{ fontWeight: 600 }}>{w.count} batches</span>
              </div>
            ))}
          </div>
        )}
        {activeTab === 'Product Report' && (
          <div>
            <h3 style={{ margin: '0 0 12px', fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)' }}>Product Batch Report</h3>
            {analytics.productDistribution.map((p) => (
              <div key={p.product} style={{ display: 'flex', justifyContent: 'space-between', padding: '8px 0', borderBottom: '1px solid var(--color-border)', fontSize: 'var(--text-body)' }}>
                <span style={{ color: 'var(--color-text-secondary)' }}>{p.product}</span>
                <span style={{ fontWeight: 600 }}>{p.count} batches</span>
              </div>
            ))}
          </div>
        )}
        {activeTab === 'Future Recall Report' && (
          <div>
            <h3 style={{ margin: '0 0 12px', fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)' }}>Future Recall Report</h3>
            <p style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-tertiary)' }}>Recall report framework — ready for Sprint 25 Part 6 implementation. Will list batches flagged for recall with status, affected quantity, and disposition workflow.</p>
            <div style={{ marginTop: 16, padding: 24, border: '2px dashed var(--color-border)', borderRadius: 'var(--radius-md)', textAlign: 'center', color: 'var(--color-text-tertiary)' }}>Recall data will be available when the Recall Management workflow is implemented.</div>
          </div>
        )}
        {activeTab === 'Future Export Placeholder' && (
          <div>
            <h3 style={{ margin: '0 0 12px', fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)' }}>Future Export Placeholder</h3>
            <p style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-tertiary)' }}>Export framework for batch reports — CSV, Excel, and PDF export will be implemented in Sprint 25 Part 6.</p>
            <div style={{ display: 'flex', gap: 8, marginTop: 16 }}>
              <button disabled style={{ padding: '8px 16px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)', background: 'var(--color-surface)', cursor: 'default', opacity: 0.5, fontSize: 'var(--text-body)' }}>Export CSV (Coming Soon)</button>
              <button disabled style={{ padding: '8px 16px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)', background: 'var(--color-surface)', cursor: 'default', opacity: 0.5, fontSize: 'var(--text-body)' }}>Export Excel (Coming Soon)</button>
              <button disabled style={{ padding: '8px 16px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)', background: 'var(--color-surface)', cursor: 'default', opacity: 0.5, fontSize: 'var(--text-body)' }}>Export PDF (Coming Soon)</button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
});

