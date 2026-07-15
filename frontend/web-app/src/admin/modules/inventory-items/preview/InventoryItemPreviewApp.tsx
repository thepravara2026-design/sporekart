import { memo, useState, type CSSProperties } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import { InventoryItemWorkspaceProvider } from '../contexts/InventoryItemWorkspaceContext';
import type { InventoryItemRole } from '../types';
import { PreviewDashboardPage } from './pages/PreviewDashboardPage';
import { PreviewRegistryPage } from './pages/PreviewRegistryPage';
import { PreviewProfilePage } from './pages/PreviewProfilePage';
import { PreviewMappingPage } from './pages/PreviewMappingPage';
import { PreviewSKUPage } from './pages/PreviewSKUPage';
import { PreviewClassificationPage } from './pages/PreviewClassificationPage';

const PREVIEW_TABS = [
  { id: 'dashboard', label: 'Dashboard', icon: 'bar-chart' },
  { id: 'registry', label: 'Registry', icon: 'package' },
  { id: 'profile', label: 'Profile', icon: 'user' },
  { id: 'mapping', label: 'Mapping', icon: 'shopping-bag' },
  { id: 'sku', label: 'SKU', icon: 'hash' },
  { id: 'classification', label: 'Classification', icon: 'bookmark' },
];

const ROLES = ['viewer', 'inventory_operator', 'inventory_manager', 'warehouse_manager', 'administrator'];

function PreviewContent({ activeTab }: { activeTab: string }) {
  switch (activeTab) {
    case 'dashboard': return <PreviewDashboardPage />;
    case 'registry': return <PreviewRegistryPage />;
    case 'profile': return <PreviewProfilePage />;
    case 'mapping': return <PreviewMappingPage />;
    case 'sku': return <PreviewSKUPage />;
    case 'classification': return <PreviewClassificationPage />;
    default: return <PreviewDashboardPage />;
  }
}

