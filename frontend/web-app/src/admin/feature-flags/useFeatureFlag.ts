import { useContext, useCallback } from 'react';
import { FeatureFlagContext } from './FeatureFlagProvider';
import type { FeatureFlagState } from './types';

export function useFeatureFlag() {
  const ctx = useContext(FeatureFlagContext);
  if (!ctx) throw new Error('useFeatureFlag must be used within FeatureFlagProvider');

  const isEnabled = useCallback(
    (key: string) => ctx.flags[key]?.state === 'enabled' || ctx.flags[key]?.state === 'beta' || ctx.flags[key]?.state === 'experimental',
    [ctx.flags]
  );

  const getState = useCallback(
    (key: string): FeatureFlagState | undefined => ctx.flags[key]?.state,
    [ctx.flags]
  );

  const getFlag = useCallback(
    (key: string) => ctx.flags[key],
    [ctx.flags]
  );

  const isVisible = useCallback(
    (key: string) => ctx.flags[key]?.state !== 'hidden',
    [ctx.flags]
  );

  return { flags: ctx.flags, isEnabled, getState, getFlag, isVisible, setFlag: ctx.setFlag };
}
