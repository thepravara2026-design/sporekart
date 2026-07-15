import { useState, memo } from 'react';

const SETTINGS_GROUPS = [
  {
    title: 'Movement Preferences',
    items: [
      { label: 'Auto-approve goods receipt', key: 'autoApproveReceipt', type: 'toggle' as const },
      { label: 'Require approval for adjustments', key: 'requireApprovalAdjustment', type: 'toggle' as const },
      { label: 'Enable transfer notifications', key: 'transferNotifications', type: 'toggle' as const },
      { label: 'Default movement type', key: 'defaultMovementType', type: 'select' as const, options: ['warehouse_transfer', 'stock_adjustment', 'goods_receipt'] },
    ],
  },
  {
    title: 'Validation Rules',
    items: [
      { label: 'Enforce quantity non-negative', key: 'enforceNonNegative', type: 'toggle' as const },
      { label: 'Require reason for adjustments', key: 'requireReason', type: 'toggle' as const },
      { label: 'Check duplicate references', key: 'checkDuplicates', type: 'toggle' as const },
      { label: 'Validate warehouse existence', key: 'validateWarehouse', type: 'toggle' as const },
      { label: 'Max items per batch', key: 'maxItemsPerBatch', type: 'number' as const },
    ],
  },
  {
    title: 'Display & Export',
    items: [
      { label: 'Rows per page', key: 'rowsPerPage', type: 'select' as const, options: ['10', '25', '50', '100'] },
      { label: 'Default date range', key: 'defaultDateRange', type: 'select' as const, options: ['7d', '30d', '90d', '1y'] },
      { label: 'Export format', key: 'exportFormat', type: 'select' as const, options: ['CSV', 'Excel', 'PDF'] },
    ],
  },
];

export const SettingsPage = memo(function SettingsPage() {
  const [settings, setSettings] = useState<Record<string, string | boolean | number>>({
    autoApproveReceipt: false,
    requireApprovalAdjustment: true,
    transferNotifications: true,
    defaultMovementType: 'warehouse_transfer',
    enforceNonNegative: true,
    requireReason: true,
    checkDuplicates: true,
    validateWarehouse: true,
    maxItemsPerBatch: 100,
    rowsPerPage: '25',
    defaultDateRange: '30d',
    exportFormat: 'CSV',
  });

  const update = (key: string, value: string | boolean | number) => {
    setSettings((prev) => ({ ...prev, [key]: value }));
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 20 }}>
      <h3 style={{ margin: 0, fontSize: 'var(--text-h3, 18px)', fontWeight: 700 }}>Settings</h3>
      {SETTINGS_GROUPS.map((group) => (
        <section key={group.title} style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)' }}>
          <div style={{ padding: '12px 16px', borderBottom: '1px solid var(--color-border)', fontWeight: 600, fontSize: 'var(--text-body)' }}>{group.title}</div>
          <div style={{ padding: '8px 0' }}>
            {group.items.map((item) => (
              <div key={item.key} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '10px 16px', borderBottom: '1px solid var(--color-border)' }}>
                <span style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-primary)' }}>{item.label}</span>
                {item.type === 'toggle' && (
                  <button onClick={() => update(item.key, !settings[item.key] as boolean)}
                    style={{ width: 44, height: 24, borderRadius: 12, border: 'none', background: settings[item.key] ? 'var(--color-primary)' : 'var(--color-border)', cursor: 'pointer', position: 'relative', transition: 'background 0.2s' }}>
                    <span style={{ position: 'absolute', top: 2, left: (settings[item.key] as boolean) ? 22 : 2, width: 20, height: 20, borderRadius: '50%', background: '#fff', transition: 'left 0.2s' }} />
                  </button>
                )}
                {(item.type === 'select' && item.options) && (
                  <select value={settings[item.key] as string} onChange={(e) => update(item.key, e.target.value)}
                    style={{ padding: '4px 8px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-sm)', background: 'var(--color-bg)', fontSize: 'var(--text-body)' }}>
                    {item.options.map((opt: string) => <option key={opt} value={opt}>{opt}</option>)}
                  </select>
                )}
                {item.type === 'number' && (
                  <input type="number" value={settings[item.key] as number} onChange={(e) => update(item.key, parseInt(e.target.value, 10) || 0)}
                    style={{ width: 80, padding: '4px 8px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-sm)', background: 'var(--color-bg)', fontSize: 'var(--text-body)', textAlign: 'right' }} />
                )}
              </div>
            ))}
          </div>
        </section>
      ))}
      <div style={{ display: 'flex', gap: 8, justifyContent: 'flex-end' }}>
        <button style={{ padding: '8px 20px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-sm)', background: 'var(--color-surface)', cursor: 'pointer', fontSize: 'var(--text-body)' }}>Reset to Defaults</button>
        <button style={{ padding: '8px 20px', border: 'none', borderRadius: 'var(--radius-sm)', background: 'var(--color-primary)', color: '#fff', cursor: 'pointer', fontSize: 'var(--text-body)', fontWeight: 600 }}>Save Settings</button>
      </div>
    </div>
  );
});
