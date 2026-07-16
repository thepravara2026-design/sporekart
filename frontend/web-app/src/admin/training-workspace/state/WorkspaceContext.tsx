import { createContext, useContext, ReactNode } from 'react';
import { useWorkspaceState, WorkspaceContextValue } from './workspaceState';

const WorkspaceCtx = createContext<WorkspaceContextValue | null>(null);

export function WorkspaceProvider({ children }: { children: ReactNode }) {
  const state = useWorkspaceState();
  return (
    <WorkspaceCtx.Provider value={state}>
      {children}
    </WorkspaceCtx.Provider>
  );
}

export function useTrainingWorkspace(): WorkspaceContextValue {
  const ctx = useContext(WorkspaceCtx);
  if (!ctx) {
    throw new Error('useTrainingWorkspace must be used within WorkspaceProvider');
  }
  return ctx;
}

export default WorkspaceCtx;
