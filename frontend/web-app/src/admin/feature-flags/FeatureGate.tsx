import { useFeatureFlag } from './useFeatureFlag';
import type { FeatureFlagState } from './types';

interface FeatureGateProps {
  flag: string;
  fallback?: React.ReactNode;
  children: React.ReactNode;
  requiredState?: FeatureFlagState[];
}

export function FeatureGate({ flag, fallback = null, children, requiredState }: FeatureGateProps) {
  const { isEnabled, getState, isVisible } = useFeatureFlag();

  if (!isVisible(flag)) return null;

  if (requiredState) {
    const state = getState(flag);
    if (!state || !requiredState.includes(state)) return <>{fallback}</>;
  } else if (!isEnabled(flag)) {
    return <>{fallback}</>;
  }

  return <>{children}</>;
}
