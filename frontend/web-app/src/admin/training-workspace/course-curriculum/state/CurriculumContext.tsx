import { createContext, useContext, type ReactNode } from 'react';
import type {
  CurriculumSection,
  CurriculumNode,
  CurriculumTemplate,
} from '../data/curriculumMockData';

type PreviewDevice = 'desktop' | 'tablet' | 'mobile';
type PreviewRole = 'student' | 'trainer';

interface CurriculumContextValue {
  state: {
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
  };
  setSection: (section: CurriculumSection) => void;
  setTree: (tree: CurriculumNode[]) => void;
  addNode: (node: CurriculumNode, parentId: string | null) => void;
  updateNode: (id: string, updates: Partial<CurriculumNode>) => void;
  removeNode: (id: string) => void;
  duplicateNode: (id: string) => void;
  toggleExpand: (id: string) => void;
  setSelected: (id: string | null) => void;
  setTemplates: (templates: CurriculumTemplate[]) => void;
  addTemplate: (template: CurriculumTemplate) => void;
  setPreviewDevice: (device: PreviewDevice) => void;
  setPreviewRole: (role: PreviewRole) => void;
  setSearch: (search: string) => void;
  setFilter: (filter: string) => void;
  setSelectedTemplate: (id: string | null) => void;
}

const CurriculumContext = createContext<CurriculumContextValue | null>(null);

export function useCurriculumContext(): CurriculumContextValue {
  const ctx = useContext(CurriculumContext);
  if (!ctx) throw new Error('useCurriculumContext must be used within CurriculumProvider');
  return ctx;
}

interface Props {
  value: CurriculumContextValue;
  children: ReactNode;
}

export function CurriculumProvider({ value, children }: Props) {
  return <CurriculumContext.Provider value={value}>{children}</CurriculumContext.Provider>;
}
