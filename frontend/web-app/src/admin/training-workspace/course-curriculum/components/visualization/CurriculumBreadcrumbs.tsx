import { useCurriculumContext } from '../../state/CurriculumContext';
import { NODE_TYPE_ICONS, type CurriculumNode } from '../../data/curriculumMockData';

function findPath(nodes: CurriculumNode[], id: string, path: CurriculumNode[] = []): CurriculumNode[] | null {
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

export function CurriculumBreadcrumbs() {
  const { state, setSelected } = useCurriculumContext();

  if (!state.selectedNodeId) {
    return (
      <nav aria-label="Breadcrumb" style={{ fontSize: 'var(--font-size-sm)', color: 'var(--color-text-tertiary)' }}>
        Select a node to view its path
      </nav>
    );
  }

  const path = findPath(state.tree, state.selectedNodeId);
  if (!path) return null;

  return (
    <nav aria-label="Breadcrumb" style={{ display: 'flex', alignItems: 'center', gap: 6, flexWrap: 'wrap' }}>
      {path.map((node, index) => (
        <span key={node.id} style={{ display: 'flex', alignItems: 'center', gap: 6 }}>
          {index > 0 && <span style={{ color: 'var(--color-text-tertiary)' }}>/</span>}
          <button
            onClick={() => setSelected(node.id)}
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
            {NODE_TYPE_ICONS[node.type]} {node.name}
          </button>
        </span>
      ))}
    </nav>
  );
}
