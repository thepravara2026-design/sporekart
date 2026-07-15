import type { FeatureFlagConfig } from './types';

export const DEFAULT_FEATURE_FLAGS: FeatureFlagConfig = {
  'advanced-analytics': {
    key: 'advanced-analytics',
    label: 'Advanced Analytics',
    description: 'AI-powered analytics and predictive insights',
    state: 'experimental',
  },
  'bulk-import': {
    key: 'bulk-import',
    label: 'Bulk Import',
    description: 'Import products and orders via CSV/Excel',
    state: 'enabled',
  },
  'bulk-export': {
    key: 'bulk-export',
    label: 'Bulk Export',
    description: 'Export data in multiple formats',
    state: 'enabled',
  },
  'dark-mode': {
    key: 'dark-mode',
    label: 'Dark Mode',
    description: 'Toggle dark theme across the admin panel',
    state: 'beta',
  },
  'inventory-forecast': {
    key: 'inventory-forecast',
    label: 'Inventory Forecast',
    description: 'Predict inventory needs using ML models',
    state: 'coming_soon',
  },
  'voice-commands': {
    key: 'voice-commands',
    label: 'Voice Commands',
    description: 'Navigate and perform actions using voice',
    state: 'hidden',
  },
  'widget-customizer': {
    key: 'widget-customizer',
    label: 'Widget Customizer',
    description: 'Drag-and-drop dashboard widget layout',
    state: 'beta',
  },
  'real-time-collaboration': {
    key: 'real-time-collaboration',
    label: 'Real-time Collaboration',
    description: 'Multi-user editing and commenting',
    state: 'coming_soon',
  },
  'print-reports': {
    key: 'print-reports',
    label: 'Print Reports',
    description: 'Generate printer-friendly report versions',
    state: 'enabled',
  },
  'audit-dashboard': {
    key: 'audit-dashboard',
    label: 'Audit Dashboard',
    description: 'Dedicated audit log viewer and analyzer',
    state: 'experimental',
  },
};
