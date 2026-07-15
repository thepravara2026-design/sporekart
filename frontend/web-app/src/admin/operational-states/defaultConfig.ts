import type { OperationalStateConfig } from './types';

export const DEFAULT_STATE_CONFIGS: Record<string, OperationalStateConfig> = {
  loading: { type: 'loading', title: 'Loading...', description: 'Please wait while we load your content.' },
  empty: { type: 'empty', title: 'No data yet', description: 'There is nothing to display yet.' },
  no_data: { type: 'no_data', title: 'No results found', description: 'Try adjusting your filters or search query.' },
  permission_denied: { type: 'permission_denied', title: 'Permission Denied', description: 'You do not have permission to perform this action.' },
  unauthorized: { type: 'unauthorized', title: 'Unauthorized', description: 'Please sign in to access this page.' },
  forbidden: { type: 'forbidden', title: 'Access Forbidden', description: 'You do not have access to this resource.' },
  offline: { type: 'offline', title: 'You are offline', description: 'Check your internet connection and try again.' },
  maintenance: { type: 'maintenance', title: 'Under Maintenance', description: 'This section is temporarily unavailable for scheduled maintenance.' },
  system_updating: { type: 'system_updating', title: 'System Updating', description: 'We are rolling out an update. Please wait a moment.' },
  feature_disabled: { type: 'feature_disabled', title: 'Feature Disabled', description: 'This feature is currently disabled.' },
  server_unavailable: { type: 'server_unavailable', title: 'Server Unavailable', description: 'Our servers are experiencing issues. Please try again later.' },
  unexpected_error: { type: 'unexpected_error', title: 'Something went wrong', description: 'An unexpected error occurred. Please try again.' },
};
