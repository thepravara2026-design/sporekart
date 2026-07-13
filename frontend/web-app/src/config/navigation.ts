import type { PageDef, Role, WorkspaceDef } from './roles';

// ============================================================================
// SINGLE SOURCE OF TRUTH for the SporeKart enterprise information architecture.
// This data drives the sidebar, breadcrumbs, router, role filtering, and the
// command palette. Adding a page is a data change here — no structural rewrite.
// Reuses the 12 workspaces defined in docs/ui/information-architecture.md.
// ============================================================================

export const WORKSPACES: WorkspaceDef[] = [
  {
    id: 'public',
    label: 'Public',
    icon: '🌐',
    group: 'discover',
    rootPath: '/',
    roles: 'public',
    description: 'Discover the platform, browse the catalog, start a purchase.',
    children: [
      { path: '/', label: 'Home', roles: 'public', workspaceId: 'public', description: 'Landing and platform overview.', primaryAction: 'Browse products' },
      { path: '/search', label: 'Search', roles: 'public', workspaceId: 'public', description: 'Global search results.' },
      { path: '/products', label: 'Products', roles: 'public', workspaceId: 'public', description: 'Product catalog.', primaryAction: 'Filter catalog' },
      { path: '/products/:id', label: 'Product detail', roles: 'public', workspaceId: 'public', description: 'Single product view.' },
      { path: '/cart', label: 'Cart', roles: 'public', workspaceId: 'public', description: 'Shopping cart.', primaryAction: 'Checkout' },
      { path: '/checkout', label: 'Checkout', roles: 'public', workspaceId: 'public', description: 'Checkout flow.', primaryAction: 'Place order' },
    ],
  },
  {
    id: 'customer',
    label: 'Customer',
    icon: '👤',
    group: 'discover',
    rootPath: '/account',
    roles: ['customer', 'grower'],
    description: 'Personal account: orders, profile, addresses, saved items.',
    children: [
      { path: '/account', label: 'Overview', roles: ['customer', 'grower'], workspaceId: 'customer', description: 'Account home.', primaryAction: 'View orders' },
      { path: '/account/orders', label: 'Orders', roles: ['customer', 'grower'], workspaceId: 'customer', description: 'My orders.' },
      { path: '/account/profile', label: 'Profile', roles: ['customer', 'grower'], workspaceId: 'customer', description: 'My profile.' },
      { path: '/account/addresses', label: 'Addresses', roles: ['customer', 'grower'], workspaceId: 'customer', description: 'Saved addresses.', primaryAction: 'Add address' },
      { path: '/account/wishlist', label: 'Wishlist', roles: ['customer', 'grower'], workspaceId: 'customer', description: 'Saved items.' },
    ],
  },
  {
    id: 'orders',
    label: 'Orders',
    icon: '📦',
    group: 'operate',
    rootPath: '/orders',
    roles: ['distributor', 'support', 'administrator'],
    description: 'Operational order processing, fulfillment, status.',
    children: [
      { path: '/orders', label: 'Order queue', roles: ['distributor', 'support', 'administrator'], workspaceId: 'orders', description: 'Order list / queue.', primaryAction: 'New order' },
      { path: '/orders/:id', label: 'Order detail', roles: ['distributor', 'support', 'administrator'], workspaceId: 'orders', description: 'Single order.' },
      { path: '/orders/:id/fulfill', label: 'Fulfill', roles: ['distributor', 'administrator'], workspaceId: 'orders', description: 'Fulfillment action.' },
    ],
  },
  {
    id: 'products',
    label: 'Products',
    icon: '🧪',
    group: 'operate',
    rootPath: '/catalog',
    roles: ['grower', 'distributor', 'administrator'],
    description: 'Catalog and inventory management.',
    children: [
      { path: '/catalog', label: 'Catalog', roles: ['grower', 'distributor', 'administrator'], workspaceId: 'products', description: 'Catalog list.', primaryAction: 'New product' },
      { path: '/catalog/:id', label: 'Product / SKU', roles: ['grower', 'distributor', 'administrator'], workspaceId: 'products', description: 'SKU detail.' },
      { path: '/catalog/new', label: 'New product', roles: ['grower', 'distributor', 'administrator'], workspaceId: 'products', description: 'Create product.', primaryAction: 'Create' },
    ],
  },
  {
    id: 'training',
    label: 'Training',
    icon: '🎓',
    group: 'operate',
    rootPath: '/training',
    roles: 'public',
    description: 'Courses, sessions, registration, engagement.',
    children: [
      { path: '/training', label: 'Training catalog', roles: 'public', workspaceId: 'training', description: 'Course catalog.', primaryAction: 'Browse sessions' },
      { path: '/training/:id', label: 'Session detail', roles: 'public', workspaceId: 'training', description: 'Session view.' },
      { path: '/training/create', label: 'Create session', roles: ['trainer', 'administrator'], workspaceId: 'training', description: 'Create a session.', primaryAction: 'Create' },
    ],
  },
  {
    id: 'ai',
    label: 'AI Workspace',
    icon: '✨',
    group: 'intelligence',
    rootPath: '/ai',
    roles: ['customer', 'grower', 'trainer', 'support', 'administrator', 'business_owner', 'governance_manager'],
    description: 'AI assistant, prompts, knowledge, RAG.',
    children: [
      { path: '/ai', label: 'Assistant', roles: ['customer', 'grower', 'trainer', 'support', 'administrator', 'business_owner', 'governance_manager'], workspaceId: 'ai', description: 'Assistant home.', primaryAction: 'Ask AI' },
      { path: '/ai/chat', label: 'Conversations', roles: ['customer', 'grower', 'trainer', 'support', 'administrator', 'business_owner', 'governance_manager'], workspaceId: 'ai', description: 'Chat history.' },
      { path: '/ai/prompts', label: 'Prompt library', roles: ['trainer', 'administrator'], workspaceId: 'ai', description: 'Prompt management.', primaryAction: 'New prompt' },
      { path: '/ai/knowledge', label: 'Knowledge', roles: ['trainer', 'administrator'], workspaceId: 'ai', description: 'RAG sources.' },
    ],
  },
  {
    id: 'governance',
    label: 'Governance',
    icon: '🛡️',
    group: 'intelligence',
    rootPath: '/governance',
    roles: ['governance_manager', 'administrator'],
    description: 'Policies, approvals, compliance, access control.',
    children: [
      { path: '/governance', label: 'Overview', roles: ['governance_manager', 'administrator'], workspaceId: 'governance', description: 'Governance home.' },
      { path: '/governance/policies', label: 'Policies', roles: ['governance_manager', 'administrator'], workspaceId: 'governance', description: 'Policy registry.', primaryAction: 'New policy' },
      { path: '/governance/approvals', label: 'Approvals', roles: ['governance_manager', 'administrator'], workspaceId: 'governance', description: 'Approval queue.' },
      { path: '/governance/compliance', label: 'Compliance', roles: ['governance_manager', 'administrator'], workspaceId: 'governance', description: 'Compliance status.' },
      { path: '/governance/access', label: 'Access control', roles: ['governance_manager', 'administrator'], workspaceId: 'governance', description: 'Access control.', primaryAction: 'Grant access' },
    ],
  },
  {
    id: 'analytics',
    label: 'Analytics',
    icon: '📈',
    group: 'intelligence',
    rootPath: '/analytics',
    roles: ['business_owner', 'administrator', 'grower'],
    description: 'Trusted, sourced metrics and drill-downs.',
    children: [
      { path: '/analytics', label: 'Overview', roles: ['business_owner', 'administrator', 'grower'], workspaceId: 'analytics', description: 'Analytics home.' },
      { path: '/analytics/sales', label: 'Sales', roles: ['business_owner', 'administrator', 'grower'], workspaceId: 'analytics', description: 'Sales metrics.', primaryAction: 'Export' },
      { path: '/analytics/operations', label: 'Operations', roles: ['business_owner', 'administrator', 'grower'], workspaceId: 'analytics', description: 'Operations metrics.' },
    ],
  },
  {
    id: 'admin',
    label: 'Administration',
    icon: '⚙️',
    group: 'platform',
    rootPath: '/admin',
    roles: ['administrator'],
    description: 'Users, content, config, monitoring.',
    children: [
      { path: '/admin', label: 'Overview', roles: ['administrator'], workspaceId: 'admin', description: 'Admin home.' },
      { path: '/admin/users', label: 'Users', roles: ['administrator'], workspaceId: 'admin', description: 'User management.', primaryAction: 'Invite user' },
      { path: '/admin/content', label: 'Content', roles: ['administrator'], workspaceId: 'admin', description: 'Content moderation.' },
      { path: '/admin/config', label: 'Config', roles: ['administrator'], workspaceId: 'admin', description: 'Platform config.' },
      { path: '/admin/monitoring', label: 'Monitoring', roles: ['administrator'], workspaceId: 'admin', description: 'Monitoring / health.' },
    ],
  },
  {
    id: 'cms',
    label: 'CMS',
    icon: '📝',
    group: 'platform',
    rootPath: '/cms',
    roles: ['administrator', 'trainer'],
    description: 'Marketing and educational content: pages and media.',
    children: [
      { path: '/cms', label: 'Overview', roles: ['administrator', 'trainer'], workspaceId: 'cms', description: 'CMS home.' },
      { path: '/cms/pages', label: 'Pages', roles: ['administrator', 'trainer'], workspaceId: 'cms', description: 'Page management.', primaryAction: 'New page' },
      { path: '/cms/media', label: 'Media', roles: ['administrator', 'trainer'], workspaceId: 'cms', description: 'Media library.' },
    ],
  },
  {
    id: 'support',
    label: 'Support',
    icon: '💬',
    group: 'platform',
    rootPath: '/support',
    roles: ['support', 'customer', 'grower', 'trainer', 'administrator'],
    description: 'Customer support: tickets and knowledge base.',
    children: [
      { path: '/support', label: 'Support home', roles: ['support', 'customer', 'grower', 'trainer', 'administrator'], workspaceId: 'support', description: 'Support home.' },
      { path: '/support/tickets', label: 'Tickets', roles: ['support', 'customer', 'grower', 'trainer', 'administrator'], workspaceId: 'support', description: 'Ticket queue / my tickets.', primaryAction: 'New ticket' },
      { path: '/support/kb', label: 'Knowledge base', roles: 'public', workspaceId: 'support', description: 'Help articles.', primaryAction: 'Search KB' },
    ],
  },
  {
    id: 'settings',
    label: 'Settings',
    icon: '🔧',
    group: 'platform',
    rootPath: '/settings',
    roles: ['customer', 'grower', 'trainer', 'support', 'administrator', 'business_owner', 'governance_manager'],
    description: 'Account and workspace configuration.',
    children: [
      { path: '/settings', label: 'Overview', roles: ['customer', 'grower', 'trainer', 'support', 'administrator', 'business_owner', 'governance_manager'], workspaceId: 'settings', description: 'Settings home.' },
      { path: '/settings/profile', label: 'Profile', roles: ['customer', 'grower', 'trainer', 'support', 'administrator', 'business_owner', 'governance_manager'], workspaceId: 'settings', description: 'Profile & preferences.' },
      { path: '/settings/security', label: 'Security', roles: ['customer', 'grower', 'trainer', 'support', 'administrator', 'business_owner', 'governance_manager'], workspaceId: 'settings', description: 'Security & sessions.' },
      { path: '/settings/workspace', label: 'Workspace', roles: ['administrator', 'business_owner'], workspaceId: 'settings', description: 'Workspace / org.' },
      { path: '/settings/billing', label: 'Billing', roles: ['administrator', 'business_owner'], workspaceId: 'settings', description: 'Billing & plans.' },
    ],
  },
  {
    id: 'demo',
    label: 'Demo',
    icon: '🧪',
    group: 'platform',
    rootPath: '/demo',
    roles: 'public',
    description: 'Part 1C UX Standards demonstrations.',
    children: [
      { path: '/demo', label: 'Demo Index', roles: 'public', workspaceId: 'demo', description: 'Overview of all demos.', primaryAction: 'Open Responsive' },
      { path: '/demo/responsive', label: 'Responsive Layout', roles: 'public', workspaceId: 'demo', description: 'Viewport toggle, grid overlay, drawer/rail behavior.' },
      { path: '/demo/keyboard', label: 'Keyboard Navigation', roles: 'public', workspaceId: 'demo', description: 'Focus order, component patterns, shortcuts.' },
      { path: '/demo/loading', label: 'Loading Experience', roles: 'public', workspaceId: 'demo', description: 'Skeletons, action loaders, offline, retry.' },
      { path: '/demo/errors', label: 'Error Pages', roles: 'public', workspaceId: 'demo', description: '404, 403, 401, 500, network, timeout, validation.' },
      { path: '/demo/empty', label: 'Empty States', roles: 'public', workspaceId: 'demo', description: 'All 30+ empty states with variants.' },
      { path: '/demo/forms', label: 'Form Patterns', roles: 'public', workspaceId: 'demo', description: 'Validation, OTP, address, checkout, auto-save.' },
      { path: '/demo/microcopy', label: 'Microcopy Gallery', roles: 'public', workspaceId: 'demo', description: 'Buttons, toasts, labels, errors, empty states.' },
    ],
  },
  {
    id: 'design-system',
    label: 'Design System',
    icon: '🎨',
    group: 'platform',
    rootPath: '/design-system',
    roles: 'public',
    description: 'Design Language Showcase — tokens, colors, typography, spacing, elevation, icons, illustrations.',
    children: [
      { path: '/design-system', label: 'Playground', roles: 'public', workspaceId: 'design-system', description: 'Enterprise Design Playground — component overview, tokens, icons, accessibility, quality dashboard.', primaryAction: 'Open Playground' },
      { path: '/design-system/buttons', label: 'Buttons Preview', roles: 'public', workspaceId: 'design-system', description: 'All Button variants, sizes, and states.' },
      { path: '/design-system/links', label: 'Links Preview', roles: 'public', workspaceId: 'design-system', description: 'All Link variants with sizes and states.' },
      { path: '/design-system/icons', label: 'Icon Library', roles: 'public', workspaceId: 'design-system', description: 'Searchable icon library with categories, sizes, accessibility labels, and usage examples.' },
      { path: '/design-system/inputs', label: 'Inputs Preview', roles: 'public', workspaceId: 'design-system', description: 'All input types, sizes, and states.' },
      { path: '/design-system/search', label: 'Search Preview', roles: 'public', workspaceId: 'design-system', description: 'Search input with all states.' },
      { path: '/design-system/password', label: 'Password Preview', roles: 'public', workspaceId: 'design-system', description: 'Password with visibility toggle and strength meter.' },
      { path: '/design-system/otp', label: 'OTP Preview', roles: 'public', workspaceId: 'design-system', description: 'OTP input with states and lengths.' },
      { path: '/design-system/checkbox', label: 'Checkbox Preview', roles: 'public', workspaceId: 'design-system', description: 'Checkbox and CheckboxGroup with all states.' },
      { path: '/design-system/radio', label: 'Radio Preview', roles: 'public', workspaceId: 'design-system', description: 'Radio group with layouts and states.' },
      { path: '/design-system/switch', label: 'Switch Preview', roles: 'public', workspaceId: 'design-system', description: 'Toggle switch with all states and sizes.' },
      { path: '/design-system/forms', label: 'Forms Index', roles: 'public', workspaceId: 'design-system', description: 'Form system showcase.' },
      { path: '/design-system/forms/layouts', label: 'Form Layouts', roles: 'public', workspaceId: 'design-system', description: 'All form layout variants.' },
      { path: '/design-system/forms/validation', label: 'Form Validation', roles: 'public', workspaceId: 'design-system', description: 'Validation framework demo.' },
      { path: '/design-system/forms/address', label: 'Address Form', roles: 'public', workspaceId: 'design-system', description: 'Address form with all fields.' },
      { path: '/design-system/forms/upload', label: 'File Upload', roles: 'public', workspaceId: 'design-system', description: 'File upload with all states.' },
      { path: '/design-system/forms/select', label: 'Select Preview', roles: 'public', workspaceId: 'design-system', description: 'All select variants.' },
      { path: '/design-system/cards', label: 'Cards Preview', roles: 'public', workspaceId: 'design-system', description: 'All 15 card variants with states.' },
      { path: '/design-system/tables', label: 'Tables Preview', roles: 'public', workspaceId: 'design-system', description: 'Enterprise table with sorting, selection, pagination.' },
      { path: '/design-system/lists', label: 'Lists Preview', roles: 'public', workspaceId: 'design-system', description: 'All list variants.' },
      { path: '/design-system/badges', label: 'Badges Preview', roles: 'public', workspaceId: 'design-system', description: 'All badge variants and sizes.' },
      { path: '/design-system/chips', label: 'Chips Preview', roles: 'public', workspaceId: 'design-system', description: 'All chip variants.' },
      { path: '/design-system/avatars', label: 'Avatars Preview', roles: 'public', workspaceId: 'design-system', description: 'Avatar types, sizes, groups.' },
      { path: '/design-system/empty-states', label: 'Empty States Preview', roles: 'public', workspaceId: 'design-system', description: 'All 8 empty state variants.' },
      { path: '/design-system/skeletons', label: 'Skeletons Preview', roles: 'public', workspaceId: 'design-system', description: 'All skeleton types.' },
      { path: '/design-system/navigation', label: 'Nav Index', roles: 'public', workspaceId: 'design-system', description: 'Navigation system overview.' },
      { path: '/design-system/header', label: 'Header Preview', roles: 'public', workspaceId: 'design-system', description: 'All header variants.' },
      { path: '/design-system/sidebar', label: 'Sidebar Preview', roles: 'public', workspaceId: 'design-system', description: 'Sidebar states and responsive.' },
      { path: '/design-system/breadcrumb', label: 'Breadcrumb Preview', roles: 'public', workspaceId: 'design-system', description: 'Breadcrumb variants.' },
      { path: '/design-system/menu', label: 'Menu Preview', roles: 'public', workspaceId: 'design-system', description: 'All menu types.' },
      { path: '/design-system/tabs', label: 'Tabs Preview', roles: 'public', workspaceId: 'design-system', description: 'Tab variants.' },
      { path: '/design-system/pagination', label: 'Pagination Preview', roles: 'public', workspaceId: 'design-system', description: 'Pagination variants.' },
      { path: '/design-system/stepper', label: 'Stepper Preview', roles: 'public', workspaceId: 'design-system', description: 'Stepper variants.' },
      { path: '/design-system/layouts', label: 'Layouts Preview', roles: 'public', workspaceId: 'design-system', description: 'Layout templates.' },
      { path: '/design-system/command-palette', label: 'Command Palette Preview', roles: 'public', workspaceId: 'design-system', description: 'Command palette demo.' },
      { path: '/design-system/dialogs', label: 'Dialogs Preview', roles: 'public', workspaceId: 'design-system', description: 'All dialog variants and states.' },
      { path: '/design-system/modals', label: 'Modals Preview', roles: 'public', workspaceId: 'design-system', description: 'All modal variants and states.' },
      { path: '/design-system/toasts', label: 'Toasts Preview', roles: 'public', workspaceId: 'design-system', description: 'Toast system with all variants and positions.' },
      { path: '/design-system/notifications', label: 'Notifications Preview', roles: 'public', workspaceId: 'design-system', description: 'Notification center demo.' },
      { path: '/design-system/alerts', label: 'Alerts Preview', roles: 'public', workspaceId: 'design-system', description: 'Alert system with all variants.' },
      { path: '/design-system/tooltips', label: 'Tooltips Preview', roles: 'public', workspaceId: 'design-system', description: 'Tooltip system with all variants.' },
      { path: '/design-system/popovers', label: 'Popovers Preview', roles: 'public', workspaceId: 'design-system', description: 'Popover system with all variants.' },
      { path: '/design-system/progress', label: 'Progress Preview', roles: 'public', workspaceId: 'design-system', description: 'All progress component variants.' },
      { path: '/design-system/loading', label: 'Loading Preview', roles: 'public', workspaceId: 'design-system', description: 'All loading experience variants.' },
      { path: '/design-system/status', label: 'Status Preview', roles: 'public', workspaceId: 'design-system', description: 'Status indicators demo.' },
      { path: '/design-system/charts', label: 'Charts Preview', roles: 'public', workspaceId: 'design-system', description: 'All chart types (line, area, bar, pie, radial, scatter, heatmap).' },
      { path: '/design-system/kpis', label: 'KPIs Preview', roles: 'public', workspaceId: 'design-system', description: 'KPI and statistics components.' },
      { path: '/design-system/timelines', label: 'Timelines Preview', roles: 'public', workspaceId: 'design-system', description: 'All timeline variants.' },
      { path: '/design-system/calendars', label: 'Calendars Preview', roles: 'public', workspaceId: 'design-system', description: 'Calendar views and date range picker.' },
      { path: '/design-system/data-filters', label: 'Data Filters Preview', roles: 'public', workspaceId: 'design-system', description: 'Filter components (date, search, quick filters).' },
      { path: '/design-system/export', label: 'Export Preview', roles: 'public', workspaceId: 'design-system', description: 'Export menu and format hooks.' },
      { path: '/design-system/catalog', label: 'Component Catalog', roles: 'public', workspaceId: 'design-system', description: 'Auto-discovered component directory with search, filters, and metadata.' },
      { path: '/design-system/tokens', label: 'Token Explorer', roles: 'public', workspaceId: 'design-system', description: 'Design token explorer — colors, typography, spacing, radius, elevation, animation.' },
      { path: '/design-system/accessibility', label: 'Accessibility Center', roles: 'public', workspaceId: 'design-system', description: 'WCAG compliance, keyboard navigation, ARIA usage, focus order, contrast validation.' },
      { path: '/design-system/docs', label: 'Docs Center', roles: 'public', workspaceId: 'design-system', description: 'Architecture, guidelines, coding standards, contribution guide, release notes.' },
      { path: '/design-system/quality', label: 'Quality Dashboard', roles: 'public', workspaceId: 'design-system', description: 'Accessibility status, responsive status, documentation coverage, review progress.' },
    ],
  },
];