export const InventoryItemPreviewApp = memo(function InventoryItemPreviewApp() {
  const [activeTab, setActiveTab] = useState('dashboard');
  const [viewport, setViewport] = useState<'desktop' | 'tablet' | 'mobile'>('desktop');
  const [theme, setTheme] = useState<'light' | 'dark'>('light');
  const [role, setRole] = useState<InventoryItemRole>('administrator');

  const themeVars: CSSProperties = {
    '--color-surface': theme === 'dark' ? '#1a1a2e' : '#ffffff',
    '--color-surface-hover': theme === 'dark' ? '#16213e' : '#f5f5f5',
    '--color-border': theme === 'dark' ? '#2a2a4a' : '#e0e0e0',
    '--color-text-primary': theme === 'dark' ? '#e0e0e0' : '#1a1a1a',
    '--color-text-secondary': theme === 'dark' ? '#a0a0b0' : '#666666',
    '--color-text-tertiary': theme === 'dark' ? '#707080' : '#999999',
    '--color-primary': '#4f46e5',
    '--color-primary-alpha': theme === 'dark' ? 'rgba(79, 70, 229, 0.15)' : 'rgba(79, 70, 229, 0.08)',
    '--color-success': '#10b981',
    '--color-success-alpha': 'rgba(16, 185, 129, 0.1)',
    '--color-warning': '#f59e0b',
    '--color-warning-alpha': 'rgba(245, 158, 11, 0.1)',
    '--color-danger': '#ef4444',
    '--color-danger-alpha': 'rgba(239, 68, 68, 0.1)',
    '--color-info': '#3b82f6',
    '--color-info-alpha': 'rgba(59, 130, 246, 0.1)',
    '--color-neutral': '#6b7280',
    '--color-neutral-alpha': 'rgba(107, 114, 128, 0.1)',
    '--text-h1': '24px',
    '--text-h2': '20px',
    '--text-h3': '16px',
    '--text-body': '14px',
    '--text-caption': '12px',
    '--radius-sm': '4px',
    '--radius-md': '6px',
    '--radius-lg': '8px',
    '--radius-badge': '12px',
    '--space-component-gap': '16px',
  } as CSSProperties;

  const maxW = viewport === 'mobile' ? 375 : viewport === 'tablet' ? 768 : '100%';

  return (
    <InventoryItemWorkspaceProvider initialRole={role}>
      <div style={{ background: '#f8f9fa', minHeight: '100vh' }}>
        <header style={{ background: 'var(--color-surface, #fff)', borderBottom: '1px solid var(--color-border, #e0e0e0)', padding: '12px 24px', display: 'flex', alignItems: 'center', justifyContent: 'space-between', flexWrap: 'wrap', gap: 12 }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
            <Icon name="package" size={20} />
            <span style={{ fontWeight: 700, fontSize: 16 }}>Inventory Items · Preview</span>
          </div>
          <div style={{ display: 'flex', gap: 8, alignItems: 'center', flexWrap: 'wrap' }}>
            <select value={viewport} onChange={(e) => setViewport(e.target.value as typeof viewport)} style={{ padding: '6px 10px', borderRadius: 6, border: '1px solid var(--color-border)', background: 'var(--color-surface)', color: 'var(--color-text-primary)' }}>
              <option value="desktop">Desktop</option>
              <option value="tablet">Tablet (768px)</option>
              <option value="mobile">Mobile (375px)</option>
            </select>
            <button onClick={() => setTheme((t) => (t === 'light' ? 'dark' : 'light'))} style={{ padding: '6px 12px', borderRadius: 6, border: '1px solid var(--color-border)', background: 'var(--color-surface)', cursor: 'pointer', color: 'var(--color-text-primary)' }}>
              {theme === 'light' ? '🌙 Dark' : '☀️ Light'}
            </button>
            <select value={role} onChange={(e) => setRole(e.target.value as typeof role)} style={{ padding: '6px 10px', borderRadius: 6, border: '1px solid var(--color-border)', background: 'var(--color-surface)', color: 'var(--color-text-primary)' }}>
              {ROLES.map((r) => <option key={r} value={r}>{r.replace(/_/g, ' ')}</option>)}
            </select>
          </div>
        </header>

        <nav style={{ background: 'var(--color-surface, #fff)', borderBottom: '1px solid var(--color-border, #e0e0e0)', padding: '0 24px', display: 'flex', gap: 0, overflowX: 'auto' }}>
          {PREVIEW_TABS.map((tab) => (
            <button
              key={tab.id}
              onClick={() => setActiveTab(tab.id)}
              style={{
                display: 'flex', alignItems: 'center', gap: 6, padding: '12px 16px',
                border: 'none', borderBottom: activeTab === tab.id ? '2px solid var(--color-primary)' : '2px solid transparent',
                background: 'none', cursor: 'pointer', color: activeTab === tab.id ? 'var(--color-primary)' : 'var(--color-text-secondary)',
                fontWeight: activeTab === tab.id ? 600 : 400, fontSize: 14, whiteSpace: 'nowrap',
              }}
            >
              <Icon name={tab.icon} size={16} /> {tab.label}
            </button>
          ))}
        </nav>

        <div style={{ display: 'flex', justifyContent: 'center', padding: 24 }}>
          <div style={{ width: '100%', maxWidth: maxW, transition: 'max-width 0.3s ease', ...themeVars }}>
            <PreviewContent activeTab={activeTab} />
          </div>
        </div>

        <footer style={{ borderTop: '1px solid var(--color-border, #e0e0e0)', padding: '12px 24px', background: 'var(--color-surface, #fff)', textAlign: 'center', fontSize: 12, color: '#999' }}>
          SporeKart Inventory Items · Preview · Mock Mode · WCAG 2.2 AA · Responsive 320–1920px
        </footer>
      </div>
    </InventoryItemWorkspaceProvider>
  );
});
