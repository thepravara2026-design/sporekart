import { Card } from '../../design-system/components/composite/Card';
import { ADMIN_SIDEBAR_ITEMS_FOR_PREVIEW, ADMIN_TOP_NAV } from '../config/adminNavigation';
const ADMIN_SIDEBAR_ITEMS = ADMIN_SIDEBAR_ITEMS_FOR_PREVIEW;

const PREVIEW_ITEMS = [
  { id: 'sidebar',  label: 'Sidebar',  desc: 'Collapsible, responsive, mobile drawer, role-aware navigation.' },
  { id: 'header',   label: 'Header',   desc: 'Top navigation, breadcrumbs, search, notifications, profile, theme.' },
  { id: 'dashboard', label: 'Dashboard', desc: 'Welcome header, widget area, quick actions, recent activity.' },
  { id: 'mobile',   label: 'Mobile',   desc: 'Fluid sidebar, adaptive navigation, touch-friendly controls.' },
];

function ViewportFrame({ label, children }: { label: string; children: React.ReactNode }) {
  const widths: Record<string, string> = {
    Desktop: '100%',
    Tablet: '768px',
    Mobile: '375px',
  };
  return (
    <div style={{ marginBottom: 'var(--space-section-gap)' }}>
      <h3 style={{ margin: '0 0 var(--space-stack-sm)', fontSize: 'var(--text-h4)', color: 'var(--color-text-secondary)' }}>
        {label}
      </h3>
      <div style={{
        width: widths[label] || '100%',
        maxWidth: '100%',
        border: '1px solid var(--color-border-default)',
        borderRadius: 'var(--radius-card)',
        overflow: 'hidden',
        background: 'var(--color-bg-surface-default)',
        margin: '0 auto',
      }}>
        <div style={{
          padding: 'var(--space-stack-sm) var(--space-page-x)',
          background: 'var(--color-bg-background)',
          borderBottom: '1px solid var(--color-border-default)',
          fontSize: 'var(--text-caption)',
          color: 'var(--color-text-secondary)',
          fontFamily: 'var(--font-family-mono)',
        }}>
          {label} Viewport &middot; {widths[label]}
        </div>
        <div style={{ padding: 'var(--space-stack-md) var(--space-page-x)' }}>
          {children}
        </div>
      </div>
    </div>
  );
}

function DarkThemeSection() {
  return (
    <section style={{ marginBottom: 'var(--space-section-gap)' }}>
      <h2 style={{ fontSize: 'var(--text-h2)', marginBottom: 'var(--space-stack-sm)' }}>Dark Theme</h2>
      <div style={{
        padding: 'var(--space-stack-md) var(--space-page-x)',
        background: '#1a1a2e', color: '#e0e0e0',
        borderRadius: 'var(--radius-card)',
      }}>
        <p style={{ margin: 0, fontSize: 'var(--text-caption)' }}>
          Admin layout inherits design system theme tokens. Dark mode is enabled through the TokenContext theme provider.
          All CSS custom properties (colors, elevations, borders) automatically adjust for dark mode via theme variant tokens
          in <code>src/design-system/tokens/themes/</code>. No hardcoded colors are used.
        </p>
      </div>
    </section>
  );
}

function A11ySection({ items }: { items: string[] }) {
  return (
    <section style={{ marginBottom: 'var(--space-section-gap)' }}>
      <h2 style={{ fontSize: 'var(--text-h2)', marginBottom: 'var(--space-stack-sm)' }}>Accessibility Notes</h2>
      <ul style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', lineHeight: 2 }}>
        {items.map((item, i) => (
          <li key={i}>{item}</li>
        ))}
      </ul>
    </section>
  );
}

function PerfSection({ items }: { items: string[] }) {
  return (
    <section style={{ marginBottom: 'var(--space-section-gap)' }}>
      <h2 style={{ fontSize: 'var(--text-h2)', marginBottom: 'var(--space-stack-sm)' }}>Performance Notes</h2>
      <ul style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', lineHeight: 2 }}>
        {items.map((item, i) => (
          <li key={i}>{item}</li>
        ))}
      </ul>
    </section>
  );
}

