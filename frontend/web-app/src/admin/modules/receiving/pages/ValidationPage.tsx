const VALIDATION_RULES: { id: string; name: string; status: 'passed' | 'warning' | 'failed'; description: string }[] = [
  { id: 'v1', name: 'Duplicate Receipt Check', status: 'passed', description: 'No duplicate receipt numbers found across 45 receipts' },
  { id: 'v2', name: 'Missing Warehouse', status: 'passed', description: 'All receipts have assigned warehouses' },
  { id: 'v3', name: 'Missing Product', status: 'passed', description: 'All receipts have valid product references' },
  { id: 'v4', name: 'Missing Inventory Item', status: 'passed', description: 'All receipts linked to inventory items' },
  { id: 'v5', name: 'Missing Inspection', status: 'warning', description: '3 completed receipts missing inspection records' },
  { id: 'v6', name: 'Missing Acceptance Decision', status: 'warning', description: '2 approved receipts missing acceptance decision' },
  { id: 'v7', name: 'Missing Batch Assignment', status: 'failed', description: '8 completed receipts lack batch assignment' },
  { id: 'v8', name: 'Missing Allocation', status: 'warning', description: '5 completed receipts lack warehouse allocation' },
  { id: 'v9', name: 'Invalid Status Transition', status: 'passed', description: 'All status transitions are valid' },
];

import { memo } from 'react';

export const ValidationPage = memo(function ValidationPage() {
  const passed = VALIDATION_RULES.filter((r) => r.status === 'passed').length;
  const warnings = VALIDATION_RULES.filter((r) => r.status === 'warning').length;
  const failed = VALIDATION_RULES.filter((r) => r.status === 'failed').length;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: 24, flexWrap: 'wrap' }}>
        <h3 style={{ margin: 0, fontSize: 'var(--text-h3, 18px)', fontWeight: 700 }}>Validation</h3>
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-success)' }}>{passed} passed</span>
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-warning)' }}>{warnings} warning(s)</span>
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-danger)' }}>{failed} failed</span>
        <button style={{ marginLeft: 'auto', padding: '6px 14px', border: 'none', borderRadius: 'var(--radius-sm)', background: 'var(--color-primary)', color: '#fff', cursor: 'pointer', fontSize: 'var(--text-caption)', fontWeight: 600 }}>Run Validation</button>
      </div>
      <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)' }}>
        {VALIDATION_RULES.map((rule) => {
          const color = rule.status === 'passed' ? 'var(--color-success)' : rule.status === 'warning' ? 'var(--color-warning)' : 'var(--color-danger)';
          const icon = rule.status === 'passed' ? '\u2713' : rule.status === 'warning' ? '\u26A0' : '\u2717';
          return (
            <div key={rule.id} style={{ display: 'flex', alignItems: 'center', gap: 12, padding: '12px 16px', borderBottom: '1px solid var(--color-border)' }}>
              <span style={{ color, fontWeight: 700, fontSize: 16 }}>{icon}</span>
              <div style={{ flex: 1 }}>
                <div style={{ fontWeight: 600, fontSize: 'var(--text-body)' }}>{rule.name}</div>
                <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{rule.description}</div>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
});
