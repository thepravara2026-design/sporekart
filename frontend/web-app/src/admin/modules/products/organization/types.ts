export type OrgSectionId =
  | 'overview'
  | 'categories'
  | 'collections'
  | 'brands'
  | 'tags'
  | 'hierarchy'
  | 'assignments'
  | 'bulk'
  | 'analytics'
  | 'settings'
  | 'help';

export type OrgPermissions =
  | 'view'
  | 'create'
  | 'edit'
  | 'delete'
  | 'assign'
  | 'archive'
  | 'restore'
  | 'bulk';

export type OrgRole = 'viewer' | 'editor' | 'manager' | 'administrator';

export interface OrgCategory {
  id: string;
  code: string;
  name: string;
  slug: string;
  description: string;
  icon: string;
  imageUrl?: string;
  parentId: string | null;
  ancestors: string[];
  displayOrder: number;
  isVisible: boolean;
  isFeatured: boolean;
  status: 'active' | 'inactive' | 'archived';
  productCount: number;
  createdAt: string;
  updatedAt: string;
  createdBy: string;
  updatedBy: string;
}

export interface OrgCollection {
  id: string;
  name: string;
  slug: string;
  description: string;
  coverImage?: string;
  collectionType: 'seasonal' | 'featured' | 'trending' | 'recommended' | 'campaign' | 'product' | 'administrator';
  status: 'active' | 'inactive' | 'archived';
  isFeatured: boolean;
  displayOrder: number;
  productCount: number;
  startDate?: string;
  endDate?: string;
  createdAt: string;
  updatedAt: string;
  createdBy: string;
}

export interface OrgBrand {
  id: string;
  name: string;
  slug: string;
  logoUrl?: string;
  description: string;
  website?: string;
  country: string;
  manufacturer: string;
  status: 'active' | 'inactive' | 'archived';
  isFeatured: boolean;
  priority: number;
  productCount: number;
  notes: string;
  createdAt: string;
  updatedAt: string;
  createdBy: string;
}

export interface OrgTag {
  id: string;
  name: string;
  slug: string;
  tagType: 'keyword' | 'search' | 'seo' | 'marketing' | 'campaign' | 'internal' | 'product' | 'color';
  color?: string;
  description: string;
  usageCount: number;
  isSystem: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface OrgActivityEvent {
  id: string;
  type: 'created' | 'edited' | 'moved' | 'merged' | 'assigned' | 'removed' | 'archived' | 'restored' | 'viewed';
  entityType: 'category' | 'collection' | 'brand' | 'tag';
  entityId: string;
  entityName: string;
  message: string;
  actor: string;
  timestamp: string;
}

export interface CategoryNode extends OrgCategory {
  children: CategoryNode[];
  depth: number;
  isExpanded: boolean;
}

export interface OrgAssignment {
  productId: string;
  productName: string;
  productSku: string;
  categories: string[];
  collections: string[];
  brands: string[];
  tags: string[];
}

export interface OrgFilters {
  search: string;
  status: ('active' | 'inactive' | 'archived')[];
  isFeatured: boolean | null;
  types: string[];
  tags: string[];
  brands: string[];
  dateFrom: string | null;
  dateTo: string | null;
}

export const EMPTY_ORG_FILTERS: OrgFilters = {
  search: '',
  status: [],
  isFeatured: null,
  types: [],
  tags: [],
  brands: [],
  dateFrom: null,
  dateTo: null,
};

export type OrgSortOption =
  | 'name_asc' | 'name_desc'
  | 'updated' | 'created'
  | 'product_count'
  | 'display_order';

export const ORG_SECTION_ICONS: Record<OrgSectionId, string> = {
  overview: 'grid',
  categories: 'tag',
  collections: 'bookmark',
  brands: 'shield',
  tags: 'pricetag',
  hierarchy: 'layers',
  assignments: 'link',
  bulk: 'zap',
  analytics: 'bar-chart',
  settings: 'settings',
  help: 'help-circle',
};

export const ORG_SECTION_LABELS: Record<OrgSectionId, string> = {
  overview: 'Overview',
  categories: 'Categories',
  collections: 'Collections',
  brands: 'Brands',
  tags: 'Tags',
  hierarchy: 'Hierarchy',
  assignments: 'Assignments',
  bulk: 'Bulk Operations',
  analytics: 'Analytics',
  settings: 'Settings',
  help: 'Help',
};

export const COLLECTION_TYPE_LABELS: Record<string, string> = {
  seasonal: 'Seasonal',
  featured: 'Featured',
  trending: 'Trending',
  recommended: 'Recommended',
  campaign: 'Campaign',
  product: 'Product',
  administrator: 'Administrator',
};
