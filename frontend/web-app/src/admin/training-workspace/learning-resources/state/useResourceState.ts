import { useCallback, useReducer } from 'react';
import type {
  ResourceSection,
  ResourceView,
  ResourceItem,
  ResourceFolder,
  ResourceCollection,
  ResourceFilters,
} from '../data/resourceMockData';
import {
  MOCK_RESOURCES,
  MOCK_FOLDERS,
  MOCK_COLLECTIONS,
} from '../data/resourceMockData';

interface ResourceState {
  section: ResourceSection;
  resources: ResourceItem[];
  folders: ResourceFolder[];
  collections: ResourceCollection[];
  expandedFolderIds: string[];
  selectedFolderId: string | null;
  selectedResourceId: string | null;
  view: ResourceView;
  search: string;
  filters: ResourceFilters;
  selectedCollectionId: string | null;
  previewResourceId: string | null;
}

type ResourceAction =
  | { type: 'SET_SECTION'; payload: ResourceSection }
  | { type: 'SET_RESOURCES'; payload: ResourceItem[] }
  | { type: 'ADD_RESOURCE'; payload: ResourceItem }
  | { type: 'UPDATE_RESOURCE'; payload: { id: string; updates: Partial<ResourceItem> } }
  | { type: 'REMOVE_RESOURCE'; payload: string }
  | { type: 'DUPLICATE_RESOURCE'; payload: string }
  | { type: 'TOGGLE_FAVORITE'; payload: string }
  | { type: 'TOGGLE_PIN'; payload: string }
  | { type: 'TOGGLE_STARRED'; payload: string }
  | { type: 'ARCHIVE_RESOURCE'; payload: string }
  | { type: 'RESTORE_RESOURCE'; payload: string }
  | { type: 'SET_FOLDERS'; payload: ResourceFolder[] }
  | { type: 'ADD_FOLDER'; payload: ResourceFolder }
  | { type: 'TOGGLE_FOLDER_EXPAND'; payload: string }
  | { type: 'SET_SELECTED_FOLDER'; payload: string | null }
  | { type: 'SET_SELECTED_RESOURCE'; payload: string | null }
  | { type: 'SET_VIEW'; payload: ResourceView }
  | { type: 'SET_SEARCH'; payload: string }
  | { type: 'SET_FILTERS'; payload: Partial<ResourceFilters> }
  | { type: 'SET_SELECTED_COLLECTION'; payload: string | null }
  | { type: 'SET_PREVIEW_RESOURCE'; payload: string | null };

function updateResource(resources: ResourceItem[], id: string, updates: Partial<ResourceItem>): ResourceItem[] {
  return resources.map((r) => (r.id === id ? { ...r, ...updates } : r));
}

function addFolderToTree(nodes: ResourceFolder[], parentId: string | null, folder: ResourceFolder): ResourceFolder[] {
  if (parentId === null) return [...nodes, folder];
  return nodes.map((n) => {
    if (n.id === parentId) return { ...n, children: [...n.children, folder] };
    if (n.children.length) return { ...n, children: addFolderToTree(n.children, parentId, folder) };
    return n;
  });
}

const INITIAL_FILTERS: ResourceFilters = {
  type: 'all',
  category: 'all',
  department: 'all',
  language: 'all',
  status: 'all',
  author: 'all',
  visibility: 'all',
};

const INITIAL_STATE: ResourceState = {
  section: 'overview',
  resources: MOCK_RESOURCES,
  folders: MOCK_FOLDERS,
  collections: MOCK_COLLECTIONS,
  expandedFolderIds: ['f-cult', 'f-lab'],
  selectedFolderId: null,
  selectedResourceId: null,
  view: 'grid',
  search: '',
  filters: INITIAL_FILTERS,
  selectedCollectionId: null,
  previewResourceId: null,
};

