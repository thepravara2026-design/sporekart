import { createContext, useContext } from 'react';

export type Theme = 'light' | 'dark' | 'high-contrast' | 'system';

export type ResolvedTheme = 'light' | 'dark' | 'high-contrast';

export interface ThemeContextValue {
  theme: 'light' | 'dark' | 'high-contrast' | 'system';
  resolvedTheme: 'light' | 'dark' | 'high-contrast';
  setTheme: (theme: 'light' | 'dark' | 'high-contrast' | 'system') => void;
  toggleTheme: () => void;
}

export const ThemeContext = createContext<ThemeContextValue | undefined>(undefined);

export function useThemeContext() {
  const context = useContext(ThemeContext);
  if (!context) {
    throw new Error('useThemeContext must be used within a ThemeProvider');
  }
  return context;
}