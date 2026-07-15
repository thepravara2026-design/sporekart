import React from 'react';
import { Navigate, Route, Routes } from 'react-router-dom';
import { OrganizationPage } from '../OrganizationPage';
import { CategoryTree } from '../components/CategoryTree';
import { CollectionExplorer } from '../components/CollectionExplorer';
import { MOCK_CATEGORIES, getRootCategories, getChildCategories } from '../mock/mockCategories';
import { MOCK_COLLECTIONS } from '../mock/mockCollections';
import type { CategoryNode } from '../types';

function buildTree(expanded: Set<string>): CategoryNode[] {
  function build(ids: string[]): CategoryNode[] {
    return ids.map((id) => {
      const cat = MOCK_CATEGORIES.find((c) => c.id === id)!;
      const children = getChildCategories(id);
      return {
        ...cat,
        children: children.length > 0 ? build(children.map((c) => c.id)) : [],
        depth: cat.parentId ? (MOCK_CATEGORIES.find((c) => c.id === cat.parentId)?.parentId ? 2 : 1) : 0,
        isExpanded: expanded.has(id),
      };
    });
  }
  return build(getRootCategories().map((c) => c.id));
}

const HierarchyPreview: React.FC = () => {
  const [expanded, setExpanded] = React.useState<Set<string>>(new Set(['cat-fresh', 'cat-dried']));
  const tree = React.useMemo(() => buildTree(expanded), [expanded]);
  const toggleExpand = (id: string) => setExpanded((prev) => { const n = new Set(prev); if (n.has(id)) n.delete(id); else n.add(id); return n; });

  return (
    <div style={{ padding: 'var(--space-component-gap)', maxWidth: 640 }}>
      <h2 style={{ margin: '0 0 var(--space-component-gap)', fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>
        Category Hierarchy
      </h2>
      <div style={{ borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
        <CategoryTree
          tree={tree}
          selectedId={null}
          onSelect={() => {}}
          onToggleExpand={toggleExpand}
          onExpandAll={() => setExpanded(new Set(MOCK_CATEGORIES.filter((c) => getChildCategories(c.id).length > 0).map((c) => c.id)))}
          onCollapseAll={() => setExpanded(new Set())}
        />
      </div>
    </div>
  );
};

const CollectionPreview: React.FC = () => {
  return (
    <div style={{ padding: 'var(--space-component-gap)' }}>
      <h2 style={{ margin: '0 0 var(--space-component-gap)', fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>
        Collection Explorer
      </h2>
      <CollectionExplorer collections={MOCK_COLLECTIONS} />
    </div>
  );
};

const FullPreview: React.FC = () => {
  return <OrganizationPage />;
};

export const OrganizationPreviewApp: React.FC = () => {
  return (
    <Routes>
      <Route index element={<Navigate to="workspace" replace />} />
      <Route path="workspace" element={<FullPreview />} />
      <Route path="categories" element={<FullPreview />} />
      <Route path="hierarchy" element={<HierarchyPreview />} />
      <Route path="collections" element={<CollectionPreview />} />
      <Route path="info" element={<OrganizationInfo />} />
    </Routes>
  );
};

function OrganizationInfo() {
  return (
    <div style={{ padding: 'var(--space-component-gap)', maxWidth: 720 }}>
      <h2 style={{ margin: '0 0 var(--space-component-gap)', fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>
        Product Organization — Architecture
      </h2>
      <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-md)' }}>
        <ArchCard title="Mock Mode">
          This module operates entirely in mock mode. All 30 categories, 16 collections, 11 brands, and 29 tags are statically defined.
          Bulk operations simulate confirmation without data modification.
        </ArchCard>
        <ArchCard title="Taxonomy Architecture">
          6 root categories with up to 2 levels of children (8 leaf categories per root). Categories support codes, icons, visibility,
          featured status, display order, and product counts. The tree supports expand/collapse with recursive rendering.
        </ArchCard>
        <ArchCard title="Collection Types">
          7 collection types: seasonal, featured, trending, recommended, campaign, product, administrator — each with date ranges,
          featured flags, and type-specific filtering.
        </ArchCard>
        <ArchCard title="Integration Points">
          Future: Supabase for persistence, Elasticsearch for search, AI-assisted categorization, marketplace sync, variant taxonomy mapping.
        </ArchCard>
        <ArchCard title="Preview Routes">
          <code>/preview/products/organization/workspace</code> — Full workspace<br />
          <code>/preview/products/organization/hierarchy</code> — Category tree<br />
          <code>/preview/products/organization/collections</code> — Collection explorer<br />
          <code>/preview/products/organization/info</code> — Architecture notes
        </ArchCard>
      </div>
    </div>
  );
}

function ArchCard({ title, children }: { title: string; children: React.ReactNode }) {
  return (
    <div style={{ padding: 'var(--space-4)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
      <h3 style={{ margin: '0 0 8px', fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)' }}>{title}</h3>
      <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', lineHeight: 'var(--leading-relaxed)' }}>{children}</div>
    </div>
  );
}

export default OrganizationPreviewApp;
