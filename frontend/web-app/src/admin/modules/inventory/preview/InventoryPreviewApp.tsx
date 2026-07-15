import type React from 'react';
import { useState } from 'react';
import { PermissionProvider } from '../../../permissions/PermissionProvider';
import { InventoryWorkspaceProvider } from '../contexts/InventoryWorkspaceContext';
import { Icon } from '../../../../design-system/icons/Icon';
import { InventoryPreviewDashboard } from './InventoryPreviewDashboard';
import { InventoryPreviewWorkspace } from './InventoryPreviewWorkspace';
import { InventoryPreviewSettings } from './InventoryPreviewSettings';
import { InventoryPreviewNavigation } from './InventoryPreviewNavigation';
import { InventoryPreviewComponents } from './InventoryPreviewComponents';

type Viewport = 'desktop' | 'tablet' | 'mobile';
type Theme = 'light' | 'dark';
type Tab = 'dashboard' | 'workspace' | 'components' | 'navigation' | 'settings';

const DARK_CSS = `
[data-theme="dark"] {
  --color-surface: #1c1c20;
  --color-surface-hover: #2a2a30;
  --color-surface-raised: #2a2a30;
  --color-border: #3a3a42;
  --color-bg-surface-default: #161619;
  --color-text-primary: #f2f2f5;
  --color-text-secondary: #b8b8c2;
  --color-text-tertiary: #80808c;
  --color-text-disabled: #5a5a64;
  --color-primary: #7aa2ff;
  --color-primary-alpha: rgba(122,162,255,0.16);
  --color-success: #4ade80;
  --color-warning: #fbbf24;
  --color-danger: #f87171;
  --color-error: #f87171;
  --color-info: #60a5fa;
  --color-bg-success-weak: rgba(74,222,128,0.16);
  --color-bg-warning-weak: rgba(251,191,36,0.16);
  --color-bg-danger-weak: rgba(248,113,113,0.16);
  --color-bg-info-weak: rgba(96,165,250,0.16);
  --color-text-success: #4ade80;
  --color-text-warning: #fbbf24;
  --color-text-danger: #f87171;
  --color-text-info: #60a5fa;
}
`;

const VIEWPORT_WIDTH: Record<Viewport, number | string> = {
  desktop: '100%',
  tablet: 820,
  mobile: 390,
};

const TABS: { id: Tab; label: string }[] = [
  { id: 'dashboard', label: 'Dashboard' },
  { id: 'workspace', label: 'Workspace' },
  { id: 'components', label: 'Components' },
  { id: 'navigation', label: 'Navigation' },
  { id: 'settings', label: 'Settings' },
];

