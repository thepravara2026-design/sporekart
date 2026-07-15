import React, { createContext, useContext, useMemo, useState } from 'react';
import type { Product, ProductLifecycleState } from '../types';

export type ProductView = 'grid' | 'list' | 'table';

export interface ProductState {
  currentProduct: Product | null;
  currentWorkspace: string;
  filters: Record<string, string>;
  currentView: ProductView;
  selectedProducts: Set<string>;
  lifecycleFilter: ProductLifecycleState | 'all';
  loading: boolean;
  empty: boolean;
  error: string | null;
  search: string;
  pagination: { page: number; pageSize: number };
}

export interface ProductStateContextValue extends ProductState {
  setCurrentProduct: (product: Product | null) => void;
  setCurrentWorkspace: (workspace: string) => void;
  setFilters: (filters: Record<string, string>) => void;
  setCurrentView: (view: ProductView) => void;
  setSelectedProducts: (selected: Set<string>) => void;
  setLifecycleFilter: (state: ProductLifecycleState | 'all') => void;
  setLoading: (loading: boolean) => void;
  setEmpty: (empty: boolean) => void;
  setError: (error: string | null) => void;
  setSearch: (search: string) => void;
  setPagination: (pagination: { page: number; pageSize: number }) => void;
}

const initialState: ProductState = {
  currentProduct: null,
  currentWorkspace: 'overview',
  filters: {},
  currentView: 'grid',
  selectedProducts: new Set<string>(),
  lifecycleFilter: 'all',
  loading: false,
  empty: false,
  error: null,
  search: '',
  pagination: { page: 1, pageSize: 10 },
};

const ProductStateContext = createContext<ProductStateContextValue | null>(null);

export const ProductProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [currentProduct, setCurrentProduct] = useState<Product | null>(initialState.currentProduct);
  const [currentWorkspace, setCurrentWorkspace] = useState<string>(initialState.currentWorkspace);
  const [filters, setFilters] = useState<Record<string, string>>(initialState.filters);
  const [currentView, setCurrentView] = useState<ProductView>(initialState.currentView);
  const [selectedProducts, setSelectedProducts] = useState<Set<string>>(initialState.selectedProducts);
  const [lifecycleFilter, setLifecycleFilter] = useState<ProductLifecycleState | 'all'>(initialState.lifecycleFilter);
  const [loading, setLoading] = useState<boolean>(initialState.loading);
  const [empty, setEmpty] = useState<boolean>(initialState.empty);
  const [error, setError] = useState<string | null>(initialState.error);
  const [search, setSearch] = useState<string>(initialState.search);
  const [pagination, setPagination] = useState<{ page: number; pageSize: number }>(initialState.pagination);

  const value = useMemo<ProductStateContextValue>(
    () => ({
      currentProduct,
      currentWorkspace,
      filters,
      currentView,
      selectedProducts,
      lifecycleFilter,
      loading,
      empty,
      error,
      search,
      pagination,
      setCurrentProduct,
      setCurrentWorkspace,
      setFilters,
      setCurrentView,
      setSelectedProducts,
      setLifecycleFilter,
      setLoading,
      setEmpty,
      setError,
      setSearch,
      setPagination,
    }),
    [
      currentProduct,
      currentWorkspace,
      filters,
      currentView,
      selectedProducts,
      lifecycleFilter,
      loading,
      empty,
      error,
      search,
      pagination,
    ],
  );

  return <ProductStateContext.Provider value={value}>{children}</ProductStateContext.Provider>;
};

export function useProductState(): ProductStateContextValue {
  const ctx = useContext(ProductStateContext);
  if (!ctx) {
    throw new Error('useProductState must be used within a ProductProvider');
  }
  return ctx;
}
