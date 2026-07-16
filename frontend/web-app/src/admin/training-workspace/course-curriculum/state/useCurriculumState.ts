import { useCallback, useReducer } from 'react';
import type {
  CurriculumSection,
  CurriculumNode,
  CurriculumTemplate,
} from '../data/curriculumMockData';
import {
  MOCK_CURRICULUM_TREE,
  MOCK_TEMPLATES,
} from '../data/curriculumMockData';

type PreviewDevice = 'desktop' | 'tablet' | 'mobile';
type PreviewRole = 'student' | 'trainer';

interface CurriculumState {
  section: CurriculumSection;
  tree: CurriculumNode[];
  expandedIds: string[];
  selectedNodeId: string | null;
  templates: CurriculumTemplate[];
  previewDevice: PreviewDevice;
  previewRole: PreviewRole;
  search: string;
  filterType: string;
  selectedTemplateId: string | null;
}

type CurriculumAction =
  | { type: 'SET_SECTION'; payload: CurriculumSection }
  | { type: 'SET_TREE'; payload: CurriculumNode[] }
  | { type: 'ADD_NODE'; payload: { node: CurriculumNode; parentId: string | null } }
  | { type: 'UPDATE_NODE'; payload: { id: string; updates: Partial<CurriculumNode> } }
  | { type: 'REMOVE_NODE'; payload: string }
  | { type: 'DUPLICATE_NODE'; payload: string }
  | { type: 'TOGGLE_EXPAND'; payload: string }
  | { type: 'SET_SELECTED'; payload: string | null }
  | { type: 'SET_TEMPLATES'; payload: CurriculumTemplate[] }
  | { type: 'ADD_TEMPLATE'; payload: CurriculumTemplate }
  | { type: 'SET_PREVIEW_DEVICE'; payload: PreviewDevice }
  | { type: 'SET_PREVIEW_ROLE'; payload: PreviewRole }
  | { type: 'SET_SEARCH'; payload: string }
  | { type: 'SET_FILTER'; payload: string }
  | { type: 'SET_SELECTED_TEMPLATE'; payload: string | null };

function mapNode(nodes: CurriculumNode[], id: string, fn: (n: CurriculumNode) => CurriculumNode): CurriculumNode[] {
  return nodes.map((node) => {
    if (node.id === id) return fn(node);
    if (node.children.length) return { ...node, children: mapNode(node.children, id, fn) };
    return node;
  });
}

function removeNode(nodes: CurriculumNode[], id: string): CurriculumNode[] {
  return nodes
    .filter((node) => node.id !== id)
    .map((node) => ({ ...node, children: removeNode(node.children, id) }));
}

function addNode(nodes: CurriculumNode[], parentId: string | null, newNode: CurriculumNode): CurriculumNode[] {
  if (parentId === null) return [...nodes, newNode];
  return nodes.map((node) => {
    if (node.id === parentId) return { ...node, children: [...node.children, newNode] };
    if (node.children.length) return { ...node, children: addNode(node.children, parentId, newNode) };
    return node;
  });
}

function cloneNode(node: CurriculumNode, idMap: Map<string, string>): CurriculumNode {
  const newId = `node-${Date.now()}-${Math.random().toString(36).slice(2, 7)}`;
  idMap.set(node.id, newId);
  return {
    ...node,
    id: newId,
    name: `${node.name} (copy)`,
    children: node.children.map((c) => cloneNode(c, idMap)),
  };
}

const INITIAL_STATE: CurriculumState = {
  section: 'overview',
  tree: MOCK_CURRICULUM_TREE,
  expandedIds: ['cur-mushroom', 'mod-1', 'mod-2', 'les-1-1'],
  selectedNodeId: null,
  templates: MOCK_TEMPLATES,
  previewDevice: 'desktop',
  previewRole: 'student',
  search: '',
  filterType: 'all',
  selectedTemplateId: null,
};

