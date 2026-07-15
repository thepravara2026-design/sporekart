export type OnlineStatus = 'online' | 'offline' | 'slow_connection';

export interface OfflineAction {
  id: string;
  label: string;
  description?: string;
  icon?: string;
  onClick: () => void;
  available: boolean;
}
