import { useMemo } from 'react';
import { Card } from '../../../../../design-system/components/composite/Card';
import { useResourceContext } from '../../state/ResourceContext';
import { FolderTree } from '../visualization/FolderTree';
import { ResourceViews } from '../visualization/ResourceViews';

export function FoldersPanel() {
  const { state, toggleFolderExpand, setSelectedFolder, openPreview, toggleFavorite, togglePin } = useResourceContext();

  const folderResources = useMemo(() => {
    if (!state.selectedFolderId) return state.resources;
    return state.resources.filter((r) => r.folderId === state.selectedFolderId);
  }, [state.resources, state.selectedFolderId]);

  return (
    <div>
      <h2 style={{ marginTop: 0 }}>Folder Organization</h2>
      <div style={{ display: 'grid', gridTemplateColumns: '260px 1fr', gap: 'var(--space-4)' }}>
        <Card variant="outlined" padding="sm">
          <div
            onClick={() => setSelectedFolder(null)}
            style={{ padding: '6px 10px', cursor: 'pointer', fontWeight: state.selectedFolderId === null ? 700 : 400, fontSize: 'var(--font-size-sm)' }}
          >
            \ud83d\udcc1 All Resources
          </div>
          <FolderTree
            nodes={state.folders}
            expandedIds={state.expandedFolderIds}
            selectedId={state.selectedFolderId}
            onToggle={toggleFolderExpand}
            onSelect={setSelectedFolder}
          />
        </Card>
        <div>
          <ResourceViews resources={folderResources} view="grid" onOpen={openPreview} onFavorite={toggleFavorite} onPin={togglePin} />
        </div>
      </div>
    </div>
  );
}
