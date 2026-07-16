import { useCallback, useReducer } from 'react';
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
import {
  MOCK_INITIAL_INFO,
  MOCK_INITIAL_SEO,
  MOCK_INITIAL_SETTINGS,
} from '../data/builderMockData';

type BuilderAction =
  | { type: 'SET_PANEL'; payload: BuilderPanel }
  | { type: 'SET_INFO'; payload: Partial<BuilderCourseInfo> }
  | { type: 'SET_OBJECTIVES'; payload: LearningObjective[] }
  | { type: 'ADD_OBJECTIVE'; payload: LearningObjective }
  | { type: 'UPDATE_OBJECTIVE'; payload: { id: string; updates: Partial<LearningObjective> } }
  | { type: 'REMOVE_OBJECTIVE'; payload: string }
  | { type: 'REORDER_OBJECTIVES'; payload: LearningObjective[] }
  | { type: 'SET_PREREQUISITES'; payload: Prerequisite[] }
  | { type: 'ADD_PREREQUISITE'; payload: Prerequisite }
  | { type: 'REMOVE_PREREQUISITE'; payload: string }
  | { type: 'SET_MEDIA'; payload: MediaPlaceholder[] }
  | { type: 'ADD_MEDIA'; payload: MediaPlaceholder }
  | { type: 'REMOVE_MEDIA'; payload: string }
  | { type: 'SET_RESOURCES'; payload: ResourceAttachment[] }
  | { type: 'ADD_RESOURCE'; payload: ResourceAttachment }
  | { type: 'REMOVE_RESOURCE'; payload: string }
  | { type: 'SET_SEO'; payload: Partial<SeoConfig> }
  | { type: 'SET_SETTINGS'; payload: Partial<CourseSettingsData> }
  | { type: 'MARK_SAVED' }
  | { type: 'MARK_CHANGED' }
  | { type: 'SET_PREVIEW_DEVICE'; payload: 'desktop' | 'tablet' | 'mobile' }
  | { type: 'SET_PREVIEW_THEME'; payload: 'light' | 'dark' }
  | { type: 'LOAD_STATE'; payload: Partial<BuilderState> };

function builderReducer(state: BuilderState, action: BuilderAction): BuilderState {
  switch (action.type) {
    case 'SET_PANEL':
      return { ...state, panel: action.payload };
    case 'SET_INFO':
      return { ...state, info: { ...state.info, ...action.payload }, unsavedChanges: true };
    case 'SET_OBJECTIVES':
      return { ...state, objectives: action.payload, unsavedChanges: true };
    case 'ADD_OBJECTIVE':
      return { ...state, objectives: [...state.objectives, action.payload], unsavedChanges: true };
    case 'UPDATE_OBJECTIVE':
      return {
        ...state,
        objectives: state.objectives.map((o) =>
          o.id === action.payload.id ? { ...o, ...action.payload.updates } : o
        ),
        unsavedChanges: true,
      };
    case 'REMOVE_OBJECTIVE':
      return {
        ...state,
        objectives: state.objectives.filter((o) => o.id !== action.payload),
        unsavedChanges: true,
      };
    case 'REORDER_OBJECTIVES':
      return { ...state, objectives: action.payload, unsavedChanges: true };
    case 'SET_PREREQUISITES':
      return { ...state, prerequisites: action.payload, unsavedChanges: true };
    case 'ADD_PREREQUISITE':
      return { ...state, prerequisites: [...state.prerequisites, action.payload], unsavedChanges: true };
    case 'REMOVE_PREREQUISITE':
      return {
        ...state,
        prerequisites: state.prerequisites.filter((p) => p.id !== action.payload),
        unsavedChanges: true,
      };
    case 'SET_MEDIA':
      return { ...state, mediaPlaceholders: action.payload, unsavedChanges: true };
    case 'ADD_MEDIA':
      return {
        ...state,
        mediaPlaceholders: [...state.mediaPlaceholders, action.payload],
        unsavedChanges: true,
      };
    case 'REMOVE_MEDIA':
      return {
        ...state,
        mediaPlaceholders: state.mediaPlaceholders.filter((m) => m.id !== action.payload),
        unsavedChanges: true,
      };
    case 'SET_RESOURCES':
      return { ...state, resources: action.payload, unsavedChanges: true };
    case 'ADD_RESOURCE':
      return { ...state, resources: [...state.resources, action.payload], unsavedChanges: true };
    case 'REMOVE_RESOURCE':
      return {
        ...state,
        resources: state.resources.filter((r) => r.id !== action.payload),
        unsavedChanges: true,
      };
    case 'SET_SEO':
      return { ...state, seo: { ...state.seo, ...action.payload }, unsavedChanges: true };
    case 'SET_SETTINGS':
      return { ...state, settings: { ...state.settings, ...action.payload }, unsavedChanges: true };
    case 'MARK_SAVED':
      return { ...state, unsavedChanges: false, lastSavedAt: new Date().toISOString(), versionNumber: state.versionNumber + 1 };
    case 'MARK_CHANGED':
      return { ...state, unsavedChanges: true };
    case 'SET_PREVIEW_DEVICE':
      return { ...state, previewDevice: action.payload };
    case 'SET_PREVIEW_THEME':
      return { ...state, previewTheme: action.payload };
    case 'LOAD_STATE':
      return { ...state, ...action.payload, unsavedChanges: false };
    default:
      return state;
  }
}

