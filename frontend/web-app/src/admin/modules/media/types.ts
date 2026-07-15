export type AssetType = 'image' | 'video' | 'document' | '3d_model' | 'audio';

export type AssetStatus = 'processing' | 'published' | 'archived' | 'failed';

export interface AssetVersion {
  id: string;
  version: number;
  fileSize: number;
  url: string;
  width?: number;
  height?: number;
  createdAt: string;
  createdBy: string;
  reason: string;
}

export interface AssetDimensions {
  width: number;
  height: number;
}

export interface Asset {
  id: string;
  name: string;
  fileName: string;
  fileSize: number;
  mimeType: string;
  type: AssetType;
  extension: string;
  url: string;
  thumbnailUrl: string;
  width?: number;
  height?: number;
  duration?: number;
  alt: string;
  title: string;
  description: string;
  tags: string[];
  collectionIds: string[];
  productIds: string[];
  status: AssetStatus;
  version: number;
  versions: AssetVersion[];
  createdBy: string;
  updatedBy: string;
  createdAt: string;
  updatedAt: string;
}

export interface AssetCollection {
  id: string;
  name: string;
  description: string;
  coverAssetId?: string;
  assetCount: number;
  isSystem: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface MediaFilters {
  types: AssetType[];
  statuses: AssetStatus[];
  collections: string[];
  tags: string[];
  dateFrom: string | null;
  dateTo: string | null;
  fileSizeMin: number | null;
  fileSizeMax: number | null;
  widthMin: number | null;
  widthMax: number | null;
  heightMin: number | null;
  heightMax: number | null;
}

export const EMPTY_FILTERS: MediaFilters = {
  types: [],
  statuses: [],
  collections: [],
  tags: [],
  dateFrom: null,
  dateTo: null,
  fileSizeMin: null,
  fileSizeMax: null,
  widthMin: null,
  widthMax: null,
  heightMin: null,
  heightMax: null,
};

export type SortOption =
  | 'newest'
  | 'oldest'
  | 'name_asc'
  | 'name_desc'
  | 'size_asc'
  | 'size_desc'
  | 'type';

export type ViewMode = 'grid' | 'list';

export type MediaPermissions =
  | 'view'
  | 'upload'
  | 'edit'
  | 'delete'
  | 'publish'
  | 'archive'
  | 'replace'
  | 'organize';

export const ASSET_TYPE_ICONS: Record<AssetType, string> = {
  image: 'image',
  video: 'video',
  document: 'file-text',
  '3d_model': 'box',
  audio: 'music',
};

export const ASSET_TYPE_LABELS: Record<AssetType, string> = {
  image: 'Image',
  video: 'Video',
  document: 'Document',
  '3d_model': '3D Model',
  audio: 'Audio',
};

export const ASSET_STATUS_LABELS: Record<AssetStatus, string> = {
  processing: 'Processing',
  published: 'Published',
  archived: 'Archived',
  failed: 'Failed',
};
