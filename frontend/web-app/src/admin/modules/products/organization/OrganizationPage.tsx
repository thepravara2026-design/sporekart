import React from 'react';
import { PermissionGate } from '../../../permissions/PermissionGate';
import { FeatureFlagProvider } from '../../../feature-flags/FeatureFlagProvider';
import { PermissionProvider } from '../../../permissions/PermissionProvider';
import { useOrganizationState } from './state/useOrganizationState';
import { OrganizationNav } from './state/OrganizationNav';
import { OrganizationToolbar } from './components/OrganizationToolbar';
import { OrganizationDashboard } from './components/OrganizationDashboard';
import { CategoryManager } from './components/CategoryManager';
import { CollectionManager } from './components/CollectionManager';
import { BrandManager } from './components/BrandManager';
import { TagManager } from './components/TagManager';
import { ProductAssignment } from './components/ProductAssignment';
import { BulkOrganization } from './components/BulkOrganization';
import { CategoryAnalytics } from './components/CategoryAnalytics';
import { BrandAnalyticsDashboard } from './components/BrandAnalytics';
import { CategoryTree } from './components/CategoryTree';
import { CURRENT_ORG_ROLE } from './permissions';
import './Organization.css';

function OrgSettings() {
  return (
    <div style={{ padding: 'var(--space-4)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
      <h3 style={{ margin: '0 0 12px', fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)' }}>Organization Settings</h3>
      <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>
        Organization settings are in Mock Mode. Future settings will include default taxonomy behavior, auto-categorization rules, and smart tag configuration.
      </p>
      <div style={{ marginTop: 12, display: 'flex', flexDirection: 'column', gap: 8 }}>
        <div style={settRow}><span>Default Display Order</span><span style={{ fontWeight: 'var(--weight-semibold)' }}>Alphabetical</span></div>
        <div style={settRow}><span>Auto-Categorization</span><span style={{ fontWeight: 'var(--weight-semibold)' }}>Disabled (Future)</span></div>
        <div style={settRow}><span>Smart Tags</span><span style={{ fontWeight: 'var(--weight-semibold)' }}>Disabled (Future)</span></div>
        <div style={settRow}><span>Category Depth Limit</span><span style={{ fontWeight: 'var(--weight-semibold)' }}>Unlimited</span></div>
        <div style={settRow}><span>Active Role (Mock)</span><span style={{ fontWeight: 'var(--weight-semibold)' }}>{CURRENT_ORG_ROLE}</span></div>
      </div>
    </div>
  );
}

function OrgHelp() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-md)' }}>
      <div style={{ padding: 'var(--space-4)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
        <h3 style={{ margin: '0 0 8px', fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)' }}>About the Organization Workspace</h3>
        <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', lineHeight: 'var(--leading-relaxed)' }}>
          The Product Organization workspace manages the entire taxonomy hierarchy of the SporeKart catalog.
          Use categories, collections, brands, and tags to structure products for efficient discovery and management.
          This module operates entirely in Mock Mode.
        </p>
      </div>
      <div style={{ padding: 'var(--space-4)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
        <h3 style={{ margin: '0 0 8px', fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)' }}>Key Concepts</h3>
        <ul style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', lineHeight: 'var(--leading-relaxed)' }}>
          <li><strong>Categories</strong> — Hierarchical product classification (tree structure)</li>
          <li><strong>Collections</strong> — Curated product groupings (seasonal, featured, campaigns)</li>
          <li><strong>Brands</strong> — Manufacturer and brand management</li>
          <li><strong>Tags</strong> — Flexible metadata for search and filtering</li>
          <li><strong>Assignments</strong> — Associate products with taxonomy entities</li>
        </ul>
      </div>
    </div>
  );
}

export const OrganizationPage: React.FC = () => {
  const state = useOrganizationState();

  const renderSection = () => {
    switch (state.section) {
      case 'overview':
        return <OrganizationDashboard />;
      case 'categories':
        return <CategoryManager state={state} />;
      case 'collections':
        return <CollectionManager collections={state.collections} />;
      case 'brands':
        return <BrandManager brands={state.brands} />;
      case 'tags':
        return <TagManager tags={state.tags} />;
      case 'hierarchy':
        return (
          <div>
            <CategoryTree
              tree={state.categoryTree}
              selectedId={null}
              onSelect={() => {}}
              onToggleExpand={state.toggleExpand}
              onExpandAll={state.expandAll}
              onCollapseAll={state.collapseAll}
            />
          </div>
        );
      case 'assignments':
        return <ProductAssignment />;
      case 'bulk':
        return <BulkOrganization />;
      case 'analytics':
        return (
          <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
            <CategoryAnalytics />
            <BrandAnalyticsDashboard />
          </div>
        );
      case 'settings':
        return <OrgSettings />;
      case 'help':
        return <OrgHelp />;
      default:
        return <OrganizationDashboard />;
    }
  };

  const sectionLabel = ORG_SECTION_LABELS[state.section];

  return (
    <PermissionProvider initialRole="manager">
      <FeatureFlagProvider>
        <PermissionGate action="view" resource="organization">
          <div className="sk-org-page">
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
              <div>
                <h1 style={{ margin: 0, fontSize: 'var(--text-h1)', color: 'var(--color-text-primary)' }}>Product Organization</h1>
                <p style={{ margin: '4px 0 0', fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)' }}>
                  Manage categories, collections, brands, and tags
                </p>
              </div>
            </div>

            <OrganizationToolbar
              search={state.search}
              onSearch={state.setSearch}
              sort={state.sort}
              onSort={state.setSort}
              selectedIds={state.selectedIds}
              onClearSelection={state.clearSelection}
              sectionLabel={sectionLabel}
              totalResults={
                state.section === 'categories' ? state.categories.length :
                state.section === 'collections' ? state.collections.length :
                state.section === 'brands' ? state.brands.length :
                state.section === 'tags' ? state.tags.length : 0
              }
            />

            <div className="sk-org-grid">
              <aside className="sk-org-sidebar">
                <OrganizationNav activeSection={state.section} onSelect={state.setSection} />
              </aside>
              <main className="sk-org-content">
                {renderSection()}
              </main>
            </div>

            <footer style={{ marginTop: 'var(--space-section-gap)', fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', borderTop: '1px solid var(--color-border)', paddingTop: 'var(--space-component-gap)', display: 'flex', justifyContent: 'space-between' }}>
              <span>Mock Mode — no persistence. Role: {CURRENT_ORG_ROLE}</span>
              <span>Product Organization · Sprint 24 Part 6</span>
            </footer>
          </div>
        </PermissionGate>
      </FeatureFlagProvider>
    </PermissionProvider>
  );
};

const ORG_SECTION_LABELS: Record<string, string> = {
  overview: 'Overview', categories: 'Categories', collections: 'Collections', brands: 'Brands', tags: 'Tags',
  hierarchy: 'Hierarchy', assignments: 'Assignments', bulk: 'Bulk Operations', analytics: 'Analytics', settings: 'Settings', help: 'Help',
};

const settRow: React.CSSProperties = { display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)', padding: '4px 0', borderBottom: '1px solid var(--color-border-weak)' };

export default OrganizationPage;
