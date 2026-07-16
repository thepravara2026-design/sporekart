import { Badge } from '../../../../../design-system/components/display/Badge';
import { useCurriculumContext } from '../../state/CurriculumContext';
import { NODE_TYPE_ICONS, type CurriculumNode } from '../../data/curriculumMockData';

interface TreeNodeProps {
  node: CurriculumNode;
  depth: number;
}

function TreeNode({ node, depth }: TreeNodeProps) {
  const { state, toggleExpand, setSelected, duplicateNode, removeNode } = useCurriculumContext();
  const isExpanded = state.expandedIds.includes(node.id);
  const isSelected = state.selectedNodeId === node.id;
  const hasChildren = node.children.length > 0;

  return (
    <div role="treeitem" aria-expanded={hasChildren ? isExpanded : undefined} aria-selected={isSelected}>
      <div
        role="button"
        tabIndex={0}
        onClick={() => {
          setSelected(node.id);
          if (hasChildren) toggleExpand(node.id);
        }}
        onKeyDown={(e: React.KeyboardEvent) => {
          if (e.key === 'Enter' || e.key === ' ') {
            e.preventDefault();
            setSelected(node.id);
            if (hasChildren) toggleExpand(node.id);
          }
        }}
        style={{
          display: 'flex',
          alignItems: 'center',
          gap: 6,
          padding: '5px 8px',
          paddingLeft: 8 + depth * 18,
          cursor: 'pointer',
          borderRadius: 'var(--radius-sm)',
          background: isSelected ? 'var(--color-primary-100)' : 'transparent',
          marginLeft: 4,
        }}
        onMouseEnter={(e) => { if (!isSelected) e.currentTarget.style.background = 'var(--color-bg-hover)'; }}
        onMouseLeave={(e) => { if (!isSelected) e.currentTarget.style.background = 'transparent'; }}
      >
        {hasChildren ? (
          <span style={{ width: 14, fontSize: 11, color: 'var(--color-text-tertiary)' }}>{isExpanded ? '\u25BC' : '\u25B6'}</span>
        ) : (
          <span style={{ width: 14 }} />
        )}
        <span style={{ fontSize: 15 }}>{NODE_TYPE_ICONS[node.type]}</span>
        <span style={{ fontSize: 'var(--font-size-sm)', flex: 1, whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>
          {node.name}
        </span>
        <Badge variant={node.status === 'published' ? 'success' : node.status === 'draft' ? 'warning' : 'neutral'} size="sm">
          {node.status}
        </Badge>
        <span
          role="button"
          tabIndex={0}
          aria-label={`Duplicate ${node.name}`}
          onClick={(e) => { e.stopPropagation(); duplicateNode(node.id); }}
          onKeyDown={(e) => { if (e.key === 'Enter') { e.stopPropagation(); duplicateNode(node.id); } }}
          style={{ cursor: 'pointer', fontSize: 12, color: 'var(--color-text-tertiary)' }}
        >
          \u2398
        </span>
        <span
          role="button"
          tabIndex={0}
          aria-label={`Delete ${node.name}`}
          onClick={(e) => { e.stopPropagation(); removeNode(node.id); }}
          onKeyDown={(e) => { if (e.key === 'Enter') { e.stopPropagation(); removeNode(node.id); } }}
          style={{ cursor: 'pointer', fontSize: 12, color: 'var(--color-danger-500)' }}
        >
          \u2716
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

export function CurriculumTree() {
  const { state } = useCurriculumContext();
  return (
    <div role="tree" aria-label="Curriculum hierarchy" style={{ padding: 4 }}>
      {state.tree.map((node) => (
        <TreeNode key={node.id} node={node} depth={0} />
      ))}
    </div>
  );
}