export const InventoryPreviewApp: React.FC = () => {
  const [viewport, setViewport] = useState<Viewport>('desktop');
  const [theme, setTheme] = useState<Theme>('light');
  const [tab, setTab] = useState<Tab>('dashboard');

  return (
    <PermissionProvider initialRole="administrator">
      <InventoryWorkspaceProvider>
      <div
        data-theme={theme}
        style={{
          minHeight: '100%',
          background: 'var(--color-bg-surface-default)',
          color: 'var(--color-text-primary)',
          padding: 16,
          display: 'flex',
          flexDirection: 'column',
          gap: 16,
        }}
      >
        <style>{DARK_CSS}</style>

        <header style={{ display: 'flex', flexWrap: 'wrap', gap: 12, alignItems: 'center', justifyContent: 'space-between' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
            <div style={{ width: 36, height: 36, borderRadius: 'var(--radius-md)', background: 'var(--color-primary-alpha)', color: 'var(--color-primary)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
              <Icon name="archive" size={20} />
            </div>
            <div>
              <h1 style={{ margin: 0, fontSize: 'var(--text-h2)', fontWeight: 700 }}>Inventory Preview</h1>
              <p style={{ margin: '2px 0 0', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>
                Frontend-only mock-mode exploration of the Inventory domain.
              </p>
            </div>
          </div>

          <div style={{ display: 'flex', gap: 8, flexWrap: 'wrap' }}>
            <div role="group" aria-label="Viewport" style={{ display: 'inline-flex', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)', overflow: 'hidden' }}>
              {(['desktop', 'tablet', 'mobile'] as Viewport[]).map((v) => (
                <button
                  key={v}
                  onClick={() => setViewport(v)}
                  aria-pressed={viewport === v}
                  style={{
                    display: 'inline-flex',
                    alignItems: 'center',
                    gap: 6,
                    padding: '8px 12px',
                    border: 'none',
                    background: viewport === v ? 'var(--color-primary)' : 'var(--color-surface)',
                    color: viewport === v ? '#fff' : 'var(--color-text-secondary)',
                    cursor: 'pointer',
                    fontSize: 'var(--text-body-sm)',
                    textTransform: 'capitalize',
                  }}
                >
                  <Icon name={v === 'desktop' ? 'monitor' : v === 'tablet' ? 'tablet' : 'smartphone'} size={14} />
                  {v}
                </button>
              ))}
            </div>
            <button
              onClick={() => setTheme((t) => (t === 'light' ? 'dark' : 'light'))}
              aria-pressed={theme === 'dark'}
              style={{
                display: 'inline-flex',
                alignItems: 'center',
                gap: 6,
                padding: '8px 14px',
                border: '1px solid var(--color-border)',
                borderRadius: 'var(--radius-md)',
                background: 'var(--color-surface)',
                color: 'var(--color-text-primary)',
                cursor: 'pointer',
                fontSize: 'var(--text-body-sm)',
              }}
            >
              <Icon name={theme === 'light' ? 'sun' : 'moon'} size={14} />
              {theme === 'light' ? 'Light' : 'Dark'}
            </button>
          </div>
        </header>

        <nav role="tablist" aria-label="Preview sections" style={{ display: 'flex', flexWrap: 'wrap', gap: 4, borderBottom: '1px solid var(--color-border)', paddingBottom: 8 }}>
          {TABS.map((t) => (
            <button
              key={t.id}
              role="tab"
              aria-selected={tab === t.id}
              onClick={() => setTab(t.id)}
              style={{
                padding: '8px 16px',
                border: 'none',
                borderBottom: tab === t.id ? '2px solid var(--color-primary)' : '2px solid transparent',
                background: 'transparent',
                color: tab === t.id ? 'var(--color-primary)' : 'var(--color-text-secondary)',
                fontWeight: tab === t.id ? 600 : 400,
                cursor: 'pointer',
                fontSize: 'var(--text-body)',
              }}
            >
              {t.label}
            </button>
          ))}
        </nav>

        <details style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)', padding: '8px 12px', background: 'var(--color-surface)' }}>
          <summary style={{ cursor: 'pointer', fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)' }}>Accessibility Notes</summary>
          <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-tertiary)', lineHeight: 1.5 }}>
            All interactive components are keyboard operable with visible focus, role, and ARIA attributes. Tables use semantic &lt;table&gt; with &lt;th scope&gt;. Loading regions expose aria-busy and labelled skeletons. Banners use role=&quot;status&quot;/&quot;alert&quot;. Targets meet WCAG 2.2 AA contrast and sizing guidance.
          </p>
        </details>

        <details style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)', padding: '8px 12px', background: 'var(--color-surface)' }}>
          <summary style={{ cursor: 'pointer', fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)' }}>Performance Notes</summary>
          <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-tertiary)', lineHeight: 1.5 }}>
            Components are wrapped in React.memo. Derived data is computed with useMemo; event handlers use useCallback. Mock services add ~400–700ms artificial latency to mimic network without real I/O. No inline object recreation occurs in hot render paths. Layouts use CSS grid auto-fit for adapter responsiveness.
          </p>
        </details>

        <div
          style={{
            width: VIEWPORT_WIDTH[viewport],
            maxWidth: '100%',
            margin: '0 auto',
            border: '1px solid var(--color-border)',
            borderRadius: 'var(--radius-lg)',
            overflow: 'hidden',
            background: 'var(--color-surface)',
          }}
        >
          {tab === 'dashboard' && <InventoryPreviewDashboard />}
          {tab === 'workspace' && <InventoryPreviewWorkspace />}
          {tab === 'components' && <InventoryPreviewComponents />}
          {tab === 'navigation' && <InventoryPreviewNavigation />}
          {tab === 'settings' && <InventoryPreviewSettings />}
        </div>
      </div>
      </InventoryWorkspaceProvider>
    </PermissionProvider>
  );
};

export default InventoryPreviewApp;

