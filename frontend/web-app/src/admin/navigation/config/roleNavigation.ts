import type { RoleNavConfig, NavItem, AdminRole } from '../types';

const MODULE_ITEMS: NavItem[] = [
  { id: 'products', label: 'Products', icon: 'package', href: '/admin/products', roles: ['super_admin', 'administrator', 'manager', 'inventory_manager', 'viewer'] },
  { id: 'media', label: 'Media Library', icon: 'image', href: '/admin/media', roles: ['super_admin', 'administrator', 'manager', 'marketing_manager', 'inventory_manager', 'viewer'] },
  { id: 'inventory', label: 'Inventory', icon: 'archive', href: '/admin/inventory', roles: ['super_admin', 'administrator', 'inventory_manager', 'manager'] },
  { id: 'orders', label: 'Orders', icon: 'shopping-cart', href: '/admin/orders', roles: ['super_admin', 'administrator', 'manager', 'support_executive', 'viewer'] },
  { id: 'customers', label: 'Customers', icon: 'users', href: '/admin/customers', roles: ['super_admin', 'administrator', 'crm_manager', 'support_executive', 'marketing_manager'] },
  { id: 'training', label: 'Training', icon: 'book-open', href: '/admin/training', roles: ['super_admin', 'administrator', 'training_manager', 'viewer'] },
  { id: 'finance', label: 'Finance', icon: 'credit-card', href: '/admin/finance', roles: ['super_admin', 'administrator', 'finance_manager'] },
  { id: 'marketing', label: 'Marketing', icon: 'megaphone', href: '/admin/marketing', roles: ['super_admin', 'administrator', 'marketing_manager'] },
  { id: 'support', label: 'Support', icon: 'headphones', href: '/admin/support', roles: ['super_admin', 'administrator', 'support_executive'] },
  { id: 'reports', label: 'Reports', icon: 'bar-chart', href: '/admin/reports', roles: ['super_admin', 'administrator', 'manager', 'finance_manager', 'marketing_manager'] },
  { id: 'shipping', label: 'Shipping', icon: 'truck', href: '/admin/shipping', roles: ['super_admin', 'administrator', 'manager', 'inventory_manager', 'support_executive'] },
  { id: 'analytics', label: 'Analytics', icon: 'trending-up', href: '/admin/analytics', roles: ['super_admin', 'administrator', 'manager', 'marketing_manager', 'finance_manager'] },
  { id: 'crm', label: 'CRM', icon: 'heart', href: '/admin/crm', roles: ['super_admin', 'administrator', 'crm_manager', 'marketing_manager'] },
  { id: 'settings', label: 'Settings', icon: 'settings', href: '/admin/settings', roles: ['super_admin', 'administrator'] },
  { id: 'users', label: 'User Management', icon: 'user-plus', href: '/admin/users', roles: ['super_admin', 'administrator'] },
  { id: 'audit', label: 'Audit Log', icon: 'clipboard', href: '/admin/audit', roles: ['super_admin', 'administrator'] },
];

const SYSTEM_ITEMS: NavItem[] = [
  { id: 'system', label: 'System', icon: 'terminal', href: '/admin/system', roles: ['super_admin', 'administrator'] },
  { id: 'help', label: 'Help', icon: 'help-circle', href: '/admin/help', roles: ['super_admin', 'administrator', 'manager', 'support_executive', 'viewer'] },
];

function buildSidebarItems(role: AdminRole): NavItem[] {
  const filteredModules = MODULE_ITEMS.filter((item) => item.roles?.includes(role));
  const filteredSystem = SYSTEM_ITEMS.filter((item) => item.roles?.includes(role));

  return [
    { id: 'dashboard', label: 'Dashboard', icon: 'layout', href: '/admin/dashboard' },
    {
      id: 'group-modules', label: 'Modules', icon: 'grid', isGroup: true, roles: [role],
      children: filteredModules,
    },
    ...(filteredSystem.length > 0
      ? [{ id: 'group-system', label: 'System', icon: 'settings', isGroup: true, children: filteredSystem } as NavItem]
      : []),
  ];
}

