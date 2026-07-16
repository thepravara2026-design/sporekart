import { ResourceProvider, useResourceContext } from '../state/ResourceContext';
import { ResourceSidebar } from './shared/ResourceSidebar';
import { OverviewPanel } from './panels/OverviewPanel';
import { LibraryExplorerPanel } from './panels/LibraryExplorerPanel';
import { CollectionsPanel } from './panels/CollectionsPanel';
import { FoldersPanel } from './panels/FoldersPanel';
import { FavoritesPanel } from './panels/FavoritesPanel';
import { RecentPanel } from './panels/RecentPanel';
import { ArchivedPanel } from './panels/ArchivedPanel';
import { PreviewPanel } from './panels/PreviewPanel';
import { LinkingPanel } from './panels/LinkingPanel';
import { StoragePanel } from './panels/StoragePanel';
import type { ResourceSection } from '../data/resourceMockData';

const PANEL_COMPONENTS: Record<ResourceSection, React.FC> = {
  overview: OverviewPanel,
  library: LibraryExplorerPanel,
  collections: CollectionsPanel,
  folders: FoldersPanel,
  favorites: FavoritesPanel,
  recent: RecentPanel,
  archived: ArchivedPanel,
  preview: PreviewPanel,
  linking: LinkingPanel,
  storage: StoragePanel,
};

function PanelContent() {
  const { state } = useResourceContext();
  const PanelComponent = PANEL_COMPONENTS[state.section] || OverviewPanel;
  return <PanelComponent />;
}

function ResourceHeader() {
  return (
    <div
      style={{
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        padding: 'var(--space-2) var(--space-4)',
        borderBottom: '1px solid var(--color-border-default)',
        background: 'var(--color-bg-primary)',
      }}
    >
      <span style={{ fontSize: 'var(--font-size-lg)', fontWeight: 700 }}>Learning Resources</span>
      <span style={{ fontSize: 'var(--font-size-xs)', color: 'var(--color-text-tertiary)' }}>Digital Knowledge Repository · Mock Mode</span>
    </div>
  );
}

export function ResourceLibraryLayout() {
  return (
    <ResourceProvider>
      <div style={{ display: 'flex', flexDirection: 'column', height: '100%', overflow: 'hidden' }}>
        <ResourceHeader />
        <div style={{ display: 'flex', flex: 1, overflow: 'hidden' }}>
          <ResourceSidebar />
          <main
            style={{ flex: 1, overflowY: 'auto', padding: 'var(--space-4)', background: 'var(--color-bg-secondary)' }}
            aria-label="Resource panel content"
            role="tabpanel"
          >
            <div style={{ maxWidth: 1200, margin: '0 auto' }}>
              <PanelContent />
            </div>
          </main>
        </div>
      </div>
    </ResourceProvider>
  );
}
