import { createContext, useState, useCallback, useMemo } from 'react';
import { DEFAULT_FEATURE_FLAGS } from './config';
import type { FeatureFlagConfig, FeatureFlagState } from './types';

interface FeatureFlagContextValue {
  flags: FeatureFlagConfig;
  setFlag: (key: string, state: FeatureFlagState) => void;
  resetFlags: () => void;
}

export const FeatureFlagContext = createContext<FeatureFlagContextValue | null>(null);

interface FeatureFlagProviderProps {
  children: React.ReactNode;
  initialFlags?: FeatureFlagConfig;
}

export function FeatureFlagProvider({ children, initialFlags }: FeatureFlagProviderProps) {
  const [flags, setFlags] = useState<FeatureFlagConfig>(initialFlags ?? DEFAULT_FEATURE_FLAGS);

  const setFlag = useCallback((key: string, state: FeatureFlagState) => {
    setFlags((prev) => {
      if (!prev[key]) return prev;
      return { ...prev, [key]: { ...prev[key], state } };
    });
  }, []);

  const resetFlags = useCallback(() => {
    setFlags(initialFlags ?? DEFAULT_FEATURE_FLAGS);
  }, [initialFlags]);

  const value = useMemo(() => ({ flags, setFlag, resetFlags }), [flags, setFlag, resetFlags]);

  return (
    <FeatureFlagContext.Provider value={value}>
      {children}
    </FeatureFlagContext.Provider>
  );
}