export const ROLE_NAV_CONFIGS: RoleNavConfig[] = [
  {
    role: 'super_admin', label: 'Super Admin',
    sidebarItems: buildSidebarItems('super_admin'),
    topNavItems: [
      { id: 'notifications', label: 'Notifications', icon: 'bell', href: '/admin/notifications' },
      { id: 'profile', label: 'Profile', icon: 'user', href: '/admin/profile' },
      { id: 'settings', label: 'Settings', icon: 'settings', href: '/admin/settings' },
    ],
    pinnedDefault: ['dashboard', 'products', 'orders', 'users'],
  },
  {
    role: 'administrator', label: 'Administrator',
    sidebarItems: buildSidebarItems('administrator'),
    topNavItems: [
      { id: 'notifications', label: 'Notifications', icon: 'bell', href: '/admin/notifications' },
      { id: 'profile', label: 'Profile', icon: 'user', href: '/admin/profile' },
      { id: 'settings', label: 'Settings', icon: 'settings', href: '/admin/settings' },
    ],
    pinnedDefault: ['dashboard', 'products', 'orders'],
  },
  {
    role: 'manager', label: 'Manager',
    sidebarItems: buildSidebarItems('manager'),
    topNavItems: [
      { id: 'notifications', label: 'Notifications', icon: 'bell', href: '/admin/notifications' },
      { id: 'profile', label: 'Profile', icon: 'user', href: '/admin/profile' },
    ],
    pinnedDefault: ['dashboard', 'orders', 'reports'],
  },
  {
    role: 'inventory_manager', label: 'Inventory Manager',
    sidebarItems: buildSidebarItems('inventory_manager'),
    topNavItems: [
      { id: 'notifications', label: 'Notifications', icon: 'bell', href: '/admin/notifications' },
      { id: 'profile', label: 'Profile', icon: 'user', href: '/admin/profile' },
    ],
    pinnedDefault: ['dashboard', 'products', 'inventory'],
  },
  {
    role: 'training_manager', label: 'Training Manager',
    sidebarItems: buildSidebarItems('training_manager'),
    topNavItems: [
      { id: 'notifications', label: 'Notifications', icon: 'bell', href: '/admin/notifications' },
      { id: 'profile', label: 'Profile', icon: 'user', href: '/admin/profile' },
    ],
    pinnedDefault: ['dashboard', 'training'],
  },
  {
    role: 'crm_manager', label: 'CRM Manager',
    sidebarItems: buildSidebarItems('crm_manager'),
    topNavItems: [
      { id: 'notifications', label: 'Notifications', icon: 'bell', href: '/admin/notifications' },
      { id: 'profile', label: 'Profile', icon: 'user', href: '/admin/profile' },
    ],
    pinnedDefault: ['dashboard', 'customers'],
  },
  {
    role: 'support_executive', label: 'Support Executive',
    sidebarItems: buildSidebarItems('support_executive'),
    topNavItems: [
      { id: 'notifications', label: 'Notifications', icon: 'bell', href: '/admin/notifications' },
      { id: 'profile', label: 'Profile', icon: 'user', href: '/admin/profile' },
    ],
    pinnedDefault: ['dashboard', 'orders', 'support', 'customers'],
  },
  {
    role: 'finance_manager', label: 'Finance Manager',
    sidebarItems: buildSidebarItems('finance_manager'),
    topNavItems: [
      { id: 'notifications', label: 'Notifications', icon: 'bell', href: '/admin/notifications' },
      { id: 'profile', label: 'Profile', icon: 'user', href: '/admin/profile' },
    ],
    pinnedDefault: ['dashboard', 'finance', 'reports'],
  },
  {
    role: 'marketing_manager', label: 'Marketing Manager',
    sidebarItems: buildSidebarItems('marketing_manager'),
    topNavItems: [
      { id: 'notifications', label: 'Notifications', icon: 'bell', href: '/admin/notifications' },
      { id: 'profile', label: 'Profile', icon: 'user', href: '/admin/profile' },
    ],
    pinnedDefault: ['dashboard', 'marketing', 'customers', 'reports'],
  },
  {
    role: 'viewer', label: 'Viewer',
    sidebarItems: buildSidebarItems('viewer'),
    topNavItems: [
      { id: 'notifications', label: 'Notifications', icon: 'bell', href: '/admin/notifications' },
      { id: 'profile', label: 'Profile', icon: 'user', href: '/admin/profile' },
    ],
    pinnedDefault: ['dashboard', 'products', 'orders'],
  },
];

export function getNavConfigForRole(role: AdminRole): RoleNavConfig {
  return ROLE_NAV_CONFIGS.find((c) => c.role === role) ?? ROLE_NAV_CONFIGS[0];
}
