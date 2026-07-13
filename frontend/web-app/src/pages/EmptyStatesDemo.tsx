import { useState } from 'react';

const EMPTY_STATES = [
  { id: 'orders-empty', workspace: 'Customer', variant: 'standard', title: 'No orders yet', body: 'When you place an order, it will appear here.', cta: 'Shop products', secondary: 'View wishlist', icon: '📦' },
  { id: 'orders-filtered', workspace: 'Customer', variant: 'filtered', title: 'No orders match your filters', body: 'Try adjusting your date range or status filters.', cta: 'Clear filters', secondary: 'View all orders', icon: '🔍' },
  { id: 'wishlist-empty', workspace: 'Customer', variant: 'standard', title: 'Your wishlist is empty', body: 'Save products you like to find them later.', cta: 'Browse products', icon: '❤️' },
  { id: 'addresses-empty', workspace: 'Customer', variant: 'standard', title: 'No saved addresses', body: 'Add an address to speed up checkout.', cta: 'Add address', icon: '📍' },
  { id: 'orders-queue-empty', workspace: 'Orders', variant: 'standard', title: 'No orders to process', body: 'Orders assigned to you will appear here.', cta: 'Refresh', icon: '📦' },
  { id: 'catalog-empty', workspace: 'Products', variant: 'standard', title: 'No products in catalog', body: 'Create your first product or import a catalog.', cta: 'New product', secondary: 'Import CSV', icon: '🧪' },
  { id: 'catalog-first-time', workspace: 'Products', variant: 'first-time', title: 'Welcome to Products', body: 'Start building your catalog by adding products manually or importing from a spreadsheet.', cta: 'Add your first product', secondary: 'Import catalog', icon: '👋' },
  { id: 'training-catalog-empty', workspace: 'Training', variant: 'standard', title: 'No training sessions', body: 'Published sessions will appear here.', cta: 'Create session', secondary: 'Browse catalog', icon: '🎓' },
  { id: 'training-enrolled-empty', workspace: 'Training', variant: 'standard', title: 'You\'re not enrolled yet', body: 'Find a session and register to get started.', cta: 'Browse training', icon: '📚' },
  { id: 'ai-chat-empty', workspace: 'AI Workspace', variant: 'standard', title: 'No conversations yet', body: 'Ask a question to start your first chat.', cta: 'Ask AI', secondary: 'View prompt library', icon: '✨' },
  { id: 'ai-prompts-empty', workspace: 'AI Workspace', variant: 'standard', title: 'No prompts saved', body: 'Save your favorite prompts for reuse.', cta: 'Create prompt', secondary: 'Browse library', icon: '💡' },
  { id: 'ai-knowledge-empty', workspace: 'AI Workspace', variant: 'standard', title: 'No knowledge sources', body: 'Add documents to enable RAG answers.', cta: 'Upload document', icon: '📚' },
  { id: 'governance-policies-empty', workspace: 'Governance', variant: 'standard', title: 'No policies defined', body: 'Create policies to govern platform behavior.', cta: 'New policy', secondary: 'Import template', icon: '🛡️' },
  { id: 'governance-approvals-empty', workspace: 'Governance', variant: 'standard', title: 'No pending approvals', body: 'Items requiring your review will appear here.', cta: 'Refresh', secondary: 'View all', icon: '✅' },
  { id: 'governance-compliance-empty', workspace: 'Governance', variant: 'standard', title: 'All compliant', body: 'No compliance issues detected.', cta: 'Run scan', secondary: 'View history', icon: '✅' },
  { id: 'governance-access-empty', workspace: 'Governance', variant: 'standard', title: 'No access grants', body: 'Grant access to users or groups.', cta: 'Grant access', secondary: 'View audit log', icon: '🔐' },
  { id: 'analytics-empty', workspace: 'Analytics', variant: 'standard', title: 'No data available', body: 'Data appears after your first transaction.', cta: 'View documentation', icon: '📈' },
  { id: 'analytics-sales-empty', workspace: 'Analytics', variant: 'standard', title: 'No sales yet', body: 'Sales metrics appear after orders are placed.', cta: 'View orders', icon: '📊' },
  { id: 'admin-users-empty', workspace: 'Administration', variant: 'standard', title: 'No users found', body: 'Invite team members to get started.', cta: 'Invite user', secondary: 'Import users', icon: '👥' },
  { id: 'admin-content-empty', workspace: 'Administration', variant: 'standard', title: 'No content items', body: 'Create pages or upload media.', cta: 'New page', secondary: 'Upload media', icon: '📝' },
  { id: 'admin-config-empty', workspace: 'Administration', variant: 'standard', title: 'Default settings active', body: 'No custom configuration.', cta: 'Edit settings', secondary: 'View defaults', icon: '⚙️' },
  { id: 'admin-monitoring-empty', workspace: 'Administration', variant: 'standard', title: 'All systems healthy', body: 'No active alerts or incidents.', cta: 'View logs', icon: '✅' },
  { id: 'cms-pages-empty', workspace: 'CMS', variant: 'standard', title: 'No pages created', body: 'Build your first page.', cta: 'New page', icon: '📄' },
  { id: 'cms-media-empty', workspace: 'CMS', variant: 'standard', title: 'No media uploaded', body: 'Upload images, PDFs, videos.', cta: 'Upload', icon: '🖼️' },
  { id: 'support-tickets-empty', workspace: 'Support', variant: 'standard', title: 'No tickets', body: 'Create a ticket if you need help.', cta: 'New ticket', secondary: 'Browse KB', icon: '🎫' },
  { id: 'support-kb-empty', workspace: 'Support', variant: 'standard', title: 'No help articles', body: 'Articles will appear as they\'re published.', cta: 'Contact support', icon: '📖' },
  { id: 'settings-profile-empty', workspace: 'Settings', variant: 'standard', title: 'Profile incomplete', body: 'Add your details to personalize your experience.', cta: 'Edit profile', icon: '👤' },
  { id: 'settings-security-empty', workspace: 'Settings', variant: 'standard', title: 'Two-factor not enabled', body: 'Enable 2FA for extra security.', cta: 'Enable 2FA', icon: '🔐' },
  { id: 'settings-workspace-empty', workspace: 'Settings', variant: 'standard', title: 'No workspace set up', body: 'Create a workspace to manage team billing.', cta: 'Create workspace', icon: '🏢' },
  { id: 'settings-billing-empty', workspace: 'Settings', variant: 'standard', title: 'No active plan', body: 'Choose a plan to unlock features.', cta: 'View plans', icon: '💳' },
  { id: 'search-empty', workspace: 'Public', variant: 'standard', title: 'No results for "query"', body: 'Try different keywords or check spelling.', cta: 'Clear search', secondary: 'Browse categories', icon: '🔍' },
  { id: 'notifications-empty', workspace: 'Support', variant: 'standard', title: 'No new notifications', body: 'You\'re all caught up.', cta: 'Mark all read', secondary: 'Settings', icon: '🔔' },
];

