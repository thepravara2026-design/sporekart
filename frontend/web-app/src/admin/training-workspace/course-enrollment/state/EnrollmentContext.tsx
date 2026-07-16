import { createContext, useContext } from 'react';
import { useEnrollmentState } from './useEnrollmentState';

type EnrollmentContextValue = ReturnType<typeof useEnrollmentState>;

const EnrollmentContext = createContext<EnrollmentContextValue | null>(null);

export function EnrollmentProvider({ children }: { children: React.ReactNode }) {
  const value = useEnrollmentState();
  return <EnrollmentContext.Provider value={value}>{children}</EnrollmentContext.Provider>;
}

export function useEnrollmentContext(): EnrollmentContextValue {
  const ctx = useContext(EnrollmentContext);
  if (!ctx) throw new Error('useEnrollmentContext must be used within EnrollmentProvider');
  return ctx;
}
