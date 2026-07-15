import { useMemo } from 'react';
import { MOCK_CATEGORIES } from '../mock/mockCategories';
import { MOCK_COLLECTIONS } from '../mock/mockCollections';
import { MOCK_BRANDS } from '../mock/mockBrands';

export interface CategoryAnalytics {
  totalCategories: number;
  activeCategories: number;
  inactiveCategories: number;
  archivedCategories: number;
  featuredCategories: number;
  rootCategories: number;
  maxDepth: number;
  unusedCategories: number;
  categoriesWithProducts: number;
  recentlyUpdated: string[];
  recentlyCreated: string[];
  productsPerCategory: { name: string; count: number }[];
  largestCategories: { name: string; count: number }[];
  hierarchyDepth: number[];
  healthScore: number;
}

export interface BrandAnalytics {
  totalBrands: number;
  activeBrands: number;
  inactiveBrands: number;
  archivedBrands: number;
  featuredBrands: number;
  productsPerBrand: { name: string; count: number }[];
  topBrands: { name: string; count: number }[];
  recentlyAdded: string[];
  brandHealth: number;
}

export interface CollectionAnalytics {
  totalCollections: number;
  activeCollections: number;
  typeDistribution: { type: string; count: number }[];
  featuredCollections: number;
  seasonalCollections: number;
}

export function useCategoryAnalytics(): CategoryAnalytics {
  return useMemo(() => {
    const now = new Date();
    const thirtyDaysAgo = new Date(now.getTime() - 30 * 86400000).toISOString();
    const categories = MOCK_CATEGORIES;
    const active = categories.filter((c) => c.status === 'active');
    const root = categories.filter((c) => !c.parentId);
    const unused = categories.filter((c) => c.productCount === 0);
    const withProducts = categories.filter((c) => c.productCount > 0);
    const updated = categories.filter((c) => c.updatedAt >= thirtyDaysAgo).sort((a, b) => new Date(b.updatedAt).getTime() - new Date(a.updatedAt).getTime()).slice(0, 5).map((c) => c.name);
    const created = categories.filter((c) => c.createdAt >= thirtyDaysAgo).sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()).slice(0, 5).map((c) => c.name);
    const prodPerCat = categories.filter((c) => c.productCount > 0).sort((a, b) => b.productCount - a.productCount).slice(0, 10).map((c) => ({ name: c.name, count: c.productCount }));
    const largest = [...prodPerCat].slice(0, 5);
    const depths = root.map((r) => {
      let depth = 1;
      let current = r;
      while (categories.find((c) => c.parentId === current.id)) {
        depth++;
        current = categories.find((c) => c.parentId === current.id)!;
      }
      return depth;
    });
    const healthScore = Math.round((active.length / Math.max(categories.length, 1)) * 60 + (withProducts.length / Math.max(categories.length, 1)) * 40);

    return {
      totalCategories: categories.length,
      activeCategories: active.length,
      inactiveCategories: categories.filter((c) => c.status === 'inactive').length,
      archivedCategories: categories.filter((c) => c.status === 'archived').length,
      featuredCategories: categories.filter((c) => c.isFeatured).length,
      rootCategories: root.length,
      maxDepth: Math.max(...depths, 0),
      unusedCategories: unused.length,
      categoriesWithProducts: withProducts.length,
      recentlyUpdated: updated,
      recentlyCreated: created,
      productsPerCategory: prodPerCat,
      largestCategories: largest,
      hierarchyDepth: depths,
      healthScore,
    };
  }, []);
}

export function useBrandAnalytics(): BrandAnalytics {
  return useMemo(() => {
    const brands = MOCK_BRANDS;
    const active = brands.filter((b) => b.status === 'active');
    const prod = brands.filter((b) => b.productCount > 0).sort((a, b) => b.productCount - a.productCount).slice(0, 10).map((b) => ({ name: b.name, count: b.productCount }));
    const top = [...prod].slice(0, 5);
    const recent = brands.sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()).slice(0, 5).map((b) => b.name);
    const health = Math.round((active.length / Math.max(brands.length, 1)) * 100);

    return {
      totalBrands: brands.length,
      activeBrands: active.length,
      inactiveBrands: brands.filter((b) => b.status === 'inactive').length,
      archivedBrands: brands.filter((b) => b.status === 'archived').length,
      featuredBrands: brands.filter((b) => b.isFeatured).length,
      productsPerBrand: prod,
      topBrands: top,
      recentlyAdded: recent,
      brandHealth: health,
    };
  }, []);
}

export function useCollectionAnalytics(): CollectionAnalytics {
  return useMemo(() => {
    const collections = MOCK_COLLECTIONS;
    const typeMap = new Map<string, number>();
    collections.forEach((c) => typeMap.set(c.collectionType, (typeMap.get(c.collectionType) ?? 0) + 1));
    return {
      totalCollections: collections.length,
      activeCollections: collections.filter((c) => c.status === 'active').length,
      typeDistribution: Array.from(typeMap.entries()).map(([type, count]) => ({ type, count })),
      featuredCollections: collections.filter((c) => c.isFeatured).length,
      seasonalCollections: collections.filter((c) => c.collectionType === 'seasonal').length,
    };
  }, []);
}
