import { useState } from 'react';
import { useFavorites } from '../favorites/useFavorites';
import { useRecentPages } from '../recent/useRecentPages';
import { useNotifications } from '../notifications/useNotifications';
import { usePreferences } from '../preferences/usePreferences';
import { NotificationCenter } from '../notifications/NotificationCenter';
import { NavigationSearch } from '../global-search/NavigationSearch';
import { NavEmptyState } from '../empty-states/NavEmptyState';
import { Icon } from '../../../design-system/icons/Icon';
import type { NavItem, NavNotification } from '../types';

const mockItems: NavItem[] = [
  { id: 'dashboard', label: 'Dashboard', icon: 'layout-dashboard', href: '/admin/dashboard' },
  { id: 'orders', label: 'Orders', icon: 'shopping-cart', href: '/admin/orders' },
  { id: 'products', label: 'Products', icon: 'package', children: [
    { id: 'all-products', label: 'All Products', href: '/admin/products' },
    { id: 'categories', label: 'Categories', href: '/admin/categories' },
    { id: 'inventory', label: 'Inventory', href: '/admin/inventory' },
  ]},
  { id: 'customers', label: 'Customers', icon: 'users', href: '/admin/customers' },
  { id: 'analytics', label: 'Analytics', icon: 'chart-bar', href: '/admin/analytics' },
];

const initialNotifications: NavNotification[] = [
  { id: 'n1', title: 'Order #1234 shipped', message: 'Dispatched successfully.', type: 'success', timestamp: '2 min ago', read: false },
  { id: 'n2', title: 'Low inventory alert', message: 'Wireless Mouse below threshold.', type: 'warning', timestamp: '15 min ago', read: false },
  { id: 'n3', title: 'Payment failed', message: 'Order #5678 was declined.', type: 'error', timestamp: '1 hour ago', read: false },
  { id: 'n4', title: 'Bulk import complete', message: '250 products imported.', type: 'success', timestamp: 'Yesterday', read: true },
];

