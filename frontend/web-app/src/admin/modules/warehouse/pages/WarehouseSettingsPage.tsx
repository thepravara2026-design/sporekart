import { memo, useState } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import { WAREHOUSE_ROLES, WAREHOUSE_PERMISSIONS, WAREHOUSE_ROLE_PERMISSIONS } from '../constants';
import { useWarehousePermissions } from '../hooks/useWarehousePermissions';
import { SectionHeader } from '../components';
import { SummaryCard } from '../components';
import { EmptyState } from '../components';

const THEMES = [{ value: 'light', label: 'Light' }, { value: 'dark', label: 'Dark' }, { value: 'system', label: 'System' }];
const DENSITIES = [{ value: 'comfortable', label: 'Comfortable' }, { value: 'compact', label: 'Compact' }];
const LANGUAGES = [{ value: 'en', label: 'English' }, { value: 'hi', label: 'Hindi' }, { value: 'mr', label: 'Marathi' }];
const ROLE_LABELS: Record<string, string> = {
  viewer: 'Viewer', warehouse_operator: 'Operator', warehouse_manager: 'Manager',
  inventory_manager: 'Inventory Mgr', administrator: 'Administrator',
};
const PERMISSION_LABELS: Record<string, string> = {
  view: 'View', create: 'Create', edit: 'Edit', archive: 'Archive', restore: 'Restore',
  settings: 'Settings', reports: 'Reports', analytics: 'Analytics', operations_future: 'Operations',
};

export const WarehouseSettingsPage = memo(function WarehouseSettingsPage() {
  const { role, setRole, can } = useWarehousePermissions();
  const [theme, setTheme] = useState('system');
  const [density, setDensity] = useState('comfortable');
  const [language, setLanguage] = useState('en');
  const [devtools, setDevtools] = useState(true);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
      <SectionHeader title="Settings" description="Configure workspace preferences and review the permission model." icon="settings" sublabel="Warehouses / Settings" />

      {!can('settings') && <EmptyState stateKey="no_permission" />}
      {can('settings') && (
        <>
          <SummaryCard title="Appearance" description="Theme, density and language" icon="palette">
            <Row label="Theme"><Seg options={THEMES} value={theme} onChange={setTheme} /></Row>
            <Row label="Density"><Seg options={DENSITIES} value={density} onChange={setDensity} /></Row>
            <Row label="Language"><Seg options={LANGUAGES} value={language} onChange={setLanguage} /></Row>
          </SummaryCard>

          <SummaryCard title="Current Role" description="Switch the active role to preview permission gating" icon="shield">
            <Row label="Active role">
              <select aria-label="Active role" value={role} onChange={(e) => setRole(e.target.value as typeof role)} style={selectStyle}>
                {WAREHOUSE_ROLES.map((r) => <option key={r} value={r}>{ROLE_LABELS[r] ?? r}</option>)}
              </select>
            </Row>
          </SummaryCard>

          <SummaryCard title="Developer Tools" description="Mock data controls (foundation)" icon="terminal">
            <Row label="Show devtools"><Toggle checked={devtools} onChange={setDevtools} /></Row>
            <Row label="Reset mock data"><button style={ghostBtn}><Icon name="refresh-cw" size={14} /> Reset</button></Row>
          </SummaryCard>

          <SummaryCard title="Permission Matrix" description="Role × permission capabilities (read-only)" icon="lock">
            <div style={{ overflowX: 'auto' }}>
              <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 'var(--text-caption)' }}>
                <thead>
                  <tr style={{ background: 'var(--color-surface-hover)', color: 'var(--color-text-secondary)' }}>
                    <th style={th}>Permission</th>
                    {WAREHOUSE_ROLES.map((r) => <th key={r} style={th}>{ROLE_LABELS[r] ?? r}</th>)}
                  </tr>
                </thead>
                <tbody>
                  {WAREHOUSE_PERMISSIONS.map((p) => (
                    <tr key={p} style={{ borderTop: '1px solid var(--color-border)' }}>
                      <td style={td}>{PERMISSION_LABELS[p] ?? p}</td>
                      {WAREHOUSE_ROLES.map((r) => {
                        const allowed = WAREHOUSE_ROLE_PERMISSIONS[r].includes(p);
                        return (
                          <td key={r} style={{ ...td, textAlign: 'center' }}>
                            {allowed ? <Icon name="check" size={14} /> : <Icon name="x" size={14} />}
                          </td>
                        );
                      })}
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
            <p style={{ margin: '12px 0 0', fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Note: A global PermissionProvider is not mounted in the admin tree; warehouse access control uses the local WarehouseWorkspaceContext + useWarehousePermissions.</p>
          </SummaryCard>
        </>
      )}
    </div>
  );
});

function Row({ label, children }: { label: string; children: React.ReactNode }) {
  return (
    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', gap: 16, padding: '8px 0' }}>
      <span style={{ color: 'var(--color-text-secondary)', fontSize: 'var(--text-body)' }}>{label}</span>
      {children}
    </div>
  );
}

function Seg({ options, value, onChange }: { options: { value: string; label: string }[]; value: string; onChange: (v: string) => void }) {
  return (
    <div style={{ display: 'inline-flex', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)', overflow: 'hidden' }}>
      {options.map((o) => (
        <button key={o.value} onClick={() => onChange(o.value)} style={{ padding: '6px 12px', border: 'none', background: value === o.value ? 'var(--color-primary)' : 'var(--color-surface)', color: value === o.value ? 'var(--color-primary-contrast)' : 'var(--color-text-secondary)', cursor: 'pointer', fontSize: 'var(--text-caption)' }}>
          {o.label}
        </button>
      ))}
    </div>
  );
}

function Toggle({ checked, onChange }: { checked: boolean; onChange: (v: boolean) => void }) {
  return (
    <button role="switch" aria-checked={checked} onClick={() => onChange(!checked)} style={{ width: 42, height: 24, borderRadius: 12, border: 'none', background: checked ? 'var(--color-primary)' : 'var(--color-border)', cursor: 'pointer', position: 'relative' }}>
      <span style={{ position: 'absolute', top: 2, left: checked ? 20 : 2, width: 20, height: 20, borderRadius: '50%', background: '#fff', transition: 'left 0.15s' }} />
    </button>
  );
}

const selectStyle: React.CSSProperties = { padding: '8px 10px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)', background: 'var(--color-surface)', color: 'var(--color-text-primary)' };
const ghostBtn: React.CSSProperties = { display: 'inline-flex', alignItems: 'center', gap: 6, padding: '6px 12px', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-surface)', color: 'var(--color-text-primary)', cursor: 'pointer' };
const th: React.CSSProperties = { textAlign: 'left', padding: '8px 10px', fontWeight: 600 };
const td: React.CSSProperties = { padding: '8px 10px', color: 'var(--color-text-primary)' };


