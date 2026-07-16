import { useMemo } from 'react';
import { BuilderProvider } from '../state/BuilderContext';
import { useBuilderState } from '../state/useBuilderState';
import { BuilderSidebar } from './shared/BuilderSidebar';
import { SaveIndicator } from './shared/SaveIndicator';
import { OverviewPanel } from './panels/OverviewPanel';
import { CourseInfoPanel } from './panels/CourseInfoPanel';
import { LearningObjectivesPanel } from './panels/LearningObjectivesPanel';
import { PrerequisitesPanel } from './panels/PrerequisitesPanel';
import { MediaPlaceholdersPanel } from './panels/MediaPlaceholdersPanel';
import { ResourcesPanel } from './panels/ResourcesPanel';
import { SeoPanel } from './panels/SeoPanel';
import { SettingsPanel } from './panels/SettingsPanel';
import { LivePreview } from './preview/LivePreview';
import { CurriculumStructurePanel } from './panels/CurriculumStructurePanel';
import { PublishingPanel } from './panels/PublishingPanel';

const PANEL_COMPONENTS: Record<string, React.FC> = {
  overview: OverviewPanel,
  information: CourseInfoPanel,
  curriculum: CurriculumStructurePanel,
  objectives: LearningObjectivesPanel,
  prerequisites: PrerequisitesPanel,
  media: MediaPlaceholdersPanel,
  resources: ResourcesPanel,
  seo: SeoPanel,
  settings: SettingsPanel,
  preview: LivePreview,
  publishing: PublishingPanel,
};

function PanelContent() {
  const PanelComponent = PANEL_COMPONENTS[useBuilderState().state.panel] || OverviewPanel;
  return <PanelComponent />;
}

function BuilderHeader() {
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
      <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
        <span style={{ fontSize: 'var(--font-size-lg)', fontWeight: 700 }}>
          Course Builder
        </span>
      </div>
      <SaveIndicator />
    </div>
  );
}

function BuilderContent() {
  return (
    <div style={{ display: 'flex', flex: 1, overflow: 'hidden' }}>
      <BuilderSidebar />
      <main
        style={{
          flex: 1,
          overflowY: 'auto',
          padding: 'var(--space-4)',
          background: 'var(--color-bg-secondary)',
        }}
        aria-label="Builder panel content"
        role="tabpanel"
      >
        <div style={{ maxWidth: 1000, margin: '0 auto' }}>
          <PanelContent />
        </div>
      </main>
    </div>
  );
}

export function CourseBuilderLayout() {
  const builderState = useBuilderState();

  const contextValue = useMemo(
    () => ({
      state: builderState.state,
      setPanel: builderState.setPanel,
      setInfo: builderState.setInfo,
      setObjectives: builderState.setObjectives,
      addObjective: builderState.addObjective,
      updateObjective: builderState.updateObjective,
      removeObjective: builderState.removeObjective,
      reorderObjectives: builderState.reorderObjectives,
      setPrerequisites: builderState.setPrerequisites,
      addPrerequisite: builderState.addPrerequisite,
      removePrerequisite: builderState.removePrerequisite,
      setMedia: builderState.setMedia,
      addMedia: builderState.addMedia,
      removeMedia: builderState.removeMedia,
      setResources: builderState.setResources,
      addResource: builderState.addResource,
      removeResource: builderState.removeResource,
      setSeo: builderState.setSeo,
      setSettings: builderState.setSettings,
      markSaved: builderState.markSaved,
      setPreviewDevice: builderState.setPreviewDevice,
      setPreviewTheme: builderState.setPreviewTheme,
      loadState: builderState.loadState,
    }),
    [builderState]
  );

  return (
    <BuilderProvider value={contextValue}>
      <div
        style={{
          display: 'flex',
          flexDirection: 'column',
          height: '100%',
          overflow: 'hidden',
        }}
      >
        <BuilderHeader />
        <BuilderContent />
      </div>
    </BuilderProvider>
  );
}