export function NavigationPreview() {
  const { favorites, pinnedFavorites, addFavorite, removeFavorite, togglePin, isFavorite } = useFavorites();
  const { recent, addRecent, clearRecent } = useRecentPages();
  const { notifications, unreadCount, markRead, markAllRead, dismiss } = useNotifications(initialNotifications);
  const { preferences, setSidebarMode, setTableDensity, setTheme, setDashboardLayout, resetPreferences } = usePreferences();

  const [pageCounter, setPageCounter] = useState(0);

  const simulateVisit = () => {
    const id = `page-${pageCounter}`;
    addRecent({ id, label: `Page ${pageCounter + 1}`, href: `/admin/page-${pageCounter}` });
    setPageCounter((p) => p + 1);
  };

  const demoPages = [
    { id: 'dashboard', label: 'Dashboard', href: '/admin/dashboard', icon: 'layout-dashboard' },
    { id: 'orders', label: 'Orders', href: '/admin/orders', icon: 'shopping-cart' },
    { id: 'products', label: 'Products', href: '/admin/products', icon: 'package' },
    { id: 'customers', label: 'Customers', href: '/admin/customers', icon: 'users' },
  ];

  return (
    <div style={{ padding: 32, display: 'flex', flexDirection: 'column', gap: 32 }}>
      <h2 style={{ margin: 0 }}>Part 5 — Navigation Hooks & Framework</h2>

      {/* useFavorites */}
      <section>
        <h3 style={{ marginBottom: 8 }}>useFavorites Hook</h3>
        <div style={{ display: 'flex', gap: 8, flexWrap: 'wrap', marginBottom: 8 }}>
          {demoPages.map((p) => (
            <button
              key={p.id}
              onClick={() => isFavorite(p.id) ? removeFavorite(p.id) : addFavorite({ ...p, pinned: false })}
              style={{
                padding: '6px 14px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)',
                background: isFavorite(p.id) ? 'var(--color-primary-alpha)' : 'var(--color-surface)',
                cursor: 'pointer', display: 'flex', alignItems: 'center', gap: 6, color: 'var(--color-text-primary)',
              }}
            >
              {p.icon && <Icon name={p.icon} size={14} />}
              {p.label}
              <span style={{ color: isFavorite(p.id) ? 'var(--color-primary)' : 'var(--color-text-tertiary)' }}>
                {isFavorite(p.id) ? '★' : '☆'}
              </span>
            </button>
          ))}
        </div>
        <div style={{ display: 'flex', gap: 8 }}>
          {demoPages.map((p) => (
            <button key={`pin-${p.id}`} onClick={() => togglePin(p.id)} style={{ padding: '4px 8px', border: 'none', borderRadius: 'var(--radius-sm)', background: 'var(--color-surface-hover)', cursor: 'pointer', color: 'var(--color-text-secondary)', fontSize: 'var(--text-caption)' }}>
              {pinnedFavorites.some((f) => f.id === p.id) ? 'Unpin' : 'Pin'} {p.label}
            </button>
          ))}
        </div>
        <div style={{ marginTop: 8, padding: 8, border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)', fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
          <strong>Favorites:</strong> {favorites.length} items | <strong>Pinned:</strong> {pinnedFavorites.length} items
          <div style={{ display: 'flex', gap: 4, marginTop: 4 }}>
            {favorites.map((f) => (
              <span key={f.id} style={{ padding: '2px 8px', background: 'var(--color-surface-hover)', borderRadius: 'var(--radius-sm)' }}>
                {f.label}{f.pinned ? ' 📌' : ''}
              </span>
            ))}
            {favorites.length === 0 && <span style={{ color: 'var(--color-text-tertiary)' }}>No favorites yet. Click ☆ above.</span>}
          </div>
        </div>
      </section>

      {/* useRecentPages */}
      <section>
        <h3 style={{ marginBottom: 8 }}>useRecentPages Hook</h3>
        <button onClick={simulateVisit} style={{ padding: '6px 14px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)', background: 'var(--color-surface)', cursor: 'pointer', color: 'var(--color-text-primary)', marginBottom: 8 }}>
          Simulate page visit
        </button>
        <button onClick={clearRecent} style={{ marginLeft: 8, padding: '6px 14px', border: 'none', borderRadius: 'var(--radius-md)', background: 'var(--color-surface-hover)', cursor: 'pointer', color: 'var(--color-text-secondary)' }}>
          Clear
        </button>
        <div style={{ marginTop: 4, padding: 8, border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)', fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
          <div style={{ display: 'flex', gap: 4, flexWrap: 'wrap' }}>
            {recent.slice(0, 10).map((r) => (
              <span key={r.id} style={{ padding: '2px 8px', background: 'var(--color-surface-hover)', borderRadius: 'var(--radius-sm)' }}>{r.label}</span>
            ))}
            {recent.length === 0 && <span style={{ color: 'var(--color-text-tertiary)' }}>No recent pages. Click "Simulate page visit".</span>}
          </div>
          <div style={{ marginTop: 4, color: 'var(--color-text-tertiary)' }}>Total: {recent.length} / max 20</div>
        </div>
      </section>

      {/* useNotifications */}
      <section>
        <h3 style={{ marginBottom: 8 }}>useNotifications Hook</h3>
        <div style={{ display: 'flex', alignItems: 'center', gap: 16 }}>
          <NotificationCenter
            notifications={notifications}
            onMarkRead={markRead}
            onMarkAllRead={markAllRead}
            onDismiss={dismiss}
            unreadCount={unreadCount}
          />
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
            {unreadCount} unread / {notifications.length} total
          </span>
        </div>
      </section>

      {/* usePreferences */}
      <section>
        <h3 style={{ marginBottom: 8 }}>usePreferences Hook</h3>
        <div style={{ display: 'flex', gap: 16, flexWrap: 'wrap', padding: 12, border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)' }}>
          <div>
            <label style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', display: 'block', marginBottom: 4 }}>Sidebar Mode</label>
            <select value={preferences.sidebarMode} onChange={(e) => setSidebarMode(e.target.value as any)} style={{ padding: '4px 8px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border)', background: 'var(--color-surface)', color: 'var(--color-text-primary)' }}>
              <option value="expanded">Expanded</option>
              <option value="collapsed">Collapsed</option>
              <option value="mini">Mini</option>
              <option value="floating">Floating</option>
            </select>
          </div>
          <div>
            <label style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', display: 'block', marginBottom: 4 }}>Table Density</label>
            <select value={preferences.tableDensity} onChange={(e) => setTableDensity(e.target.value as any)} style={{ padding: '4px 8px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border)', background: 'var(--color-surface)', color: 'var(--color-text-primary)' }}>
              <option value="compact">Compact</option>
              <option value="comfortable">Comfortable</option>
              <option value="spacious">Spacious</option>
            </select>
          </div>
          <div>
            <label style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', display: 'block', marginBottom: 4 }}>Theme</label>
            <select value={preferences.theme} onChange={(e) => setTheme(e.target.value as any)} style={{ padding: '4px 8px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border)', background: 'var(--color-surface)', color: 'var(--color-text-primary)' }}>
              <option value="light">Light</option>
              <option value="dark">Dark</option>
              <option value="system">System</option>
            </select>
          </div>
          <div>
            <label style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', display: 'block', marginBottom: 4 }}>Dashboard Layout</label>
            <select value={preferences.dashboardLayout} onChange={(e) => setDashboardLayout(e.target.value as any)} style={{ padding: '4px 8px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border)', background: 'var(--color-surface)', color: 'var(--color-text-primary)' }}>
              <option value="default">Default</option>
              <option value="compact">Compact</option>
              <option value="expanded">Expanded</option>
            </select>
          </div>
          <button onClick={resetPreferences} style={{ alignSelf: 'flex-end', padding: '4px 12px', border: '1px solid var(--color-error)', borderRadius: 'var(--radius-sm)', background: 'transparent', color: 'var(--color-error)', cursor: 'pointer', fontSize: 'var(--text-caption)' }}>
            Reset to defaults
          </button>
        </div>
        <div style={{ marginTop: 4, padding: 8, border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)', fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
          Current preferences: {JSON.stringify(preferences, null, 2)}
        </div>
      </section>

      {/* NavigationSearch */}
      <section>
        <h3 style={{ marginBottom: 8 }}>Navigation Search</h3>
        <NavigationSearch items={mockItems} recentPages={recent.slice(0, 5)} favorites={favorites} />
      </section>

      {/* Empty States */}
      <section>
        <h3 style={{ marginBottom: 8 }}>Empty State Components</h3>
        <div style={{ display: 'flex', gap: 16 }}>
          <div style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', width: 220 }}>
            <NavEmptyState icon="inbox" title="No notifications" description="You're all caught up!" />
          </div>
          <div style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', width: 220 }}>
            <NavEmptyState icon="bookmark" title="No favorites yet" description="Pin pages you use often" action={{ label: 'Browse pages', onClick: () => {} }} />
          </div>
          <div style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', width: 220 }}>
            <NavEmptyState icon="clock" title="No recent pages" description="Your recent activity appears here" />
          </div>
        </div>
      </section>
    </div>
  );
}
