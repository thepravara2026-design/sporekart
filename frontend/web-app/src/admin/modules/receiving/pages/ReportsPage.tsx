import { memo } from 'react';
import { useReceivingData } from '../hooks/useReceivingData';

const REPORT_TABS = ['Receiving Summary', 'Inspection Report', 'Acceptance Report', 'Rejection Report', 'Warehouse Report', 'Batch Assignment Report', 'Audit Report', 'Supplier Report'];

export const ReportsPage = memo(function ReportsPage() {
  const { receipts, inspections, allocations, auditRecords } = useReceivingData();

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 8 }}>
        <h3 style={{ margin: 0, fontSize: 'var(--text-h3, 18px)', fontWeight: 700 }}>Reports</h3>
        <button style={{ padding: '6px 14px', border: 'none', borderRadius: 'var(--radius-sm)', background: 'var(--color-primary)', color: '#fff', cursor: 'pointer', fontSize: 'var(--text-caption)', fontWeight: 600 }}>Generate Report</button>
      </div>
      <div style={{ display: 'flex', gap: 6, flexWrap: 'wrap' }}>
        {REPORT_TABS.map((tab) => (
          <button key={tab} style={{ padding: '6px 14px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-badge)', background: 'var(--color-surface)', cursor: 'pointer', fontSize: 'var(--text-caption)' }}>{tab}</button>
        ))}
      </div>
      <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 20, display: 'flex', flexDirection: 'column', gap: 16 }}>
        <h4 style={{ margin: 0, fontSize: 'var(--text-h4, 16px)', fontWeight: 600 }}>Data Summary</h4>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(180px, 1fr))', gap: 12 }}>
          <div><span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Receipts</span><div style={{ fontWeight: 700, fontSize: 'var(--text-body)' }}>{receipts.length}</div></div>
          <div><span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Inspections</span><div style={{ fontWeight: 700, fontSize: 'var(--text-body)' }}>{inspections.length}</div></div>
          <div><span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Allocations</span><div style={{ fontWeight: 700, fontSize: 'var(--text-body)' }}>{allocations.length}</div></div>
          <div><span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Audit Records</span><div style={{ fontWeight: 700, fontSize: 'var(--text-body)' }}>{auditRecords.length}</div></div>
        </div>
      </div>
    </div>
  );
});
