import { createContext, useContext } from 'react';
import type { User, Session } from '@supabase/supabase-js';
import type { Role } from './config/roles';

export interface AuthState {
  user: User | null;
  session: Session | null;
  loading: boolean;
  isAuthenticated: boolean;
  userRole: Role;
}

export interface AppContextValue {
  auth: AuthState;
  logout: (reason?: 'user' | 'expired') => void;
  paletteOpen: boolean;
  setPaletteOpen: (b: boolean) => void;
}

export const AppContext = createContext<AppContextValue | null>(null);

export function useApp(): AppContextValue {
  const ctx = useContext(AppContext);
  if (!ctx) throw new Error('useApp must be used within AppContext');
  return ctx;
}
