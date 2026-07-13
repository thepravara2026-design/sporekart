# ThemeProvider

## Overview

`ThemeProvider` manages theme state across the application. It supports light, dark, high-contrast, and system-preference modes with automatic persistence.

## Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `defaultTheme` | `'light' \| 'dark'` | `'light'` | Initial theme if no stored preference |
| `storageKey` | `string` | `'sporekart-theme'` | localStorage key for persistence |
| `enableHighContrast` | `boolean` | `true` | Whether to respect `prefers-contrast: more` |
| `enableSystemPreference` | `boolean` | `true` | Whether to respect `prefers-color-scheme` |
| `children` | `ReactNode` | — | |

## Theme Context API

```ts
interface ThemeContext {
  theme: 'light' | 'dark';
  resolvedTheme: 'light' | 'dark';   // actual applied theme (accounts for system)
  setTheme: (theme: 'light' | 'dark' | 'system') => void;
  toggleTheme: () => void;
  isHighContrast: boolean;
  colorScheme: 'light' | 'dark';     // matches resolvedTheme
}
```

Access via `useTheme()` hook.

## Behavior

- **localStorage persistence** — theme choice is saved under `storageKey` and restored on mount.
- **System preference detection** — when `theme === 'system'`, listens to `matchMedia('prefers-color-scheme')` and applies automatically.
- **High contrast mode** — when `prefers-contrast: more` is detected and `enableHighContrast` is true, high-contrast token overrides are applied.
- **CSS class** — a `data-theme` attribute is set on `<html>` (`"light"`, `"dark"`, or `"high-contrast"`).

## Usage

```tsx
import { ThemeProvider, useTheme } from '@/design-system';

function App() {
  return (
    <ThemeProvider defaultTheme="system">
      <MainContent />
    </ThemeProvider>
  );
}

function ThemeToggle() {
  const { theme, toggleTheme, resolvedTheme } = useTheme();
  return (
    <button onClick={toggleTheme}>
      Current: {resolvedTheme}
    </button>
  );
}
```
