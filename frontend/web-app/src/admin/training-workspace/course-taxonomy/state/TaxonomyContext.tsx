import { createContext, useContext, type ReactNode } from 'react';
import type {
  TaxonomySection,
  TaxonomyNode,
  TaxonomyTag,
  TopicNode,
} from '../data/taxonomyMockData';

interface TaxonomyContextValue {
  state: {
    section: TaxonomySection;
    tree: TaxonomyNode[];
    expandedIds: string[];
    selectedCategoryId: string | null;
    tags: TaxonomyTag[];
    topics: TopicNode[];
    search: string;
    filterType: string;
    selectedTopicId: string | null;
  };
  setSection: (section: TaxonomySection) => void;
  setTree: (tree: TaxonomyNode[]) => void;
  addCategory: (node: TaxonomyNode) => void;
  updateCategory: (id: string, updates: Partial<TaxonomyNode>) => void;
  removeCategory: (id: string) => void;
  toggleExpand: (id: string) => void;
  setSelectedCategory: (id: string | null) => void;
  setTags: (tags: TaxonomyTag[]) => void;
  addTag: (tag: TaxonomyTag) => void;
  removeTag: (id: string) => void;
  setTopics: (topics: TopicNode[]) => void;
  addTopic: (topic: TopicNode) => void;
  removeTopic: (id: string) => void;
  setSearch: (search: string) => void;
  setFilter: (filter: string) => void;
  setSelectedTopic: (id: string | null) => void;
}

const TaxonomyContext = createContext<TaxonomyContextValue | null>(null);

export function useTaxonomyContext(): TaxonomyContextValue {
  const ctx = useContext(TaxonomyContext);
  if (!ctx) throw new Error('useTaxonomyContext must be used within TaxonomyProvider');
  return ctx;
}

interface Props {
  value: TaxonomyContextValue;
  children: ReactNode;
}

export function TaxonomyProvider({ value, children }: Props) {
  return <TaxonomyContext.Provider value={value}>{children}</TaxonomyContext.Provider>;
}
