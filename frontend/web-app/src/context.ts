import type { Role } from './config/roles';

export interface AppContextValue {
  activeRole: Role;
  setActiveRole: (r: Role) => void;
  paletteOpen: boolean;
  setPaletteOpen: (b: boolean) => void;
}

import { createContext, useContext } from 'react';

export const AppContext = createContext<AppContextValue | null>(null);

export function useApp(): AppContextValue {
  const ctx = useContext(AppContext);
  if (!ctx) throw new Error('useApp must be used within AppContext');
  return ctx;
}
