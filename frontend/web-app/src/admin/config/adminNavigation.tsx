import type { SidebarItemData } from '../../design-system/components/navigation/SidebarItem';
import type { TopNavItem } from '../../design-system/components/navigation/NavItem';
import type { Crumb } from '../../design-system/components/navigation/BreadcrumbItem';
import type { Role } from '../../config/roles';
import { Icon } from '../../design-system/icons/Icon';

type AdminRole = 'administrator' | 'support' | 'business_owner' | 'governance_manager';

const ADMIN_ROLES: AdminRole[] = ['administrator', 'support', 'business_owner', 'governance_manager'];

interface AdminNavItem extends SidebarItemData {
  roles?: AdminRole[];
}

const RAW_SIDEBAR_ITEMS: AdminNavItem[] = [
  { id: 'dashboard', label: 'Dashboard',  href: '/admin/dashboard', icon: <Icon name="layout" size={18} color="currentColor" />, roles: ['administrator', 'support', 'business_owner', 'governance_manager'] },
  { id: 'products',  label: 'Products',   href: '/admin/products',  icon: <Icon name="package" size={18} color="currentColor" />, roles: ['administrator', 'support', 'business_owner'] },
  { id: 'inventory', label: 'Inventory',  href: '/admin/inventory', icon: <Icon name="archive" size={18} color="currentColor" />, roles: ['administrator', 'business_owner'] },
  { id: 'warehouse', label: 'Warehouse',  href: '/admin/warehouse', icon: <Icon name="home" size={18} color="currentColor" />, roles: ['administrator', 'business_owner'] },
  { id: 'inventory-items', label: 'Inventory Items',  href: '/admin/inventory-items', icon: <Icon name="package" size={18} color="currentColor" />, roles: ['administrator', 'business_owner'] },
  { id: 'stock', label: 'Stock',  href: '/admin/stock', icon: <Icon name="database" size={18} color="currentColor" />, roles: ['administrator', 'business_owner'] },
  { id: 'batch', label: 'Batch',  href: '/admin/batch', icon: <Icon name="layers" size={18} color="currentColor" />, roles: ['administrator', 'business_owner'] },
  { id: 'movements', label: 'Movements',  href: '/admin/movements', icon: <Icon name="activity" size={18} color="currentColor" />, roles: ['administrator', 'business_owner'] },
  { id: 'receiving', label: 'Receiving',  href: '/admin/receiving', icon: <Icon name="arrow-down" size={18} color="currentColor" />, roles: ['administrator', 'business_owner'] },
  { id: 'intelligence', label: 'Intelligence',  href: '/admin/intelligence', icon: <Icon name="zap" size={18} color="currentColor" />, roles: ['administrator', 'business_owner'] },
  { id: 'orders',    label: 'Orders',     href: '/admin/orders',    icon: <Icon name="shopping-cart" size={18} color="currentColor" />, roles: ['administrator', 'support', 'business_owner'] },
  { id: 'customers', label: 'Customers',  href: '/admin/customers', icon: <Icon name="users" size={18} color="currentColor" />, roles: ['administrator', 'support', 'business_owner'] },
  { id: 'crm',       label: 'CRM',        href: '/admin/crm',       icon: <Icon name="heart" size={18} color="currentColor" />, roles: ['administrator', 'business_owner'] },
  { id: 'training',  label: 'Training',   href: '/admin/training',  icon: <Icon name="book-open" size={18} color="currentColor" />, roles: ['administrator', 'support'] },
  { id: 'shipping',  label: 'Shipping',   href: '/admin/shipping',  icon: <Icon name="truck" size={18} color="currentColor" />, roles: ['administrator', 'support', 'business_owner'] },
  { id: 'finance',   label: 'Finance',    href: '/admin/finance',   icon: <Icon name="credit-card" size={18} color="currentColor" />, roles: ['administrator', 'business_owner'] },
  { id: 'reports',   label: 'Reports',    href: '/admin/reports',   icon: <Icon name="bar-chart" size={18} color="currentColor" />, roles: ['administrator', 'business_owner'] },
  { id: 'analytics', label: 'Analytics',  href: '/admin/analytics', icon: <Icon name="trending-up" size={18} color="currentColor" />, roles: ['administrator', 'support', 'business_owner'] },
  { id: 'alert-center', label: 'Alert Center',  href: '/admin/alert-center', icon: <Icon name="bell" size={18} color="currentColor" />, roles: ['administrator', 'business_owner'] },
  { id: 'risk-dashboard', label: 'Risk Dashboard',  href: '/admin/risk-dashboard', icon: <Icon name="alert-triangle" size={18} color="currentColor" />, roles: ['administrator', 'business_owner'] },
  { id: 'timeline-view', label: 'Timeline',  href: '/admin/timeline-view', icon: <Icon name="activity" size={18} color="currentColor" />, roles: ['administrator', 'business_owner'] },
  { id: 'profile',   label: 'Profile',    href: '/admin/profile',   icon: <Icon name="user" size={18} color="currentColor" />, roles: ['administrator', 'support', 'business_owner', 'governance_manager'] },
  { id: 'settings',  label: 'Settings',   href: '/admin/settings',  icon: <Icon name="settings" size={18} color="currentColor" />, roles: ['administrator'] },
  { id: 'system',    label: 'System',     href: '/admin/system',    icon: <Icon name="terminal" size={18} color="currentColor" />, roles: ['administrator'] },
  { id: 'help',      label: 'Help',       href: '/admin/help',      icon: <Icon name="help-circle" size={18} color="currentColor" />, roles: ['administrator', 'support', 'business_owner', 'governance_manager'] },
];

