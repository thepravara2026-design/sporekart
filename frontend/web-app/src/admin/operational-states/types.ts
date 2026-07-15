export type OperationalStateType =
  | 'loading'
  | 'empty'
  | 'no_data'
  | 'permission_denied'
  | 'unauthorized'
  | 'forbidden'
  | 'offline'
  | 'maintenance'
  | 'system_updating'
  | 'feature_disabled'
  | 'server_unavailable'
  | 'unexpected_error';

export interface OperationalStateConfig {
  type: OperationalStateType;
  title?: string;
  description?: string;
  icon?: string;
  action?: {
    label: string;
    onClick: () => void;
  };
  secondaryAction?: {
    label: string;
    onClick: () => void;
  };
}
