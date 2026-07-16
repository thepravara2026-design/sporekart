import { Card } from '../../../../../design-system/components/composite/Card';
import { Stack } from '../../../../../design-system/components/layout/Stack';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { Inline } from '../../../../../design-system/components/layout/Inline';
import { Button } from '../../../../../design-system/components/core/Button';
import { HierarchyTree } from './HierarchyTree';
import { TaxonomyBreadcrumbs } from './TaxonomyBreadcrumbs';
import { useTaxonomyContext } from '../../state/TaxonomyContext';
import { useState } from 'react';
import type { TaxonomyNode } from '../../data/taxonomyMockData';

function findNode(nodes: TaxonomyNode[], id: string | null): TaxonomyNode | null {
  if (!id) return null;
  for (const node of nodes) {
    if (node.id === id) return node;
    if (node.children.length) {
      const found = findNode(node.children, id);
      if (found) return found;
    }
  }
  return null;
}

export function CategoryExplorer() {
  const { state, setSelectedCategory, addCategory, removeCategory } = useTaxonomyContext();
  const [showAdd, setShowAdd] = useState(false);
  const [newName, setNewName] = useState('');

  const selected = findNode(state.tree, state.selectedCategoryId);

  const handleAdd = () => {
    if (!newName.trim()) return;
    addCategory({
      id: `cat-${Date.now()}`,
      name: newName.trim(),
      description: '',
      icon: '\uD83E\uDDEC',
      color: '#7cb342',
      status: 'draft',
      visibility: 'private',
      order: 99,
      featured: false,
      parentId: state.selectedCategoryId,
      courseCount: 0,
      tags: [],
      children: [],
    });
    setNewName('');
    setShowAdd(false);
  };

  return (
    <div style={{ display: 'grid', gridTemplateColumns: '320px 1fr', gap: 16 }}>
      <Card variant="outlined" padding="sm" style={{ maxHeight: '70vh', overflow: 'auto' }}>
        <Stack gap="sm">
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', gap: 8 }}>
            <span style={{ fontSize: 'var(--font-size-sm)', fontWeight: 600 }}>Hierarchy</span>
            <Button variant="outline" size="sm" onClick={() => setShowAdd(true)}>Add</Button>
          </div>
          <HierarchyTree />
          {showAdd && (
            <div style={{ display: 'flex', gap: 6, padding: 8, background: 'var(--color-bg-secondary)', borderRadius: 'var(--radius-sm)' }}>
              <input
                value={newName}
                onChange={(e) => setNewName(e.target.value)}
                placeholder="New subcategory"
                style={{ flex: 1, padding: '6px 8px', border: '1px solid var(--color-border-default)', borderRadius: 'var(--radius-sm)', fontSize: 'var(--font-size-sm)' }}
                aria-label="New category name"
              />
              <Button variant="primary" size="sm" onClick={handleAdd}>Add</Button>
            </div>
          )}
        </Stack>
      </Card>

      <Card variant="outlined" padding="lg">
        <Stack gap="md">
          <TaxonomyBreadcrumbs />
          {selected ? (
            <>
              <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
                <span style={{ fontSize: 40 }}>{selected.icon}</span>
                <div style={{ flex: 1 }}>
                  <div style={{ fontSize: 'var(--font-size-lg)', fontWeight: 700 }}>{selected.name}</div>
                  <div style={{ fontSize: 'var(--font-size-sm)', color: 'var(--color-text-secondary)' }}>{selected.description}</div>
                </div>
                <Badge variant="default" size="sm">{selected.visibility}</Badge>
                <Badge variant={selected.status === 'active' ? 'success' : selected.status === 'draft' ? 'warning' : 'neutral'} size="sm">{selected.status}</Badge>
              </div>
              <Inline gap="sm" wrap>
                <Badge variant="info" size="sm">{selected.courseCount} courses</Badge>
                {selected.featured && <Badge variant="warning" size="sm">Featured</Badge>}
                {selected.tags.map((t) => <Badge key={t} variant="default" size="sm">{t}</Badge>)}
              </Inline>
              <Inline gap="sm">
                <Button variant="outline" size="sm" onClick={() => setShowAdd(true)}>Add Subcategory</Button>
                <Button variant="destructive" size="sm" onClick={() => { removeCategory(selected.id); setSelectedCategory(null); }}>Delete</Button>
              </Inline>
              {selected.children.length > 0 && (
                <div style={{ fontSize: 'var(--font-size-sm)', color: 'var(--color-text-secondary)' }}>
                  {selected.children.length} subcategor{selected.children.length === 1 ? 'y' : 'ies'}
                </div>
              )}
            </>
          ) : (
            <div style={{ color: 'var(--color-text-secondary)', fontSize: 'var(--font-size-sm)' }}>
              Select a category from the hierarchy to view details, manage subcategories, or add new nodes.
            </div>
          )}
        </Stack>
      </Card>
    </div>
  );
}
