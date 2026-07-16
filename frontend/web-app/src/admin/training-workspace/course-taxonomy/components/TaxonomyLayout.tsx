import { useMemo } from 'react';
import { TaxonomyProvider } from '../state/TaxonomyContext';
import { useTaxonomyState } from '../state/useTaxonomyState';
import { TaxonomySidebar } from './shared/TaxonomySidebar';
import { OverviewPanel } from './panels/OverviewPanel';
import { CategoryTreePanel } from './panels/CategoryTreePanel';
import { TagsPanel } from './panels/TagsPanel';
import { TopicsPanel } from './panels/TopicsPanel';
import { SkillsPanel } from './panels/SkillsPanel';
import { CompetenciesPanel } from './panels/CompetenciesPanel';
import { LanguagesPanel } from './panels/LanguagesPanel';
import { DeliveryPanel } from './panels/DeliveryPanel';
import { HierarchyExplorerPanel } from './panels/HierarchyExplorerPanel';
import { RelationshipsPanel } from './panels/RelationshipsPanel';
import { DiscoveryPanel } from './panels/DiscoveryPanel';
import { LearningPathsPanel } from './panels/LearningPathsPanel';

const PANEL_COMPONENTS: Record<string, React.FC> = {
  overview: OverviewPanel,
  categories: CategoryTreePanel,
  tags: TagsPanel,
  topics: TopicsPanel,
  skills: SkillsPanel,
  competencies: CompetenciesPanel,
  languages: LanguagesPanel,
  delivery: DeliveryPanel,
  explorer: HierarchyExplorerPanel,
  relationships: RelationshipsPanel,
  discovery: DiscoveryPanel,
  'learning-paths': LearningPathsPanel,
};

function PanelContent() {
  const PanelComponent = PANEL_COMPONENTS[useTaxonomyState().state.section] || OverviewPanel;
  return <PanelComponent />;
}

function TaxonomyHeader() {
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
      <span style={{ fontSize: 'var(--font-size-lg)', fontWeight: 700 }}>Course Taxonomy & Classification</span>
      <span style={{ fontSize: 'var(--font-size-xs)', color: 'var(--color-text-tertiary)' }}>Knowledge Information Architecture</span>
    </div>
  );
}

export function TaxonomyLayout() {
  const taxonomyState = useTaxonomyState();

  const contextValue = useMemo(
    () => ({
      state: taxonomyState.state,
      setSection: taxonomyState.setSection,
      setTree: taxonomyState.setTree,
      addCategory: taxonomyState.addCategory,
      updateCategory: taxonomyState.updateCategory,
      removeCategory: taxonomyState.removeCategory,
      toggleExpand: taxonomyState.toggleExpand,
      setSelectedCategory: taxonomyState.setSelectedCategory,
      setTags: taxonomyState.setTags,
      addTag: taxonomyState.addTag,
      removeTag: taxonomyState.removeTag,
      setTopics: taxonomyState.setTopics,
      addTopic: taxonomyState.addTopic,
      removeTopic: taxonomyState.removeTopic,
      setSearch: taxonomyState.setSearch,
      setFilter: taxonomyState.setFilter,
      setSelectedTopic: taxonomyState.setSelectedTopic,
    }),
    [taxonomyState]
  );

  return (
    <TaxonomyProvider value={contextValue}>
      <div style={{ display: 'flex', flexDirection: 'column', height: '100%', overflow: 'hidden' }}>
        <TaxonomyHeader />
        <div style={{ display: 'flex', flex: 1, overflow: 'hidden' }}>
          <TaxonomySidebar />
          <main
            style={{ flex: 1, overflowY: 'auto', padding: 'var(--space-4)', background: 'var(--color-bg-secondary)' }}
            aria-label="Taxonomy panel content"
            role="tabpanel"
          >
            <div style={{ maxWidth: 1100, margin: '0 auto' }}>
              <PanelContent />
            </div>
          </main>
        </div>
      </div>
    </TaxonomyProvider>
  );
}
