import { Badge } from '../../../../../design-system/components/display/Badge';
import { useTaxonomyContext } from '../../state/TaxonomyContext';
import type { TaxonomyNode } from '../../data/taxonomyMockData';

interface TreeNodeProps {
  node: TaxonomyNode;
  depth: number;
}

function TreeNode({ node, depth }: TreeNodeProps) {
  const { state, toggleExpand, setSelectedCategory } = useTaxonomyContext();
  const isExpanded = state.expandedIds.includes(node.id);
  const isSelected = state.selectedCategoryId === node.id;
  const hasChildren = node.children.length > 0;

  return (
    <div role="treeitem" aria-expanded={hasChildren ? isExpanded : undefined} aria-selected={isSelected}>
      <div
        role="button"
        tabIndex={0}
        onClick={() => {
          setSelectedCategory(node.id);
          if (hasChildren) toggleExpand(node.id);
        }}
        onKeyDown={(e: React.KeyboardEvent) => {
          if (e.key === 'Enter' || e.key === ' ') {
            e.preventDefault();
            setSelectedCategory(node.id);
            if (hasChildren) toggleExpand(node.id);
          }
        }}
        style={{
          display: 'flex',
          alignItems: 'center',
          gap: 8,
          padding: '6px 8px',
          paddingLeft: 8 + depth * 20,
          cursor: 'pointer',
          borderRadius: 'var(--radius-sm)',
          background: isSelected ? 'var(--color-primary-100)' : 'transparent',
          borderLeft: `3px solid ${node.color}`,
          marginLeft: 4,
        }}
        onMouseEnter={(e) => { if (!isSelected) e.currentTarget.style.background = 'var(--color-bg-hover)'; }}
        onMouseLeave={(e) => { if (!isSelected) e.currentTarget.style.background = 'transparent'; }}
      >
        {hasChildren ? (
          <span style={{ width: 16, fontSize: 12, color: 'var(--color-text-tertiary)' }}>
            {isExpanded ? '\u25BC' : '\u25B6'}
          </span>
        ) : (
          <span style={{ width: 16 }} />
        )}
        <span style={{ fontSize: 18 }}>{node.icon}</span>
        <span style={{ fontSize: 'var(--font-size-sm)', flex: 1 }}>{node.name}</span>
        <Badge variant={node.status === 'active' ? 'success' : node.status === 'draft' ? 'warning' : 'neutral'} size="sm">
          {node.status}
        </Badge>
        <span style={{ fontSize: 'var(--font-size-xs)', color: 'var(--color-text-tertiary)' }}>
          {node.courseCount}
        </span>
      </div>
      {isExpanded && hasChildren && (
        <div role="group">
          {node.children.map((child) => (
            <TreeNode key={child.id} node={child} depth={depth + 1} />
          ))}
        </div>
      )}
    </div>
  );
}

export function HierarchyTree() {
  const { state } = useTaxonomyContext();
  return (
    <div role="tree" aria-label="Category hierarchy" style={{ padding: 4 }}>
      {state.tree.map((node) => (
        <TreeNode key={node.id} node={node} depth={0} />
      ))}
    </div>
  );
}
