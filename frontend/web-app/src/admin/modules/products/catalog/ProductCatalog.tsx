import React, { memo, useCallback, useState } from 'react';
import { Button } from '../../../../design-system/components/core/Button';
import { Icon } from '../../../../design-system/icons/Icon';
import { PermissionProvider } from '../../../permissions/PermissionProvider';
import { FeatureFlagProvider } from '../../../feature-flags/FeatureFlagProvider';
import { PermissionGate } from '../../../permissions/PermissionGate';
import { ProductWorkspace } from '../workspace/ProductWorkspace';
import { useCatalogState } from './useCatalogState';
import { CatalogToolbar } from './CatalogToolbar';
import { FilterPanel } from './FilterPanel';
import { ProductTableView } from './ProductTableView';
import { ProductCardView } from './ProductCardView';
import { ProductCompactView } from './ProductCompactView';
import { PaginationBar } from './PaginationBar';
import { ProductQuickPreview } from './ProductQuickPreview';
import { CatalogLoading } from './CatalogLoading';
import { CatalogEmptyStates } from './CatalogEmptyStates';
import { BulkActionDialogs, type BulkActionKind } from './bulkActions';
import type { CatalogProduct } from '../mock/catalogMock';
import type { CatalogViewMode } from './types';

export interface ProductCatalogProps {
  initialView?: CatalogViewMode;
  focusSearch?: boolean;
  focusFilters?: boolean;
  pageSize?: number;
}

const CatalogInner = memo(function CatalogInner({ initialView, focusSearch, focusFilters, pageSize }: ProductCatalogProps) {
  const state = useCatalogState({ initialView, initialPageSize: pageSize });
  const [previewProduct, setPreviewProduct] = useState<CatalogProduct | null>(null);
  const [bulkAction, setBulkAction] = useState<BulkActionKind>(null);
  const [filtersOpen, setFiltersOpen] = useState<boolean>(focusFilters ?? true);

  const handlePreview = useCallback((product: CatalogProduct) => setPreviewProduct(product), []);
  const closePreview = useCallback(() => setPreviewProduct(null), []);

  const handleBulkConfirm = useCallback(() => {
    setBulkAction(null);
    state.clearSelection();
  }, [state]);

  const handleBulkCancel = useCallback(() => setBulkAction(null), []);

  const renderMain = () => {
    if (state.loading) {
      return <CatalogLoading viewMode={state.viewMode} rows={state.pageSize > 12 ? 12 : state.pageSize} />;
    }
    if (state.totalResults === 0) {
      if (state.search.trim()) {
        return <CatalogEmptyStates.NoSearchResults onClear={() => state.setSearch('')} />;
      }
      if (state.activeFilterCount > 0) {
        return <CatalogEmptyStates.NoFilterResults onClear={state.clearAllFilters} />;
      }
      return <CatalogEmptyStates.NoProducts />;
    }

    switch (state.viewMode) {
      case 'table':
        return (
          <ProductTableView
            products={state.pagedProducts}
            selectedIds={state.selectedIds}
            onToggleSelect={state.toggleSelect}
            onToggleSelectAll={state.toggleSelectAll}
            sort={state.sort}
            onSort={state.setSort}
            onPreview={handlePreview}
          />
        );
      case 'compact':
        return (
          <ProductCompactView
            products={state.pagedProducts}
            selectedIds={state.selectedIds}
            onToggleSelect={state.toggleSelect}
            onPreview={handlePreview}
          />
        );
      case 'grid':
      case 'card':
      default:
        return (
          <ProductCardView
            products={state.pagedProducts}
            selectedIds={state.selectedIds}
            onToggleSelect={state.toggleSelect}
            onPreview={handlePreview}
          />
        );
    }
  };

  return (
    <PermissionGate action="view" resource="products" fallback={<CatalogEmptyStates.PermissionDenied />}>
      <style>{`
        .sk-catalog-grid { display: grid; grid-template-columns: minmax(0, 280px) minmax(0, 1fr); gap: var(--space-section-gap); align-items: start; }
        .sk-catalog-filter-toggle { display: none; }
        @media (max-width: 1023px) {
          .sk-catalog-grid { grid-template-columns: 1fr; }
          .sk-catalog-filter-toggle { display: inline-flex; }
        }
      `}</style>

      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', gap: 'var(--space-inline-xs)', marginBottom: 'var(--space-component-gap)', flexWrap: 'wrap' }}>
        <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>
          {state.totalResults} product{state.totalResults === 1 ? '' : 's'}
        </span>
        <div style={{ display: 'flex', gap: 'var(--space-inline-xs)' }}>
          <Button
            className="sk-catalog-filter-toggle"
            variant="outline"
            size="sm"
            leftIcon={<Icon name="filter" size={14} />}
            onClick={() => setFiltersOpen((v) => !v)}
          >
            {filtersOpen ? 'Hide Filters' : 'Filters'}
          </Button>
          <PermissionGate action="create" resource="products">
            <Button variant="primary" size="sm" leftIcon={<Icon name="plus" size={14} />} disabled title="Mock Mode — creation disabled">
              New Product
            </Button>
          </PermissionGate>
        </div>
      </div>

      <CatalogToolbar
        viewMode={state.viewMode}
        setViewMode={state.setViewMode}
        search={state.search}
        setSearch={state.setSearch}
        addRecentSearch={state.addRecentSearch}
        recentSearches={state.recentSearches}
        sort={state.sort}
        setSort={state.setSort}
        setFilters={state.setFilters}
        savedViews={state.savedViews}
        saveView={state.saveView}
        loadView={state.loadView}
        deleteView={state.deleteView}
        selectedCount={state.selectedIds.size}
        clearSelection={state.clearSelection}
        onBulkAction={setBulkAction}
        focusSearch={focusSearch}
      />

      <div className="sk-catalog-grid">
        {filtersOpen && (
          <FilterPanel
            filters={state.filters}
            setFilters={state.setFilters}
            activeFilterCount={state.activeFilterCount}
            clearAllFilters={state.clearAllFilters}
          />
        )}
        <div style={{ minWidth: 0 }}>
          {renderMain()}
          <PaginationBar
            page={state.page}
            pageSize={state.pageSize}
            total={state.totalResults}
            onPageChange={state.setPage}
            onPageSizeChange={state.setPageSize}
          />
        </div>
      </div>

      <ProductQuickPreview product={previewProduct} onClose={closePreview} />
      <BulkActionDialogs
        active={bulkAction}
        count={state.selectedIds.size}
        onConfirm={handleBulkConfirm}
        onCancel={handleBulkCancel}
      />
    </PermissionGate>
  );
});

export const ProductCatalog: React.FC<ProductCatalogProps> = memo(function ProductCatalog(props) {
  return (
    <PermissionProvider initialRole="manager">
      <FeatureFlagProvider>
        <ProductWorkspace activeSection="products">
          <CatalogInner {...props} />
        </ProductWorkspace>
      </FeatureFlagProvider>
    </PermissionProvider>
  );
});

export default ProductCatalog;
