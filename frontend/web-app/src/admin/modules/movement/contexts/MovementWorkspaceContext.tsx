import { createContext, useContext, useState, type ReactNode } from 'react';

interface MovementWorkspaceContextValue {
  activeSection: string;
  setActiveSection: (section: string) => void;
  searchQuery: string;
  setSearchQuery: (query: string) => void;
  profileTransactionId: string | null;
  setProfileTransactionId: (id: string | null) => void;
}

const MovementWorkspaceContext = createContext<MovementWorkspaceContextValue | null>(null);

export function MovementWorkspaceProvider({ children, initialSection = 'overview' }: { children: ReactNode; initialSection?: string }) {
  const [activeSection, setActiveSection] = useState(initialSection);
  const [searchQuery, setSearchQuery] = useState('');
  const [profileTransactionId, setProfileTransactionId] = useState<string | null>(null);

  return (
    <MovementWorkspaceContext.Provider value={{ activeSection, setActiveSection, searchQuery, setSearchQuery, profileTransactionId, setProfileTransactionId }}>
      {children}
    </MovementWorkspaceContext.Provider>
  );
}

export function useMovementWorkspace() {
  const ctx = useContext(MovementWorkspaceContext);
  if (!ctx) throw new Error('useMovementWorkspace must be used within MovementWorkspaceProvider');
  return ctx;
}
