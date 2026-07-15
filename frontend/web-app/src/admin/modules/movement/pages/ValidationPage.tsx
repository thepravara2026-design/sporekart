const VALIDATION_RULES: { id: string; name: string; status: 'passed' | 'warning' | 'failed'; description: string }[] = [
  { id: 'v1', name: 'Quantity vs. Batch Balance', status: 'passed', description: 'All transaction quantities match batch remaining quantities' },
  { id: 'v2', name: 'Duplicate Reference Check', status: 'passed', description: 'No duplicate reference numbers found across 65 transactions' },
  { id: 'v3', name: 'Warehouse Existence', status: 'passed', description: 'All warehouse references correspond to active warehouses' },
  { id: 'v4', name: 'Status Transition Validity', status: 'warning', description: '3 transactions missing intermediate "in_transit" status' },
  { id: 'v5', name: 'Date Chronology', status: 'passed', description: 'All timestamps are in chronological order per entity' },
  { id: 'v6', name: 'Quantity Non-Negative', status: 'failed', description: 'Adjustment REF-ADJ-003 has negative delta and requires review' },
  { id: 'v7', name: 'User Authorization', status: 'passed', description: 'All actions performed by authorized roles' },
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
