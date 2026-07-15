export type AdminRole =
  | 'super_admin'
  | 'administrator'
  | 'manager'
  | 'inventory_manager'
  | 'training_manager'
  | 'crm_manager'
  | 'support_executive'
  | 'finance_manager'
  | 'marketing_manager'
  | 'viewer';

export type SidebarMode = 'expanded' | 'collapsed' | 'mini' | 'floating';

export interface NavGroup {
  id: string;
  label: string;
  icon?: string;
  children: NavItem[];
  collapsed?: boolean;
  roles?: AdminRole[];
}

export interface NavItem {
  id: string;
  label: string;
  href?: string;
  icon?: string;
  badge?: string | number;
  badgeColor?: string;
  children?: NavItem[];
  disabled?: boolean;
  roles?: AdminRole[];
  pinned?: boolean;
  separator?: boolean;
  isGroup?: boolean;
}

export interface RoleNavConfig {
  role: AdminRole;
  label: string;
  sidebarItems: NavItem[];
  topNavItems: NavItem[];
  pinnedDefault: string[];
}

export type NotificationItem = NavNotification;

export interface NavNotification {
  id: string;
  title: string;
  message: string;
  type: 'info' | 'warning' | 'error' | 'success';
  read: boolean;
  timestamp: string;
  href?: string;
  category?: string;
}

export interface FavoriteItem {
  id: string;
  label: string;
  href: string;
  icon?: string;
  pinned: boolean;
  timestamp: number;
}

export interface RecentPage {
  id: string;
  label: string;
  href: string;
  icon?: string;
  timestamp: number;
}

export interface CommandItem {
  id: string;
  label: string;
  description?: string;
  category: string;
  href?: string;
  icon?: string;
  shortcut?: string;
  keywords?: string[];
}

export interface WorkspaceConfig {
  title: string;
  description?: string;
  icon?: string;
  tabs?: { id: string; label: string; href: string }[];
  actions?: { id: string; label: string; icon?: string; onClick: () => void }[];
}

export interface UserPreferences {
  sidebarMode: SidebarMode;
  sidebarWidth: number;
  tableDensity: 'compact' | 'comfortable' | 'spacious';
  theme: 'light' | 'dark' | 'system';
  dashboardLayout: 'default' | 'compact' | 'expanded';
}
