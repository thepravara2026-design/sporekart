import { createContext, useContext, useState, type ReactNode } from 'react';

interface BatchWorkspaceContextValue {
  activeSection: string;
  setActiveSection: (section: string) => void;
  searchQuery: string;
  setSearchQuery: (query: string) => void;
  profileBatchId: string | null;
  setProfileBatchId: (id: string | null) => void;
}

const BatchWorkspaceContext = createContext<BatchWorkspaceContextValue | null>(null);

export function BatchWorkspaceProvider({ children, initialSection = 'overview' }: { children: ReactNode; initialSection?: string }) {
  const [activeSection, setActiveSection] = useState(initialSection);
  const [searchQuery, setSearchQuery] = useState('');
  const [profileBatchId, setProfileBatchId] = useState<string | null>(null);

  return (
    <BatchWorkspaceContext.Provider value={{ activeSection, setActiveSection, searchQuery, setSearchQuery, profileBatchId, setProfileBatchId }}>
      {children}
    </BatchWorkspaceContext.Provider>
  );
}

export function useBatchWorkspace() {
  const ctx = useContext(BatchWorkspaceContext);
  if (!ctx) throw new Error('useBatchWorkspace must be used within BatchWorkspaceProvider');
  return ctx;
}

