import { useState, useCallback } from 'react';
import { WorkspaceHeader } from '../workspace/WorkspaceHeader';
import { WorkspaceTabs } from '../workspace/WorkspaceTabs';
import { EnterpriseBreadcrumbs } from '../breadcrumbs/EnterpriseBreadcrumbs';
import { NotificationCenter } from '../notifications/NotificationCenter';
import { NavEmptyState } from '../empty-states/NavEmptyState';
import { useFavorites } from '../favorites/useFavorites';
import { useRecentPages } from '../recent/useRecentPages';
import type { NavNotification } from '../types';

const initialNotifications: NavNotification[] = [
  { id: 'n1', title: 'Order #1234 shipped', message: 'Dispatched successfully.', type: 'success', timestamp: '2 min ago', read: false },
  { id: 'n2', title: 'Low inventory alert', message: 'Wireless Mouse below threshold.', type: 'warning', timestamp: '15 min ago', read: false },
  { id: 'n3', title: 'Payment failed', message: 'Order #5678 was declined.', type: 'error', timestamp: '1 hour ago', read: false },
];

export function WorkspacePreview() {
  const [notifications, setNotifications] = useState(initialNotifications);
  const { favorites, addFavorite, removeFavorite, isFavorite } = useFavorites();
  const { recent, addRecent, clearRecent } = useRecentPages();

  const handleMarkRead = useCallback((id: string) => {
    setNotifications((prev) => prev.map((n) => n.id === id ? { ...n, read: true } : n));
  }, []);
  const handleMarkAllRead = useCallback(() => {
    setNotifications((prev) => prev.map((n) => ({ ...n, read: true })));
  }, []);
  const handleDismiss = useCallback((id: string) => {
    setNotifications((prev) => prev.filter((n) => n.id !== id));
  }, []);
  const unreadCount = notifications.filter((n) => !n.read).length;

  const crumbs = [
    { label: 'Workspace', icon: 'grid' },
    { label: 'Order Management', href: '/admin/workspace/orders', icon: 'shopping-cart' },
    { label: 'Order #1234' },
  ];

  const breadcrumbs2 = [
    { label: 'Dashboard', href: '/admin/dashboard', icon: 'layout-dashboard' },
    { label: 'Products', href: '/admin/products', icon: 'package' },
    { label: 'Inventory', icon: 'archive' },
  ];

  const breadcrumbs3 = [
    { label: 'Home', href: '/admin', icon: 'home' },
    { label: 'Analytics', href: '/admin/analytics', icon: 'chart-bar' },
    { label: 'Reports', href: '/admin/reports', icon: 'file-text' },
    { label: 'Sales Report', href: '/admin/reports/sales' },
    { label: 'Q2 2026' },
  ];

  const workspaceConfigs = [
    {
      title: 'Order Management',
      description: 'Manage, track, and fulfill customer orders across all channels',
      icon: 'shopping-cart',
      actions: [
        { id: 'export', label: 'Export', icon: 'download', onClick: () => addRecent({ id: 'export', label: 'Export Orders', href: '/admin/orders/export' }) },
        { id: 'filter', label: 'Filter', icon: 'filter', onClick: () => {} },
        { id: 'refresh', label: 'Refresh', icon: 'refresh-cw', onClick: () => {} },
      ],
    },
    {
      title: 'Product Catalog',
      description: 'Browse, edit, and manage your product inventory',
      icon: 'package',
      actions: [
        { id: 'add', label: 'Add Product', icon: 'plus', onClick: () => {} },
        { id: 'import', label: 'Import CSV', icon: 'upload', onClick: () => {} },
      ],
    },
    {
      title: 'Customer Insights',
      icon: 'users',
      actions: [
        { id: 'export', label: 'Export', icon: 'download', onClick: () => {} },
      ],
    },
  ];

  const tabsets = [
    [
      { id: 'all', label: 'All Orders', href: '/admin/orders' },
      { id: 'pending', label: 'Pending', href: '/admin/orders/pending' },
      { id: 'processing', label: 'Processing', href: '/admin/orders/processing' },
      { id: 'shipped', label: 'Shipped', href: '/admin/orders/shipped' },
      { id: 'cancelled', label: 'Cancelled', href: '/admin/orders/cancelled' },
    ],
    [
      { id: 'active', label: 'Active', href: '/admin/products' },
      { id: 'drafts', label: 'Drafts', href: '/admin/products/drafts' },
      { id: 'archived', label: 'Archived', href: '/admin/products/archived' },
    ],
  ];

  return (
    <div style={{ padding: 32, display: 'flex', flexDirection: 'column', gap: 32 }}>
      <h2 style={{ margin: 0 }}>Part 5 — Workspace & Breadcrumbs</h2>

      {/* EnterpriseBreadcrumbs */}
      <section>
        <h3 style={{ marginBottom: 8 }}>EnterpriseBreadcrumbs</h3>
        <div style={{ display: 'flex', flexDirection: 'column', gap: 12, padding: 16, border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Default (≤ maxItems=4):</div>
          <EnterpriseBreadcrumbs crumbs={crumbs} />
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>With icons:</div>
          <EnterpriseBreadcrumbs crumbs={breadcrumbs2} />
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Overflow (5 items, maxItems=3):</div>
          <EnterpriseBreadcrumbs crumbs={breadcrumbs3} maxItems={3} />
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Empty (no crumbs):</div>
          <EnterpriseBreadcrumbs crumbs={[]} />
        </div>
      </section>

      {/* WorkspaceHeader variants */}
      <section>
        <h3 style={{ marginBottom: 8 }}>WorkspaceHeader Variants</h3>
        <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
          {workspaceConfigs.map((config, i) => (
            <div key={i} style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', padding: '0 16px' }}>
              <WorkspaceHeader config={config} />
            </div>
          ))}
        </div>
      </section>

      {/* WorkspaceTabs variants */}
      <section>
        <h3 style={{ marginBottom: 8 }}>WorkspaceTabs</h3>
        <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
          {tabsets.map((tabs, i) => (
            <div key={i} style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', padding: '0 16px' }}>
              <WorkspaceTabs tabs={tabs} />
            </div>
          ))}
        </div>
      </section>

      {/* Composed Workspace Example */}
      <section>
        <h3 style={{ marginBottom: 8 }}>Composed Workspace Page</h3>
        <div style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', overflow: 'hidden' }}>
          <div style={{ padding: '0 16px', borderBottom: '1px solid var(--color-border)', backgroundColor: 'var(--color-surface)' }}>
            <EnterpriseBreadcrumbs crumbs={[{ label: 'Workspace', href: '/admin/workspace', icon: 'grid' }, { label: 'Order Management' }]} />
          </div>
          <div style={{ padding: '0 16px', background: 'var(--color-surface)' }}>
            <WorkspaceHeader config={workspaceConfigs[0]} />
          </div>
          <div style={{ padding: '0 16px', background: 'var(--color-surface)' }}>
            <WorkspaceTabs tabs={tabsets[0]} />
          </div>
          <div style={{ padding: 24, background: 'var(--color-surface-hover)', minHeight: 200, display: 'flex', alignItems: 'center', justifyContent: 'center', color: 'var(--color-text-tertiary)' }}>
            Workspace content area
          </div>
        </div>
      </section>

      {/* Notification Center in context */}
      <section>
        <h3 style={{ marginBottom: 8 }}>Notification Center (in context)</h3>
        <div style={{ display: 'flex', alignItems: 'center', gap: 24, padding: 16, border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)' }}>
          <NotificationCenter
            notifications={notifications}
            onMarkRead={handleMarkRead}
            onMarkAllRead={handleMarkAllRead}
            onDismiss={handleDismiss}
            unreadCount={unreadCount}
          />
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
            {unreadCount} unread / {notifications.length} total
          </span>
          <button onClick={() => setNotifications((prev) => [...prev, { id: `n-${Date.now()}`, title: 'New notification', message: 'Triggered from demo', type: 'info', timestamp: 'Just now', read: false }])} style={{ padding: '6px 14px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)', background: 'var(--color-surface)', cursor: 'pointer', color: 'var(--color-text-primary)', fontSize: 'var(--text-caption)' }}>
            Add test notification
          </button>
        </div>
      </section>

      {/* Favorites + Recent in workspace context */}
      <section>
        <h3 style={{ marginBottom: 8 }}>Favorites & Recent (workspace integration)</h3>
        <div style={{ display: 'flex', gap: 24, flexWrap: 'wrap' }}>
          <div style={{ flex: 1, minWidth: 260, padding: 16, border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)' }}>
            <h4 style={{ margin: '0 0 8px', fontSize: 'var(--text-body)', color: 'var(--color-text-primary)' }}>Favorites</h4>
            {favorites.length === 0 ? (
              <NavEmptyState icon="bookmark" title="No favorites" description="Click ☆ on a page below" />
            ) : (
              <div style={{ display: 'flex', gap: 4, flexWrap: 'wrap' }}>
                {favorites.map((f) => (
                  <span key={f.id} style={{ padding: '2px 8px', background: 'var(--color-surface-hover)', borderRadius: 'var(--radius-sm)', fontSize: 'var(--text-caption)' }}>
                    {f.label}
                    <button onClick={() => removeFavorite(f.id)} style={{ marginLeft: 4, padding: 0, border: 'none', background: 'none', cursor: 'pointer', color: 'var(--color-text-tertiary)' }}>✕</button>
                  </span>
                ))}
              </div>
            )}
          </div>
          <div style={{ flex: 1, minWidth: 260, padding: 16, border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)' }}>
            <h4 style={{ margin: '0 0 8px', fontSize: 'var(--text-body)', color: 'var(--color-text-primary)' }}>Recent Pages</h4>
            <button onClick={() => {
              const id = `page-${Date.now()}`;
              addRecent({ id, label: `Page ${recent.length + 1}`, href: `/admin/page-${recent.length + 1}` });
            }} style={{ padding: '4px 10px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-sm)', background: 'var(--color-surface)', cursor: 'pointer', color: 'var(--color-text-secondary)', fontSize: 'var(--text-caption)', marginBottom: 8 }}>
              Simulate visit
            </button>
            <button onClick={clearRecent} style={{ marginLeft: 4, padding: '4px 10px', border: 'none', borderRadius: 'var(--radius-sm)', background: 'var(--color-surface-hover)', cursor: 'pointer', color: 'var(--color-text-secondary)', fontSize: 'var(--text-caption)', marginBottom: 8 }}>
              Clear
            </button>
            {recent.length === 0 ? (
              <NavEmptyState icon="clock" title="No recent pages" description="Your activity appears here" />
            ) : (
              <div style={{ display: 'flex', gap: 4, flexWrap: 'wrap' }}>
                {recent.slice(0, 10).map((r) => (
                  <span key={r.id} style={{ padding: '2px 8px', background: 'var(--color-surface-hover)', borderRadius: 'var(--radius-sm)', fontSize: 'var(--text-caption)' }}>
                    {r.label}
                  </span>
                ))}
              </div>
            )}
          </div>
        </div>
        <div style={{ display: 'flex', gap: 8, marginTop: 8 }}>
          {['Dashboard', 'Orders', 'Products', 'Customers', 'Analytics'].map((name) => (
            <button key={name} onClick={() => {
              const id = name.toLowerCase();
              if (isFavorite(id)) removeFavorite(id);
              else addFavorite({ id, label: name, href: `/admin/${id}`, icon: id === 'orders' ? 'shopping-cart' : undefined, pinned: false });
            }} style={{ padding: '6px 12px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)', background: isFavorite(name.toLowerCase()) ? 'var(--color-primary-alpha)' : 'var(--color-surface)', cursor: 'pointer', color: 'var(--color-text-primary)', fontSize: 'var(--text-caption)', display: 'flex', alignItems: 'center', gap: 4 }}>
              {name}
              <span>{isFavorite(name.toLowerCase()) ? '★' : '☆'}</span>
            </button>
          ))}
        </div>
      </section>
    </div>
  );
}
