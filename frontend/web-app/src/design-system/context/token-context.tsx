import React, { createContext, useContext } from 'react';

export interface DesignTokens {
  color: {
    primitives: Record<string, Record<string, { value: string; type: string; description?: string }>>;
    semantic: Record<string, any>;
  };
  typography: {
    fontFamily: Record<string, { value: string; type: string; description?: string }>;
    fontSize: Record<string, { value: string; type: string; description?: string }>;
    fontWeight: Record<string, { value: string; type: string; description?: string }>;
    lineHeight: Record<string, { value: string | number; type: string; description?: string }>;
    letterSpacing: Record<string, { value: string; type: string; description?: string }>;
  };
  spacing: Record<string, any>;
  radius: Record<string, any>;
  elevation: Record<string, any>;
  border: Record<string, any>;
  opacity: Record<string, any>;
  animation: Record<string, any>;
  sizing: Record<string, any>;
  breakpoints: Record<string, { value: string; type: string; description?: string }>;
  zIndex: Record<string, { value: number; type: string; description?: string }>;
}

export interface DesignTokensContextValue {
  tokens: DesignTokens;
  resolvedTheme: 'light' | 'dark' | 'high-contrast';
  getToken: (path: string) => string | number | undefined;
}

export const TokenContext = createContext<DesignTokensContextValue | undefined>(undefined);

export function DesignTokensProvider({ children, tokens, resolvedTheme }: { children: React.ReactNode; tokens: DesignTokens; resolvedTheme: 'light' | 'dark' | 'high-contrast' }) {
  const getToken = (path: string): string | number | undefined => {
    const parts = path.split('.');
    let current: any = tokens;
    for (const part of parts) {
      if (current && typeof current === 'object' && part in current) {
        current = current[part];
      } else {
        return undefined;
      }
    }
    return current?.value;
  };

  return (
    <TokenContext.Provider value={{ tokens, resolvedTheme, getToken }}>
      {children}
    </TokenContext.Provider>
  );
}

export function useTokenContext() {
  const context = useContext(TokenContext);
  if (!context) {
    throw new Error('useTokenContext must be used within a DesignTokensProvider');
  }
  return context;
}

export function useDesignTokens() {
  return useTokenContext();
}

export function useToken(path: string): string | number | undefined {
  const { getToken } = useTokenContext();
  return getToken(path);
}