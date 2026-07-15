import { useState } from 'react';
import { EnterpriseSidebar } from '../sidebar/EnterpriseSidebar';
import { ROLE_NAV_CONFIGS } from '../config/roleNavigation';
import { Icon } from '../../../design-system/icons/Icon';
import type { SidebarMode, NavItem, AdminRole } from '../types';

const DEMO_ITEMS: NavItem[] = [
  { id: 'dashboard', label: 'Dashboard', icon: 'layout-dashboard', href: '/admin/dashboard' },
  { id: 'orders', label: 'Orders', icon: 'shopping-cart', badge: 12, children: [
    { id: 'all-orders', label: 'All Orders', href: '/admin/orders' },
    { id: 'pending', label: 'Pending', href: '/admin/orders/pending', badge: 5, badgeColor: '#f59e0b' },
    { id: 'shipped', label: 'Shipped', href: '/admin/orders/shipped' },
    { id: 'returns', label: 'Returns', href: '/admin/orders/returns', badge: 3, badgeColor: '#ef4444' },
  ]},
  { id: 'products', label: 'Products', icon: 'package', children: [
    { id: 'all-products', label: 'All Products', href: '/admin/products' },
    { id: 'categories', label: 'Categories', href: '/admin/categories' },
    { id: 'inventory', label: 'Inventory', href: '/admin/inventory', badge: 2, badgeColor: '#f59e0b' },
  ]},
  { id: 'customers', label: 'Customers', icon: 'users', href: '/admin/customers' },
  { id: 'analytics', label: 'Analytics', icon: 'chart-bar', href: '/admin/analytics', disabled: true },
];

export function SidebarPreview() {
  const [mode, setMode] = useState<SidebarMode>('expanded');
  const [selectedRole, setSelectedRole] = useState<AdminRole>('super_admin');

  const roleConfig = ROLE_NAV_CONFIGS.find((c) => c.role === selectedRole);
  const sidebarItems = roleConfig?.sidebarItems ?? DEMO_ITEMS;

  const modes: SidebarMode[] = ['expanded', 'collapsed', 'mini', 'floating'];

  return (
    <div style={{ padding: 32, display: 'flex', flexDirection: 'column', gap: 24 }}>
      <h2 style={{ margin: 0 }}>Part 5 — EnterpriseSidebar</h2>

      {/* Controls */}
      <div style={{ display: 'flex', gap: 16, alignItems: 'center', flexWrap: 'wrap', padding: 16, border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)' }}>
        <div>
          <label style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', display: 'block', marginBottom: 4 }}>Sidebar Mode</label>
          <div style={{ display: 'flex', gap: 4 }}>
            {modes.map((m) => (
              <button
                key={m}
                onClick={() => setMode(m)}
                style={{
                  padding: '6px 14px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)',
                  background: mode === m ? 'var(--color-primary)' : 'var(--color-surface)',
                  color: mode === m ? '#fff' : 'var(--color-text-primary)',
                  cursor: 'pointer', fontSize: 'var(--text-caption)', fontWeight: 500,
                }}
              >
                {m}
              </button>
            ))}
          </div>
        </div>
        <div>
          <label style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', display: 'block', marginBottom: 4 }}>Role</label>
          <select
            value={selectedRole}
            onChange={(e) => setSelectedRole(e.target.value as AdminRole)}
            style={{ padding: '6px 10px', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-surface)', color: 'var(--color-text-primary)' }}
          >
            {ROLE_NAV_CONFIGS.map((cfg) => (
              <option key={cfg.role} value={cfg.role}>{cfg.label} ({cfg.role})</option>
            ))}
          </select>
        </div>
      </div>

      {/* Sidebar Demo */}
      <div style={{ display: 'flex', gap: 0, border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', overflow: 'hidden', minHeight: 400, position: 'relative' }}>
        <EnterpriseSidebar
          items={sidebarItems}
          mode={mode}
          onModeChange={setMode}
        />
        <div style={{ flex: 1, padding: 24, background: 'var(--color-surface-hover)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: 'var(--color-text-tertiary)', fontSize: 'var(--text-body)' }}>
          <div style={{ textAlign: 'center' }}>
            <Icon name="layout-dashboard" size={24} style={{ marginBottom: 8 }} />
            <p style={{ margin: 0 }}>Content Area</p>
            <p style={{ margin: '4px 0 0', fontSize: 'var(--text-caption)' }}>Mode: <strong>{mode}</strong> | Role: <strong>{selectedRole}</strong></p>
          </div>
        </div>
      </div>

      {/* SidebarItem matrix */}
      <section>
        <h3 style={{ marginBottom: 8 }}>SidebarItem Variants</h3>
        <div style={{ display: 'flex', gap: 24, flexWrap: 'wrap' }}>
          <div style={{ width: 280, border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', overflow: 'hidden' }}>
            <div style={{ padding: '8px 12px', fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', textTransform: 'uppercase', letterSpacing: '0.05em' }}>Expanded</div>
            <EnterpriseSidebar items={DEMO_ITEMS} mode="expanded" />
          </div>
          <div style={{ width: 56, border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', overflow: 'hidden' }}>
            <div style={{ padding: '4px 0', fontSize: 10, color: 'var(--color-text-tertiary)', textAlign: 'center' }}>Mini</div>
            <EnterpriseSidebar items={DEMO_ITEMS} mode="mini" />
          </div>
        </div>
      </section>
    </div>
  );
}
