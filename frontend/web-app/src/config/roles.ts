export type Role =
  | 'guest'
  | 'customer'
  | 'grower'
  | 'trainer'
  | 'distributor'
  | 'support'
  | 'administrator'
  | 'business_owner'
  | 'governance_manager';

export type WorkspaceGroup = 'discover' | 'operate' | 'intelligence' | 'platform';

export const ALL_ROLES: Role[] = [
  'guest',
  'customer',
  'grower',
  'trainer',
  'distributor',
  'support',
  'administrator',
  'business_owner',
  'governance_manager',
];

export const ROLE_LABELS: Record<Role, string> = {
  guest: 'Guest',
  customer: 'Customer',
  grower: 'Grower',
  trainer: 'Trainer',
  distributor: 'Distributor',
  support: 'Support',
  administrator: 'Administrator',
  business_owner: 'Business Owner',
  governance_manager: 'Governance Manager',
};

export interface PageDef {
  path: string;
  label: string;
  roles: Role[] | 'public';
  primaryAction?: string;
  description: string;
  workspaceId: string;
}

export interface WorkspaceDef {
  id: string;
  label: string;
  icon: string;
  group: WorkspaceGroup;
  rootPath: string;
  roles: Role[] | 'public';
  description: string;
  children: PageDef[];
}
