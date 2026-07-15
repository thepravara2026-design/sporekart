import { memo } from 'react';
import { useStockPermissions } from '../hooks/useStockPermissions';

export const HelpPage = memo(function HelpPage() {
  const { role } = useStockPermissions();

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap, 16px)' }}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2, 20px)', color: 'var(--color-text-primary)' }}>Help</h2>
      <p style={{ margin: 0, fontSize: 'var(--text-body, 14px)', color: 'var(--color-text-secondary)' }}>Stock engine documentation and support.</p>

      <div style={{ display: 'flex', flexDirection: 'column', gap: 16, padding: 24, background: 'var(--color-surface, #fff)', borderRadius: 'var(--radius-lg, 8px)', border: '1px solid var(--color-border, #e0e0e0)' }}>
        <h3 style={{ margin: 0, fontSize: 'var(--text-h3, 16px)', color: 'var(--color-text-primary, #1a1a1a)' }}>Getting Started</h3>
        <p style={{ margin: 0, fontSize: 'var(--text-body, 14px)', color: 'var(--color-text-secondary, #666)', lineHeight: 1.6 }}>
          The Stock Management Engine tracks inventory quantities across 15 states for every Inventory Item. Use the workspace sections to view, filter, and manage stock records.
        </p>

        <h3 style={{ margin: '16px 0 0', fontSize: 'var(--text-h3, 16px)', color: 'var(--color-text-primary, #1a1a1a)' }}>Stock States</h3>
        <ul style={{ margin: 0, paddingLeft: 20, color: 'var(--color-text-secondary, #666)', fontSize: 'var(--text-body, 14px)', lineHeight: 2 }}>
          <li><strong>Active</strong> — Available, Reserved, Incoming, Allocated</li>
          <li><strong>Problem</strong> — Damaged, Expired, Blocked, Quarantine, Lost</li>
          <li><strong>Inspection</strong> — Inspection, Returned</li>
          <li><strong>Adjustment</strong> — Adjustment Pending</li>
          <li><strong>Future</strong> — Future Manufacturing, Future Transit, Future Consignment</li>
        </ul>

        <h3 style={{ margin: '16px 0 0', fontSize: 'var(--text-h3, 16px)', color: 'var(--color-text-primary, #1a1a1a)' }}>Permissions</h3>
        <p style={{ margin: 0, fontSize: 'var(--text-body, 14px)', color: 'var(--color-text-secondary, #666)', lineHeight: 1.6 }}>
          Current role: <strong>{role?.replace(/_/g, ' ') ?? 'viewer'}</strong>. Role-based access controls determine which sections and actions are available.
        </p>

        <h3 style={{ margin: '16px 0 0', fontSize: 'var(--text-h3, 16px)', color: 'var(--color-text-primary, #1a1a1a)' }}>Mock Mode</h3>
        <p style={{ margin: 0, fontSize: 'var(--text-body, 14px)', color: 'var(--color-text-secondary, #666)', lineHeight: 1.6 }}>
          This module operates in Mock Mode. All data is generated client-side for demonstration and development purposes. No backend, database, or API is connected.
        </p>
      </div>
    </div>
  );
});
