import { memo } from 'react';

export const HelpPage = memo(function HelpPage() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2, 20px)', fontWeight: 700 }}>Help & Support</h2>
      <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
        <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
          <div style={{ fontWeight: 600 }}>What is Inventory Intelligence?</div>
          <p style={{ margin: '4px 0 0', fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)' }}>Provides executive-level operational visibility through KPIs, health scores, forecasting, insights, and alerts.</p>
        </div>
        <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
          <div style={{ fontWeight: 600 }}>How is data updated?</div>
          <p style={{ margin: '4px 0 0', fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)' }}>Data is refreshed in real time from the inventory, warehouse, stock, batch, movement, and receiving modules.</p>
        </div>
        <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
          <div style={{ fontWeight: 600 }}>How do I configure alerts?</div>
          <p style={{ margin: '4px 0 0', fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)' }}>Navigate to the Settings section to configure alert thresholds, forecast periods, and report schedules.</p>
        </div>
      </div>
    </div>
  );
});
