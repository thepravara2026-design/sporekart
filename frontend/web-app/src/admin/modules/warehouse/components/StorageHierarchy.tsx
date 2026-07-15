import { memo, useMemo, useState } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import { StatusBadge } from '../../../components/status/StatusBadge';
import type { StorageNode, StorageLevel } from '../types';
import { SkeletonTable } from '../../inventory/components';

const LEVEL_ORDER: StorageLevel[] = ['warehouse', 'building', 'floor', 'zone', 'rack', 'shelf', 'bin'];
const LEVEL_ICON: Record<StorageLevel, string> = {
  warehouse: 'home', building: 'office', floor: 'layers', zone: 'grid',
  rack: 'columns', shelf: 'book-open', bin: 'package',
};
const LEVEL_LABEL: Record<StorageLevel, string> = {
  warehouse: 'Warehouse', building: 'Building', floor: 'Floor', zone: 'Zone',
  rack: 'Rack', shelf: 'Shelf', bin: 'Bin',
};

function Node({ node, childrenMap, depth }: { node: StorageNode; childrenMap: Map<string | null, StorageNode[]>; depth: number }) {
  const kids = childrenMap.get(node.id) ?? [];
  const [open, setOpen] = useState(depth < 3);
  const hasKids = kids.length > 0;
  return (
    <li style={{ listStyle: 'none' }}>
      <div
        style={{ display: 'flex', alignItems: 'center', gap: 8, padding: '8px 12px', borderRadius: 'var(--radius-md)', background: depth === 0 ? 'var(--color-primary-alpha)' : 'var(--color-surface)', marginLeft: depth * 16 }}
      >
        {hasKids ? (
          <button onClick={() => setOpen((o) => !o)} aria-label={open ? 'Collapse' : 'Expand'} aria-expanded={open} style={{ border: 'none', background: 'transparent', cursor: 'pointer', color: 'var(--color-text-secondary)' }}>
            <Icon name={open ? 'chevron-down' : 'chevron-right'} size={14} />
          </button>
        ) : <span style={{ width: 14 }} />}
        <Icon name={LEVEL_ICON[node.level]} size={16} />
        <span style={{ fontWeight: 600, color: 'var(--color-text-primary)', fontSize: 'var(--text-body)' }}>{node.code}</span>
        <span style={{ color: 'var(--color-text-secondary)', fontSize: 'var(--text-body)' }}>{node.name}</span>
        <span style={{ marginLeft: 'auto', display: 'inline-flex', alignItems: 'center', gap: 6 }}>
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', textTransform: 'uppercase', letterSpacing: '0.04em' }}>{LEVEL_LABEL[node.level]}</span>
          <StatusBadge status={node.status} variant={node.status === 'active' ? 'success' : node.status === 'maintenance' ? 'warning' : 'neutral'} />
        </span>
      </div>
      {hasKids && open && (
        <ul style={{ margin: 0, padding: 0 }}>
          {kids.map((k) => <Node key={k.id} node={k} childrenMap={childrenMap} depth={depth + 1} />)}
        </ul>
      )}
    </li>
  );
}

export const StorageHierarchy = memo(function StorageHierarchy({ nodes, loading }: { nodes: StorageNode[]; loading?: boolean }) {
  const childrenMap = useMemo(() => {
    const map = new Map<string | null, StorageNode[]>();
    nodes.forEach((n) => {
      const arr = map.get(n.parentId) ?? [];
      arr.push(n);
      map.set(n.parentId, arr);
    });
    return map;
  }, [nodes]);

  if (loading) return <SkeletonTable rows={8} />;

  const roots = childrenMap.get(null) ?? [];
  return (
    <div style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', padding: 12, background: 'var(--color-surface)' }}>
      <ul style={{ margin: 0, padding: 0 }}>
        {roots.map((r) => <Node key={r.id} node={r} childrenMap={childrenMap} depth={0} />)}
      </ul>
      <p style={{ margin: '12px 4px 0', fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
        Hierarchy: {LEVEL_ORDER.join(' → ')}
      </p>
    </div>
  );
});


