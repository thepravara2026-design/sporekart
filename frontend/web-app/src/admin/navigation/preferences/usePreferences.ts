import { useState, useCallback, useEffect } from 'react';
import type { UserPreferences, SidebarMode } from '../types';

const STORAGE_KEY = 'user_preferences';

const DEFAULTS: UserPreferences = {
  sidebarMode: 'expanded',
  sidebarWidth: 280,
  tableDensity: 'comfortable',
  theme: 'system',
  dashboardLayout: 'default',
};

function loadPrefs(): UserPreferences {
  try { return { ...DEFAULTS, ...JSON.parse(localStorage.getItem(STORAGE_KEY) ?? '{}') }; } catch { return DEFAULTS; }
}

function savePrefs(prefs: UserPreferences) {
  try { localStorage.setItem(STORAGE_KEY, JSON.stringify(prefs)); } catch {}
}

export function usePreferences() {
  const [preferences, setPreferences] = useState<UserPreferences>(loadPrefs);

  useEffect(() => { savePrefs(preferences); }, [preferences]);

  const updatePreference = useCallback(<K extends keyof UserPreferences>(key: K, value: UserPreferences[K]) => {
    setPreferences((prev) => ({ ...prev, [key]: value }));
  }, []);

  const setSidebarMode = useCallback((mode: SidebarMode) => updatePreference('sidebarMode', mode), [updatePreference]);
  const setSidebarWidth = useCallback((width: number) => updatePreference('sidebarWidth', width), [updatePreference]);
  const setTableDensity = useCallback((density: UserPreferences['tableDensity']) => updatePreference('tableDensity', density), [updatePreference]);
  const setTheme = useCallback((theme: UserPreferences['theme']) => updatePreference('theme', theme), [updatePreference]);
  const setDashboardLayout = useCallback((layout: UserPreferences['dashboardLayout']) => updatePreference('dashboardLayout', layout), [updatePreference]);

  const resetPreferences = useCallback(() => {
    setPreferences(DEFAULTS);
  }, []);

  return {
    preferences,
    setSidebarMode,
    setSidebarWidth,
    setTableDensity,
    setTheme,
    setDashboardLayout,
    resetPreferences,
  };
}
