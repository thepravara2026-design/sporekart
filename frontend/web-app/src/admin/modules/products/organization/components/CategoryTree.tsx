import React from 'react';
import Icon from '../../../../../design-system/icons/Icon';
import type { CategoryNode } from '../types';

interface CategoryTreeProps {
  tree: CategoryNode[];
  selectedId: string | null;
  onSelect: (id: string) => void;
  onToggleExpand: (id: string) => void;
  onExpandAll: () => void;
  onCollapseAll: () => void;
}

const rowStyle = (isSelected: boolean, depth: number): React.CSSProperties => ({
  display: 'flex', alignItems: 'center', gap: 4,
  padding: '6px 8px', paddingLeft: 12 + depth * 20, cursor: 'pointer',
  borderRadius: 'var(--radius-sm)', transition: 'background var(--duration-fast) var(--easing-standard)',
  background: isSelected ? 'var(--color-primary-alpha)' : 'transparent',
  color: isSelected ? 'var(--color-primary)' : 'var(--color-text-primary)',
  fontWeight: isSelected ? 'var(--weight-semibold)' : 'var(--weight-normal)',
  fontSize: 'var(--text-body-sm)', fontFamily: 'var(--font-family-sans)',
  border: 'none', textAlign: 'left', width: '100%',
});

const toggleBtnStyle: React.CSSProperties = {
  display: 'inline-flex', alignItems: 'center', justifyContent: 'center',
  width: 20, height: 20, border: 'none', background: 'none',
  cursor: 'pointer', borderRadius: 'var(--radius-xs)',
  flexShrink: 0, fontSize: 14, lineHeight: 1,
};

function TreeNode({ node, selectedId, onSelect, onToggleExpand }: {
  node: CategoryNode; selectedId: string | null; onSelect: (id: string) => void; onToggleExpand: (id: string) => void;
}) {
  const hasChildren = node.children.length > 0;
  return (
    <>
      <button
        type="button"
        style={rowStyle(selectedId === node.id, node.depth)}
        onClick={() => onSelect(node.id)}
        aria-current={selectedId === node.id ? 'page' : undefined}
      >
        <span
          style={toggleBtnStyle}
          onClick={(e) => { e.stopPropagation(); onToggleExpand(node.id); }}
          aria-label={node.isExpanded ? 'Collapse' : 'Expand'}
        >
          {hasChildren ? (node.isExpanded ? '▼' : '▶') : '○'}
        </span>
        <Icon name={node.icon || 'tag'} size={14} style={{ flexShrink: 0 }} />
        <span style={{ flex: 1, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>{node.name}</span>
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', marginLeft: 'auto' }}>{node.productCount}</span>
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', marginLeft: 4, textTransform: 'uppercase' }}>{node.code}</span>
      </button>
      {hasChildren && node.isExpanded && node.children.map((child) => (
        <TreeNode key={child.id} node={child} selectedId={selectedId} onSelect={onSelect} onToggleExpand={onToggleExpand} />
      ))}
    </>
  );
}

export const CategoryTree = React.memo(function CategoryTree({ tree, selectedId, onSelect, onToggleExpand, onExpandAll, onCollapseAll }: CategoryTreeProps) {
  return (
    <div>
      <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-inline-xs)', padding: '0 0 8px', borderBottom: '1px solid var(--color-border-default)', marginBottom: 8 }}>
        <button type="button" onClick={onExpandAll} style={actionBtnStyle}>Expand All</button>
        <button type="button" onClick={onCollapseAll} style={actionBtnStyle}>Collapse All</button>
        <span style={{ marginLeft: 'auto', fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{tree.length} roots</span>
      </div>
      <div role="tree" aria-label="Category hierarchy">
        {tree.map((node) => (
          <TreeNode key={node.id} node={node} selectedId={selectedId} onSelect={onSelect} onToggleExpand={onToggleExpand} />
        ))}
      </div>
    </div>
  );
});

const actionBtnStyle: React.CSSProperties = {
  padding: '4px 8px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)',
  background: 'var(--color-bg-surface-default)', cursor: 'pointer',
  fontFamily: 'var(--font-family-sans)', fontSize: 'var(--text-caption)',
  color: 'var(--color-text-secondary)',
};

export default CategoryTree;
