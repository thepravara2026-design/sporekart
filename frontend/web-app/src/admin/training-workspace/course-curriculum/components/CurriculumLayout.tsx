import { useMemo } from 'react';
import { CurriculumProvider } from '../state/CurriculumContext';
import { useCurriculumState } from '../state/useCurriculumState';
import { CurriculumSidebar } from './shared/CurriculumSidebar';
import { OverviewPanel } from './panels/OverviewPanel';
import { CurriculumBuilderPanel } from './panels/CurriculumBuilderPanel';
import { ModuleEditorPanel } from './panels/ModuleEditorPanel';
import { LessonOverviewPanel } from './panels/LessonOverviewPanel';
import { TopicOrgPanel } from './panels/TopicOrgPanel';
import { ActivitiesPanel } from './panels/ActivitiesPanel';
import { TemplatesPanel } from './panels/TemplatesPanel';
import { LearningPathsPanel } from './panels/LearningPathsPanel';
import { CompletionRulesPanel } from './panels/CompletionRulesPanel';
import { ResourceMappingPanel } from './panels/ResourceMappingPanel';
import { DragDropPanel } from './panels/DragDropPanel';
import { PreviewPanel } from './panels/PreviewPanel';

const PANEL_COMPONENTS: Record<string, React.FC> = {
  overview: OverviewPanel,
  builder: CurriculumBuilderPanel,
  module: ModuleEditorPanel,
  lesson: LessonOverviewPanel,
  topic: TopicOrgPanel,
  activities: ActivitiesPanel,
  templates: TemplatesPanel,
  'learning-paths': LearningPathsPanel,
  completion: CompletionRulesPanel,
  resources: ResourceMappingPanel,
  dragdrop: DragDropPanel,
  preview: PreviewPanel,
};

function PanelContent() {
  const PanelComponent = PANEL_COMPONENTS[useCurriculumState().state.section] || OverviewPanel;
  return <PanelComponent />;
}

function CurriculumHeader() {
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
      <span style={{ fontSize: 'var(--font-size-lg)', fontWeight: 700 }}>Curriculum Builder</span>
      <span style={{ fontSize: 'var(--font-size-xs)', color: 'var(--color-text-tertiary)' }}>Academic Content Architecture</span>
    </div>
  );
}

export function CurriculumLayout() {
  const curriculumState = useCurriculumState();

  const contextValue = useMemo(
    () => ({
      state: curriculumState.state,
      setSection: curriculumState.setSection,
      setTree: curriculumState.setTree,
      addNode: curriculumState.addNode,
      updateNode: curriculumState.updateNode,
      removeNode: curriculumState.removeNode,
      duplicateNode: curriculumState.duplicateNode,
      toggleExpand: curriculumState.toggleExpand,
      setSelected: curriculumState.setSelected,
      setTemplates: curriculumState.setTemplates,
      addTemplate: curriculumState.addTemplate,
      setPreviewDevice: curriculumState.setPreviewDevice,
      setPreviewRole: curriculumState.setPreviewRole,
      setSearch: curriculumState.setSearch,
      setFilter: curriculumState.setFilter,
      setSelectedTemplate: curriculumState.setSelectedTemplate,
    }),
    [curriculumState]
  );

  return (
    <CurriculumProvider value={contextValue}>
      <div style={{ display: 'flex', flexDirection: 'column', height: '100%', overflow: 'hidden' }}>
        <CurriculumHeader />
        <div style={{ display: 'flex', flex: 1, overflow: 'hidden' }}>
          <CurriculumSidebar />
          <main
            style={{ flex: 1, overflowY: 'auto', padding: 'var(--space-4)', background: 'var(--color-bg-secondary)' }}
            aria-label="Curriculum panel content"
            role="tabpanel"
          >
            <div style={{ maxWidth: 1100, margin: '0 auto' }}>
              <PanelContent />
            </div>
          </main>
        </div>
      </div>
    </CurriculumProvider>
  );
}
