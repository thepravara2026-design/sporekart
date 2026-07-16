import { createContext, useContext, ReactNode } from 'react';
import { useStudentWorkspaceState, StudentWorkspaceContextValue } from './workspaceState';

const StudentWorkspaceCtx = createContext<StudentWorkspaceContextValue | null>(null);

export function StudentWorkspaceProvider({ children }: { children: ReactNode }) {
  const state = useStudentWorkspaceState();
  return (
    <StudentWorkspaceCtx.Provider value={state}>
      {children}
    </StudentWorkspaceCtx.Provider>
  );
}

export function useStudentWorkspace(): StudentWorkspaceContextValue {
  const ctx = useContext(StudentWorkspaceCtx);
  if (!ctx) {
    throw new Error('useStudentWorkspace must be used within StudentWorkspaceProvider');
  }
  return ctx;
}

export default StudentWorkspaceCtx;
