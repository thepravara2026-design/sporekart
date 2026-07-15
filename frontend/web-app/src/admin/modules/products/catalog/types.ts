import type { ProductLifecycleState, ProductType } from '../types';
import type { SavedView } from '../../../components/data-grid/types';

export type CatalogViewMode = 'table' | 'grid' | 'compact' | 'card';

export type SortOption =
  | 'newest'
  | 'oldest'
  | 'name_asc'
  | 'name_desc'
  | 'price_asc'
  | 'price_desc'
  | 'updated'
  | 'status'
  | 'category'
  | 'brand';

export interface CatalogFilters {
  categories: string[];
  brands: string[];
  collections: string[];
  statuses: ProductLifecycleState[];
  types: ProductType[];
  priceMin: number | null;
  priceMax: number | null;
  createdFrom: string | null;
  createdTo: string | null;
  updatedFrom: string | null;
  updatedTo: string | null;
  hasImages: boolean | null;
  featured: boolean | null;
  draft: boolean | null;
  published: boolean | null;
  archived: boolean | null;
}

export type SavedCatalogView = SavedView & {
  filters: CatalogFilters;
  sort: SortOption;
  viewMode: CatalogViewMode;
};

export const EMPTY_FILTERS: CatalogFilters = {
  categories: [],
  brands: [],
  collections: [],
  statuses: [],
  types: [],
  priceMin: null,
  priceMax: null,
  createdFrom: null,
  createdTo: null,
  updatedFrom: null,
  updatedTo: null,
  hasImages: null,
  featured: null,
  draft: null,
  published: null,
  archived: null,
};
