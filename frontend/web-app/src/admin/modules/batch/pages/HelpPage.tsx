import { memo } from 'react';
import { BATCH_SECTIONS, BATCH_LIFECYCLE_STATES } from '../constants';
export const HelpPage = memo(function HelpPage() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap, 16px)' }}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2, 20px)', color: 'var(--color-text-primary)' }}>Help</h2>
      <p style={{ margin: 0, fontSize: 'var(--text-body, 14px)', color: 'var(--color-text-secondary)' }}>Batch engine documentation and support.</p>
      <div style={{ display: 'flex', flexDirection: 'column', gap: 16, padding: 24, background: 'var(--color-surface, #fff)', borderRadius: 'var(--radius-lg, 8px)', border: '1px solid var(--color-border, #e0e0e0)' }}>
        <h3 style={{ margin: 0, fontSize: 'var(--text-h3, 16px)', color: 'var(--color-text-primary, #1a1a1a)' }}>Getting Started</h3>
        <p style={{ margin: 0, fontSize: 'var(--text-body, 14px)', color: 'var(--color-text-secondary, #666)', lineHeight: 1.6 }}>
          The Batch & Lot Management Engine tracks batches and lots through their complete lifecycle. Each batch has a lifecycle status, quality status, and expiry status.
        </p>
        <h3 style={{ margin: '16px 0 0', fontSize: 'var(--text-h3, 16px)', color: 'var(--color-text-primary, #1a1a1a)' }}>Workspace Sections</h3>
        <ul style={{ margin: 0, paddingLeft: 20, color: 'var(--color-text-secondary, #666)', fontSize: 'var(--text-body, 14px)', lineHeight: 2 }}>
          {BATCH_SECTIONS.map((s) => <li key={s.id}><strong>{s.label}:</strong> {s.description}</li>)}
        </ul>
        <h3 style={{ margin: '16px 0 0', fontSize: 'var(--text-h3, 16px)', color: 'var(--color-text-primary, #1a1a1a)' }}>Batch Lifecycle ({BATCH_LIFECYCLE_STATES.length} states)</h3>
        <p style={{ margin: 0, fontSize: 'var(--text-body, 14px)', color: 'var(--color-text-secondary, #666)' }}>Created → Quality Review → Approved → Operational → Near Expiry → Expired → Archived/Disposed. Also supports Rejected, Blocked, Returned, and Recalled.</p>
        <h3 style={{ margin: '16px 0 0', fontSize: 'var(--text-h3, 16px)', color: 'var(--color-text-primary, #1a1a1a)' }}>Mock Mode</h3>
        <p style={{ margin: 0, fontSize: 'var(--text-body, 14px)', color: 'var(--color-text-secondary, #666)' }}>This module operates in Mock Mode. All data is generated client-side. No backend, database, or API is connected.</p>
      </div>
    </div>
  );
});