// ---- Derived helpers -------------------------------------------------------

export function isPublic(roles: Role[] | 'public'): boolean {
  return roles === 'public';
}

export function canView(roles: Role[] | 'public', active: Role): boolean {
  if (roles === 'public') return true;
  return roles.includes(active);
}

export function getVisibleWorkspaces(active: Role): WorkspaceDef[] {
  return WORKSPACES.filter((w) => canView(w.roles, active));
}

export function getAllPages(): PageDef[] {
  return WORKSPACES.flatMap((w) => w.children);
}

export function getWorkspaceById(id: string): WorkspaceDef | undefined {
  return WORKSPACES.find((w) => w.id === id);
}

// Convert a route pattern with :params into a matcher + param extractor.
function matchPattern(pattern: string, pathname: string): Record<string, string> | null {
  const pSeg = pattern.split('/').filter(Boolean);
  const aSeg = pathname.split('/').filter(Boolean);
  if (pSeg.length !== aSeg.length) return null;
  const params: Record<string, string> = {};
  for (let i = 0; i < pSeg.length; i++) {
    if (pSeg[i].startsWith(':')) {
      params[pSeg[i].slice(1)] = decodeURIComponent(aSeg[i]);
    } else if (pSeg[i] !== aSeg[i]) {
      return null;
    }
  }
  return params;
}

export interface ResolvedRoute {
  page: PageDef;
  workspace: WorkspaceDef;
  params: Record<string, string>;
}

export function resolveRoute(pathname: string): ResolvedRoute | null {
  for (const workspace of WORKSPACES) {
    for (const page of workspace.children) {
      const params = matchPattern(page.path, pathname);
      if (params) return { page, workspace, params };
    }
  }
  return null;
}

// Breadcrumb trail: [workspace label -> link, page label (current)].
export interface Crumb {
  label: string;
  to?: string;
}

export function buildBreadcrumb(pathname: string): { crumbs: Crumb[]; workspace: WorkspaceDef | null } {
  const resolved = resolveRoute(pathname);
  if (!resolved) return { crumbs: [{ label: 'Not found' }], workspace: null };
  const { page, workspace, params } = resolved;
  const crumbs: Crumb[] = [{ label: workspace.label, to: workspace.rootPath }];
  // For the workspace root itself, do not duplicate into a self-link.
  if (page.path !== workspace.rootPath) {
    let label = page.label;
    if (params.id) label = params.id;
    crumbs.push({ label, to: undefined });
  }
  return { crumbs, workspace };
}
