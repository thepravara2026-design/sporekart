import { memo, useState, type ReactNode } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import { WarehouseWorkspaceProvider } from '../contexts/WarehouseWorkspaceContext';
import { useWarehousePermissions } from '../hooks/useWarehousePermissions';
import { WarehouseDashboardPreview } from './pages/WarehouseDashboardPreview';
import { WarehouseDirectoryPreview } from './pages/WarehouseDirectoryPreview';
import { WarehouseProfilePreview } from './pages/WarehouseProfilePreview';
import { WarehouseStoragePreview } from './pages/WarehouseStoragePreview';
import { WarehouseZonesPreview } from './pages/WarehouseZonesPreview';
import { WarehouseSettingsPreview } from './pages/WarehouseSettingsPreview';

const ROLE_OPTIONS = [
  { value: 'viewer', label: 'Viewer' },
  { value: 'warehouse_operator', label: 'Operator' },
  { value: 'warehouse_manager', label: 'Manager' },
  { value: 'inventory_manager', label: 'Inventory Mgr' },
  { value: 'administrator', label: 'Administrator' },
];

const TABS = [
  { id: 'dashboard', label: 'Dashboard', icon: 'bar-chart', node: <WarehouseDashboardPreview /> },
  { id: 'directory', label: 'Directory', icon: 'list', node: <WarehouseDirectoryPreview /> },
  { id: 'profile', label: 'Profile', icon: 'home', node: <WarehouseProfilePreview /> },
  { id: 'storage', label: 'Storage', icon: 'git-branch', node: <WarehouseStoragePreview /> },
  { id: 'zones', label: 'Zones', icon: 'grid', node: <WarehouseZonesPreview /> },
  { id: 'settings', label: 'Settings', icon: 'settings', node: <WarehouseSettingsPreview /> },
] as const;

function TabBody({ children }: { children: ReactNode }) {
  return <>{children}</>;
}

function PreviewRoleSelect() {
  const { role, setRole } = useWarehousePermissions();
  return (
    <select aria-label="Preview role" value={role} onChange={(e) => setRole(e.target.value as typeof role)} style={{ padding: '6px 10px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)', background: 'var(--color-surface)', color: 'var(--color-text-primary)' }}>
      {ROLE_OPTIONS.map((r) => <option key={r.value} value={r.value}>{r.label}</option>)}
    </select>
  );
}

export const WarehousePreviewApp = memo(function WarehousePreviewApp() {
  const [tab, setTab] = useState<(typeof TABS)[number]['id']>('dashboard');

  return (
    <WarehouseWorkspaceProvider initialRole="administrator">
      <div style={{ display: 'flex', flexDirection: 'column', height: '100%', background: 'var(--color-background)', color: 'var(--color-text-primary)' }}>
        <header style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', gap: 16, padding: '12px 24px', borderBottom: '1px solid var(--color-border)', background: 'var(--color-surface)', flexWrap: 'wrap' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
            <Icon name="eye" size={18} />
            <h1 style={{ margin: 0, fontSize: 'var(--text-h2)' }}>Warehouse Preview</h1>
          </div>
          <div style={{ display: 'inline-flex', alignItems: 'center', gap: 8 }}>
            <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Role:</span>
            <PreviewRoleSelect />
            <a href="/preview/warehouse/dashboard" target="_blank" rel="noreferrer" style={{ ...linkStyle }}><Icon name="external-link" size={14} /> Open</a>
          </div>
        </header>

        <nav aria-label="Preview tabs" style={{ display: 'flex', gap: 4, padding: '8px 16px', borderBottom: '1px solid var(--color-border)', background: 'var(--color-surface)', overflowX: 'auto', flexWrap: 'nowrap' }}>
          {TABS.map((t) => {
            const active = t.id === tab;
            return (
              <button key={t.id} onClick={() => setTab(t.id)} aria-current={active ? 'page' : undefined} style={{ display: 'inline-flex', alignItems: 'center', gap: 6, padding: '8px 14px', border: 'none', borderRadius: 'var(--radius-md)', background: active ? 'var(--color-primary-alpha)' : 'transparent', color: active ? 'var(--color-primary)' : 'var(--color-text-secondary)', cursor: 'pointer', fontWeight: active ? 600 : 400, fontSize: 'var(--text-body)', whiteSpace: 'nowrap' }}>
                <Icon name={t.icon} size={14} /> {t.label}
              </button>
            );
          })}
        </nav>

        <main id="warehouse-preview-main" tabIndex={-1} style={{ flex: 1, overflowY: 'auto', padding: 24, outline: 'none' }}>
          {TABS.find((t) => t.id === tab) && <TabBody>{TABS.find((t) => t.id === tab)!.node}</TabBody>}
        </main>
      </div>
    </WarehouseWorkspaceProvider>
  );
});

const linkStyle: React.CSSProperties = { display: 'inline-flex', alignItems: 'center', gap: 6, padding: '6px 10px', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-surface)', color: 'var(--color-text-primary)', textDecoration: 'none', fontSize: 'var(--text-caption)' };