const INITIAL_STATE: BuilderState = {
  panel: 'overview',
  info: MOCK_INITIAL_INFO,
  objectives: [],
  prerequisites: [],
  mediaPlaceholders: [],
  resources: [],
  seo: MOCK_INITIAL_SEO,
  settings: MOCK_INITIAL_SETTINGS,
  unsavedChanges: false,
  lastSavedAt: null,
  versionNumber: 1,
  previewDevice: 'desktop',
  previewTheme: 'light',
};

export function useBuilderState() {
  const [state, dispatch] = useReducer(builderReducer, INITIAL_STATE);

  const setPanel = useCallback((panel: BuilderPanel) => {
    dispatch({ type: 'SET_PANEL', payload: panel });
  }, []);

  const setInfo = useCallback((payload: Partial<BuilderCourseInfo>) => {
    dispatch({ type: 'SET_INFO', payload });
  }, []);

  const setObjectives = useCallback((payload: LearningObjective[]) => {
    dispatch({ type: 'SET_OBJECTIVES', payload });
  }, []);

  const addObjective = useCallback((payload: LearningObjective) => {
    dispatch({ type: 'ADD_OBJECTIVE', payload });
  }, []);

  const updateObjective = useCallback((id: string, updates: Partial<LearningObjective>) => {
    dispatch({ type: 'UPDATE_OBJECTIVE', payload: { id, updates } });
  }, []);

  const removeObjective = useCallback((id: string) => {
    dispatch({ type: 'REMOVE_OBJECTIVE', payload: id });
  }, []);

  const reorderObjectives = useCallback((payload: LearningObjective[]) => {
    dispatch({ type: 'REORDER_OBJECTIVES', payload });
  }, []);

  const setPrerequisites = useCallback((payload: Prerequisite[]) => {
    dispatch({ type: 'SET_PREREQUISITES', payload });
  }, []);

  const addPrerequisite = useCallback((payload: Prerequisite) => {
    dispatch({ type: 'ADD_PREREQUISITE', payload });
  }, []);

  const removePrerequisite = useCallback((id: string) => {
    dispatch({ type: 'REMOVE_PREREQUISITE', payload: id });
  }, []);

  const setMedia = useCallback((payload: MediaPlaceholder[]) => {
    dispatch({ type: 'SET_MEDIA', payload });
  }, []);

  const addMedia = useCallback((payload: MediaPlaceholder) => {
    dispatch({ type: 'ADD_MEDIA', payload });
  }, []);

  const removeMedia = useCallback((id: string) => {
    dispatch({ type: 'REMOVE_MEDIA', payload: id });
  }, []);

  const setResources = useCallback((payload: ResourceAttachment[]) => {
    dispatch({ type: 'SET_RESOURCES', payload });
  }, []);

  const addResource = useCallback((payload: ResourceAttachment) => {
    dispatch({ type: 'ADD_RESOURCE', payload });
  }, []);

  const removeResource = useCallback((id: string) => {
    dispatch({ type: 'REMOVE_RESOURCE', payload: id });
  }, []);

  const setSeo = useCallback((payload: Partial<SeoConfig>) => {
    dispatch({ type: 'SET_SEO', payload });
  }, []);

  const setSettings = useCallback((payload: Partial<CourseSettingsData>) => {
    dispatch({ type: 'SET_SETTINGS', payload });
  }, []);

  const markSaved = useCallback(() => {
    dispatch({ type: 'MARK_SAVED' });
  }, []);

  const setPreviewDevice = useCallback((device: 'desktop' | 'tablet' | 'mobile') => {
    dispatch({ type: 'SET_PREVIEW_DEVICE', payload: device });
  }, []);

  const setPreviewTheme = useCallback((theme: 'light' | 'dark') => {
    dispatch({ type: 'SET_PREVIEW_THEME', payload: theme });
  }, []);

  const loadState = useCallback((payload: Partial<BuilderState>) => {
    dispatch({ type: 'LOAD_STATE', payload });
  }, []);

  return {
    state,
    setPanel,
    setInfo,
    setObjectives,
    addObjective,
    updateObjective,
    removeObjective,
    reorderObjectives,
    setPrerequisites,
    addPrerequisite,
    removePrerequisite,
    setMedia,
    addMedia,
    removeMedia,
    setResources,
    addResource,
    removeResource,
    setSeo,
    setSettings,
    markSaved,
    setPreviewDevice,
    setPreviewTheme,
    loadState,
  };
}
