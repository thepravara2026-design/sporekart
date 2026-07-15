# Feature Flags

## Architecture

Config-driven feature flag system using React Context. Flags can be enabled, disabled, or marked as experimental/beta/coming-soon/hidden. Components react automatically to flag state.

```
FeatureFlagProvider (holds flag config)
  └─ useFeatureFlag() hook
       └─ FeatureGate (conditional render by flag key)
```

## Usage

### Provider

```tsx
<FeatureFlagProvider>
  <App />
</FeatureFlagProvider>
```

### Hook

```tsx
const { isEnabled, getState, getFlag, isVisible, setFlag } = useFeatureFlag();
isEnabled('advanced-analytics')     // true if enabled/beta/experimental
getState('inventory-forecast')      // 'coming_soon'
isVisible('voice-commands')         // false (hidden)
setFlag('dark-mode', 'enabled')     // change state at runtime
```

### Component Gate

```tsx
<FeatureGate flag="advanced-analytics" fallback={<DisabledFeature />}>
  <AnalyticsDashboard />
</FeatureGate>

<FeatureGate flag="dark-mode" requiredState={['enabled', 'beta']}>
  <DarkModeToggle />
</FeatureGate>
```

## Flag States

| State | Description | isEnabled |
|---|---|---|
| `enabled` | Fully available | true |
| `disabled` | Turned off | false |
| `experimental` | In testing | true |
| `beta` | Preview release | true |
| `coming_soon` | Announced but not ready | false |
| `hidden` | Completely invisible | false |

## Configuration

Flags are defined in `config.ts`:

```ts
{
  'advanced-analytics': {
    key: 'advanced-analytics',
    label: 'Advanced Analytics',
    description: 'AI-powered analytics and predictive insights',
    state: 'experimental',
  },
}
```

## Extension Points

- **Remote config**: Replace `DEFAULT_FEATURE_FLAGS` with fetched configuration via the `setFlag()` API
- **A/B testing**: Flag states can be driven by user cohorts
- **Dependencies**: The `dependencies` field allows cascading flag resolution
