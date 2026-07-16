import type { ResourceFolder } from '../../data/resourceMockData';

interface FolderTreeProps {
  nodes: ResourceFolder[];
  expandedIds: string[];
  selectedId: string | null;
  onToggle: (id: string) => void;
  onSelect: (id: string | null) => void;
  depth?: number;
}

export function FolderTree({ nodes, expandedIds, selectedId, onToggle, onSelect, depth = 0 }: FolderTreeProps) {
  return (
    <ul role="tree" style={{ listStyle: 'none', margin: 0, padding: 0, paddingLeft: depth === 0 ? 0 : 16 }}>
      {nodes.map((node) => {
        const isExpanded = expandedIds.includes(node.id);
        const isSelected = selectedId === node.id;
        const hasChildren = node.children.length > 0;
        return (
          <li key={node.id} role="treeitem" aria-expanded={hasChildren ? isExpanded : undefined}>
            <div
              onClick={() => onSelect(node.id)}
              style={{
                display: 'flex',
                alignItems: 'center',
                gap: 6,
                padding: '6px 10px',
                borderRadius: 'var(--radius-sm)',
                cursor: 'pointer',
                fontSize: 'var(--font-size-sm)',
                background: isSelected ? 'var(--color-primary-100)' : 'transparent',
                color: isSelected ? 'var(--color-primary-700)' : 'var(--color-text-primary)',
              }}
            >
              {hasChildren ? (
                <button
                  onClick={(e) => { e.stopPropagation(); onToggle(node.id); }}
                  style={{ border: 'none', background: 'transparent', cursor: 'pointer', fontSize: 12 }}
                  aria-label={isExpanded ? 'Collapse' : 'Expand'}
                >
                  {isExpanded ? '\u25BC' : '\u25B6'}
                </button>
              ) : (
                <span style={{ width: 14 }} />
              )}
              <span>{isExpanded ? '\ud83d\udcc2' : '\ud83d\udcc1'}</span>
              <span>{node.name}</span>
            </div>
            {hasChildren && isExpanded && (
              <FolderTree
                nodes={node.children}
                expandedIds={expandedIds}
                selectedId={selectedId}
                onToggle={onToggle}
                onSelect={onSelect}
                depth={depth + 1}
              />
            )}
          </li>
        );
      })}
    </ul>
  );
}
