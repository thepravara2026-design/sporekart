import { useResourceContext } from '../../state/ResourceContext';
import { ResourceViews } from '../visualization/ResourceViews';

export function FavoritesPanel() {
  const { state, openPreview, toggleFavorite, togglePin } = useResourceContext();
  const favorites = state.resources.filter((r) => r.favorite);

  return (
    <div>
      <h2 style={{ marginTop: 0 }}>Favorites</h2>
      <p style={{ color: 'var(--color-text-tertiary)' }}>Starred learning resources for quick access ({favorites.length}).</p>
      <ResourceViews resources={favorites} view="grid" onOpen={openPreview} onFavorite={toggleFavorite} onPin={togglePin} />
    </div>
  );
}
