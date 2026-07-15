import { memo, type CSSProperties, type ReactNode } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import { INVENTORY_SECTIONS } from '../constants';
import { useInventoryWorkspace } from '../contexts/InventoryWorkspaceContext';
import { useInventoryPermissions } from '../hooks/useInventoryPermissions';
import { useInventoryResponsive } from '../hooks';
import { SearchComponent } from '../components/SearchComponent';
import { WorkspaceBanner } from '../components/WorkspaceBanner';
import { InventoryOverviewPage } from '../pages/InventoryOverviewPage';
import { InventoryDashboardPage } from '../pages/InventoryDashboardPage';
import { InventorySettingsPage } from '../pages/InventorySettingsPage';

const ROLES = ['viewer', 'inventory_operator', 'inventory_manager', 'warehouse_manager', 'administrator'];

function SectionPlaceholder({ section }: { section: { label: string; description?: string; icon: string } }) {
  return (
    <div style={{ border: '2px dashed var(--color-border)', borderRadius: 'var(--radius-lg)', background: 'var(--color-surface)', padding: '48px 24px', display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 12, textAlign: 'center', minHeight: 320 }}>
      <div style={{ width: 56, height: 56, borderRadius: 'var(--radius-md)', background: 'var(--color-primary-alpha)', color: 'var(--color-primary)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
        <Icon name={section.icon} size={28} />
      </div>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>{section.label}</h2>
      <p style={{ margin: 0, color: 'var(--color-text-secondary)', maxWidth: 'min(460px, 90vw)' }}>{section.description}</p>
      <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', background: 'var(--color-surface-hover)', padding: '4px 10px', borderRadius: 'var(--radius-badge)' }}>Extensible placeholder · Sprint 25 Part 2 ready</span>
    </div>
  );
}

export const InventoryWorkspaceLayout = memo(function InventoryWorkspaceLayout({ children }: { children?: ReactNode }) {
  const { activeSection, setActiveSection, searchQuery, setSearchQuery } = useInventoryWorkspace();
  const { role, setRole } = useInventoryPermissions();
  const { isMobile } = useInventoryResponsive();

  const section = INVENTORY_SECTIONS.find((s) => s.id === activeSection) ?? INVENTORY_SECTIONS[0];

  const renderContent = () => {
    if (activeSection === 'overview') return <InventoryOverviewPage />;
    if (activeSection === 'dashboard') return <InventoryDashboardPage />;
    if (activeSection === 'settings') return <InventorySettingsPage />;
    return <SectionPlaceholder section={section} />;
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', minHeight: '100%' }}>
      <header style={{ display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between', gap: 16, padding: '16px 24px', borderBottom: '1px solid var(--color-border)', background: 'var(--color-surface)', flexWrap: 'wrap' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
          <div style={{ width: 40, height: 40, borderRadius: 'var(--radius-md)', background: 'var(--color-primary-alpha)', color: 'var(--color-primary)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
            <Icon name="archive" size={20} />
          </div>
          <div>
            <h1 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Inventory</h1>
            <p style={{ margin: '4px 0 0', fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)' }}>Enterprise Inventory Management · Foundation</p>
          </div>
        </div>
        <div style={{ display: 'flex', gap: 8, flexWrap: 'wrap', alignItems: 'center' }}>
          <select
            aria-label="Active role"
            value={role}
            onChange={(e) => setRole(e.target.value as typeof role)}
            style={{ padding: '8px 10px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)', background: 'var(--color-surface)', color: 'var(--color-text-primary)', fontSize: 'var(--text-body)' }}
          >
            {ROLES.map((r) => <option key={r} value={r}>{r.replace(/_/g, ' ')}</option>)}
          </select>
          <button style={actionBtn} onClick={() => setActiveSection('settings')}><Icon name="settings" size={14} /> Settings</button>
          <button style={actionBtn} onClick={() => setActiveSection('help')}><Icon name="help-circle" size={14} /> Help</button>
        </div>
      </header>

      <nav aria-label="Breadcrumb" style={{ padding: '10px 24px', borderBottom: '1px solid var(--color-border)', background: 'var(--color-surface)', fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
        <span>Admin</span> <span style={{ color: 'var(--color-border)' }}>/</span> <span style={{ color: 'var(--color-text-primary)', fontWeight: 600 }}>Inventory</span> <span style={{ color: 'var(--color-border)' }}>/</span> <span>{section.label}</span>
      </nav>

      <div style={{ padding: '16px 24px', display: 'flex', flexDirection: 'column', gap: 12, background: 'var(--color-surface)' }}>
        <SearchComponent query={searchQuery} onQueryChange={setSearchQuery} activeFields={[]} onToggleField={() => {}} />
        <WorkspaceBanner variant="info" title="No new notifications" message="Inventory is synced and up to date." />
      </div>

      <div style={{ display: 'flex', flex: 1, minHeight: 0, flexDirection: isMobile ? 'column' : 'row' }}>
        <nav aria-label="Inventory sections" style={{ ...navStyle, width: isMobile ? '100%' : 240, borderRight: isMobile ? 'none' : '1px solid var(--color-border)', borderBottom: isMobile ? '1px solid var(--color-border)' : 'none' }}>
          {INVENTORY_SECTIONS.map((s) => {
            const active = s.id === activeSection;
            return (
              <button
                key={s.id}
                onClick={() => setActiveSection(s.id)}
                aria-current={active ? 'page' : undefined}
                style={{
                  display: 'flex', alignItems: 'center', gap: 10, width: '100%', padding: '8px 12px',
                  border: 'none', background: active ? 'var(--color-primary-alpha)' : 'transparent',
                  color: active ? 'var(--color-primary)' : 'var(--color-text-secondary)', fontWeight: active ? 600 : 400,
                  cursor: 'pointer', fontSize: 'var(--text-body)', borderRadius: 'var(--radius-md)', textAlign: 'left',
                }}
              >
                <Icon name={s.icon} size={16} /> {s.label}
              </button>
            );
          })}
        </nav>

        <main id="inventory-main" tabIndex={-1} style={{ flex: 1, padding: 24, minWidth: 0, outline: 'none', display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
          {children ?? renderContent()}
        </main>
      </div>

      <aside aria-label="Activity feed" style={{ borderTop: '1px solid var(--color-border)', background: 'var(--color-surface)', padding: '12px 24px', fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', display: 'flex', alignItems: 'center', gap: 8 }}>
        <Icon name="activity" size={14} /> Activity feed coming soon
      </aside>

      <footer style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', gap: 12, padding: '12px 24px', borderTop: '1px solid var(--color-border)', background: 'var(--color-surface)', fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', flexWrap: 'wrap' }}>
        <span>SporeKart Inventory · Mock Mode</span>
        <button style={profileBtn} aria-label="Profile actions"><Icon name="user" size={14} /> Profile</button>
      </footer>
    </div>
  );
});

const navStyle: CSSProperties = {
  background: 'var(--color-surface)', padding: 12, display: 'flex', flexDirection: 'column', gap: 2, overflowY: 'auto',
};

const actionBtn: CSSProperties = {
  display: 'inline-flex', alignItems: 'center', gap: 6, padding: '8px 14px', borderRadius: 'var(--radius-md)',
  border: '1px solid var(--color-border)', background: 'var(--color-surface)', cursor: 'pointer',
  color: 'var(--color-text-primary)', fontSize: 'var(--text-body)', fontWeight: 500,
};

const profileBtn: CSSProperties = {
  display: 'inline-flex', alignItems: 'center', gap: 6, padding: '6px 12px', borderRadius: 'var(--radius-md)',
  border: '1px solid var(--color-border)', background: 'var(--color-surface)', cursor: 'pointer',
  color: 'var(--color-text-secondary)', fontSize: 'var(--text-caption)',
};
