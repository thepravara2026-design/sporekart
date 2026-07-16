import { createContext, useContext, type ReactNode } from 'react';
import type {
  BuilderPanel,
  BuilderCourseInfo,
  LearningObjective,
  Prerequisite,
  MediaPlaceholder,
  ResourceAttachment,
  SeoConfig,
  CourseSettingsData,
  BuilderState,
} from '../data/builderMockData';

interface BuilderContextValue {
  state: BuilderState;
  setPanel: (panel: BuilderPanel) => void;
  setInfo: (payload: Partial<BuilderCourseInfo>) => void;
  setObjectives: (payload: LearningObjective[]) => void;
  addObjective: (payload: LearningObjective) => void;
  updateObjective: (id: string, updates: Partial<LearningObjective>) => void;
  removeObjective: (id: string) => void;
  reorderObjectives: (payload: LearningObjective[]) => void;
  setPrerequisites: (payload: Prerequisite[]) => void;
  addPrerequisite: (payload: Prerequisite) => void;
  removePrerequisite: (id: string) => void;
  setMedia: (payload: MediaPlaceholder[]) => void;
  addMedia: (payload: MediaPlaceholder) => void;
  removeMedia: (id: string) => void;
  setResources: (payload: ResourceAttachment[]) => void;
  addResource: (payload: ResourceAttachment) => void;
  removeResource: (id: string) => void;
  setSeo: (payload: Partial<SeoConfig>) => void;
  setSettings: (payload: Partial<CourseSettingsData>) => void;
  markSaved: () => void;
  setPreviewDevice: (device: 'desktop' | 'tablet' | 'mobile') => void;
  setPreviewTheme: (theme: 'light' | 'dark') => void;
  loadState: (payload: Partial<BuilderState>) => void;
}

const BuilderContext = createContext<BuilderContextValue | null>(null);

export function useBuilderContext(): BuilderContextValue {
  const ctx = useContext(BuilderContext);
  if (!ctx) throw new Error('useBuilderContext must be used within BuilderProvider');
  return ctx;
}

interface Props {
  value: BuilderContextValue;
  children: ReactNode;
}

export function BuilderProvider({ value, children }: Props) {
  return <BuilderContext.Provider value={value}>{children}</BuilderContext.Provider>;
}
