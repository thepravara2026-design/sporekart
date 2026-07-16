import { Card } from '../../../../../design-system/components/composite/Card';
import { Badge } from '../../../../../design-system/components/display/Badge';
import { Button } from '../../../../../design-system/components/core/Button';
import { useResourceContext } from '../../state/ResourceContext';
import { ResourceViews } from '../visualization/ResourceViews';

export function CollectionsPanel() {
  const { state, setSelectedCollection, openPreview, toggleFavorite, togglePin } = useResourceContext();

  const selected = state.collections.find((c) => c.id === state.selectedCollectionId);
  const collectionResources = selected
    ? state.resources.filter((r) => selected.resourceIds.includes(r.id))
    : [];

  if (selected) {
    return (
      <div>
        <div style={{ display: 'flex', alignItems: 'center', gap: 12, marginBottom: 'var(--space-3)' }}>
          <Button size="sm" variant="ghost" onClick={() => setSelectedCollection(null)}>\u2190 Back</Button>
          <h2 style={{ margin: 0 }}>{selected.icon} {selected.name}</h2>
          <Badge variant="info" size="sm">{collectionResources.length} items</Badge>
        </div>
        <p style={{ color: 'var(--color-text-tertiary)' }}>{selected.description}</p>
        <ResourceViews resources={collectionResources} view="grid" onOpen={openPreview} onFavorite={toggleFavorite} onPin={togglePin} />
      </div>
    );
  }

  return (
    <div>
      <h2 style={{ marginTop: 0 }}>Collections</h2>
      <p style={{ color: 'var(--color-text-tertiary)' }}>Curated resource bundles for structured learning journeys.</p>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(240px, 1fr))', gap: 'var(--space-3)' }}>
        {state.collections.map((c) => (
          <Card key={c.id} variant="elevated" padding="md" hoverable onClick={() => setSelectedCollection(c.id)} style={{ cursor: 'pointer' }}>
            <div style={{ fontSize: 32 }}>{c.icon}</div>
            <div style={{ fontWeight: 700, marginTop: 8 }}>{c.name}</div>
            <div style={{ fontSize: 12, color: 'var(--color-text-tertiary)', marginBottom: 8 }}>{c.description}</div>
            <Badge variant="neutral" size="sm">{c.resourceIds.length} resources</Badge>
          </Card>
        ))}
      </div>
    </div>
  );
}
