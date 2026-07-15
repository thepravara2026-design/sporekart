export type PermissionAction =
  | 'view' | 'create' | 'update' | 'delete'
  | 'export' | 'import'
  | 'approve' | 'publish' | 'archive'
  | 'bulk_actions';

export type PermissionResource = string;

export interface PermissionCheck {
  action: PermissionAction;
  resource: PermissionResource;
}

export interface PermissionConfig {
  role: string;
  grants: Partial<Record<PermissionResource, PermissionAction[]>>;
}
