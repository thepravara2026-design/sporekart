import { createContext, useContext } from 'react';
import { useResourceState } from './useResourceState';

type ResourceContextValue = ReturnType<typeof useResourceState>;

const ResourceContext = createContext<ResourceContextValue | null>(null);

export function ResourceProvider({ children }: { children: React.ReactNode }) {
  const value = useResourceState();
  return <ResourceContext.Provider value={value}>{children}</ResourceContext.Provider>;
}

export function useResourceContext(): ResourceContextValue {
  const ctx = useContext(ResourceContext);
  if (!ctx) throw new Error('useResourceContext must be used within ResourceProvider');
  return ctx;
}
