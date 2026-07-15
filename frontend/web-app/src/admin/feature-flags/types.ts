export type FeatureFlagState = 'enabled' | 'disabled' | 'experimental' | 'beta' | 'coming_soon' | 'hidden';

export interface FeatureFlag {
  key: string;
  label: string;
  description?: string;
  state: FeatureFlagState;
  dependencies?: string[];
}

export type FeatureFlagConfig = Record<string, FeatureFlag>;
