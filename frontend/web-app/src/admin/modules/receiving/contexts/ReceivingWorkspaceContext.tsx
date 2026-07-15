import { createContext, useContext, useState, type ReactNode } from 'react';

interface ReceivingWorkspaceContextValue {
  activeSection: string;
  setActiveSection: (section: string) => void;
  searchQuery: string;
  setSearchQuery: (query: string) => void;
  profileReceiptId: string | null;
  setProfileReceiptId: (id: string | null) => void;
}

const ReceivingWorkspaceContext = createContext<ReceivingWorkspaceContextValue | null>(null);

export function ReceivingWorkspaceProvider({ children, initialSection = 'overview' }: { children: ReactNode; initialSection?: string }) {
  const [activeSection, setActiveSection] = useState(initialSection);
  const [searchQuery, setSearchQuery] = useState('');
  const [profileReceiptId, setProfileReceiptId] = useState<string | null>(null);
  return (
    <ReceivingWorkspaceContext.Provider value={{ activeSection, setActiveSection, searchQuery, setSearchQuery, profileReceiptId, setProfileReceiptId }}>
      {children}
    </ReceivingWorkspaceContext.Provider>
  );
}

export function useReceivingWorkspace() {
  const ctx = useContext(ReceivingWorkspaceContext);
  if (!ctx) throw new Error('useReceivingWorkspace must be used within ReceivingWorkspaceProvider');
  return ctx;
}
