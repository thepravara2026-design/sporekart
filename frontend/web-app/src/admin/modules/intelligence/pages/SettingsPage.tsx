import { memo } from 'react';

export const SettingsPage = memo(function SettingsPage() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2, 20px)', fontWeight: 700 }}>Intelligence Settings</h2>
      <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
        <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
          <div style={{ fontWeight: 600, marginBottom: 4 }}>Forecast Period</div>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Next 30 days (default)</div>
        </div>
        <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
          <div style={{ fontWeight: 600, marginBottom: 4 }}>Alert Thresholds</div>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Low stock: &lt;10 units | Overstock: &gt;500 units</div>
        </div>
        <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 16 }}>
          <div style={{ fontWeight: 600, marginBottom: 4 }}>Report Schedule</div>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Daily at 08:00 | Weekly on Monday</div>
        </div>
      </div>
    </div>
  );
});
