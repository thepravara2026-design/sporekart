import React, { createContext, useContext, useState, useCallback } from 'react';

const FeatureFlagContext = createContext<any>(undefined);

export interface FeatureFlag {
  key: string;
  enabled: boolean;
  description?: string;
  rolloutPercentage?: number;
  conditions?: Record<string, any>;
}

export function FeatureFlagProvider({ children, initialFlags = {} }: { children: React.ReactNode; initialFlags?: Record<string, any> }) {
  const [flags, setFlags] = useState<Map<string, any>>(new Map(Object.entries(initialFlags)));

  const isEnabled = useCallback((key: string) => {
    const flag = flags.get(key);
    return flag?.enabled ?? false;
  }, [flags]);

  const getFlag = useCallback((key: string) => {
    return flags.get(key);
  }, [flags]);

  const setFlag = useCallback((key: string, enabled: boolean) => {
    setFlags((_prev) => {
      const newMap = new Map(flags);
      const existing = newMap.get(key) || {};
      newMap.set(key, { ...existing, enabled });
      return newMap;
    });
  }, [flags]);

  const refreshFlags = useCallback(async () => {
    // In production, fetch from API
    console.log('Refreshing feature flags...');
  }, []);

  return (
    <FeatureFlagContext.Provider
      value={{
        flags,
        isEnabled,
        getFlag,
        setFlag,
        refreshFlags,
      }}
    >
      {children}
    </FeatureFlagContext.Provider>
  );
}

export function useFeatureFlags() {
  const context = useContext(FeatureFlagContext);
  if (!context) {
    throw new Error('useFeatureFlags must be used within a FeatureFlagProvider');
  }
  return context;
}