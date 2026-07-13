import { createContext, useContext } from 'react';
import type { FormContextValue } from './types';

export const FormContext = createContext<FormContextValue | undefined>(undefined);

export function useFormContext(): FormContextValue {
  const context = useContext(FormContext);
  if (!context) {
    throw new Error('useFormContext must be used within a FormProvider');
  }
  return context;
}
