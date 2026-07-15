import { useState } from 'react';
import { useNavigate, useLocation, Outlet } from 'react-router-dom';
import { AuthenticatedLayout } from '../../design-system/components/layout/AuthenticatedLayout';
import { Sidebar } from '../../design-system/components/navigation/Sidebar';
import { TopNav } from '../../design-system/components/navigation/TopNav';
import { Breadcrumb } from '../../design-system/components/navigation/Breadcrumb';
import { PageFooter } from '../../design-system/components/layout/PageFooter';
import { PageHeader } from '../../design-system/components/layout/PageHeader';
import { Icon } from '../../design-system/icons/Icon';
import type { SidebarItemData } from '../../design-system/components/navigation/SidebarItem';
import type { TopNavItem } from '../../design-system/components/navigation/NavItem';
import type { Crumb } from '../../design-system/components/navigation/BreadcrumbItem';
import '../auth/auth.css';
import './customer.css';

const PRIMARY_NAV: SidebarItemData[] = [
  { id: 'dashboard', label: 'Dashboard', href: '/dashboard', icon: <Icon name="layout" size={18} color="currentColor" /> },
  { id: 'orders', label: 'Orders', href: '/dashboard/orders', icon: <Icon name="shopping-bag" size={18} color="currentColor" /> },
  { id: 'wishlist', label: 'Wishlist', href: '/dashboard/wishlist', icon: <Icon name="heart" size={18} color="currentColor" /> },
  { id: 'training', label: 'Training', href: '/dashboard/training', icon: <Icon name="book-open" size={18} color="currentColor" /> },
  { id: 'products', label: 'Products', href: '/dashboard/products', icon: <Icon name="package" size={18} color="currentColor" /> },
  { id: 'addresses', label: 'Addresses', href: '/dashboard/addresses', icon: <Icon name="map-pin" size={18} color="currentColor" /> },
  { id: 'support', label: 'Support', href: '/dashboard/support', icon: <Icon name="help-circle" size={18} color="currentColor" /> },
  { id: 'notifications', label: 'Notifications', href: '/dashboard/notifications', icon: <Icon name="bell" size={18} color="currentColor" /> },
];

const SECONDARY_NAV: TopNavItem[] = [
  { id: 'settings', label: 'Settings', href: '/dashboard/settings', icon: <Icon name="settings" size={16} color="currentColor" /> },
  { id: 'help', label: 'Help', href: '/support', icon: <Icon name="help-circle" size={16} color="currentColor" /> },
  { id: 'logout', label: 'Logout', href: '/login', icon: <Icon name="log-out" size={16} color="currentColor" /> },
];

function buildBreadcrumbs(pathname: string): Crumb[] {
  const segments = pathname.split('/').filter(Boolean);
  const crumbs: Crumb[] = [{ label: 'Dashboard', href: '/dashboard' }];
  if (segments.length >= 2) {
    const section = segments[1];
    const labels: Record<string, string> = {
      orders: 'Orders',
      wishlist: 'Wishlist',
      training: 'Training',
      products: 'Products',
      addresses: 'Addresses',
      support: 'Support',
      notifications: 'Notifications',
      settings: 'Settings',
    };
    if (section === 'profile') {
      const subpage = segments[2];
      const subpageLabels: Record<string, string> = {
        edit: 'Edit Profile',
        security: 'Security',
        preferences: 'Preferences',
        privacy: 'Privacy',
        activity: 'Activity',
        sessions: 'Sessions',
        'account-status': 'Account Status',
      };
      crumbs.push({ label: subpageLabels[subpage] || 'Profile', href: `/dashboard/${section}` });
      if (subpage && segments.length > 2) {
        crumbs.push({ label: subpageLabels[subpage], href: pathname });
      }
    } else {
      crumbs.push({ label: labels[section] || section, href: `/dashboard/${section}` });
      if (segments.length > 2) {
        crumbs.push({ label: segments.slice(2).join(' / '), href: pathname });
      }
    }
  }
  return crumbs;
}

export default function CustomerLayout() {
  const navigate = useNavigate();
  const location = useLocation();
  const [sidebarCollapsed, setSidebarCollapsed] = useState(false);
  const [sidebarOpen, setSidebarOpen] = useState(false);

  const handleSidebarNavigate = (item: SidebarItemData) => {
    if (item.href) {
      navigate(item.href);
      if (window.innerWidth < 768) setSidebarOpen(false);
    }
  };

  const handleTopNavNavigate = (item: TopNavItem) => {
    if (item.href) navigate(item.href);
    if (item.id === 'logout') {
      setSidebarOpen(false);
      setTimeout(() => navigate('/login'), 200);
    }
  };

  const crumbs = buildBreadcrumbs(location.pathname);

  return (
    <AuthenticatedLayout
      header={
        <TopNav
          items={SECONDARY_NAV}
          activeId={crumbs[crumbs.length - 1]?.label?.toLowerCase() || 'dashboard'}
          onNavigate={handleTopNavNavigate}
          variant="primary"
          aria-label="Account actions"
        />
      }
      sidebar={
        <Sidebar
          items={PRIMARY_NAV}
          variant="primary"
          collapsed={sidebarCollapsed}
          onCollapse={setSidebarCollapsed}
          onNavigate={handleSidebarNavigate}
          activeId={location.pathname.split('/')[2] || 'dashboard'}
          header={
            <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-inline-sm)', color: 'var(--color-text-on-primary)' }}>
              <span style={{ width: 32, height: 32, borderRadius: 'var(--radius-sm)', background: 'var(--color-text-on-primary)', color: 'var(--color-bg-primary-default)', display: 'flex', alignItems: 'center', justifyContent: 'center', flexShrink: 0 }}>
                <Icon name="leaf" size={18} color="currentColor" />
              </span>
              <span style={{ fontWeight: 'var(--weight-bold)', fontSize: 'var(--text-body-lg)' }}>SporeKart</span>
            </div>
          }
          footer={
            <div style={{ padding: 'var(--space-stack-sm) var(--space-page-x)', borderTop: '1px solid var(--color-border-default)', fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', textAlign: 'center' }}>
              v1.0.0-beta · Customer Workspace
            </div>
          }
          responsive
          open={sidebarOpen}
          onClose={() => setSidebarOpen(false)}
          aria-label="Customer workspace navigation"
        />
      }
    >
      <div className="cw-layout">
        <PageHeader
          title={crumbs[crumbs.length - 1]?.label || 'Dashboard'}
          description={crumbs.length > 1 ? crumbs[0].label : 'Your customer workspace overview'}
        />
        <Breadcrumb crumbs={crumbs} maxItems={4} collapsedLabel="…" aria-label="Breadcrumb" />
        <div className="cw-content" id="main" tabIndex={-1}>
          <Outlet />
        </div>
        <PageFooter>
          <span>&copy; {new Date().getFullYear()} SporeKart. All rights reserved.</span>
          <nav style={{ display: 'flex', gap: 'var(--space-inline-md)' }}>
            <a href="/privacy-policy" style={{ color: 'var(--color-text-secondary)' }}>Privacy</a>
            <a href="/terms-and-conditions" style={{ color: 'var(--color-text-secondary)' }}>Terms</a>
            <a href="/support" style={{ color: 'var(--color-text-secondary)' }}>Support</a>
          </nav>
        </PageFooter>
      </div>
    </AuthenticatedLayout>
  );
}