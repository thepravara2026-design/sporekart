import { memo } from 'react';
import { useMovementData } from '../hooks/useMovementData';

const REPORT_TABS = ['Movement Summary', 'Transaction Log', 'Transfer Report', 'Adjustment Report', 'Goods Receipt', 'Goods Issue', 'Audit Report', 'Analytics Report'];

export const ReportsPage = memo(function ReportsPage() {
  const { transactions, transfers, adjustments, auditRecords } = useMovementData();

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
          <div><span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Transactions</span><div style={{ fontWeight: 700, fontSize: 'var(--text-body)' }}>{transactions.length}</div></div>
          <div><span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Transfers</span><div style={{ fontWeight: 700, fontSize: 'var(--text-body)' }}>{transfers.length}</div></div>
          <div><span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Adjustments</span><div style={{ fontWeight: 700, fontSize: 'var(--text-body)' }}>{adjustments.length}</div></div>
          <div><span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Audit Records</span><div style={{ fontWeight: 700, fontSize: 'var(--text-body)' }}>{auditRecords.length}</div></div>
        </div>
      </div>
    </div>
  );
});
