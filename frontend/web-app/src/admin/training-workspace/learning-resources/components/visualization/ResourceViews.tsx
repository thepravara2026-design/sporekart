import { ResourceCard } from './ResourceCard';
import type { ResourceItem, ResourceView } from '../../data/resourceMockData';

interface ResourceViewsProps {
  resources: ResourceItem[];
  view: ResourceView;
  onOpen?: (id: string) => void;
  onFavorite?: (id: string) => void;
  onPin?: (id: string) => void;
}

export function ResourceViews({ resources, view, onOpen, onFavorite, onPin }: ResourceViewsProps) {
  if (resources.length === 0) {
    return (
      <div style={{ padding: 'var(--space-6)', textAlign: 'center', color: 'var(--color-text-tertiary)' }}>
        No resources match the current filters.
      </div>
    );
  }

  if (view === 'grid') {
    return (
      <div
        style={{
          display: 'grid',
          gridTemplateColumns: 'repeat(auto-fill, minmax(240px, 1fr))',
          gap: 'var(--space-3)',
        }}
      >
        {resources.map((r) => (
          <ResourceCard key={r.id} resource={r} variant="grid" onOpen={onOpen} onFavorite={onFavorite} onPin={onPin} />
        ))}
      </div>
    );
  }

  if (view === 'list') {
    return (
      <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-2)' }}>
        {resources.map((r) => (
          <ResourceCard key={r.id} resource={r} variant="list" onOpen={onOpen} onFavorite={onFavorite} onPin={onPin} />
        ))}
      </div>
    );
  }

  if (view === 'compact') {
    return (
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))', gap: 'var(--space-2)' }}>
        {resources.map((r) => (
          <ResourceCard key={r.id} resource={r} variant="compact" onOpen={onOpen} onFavorite={onFavorite} onPin={onPin} />
        ))}
      </div>
    );
  }

  if (view === 'table') {
    return (
      <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 'var(--font-size-sm)' }}>
        <thead>
          <tr style={{ textAlign: 'left', color: 'var(--color-text-tertiary)', borderBottom: '1px solid var(--color-border-default)' }}>
            <th style={{ padding: '8px 12px' }}>Name</th>
            <th style={{ padding: '8px 12px' }}>Type</th>
            <th style={{ padding: '8px 12px' }}>Category</th>
            <th style={{ padding: '8px 12px' }}>Status</th>
            <th style={{ padding: '8px 12px' }}>Version</th>
            <th style={{ padding: '8px 12px' }}>Uses</th>
          </tr>
        </thead>
        <tbody>
          {resources.map((r) => (
            <tr key={r.id} style={{ borderBottom: '1px solid var(--color-border-default)', cursor: 'pointer' }} onClick={() => onOpen?.(r.id)}>
              <td style={{ padding: '8px 12px', fontWeight: 600 }}>{r.name}</td>
              <td style={{ padding: '8px 12px' }}>{r.type}</td>
              <td style={{ padding: '8px 12px' }}>{r.category}</td>
              <td style={{ padding: '8px 12px' }}>{r.status}</td>
              <td style={{ padding: '8px 12px' }}>v{r.version}</td>
              <td style={{ padding: '8px 12px' }}>{r.usageCount}</td>
            </tr>
          ))}
        </tbody>
      </table>
    );
  }

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-2)' }}>
      {resources.map((r) => (
        <ResourceCard key={r.id} resource={r} variant="list" onOpen={onOpen} onFavorite={onFavorite} onPin={onPin} />
      ))}
    </div>
  );
}
