import React from 'react';
import { StatusBadge } from '../../../components/status';
import { LifecycleBadge } from '../components/LifecycleBadge';
import { ProductCard } from '../components/ProductCard';
import {
  STOCK_LABELS,
  TYPE_LABELS,
  brandNameById,
  categoryNameById,
  type CatalogProduct,
  type StockStatus,
} from '../mock/catalogMock';
import type { SortOption } from './types';

export interface TableColumnDef {
  key: string;
  header: string;
  sortable?: boolean;
  width?: number;
  align?: 'left' | 'center' | 'right';
  sortAsc?: SortOption;
  sortDesc?: SortOption;
  defaultVisible?: boolean;
  render?: (p: CatalogProduct) => React.ReactNode;
}

export function formatPrice(value: number): string {
  return `\u20B9${value.toLocaleString('en-IN')}`;
}

export function formatDate(iso: string): string {
  try {
    return new Date(iso).toLocaleDateString('en-IN', { day: '2-digit', month: 'short', year: 'numeric' });
  } catch {
    return iso;
  }
}

const stockVariant: Record<StockStatus, 'success' | 'warning' | 'danger' | 'info'> = {
  in_stock: 'success',
  low_stock: 'warning',
  out_of_stock: 'danger',
  preorder: 'info',
};

export const productTableColumns: TableColumnDef[] = [
  { key: 'thumb', header: '', width: 56, align: 'center', defaultVisible: true },
  {
    key: 'name',
    header: 'Name',
    sortable: true,
    width: 240,
    sortAsc: 'name_asc',
    sortDesc: 'name_desc',
    defaultVisible: true,
    render: (p) => (
      <div style={{ display: 'flex', flexDirection: 'column', minWidth: 0 }}>
        <span
          style={{
            fontSize: 'var(--text-body-sm)',
            color: 'var(--color-text-primary)',
            fontWeight: 'var(--weight-semibold)',
            whiteSpace: 'nowrap',
            overflow: 'hidden',
            textOverflow: 'ellipsis',
          }}
        >
          {p.name}
        </span>
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{p.sku}</span>
      </div>
    ),
  },
  { key: 'sku', header: 'SKU', sortable: false, width: 140, defaultVisible: true, render: (p) => p.sku },
  {
    key: 'category',
    header: 'Category',
    sortable: true,
    width: 150,
    sortAsc: 'category',
    sortDesc: 'category',
    defaultVisible: true,
    render: (p) => <StatusBadge status={categoryNameById[p.categoryId] ?? '—'} variant="neutral" />,
  },
  {
    key: 'brand',
    header: 'Brand',
    sortable: true,
    width: 140,
    sortAsc: 'brand',
    sortDesc: 'brand',
    defaultVisible: true,
    render: (p) => brandNameById[p.brandId] ?? '—',
  },
  {
    key: 'type',
    header: 'Type',
    width: 140,
    defaultVisible: true,
    render: (p) => TYPE_LABELS[p.productType] ?? p.productType,
  },
  {
    key: 'stock',
    header: 'Stock',
    width: 130,
    defaultVisible: true,
    render: (p) => <StatusBadge status={STOCK_LABELS[p.stockStatus]} variant={stockVariant[p.stockStatus]} />,
  },
  {
    key: 'lifecycle',
    header: 'Lifecycle',
    sortable: true,
    width: 130,
    sortAsc: 'status',
    sortDesc: 'status',
    defaultVisible: true,
    render: (p) => <LifecycleBadge state={p.lifecycleState} />,
  },
  {
    key: 'price',
    header: 'Price',
    sortable: true,
    width: 120,
    align: 'right',
    sortAsc: 'price_asc',
    sortDesc: 'price_desc',
    defaultVisible: true,
    render: (p) => (
      <span style={{ fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)' }}>
        {formatPrice(p.pricing.sellingPrice)}
      </span>
    ),
  },
  {
    key: 'created',
    header: 'Created',
    sortable: true,
    width: 120,
    sortAsc: 'oldest',
    sortDesc: 'newest',
    defaultVisible: true,
    render: (p) => formatDate(p.createdAt),
  },
  {
    key: 'updated',
    header: 'Updated',
    sortable: true,
    width: 120,
    sortAsc: 'updated',
    sortDesc: 'updated',
    defaultVisible: true,
    render: (p) => formatDate(p.updatedAt),
  },
  { key: 'actions', header: '', width: 96, align: 'right', defaultVisible: true },
];

export const PRODUCT_SORT_OPTIONS: { value: SortOption; label: string }[] = [
  { value: 'newest', label: 'Newest' },
  { value: 'oldest', label: 'Oldest' },
  { value: 'name_asc', label: 'Name A-Z' },
  { value: 'name_desc', label: 'Name Z-A' },
  { value: 'price_asc', label: 'Price Low-High' },
  { value: 'price_desc', label: 'Price High-Low' },
  { value: 'updated', label: 'Updated Recently' },
  { value: 'status', label: 'Status' },
  { value: 'category', label: 'Category' },
  { value: 'brand', label: 'Brand' },
];

export function renderProductCard(p: CatalogProduct, onClick?: (p: CatalogProduct) => void): React.ReactNode {
  return <ProductCard product={p} onClick={onClick ? () => onClick(p) : undefined} />;
}