const VARIANTS = ['standard', 'first-time', 'filtered', 'permission', 'error'] as const;

export default function EmptyStatesDemo() {
  const [filter, setFilter] = useState<'all' | 'standard' | 'first-time' | 'filtered' | 'permission' | 'error'>('all');
  const [search, setSearch] = useState('');

  const filtered = EMPTY_STATES.filter(s => {
    if (filter !== 'all' && s.variant !== filter) return false;
    if (search && !s.title.toLowerCase().includes(search.toLowerCase()) && !s.workspace.toLowerCase().includes(search.toLowerCase())) return false;
    return true;
  });

  return (
    <div className="sk-content__header">
      <div className="sk-content__title-row">
        <div>
          <h1>Empty States Gallery</h1>
          <p className="sk-content__subtitle">All 30+ empty states with variants: standard, first-time, filtered, permission, error. Formula: Explain → Guide → Act.</p>
        </div>
      </div>

      <div className="sk-demo-toolbar">
        <input type="search" placeholder="Search empty states…" value={search} onChange={e => setSearch(e.target.value)} className="sk-search-input" aria-label="Filter empty states" />
        <div className="sk-filter-tabs" role="tablist">
          {['all', ...VARIANTS].map(v => (
            <button key={v} role="tab" aria-selected={filter === v} className={`sk-tab ${filter === v ? 'sk-tab--active' : ''}`} onClick={() => setFilter(v as any)}>
              {v === 'all' ? 'All' : v.charAt(0).toUpperCase() + v.slice(1)}
            </button>
          ))}
        </div>
      </div>

      <div className="sk-empty-grid" role="list" aria-label="Empty states">
        {filtered.map(state => (
          <article key={state.id} className="sk-empty-card" role="listitem">
            <div className="sk-empty-card__variant sk-empty-card__variant--{state.variant}">{state.variant}</div>
            <div className="sk-empty-card__icon" aria-hidden="true">{state.icon}</div>
            <h3>{state.title}</h3>
            <p className="sk-empty-card__workspace">{state.workspace} / {state.variant}</p>
            <p className="sk-empty-card__body">{state.body}</p>
            <div className="sk-empty-card__actions">
              <button className="sk-primary-action sk-empty-card__cta">{state.cta}</button>
              {state.secondary && <button className="sk-secondary-action sk-empty-card__cta">{state.secondary}</button>}
            </div>
          </article>
        ))}
      </div>

      {filtered.length === 0 && (
        <section className="sk-panel" aria-labelledby="no-results">
          <h2 id="no-results">No results for "{search}" in {filter}</h2>
          <p>Try a different search or filter.</p>
        </section>
      )}
    </div>
  );
}