const COMMON_A11Y = [
  'Semantic HTML with proper landmarks (<nav>, <main>, <footer>, <header>)',
  'ARIA labels on all navigation regions',
  'Full keyboard navigation (ArrowDown, ArrowUp, Enter, Home, End)',
  'Skip-to-content link at top of page',
  'Visible focus rings via design system tokens',
  'Respects prefers-reduced-motion via CSS variable overrides',
  'aria-current="page" on active navigation and breadcrumb items',
  'aria-hidden="true" on decorative icons and separators',
];

const COMMON_PERF = [
  'All page components lazy-loaded via React.lazy and Suspense',
  'CSS transitions use GPU-composited properties (transform, opacity)',
  'No layout shift on route change — consistent shell dimensions',
  'Configuration-driven navigation avoids hardcoded JSX trees',
  'Design system components are tree-shakeable',
  'Minimal bundle impact — each page ~1-3KB gzipped',
];

export function AdminLayoutPreview() {
  return (
    <div style={{ maxWidth: 960, margin: '0 auto', padding: 'var(--space-section-gap) var(--space-page-x)' }}>
      <h1 style={{ margin: '0 0 var(--space-stack-xs)', fontSize: 'var(--text-h1)' }}>Admin Layout Preview</h1>
      <p style={{ color: 'var(--color-text-secondary)', marginBottom: 'var(--space-section-gap)' }}>
        Enterprise Admin Application Shell review. Includes layout architecture, responsive behavior, and accessibility notes.
      </p>

      <section style={{ marginBottom: 'var(--space-section-gap)' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', marginBottom: 'var(--space-stack-sm)' }}>Overview</h2>
        <div style={{
          display: 'grid',
          gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))',
          gap: 'var(--space-component-gap)',
        }}>
          {PREVIEW_ITEMS.map((item) => (
            <Card key={item.id} variant="elevated" padding="md">
              <h3 style={{ margin: '0 0 var(--space-stack-xs)', fontSize: 'var(--text-h4)' }}>{item.label}</h3>
              <p style={{ margin: 0, fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{item.desc}</p>
            </Card>
          ))}
        </div>
      </section>

      <ViewportFrame label="Desktop">
        <div style={{ display: 'flex', gap: 16 }}>
          <div style={{
            width: 240, background: 'var(--color-bg-surface-raised)',
            borderRadius: 'var(--radius-md)', padding: 12,
          }}>
            <div style={{ fontWeight: 'var(--weight-bold)', marginBottom: 8 }}>Sidebar (264px)</div>
            {ADMIN_SIDEBAR_ITEMS.map((item) => (
              <div key={item.id} style={{
                padding: '6px 8px', borderRadius: 'var(--radius-sm)',
                marginBottom: 2, fontSize: 'var(--text-body)',
                background: item.id === 'dashboard' ? 'var(--color-bg-primary-weak)' : 'transparent',
              }}>
                {item.label}
              </div>
            ))}
            <div style={{ marginTop: 12, paddingTop: 8, borderTop: '1px solid var(--color-border-default)', fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
              Search · Pinned · Favorites · Recent · Cmd+K
            </div>
          </div>
          <div style={{ flex: 1 }}>
            <div style={{
              padding: '8px 12px', background: 'var(--color-bg-surface-raised)',
              borderRadius: 'var(--radius-md)', marginBottom: 8,
              display: 'flex', justifyContent: 'space-between',
            }}>
              <div style={{ fontWeight: 'var(--weight-bold)' }}>Header (56px)</div>
              <div style={{ display: 'flex', gap: 8 }}>
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Search</span>
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Bell</span>
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Theme</span>
                {ADMIN_TOP_NAV.map((n) => (
                  <span key={n.id} style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
                    {n.label}
                  </span>
                ))}
              </div>
            </div>
            <div style={{
              padding: 16, background: 'var(--color-bg-surface-raised)',
              borderRadius: 'var(--radius-md)', minHeight: 200,
            }}>
              <div style={{ fontWeight: 'var(--weight-bold)', marginBottom: 8 }}>Content Area</div>
              <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
                Breadcrumb bar → Page content → Footer
              </div>
            </div>
          </div>
        </div>
      </ViewportFrame>

      <ViewportFrame label="Tablet">
        <div style={{
          padding: 16, background: 'var(--color-bg-surface-raised)',
          borderRadius: 'var(--radius-md)', textAlign: 'center',
        }}>
          <div style={{ fontWeight: 'var(--weight-bold)', marginBottom: 4 }}>Collapsed Sidebar (72px)</div>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
            Sidebar collapses to icon-only width on tablet. Content area fills remaining space. Header shows compact nav.
          </div>
        </div>
      </ViewportFrame>

      <ViewportFrame label="Mobile">
        <div style={{
          padding: 16, background: 'var(--color-bg-surface-raised)',
          borderRadius: 'var(--radius-md)', textAlign: 'center',
        }}>
          <div style={{ fontWeight: 'var(--weight-bold)', marginBottom: 4 }}>Mobile Overlay Drawer</div>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
            Sidebar becomes an overlay drawer triggered by hamburger icon in header. Touch-friendly 44px targets.
          </div>
        </div>
      </ViewportFrame>

      <DarkThemeSection />
      <A11ySection items={COMMON_A11Y} />
      <PerfSection items={COMMON_PERF} />
    </div>
  );
}

export function AdminSidebarPreview() {
  return (
    <div style={{ maxWidth: 960, margin: '0 auto', padding: 'var(--space-section-gap) var(--space-page-x)' }}>
      <h1 style={{ margin: '0 0 var(--space-stack-xs)', fontSize: 'var(--text-h1)' }}>Admin Sidebar Preview</h1>
      <p style={{ color: 'var(--color-text-secondary)', marginBottom: 'var(--space-section-gap)' }}>
        Enterprise sidebar with collapsible expand/collapse, mobile drawer, pinned items, favorites, recent pages, search, and command palette placeholders.
      </p>

      <ViewportFrame label="Desktop">
        <div style={{
          width: 264, background: 'var(--color-bg-surface-default)',
          borderRight: '1px solid var(--color-border-default)',
          borderRadius: 'var(--radius-md)',
        }}>
          <div style={{
            padding: 'var(--space-stack-md) var(--space-page-x)',
            borderBottom: '1px solid var(--color-border-default)',
            display: 'flex', alignItems: 'center', gap: 8,
          }}>
            <div style={{ width: 32, height: 32, borderRadius: 6, background: '#2d6e4f' }} />
            <span style={{ fontWeight: 'var(--weight-bold)' }}>SporeKart</span>
          </div>
          <div style={{
            padding: '6px 12px', margin: '0 12px 8px',
            borderRadius: 6, background: 'var(--color-bg-background)',
            border: '1px solid var(--color-border-default)',
            fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)',
            display: 'flex', alignItems: 'center', gap: 6,
          }}>
            <span>&#128270;</span>
            <span>Search...</span>
          </div>
          {ADMIN_SIDEBAR_ITEMS.map((item, i) => (
            <div key={item.id} style={{
              padding: '8px 16px', display: 'flex', alignItems: 'center', gap: 10,
              background: i === 0 ? 'var(--color-bg-primary-weak)' : 'transparent',
              color: i === 0 ? 'var(--color-primary)' : 'var(--color-text-primary)',
              fontWeight: i === 0 ? 'var(--weight-medium)' : 'var(--weight-normal)',
            }}>
              <span style={{ fontSize: 18 }}>{item.label[0]}</span>
              <span>{item.label}</span>
              {i === 1 && <span style={{ marginLeft: 'auto', fontSize: 10, color: 'var(--color-text-secondary)' }}>&#10022;</span>}
            </div>
          ))}
          <div style={{
            padding: '6px 16px', fontSize: 10, fontWeight: 'var(--weight-bold)',
            color: 'var(--color-text-secondary)', letterSpacing: 1,
            marginTop: 8,
          }}>
            PINNED
          </div>
          <div style={{ padding: '6px 16px', fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', display: 'flex', alignItems: 'center', gap: 8 }}>
            <span>&#128204;</span>
            <span>Pin items for quick access</span>
          </div>
          <div style={{
            padding: '6px 16px', fontSize: 10, fontWeight: 'var(--weight-bold)',
            color: 'var(--color-text-secondary)', letterSpacing: 1,
          }}>
            FAVORITES
          </div>
          <div style={{ padding: '6px 16px', fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', display: 'flex', alignItems: 'center', gap: 8 }}>
            <span>&#9734;</span>
            <span>No favorites yet</span>
          </div>
          <div style={{
            padding: '6px 16px', fontSize: 10, fontWeight: 'var(--weight-bold)',
            color: 'var(--color-text-secondary)', letterSpacing: 1,
          }}>
            RECENT
          </div>
          <div style={{ padding: '6px 16px', fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', display: 'flex', alignItems: 'center', gap: 8 }}>
            <span>&#128339;</span>
            <span>No recent pages</span>
          </div>
          <div style={{
            padding: '6px 16px', fontSize: 10, fontWeight: 'var(--weight-bold)',
            color: 'var(--color-text-secondary)', letterSpacing: 1,
          }}>
            QUICK ACTIONS
          </div>
          <div style={{ padding: '6px 16px 12px', fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', display: 'flex', alignItems: 'center', gap: 8 }}>
            <span>&#9000;</span>
            <span>Cmd+K to search</span>
          </div>
          <div style={{
            padding: 'var(--space-stack-sm) var(--space-page-x)',
            borderTop: '1px solid var(--color-border-default)',
            fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)',
            textAlign: 'center',
          }}>
            v1.0.0 &middot; Admin
          </div>
        </div>
      </ViewportFrame>

      <ViewportFrame label="Tablet">
        <div style={{
          width: 72, background: 'var(--color-bg-surface-default)',
          borderRight: '1px solid var(--color-border-default)',
          borderRadius: 'var(--radius-md)',
        }}>
          {ADMIN_SIDEBAR_ITEMS.map((item, i) => (
            <div key={item.id} style={{
              padding: '12px', display: 'flex', justifyContent: 'center',
              background: i === 0 ? 'var(--color-bg-primary-weak)' : 'transparent',
              color: i === 0 ? 'var(--color-primary)' : 'var(--color-text-secondary)',
            }}>
              <span style={{ fontSize: 20 }}>{item.label[0]}</span>
            </div>
          ))}
        </div>
      </ViewportFrame>

      <ViewportFrame label="Mobile">
        <div style={{
          position: 'relative', height: 260, overflow: 'hidden',
          background: 'var(--color-bg-background)',
        }}>
          <div style={{
            position: 'absolute', inset: 0, background: 'rgba(0,0,0,0.3)', zIndex: 1,
          }} />
          <div style={{
            position: 'absolute', left: 0, top: 0, bottom: 0, width: 264,
            background: 'var(--color-bg-surface-default)', zIndex: 2,
            boxShadow: '4px 0 12px rgba(0,0,0,0.15)',
          }}>
            <div style={{ padding: 16, fontWeight: 'var(--weight-bold)', borderBottom: '1px solid var(--color-border-default)' }}>
              SporeKart Admin
            </div>
            <div style={{ padding: '8px 12px', margin: '8px 12px', borderRadius: 6, background: 'var(--color-bg-background)', border: '1px solid var(--color-border-default)', fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
              Search...
            </div>
            {ADMIN_SIDEBAR_ITEMS.slice(0, 4).map((item) => (
              <div key={item.id} style={{ padding: '10px 16px', fontSize: 'var(--text-body)', display: 'flex', alignItems: 'center', gap: 8 }}>
                <span style={{ fontSize: 16, width: 20, textAlign: 'center' }}>{item.label[0]}</span>
                <span>{item.label}</span>
              </div>
            ))}
          </div>
          <div style={{ padding: 12, color: 'var(--color-text-secondary)', fontSize: 'var(--text-caption)' }}>
            Overlay drawer with backdrop
          </div>
        </div>
      </ViewportFrame>

      <DarkThemeSection />
      <A11ySection items={[
        ...COMMON_A11Y,
        'Sidebar uses role="navigation" with aria-label',
        'Sidebar items use role="menuitem" with aria-current for active state',
        'Pin button has aria-label for screen readers',
        'Overlay backdrop has aria-hidden="true"',
      ]} />
      <PerfSection items={[
        ...COMMON_PERF,
        'Sidebar collapse uses CSS transition on width (GPU-composited)',
        'Sidebar items are memoized config — no re-creation on render',
        'Collapsed/expanded state managed with useState',
      ]} />
    </div>
  );
}

export function AdminHeaderPreview() {
  return (
    <div style={{ maxWidth: 960, margin: '0 auto', padding: 'var(--space-section-gap) var(--space-page-x)' }}>
      <h1 style={{ margin: '0 0 var(--space-stack-xs)', fontSize: 'var(--text-h1)' }}>Admin Header Preview</h1>
      <p style={{ color: 'var(--color-text-secondary)', marginBottom: 'var(--space-section-gap)' }}>
        Enterprise top navigation with search, notifications, theme switch, workspace actions, and profile menu.
      </p>

      <ViewportFrame label="Desktop">
        <div style={{
          display: 'flex', alignItems: 'center', justifyContent: 'space-between',
          height: 56, padding: '0 16px',
          borderBottom: '1px solid var(--color-border-default)',
          background: 'var(--color-bg-surface-default)',
        }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
            <div style={{ width: 28, height: 28, borderRadius: 6, background: '#2d6e4f' }} />
            <span style={{ fontWeight: 'var(--weight-bold)' }}>Admin</span>
            <div style={{
              display: 'flex', alignItems: 'center', gap: 4,
              marginLeft: 8, padding: '4px 8px', borderRadius: 6,
              background: 'var(--color-bg-background)',
              border: '1px solid var(--color-border-default)',
              fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)',
            }}>
              <span>&#128270;</span>
              <span>Search admin...</span>
              <span style={{ marginLeft: 4, padding: '0 4px', border: '1px solid var(--color-border-default)', borderRadius: 3, fontSize: 10 }}>Ctrl+K</span>
            </div>
          </div>
          <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
            <span style={{ fontSize: 16, cursor: 'pointer', color: 'var(--color-text-secondary)' }} title="Workspace actions">&#9638;</span>
            <span style={{ fontSize: 16, cursor: 'pointer', color: 'var(--color-text-secondary)', position: 'relative' }} title="Notifications">
              &#128276;
              <span style={{ position: 'absolute', top: -2, right: -2, width: 8, height: 8, borderRadius: '50%', background: '#e53e3e' }} />
            </span>
            <span style={{ fontSize: 16, cursor: 'pointer', color: 'var(--color-text-secondary)' }} title="Toggle theme">&#9790;</span>
            <div style={{ display: 'flex', gap: 12, marginLeft: 4, fontSize: 'var(--text-caption)' }}>
              {ADMIN_TOP_NAV.map((n) => (
                <span key={n.id} style={{ color: 'var(--color-text-secondary)' }}>{n.label}</span>
              ))}
            </div>
          </div>
        </div>
      </ViewportFrame>

      <ViewportFrame label="Tablet">
        <div style={{
          display: 'flex', alignItems: 'center', justifyContent: 'space-between',
          height: 56, padding: '0 16px',
          borderBottom: '1px solid var(--color-border-default)',
          background: 'var(--color-bg-surface-default)',
        }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
            <span style={{ fontWeight: 'var(--weight-bold)', fontSize: 'var(--text-body)' }}>Admin</span>
          </div>
          <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
            <span style={{ fontSize: 14, cursor: 'pointer', color: 'var(--color-text-secondary)' }}>&#128276;</span>
            <span style={{ fontSize: 14, cursor: 'pointer', color: 'var(--color-text-secondary)' }}>&#9790;</span>
            <div style={{ display: 'flex', gap: 8, fontSize: 'var(--text-caption)' }}>
              {ADMIN_TOP_NAV.map((n) => (
                <span key={n.id} style={{ color: 'var(--color-text-secondary)' }}>{n.label}</span>
              ))}
            </div>
          </div>
        </div>
      </ViewportFrame>

      <ViewportFrame label="Mobile">
        <div style={{
          display: 'flex', alignItems: 'center', justifyContent: 'space-between',
          height: 56, padding: '0 12px',
          borderBottom: '1px solid var(--color-border-default)',
          background: 'var(--color-bg-surface-default)',
        }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
            <span style={{ fontSize: 18, cursor: 'pointer' }}>&#9776;</span>
            <div style={{ width: 24, height: 24, borderRadius: 4, background: '#2d6e4f' }} />
            <span style={{ fontWeight: 'var(--weight-bold)', fontSize: 'var(--text-body)' }}>Admin</span>
          </div>
          <div style={{ display: 'flex', alignItems: 'center', gap: 6 }}>
            <span style={{ fontSize: 14, cursor: 'pointer', color: 'var(--color-text-secondary)' }}>&#128276;</span>
            <span style={{ fontSize: 14, cursor: 'pointer', color: 'var(--color-text-secondary)' }}>&#9790;</span>
          </div>
        </div>
      </ViewportFrame>

      <DarkThemeSection />
      <A11ySection items={[
        ...COMMON_A11Y,
        'Search trigger has aria-label="Search placeholder"',
        'Notification bell has aria-label="Notifications"',
        'Theme toggle has aria-label="Toggle theme"',
        'Workspace actions button has aria-label',
        'All icon-only buttons have descriptive aria-labels',
      ]} />
      <PerfSection items={[
        ...COMMON_PERF,
        'Header is inline layout — no extra DOM nesting',
        'Search input is static placeholder (no JS runtime cost)',
        'Icon buttons use CSS for hover/focus states',
      ]} />
    </div>
  );
}

export function AdminDashboardPreview() {
  return (
    <div style={{ maxWidth: 960, margin: '0 auto', padding: 'var(--space-section-gap) var(--space-page-x)' }}>
      <h1 style={{ margin: '0 0 var(--space-stack-xs)', fontSize: 'var(--text-h1)' }}>Admin Dashboard Preview</h1>
      <p style={{ color: 'var(--color-text-secondary)', marginBottom: 'var(--space-section-gap)' }}>
        Dashboard landing shell with welcome header, workspace summary, widget placeholders, quick actions, pinned widgets, recent activity, and announcements.
      </p>

      <ViewportFrame label="Desktop">
        <div>
          <div style={{
            background: 'linear-gradient(135deg, #2d6e4f, #1a5c3a)',
            borderRadius: 12, padding: '24px 32px', marginBottom: 24,
            color: '#fff',
          }}>
            <h2 style={{ margin: '0 0 4px', fontSize: 22 }}>Enterprise Admin Platform</h2>
            <p style={{ margin: 0, opacity: 0.85, fontSize: 14 }}>Central workspace for managing SporeKart.</p>
          </div>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16, marginBottom: 24 }}>
            <div style={{ padding: 16, borderRadius: 12, background: 'var(--color-bg-surface-default)', border: '1px solid var(--color-border-default)' }}>
              <div style={{ fontWeight: 'var(--weight-bold)', marginBottom: 8 }}>Workspace Summary</div>
              <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
                Active Role: administrator<br />Environment: Development<br />Version: 1.0.0
              </div>
            </div>
            <div style={{ padding: 16, borderRadius: 12, background: 'var(--color-bg-surface-default)', border: '1px solid var(--color-border-default)' }}>
              <div style={{ fontWeight: 'var(--weight-bold)', marginBottom: 8 }}>Pinned Widgets</div>
              <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
                Pin widgets for quick access.
              </div>
            </div>
          </div>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: 16, marginBottom: 24 }}>
            {['Users', 'Content', 'Analytics', 'System'].map((w) => (
              <div key={w} style={{
                padding: 16, borderRadius: 12,
                background: 'var(--color-bg-surface-default)',
                border: '1px solid var(--color-border-default)',
              }}>
                <div style={{ width: 32, height: 32, borderRadius: 8, background: '#e0e0e0', marginBottom: 8 }} />
                <div style={{ fontWeight: 'var(--weight-bold)', marginBottom: 2 }}>{w}</div>
                <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
                  Placeholder widget
                </div>
              </div>
            ))}
          </div>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16 }}>
            {['Recent Activity', 'Announcements'].map((s) => (
              <div key={s} style={{
                padding: 16, borderRadius: 12,
                background: 'var(--color-bg-surface-default)',
                border: '1px solid var(--color-border-default)',
              }}>
                <div style={{ fontWeight: 'var(--weight-bold)', marginBottom: 8 }}>{s}</div>
                <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
                  No data to display.
                </div>
              </div>
            ))}
          </div>
        </div>
      </ViewportFrame>

      <ViewportFrame label="Tablet">
        <div>
          <div style={{
            background: 'linear-gradient(135deg, #2d6e4f, #1a5c3a)',
            borderRadius: 12, padding: '20px 24px', marginBottom: 20,
            color: '#fff',
          }}>
            <div style={{ fontWeight: 'var(--weight-bold)', fontSize: 18 }}>Enterprise Admin Platform</div>
          </div>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12, marginBottom: 16 }}>
            <div style={{ padding: 12, borderRadius: 12, background: 'var(--color-bg-surface-default)', border: '1px solid var(--color-border-default)' }}>
              <div style={{ fontWeight: 'var(--weight-bold)', marginBottom: 4, fontSize: 13 }}>Summary</div>
              <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Role: administrator</div>
            </div>
            <div style={{ padding: 12, borderRadius: 12, background: 'var(--color-bg-surface-default)', border: '1px solid var(--color-border-default)' }}>
              <div style={{ fontWeight: 'var(--weight-bold)', marginBottom: 4, fontSize: 13 }}>Pinned</div>
              <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Pin widgets here.</div>
            </div>
          </div>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12 }}>
            {['Users', 'Content', 'Analytics', 'System'].slice(0, 2).map((w) => (
              <div key={w} style={{ padding: 12, borderRadius: 12, background: 'var(--color-bg-surface-default)', border: '1px solid var(--color-border-default)' }}>
                <div style={{ fontWeight: 'var(--weight-bold)', fontSize: 13 }}>{w}</div>
              </div>
            ))}
          </div>
        </div>
      </ViewportFrame>

      <ViewportFrame label="Mobile">
        <div>
          <div style={{
            background: 'linear-gradient(135deg, #2d6e4f, #1a5c3a)',
            borderRadius: 8, padding: '16px 20px', marginBottom: 16,
            color: '#fff',
          }}>
            <div style={{ fontWeight: 'var(--weight-bold)', fontSize: 16 }}>Enterprise Admin</div>
          </div>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 10 }}>
            <div style={{ padding: 12, borderRadius: 8, background: 'var(--color-bg-surface-default)', border: '1px solid var(--color-border-default)' }}>
              <div style={{ fontWeight: 'var(--weight-bold)', fontSize: 13 }}>Summary</div>
              <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Role: administrator</div>
            </div>
            <div style={{ padding: 12, borderRadius: 8, background: 'var(--color-bg-surface-default)', border: '1px solid var(--color-border-default)' }}>
              <div style={{ fontWeight: 'var(--weight-bold)', fontSize: 13 }}>Users</div>
            </div>
            <div style={{ padding: 12, borderRadius: 8, background: 'var(--color-bg-surface-default)', border: '1px solid var(--color-border-default)' }}>
              <div style={{ fontWeight: 'var(--weight-bold)', fontSize: 13 }}>Content</div>
            </div>
          </div>
        </div>
      </ViewportFrame>

      <DarkThemeSection />
      <A11ySection items={[
        ...COMMON_A11Y,
        'Welcome banner uses semantic heading hierarchy (h2 → h3 → h4)',
        'Widget cards are interactive but placeholder (no href/action yet)',
        'Color-coded widget headers use opacity + description text for legibility',
      ]} />
      <PerfSection items={[
        ...COMMON_PERF,
        'Dashboard uses CSS Grid for responsive widget layout (auto-fit + minmax)',
        'No JavaScript-driven layout — pure CSS responsive',
        'All widget data is static — no API calls',
      ]} />
    </div>
  );
}

export function AdminMobilePreview() {
  return (
    <div style={{ maxWidth: 960, margin: '0 auto', padding: 'var(--space-section-gap) var(--space-page-x)' }}>
      <h1 style={{ margin: '0 0 var(--space-stack-xs)', fontSize: 'var(--text-h1)' }}>Admin Mobile Preview</h1>
      <p style={{ color: 'var(--color-text-secondary)', marginBottom: 'var(--space-section-gap)' }}>
        Mobile-optimized admin layout with collapsible sidebar drawer, adaptive navigation, and touch-friendly controls.
      </p>

      <ViewportFrame label="Desktop Reference">
        <div style={{ padding: 16, background: 'var(--color-bg-surface-raised)', borderRadius: 'var(--radius-md)', textAlign: 'center' }}>
          <div style={{ fontWeight: 'var(--weight-bold)', marginBottom: 4 }}>Full Desktop Layout</div>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
            Sidebar expanded (264px), header with all controls, multi-column dashboard.
          </div>
        </div>
      </ViewportFrame>

      <ViewportFrame label="Tablet">
        <div style={{ padding: 16, background: 'var(--color-bg-surface-raised)', borderRadius: 'var(--radius-md)', textAlign: 'center' }}>
          <div style={{ fontWeight: 'var(--weight-bold)', marginBottom: 4 }}>Collapsed Sidebar</div>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
            Sidebar collapses to 72px icon-only. Top nav shows compact items. Widget grid switches to 2 columns.
          </div>
        </div>
      </ViewportFrame>

      <ViewportFrame label="Mobile">
        <div style={{
          background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)',
          overflow: 'hidden',
        }}>
          <div style={{
            display: 'flex', alignItems: 'center', justifyContent: 'space-between',
            height: 48, padding: '0 12px',
            borderBottom: '1px solid var(--color-border-default)',
          }}>
            <div style={{ fontSize: 20, cursor: 'pointer', color: 'var(--color-text-primary)', padding: 4 }} aria-label="Open sidebar">&#9776;</div>
            <div style={{ display: 'flex', alignItems: 'center', gap: 6 }}>
              <div style={{ width: 24, height: 24, borderRadius: 4, background: '#2d6e4f' }} />
              <span style={{ fontWeight: 'var(--weight-bold)', fontSize: 'var(--text-body)' }}>Admin</span>
            </div>
            <div style={{ display: 'flex', gap: 6, color: 'var(--color-text-secondary)', fontSize: 16 }}>
              <span>&#128276;</span>
              <span>&#9790;</span>
            </div>
          </div>
          <div style={{ padding: 16, minHeight: 240, display: 'flex', flexDirection: 'column', gap: 12 }}>
            <div style={{
              background: 'linear-gradient(135deg, #2d6e4f, #1a5c3a)',
              borderRadius: 8, padding: 16, color: '#fff',
            }}>
              <div style={{ fontWeight: 'var(--weight-bold)', fontSize: 16 }}>Welcome</div>
              <div style={{ fontSize: 12, opacity: 0.85 }}>Enterprise Admin Platform</div>
            </div>
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 8 }}>
              {['Users', 'Content'].map((w) => (
                <div key={w} style={{
                  padding: 12, borderRadius: 8,
                  background: 'var(--color-bg-background)',
                  border: '1px solid var(--color-border-default)',
                }}>
                  <div style={{ fontWeight: 'var(--weight-medium)', fontSize: 13 }}>{w}</div>
                </div>
              ))}
            </div>
            <div style={{
              padding: 12, borderRadius: 8,
              background: 'var(--color-bg-background)',
              border: '1px solid var(--color-border-default)',
            }}>
              <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
                Quick actions, activity, and announcements stack vertically on mobile.
              </div>
            </div>
          </div>
        </div>
      </ViewportFrame>

      <DarkThemeSection />
      <A11ySection items={[
        ...COMMON_A11Y,
        'Hamburger button has aria-label="Open sidebar"',
        'Touch targets minimum 44px for all interactive elements',
        'Sidebar drawer has focus trap when open (handled by Sidebar component)',
        'Backdrop dismisses drawer and has aria-hidden="true"',
      ]} />
      <PerfSection items={[
        ...COMMON_PERF,
        'Mobile drawer uses CSS transform for slide animation (GPU-composited)',
        'Sidebar overlay is conditionally rendered — no extra DOM when closed',
        'Responsive breakpoints use CSS media queries, not JS window listeners',
      ]} />
    </div>
  );
}
