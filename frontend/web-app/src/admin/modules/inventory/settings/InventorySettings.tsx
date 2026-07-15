import { memo, useMemo, useState } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import { INVENTORY_SETTINGS_SECTIONS, INVENTORY_UNITS } from '../constants';
import { SectionHeader } from '../components/SectionHeader';
import { PermissionPlaceholder } from '../components/PermissionPlaceholder';
import { EmptyState } from '../components/EmptyState';

interface ToggleRow {
  id: string;
  label: string;
  description?: string;
}

const TOGGLE_GROUPS: Record<string, ToggleRow[]> = {
  general: [
    { id: 'low_stock_alerts', label: 'Low-stock alerts', description: 'Notify when items fall below reorder point.' },
    { id: 'auto_sync', label: 'Auto-sync with warehouses', description: 'Periodically reconcile stock counts.' },
    { id: 'compact_density', label: 'Compact table density', description: 'Show more rows per page.' },
  ],
  inventory_preferences: [
    { id: 'require_sku', label: 'Require SKU on items', description: 'Enforce unique SKU per inventory item.' },
    { id: 'batch_tracking', label: 'Enable batch tracking', description: 'Track lots and expiry per batch.' },
  ],
  warehouse_preferences: [
    { id: 'multi_warehouse', label: 'Multi-warehouse mode', description: 'Allow stock across multiple facilities.' },
    { id: 'cold_storage', label: 'Cold storage zones', description: 'Support temperature-controlled zones.' },
  ],
  stock_preferences: [
    { id: 'negative_stock', label: 'Allow negative stock', description: 'Permit oversell until reconciled.' },
    { id: 'reorder_auto', label: 'Auto-generate reorder', description: 'Create draft purchase orders at threshold.' },
  ],
};

function Toggle({ row, checked, onChange }: { row: ToggleRow; checked: boolean; onChange: (v: boolean) => void }) {
  return (
    <label
      style={{
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        gap: 12,
        padding: '12px 0',
        borderBottom: '1px solid var(--color-border)',
        cursor: 'pointer',
      }}
    >
      <span>
        <span style={{ display: 'block', fontSize: 'var(--text-body)', color: 'var(--color-text-primary)', fontWeight: 500 }}>{row.label}</span>
        {row.description && (
          <span style={{ display: 'block', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', marginTop: 2 }}>{row.description}</span>
        )}
      </span>
      <input
        type="checkbox"
        checked={checked}
        onChange={(e) => onChange(e.target.checked)}
        aria-label={row.label}
        style={{ width: 18, height: 18, accentColor: 'var(--color-primary)' }}
      />
    </label>
  );
}

export const InventorySettings = memo(function InventorySettings() {
  const [active, setActive] = useState('general');
  const [toggles, setToggles] = useState<Record<string, boolean>>({
    low_stock_alerts: true,
    auto_sync: false,
    compact_density: false,
    require_sku: true,
    batch_tracking: true,
    multi_warehouse: true,
    cold_storage: false,
    negative_stock: false,
    reorder_auto: false,
  });

  const activeSection = useMemo(
    () => INVENTORY_SETTINGS_SECTIONS.find((s) => s.id === active) ?? INVENTORY_SETTINGS_SECTIONS[0],
    [active]
  );

  const sectionRows = TOGGLE_GROUPS[activeSection.id] ?? [];

  return (
    <PermissionPlaceholder permission="settings">
      <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
        <SectionHeader title="Inventory Settings" description="Configure inventory workspace behavior." icon="settings" sublabel="Inventory / Settings" />

        <div style={{ display: 'flex', flexWrap: 'wrap', gap: 16, alignItems: 'flex-start' }}>
          <nav aria-label="Settings sections" style={{ flex: '1 1 220px', minWidth: 200, border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', padding: 8, display: 'flex', flexDirection: 'column', gap: 2 }}>
            {INVENTORY_SETTINGS_SECTIONS.map((s) => (
              <button
                key={s.id}
                onClick={() => setActive(s.id)}
                aria-current={active === s.id ? 'page' : undefined}
                disabled={s.placeholder && false}
                style={{
                  display: 'flex',
                  alignItems: 'center',
                  gap: 10,
                  width: '100%',
                  padding: '8px 12px',
                  border: 'none',
                  borderLeft: active === s.id ? '3px solid var(--color-primary)' : '3px solid transparent',
                  background: active === s.id ? 'var(--color-primary-alpha)' : 'transparent',
                  color: active === s.id ? 'var(--color-primary)' : s.placeholder ? 'var(--color-text-tertiary)' : 'var(--color-text-secondary)',
                  fontSize: 'var(--text-body)',
                  fontWeight: active === s.id ? 600 : 400,
                  cursor: 'pointer',
                  textAlign: 'left',
                  borderRadius: 'var(--radius-sm)',
                }}
              >
                <Icon name={s.icon} size={18} />
                <span>{s.label}</span>
                {s.placeholder && <Icon name="clock" size={12} style={{ marginLeft: 'auto' }} />}
              </button>
            ))}
          </nav>

          <div style={{ flex: '3 1 420px', minWidth: 280, border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', padding: 20 }}>
            <h3 style={{ margin: '0 0 8px', fontSize: 'var(--text-body)', fontWeight: 600, color: 'var(--color-text-primary)' }}>{activeSection.label}</h3>
            <p style={{ margin: '0 0 12px', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>{activeSection.description}</p>

            {activeSection.placeholder ? (
              <EmptyState stateKey="configuration_required" title={`${activeSection.label} coming soon`} message="This settings area is a placeholder for a future sprint." actionLabel={undefined} />
            ) : activeSection.id === 'units' ? (
              <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
                {INVENTORY_UNITS.map((u) => (
                  <div key={u.value} style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', gap: 12, padding: '10px 0', borderBottom: '1px solid var(--color-border)' }}>
                    <span style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-primary)' }}>{u.label}</span>
                    {u.futureConversion && (
                      <span style={{ display: 'inline-flex', alignItems: 'center', gap: 4, fontSize: 'var(--text-caption)', color: 'var(--color-text-warning)' }}>
                        <Icon name="clock" size={12} /> Conversion coming soon
                      </span>
                    )}
                  </div>
                ))}
              </div>
            ) : (
              <div>
                {sectionRows.map((row) => (
                  <Toggle
                    key={row.id}
                    row={row}
                    checked={!!toggles[row.id]}
                    onChange={(v) => setToggles((prev) => ({ ...prev, [row.id]: v }))}
                  />
                ))}
              </div>
            )}
          </div>
        </div>
      </div>
    </PermissionPlaceholder>
  );
});

export default InventorySettings;