const PLACEHOLDER_ITEMS: SidebarItemData[] = [
  { id: 'sep-pinned', label: 'PINNED', disabled: true },
  { id: 'pinned-placeholder', label: 'Pin items for quick access', disabled: true },
  { id: 'sep-favorites', label: 'FAVORITES', disabled: true },
  { id: 'favorites-placeholder', label: 'No favorites yet', disabled: true, icon: <Icon name="star" size={18} color="currentColor" /> },
  { id: 'sep-recent', label: 'RECENT', disabled: true },
  { id: 'recent-placeholder', label: 'No recent pages', disabled: true, icon: <Icon name="clock" size={18} color="currentColor" /> },
  { id: 'sep-cmd', label: 'QUICK ACTIONS', disabled: true },
  { id: 'cmd-placeholder', label: 'Cmd+K to search', disabled: true, icon: <Icon name="terminal" size={18} color="currentColor" /> },
];

export function getFilteredSidebarItems(activeRole: Role): SidebarItemData[] {
  const adminRole = ADMIN_ROLES.includes(activeRole as AdminRole)
    ? (activeRole as AdminRole)
    : null;
  const navItems = adminRole
    ? RAW_SIDEBAR_ITEMS.filter((item) => item.roles?.includes(adminRole))
    : [];
  return [...navItems, ...PLACEHOLDER_ITEMS];
}

export const ADMIN_TOP_NAV: TopNavItem[] = [
  { id: 'profile',  label: 'Profile',  href: '/admin/profile',  icon: <Icon name="user" size={16} color="currentColor" /> },
  { id: 'settings', label: 'Settings', href: '/admin/settings', icon: <Icon name="settings" size={16} color="currentColor" /> },
  { id: 'help',     label: 'Help',     href: '/admin/help',     icon: <Icon name="help-circle" size={16} color="currentColor" /> },
];

const LABELS: Record<string, string> = {
  dashboard: 'Dashboard',
  workspace: 'Workspace',
  products: 'Products',
  inventory: 'Inventory',
  warehouse: 'Warehouse',
  'inventory-items': 'Inventory Items',
  stock: 'Stock',
  batch: 'Batch',
  movements: 'Movements',
  receiving: 'Receiving',
  intelligence: 'Intelligence',
  orders: 'Orders',
  customers: 'Customers',
  crm: 'CRM',
  training: 'Training',
  shipping: 'Shipping',
  finance: 'Finance',
  reports: 'Reports',
  analytics: 'Analytics',
  profile: 'Profile',
  settings: 'Settings',
  system: 'System',
  help: 'Help',
};

export function buildAdminBreadcrumbs(pathname: string): Crumb[] {
  const crumbs: Crumb[] = [{ label: 'Admin', href: '/admin/dashboard' }];
  const segments = pathname.replace('/admin', '').split('/').filter(Boolean);
  if (segments.length > 0 && segments[0] !== 'dashboard') {
    const section = segments[0];
    crumbs.push({ label: LABELS[section] || section, href: `/admin/${section}` });
  }
  if (segments.length > 1) {
    crumbs.push({ label: segments.slice(1).join(' / '), href: pathname });
  }
  return crumbs;
}

export function getAdminActiveId(pathname: string): string {
  const segments = pathname.replace('/admin', '').split('/').filter(Boolean);
  return segments[0] || 'dashboard';
}

export const ADMIN_SIDEBAR_ITEMS_FOR_PREVIEW: SidebarItemData[] = RAW_SIDEBAR_ITEMS.map(
  ({ roles: _roles, ...rest }) => rest,
);
