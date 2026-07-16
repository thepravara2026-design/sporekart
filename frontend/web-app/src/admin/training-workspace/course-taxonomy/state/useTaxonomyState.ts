import { useCallback, useReducer } from 'react';
import type {
  TaxonomySection,
  TaxonomyNode,
  TaxonomyTag,
  TopicNode,
} from '../data/taxonomyMockData';
import {
  MOCK_TAXONOMY_TREE,
  MOCK_TAGS,
  MOCK_TOPICS,
} from '../data/taxonomyMockData';

interface TaxonomyState {
  section: TaxonomySection;
  tree: TaxonomyNode[];
  expandedIds: string[];
  selectedCategoryId: string | null;
  tags: TaxonomyTag[];
  topics: TopicNode[];
  search: string;
  filterType: string;
  selectedTopicId: string | null;
}

type TaxonomyAction =
  | { type: 'SET_SECTION'; payload: TaxonomySection }
  | { type: 'SET_TREE'; payload: TaxonomyNode[] }
  | { type: 'ADD_CATEGORY'; payload: TaxonomyNode }
  | { type: 'UPDATE_CATEGORY'; payload: { id: string; updates: Partial<TaxonomyNode> } }
  | { type: 'REMOVE_CATEGORY'; payload: string }
  | { type: 'TOGGLE_EXPAND'; payload: string }
  | { type: 'SET_SELECTED_CATEGORY'; payload: string | null }
  | { type: 'SET_TAGS'; payload: TaxonomyTag[] }
  | { type: 'ADD_TAG'; payload: TaxonomyTag }
  | { type: 'REMOVE_TAG'; payload: string }
  | { type: 'SET_TOPICS'; payload: TopicNode[] }
  | { type: 'ADD_TOPIC'; payload: TopicNode }
  | { type: 'REMOVE_TOPIC'; payload: string }
  | { type: 'SET_SEARCH'; payload: string }
  | { type: 'SET_FILTER'; payload: string }
  | { type: 'SET_SELECTED_TOPIC'; payload: string | null };

function updateNode(nodes: TaxonomyNode[], id: string, updates: Partial<TaxonomyNode>): TaxonomyNode[] {
  return nodes.map((node) => {
    if (node.id === id) return { ...node, ...updates };
    if (node.children.length) {
      return { ...node, children: updateNode(node.children, id, updates) };
    }
    return node;
  });
}

function removeNode(nodes: TaxonomyNode[], id: string): TaxonomyNode[] {
  return nodes
    .filter((node) => node.id !== id)
    .map((node) => ({ ...node, children: removeNode(node.children, id) }));
}

function addNode(nodes: TaxonomyNode[], parentId: string | null, newNode: TaxonomyNode): TaxonomyNode[] {
  if (parentId === null) return [...nodes, newNode];
  return nodes.map((node) => {
    if (node.id === parentId) {
      return { ...node, children: [...node.children, newNode] };
    }
    if (node.children.length) {
      return { ...node, children: addNode(node.children, parentId, newNode) };
    }
    return node;
  });
}

const INITIAL_STATE: TaxonomyState = {
  section: 'overview',
  tree: MOCK_TAXONOMY_TREE,
  expandedIds: ['cat-agriculture', 'cat-mushroom', 'cat-oyster', 'cat-spawn'],
  selectedCategoryId: null,
  tags: MOCK_TAGS,
  topics: MOCK_TOPICS,
  search: '',
  filterType: 'all',
  selectedTopicId: null,
};