function resourceReducer(state: ResourceState, action: ResourceAction): ResourceState {
  switch (action.type) {
    case 'SET_SECTION':
      return { ...state, section: action.payload };
    case 'SET_RESOURCES':
      return { ...state, resources: action.payload };
    case 'ADD_RESOURCE':
      return { ...state, resources: [action.payload, ...state.resources] };
    case 'UPDATE_RESOURCE':
      return { ...state, resources: updateResource(state.resources, action.payload.id, action.payload.updates) };
    case 'REMOVE_RESOURCE':
      return { ...state, resources: state.resources.filter((r) => r.id !== action.payload) };
    case 'DUPLICATE_RESOURCE': {
      const orig = state.resources.find((r) => r.id === action.payload);
      if (!orig) return state;
      const copy: ResourceItem = { ...orig, id: `r-${Date.now()}`, name: `${orig.name} (copy)`, usageCount: 0 };
      return { ...state, resources: [copy, ...state.resources] };
    }
    case 'TOGGLE_FAVORITE':
      return { ...state, resources: updateResource(state.resources, action.payload, { favorite: !state.resources.find((r) => r.id === action.payload)?.favorite }) };
    case 'TOGGLE_PIN':
      return { ...state, resources: updateResource(state.resources, action.payload, { pinned: !state.resources.find((r) => r.id === action.payload)?.pinned }) };
    case 'TOGGLE_STARRED':
      return { ...state, resources: updateResource(state.resources, action.payload, { starred: !state.resources.find((r) => r.id === action.payload)?.starred }) };
    case 'ARCHIVE_RESOURCE':
      return { ...state, resources: updateResource(state.resources, action.payload, { status: 'archived' }) };
    case 'RESTORE_RESOURCE':
      return { ...state, resources: updateResource(state.resources, action.payload, { status: 'active' }) };
    case 'SET_FOLDERS':
      return { ...state, folders: action.payload };
    case 'ADD_FOLDER':
      return { ...state, folders: addFolderToTree(state.folders, action.payload.parentId, action.payload) };
    case 'TOGGLE_FOLDER_EXPAND':
      return {
        ...state,
        expandedFolderIds: state.expandedFolderIds.includes(action.payload)
          ? state.expandedFolderIds.filter((id) => id !== action.payload)
          : [...state.expandedFolderIds, action.payload],
      };
    case 'SET_SELECTED_FOLDER':
      return { ...state, selectedFolderId: action.payload };
    case 'SET_SELECTED_RESOURCE':
      return { ...state, selectedResourceId: action.payload };
    case 'SET_VIEW':
      return { ...state, view: action.payload };
    case 'SET_SEARCH':
      return { ...state, search: action.payload };
    case 'SET_FILTERS':
      return { ...state, filters: { ...state.filters, ...action.payload } };
    case 'SET_SELECTED_COLLECTION':
      return { ...state, selectedCollectionId: action.payload };
    case 'SET_PREVIEW_RESOURCE':
      return { ...state, previewResourceId: action.payload };
    default:
      return state;
  }
}

export function useResourceState() {
  const [state, dispatch] = useReducer(resourceReducer, INITIAL_STATE);

  const setSection = useCallback((section: ResourceSection) => dispatch({ type: 'SET_SECTION', payload: section }), []);
  const setResources = useCallback((resources: ResourceItem[]) => dispatch({ type: 'SET_RESOURCES', payload: resources }), []);
  const addResource = useCallback((resource: ResourceItem) => dispatch({ type: 'ADD_RESOURCE', payload: resource }), []);
  const updateResource = useCallback((id: string, updates: Partial<ResourceItem>) =>
    dispatch({ type: 'UPDATE_RESOURCE', payload: { id, updates } }), []);
  const removeResource = useCallback((id: string) => dispatch({ type: 'REMOVE_RESOURCE', payload: id }), []);
  const duplicateResource = useCallback((id: string) => dispatch({ type: 'DUPLICATE_RESOURCE', payload: id }), []);
  const toggleFavorite = useCallback((id: string) => dispatch({ type: 'TOGGLE_FAVORITE', payload: id }), []);
  const togglePin = useCallback((id: string) => dispatch({ type: 'TOGGLE_PIN', payload: id }), []);
  const toggleStarred = useCallback((id: string) => dispatch({ type: 'TOGGLE_STARRED', payload: id }), []);
  const archiveResource = useCallback((id: string) => dispatch({ type: 'ARCHIVE_RESOURCE', payload: id }), []);
  const restoreResource = useCallback((id: string) => dispatch({ type: 'RESTORE_RESOURCE', payload: id }), []);
  const setFolders = useCallback((folders: ResourceFolder[]) => dispatch({ type: 'SET_FOLDERS', payload: folders }), []);
  const addFolder = useCallback((folder: ResourceFolder) => dispatch({ type: 'ADD_FOLDER', payload: folder }), []);
  const toggleFolderExpand = useCallback((id: string) => dispatch({ type: 'TOGGLE_FOLDER_EXPAND', payload: id }), []);
  const setSelectedFolder = useCallback((id: string | null) => dispatch({ type: 'SET_SELECTED_FOLDER', payload: id }), []);
  const setSelectedResource = useCallback((id: string | null) => dispatch({ type: 'SET_SELECTED_RESOURCE', payload: id }), []);
  const setView = useCallback((view: ResourceView) => dispatch({ type: 'SET_VIEW', payload: view }), []);
  const setSearch = useCallback((search: string) => dispatch({ type: 'SET_SEARCH', payload: search }), []);
  const setFilters = useCallback((filters: Partial<ResourceFilters>) => dispatch({ type: 'SET_FILTERS', payload: filters }), []);
  const setSelectedCollection = useCallback((id: string | null) => dispatch({ type: 'SET_SELECTED_COLLECTION', payload: id }), []);
  const setPreviewResource = useCallback((id: string | null) => dispatch({ type: 'SET_PREVIEW_RESOURCE', payload: id }), []);
  const openPreview = useCallback((id: string) => {
    dispatch({ type: 'SET_PREVIEW_RESOURCE', payload: id });
    dispatch({ type: 'SET_SECTION', payload: 'preview' });
  }, []);

  return {
    state,
    setSection,
    setResources,
    addResource,
    updateResource,
    removeResource,
    duplicateResource,
    toggleFavorite,
    togglePin,
    toggleStarred,
    archiveResource,
    restoreResource,
    setFolders,
    addFolder,
    toggleFolderExpand,
    setSelectedFolder,
    setSelectedResource,
    setView,
    setSearch,
    setFilters,
    setSelectedCollection,
    setPreviewResource,
    openPreview,
  };
}
