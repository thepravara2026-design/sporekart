import { memo, type CSSProperties, type ReactNode } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import { INVENTORY_ITEM_SECTIONS } from '../constants';
import { useInventoryItemWorkspace } from '../contexts/InventoryItemWorkspaceContext';
import { useInventoryItemPermissions } from '../hooks/useInventoryItemPermissions';
import { useInventoryItemResponsive } from '../hooks';
import { SearchComponent } from '../../inventory/components';
import { WorkspaceBanner } from '../../inventory/components';
import { InventoryItemsRegistryPage } from '../pages/InventoryItemsRegistryPage';
import { ProductsMappingPage } from '../pages/ProductsMappingPage';
import { VariantsMappingPage } from '../pages/VariantsMappingPage';
import { SKUMappingPage } from '../pages/SKUMappingPage';
import { UnitsPage } from '../pages/UnitsPage';
import { ClassificationPage } from '../pages/ClassificationPage';
import { LifecyclePage } from '../pages/LifecyclePage';
import { ValidationPage } from '../pages/ValidationPage';
import { ReportsPage } from '../pages/ReportsPage';
import { HistoryPage } from '../pages/HistoryPage';
import { SettingsPage } from '../pages/SettingsPage';

const ROLES = ['viewer', 'inventory_operator', 'inventory_manager', 'warehouse_manager', 'administrator'];

function SectionPlaceholder({ section }: { section: { label: string; description?: string; icon: string } }) {
  return (
    <div style={{ border: '2px dashed var(--color-border)', borderRadius: 'var(--radius-lg)', background: 'var(--color-surface)', padding: '48px 24px', display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 12, textAlign: 'center', minHeight: 320 }}>
      <div style={{ width: 56, height: 56, borderRadius: 'var(--radius-md)', background: 'var(--color-primary-alpha)', color: 'var(--color-primary)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
        <Icon name={section.icon} size={28} />
      </div>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>{section.label}</h2>
      <p style={{ margin: 0, color: 'var(--color-text-secondary)', maxWidth: 460 }}>{section.description}</p>
      <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', background: 'var(--color-surface-hover)', padding: '4px 10px', borderRadius: 'var(--radius-badge)' }}>Extensible placeholder · Sprint 25 Part 4 ready</span>
    </div>
  );
}

export const InventoryItemWorkspaceLayout = memo(function InventoryItemWorkspaceLayout({ children }: { children?: ReactNode }) {
  const { activeSection, setActiveSection, searchQuery, setSearchQuery } = useInventoryItemWorkspace();
  const { role, setRole } = useInventoryItemPermissions();
  const { isMobile } = useInventoryItemResponsive();

  const section = INVENTORY_ITEM_SECTIONS.find((s) => s.id === activeSection) ?? INVENTORY_ITEM_SECTIONS[0];

  const renderContent = () => {
    switch (activeSection) {
      case 'overview': return <SectionPlaceholder section={{ label: 'Inventory Item Dashboard', description: 'Executive overview of inventory item metrics, health, and quick actions.', icon: 'layout' }} />;
      case 'items': return <InventoryItemsRegistryPage />;
      case 'products': return <ProductsMappingPage />;
      case 'variants': return <VariantsMappingPage />;
      case 'sku': return <SKUMappingPage />;
      case 'units': return <UnitsPage />;
      case 'classification': return <ClassificationPage />;
      case 'lifecycle': return <LifecyclePage />;
      case 'validation': return <ValidationPage />;
      case 'reports': return <ReportsPage />;
      case 'history': return <HistoryPage />;
      case 'settings': return <SettingsPage />;
      default: return <SectionPlaceholder section={section} />;
    }
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', minHeight: '100%' }}>
      <header style={{ display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between', gap: 16, padding: '16px 24px', borderBottom: '1px solid var(--color-border)', background: 'var(--color-surface)', flexWrap: 'wrap' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
          <div style={{ width: 40, height: 40, borderRadius: 'var(--radius-md)', background: 'var(--color-primary-alpha)', color: 'var(--color-primary)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
            <Icon name="package" size={20} />
          </div>
          <div>
            <h1 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Inventory Items</h1>
            <p style={{ margin: '4px 0 0', fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)' }}>Enterprise Inventory Item Management · Product Mapping & SKU Association</p>
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
          <button style={actionBtn} onClick={() => setActiveSection('settings')}><Icon name="help-circle" size={14} /> Help</button>
        </div>
      </header>

      <nav aria-label="Breadcrumb" style={{ padding: '10px 24px', borderBottom: '1px solid var(--color-border)', background: 'var(--color-surface)', fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
        <span>Admin</span> <span style={{ color: 'var(--color-border)' }}>/</span> <span style={{ color: 'var(--color-text-primary)', fontWeight: 600 }}>Inventory Items</span> <span style={{ color: 'var(--color-border)' }}>/</span> <span>{section.label}</span>
      </nav>

      <div style={{ padding: '16px 24px', display: 'flex', flexDirection: 'column', gap: 12, background: 'var(--color-surface)' }}>
        <SearchComponent query={searchQuery} onQueryChange={setSearchQuery} activeFields={[]} onToggleField={() => {}} />
        <WorkspaceBanner variant="info" title="Product Mapping & SKU Association" message="Every product, variant and SKU is now an inventory item. Future stock, warehouse, procurement and sales transactions will reference these records." />
      </div>

      <div style={{ display: 'flex', flex: 1, minHeight: 0, flexDirection: isMobile ? 'column' : 'row' }}>
        <nav aria-label="Inventory item sections" style={{ ...navStyle, width: isMobile ? '100%' : 240, borderRight: isMobile ? 'none' : '1px solid var(--color-border)', borderBottom: isMobile ? '1px solid var(--color-border)' : 'none' }}>
          {INVENTORY_ITEM_SECTIONS.map((s) => {
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

        <main id="inventory-items-main" tabIndex={-1} style={{ flex: 1, padding: 24, minWidth: 0, outline: 'none', display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
          {children ?? renderContent()}
        </main>
      </div>

      <aside aria-label="Activity feed" style={{ borderTop: '1px solid var(--color-border)', background: 'var(--color-surface)', padding: '12px 24px', fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', display: 'flex', alignItems: 'center', gap: 8 }}>
        <Icon name="activity" size={14} /> Activity feed coming soon
      </aside>

      <footer style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', gap: 12, padding: '12px 24px', borderTop: '1px solid var(--color-border)', background: 'var(--color-surface)', fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', flexWrap: 'wrap' }}>
        <span>SporeKart Inventory Items · Mock Mode</span>
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
