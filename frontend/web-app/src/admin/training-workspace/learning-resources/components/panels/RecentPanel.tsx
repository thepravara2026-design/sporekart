import { useMemo } from 'react';
import { useResourceContext } from '../../state/ResourceContext';
import { ResourceViews } from '../visualization/ResourceViews';

export function RecentPanel() {
  const { state, openPreview, toggleFavorite, togglePin } = useResourceContext();

  const recent = useMemo(
    () => [...state.resources].sort((a, b) => b.updatedDate.localeCompare(a.updatedDate)).slice(0, 8),
    [state.resources],
  );

  return (
    <div>
      <h2 style={{ marginTop: 0 }}>Recent</h2>
      <p style={{ color: 'var(--color-text-tertiary)' }}>Recently updated resources.</p>
      <ResourceViews resources={recent} view="list" onOpen={openPreview} onFavorite={toggleFavorite} onPin={togglePin} />
    </div>
  );
}
