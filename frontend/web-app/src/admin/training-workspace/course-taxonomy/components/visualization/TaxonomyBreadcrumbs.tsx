import { useTaxonomyContext } from '../../state/TaxonomyContext';
import type { TaxonomyNode } from '../../data/taxonomyMockData';

function findPath(nodes: TaxonomyNode[], id: string, path: TaxonomyNode[] = []): TaxonomyNode[] | null {
  for (const node of nodes) {
    const current = [...path, node];
    if (node.id === id) return current;
    if (node.children.length) {
      const found = findPath(node.children, id, current);
      if (found) return found;
    }
  }
  return null;
}

export function TaxonomyBreadcrumbs() {
  const { state, setSelectedCategory } = useTaxonomyContext();

  if (!state.selectedCategoryId) {
    return (
      <nav aria-label="Breadcrumb" style={{ fontSize: 'var(--font-size-sm)', color: 'var(--color-text-tertiary)' }}>
        Select a category to view its path
      </nav>
    );
  }

  const path = findPath(state.tree, state.selectedCategoryId);

  if (!path) return null;

  return (
    <nav aria-label="Breadcrumb" style={{ display: 'flex', alignItems: 'center', gap: 6, flexWrap: 'wrap' }}>
      {path.map((node, index) => (
        <span key={node.id} style={{ display: 'flex', alignItems: 'center', gap: 6 }}>
          {index > 0 && <span style={{ color: 'var(--color-text-tertiary)' }}>/</span>}
          <button
            onClick={() => setSelectedCategory(node.id)}
            style={{
              border: 'none',
              background: 'none',
              cursor: 'pointer',
              fontSize: 'var(--font-size-sm)',
              color: index === path.length - 1 ? 'var(--color-primary-700)' : 'var(--color-text-secondary)',
              fontWeight: index === path.length - 1 ? 600 : 400,
              padding: 0,
            }}
          >
            {node.icon} {node.name}
          </button>
        </span>
      ))}
    </nav>
  );
}
