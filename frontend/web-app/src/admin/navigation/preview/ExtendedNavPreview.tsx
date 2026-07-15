import { useState } from 'react';
import { CommandPalette } from '../command-palette/CommandPalette';
import { NavigationSearch } from '../global-search/NavigationSearch';
import { WorkspaceHeader } from '../workspace/WorkspaceHeader';
import { WorkspaceTabs } from '../workspace/WorkspaceTabs';
import { EnterpriseBreadcrumbs } from '../breadcrumbs/EnterpriseBreadcrumbs';
import { NotificationCenter } from '../notifications/NotificationCenter';
import { NavEmptyState } from '../empty-states/NavEmptyState';
import { Icon } from '../../../design-system/icons/Icon';
import type { NavItem, CommandItem, NotificationItem, WorkspaceConfig } from '../types';

const mockNavItems: NavItem[] = [
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

const mockCommands: CommandItem[] = [
  { id: 'go-dashboard', label: 'Go to Dashboard', category: 'Navigation', icon: 'layout-dashboard', href: '/admin/dashboard', shortcut: 'G D' },
  { id: 'go-orders', label: 'View Orders', category: 'Navigation', icon: 'shopping-cart', href: '/admin/orders', shortcut: 'G O' },
  { id: 'go-products', label: 'Browse Products', category: 'Navigation', icon: 'package', href: '/admin/products', shortcut: 'G P' },
  { id: 'go-customers', label: 'Manage Customers', category: 'Navigation', icon: 'users', href: '/admin/customers', shortcut: 'G C' },
  { id: 'go-analytics', label: 'View Analytics', category: 'Navigation', icon: 'chart-bar', href: '/admin/analytics', shortcut: 'G A' },
  { id: 'new-order', label: 'Create New Order', category: 'Actions', icon: 'plus-circle', keywords: ['create order', 'add order'] },
  { id: 'new-product', label: 'Add Product', category: 'Actions', icon: 'plus-circle', keywords: ['create product', 'new item'] },
  { id: 'export-csv', label: 'Export as CSV', category: 'Export', icon: 'file-text', keywords: ['download', 'csv', 'spreadsheet'] },
  { id: 'export-pdf', label: 'Export as PDF', category: 'Export', icon: 'file-text', keywords: ['download', 'pdf'] },
  { id: 'settings', label: 'Open Settings', category: 'System', icon: 'settings', href: '/admin/settings', shortcut: 'G S' },
];

const mockNotifications: NotificationItem[] = [
  { id: 'n1', title: 'Order #1234 shipped', message: 'Customer order has been dispatched.', type: 'success', timestamp: '2 min ago', read: false },
  { id: 'n2', title: 'Low inventory alert', message: 'Product "Wireless Mouse" is below threshold.', type: 'warning', timestamp: '15 min ago', read: false },
  { id: 'n3', title: 'Payment failed', message: 'Transaction for order #5678 was declined.', type: 'error', timestamp: '1 hour ago', read: false },
  { id: 'n4', title: 'New customer registered', message: 'John Doe created an account.', type: 'info', timestamp: '3 hours ago', read: false },
  { id: 'n5', title: 'Bulk import complete', message: '250 products imported successfully.', type: 'success', timestamp: 'Yesterday', read: true },
  { id: 'n6', title: 'System maintenance', message: 'Scheduled maintenance tonight at 2 AM.', type: 'warning', timestamp: 'Yesterday', read: true },
];

const mockWorkspaceConfig: WorkspaceConfig = {
  title: 'Order Management',
  description: 'Manage, track, and fulfill customer orders',
  icon: 'shopping-cart',
  actions: [
    { id: 'export', label: 'Export', icon: 'download', onClick: () => {} },
    { id: 'filter', label: 'Filter', icon: 'filter', onClick: () => {} },
  ],
};

const mockTabs = [
  { id: 'all', label: 'All Orders', href: '/admin/orders' },
  { id: 'pending', label: 'Pending', href: '/admin/orders/pending' },
  { id: 'processing', label: 'Processing', href: '/admin/orders/processing' },
  { id: 'shipped', label: 'Shipped', href: '/admin/orders/shipped' },
  { id: 'cancelled', label: 'Cancelled', href: '/admin/orders/cancelled' },
];

const mockCrumbs = [
  { label: 'Home', href: '/admin/dashboard', icon: 'home' },
  { label: 'Orders', href: '/admin/orders', icon: 'shopping-cart' },
  { label: 'Order Details' },
];

export function ExtendedNavPreview() {
  const [cmdOpen, setCmdOpen] = useState(false);
  const [notifications, setNotifications] = useState(mockNotifications);

  const handleMarkRead = (id: string) => {
    setNotifications((prev) => prev.map((n) => n.id === id ? { ...n, read: true } : n));
  };
  const handleMarkAllRead = () => {
    setNotifications((prev) => prev.map((n) => ({ ...n, read: true })));
  };
  const handleDismiss = (id: string) => {
    setNotifications((prev) => prev.filter((n) => n.id !== id));
  };
  const unreadCount = notifications.filter((n) => !n.read).length;

  return (
    <div style={{ padding: 32, display: 'flex', flexDirection: 'column', gap: 32 }}>
      <h2 style={{ margin: 0 }}>Extended Navigation Components</h2>

      {/* Breadcrumbs */}
      <section>
        <h3 style={{ marginBottom: 8 }}>Enterprise Breadcrumbs</h3>
        <EnterpriseBreadcrumbs crumbs={mockCrumbs} />
      </section>

      {/* Navigation Search */}
      <section>
        <h3 style={{ marginBottom: 8 }}>Navigation Search</h3>
        <NavigationSearch items={mockNavItems} />
      </section>

      {/* Command Palette Trigger */}
      <section>
        <h3 style={{ marginBottom: 8 }}>Command Palette</h3>
        <p style={{ margin: '0 0 8px', fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Press Ctrl+K or click to open</p>
        <button
          onClick={() => setCmdOpen(true)}
          style={{
            padding: '8px 20px', border: '1px solid var(--color-border)',
            borderRadius: 'var(--radius-md)', background: 'var(--color-surface)',
            cursor: 'pointer', color: 'var(--color-text-primary)', display: 'flex', alignItems: 'center', gap: 8,
          }}
        >
          <Icon name="command" size={14} />
          <span>Open Command Palette</span>
          <kbd style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', background: 'var(--color-surface-hover)', padding: '2px 6px', borderRadius: 'var(--radius-sm)' }}>Ctrl+K</kbd>
        </button>
        <CommandPalette
          commands={mockCommands}
          open={cmdOpen}
          onClose={() => setCmdOpen(false)}
          onExecute={() => {}}
        />
      </section>

      {/* Notification Center */}
      <section>
        <h3 style={{ marginBottom: 8 }}>Notification Center</h3>
        <NotificationCenter
          notifications={notifications}
          onMarkRead={handleMarkRead}
          onMarkAllRead={handleMarkAllRead}
          onDismiss={handleDismiss}
          unreadCount={unreadCount}
        />
      </section>

      {/* Workspace Header */}
      <section>
        <h3 style={{ marginBottom: 8 }}>Workspace Header</h3>
        <WorkspaceHeader config={mockWorkspaceConfig} />
      </section>

      {/* Workspace Tabs */}
      <section>
        <h3 style={{ marginBottom: 8 }}>Workspace Tabs</h3>
        <WorkspaceTabs tabs={mockTabs} />
      </section>

      {/* Empty States */}
      <section>
        <h3 style={{ marginBottom: 8 }}>Navigation Empty States</h3>
        <div style={{ display: 'flex', gap: 16 }}>
          <div style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', width: 240 }}>
            <NavEmptyState icon="inbox" title="No notifications" description="You're all caught up!" />
          </div>
          <div style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', width: 240 }}>
            <NavEmptyState icon="bookmark" title="No favorites yet" description="Pin your most-used pages" action={{ label: 'Browse pages', onClick: () => {} }} />
          </div>
          <div style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', width: 240 }}>
            <NavEmptyState icon="clock" title="No recent pages" description="Pages you visit will appear here" />
          </div>
        </div>
      </section>
    </div>
  );
}