function taxonomyReducer(state: TaxonomyState, action: TaxonomyAction): TaxonomyState {
  switch (action.type) {
    case 'SET_SECTION':
      return { ...state, section: action.payload };
    case 'SET_TREE':
      return { ...state, tree: action.payload };
    case 'ADD_CATEGORY':
      return { ...state, tree: addNode(state.tree, action.payload.parentId, action.payload) };
    case 'UPDATE_CATEGORY':
      return { ...state, tree: updateNode(state.tree, action.payload.id, action.payload.updates) };
    case 'REMOVE_CATEGORY':
      return { ...state, tree: removeNode(state.tree, action.payload) };
    case 'TOGGLE_EXPAND':
      return {
        ...state,
        expandedIds: state.expandedIds.includes(action.payload)
          ? state.expandedIds.filter((id) => id !== action.payload)
          : [...state.expandedIds, action.payload],
      };
    case 'SET_SELECTED_CATEGORY':
      return { ...state, selectedCategoryId: action.payload };
    case 'SET_TAGS':
      return { ...state, tags: action.payload };
    case 'ADD_TAG':
      return { ...state, tags: [...state.tags, action.payload] };
    case 'REMOVE_TAG':
      return { ...state, tags: state.tags.filter((t) => t.id !== action.payload) };
    case 'SET_TOPICS':
      return { ...state, topics: action.payload };
    case 'ADD_TOPIC':
      return { ...state, topics: [...state.topics, action.payload] };
    case 'REMOVE_TOPIC':
      return { ...state, topics: state.topics.filter((t) => t.id !== action.payload) };
    case 'SET_SEARCH':
      return { ...state, search: action.payload };
    case 'SET_FILTER':
      return { ...state, filterType: action.payload };
    case 'SET_SELECTED_TOPIC':
      return { ...state, selectedTopicId: action.payload };
    default:
      return state;
  }
}

export function useTaxonomyState() {
  const [state, dispatch] = useReducer(taxonomyReducer, INITIAL_STATE);

  const setSection = useCallback((section: TaxonomySection) => dispatch({ type: 'SET_SECTION', payload: section }), []);
  const setTree = useCallback((tree: TaxonomyNode[]) => dispatch({ type: 'SET_TREE', payload: tree }), []);
  const addCategory = useCallback((node: TaxonomyNode) => dispatch({ type: 'ADD_CATEGORY', payload: node }), []);
  const updateCategory = useCallback((id: string, updates: Partial<TaxonomyNode>) =>
    dispatch({ type: 'UPDATE_CATEGORY', payload: { id, updates } }), []);
  const removeCategory = useCallback((id: string) => dispatch({ type: 'REMOVE_CATEGORY', payload: id }), []);
  const toggleExpand = useCallback((id: string) => dispatch({ type: 'TOGGLE_EXPAND', payload: id }), []);
  const setSelectedCategory = useCallback((id: string | null) => dispatch({ type: 'SET_SELECTED_CATEGORY', payload: id }), []);
  const setTags = useCallback((tags: TaxonomyTag[]) => dispatch({ type: 'SET_TAGS', payload: tags }), []);
  const addTag = useCallback((tag: TaxonomyTag) => dispatch({ type: 'ADD_TAG', payload: tag }), []);
  const removeTag = useCallback((id: string) => dispatch({ type: 'REMOVE_TAG', payload: id }), []);
  const setTopics = useCallback((topics: TopicNode[]) => dispatch({ type: 'SET_TOPICS', payload: topics }), []);
  const addTopic = useCallback((topic: TopicNode) => dispatch({ type: 'ADD_TOPIC', payload: topic }), []);
  const removeTopic = useCallback((id: string) => dispatch({ type: 'REMOVE_TOPIC', payload: id }), []);
  const setSearch = useCallback((search: string) => dispatch({ type: 'SET_SEARCH', payload: search }), []);
  const setFilter = useCallback((filter: string) => dispatch({ type: 'SET_FILTER', payload: filter }), []);
  const setSelectedTopic = useCallback((id: string | null) => dispatch({ type: 'SET_SELECTED_TOPIC', payload: id }), []);

  return {
    state,
    setSection,
    setTree,
    addCategory,
    updateCategory,
    removeCategory,
    toggleExpand,
    setSelectedCategory,
    setTags,
    addTag,
    removeTag,
    setTopics,
    addTopic,
    removeTopic,
    setSearch,
    setFilter,
    setSelectedTopic,
  };
}