function curriculumReducer(state: CurriculumState, action: CurriculumAction): CurriculumState {
  switch (action.type) {
    case 'SET_SECTION':
      return { ...state, section: action.payload };
    case 'SET_TREE':
      return { ...state, tree: action.payload };
    case 'ADD_NODE':
      return { ...state, tree: addNode(state.tree, action.payload.parentId, action.payload.node) };
    case 'UPDATE_NODE':
      return { ...state, tree: mapNode(state.tree, action.payload.id, (n) => ({ ...n, ...action.payload.updates })) };
    case 'REMOVE_NODE':
      return { ...state, tree: removeNode(state.tree, action.payload) };
    case 'DUPLICATE_NODE': {
      const idMap = new Map<string, string>();
      const cloned = (() => {
        const findAndClone = (nodes: CurriculumNode[]): CurriculumNode[] =>
          nodes.flatMap((n) => {
            if (n.id === action.payload) return [cloneNode(n, idMap)];
            if (n.children.length) return [{ ...n, children: findAndClone(n.children) }];
            return [n];
          });
        return findAndClone(state.tree);
      })();
      return { ...state, tree: cloned };
    }
    case 'TOGGLE_EXPAND':
      return {
        ...state,
        expandedIds: state.expandedIds.includes(action.payload)
          ? state.expandedIds.filter((id) => id !== action.payload)
          : [...state.expandedIds, action.payload],
      };
    case 'SET_SELECTED':
      return { ...state, selectedNodeId: action.payload };
    case 'SET_TEMPLATES':
      return { ...state, templates: action.payload };
    case 'ADD_TEMPLATE':
      return { ...state, templates: [...state.templates, action.payload] };
    case 'SET_PREVIEW_DEVICE':
      return { ...state, previewDevice: action.payload };
    case 'SET_PREVIEW_ROLE':
      return { ...state, previewRole: action.payload };
    case 'SET_SEARCH':
      return { ...state, search: action.payload };
    case 'SET_FILTER':
      return { ...state, filterType: action.payload };
    case 'SET_SELECTED_TEMPLATE':
      return { ...state, selectedTemplateId: action.payload };
    default:
      return state;
  }
}

export function useCurriculumState() {
  const [state, dispatch] = useReducer(curriculumReducer, INITIAL_STATE);

  const setSection = useCallback((section: CurriculumSection) => dispatch({ type: 'SET_SECTION', payload: section }), []);
  const setTree = useCallback((tree: CurriculumNode[]) => dispatch({ type: 'SET_TREE', payload: tree }), []);
  const addNodeAction = useCallback((node: CurriculumNode, parentId: string | null) =>
    dispatch({ type: 'ADD_NODE', payload: { node, parentId } }), []);
  const updateNode = useCallback((id: string, updates: Partial<CurriculumNode>) =>
    dispatch({ type: 'UPDATE_NODE', payload: { id, updates } }), []);
  const removeNode = useCallback((id: string) => dispatch({ type: 'REMOVE_NODE', payload: id }), []);
  const duplicateNode = useCallback((id: string) => dispatch({ type: 'DUPLICATE_NODE', payload: id }), []);
  const toggleExpand = useCallback((id: string) => dispatch({ type: 'TOGGLE_EXPAND', payload: id }), []);
  const setSelected = useCallback((id: string | null) => dispatch({ type: 'SET_SELECTED', payload: id }), []);
  const setTemplates = useCallback((templates: CurriculumTemplate[]) => dispatch({ type: 'SET_TEMPLATES', payload: templates }), []);
  const addTemplate = useCallback((template: CurriculumTemplate) => dispatch({ type: 'ADD_TEMPLATE', payload: template }), []);
  const setPreviewDevice = useCallback((device: PreviewDevice) => dispatch({ type: 'SET_PREVIEW_DEVICE', payload: device }), []);
  const setPreviewRole = useCallback((role: PreviewRole) => dispatch({ type: 'SET_PREVIEW_ROLE', payload: role }), []);
  const setSearch = useCallback((search: string) => dispatch({ type: 'SET_SEARCH', payload: search }), []);
  const setFilter = useCallback((filter: string) => dispatch({ type: 'SET_FILTER', payload: filter }), []);
  const setSelectedTemplate = useCallback((id: string | null) => dispatch({ type: 'SET_SELECTED_TEMPLATE', payload: id }), []);

  return {
    state,
    setSection,
    setTree,
    addNode: addNodeAction,
    updateNode,
    removeNode,
    duplicateNode,
    toggleExpand,
    setSelected,
    setTemplates,
    addTemplate,
    setPreviewDevice,
    setPreviewRole,
    setSearch,
    setFilter,
    setSelectedTemplate,
  };
}
