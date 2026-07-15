import { createContext, useContext, useState, type ReactNode } from 'react';

interface IntelligenceWorkspaceContextValue {
  activeSection: string;
  setActiveSection: (section: string) => void;
  searchQuery: string;
  setSearchQuery: (query: string) => void;
}

const ctx = createContext<IntelligenceWorkspaceContextValue | null>(null);

export function IntelligenceWorkspaceProvider({ children, initialSection = 'overview' }: { children: ReactNode; initialSection?: string }) {
  const [activeSection, setActiveSection] = useState(initialSection);
  const [searchQuery, setSearchQuery] = useState('');
  return (
    <ctx.Provider value={{ activeSection, setActiveSection, searchQuery, setSearchQuery }}>
      {children}
    </ctx.Provider>
  );
}

export function useIntelligenceWorkspace() {
  const c = useContext(ctx);
  if (!c) throw new Error('useIntelligenceWorkspace must be used within IntelligenceWorkspaceProvider');
  return c;
}
