import React from 'react';
import type { OrgCategory } from '../types';
import { CategoryTree } from './CategoryTree';
import { DetailPanel } from './DetailPanel';
import type { OrganizationState } from '../state/useOrganizationState';

interface CategoryManagerProps {
  state: OrganizationState;
}

export const CategoryManager = React.memo(function CategoryManager({ state }: CategoryManagerProps) {
  const [selectedId, setSelectedId] = React.useState<string | null>(null);
  const selectedCategory = selectedId ? state.allCategories.find((c) => c.id === selectedId) ?? null : null;

  return (
    <div style={{ display: 'grid', gridTemplateColumns: '1fr 320px', gap: 'var(--space-section-gap)', alignItems: 'start' }}>
      <div style={{ minWidth: 0 }}>
        <CategoryTree
          tree={state.categoryTree}
          selectedId={selectedId}
          onSelect={setSelectedId}
          onToggleExpand={state.toggleExpand}
          onExpandAll={state.expandAll}
          onCollapseAll={state.collapseAll}
        />
      </div>
      <aside style={{ position: 'sticky', top: 'var(--space-component-gap)' }}>
        {selectedCategory ? (
          <CategoryDetailPanel category={selectedCategory} />
        ) : (
          <div style={{ padding: 'var(--space-8)', textAlign: 'center', color: 'var(--color-text-tertiary)', fontSize: 'var(--text-body-sm)', borderRadius: 'var(--radius-md)', border: '1px dashed var(--color-border-default)' }}>
            Select a category to view details
          </div>
        )}
      </aside>
    </div>
  );
});

function CategoryDetailPanel({ category }: { category: OrgCategory }) {
  const rowStyle: React.CSSProperties = {
    display: 'flex', justifyContent: 'space-between', padding: '6px 0', borderBottom: '1px solid var(--color-border-weak)', fontSize: 'var(--text-body-sm)',
  };
  const labelStyle: React.CSSProperties = { color: 'var(--color-text-secondary)', fontWeight: 'var(--weight-medium)' };

  return (
    <DetailPanel title={category.name} subtitle={`Code: ${category.code}`} icon="tag">
      <div style={rowStyle}><span style={labelStyle}>Status</span><span style={{ textTransform: 'capitalize' }}>{category.status}</span></div>
      <div style={rowStyle}><span style={labelStyle}>Slug</span><span>{category.slug}</span></div>
      <div style={rowStyle}><span style={labelStyle}>Products</span><span>{category.productCount}</span></div>
      <div style={rowStyle}><span style={labelStyle}>Display Order</span><span>{category.displayOrder}</span></div>
      <div style={rowStyle}><span style={labelStyle}>Visible</span><span>{category.isVisible ? 'Yes' : 'No'}</span></div>
      <div style={rowStyle}><span style={labelStyle}>Featured</span><span>{category.isFeatured ? 'Yes' : 'No'}</span></div>
      {category.parentId && (
        <div style={rowStyle}><span style={labelStyle}>Parent</span><span>{category.ancestors.join(' > ') || '—'}</span></div>
      )}
      <div style={rowStyle}><span style={labelStyle}>Created</span><span>{new Date(category.createdAt).toLocaleDateString()}</span></div>
      <div style={rowStyle}><span style={labelStyle}>By</span><span>{category.createdBy}</span></div>
      {category.description && (
        <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '8px 0 0', lineHeight: 'var(--leading-relaxed)' }}>
          {category.description}
        </p>
      )}
    </DetailPanel>
  );
}

export default CategoryManager;
